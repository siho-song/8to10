package com.eighttoten.schedule.fschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "f_schedule_detail")
public class FScheduleDetailEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "f_schedule_id", nullable = false)
    private FScheduleEntity fScheduleEntity;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String detailDescription;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    public static FScheduleDetailEntity from(NewFDetail newFDetail, FScheduleEntity fScheduleEntity) {
        FScheduleDetailEntity fScheduleDetailEntity = new FScheduleDetailEntity();
        fScheduleDetailEntity.fScheduleEntity = fScheduleEntity;
        fScheduleDetailEntity.detailDescription = newFDetail.getDetailDescription();
        fScheduleDetailEntity.startDateTime = newFDetail.getStartDateTime();
        fScheduleDetailEntity.endDateTime = newFDetail.getEndDateTime();
        return fScheduleDetailEntity;
    }

    public FScheduleDetail toFScheduleDetail(){
        return new FScheduleDetail(
                id, fScheduleEntity.getId(),
                detailDescription, startDateTime, endDateTime, createdBy);
    }

    public FDetailWithParent toFDetailWithParent(){
        return new FDetailWithParent(id, fScheduleEntity.toFSchedule(), detailDescription, startDateTime, endDateTime,
                createdBy);
    }

    public void update(FScheduleDetail fScheduleDetail){
        detailDescription = fScheduleDetail.getDetailDescription();
        startDateTime = fScheduleDetail.getStartDateTime();
        endDateTime = fScheduleDetail.getEndDateTime();
    }
}