package com.eighttoten.achievement;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.achievement.dto.AchievementResponse;
import com.eighttoten.member.domain.Member;
import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping(value = "/{year}/{month}" , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<AchievementResponse>> getMonthAchievement(
            @CurrentMember Member member,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "month") int month)
    {
        List<Achievement> achievements = achievementService.getMonthlyAchievement(member.getId(), year, month);

        Result<AchievementResponse> result = Result.fromElements(achievements, AchievementResponse::from);

        return ResponseEntity.ok(result);
    }
}