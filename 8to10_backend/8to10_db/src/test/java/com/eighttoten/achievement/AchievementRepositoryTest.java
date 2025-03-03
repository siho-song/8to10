package com.eighttoten.achievement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.achievement.repository.AchievementRepositoryImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("성취도 레포지토리 테스트")
@Import(AchievementRepositoryImpl.class)
public class AchievementRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    AchievementRepository achievementRepository;

    @Test
    @DisplayName("새로운 성취도를 저장한다.")
    void save(){
        //given
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        double achievementRate = 10.0;
        NewAchievement newAchievement = NewAchievement.from(memberId, date, achievementRate);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(() -> achievementRepository.save(newAchievement)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("성취도의 성취율을 업데이트한다.")
    void update(){
        //given
        Long memberId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 1);
        double newAchievementRate = 25.0;
        Achievement achievement = achievementRepository.findByMemberIdAndDate(memberId, date).orElseThrow();
        achievement.updateAchievementRate(newAchievementRate);

        //when
        achievementRepository.update(achievement);

        //then
        assertThat(achievementRepository.findByMemberIdAndDate(memberId, date).orElseThrow()
                .getAchievementRate()).isEqualTo(newAchievementRate);
    }

    @Test
    @DisplayName("멤버의 id와 특정날짜로 성취도를 조회한다.")
    void findByMemberIdAndDate(){
        //given
        Long memberId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 1);

        //when,then
        assertThat(achievementRepository.findByMemberIdAndDate(memberId, date)).isNotEmpty();
    }

    @Test
    @DisplayName("멤버의 id와 시작날짜, 종료날짜 사이의 모든 일정들을 조회한다.")
    void findAllByMemberIdAndBetweenStartAndEnd(){
        //given
        Long memberId = 1L;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 7);

        //when
        List<Achievement> achievements = achievementRepository.findAllByMemberIdAndBetweenStartAndEnd(
                memberId, start, end);

        //then
        assertThat(achievements).hasSize(7);
    }

    @Test
    @DisplayName("특정날짜로 모든 일정들을 멤버와 같이 조회한다.")
    void findAllByDateWithMember(){
        //given
        LocalDate date = LocalDate.of(2024, 1, 3);
        //when
        List<AchievementWithMember> achievementWithMembers = achievementRepository.findAllByDateWithMember(date);

        //then
        assertThat(achievementWithMembers).hasSize(3);
        assertThat(achievementWithMembers.get(0).getMember().getEmail()).isNotNull();
        assertThat(achievementWithMembers.get(1).getMember().getEmail()).isNotNull();
        assertThat(achievementWithMembers.get(2).getMember().getEmail()).isNotNull();
    }
}
