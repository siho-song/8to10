package com.eighttoten.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.repository.ReplyRepositoryImpl;
import java.util.ArrayList;
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
@DisplayName("댓글 레포지토리 테스트")
@Import(ReplyRepositoryImpl.class)
public class ReplyRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @DisplayName("댓글을 id로 조회한다.")
    void findById(){
        //given
        Long replyId = 1L;

        //when,then
        assertThat(replyRepository.findById(replyId)).isNotEmpty();
    }

    @Test
    @DisplayName("댓글을 id로 삭제한다")
    void deleteById(){
        //given
        Long replyId = 6L;

        //when
        replyRepository.deleteById(replyId);

        //then
        assertThat(replyRepository.findById(replyId)).isEmpty();
    }

    @Test
    @DisplayName("해당 id들의 댓글을 삭제한다")
    void deleteByReplyIds(){
        //given
        List<Long> ids = new ArrayList<>();
        ids.add(7L);
        ids.add(8L);
        ids.add(9L);

        //when
        replyRepository.deleteByReplyIds(ids);

        //then
        assertThat(replyRepository.findById(7L)).isEmpty();
        assertThat(replyRepository.findById(8L)).isEmpty();
        assertThat(replyRepository.findById(9L)).isEmpty();
    }

    @Test
    @DisplayName("새로운 댓글을 저장한다")
    void save(){
        //given
        NewReply newReply = new NewReply(1L, 1L, null, "새로운댓글");
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when
        long savedId = replyRepository.save(newReply);

        //then
        assertThat(replyRepository.findById(savedId)).isNotEmpty();
    }

    @Test
    @DisplayName("댓글과 게시글을 함께 id로 조회한다")
    void findByIdWithPost(){
        //given
        Long replyId = 1L;

        //when
        ReplyWithPost replyWithPost = replyRepository.findByIdWithPost(replyId).orElseThrow();

        //then
        assertThat(replyWithPost.getPost().getContents()).isNotNull();
    }

    @Test
    @DisplayName("게시글의 id로 게시글의 모든 댓글들을 조회한다.")
    void findAllByPostId(){
        //given
        Long postId = 1L;

        //when
        List<Reply> replies = replyRepository.findAllByPostId(postId);

        //then
        assertThat(replies.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("멤버의 id로 멤버가 작성한 모든 댓글들을 조회한다.")
    void findAllByMemberId(){
        //given
        Long memberId = 1L;

        //when
        List<Reply> replies = replyRepository.findAllByMemberId(memberId);

        //then
        assertThat(replies.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("부모댓글의 id로 해당 댓글의 모든 대댓글들을 조회한다.")
    void findAllByParentId(){
        //given
        Long parentId = 1L;

        //when
        List<Reply> replies = replyRepository.findAllByParentId(parentId);

        //then
        assertThat(replies.size()).isGreaterThan(1);
    }
}