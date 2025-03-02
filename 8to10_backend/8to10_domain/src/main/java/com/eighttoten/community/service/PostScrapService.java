package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.DUPLICATED_POST_SCRAP;

import com.eighttoten.community.domain.post.NewPostScrap;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.event.post.PostScrapAddEvent;
import com.eighttoten.community.event.post.PostScrapSubEvent;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostScrapService {
    private final PostScrapRepository postScrapRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void save(Member member, Long postId) {
        boolean hasScrap = postScrapRepository.existsByMemberIdAndPostId(member.getId(), postId);

        if(hasScrap){
            throw new DuplicatedException(DUPLICATED_POST_SCRAP);
        }

        postScrapRepository.save(NewPostScrap.from(member.getId(), postId));
        publisher.publishEvent(new PostScrapAddEvent(postId));
    }

    @Transactional
    public void deleteByMemberIdAndPostId(Long memberId, Long postId) {
        long deleteCount = postScrapRepository.deleteByMemberIdAndPostId(memberId, postId);
        if(deleteCount > 0) {
            publisher.publishEvent(new PostScrapSubEvent(postId));
        }
    }
}