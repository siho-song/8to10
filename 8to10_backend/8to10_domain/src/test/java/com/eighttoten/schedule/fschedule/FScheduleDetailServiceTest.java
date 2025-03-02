package com.eighttoten.schedule.service.fschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("고정일정 자식일정 서비스 테스트")
class FScheduleDetailServiceTest {
    @MockBean
    FScheduleDetailRepository fScheduleDetailRepository;

    @Autowired
    FScheduleDetailService fScheduleDetailService;

    @Test
    @DisplayName("고정 자식일정 단건 업데이트에 성공한다.")
    void update(){
        //given
        Member member = TestDataUtils.createTestMember(1L,"normal@example.com");
        FScheduleDetail fScheduleDetail = new FScheduleDetail(1L, null, "메모", null, null, member.getEmail());
        FDetailUpdate fDetailUpdate = new FDetailUpdate(1L, "수정된 메모", LocalDateTime.of(1, 1, 1, 1, 1, 1),
                LocalDateTime.of(2, 2, 2, 2, 2, 2));

        when(fScheduleDetailRepository.findById(any())).thenReturn(Optional.of(fScheduleDetail));
        doNothing().when(fScheduleDetailRepository).update(any());

        //when
        fScheduleDetailService.update(member, fDetailUpdate);

        //then
        assertThat(fScheduleDetail.getDetailDescription()).isEqualTo(fDetailUpdate.getDetailDescription());
        assertThat(fScheduleDetail.getStartDateTime()).isEqualTo(fDetailUpdate.getStartDateTime());
        assertThat(fScheduleDetail.getEndDateTime()).isEqualTo(fDetailUpdate.getEndDateTime());
    }
}