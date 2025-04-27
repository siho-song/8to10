package com.eighttoten.schedule.domain.scheduleable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public interface ScheduleAble {
    @JsonIgnore
    LocalDateTime getScheduleStart();

    @JsonIgnore
    LocalDateTime getScheduleEnd();
}