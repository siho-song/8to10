package com.eighttoten.schedule.service.nschedule;

import com.eighttoten.common.AppConstant;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.nschedule.TimeSlot;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotService {
    public Map<LocalDate, List<TimeSlot>> findAllBetweenStartAndEnd(
            Map<LocalDate, List<ScheduleAble>> scheduleAbleMap,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        Map<LocalDate, List<TimeSlot>> slotsByDate = new HashMap<>();
        LocalDate currentDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        while (currentDate.isBefore(endDate)) {
            List<ScheduleAble> scheduleAbles = scheduleAbleMap.getOrDefault(currentDate, Collections.emptyList());
            scheduleAbles.sort(Comparator.comparing(ScheduleAble::getScheduleStart));
            List<TimeSlot> timeslots = findSlotsForDate(scheduleAbles);
            slotsByDate.put(currentDate, timeslots);
            currentDate = currentDate.plusDays(1);
        }

        return slotsByDate;
    }

    public Map<DayOfWeek, List<TimeSlot>> findCommonSlotsForEachDay(
            Map<LocalDate, List<TimeSlot>> slotsForPeriodByDate,
            Duration necessaryTime) {

        Map<DayOfWeek, List<TimeSlot>> commonRangeSlotsByDay = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if(!hasAllDateSlots(slotsForPeriodByDate, dayOfWeek)){
                continue;
            }

            List<List<TimeSlot>> allSlotsForDateEqualDay = collectSlotsByDay(slotsForPeriodByDate, dayOfWeek)
                    .values()
                    .stream()
                    .toList();
            List<TimeSlot> commonTimeSlots = findCommonTimeSlots(necessaryTime, allSlotsForDateEqualDay);

            if(!commonTimeSlots.isEmpty()) {
                commonRangeSlotsByDay.put(dayOfWeek, commonTimeSlots);
            }
        }
        return commonRangeSlotsByDay;
    }

    public Map<DayOfWeek, TimeSlot> selectEarlierTimeSlot(List<DayOfWeek> days, Map<DayOfWeek, List<TimeSlot>> availableTimeSlotsByDay) {
        return days.stream()
                .filter(availableTimeSlotsByDay::containsKey)
                .collect(Collectors.toMap(
                        day -> day,
                        day -> availableTimeSlotsByDay.get(day).get(0)
                ));
    }

    private List<TimeSlot> findSlotsForDate(List<ScheduleAble> sortedScheduleAbles) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        if (sortedScheduleAbles.isEmpty()) {
            return addTotalWorkTime();
        }

        LocalTime firstScheduleStart = sortedScheduleAbles.get(0).getScheduleStart().toLocalTime();
        if (isTimeSlotAvailable(AppConstant.WORK_START_TIME, firstScheduleStart)) {
            timeSlots.add(new TimeSlot(AppConstant.WORK_START_TIME, firstScheduleStart));
        }

        LocalTime beforeScheduleEnd = null;
        for (ScheduleAble scheduleAble : sortedScheduleAbles) {
            LocalTime nextScheduleStart = scheduleAble.getScheduleStart().toLocalTime();
            LocalTime nextScheduleEnd = scheduleAble.getScheduleEnd().toLocalTime();

            if (beforeScheduleEnd != null && isTimeSlotAvailable(beforeScheduleEnd, nextScheduleStart)) {
                timeSlots.add(new TimeSlot(beforeScheduleEnd, nextScheduleStart));
            }
            beforeScheduleEnd = nextScheduleEnd;
        }

        LocalTime lastScheduleEnd = sortedScheduleAbles.stream()
                .map(scheduleAble -> scheduleAble.getScheduleEnd().toLocalTime())
                .max(LocalTime::compareTo).get(); // 종료 시간 기준 가장 늦은 시간

        if (isTimeSlotAvailable(lastScheduleEnd, AppConstant.WORK_END_TIME)) {
            timeSlots.add(new TimeSlot(lastScheduleEnd, AppConstant.WORK_END_TIME));
        }
        return timeSlots;
    }

    private List<TimeSlot> addTotalWorkTime() {
        return List.of(new TimeSlot(AppConstant.WORK_START_TIME, AppConstant.WORK_END_TIME));
    }

    private boolean isTimeSlotAvailable(LocalTime start, LocalTime end) {
        return Duration.between(start, end).toMinutes() > 0;
    }

    private boolean hasAllDateSlots(Map<LocalDate, List<TimeSlot>> allSlotsForPeriodByDate, DayOfWeek dayOfWeek) {
        return allSlotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .noneMatch(entry -> entry.getValue().isEmpty());
    }

    private LinkedHashMap<LocalDate, List<TimeSlot>> collectSlotsByDay(Map<LocalDate, List<TimeSlot>> slotsForPeriodByDate,
                                                                       DayOfWeek dayOfWeek) {
        return slotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (existValue, newValue) -> existValue,
                        LinkedHashMap::new
                ));
    }

    private List<TimeSlot> findCommonTimeSlots(Duration necessaryTime, List<List<TimeSlot>> allSlotsForDays) {
        if (allSlotsForDays.isEmpty()) {
            return new ArrayList<>();
        }

        List<TimeSlot> finalList = new ArrayList<>(allSlotsForDays.get(0)); //최종리스트

        for (int i = 1; i < allSlotsForDays.size() ; i++) {
            List<TimeSlot> nextDaySlots = allSlotsForDays.get(i);
            List<TimeSlot> tempSlots = new ArrayList<>();
            for (TimeSlot slot1 : finalList) {
                for (TimeSlot slot2 : nextDaySlots) {
                    LocalTime start = getLaterTime(slot1.getStartTime(), slot2.getStartTime());
                    LocalTime end = getEarlierTime(slot1.getEndTime(), slot2.getEndTime());

                    Duration spareTime = Duration.between(start, end); //end-start

                    if (spareTime.compareTo(necessaryTime) >= 0) { // 겹치는 부분이 있는지 확인
                        tempSlots.add(new TimeSlot(start, end));
                    }
                }
            }
            finalList = tempSlots;
            if(finalList.isEmpty()) break;
        }
        return finalList;
    }

    private LocalTime getLaterTime(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    private LocalTime getEarlierTime(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2) ? time1 : time2;
    }
}