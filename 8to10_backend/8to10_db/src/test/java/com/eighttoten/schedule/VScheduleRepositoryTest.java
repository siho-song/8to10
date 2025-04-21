package com.eighttoten.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.VScheduleUpdate;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import com.eighttoten.schedule.vschedule.repository.VScheduleRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("변동일정 레포지토리 테스트")
@Import(VScheduleRepositoryImpl.class)
public class VScheduleRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    VScheduleRepository vScheduleRepository;

    @Test
    @DisplayName("새로운 변동일정을 저장한다.")
    void save(){
        //given
        NewVSchedule newVSchedule = new NewVSchedule(1L, "새로운 변동일정",
                "test", LocalDateTime.now(), LocalDateTime.now());
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(() -> vScheduleRepository.save(newVSchedule)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("변동일정을 업데이트한다.")
    void update(){
        //given
        Long vScheduleId = 1L;
        VSchedule vSchedule = vScheduleRepository.findById(vScheduleId).orElseThrow();

        String newTitle = "업데이트 제목";
        String newCommonDescription = "업데이트 메모";
        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = LocalDateTime.now();
        VScheduleUpdate vScheduleUpdate = new VScheduleUpdate(vScheduleId, newTitle, newCommonDescription, newStart, newEnd);
        vSchedule.update(vScheduleUpdate);

        //when
        vScheduleRepository.update(vSchedule);

        //then
        assertThat(vSchedule.getTitle()).isEqualTo(newTitle);
        assertThat(vSchedule.getCommonDescription()).isEqualTo(newCommonDescription);
        assertThat(vSchedule.getStartDateTime()).isEqualTo(newStart);
        assertThat(vSchedule.getEndDateTime()).isEqualTo(newEnd);
    }

    @Test
    @DisplayName("변동일정을 id로 삭제한다")
    void deleteById(){
        //given
        Long vScheduleId = 3L;

        //when
        vScheduleRepository.deleteById(vScheduleId);

        //then
        assertThat(vScheduleRepository.findById(3L)).isEmpty();
    }

    @Test
    @DisplayName("변동일정을 id로 조회한다")
    void findById(){
        //given
        Long vScheduleId = 1L;

        //when,then
        assertThat(vScheduleRepository.findById(vScheduleId)).isNotEmpty();
    }

    @Test
    @DisplayName("시작날짜와 종료날짜 사이에 있는 모든 변동일정을 조회한다.")
    void findAllByEmailInPeriodBetweenStartAndEnd(){
        //given
        String email = "normal@example.com";
        LocalDateTime start = LocalDateTime.of(2024, 7, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 1, 0, 0);

        //when,then
        assertThat(vScheduleRepository.findAllByMemberEmailInPeriod(email, start, end).size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("멤버의 이메일로 모든 변동일정을 조회한다.")
    void findAllByEmailInPeriod(){
        //given
        String email = "normal@example.com";

        //when,then
        assertThat(vScheduleRepository.findAllByEmailInPeriod(email).size()).isGreaterThan(1);
    }
}