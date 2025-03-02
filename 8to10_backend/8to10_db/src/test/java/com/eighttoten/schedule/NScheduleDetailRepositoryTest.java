package com.eighttoten.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.nschedule.repository.NScheduleDetailRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
@DisplayName("일반일정 자식일정 레포지토리 테스트")
@Import(NScheduleDetailRepositoryImpl.class)
public class NScheduleDetailRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    NScheduleDetailRepository nScheduleDetailRepository;

    @Test
    @DisplayName("모든 일반일정 자식일정을 저장한다.")
    void saveAll(){
        //given
        Long nScheduleId = 6L;
        NewNDetail newNDetail1 = new NewNDetail(nScheduleId, LocalDateTime.now(), LocalDateTime.now(),
                LocalTime.of(0, 10), "새로운 일반자식일정1", false, 10, 0);

        NewNDetail newNDetail2 = new NewNDetail(nScheduleId, LocalDateTime.now(), LocalDateTime.now(),
                LocalTime.of(0, 10), "새로운 일반자식일정1", false, 10, 0);

        NewNDetail newNDetail3 = new NewNDetail(nScheduleId, LocalDateTime.now(), LocalDateTime.now(),
                LocalTime.of(0, 10), "새로운 일반자식일정1", false, 10, 0);

        List<NewNDetail> nDetails = List.of(newNDetail1, newNDetail2, newNDetail3);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(()->nScheduleDetailRepository.saveAll(nScheduleId, nDetails)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("일반일정 자식일정을 업데이트한다.")
    void update(){
        //given
        Long nDetailId = 1L;
        String newDetailDescription = "새로운 메모";
        int newAchievementAmount = 10;
        boolean newCompleteStatus = true;
        NScheduleDetail nDetail = nScheduleDetailRepository.findById(nDetailId).orElseThrow();

        nDetail.update(newDetailDescription);
        nDetail.updateAchievedAmount(10);
        nDetail.updateCompleteStatus(true);

        //when
        nScheduleDetailRepository.update(nDetail);

        //then
        NScheduleDetail updated = nScheduleDetailRepository.findById(nDetailId).orElseThrow();
        assertThat(updated.getDetailDescription()).isEqualTo(newDetailDescription);
        assertThat(updated.getAchievedAmount()).isEqualTo(newAchievementAmount);
        assertThat(updated.isCompleteStatus()).isEqualTo(newCompleteStatus);
    }

    @Test
    @DisplayName("일반일정 자식일정을 id로 조회한다.")
    void findById(){
        //given
        Long nDetailId = 1L;

        //when,then
        assertThat(nScheduleDetailRepository.findById(nDetailId)).isNotEmpty();
    }

    @Test
    @DisplayName("일반일정 자식일정을 부모일정과 함께 id로 조회한다.")
    void findByIdWithParent(){
        //given
        Long nDetailId = 1L;

        //when
        Optional<NDetailWithParent> withParent = nScheduleDetailRepository.findByIdWithParent(nDetailId);
        NDetailWithParent nDetailWithParent = withParent.orElseThrow();

        //then
        assertThat(withParent).isNotEmpty();
        assertThat(nDetailWithParent.getNSchedule().getTitle()).isNotNull();
    }

    @Test
    @DisplayName("일반일정 자식일정을 id로 삭제한다.")
    void deleteById(){
        //given
        Long nDetailId = 2L;

        //when
        nScheduleDetailRepository.deleteById(nDetailId);

        //then
        assertThat(nScheduleDetailRepository.findById(nDetailId)).isEmpty();
    }

    @Test
    @DisplayName("일반일정 자식일정들을 id 리스트로 조회한다.")
    void findAllByIds(){
        //given
        List<Long> ids = List.of(6L, 7L, 8L, 9L, 10L);

        //when,then
        assertThat(nScheduleDetailRepository.findAllByIds(ids)).hasSize(5);
    }

    @Test
    @DisplayName("일반일정 자식일정들을 id 리스트로 삭제한다.")
    void deleteAllByIds(){
        //given
        List<Long> ids = List.of(17L, 18L, 19L, 20L, 21L);

        //when
        nScheduleDetailRepository.deleteAllByIds(ids);

        //then
        assertThat(nScheduleDetailRepository.findAllByIds(ids)).isEmpty();
    }

    @Test
    @DisplayName("시작날짜 이후의 모든 일반일정들을 자식일정을 멤버의 이메일과, 부모일정의 id로 조회한다.")
    void findAllByMemberEmailAndParentIdGEStart(){
        //given
        String email = "normal2@example.com";
        Long parentId = 3L;
        LocalDateTime start = LocalDateTime.of(2024, 4, 1, 0, 0);

        //when
        List<NScheduleDetail> nDetails = nScheduleDetailRepository.findAllByMemberEmailAndParentIdGEStart(email, parentId, start);

        //then
        assertThat(nDetails).hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("모든 일반일정 자식일정들을 멤버의 이메일, 날짜로 조회한다.")
    void findAllByMemberEmailAndDate(){
        //given
        String email = "normal2@example.com";
        LocalDate date = LocalDate.of(2024, 6, 1);

        //when
        List<NScheduleDetail> nDetails = nScheduleDetailRepository.findAllByMemberEmailAndDate(email, date);

        //then
        assertThat(nDetails).hasSize(2);
    }

    @Test
    @DisplayName("멤버의 이메일로 시작날짜와 종료날짜 사이에 있는 모든 일반일정 자식일정들을 조회한다.")
    void findAllByMemberEmailBetweenStartAndEnd(){
        //given
        String email = "normal2@example.com";
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 7, 0, 0);

        //when
        List<NScheduleDetail> nDetails = nScheduleDetailRepository.findAllByMemberEmailBetweenStartAndEnd(email, start, end);

        //then
        assertThat(nDetails).hasSize(6);
    }

    @Test
    @DisplayName("멤버의 이메일로 모든 일반일정 자식일정들을 부모일정과 함께 조회한다.")
    void findAllWithParentByMemberEmail(){
        //given
        String email = "normal2@example.com";

        //when
        List<NDetailWithParent> nDetails = nScheduleDetailRepository.findAllWithParentByMemberEmail(email);

        //then
        assertThat(nDetails).isNotEmpty();
        assertThat(nDetails.get(0).getNSchedule().getTitle()).isNotNull();
    }
}