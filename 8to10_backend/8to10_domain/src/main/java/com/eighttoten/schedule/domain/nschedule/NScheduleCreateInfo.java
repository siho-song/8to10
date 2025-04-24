package com.eighttoten.schedule.domain.nschedule;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class NScheduleCreateInfo {
    private final LocalTime bufferTime;
    private final LocalTime performInDay;
    private final Boolean isIncludeSaturday;
    private final Boolean isIncludeSunday;
    private final int performInWeek;

    public NScheduleCreateInfo(LocalTime bufferTime, LocalTime performInDay,
                               Boolean isIncludeSaturday, Boolean isIncludeSunday, int performInWeek) {
        this.bufferTime = bufferTime;
        this.performInDay = performInDay;
        this.isIncludeSaturday = isIncludeSaturday;
        this.isIncludeSunday = isIncludeSunday;
        this.performInWeek = performInWeek;
    }

    public List<DayOfWeek> getDays() {
        List<DayOfWeek> days = new ArrayList<>(
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY));

        if(isIncludeSaturday) {
            days.add(DayOfWeek.SATURDAY);
        }
        if (isIncludeSunday) {
            days.add(DayOfWeek.SUNDAY);
        }
        return days;
    }

    public Duration getNecessaryTime() {
        long hour = (long) bufferTime.getHour() + performInDay.getHour();
        long minute = (long) bufferTime.getMinute() + performInDay.getMinute();
        if(minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        }
        return Duration.ofHours(hour).plusMinutes(minute);
    }
}
