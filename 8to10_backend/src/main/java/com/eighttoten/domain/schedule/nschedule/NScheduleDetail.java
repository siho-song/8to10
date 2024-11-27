package com.eighttoten.domain.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.domain.auditing.baseentity.BaseEntity;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class NScheduleDetail extends BaseEntity implements ScheduleAble {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private NSchedule nSchedule;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    private String detailDescription;

    private boolean completeStatus;

    @Setter
    private int dailyAmount;

    private int achievedAmount;

    public static NScheduleDetail from(String commonDescription, LocalDateTime startDate, LocalDateTime endDate) {
        NScheduleDetail nScheduleDetail = new NScheduleDetail();
        nScheduleDetail.completeStatus = false;
        nScheduleDetail.detailDescription = commonDescription;
        nScheduleDetail.startDate = startDate;
        nScheduleDetail.endDate = endDate;
        return nScheduleDetail;
    }

    public void setNSchedule(NSchedule nSchedule) {
        this.nSchedule = nSchedule;
        nSchedule.getNScheduleDetails().add(this);
    }

    @Override
    public LocalDateTime getStartDate(){
        LocalTime bufferTime = nSchedule.getBufferTime();
        return this.startDate.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }

    public void update(NScheduleDetailUpdate nScheduleDetailUpdate){
        detailDescription = nScheduleDetailUpdate.getDetailDescription();
    }

    public void updateCompleteStatus(boolean completeStatus){
        this.completeStatus = completeStatus;
    }

    public void updateAchievedAmount(int achievedAmount){
        this.achievedAmount = achievedAmount;
    }

    public double getAchievementRate(){
        if(completeStatus){
            return 1;
        }
        return (double) achievedAmount / dailyAmount;
    }
}