package com.eighttoten.achievement.repository;

import com.eighttoten.achievement.Achievement;
import com.eighttoten.achievement.AchievementEntity;
import com.eighttoten.achievement.AchievementRepository;
import com.eighttoten.achievement.AchievementWithMember;
import com.eighttoten.achievement.NewAchievement;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AchievementRepositoryImpl implements AchievementRepository {
    private final AchievementJpaRepository achievementRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public void save(NewAchievement newAchievement) {
        MemberEntity memberEntity = memberRepository.findById(newAchievement.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        achievementRepository.save(AchievementEntity.from(newAchievement, memberEntity));
    }

    @Override
    public void update(Achievement achievement) {
        AchievementEntity achievementEntity = achievementRepository.findById(achievement.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ACHIEVEMENT));
        achievementEntity.update(achievement);
    }

    @Override
    public Optional<Achievement> findByMemberIdAndDate(Long memberId, LocalDate date) {
        Optional<AchievementEntity> entity = achievementRepository.findByMemberEntityIdAndAchievementDate(memberId, date);
        return entity.map(AchievementEntity::toAchievement);
    }

    @Override
    public List<Achievement> findAllByMemberIdAndBetweenStartAndEnd(Long memberId, LocalDate start, LocalDate end) {
        List<AchievementEntity> result = achievementRepository.findAllByMemberIdBetweenStartAndEnd(memberId, start, end);
        return result.stream().map(AchievementEntity::toAchievement).toList();
    }

    @Override
    public List<AchievementWithMember> findAllByDateWithMember(LocalDate date) {
        List<AchievementEntity> result = achievementRepository.findAllByDateWithMember(date);
        return result.stream().map(AchievementEntity::toAchievementWithMember).toList();
    }
}
