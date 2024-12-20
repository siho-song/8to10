package com.eighttoten.dto.schedule.request.nschedule;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import com.eighttoten.dto.schedule.request.ScheduleSave;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.validator.schedule.fielderror.PerformInDay;
import com.eighttoten.validator.schedule.fielderror.ZeroSeconds;
import com.eighttoten.validator.schedule.objecterror.PerformInWeek;
import com.eighttoten.validator.schedule.objecterror.StartDateBeforeEqEndDate;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@PerformInWeek(groups = ObjectErrorGroup.class)
@StartDateBeforeEqEndDate(groups = ObjectErrorGroup.class)
public class NScheduleSave extends ScheduleSave implements DateRangeValidatable {
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate startDate;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate endDate;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime bufferTime;

    @PerformInDay(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime performInDay;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSaturday;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSunday;

    private int totalAmount;
    private int performInWeek;

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
        long hour = bufferTime.getHour() + performInDay.getHour();
        long minute = bufferTime.getMinute() + performInDay.getMinute();
        if(minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        }
        return Duration.ofHours(hour).plusMinutes(minute);
    }

    @Override
    public LocalDateTime takeStartDateTime() {
        return this.getStartDate().atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return this.getEndDate().atStartOfDay();
    }
}