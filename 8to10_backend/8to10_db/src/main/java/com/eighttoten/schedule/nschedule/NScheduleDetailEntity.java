package com.eighttoten.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "n_schedule_detail")
public class NScheduleDetailEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "n_schedule_id", nullable = false)
    private NScheduleEntity nScheduleEntity;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String detailDescription;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private LocalTime bufferTime;

    private boolean completeStatus;

    @Setter
    private int dailyAmount;

    private int achievedAmount;

    public static NScheduleDetailEntity from(NewNDetail nDetail, NScheduleEntity nScheduleEntity) {
        NScheduleDetailEntity entity = new NScheduleDetailEntity();
        entity.nScheduleEntity = nScheduleEntity;
        entity.startDateTime = nDetail.getStartDateTime();
        entity.endDateTime = nDetail.getEndDateTime();
        entity.bufferTime = nDetail.getBufferTime();
        entity.detailDescription = nDetail.getDetailDescription();
        entity.completeStatus = nDetail.isCompleteStatus();
        entity.dailyAmount = nDetail.getDailyAmount();
        entity.achievedAmount = nDetail.getAchievedAmount();
        return entity;
    }

    public NScheduleDetail toNScheduleDetail(){
        return new NScheduleDetail(
                id, nScheduleEntity.getId(), detailDescription, startDateTime,
                endDateTime, bufferTime, createdBy, completeStatus,
                dailyAmount, achievedAmount
        );
    }

    public NDetailWithParent toNDetailWithParent(){
        return new NDetailWithParent(id, nScheduleEntity.toNSchedule(), detailDescription, startDateTime, endDateTime,
                bufferTime, createdBy, completeStatus, dailyAmount, achievedAmount);
    }

    public void update(NScheduleDetail nScheduleDetail){
        detailDescription = nScheduleDetail.getDetailDescription();
        completeStatus = nScheduleDetail.isCompleteStatus();
        dailyAmount = nScheduleDetail.getDailyAmount();
        achievedAmount = nScheduleDetail.getAchievedAmount();
    }
}