package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.DUPLICATED_POST_HEART;

import com.eighttoten.community.domain.post.NewPostHeart;
import com.eighttoten.community.domain.post.repository.PostHeartRepository;
import com.eighttoten.community.event.post.PostHeartAddEvent;
import com.eighttoten.community.event.post.PostHeartSubEvent;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostHeartService {
    private final PostHeartRepository postHeartRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void save(Member member, Long postId) {
        boolean hasLiked = postHeartRepository.existsByMemberIdAndPostId(member.getId(), postId);

        if(hasLiked){
            throw new DuplicatedException(DUPLICATED_POST_HEART);
        }

        postHeartRepository.save(NewPostHeart.from(member.getId(), postId));
        publisher.publishEvent(new PostHeartAddEvent(postId));
    }

    @Transactional
    public void deleteByMemberIdAndPostId(Long memberId, Long postId) {
        long deleteCount = postHeartRepository.deleteByMemberIdAndPostId(memberId, postId);
        if (deleteCount > 0) {
            publisher.publishEvent(new PostHeartSubEvent(postId));
        }
    }
}