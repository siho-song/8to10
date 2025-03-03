package com.eighttoten.schedule.vschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
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
@Table(name = "v_schedule")
public class VScheduleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "v_schedule_id")
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

    public static VScheduleEntity from(NewVSchedule newVSchedule, MemberEntity memberEntity){
        VScheduleEntity vScheduleEntity = new VScheduleEntity();
        vScheduleEntity.memberEntity = memberEntity;
        vScheduleEntity.title = newVSchedule.getTitle();
        vScheduleEntity.commonDescription = newVSchedule.getCommonDescription();
        vScheduleEntity.startDateTime = newVSchedule.getStartDateTime();
        vScheduleEntity.endDateTime = newVSchedule.getEndDateTime();
        return vScheduleEntity;
    }

    public VSchedule toVSchedule(){
        return new VSchedule(id, memberEntity.toMember(), title, commonDescription,
                startDateTime, endDateTime, createdBy);
    }

    public void update(String title,
                       String commonDescription,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime
    ) {
        this.title = title;
        this.commonDescription = commonDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}