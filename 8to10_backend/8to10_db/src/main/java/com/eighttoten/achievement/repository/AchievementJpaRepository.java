package com.eighttoten.achievement.repository;

import com.eighttoten.achievement.AchievementEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AchievementJpaRepository extends JpaRepository<AchievementEntity, Long> {
    Optional<AchievementEntity> findByMemberEntityIdAndAchievementDate(Long memberId, LocalDate achievementDate);

    @Query("select a from AchievementEntity a where a.memberEntity.id = :memberId and a.achievementDate between :start and :end")
    List<AchievementEntity> findAllByMemberIdBetweenStartAndEnd(@Param(value = "memberId") Long memberId,
                                                                @Param(value = "start") LocalDate start,
                                                                @Param(value = "end") LocalDate end);

    @Query("select a from AchievementEntity a where a.achievementDate = :date")
    @EntityGraph(attributePaths = {"memberEntity"})
    List<AchievementEntity> findAllByDateWithMember(@Param(value = "date") LocalDate date);
}