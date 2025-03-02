package com.eighttoten.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleUpdate;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.nschedule.repository.NScheduleRepositoryImpl;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("일반일정 레포지토리 테스트")
@Import(NScheduleRepositoryImpl.class)
public class NScheduleRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    NScheduleRepository nScheduleRepository;

    @Test
    @DisplayName("새로운 일반일정을 저장한다.")
    void save(){
        //given
        NewNSchedule newNSchedule = new NewNSchedule(1L, "새로운 일정", "새로운 일정메모",
                LocalDateTime.now(), LocalDateTime.now(), LocalTime.of(0, 10), 0);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when
        long savedId = nScheduleRepository.save(newNSchedule);

        //then
        assertThat(nScheduleRepository.findById(savedId)).isNotEmpty();
    }

    @Test
    @DisplayName("일반일정을 업데이트한다.")
    void update(){
        //given
        Long nScheduleId = 1L;
        String newTitle = "새로운 일정제목";
        String newCommonDescription = "새로운 일정메모";
        NSchedule nSchedule = nScheduleRepository.findById(nScheduleId).orElseThrow();
        nSchedule.update(new NScheduleUpdate(nScheduleId, newTitle, newCommonDescription));

        //when
        nScheduleRepository.update(nSchedule);

        //then
        NSchedule updated = nScheduleRepository.findById(nScheduleId).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getCommonDescription()).isEqualTo(newCommonDescription);
    }

    @Test
    @DisplayName("일반일정을 id로 삭제한다.")
    void deleteById(){
        //given
        Long nScheduleId = 5L;

        //when
        nScheduleRepository.deleteById(nScheduleId);

        //then
        assertThat(nScheduleRepository.findById(nScheduleId)).isEmpty();
    }

    @Test
    @DisplayName("일반일정을 id로 조회한다.")
    void findById(){
        //given
        Long nScheduleId = 1L;

        //when,then
        assertThat(nScheduleRepository.findById(nScheduleId)).isNotEmpty();
    }
}