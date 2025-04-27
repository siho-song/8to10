package com.eighttoten.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("일정화 가능 객체 단위 테스트")
@Transactional
class ScheduleAbleTest {
    @MockBean
    VScheduleRepository vScheduleRepository;

    @MockBean
    FScheduleDetailRepository fScheduleDetailRepository;

    @MockBean
    NScheduleDetailRepository nScheduleDetailRepository;

    @Autowired
    ScheduleAbleService scheduleAbleService;

    @Test
    @DisplayName("기간 내 멤버의 모든 일정화 할수 있는 객체들을 조회한다.")
    void findAllBetweenStartAndEnd(){
        //given
        VSchedule vSchedule = new VSchedule(null, null, null, null,
                null, null, null);
        when(vScheduleRepository.findAllByMemberEmailInPeriod(any(), any(), any())).thenReturn(List.of(vSchedule));

        NScheduleDetail nScheduleDetail = new NScheduleDetail(null, null, null, null, null,
                null, null, false, 0, 0);
        when(nScheduleDetailRepository.findAllByMemberEmailInPeriod(any(), any(), any())).thenReturn(
                List.of(nScheduleDetail));

        FScheduleDetail fScheduleDetail = new FScheduleDetail(null, null, null,
                null, null, null);
        when(fScheduleDetailRepository.findAllByMemberEmailInPeriod(any(), any(), any())).thenReturn(
                List.of(fScheduleDetail));

        //when
        List<ScheduleAble> scheduleAbles = scheduleAbleService.findAllByMemberEmailInPeriod(null, null, null);

        //then
        assertThat(scheduleAbles).hasSize(3);
    }

    @Test
    @DisplayName("멤버의 3개월 내 모든 일정화 할수 있는 객체들을 부모 객체와 함께 조회한다.")
    void findAllWithParentByMember(){
        //given
        VSchedule vSchedule = new VSchedule(null, null, null, null,
                null, null, null);
        when(vScheduleRepository.findAllByMemberEmailInPeriod(any(),any(),any())).thenReturn(List.of(vSchedule));

        NDetailWithParent nDetailWithParent = new NDetailWithParent(null, null, null, null, null,
                null, null, false, 0, 0);
        when(nScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(any(),any(),any())).thenReturn(List.of(nDetailWithParent));

        FDetailWithParent fDetailWithParent = new FDetailWithParent(null, null, null,
                null, null, null);
        when(fScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(any(),any(),any())).thenReturn(List.of(fDetailWithParent));

        //when
        List<ScheduleAble> scheduleAbles = scheduleAbleService.findAllWithParentFromRecentMonth(null,
                LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        //then
        assertThat(scheduleAbles).hasSize(3);
    }

}