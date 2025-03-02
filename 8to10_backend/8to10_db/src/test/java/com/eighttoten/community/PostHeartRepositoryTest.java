package com.eighttoten.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.community.domain.post.NewPostHeart;
import com.eighttoten.community.domain.post.repository.PostHeartRepository;
import com.eighttoten.community.repository.post.PostHeartRepositoryImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("게시글 좋아요 레포지토리 테스트")
@Import(PostHeartRepositoryImpl.class)
public class PostHeartRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    PostHeartRepository postHeartRepository;

    @Test
    @DisplayName("게시글 좋아요를 저장한다.")
    void save(){
        //given
        NewPostHeart newPostHeart = NewPostHeart.from(1L, 3L);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(() -> postHeartRepository.save(newPostHeart)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("멤버의 id, 게시글의 id로 게시글 좋아요가 있는지 확인한다.")
    void existsByMemberIdAndPostId(){
        //given
        Long memberId = 1L;
        Long postId = 2L;

        //when
        boolean isTrue = postHeartRepository.existsByMemberIdAndPostId(memberId, postId);

        //then
        assertThat(isTrue).isTrue();
    }

    @Test
    @DisplayName("게시글의 id로 게시글에 저장된 게시글 좋아요들을 모두 삭제한다.")
    void deleteAllByPostId(){
        //given
        Long postId = 1L;
        Long memberId = 1L;

        //when,then
        assertThatCode(() -> postHeartRepository.deleteAllByPostId(postId)).doesNotThrowAnyException();
        assertThat(postHeartRepository.existsByMemberIdAndPostId(memberId,postId)).isFalse();
    }

    @Test
    @DisplayName("멤버의 id, 게시글의 id로 게시글 좋아요를 삭제한다.")
    void deleteByMemberIdAndPostId(){
        //given
        Long memberId = 2L;
        Long postId = 1L;

        //when
        long deletedCount = postHeartRepository.deleteByMemberIdAndPostId(memberId, postId);

        //then
        assertThat(deletedCount).isGreaterThan(0);
    }
}