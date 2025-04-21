package com.eighttoten.schedule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("일정 공통 컨트롤러 통합 테스트")
class ScheduleAbleControllerTest {
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MockMvc mockMvc;

    String token ;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal2@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("해당 멤버의 모든일정을 조회한다.")
    void getAllSchedule() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/schedule/{year}/{month}",2024,6)
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(6));
    }
}