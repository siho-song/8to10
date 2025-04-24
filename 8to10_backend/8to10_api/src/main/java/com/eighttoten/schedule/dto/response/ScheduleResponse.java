package com.eighttoten.schedule.dto.response;

import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.BusinessException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class ScheduleResponse {
    protected Long id;
    protected String title;
    protected String commonDescription;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected String type; // "normal","fixed","variable"
    protected String color;

    public static ScheduleResponse from(ScheduleAble scheduleAble) {
        if (scheduleAble instanceof NDetailWithParent) {
            return new NScheduleResponse((NDetailWithParent) scheduleAble);
        }
        else if (scheduleAble instanceof FDetailWithParent) {
            return new FScheduleResponse((FDetailWithParent) scheduleAble);
        } else if (scheduleAble instanceof VSchedule) {
            return new VScheduleResponse((VSchedule) scheduleAble);
        }
        else {
            throw new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}