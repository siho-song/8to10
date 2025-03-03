package com.eighttoten.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
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

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "n_schedule")
public class NScheduleEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String commonDescription;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private LocalTime bufferTime;

    private int totalAmount;

    public NSchedule toNSchedule(){
        return new NSchedule(
                id, title,
                commonDescription, startDateTime, endDateTime,
                bufferTime, createdBy, totalAmount
        );
    }

    public static NScheduleEntity from(NewNSchedule newNSchedule, MemberEntity memberEntity) {
        NScheduleEntity nScheduleEntity = new NScheduleEntity();
        nScheduleEntity.memberEntity = memberEntity;
        nScheduleEntity.title = newNSchedule.getTitle();
        nScheduleEntity.commonDescription = newNSchedule.getCommonDescription();
        nScheduleEntity.startDateTime = newNSchedule.getStartDateTime();
        nScheduleEntity.endDateTime = newNSchedule.getEndDateTime();
        nScheduleEntity.bufferTime = newNSchedule.getBufferTime();
        nScheduleEntity.totalAmount = newNSchedule.getTotalAmount();
        return nScheduleEntity;
    }

    public void update(NSchedule nSchedule){
        title = nSchedule.getTitle();
        commonDescription = nSchedule.getCommonDescription();
        totalAmount = nSchedule.getTotalAmount();
    }
}