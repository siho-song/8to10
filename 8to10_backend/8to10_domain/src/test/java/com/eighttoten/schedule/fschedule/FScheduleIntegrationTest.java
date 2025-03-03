package com.eighttoten.schedule.service.fschedule;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FScheduleCreateInfo;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import com.eighttoten.support.AuthAccessor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

@DisplayName("고정일정 통합테스트")
@SpringBootTest
public class FScheduleIntegrationTest {
    @Autowired
    AuthAccessor authAccessor;

    @Autowired
    FScheduleDetailService fScheduleDetailService;

    @Autowired
    FScheduleService fScheduleService;

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("고정일정 생성 - 고정일정과, 자식 고정일정을 생성한다.")
    void addDetailsForEachEvent() {
        //given
        Member member = authAccessor.getAuthenticatedMember();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 3), LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 22), LocalTime.of(0, 0));

        NewFSchedule newFSchedule = new NewFSchedule(member.getId(), "테스트 title", "테스트 commonDescription", startDateTime, endDateTime);
        long savedId = fScheduleService.save(newFSchedule);

        FScheduleCreateInfo fScheduleCreateInfo = new FScheduleCreateInfo(
                LocalTime.of(8, 0),
                LocalTime.of(2, 0),
                "weekly",
                new ArrayList<>(List.of("mo", "we", "fr")));

        //when,then
        assertThatCode(() -> fScheduleDetailService.saveAllDetails(savedId, fScheduleCreateInfo)).doesNotThrowAnyException();
    }
}
