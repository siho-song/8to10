package com.eighttoten.schedule.service.nschedule;

import static com.eighttoten.exception.ExceptionCode.INVALID_ACHIEVEMENT_AMOUNT;
import static com.eighttoten.exception.ExceptionCode.NOT_EQUAL_DATE;
import static com.eighttoten.exception.ExceptionCode.NOT_EXIST_N_DETAIL;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_DETAIL;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_SCHEDULE;

import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ProgressUpdates;
import com.eighttoten.schedule.domain.ProgressUpdates.ProgressUpdate;
import com.eighttoten.schedule.domain.nschedule.NDetailUpdate;
import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleCreateInfo;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import com.eighttoten.schedule.domain.nschedule.TimeSlot;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.event.AchievementUpdateEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NScheduleDetailService {
    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final NScheduleRepository nScheduleRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void saveDetails(
            Long nScheduleId,
            Map<DayOfWeek, TimeSlot> selectedTimeSlots,
            NScheduleCreateInfo nScheduleCreateInfo) {

        NSchedule nSchedule = nScheduleRepository.findById(nScheduleId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        LocalDateTime current = nSchedule.getStartDateTime();
        LocalDateTime end = nSchedule.getEndDateTime();
        LocalTime bufferTime = nScheduleCreateInfo.getBufferTime();
        LocalTime performInDay = nScheduleCreateInfo.getPerformInDay();

        List<NewNDetail> newNDetails = new ArrayList<>();
        while(!current.isAfter(end)){
            LocalDateTime startDate = current;
            DayOfWeek day = current.getDayOfWeek();
            TimeSlot timeSlot = selectedTimeSlots.get(day);
            if(timeSlot != null){
                LocalDateTime startDateTime = startDate.plusHours(
                                timeSlot.getStartTime().getHour() + bufferTime.getHour())
                        .plusMinutes(timeSlot.getStartTime().getMinute() + bufferTime.getMinute());

                LocalDateTime endDateTime = startDateTime.plusHours(performInDay.getHour())
                        .plusMinutes(performInDay.getMinute());

                newNDetails.add(new NewNDetail(nSchedule.getId(), startDateTime, endDateTime, bufferTime,
                        nSchedule.getCommonDescription(), false, 0, 0));
            }
            current = current.plusDays(1L);
        }
        setScheduleDailyAmount(newNDetails, nSchedule.getTotalAmount());
        nScheduleDetailRepository.saveAll(nScheduleId,newNDetails);
    }

    @Transactional
    public void update(Member member, NDetailUpdate nDetailUpdate){
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        member.checkIsSameEmail(nScheduleDetail.getCreatedBy());
        nScheduleDetail.update(nDetailUpdate.getDetailDescription());
        nScheduleDetailRepository.update(nScheduleDetail);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NDetailWithParent nScheduleDetail = nScheduleDetailRepository.findByIdWithParent(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));;

        member.checkIsSameEmail(nScheduleDetail.getCreatedBy());
        NSchedule parent = nScheduleDetail.getNSchedule();
        parent.updateTotalAmount(true, nScheduleDetail.getDailyAmount());
        nScheduleRepository.update(parent);
        nScheduleDetailRepository.deleteById(nScheduleDetail.getId());
    }

    @Transactional
    public void deleteAllByStartDateGEAndMemberAndParentId(
            LocalDateTime startDate,
            Member member,
            Long parentId)
    {
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByMemberEmailAndParentIdGEStart(
                member.getEmail(),
                parentId,
                startDate);
        NSchedule parent = nScheduleRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        nScheduleDetailRepository.deleteAllByIds(nScheduleDetails.stream().map(NScheduleDetail::getId).toList());

        parent.updateTotalAmount(true, getDailyAmountSum(nScheduleDetails));
        nScheduleRepository.update(parent);
    }

    @Transactional
    public void updateProgressList(Member member, ProgressUpdates progressUpdates) {
        List<ProgressUpdate> allProgress = progressUpdates.getProgressUpdates();
        List<Long> ids = progressUpdates.getProgressUpdates().stream()
                .map(ProgressUpdate::getScheduleDetailId).toList();

        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByIds(ids);
        LocalDate updateDate = progressUpdates.getDate();

        if(!isAllSameDate(nScheduleDetails, updateDate)){
            throw new BadRequestException(NOT_EQUAL_DATE);
        }

        allProgress.forEach(progressUpdate -> updateProgress(progressUpdate, nScheduleDetails));
        publisher.publishEvent(new AchievementUpdateEvent(member, updateDate));
    }

    private void setScheduleDailyAmount(
            List<NewNDetail> newNDetails,
            int totalAmount) {

        int size = newNDetails.size();
        int div = totalAmount / size;
        int mod = totalAmount % size;

        for (int i = 0; i < size; i++) {
            if (i < mod){
                newNDetails.get(i).setDailyAmount(div + 1);
            } else {
                newNDetails.get(i).setDailyAmount(div);
            }
        }
    }

    private boolean isValidAchievementAmount(int newAchievementAmount, int dailyAmount) {
        return newAchievementAmount >= 0 && dailyAmount >= 0 && newAchievementAmount <= dailyAmount;
    }

    private double getDailyAmountSum(List<NScheduleDetail> nScheduleDetails) {
        return nScheduleDetails.stream().mapToDouble(NScheduleDetail::getDailyAmount).sum();
    }

    private void updateProgress(ProgressUpdate progressUpdate, List<NScheduleDetail> nScheduleDetails) {
        NScheduleDetail nScheduleDetail = nScheduleDetails.stream()
                .filter(nd -> nd.getId().equals(progressUpdate.getScheduleDetailId()))
                .findFirst().orElseThrow(() -> new NotFoundEntityException(NOT_EXIST_N_DETAIL));

        int dailyAmount = nScheduleDetail.getDailyAmount();
        int newAchievementAmount = progressUpdate.getAchievedAmount();

        if (!isValidAchievementAmount(newAchievementAmount, dailyAmount)) {
            throw new BadRequestException(INVALID_ACHIEVEMENT_AMOUNT);
        }

        if(dailyAmount == 0){
            nScheduleDetail.updateCompleteStatus(progressUpdate.isCompleteStatus());
        }
        else {
            nScheduleDetail.updateAchievedAmount(newAchievementAmount);
            nScheduleDetail.updateCompleteStatus(nScheduleDetail.getAchievedAmount() == dailyAmount);
        }
        nScheduleDetailRepository.update(nScheduleDetail);
    }

    private boolean isAllSameDate(List<NScheduleDetail> nScheduleDetails, LocalDate updateDate) {
        return nScheduleDetails.stream()
                .allMatch(nScheduleDetail -> nScheduleDetail.getStartDateTime().toLocalDate().equals(updateDate));
    }
}