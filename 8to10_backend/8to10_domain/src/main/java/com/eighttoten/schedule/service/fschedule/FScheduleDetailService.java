package com.eighttoten.schedule.service.fschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_F_DETAIL;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ScheduleDay;
import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleCreateInfo;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleRepository fScheduleRepository;
    private final FScheduleDetailRepository fScheduleDetailRepository;

    @Transactional
    public void saveAllDetails(Long fScheduleId, FScheduleCreateInfo fScheduleCreateInfo) {
        FSchedule fSchedule = fScheduleRepository.findById(fScheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_SCHEDULE));

        LocalDateTime currentDateTime = fSchedule.getStartDateTime();
        LocalDateTime endDateTime = fSchedule.getEndDateTime();
        List<String> days = fScheduleCreateInfo.getDays();
        LocalTime duration = fScheduleCreateInfo.getDuration();
        LocalTime startTime = fScheduleCreateInfo.getStartTime();

        while (!currentDateTime.isAfter(endDateTime)) {
            DayOfWeek currentDayOfWeek = currentDateTime.getDayOfWeek();
            for (String day : days) {
                if (currentDayOfWeek.equals(ScheduleDay.of(day))) {
                    LocalDateTime start = LocalDateTime.of(currentDateTime.toLocalDate(), startTime);
                    LocalDateTime end = start.plusHours(duration.getHour())
                            .plusMinutes(duration.getMinute());

                    fScheduleDetailRepository.save(new NewFDetail(fScheduleId, fSchedule.getCommonDescription(), start, end));
                }
            }
            currentDateTime = currentDateTime.plusDays(1);
        }
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = fScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_DETAIL));

        member.checkIsSameEmail(fScheduleDetail.getCreatedBy());
        fScheduleDetailRepository.deleteById(id);
    }

    @Transactional
    public void update(Member member, FDetailUpdate fDetailUpdate){
        FScheduleDetail fScheduleDetail = fScheduleDetailRepository.findById(fDetailUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_DETAIL));

        member.checkIsSameEmail(fScheduleDetail.getCreatedBy());
        fScheduleDetail.update(fDetailUpdate);
        fScheduleDetailRepository.update(fScheduleDetail);
    }

    @Transactional
    public void deleteByMemberAndParentIdGEStartDate(Member member, Long parentId, LocalDateTime start) {
        List<FScheduleDetail> fScheduleDetails = fScheduleDetailRepository
                .findAllByMemberEmailAndParentIdGEStart(member.getEmail(), parentId, start);

        fScheduleDetailRepository.deleteAllByIds(fScheduleDetails.stream()
                .map(FScheduleDetail::getId)
                .toList());
    }
}