package com.eighttoten.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.UpdateReply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.service.ReplyService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.service.AsyncNotificationEventPublisher;
import com.eighttoten.support.AuthAccessor;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

@DisplayName("댓글 서비스 통합테스트")
@SpringBootTest
public class ReplyIntegrationTest {
    @MockBean
    AsyncNotificationEventPublisher eventPublisher;

    @Autowired
    AuthAccessor authAccessor;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReplyService replyService;

    @Test
    @WithUserDetails("normal@example.com")
    @DisplayName("댓글이 정상등록 되고, 댓글 추가 이벤트가 생성되면 성공한다")
    void add_reply(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        Long postId = 1L;
        String contents = "새로운 테스트 댓글";
        NewReply newReply = new NewReply(member.getId(), postId, null, contents);

        //when
        replyService.addReply(newReply);

        //then
        verify(eventPublisher, times(1)).notifyReplyAddEvent(any());
    }

    @Test
    @WithUserDetails("normal@example.com")
    @DisplayName("대댓글이 정상등록 되고, 대댓글 추가 이벤트가 생성되면 성공한다")
    void add_nested_reply(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        Long postId = 1L;
        Long parentId = 1L;
        String contents = "새로운 테스트 댓글";
        NewReply newReply = new NewReply(member.getId(), postId, parentId, contents);

        //when
        replyService.addReply(newReply);

        //then
        verify(eventPublisher, times(1)).notifyReplyAddEvent(any());
    }
    
    @Test
    @WithUserDetails("normal@example.com")
    @DisplayName("댓글의 작성자와 클라이언트의 이메일이 같으면 댓글을 수정에 성공한다.")
    void update_reply_if_equal_writer(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        Long replyId = 2L;
        String newContents = "새로운 댓글 내용";
        UpdateReply updateReply = new UpdateReply(replyId, newContents);

        //when
        replyService.update(member, updateReply);

        //then
        Reply updated = replyRepository.findById(replyId).orElseThrow();
        assertThat(updated.getContents()).isEqualTo(newContents);
    }
    

    @Test
    @WithUserDetails("normal@example.com")
    @DisplayName("댓글에 등록된 좋아요가 삭제된 후, 댓글이 삭제되면 성공한다")
    void delete_reply_after_reply_heart_delete(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        Long replyId = 4L;

        //when
        replyService.deleteById(member, replyId);

        //then
        assertThat(replyRepository.findById(replyId)).isEmpty();
    }

    @Test
    @WithUserDetails("normal@example.com")
    @DisplayName("댓글에 등록된 대댓글의 좋아요가 삭제되고, 대댓글이 삭제되고, 댓글의 좋아요가 삭제된 후 댓글이 삭제되면 성공한다")
    void delete_reply_after_nested_reply_delete(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        Long replyId = 1L;
        List<Long> nestedReplyIds = replyRepository.findAllByParentId(1L)
                .stream().map(Reply::getId).toList();

        //when
        replyService.deleteById(member, replyId);

        //then
        assertThat(replyRepository.findById(replyId)).isEmpty();
        nestedReplyIds.forEach(id -> assertThat(replyRepository.findById(id)).isEmpty());
    }
}
