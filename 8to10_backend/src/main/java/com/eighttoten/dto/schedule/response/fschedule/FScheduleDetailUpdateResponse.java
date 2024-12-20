package com.eighttoten.dto.schedule.response.fschedule;

import com.eighttoten.domain.schedule.fschedule.FScheduleDetail;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FScheduleDetailUpdateResponse {
    private Long id;
    private String detailDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static FScheduleDetailUpdateResponse from(FScheduleDetail fScheduleDetail){
        return FScheduleDetailUpdateResponse.builder()
                .id(fScheduleDetail.getId())
                .detailDescription(fScheduleDetail.getDetailDescription())
                .startDate(fScheduleDetail.getStartDate())
                .endDate(fScheduleDetail.getEndDate())
                .build();
    }
}