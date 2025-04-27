package com.eighttoten.schedule.domain.nschedule;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class NScheduleDetail implements ScheduleAble {
    private Long id;
    private Long nScheduleId;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String createdBy;
    private boolean completeStatus;
    @Setter
    private int dailyAmount;
    private int achievedAmount;

    public void update(String detailDescription) {
        this.detailDescription = detailDescription;
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
        if(dailyAmount == 0){
            return 0;
        }
        return (double) achievedAmount / dailyAmount;
    }

    @Override
    public LocalDateTime getScheduleStart() {
        return this.startDateTime.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }

    @Override
    public LocalDateTime getScheduleEnd() {
        return endDateTime;
    }
}
