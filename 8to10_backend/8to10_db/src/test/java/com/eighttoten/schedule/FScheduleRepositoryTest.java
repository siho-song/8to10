package com.eighttoten.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleUpdate;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import com.eighttoten.schedule.fschedule.repository.FScheduleRepositoryImpl;
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
@DisplayName("고정일정 레포지토리 테스트")
@Import(FScheduleRepositoryImpl.class)
public class FScheduleRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    FScheduleRepository fScheduleRepository;

    @Test
    @DisplayName("새로운 고정일정을 저장한다.")
    void save(){
        //given
        NewFSchedule newFSchedule = new NewFSchedule(1L, "새로운 고정일정", "새로운 메모",
                LocalDateTime.now(), LocalDateTime.now());
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when
        long savedId = fScheduleRepository.save(newFSchedule);

        //then
        assertThat(fScheduleRepository.findById(savedId)).isNotEmpty();
    }

    @Test
    @DisplayName("고정일정을 업데이트한다.")
    void update(){
        //given
        Long fScheduleId = 1L;
        String newTitle = "새로운 일정제목";
        String newCommonDescription = "새로운 일정메모";
        FSchedule fSchedule = fScheduleRepository.findById(fScheduleId).orElseThrow();
        fSchedule.update(new FScheduleUpdate(fScheduleId, newTitle, newCommonDescription));

        //when
        fScheduleRepository.update(fSchedule);

        //then
        FSchedule updated = fScheduleRepository.findById(fScheduleId).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getCommonDescription()).isEqualTo(newCommonDescription);
    }

    @Test
    @DisplayName("고정일정을 id로 조회한다.")
    void findById(){
        //given
        Long fScheduleId = 1L;

        //when,then
        assertThat(fScheduleRepository.findById(fScheduleId)).isNotEmpty();
    }

    @Test
    @DisplayName("고정일정을 id로 삭제한다.")
    void deleteById(){
        //given
        Long fScheduleId = 4L;

        //when
        fScheduleRepository.deleteById(fScheduleId);

        //then
        assertThat(fScheduleRepository.findById(fScheduleId)).isEmpty();
    }
}