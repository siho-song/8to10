package com.eighttoten.schedule.service.nschedule;

import static com.eighttoten.exception.ExceptionCode.INVALID_N_SCHEDULE_CREATION;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.NOT_WITHIN_WORK_TIME;

import com.eighttoten.common.AppConstant;
import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.BusinessException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleCreateInfo;
import com.eighttoten.schedule.domain.nschedule.NScheduleUpdate;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import com.eighttoten.schedule.domain.nschedule.TimeSlot;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.service.ScheduleAbleService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NScheduleService {
    private final NScheduleRepository nScheduleRepository;
    private final ScheduleAbleService scheduleAbleService;
    private final NScheduleDetailService nScheduleDetailService;
    private final TimeSlotService timeSlotService;

    @Transactional
    public void saveWithDetails(Member member, NScheduleCreateInfo nScheduleCreateInfo, NewNSchedule newNSchedule) {
        int performInWeek = nScheduleCreateInfo.getPerformInWeek();
        LocalDateTime startDateTime = newNSchedule.getStartDateTime();
        LocalDateTime endDateTime = newNSchedule.getEndDateTime();

        List<ScheduleAble> scheduleAbles = scheduleAbleService
                .findAllByMemberEmailInPeriod(member.getEmail(), startDateTime, endDateTime);

        Map<LocalDate, List<TimeSlot>> slotMap = timeSlotService.findAllBetweenStartAndEnd(
                getScheduleMapWithinWorkTime(scheduleAbles), startDateTime, endDateTime);

        checkAllSlotsWithinWorkTime(slotMap);

        Map<DayOfWeek, List<TimeSlot>> availableSlotMap = timeSlotService
                .findCommonSlotsForEachDay(slotMap, nScheduleCreateInfo.getNecessaryTime());

        List<DayOfWeek> selectedDays = selectRandomDays(
                nScheduleCreateInfo.getDays(),
                availableSlotMap.keySet(),
                performInWeek
        );

        validateCreateNSchedule(selectedDays, performInWeek);

        Map<DayOfWeek, TimeSlot> selectedTimeSlots = timeSlotService
                .selectEarlierTimeSlot(selectedDays, availableSlotMap);

        long nScheduleId = nScheduleRepository.save(newNSchedule);
        nScheduleDetailService.saveDetails(nScheduleId, selectedTimeSlots, nScheduleCreateInfo);
    }

    @Transactional
    public void update(Member member, NScheduleUpdate nScheduleUpdate){
        NSchedule nSchedule = nScheduleRepository.findById(nScheduleUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        member.checkIsSameEmail(nSchedule.getCreatedBy());
        nSchedule.update(nScheduleUpdate);
        nScheduleRepository.update(nSchedule);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NSchedule nSchedule = nScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        member.checkIsSameEmail(nSchedule.getCreatedBy());
        nScheduleRepository.deleteById(id);
    }

    private Map<LocalDate, List<ScheduleAble>> getScheduleMapWithinWorkTime(List<ScheduleAble> scheduleAbles) {
        LocalTime startTime = AppConstant.WORK_START_TIME;
        LocalTime endTime = AppConstant.WORK_END_TIME;

        return scheduleAbles.stream()
                .filter(scheduleAble -> isWithinWorkTimeRange(scheduleAble, startTime, endTime))
                .collect(Collectors.groupingBy(
                        scheduleAble -> scheduleAble.getScheduleStart().toLocalDate(),
                        Collectors.toList()));
    }

    private boolean isWithinWorkTimeRange(ScheduleAble scheduleAble, LocalTime startTime, LocalTime endTime) {
        LocalTime scheduleEnd = scheduleAble.getScheduleEnd().toLocalTime();
        return !scheduleEnd.isBefore(startTime) && !scheduleEnd.isAfter(endTime);
    }

    private List<DayOfWeek> selectRandomDays(
            List<DayOfWeek> candidateDays,
            Set<DayOfWeek> availableDays,
            int numberOfDays) {

        List<DayOfWeek> selectableDays = candidateDays.stream()
                .filter(availableDays::contains)
                .collect(Collectors.toList());
        Collections.shuffle(selectableDays, ThreadLocalRandom.current());
        return selectableDays.subList(0, Math.min(numberOfDays, selectableDays.size()));
    }

    private void validateCreateNSchedule(List<DayOfWeek> selectedDays, int performInWeek) {
        if(selectedDays.size() < performInWeek){
            throw new BadRequestException(INVALID_N_SCHEDULE_CREATION);
        }
    }

    private void checkAllSlotsWithinWorkTime(Map<LocalDate, List<TimeSlot>> slots) {
        if (!slots.values().stream().flatMap(Collection::stream).allMatch(TimeSlot::isWithinWorkTime)) {
            throw new BusinessException(NOT_WITHIN_WORK_TIME);
        }
    }
}