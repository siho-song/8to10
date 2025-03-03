package com.eighttoten.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.fschedule.repository.FScheduleDetailRepositoryImpl;
import java.time.LocalDateTime;
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
@DisplayName("고정일정 자식일정 레포지토리 테스트")
@Import(FScheduleDetailRepositoryImpl.class)
public class FScheduleDetailRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    FScheduleDetailRepository fScheduleDetailRepository;

    @Test
    @DisplayName("고정일정 자식일정을 저장한다.")
    void save(){
        //given
        Long fScheduleId = 1L;
        String detailDescription = "메모";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        NewFDetail newFDetail = new NewFDetail(fScheduleId, detailDescription, start, end);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(()->fScheduleDetailRepository.save(newFDetail)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("고정일정 자식일정을 업데이트한다.")
    void update(){
        //given
        Long fDetailId = 1L;
        String newDetailDescription = "새로운 메모";
        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = LocalDateTime.now();
        FScheduleDetail fDetail = fScheduleDetailRepository.findById(fDetailId).orElseThrow();

        fDetail.update(new FDetailUpdate(fDetailId, newDetailDescription, newStart, newEnd));

        //when
        fScheduleDetailRepository.update(fDetail);

        //then
        FScheduleDetail updated = fScheduleDetailRepository.findById(fDetailId).orElseThrow();
        assertThat(updated.getDetailDescription()).isEqualTo(newDetailDescription);
        assertThat(updated.getStartDateTime()).isEqualTo(newStart);
        assertThat(updated.getEndDateTime()).isEqualTo(newEnd);
    }

    @Test
    @DisplayName("고정일정 자식일정을 id로 조회한다.")
    void findById(){
        //given
        Long fDetailId = 1L;

        //when,then
        assertThat(fScheduleDetailRepository.findById(fDetailId)).isNotEmpty();
    }

    @Test
    @DisplayName("고정일정 자식일정을 id로 삭제한다.")
    void deleteById(){
        //given
        Long fDetailId = 2L;

        //when
        fScheduleDetailRepository.deleteById(fDetailId);

        //then
        assertThat(fScheduleDetailRepository.findById(fDetailId)).isEmpty();
    }

    @Test
    @DisplayName("고정일정 자식일정들을 id 리스트로 삭제한다.")
    void deleteAllByIds(){
        //given
        List<Long> ids = List.of(12L, 13L, 14L, 15L);

        //when
        fScheduleDetailRepository.deleteAllByIds(ids);

        //then
        assertThat(fScheduleDetailRepository.findById(12L)).isEmpty();
        assertThat(fScheduleDetailRepository.findById(13L)).isEmpty();
        assertThat(fScheduleDetailRepository.findById(14L)).isEmpty();
        assertThat(fScheduleDetailRepository.findById(15L)).isEmpty();
    }

    @Test
    @DisplayName("멤버의 이메일로 모든 고정일정 자식일정들을 부모일정과 함께 조회한다.")
    void findAllWithParentByMemberEmail(){
        //given
        String email = "normal2@example.com";

        //when
        List<FDetailWithParent> fDetails = fScheduleDetailRepository.findAllWithParentByMemberEmail(email);

        //then
        assertThat(fDetails).hasSize(5);
    }

    @Test
    @DisplayName("멤버의 이메일로 시작날짜와 종료날짜 사이에 있는 모든 고정일정 자식일정들을 조회한다.")
    void findAllByMemberEmailBetweenStartAndEnd(){
        //given
        String email = "normal2@example.com";
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 2, 28, 0, 0);

        //when
        List<FScheduleDetail> fDetails = fScheduleDetailRepository.findAllByMemberEmailBetweenStartAndEnd(email, start, end);

        //then
        assertThat(fDetails).hasSize(5);
    }

    @Test
    @DisplayName("시작날짜 이후의 모든 고정일정들을 자식일정을 멤버의 이메일과, 부모일정의 id로 조회한다.")
    void findAllByMemberEmailAndParentIdGEStart(){
        //given
        String email = "normal2@example.com";
        Long parentId = 2L;
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 0, 0);

        //when
        List<FScheduleDetail> fDetails = fScheduleDetailRepository.findAllByMemberEmailAndParentIdGEStart(email, parentId, start);

        //then
        assertThat(fDetails).hasSizeGreaterThan(1);
    }
}