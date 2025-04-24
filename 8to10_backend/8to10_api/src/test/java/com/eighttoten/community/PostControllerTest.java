package com.eighttoten.community;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("게시글 컨트롤러 통합테스트")
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setAuthenticationToken(){
        token = tokenProvider.generateAccessToken("normal@example.com");
    }

    @Test
    @DisplayName("게시글 페이지 조회에 성공한다.")
    void searchPostPreviewPage() throws Exception {
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/community/post")
                .header("Authorization", "Bearer " + token)
                .param("keyword", "nick3")
                .param("searchCond", "WRITER")
                .param("sortCond", "LIKE")
                .param("sortDirection", "DESC")
                .param("pageNum", "1")
                .param("pageSize", "10"));

        //then
        result.andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.pageNumber").value(1)) // 페이지 번호 확인
                .andExpect(jsonPath("$.pageSize").value(10)) // 페이지 크기 확인
                .andExpect(jsonPath("$.totalPages").value(1)) // 총 페이지 수 확인
                .andExpect(jsonPath("$.totalElements").value(15)) // 전체 요소 개수 확인

                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.contents.length()").value(10)) // 첫 페이지 10개 데이터 확인

                .andExpect(jsonPath("$.contents[0].id").value(38))
                .andExpect(jsonPath("$.contents[0].nickname").value("nick3"))
                .andExpect(jsonPath("$.contents[0].totalLike").value(7))
                .andExpect(jsonPath("$.contents[0].totalScrap").value(2));
    }

    @Test
    @DisplayName("게시글 단건 조회에 성공한다.")
    void searchPost() throws Exception {
        //given
        Long postId = 1L;

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/community/post/{id}", postId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("First PostEntity Post 1"))
                .andExpect(jsonPath("$.writer").value("normal@example.com"))
                .andExpect(jsonPath("$.totalLike").value(5))
                .andExpect(jsonPath("$.totalScrap").value(2))
                .andExpect(jsonPath("$.hasLike").value(true))
                .andExpect(jsonPath("$.hasScrap").value(true))

                .andExpect(jsonPath("$.likedReplyIds").isArray())
                .andExpect(jsonPath("$.likedReplyIds[0]").value(1))
                .andExpect(jsonPath("$.likedReplyIds[1]").value(4))

                .andExpect(jsonPath("$.replies").isArray())
                .andExpect(jsonPath("$.replies.length()").value(13))
                .andExpect(jsonPath("$.replies[0].id").value(1));
    }

    @Test
    @DisplayName("해당 멤버의 게시글 등록에 성공한다.")
    void addPost() throws Exception {
        //given
        String token1 = tokenProvider.generateAccessToken("normal2@example.com");
        Map<String, Object> request = new HashMap<>();
        String title = "게시판 테스트용";
        String contents = "테스트 내용";
        request.put("title", title);
        request.put("contents", contents);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/post/save")
                        .header("Authorization","Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 단건 삭제에 성공한다.")
    void deletePost() throws Exception {
        //given
        Long postId = 2L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/post/{id}", postId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 업데이트에 성공한다.")
    void updatePost() throws Exception {
        //given
        Long postId = 1L;
        String newContents = "게시글 수정 테스트 내용";
        String newTitle = "게시글 수정 테스트 제목";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", postId);
        requestBody.put("title", newTitle);
        requestBody.put("contents", newContents);
        String body = objectMapper.writeValueAsString(requestBody);

        //then,then
        mockMvc.perform(MockMvcRequestBuilders.put("/community/post")
                .header("Authorization","Bearer " + token)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 좋아요에 성공한다.")
    void addHeart() throws Exception {
        //given
        Long postId = 3L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/post/{id}/heart", postId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 좋아요 삭제에 성공한다.")
    void deleteHeart() throws Exception {
        //given
        Long postId = 2L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/post/{id}/heart", postId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 스크랩 등록에 성공한다.")
    void addScrap() throws Exception {
        //given
        long postId = 2L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/post/{id}/scrap", postId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("해당 멤버의 게시글 스크랩 삭제에 성공한다.")
    void deleteScrap() throws Exception {
        //given
        long postId = 2L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/post/{id}/scrap", postId)
                        .header("Authorization","Bearer " + token)
                ).andExpect(status().isNoContent());
    }
}