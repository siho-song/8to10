package com.eighttoten.schedule.service.fschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleUpdate;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("고정일정 서비스 테스트")
@SpringBootTest
class FScheduleServiceTest {
    @MockBean
    FScheduleRepository fScheduleRepository;

    @Autowired
    FScheduleService fScheduleService;

    @Test
    @DisplayName("고정일정 수정에 성공한다.")
    void update(){
        //given
        FScheduleUpdate fScheduleUpdate = new FScheduleUpdate(1L, "수정된 제목", "수정된 내용");
        Member member = TestDataUtils.createTestMember(1L,"normal@example.com");
        FSchedule fSchedule = new FSchedule(null, "제목", "메모", null, null, member.getEmail());

        when(fScheduleRepository.findById(any())).thenReturn(Optional.of(fSchedule));
        doNothing().when(fScheduleRepository).update(any());

        //when
        fScheduleService.update(member, fScheduleUpdate);

        //then
        assertThat(fSchedule.getTitle()).isEqualTo(fScheduleUpdate.getTitle());
        assertThat(fSchedule.getCommonDescription()).isEqualTo(fScheduleUpdate.getCommonDescription());
    }
}