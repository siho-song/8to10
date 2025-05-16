package com.eighttoten.schedule.fschedule.projection;

import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FScheduleDetailProjection {
    private Long id;
    private Long parentId;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String detailDescription;

    public FDetailWithParent toFDetailWithParent(){
        return new FDetailWithParent(id,
                new FSchedule(parentId, title, commonDescription, null, null, null),
                detailDescription, startDateTime, endDateTime, null);
    }
}
