package com.eighttoten.achievement.dto;

import com.eighttoten.achievement.Achievement;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AchievementResponse {
    private LocalDate achievementDate;
    private double achievementRate;

    public static AchievementResponse from(Achievement achievement){
        return AchievementResponse.builder()
                .achievementDate(achievement.getAchievementDate())
                .achievementRate(achievement.getAchievementRate())
                .build();
    }
}