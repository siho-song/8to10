package com.eighttoten.schedule.service.vschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.VScheduleUpdate;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("변동일정 서비스 테스트")
class VScheduleServiceTest {
    @MockBean
    VScheduleRepository vScheduleRepository;

    @Autowired
    VScheduleService vScheduleService;

    @Test
    @DisplayName("변동일정 수정에 성공한다.")
    void update() {
        //given
        Member member = TestDataUtils.createTestMember(1L,"normal@example.com");
        VSchedule vSchedule = new VSchedule(null, "변동일정 제목", "변동일정 메모", null, null, member.getEmail());
        VScheduleUpdate vScheduleUpdate = new VScheduleUpdate(1L, "수정된 변동일정 제목", "수정된 변동일정 메모",
                LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(2, 2, 2, 2, 2));
        when(vScheduleRepository.findById(any())).thenReturn(Optional.of(vSchedule));
        doNothing().when(vScheduleRepository).update(any());

        //when
        vScheduleService.update(member, vScheduleUpdate);

        //then
        assertThat(vSchedule.getTitle()).isEqualTo(vScheduleUpdate.getTitle());
        assertThat(vSchedule.getCommonDescription()).isEqualTo(vScheduleUpdate.getCommonDescription());
        assertThat(vSchedule.getStartDateTime()).isEqualTo(vScheduleUpdate.getStartDateTime());
        assertThat(vSchedule.getEndDateTime()).isEqualTo(vScheduleUpdate.getEndDateTime());
    }
}