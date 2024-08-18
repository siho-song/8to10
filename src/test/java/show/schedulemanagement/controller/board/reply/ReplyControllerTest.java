package show.schedulemanagement.controller.board.reply;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("댓글 CRUD")
class ReplyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    ObjectMapper objectMapper;

    private String token;
    private MockCookie jwtCookie;

    @BeforeEach
    void init(){
        token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
    }


    @Test
    @DisplayName("댓글 정상 등록")
    @Transactional
    void save_reply() throws Exception {
        ReplySaveRequest request = new ReplySaveRequest();
        request.setBoardId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(null);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply/add")
                .contentType(APPLICATION_JSON)
                .cookie(jwtCookie)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("대댓글 정상 등록")
    @Transactional
    void save_nested_reply() throws Exception {
        ReplySaveRequest request = new ReplySaveRequest();
        request.setBoardId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(1L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply/add")
                .contentType(APPLICATION_JSON)
                .cookie(jwtCookie)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("대댓글 레벨이 1보다 높을 경우 예외 발생")
    @Transactional
    void save_nested_reply_level() throws Exception {
        ReplySaveRequest request = new ReplySaveRequest();
        request.setBoardId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(13L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply/add")
                .contentType(APPLICATION_JSON)
                .cookie(jwtCookie)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("대댓글 게시판과, 부모댓글의 게시판이 다를 경우 예외 발생")
    @Transactional
    void save_nested_reply_board() throws Exception {
        ReplySaveRequest request = new ReplySaveRequest();
        request.setBoardId(2L);
        request.setContents("테스트용 댓글");
        request.setParentId(1L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/reply/add")
                .contentType(APPLICATION_JSON)
                .cookie(jwtCookie)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }
}