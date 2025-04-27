package com.eighttoten.schedule.domain.vschedule;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class VSchedule implements ScheduleAble {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String createdBy;

    public VSchedule(Long id, String title, String commonDescription,
                     LocalDateTime startDateTime, LocalDateTime endDateTime, String createdBy)
    {
        this.id = id;
        this.title = title;
        this.commonDescription = commonDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdBy = createdBy;
    }

    public void update(VScheduleUpdate vScheduleUpdate){
        this.title = vScheduleUpdate.getTitle();
        this.commonDescription = vScheduleUpdate.getCommonDescription();
        this.startDateTime = vScheduleUpdate.getStartDateTime();
        this.endDateTime = vScheduleUpdate.getEndDateTime();
    }

    @Override
    public LocalDateTime getScheduleStart() {
        return startDateTime;
    }

    @Override
    public LocalDateTime getScheduleEnd() {
        return endDateTime;
    }
}
