package com.eighttoten.schedule.domain.nschedule;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class NDetailWithParent implements ScheduleAble {
    private Long id;
    @JsonProperty(value = "nschedule")
    private NSchedule nSchedule;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String createdBy;
    private boolean completeStatus;
    @Setter
    private int dailyAmount;
    private int achievedAmount;

    @Override
    public LocalDateTime getScheduleStart() {
        return this.startDateTime.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }

    @Override
    public LocalDateTime getScheduleEnd() { return endDateTime; }
}
