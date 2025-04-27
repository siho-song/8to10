package com.eighttoten.schedule.domain.fschedule;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FScheduleDetail implements ScheduleAble {
    private Long id;
    private Long fScheduleId;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String createdBy;

    @Override
    public LocalDateTime getScheduleStart() {
        return startDateTime;
    }

    @Override
    public LocalDateTime getScheduleEnd() { return endDateTime; }

    public void update(FDetailUpdate fDetailUpdate) {
        detailDescription = fDetailUpdate.getDetailDescription();
        startDateTime = fDetailUpdate.getStartDateTime();
        endDateTime = fDetailUpdate.getEndDateTime();
    }
}
