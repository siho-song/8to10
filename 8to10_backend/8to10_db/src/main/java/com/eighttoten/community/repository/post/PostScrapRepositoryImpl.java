package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostEntity;
import com.eighttoten.community.PostScrapEntity;
import com.eighttoten.community.domain.post.NewPostScrap;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostScrapRepositoryImpl implements PostScrapRepository {
    private final PostScrapJpaRepository postScrapRepository;
    private final PostJpaRepository postRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public void save(NewPostScrap newPostScrap) {
        PostEntity postEntity = postRepository.findById(newPostScrap.getPostId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_POST));

        MemberEntity memberEntity = memberRepository.findById(newPostScrap.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        postScrapRepository.save(PostScrapEntity.from(postEntity, memberEntity));
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        postScrapRepository.deleteAllByPostId(postId);
    }

    @Override
    public long deleteByMemberIdAndPostId(Long memberId, Long postId) {
        return postScrapRepository.deleteByMemberEntityIdAndPostEntityId(memberId, postId);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return postScrapRepository.existsByMemberEntityIdAndPostEntityId(memberId, postId);
    }

    @Override
    public List<PostScrap> findAllByMemberIdWithPost(Long memberId) {
        return postScrapRepository.findAllByMemberIdWithPost(memberId).stream().map(PostScrapEntity::toPostScrap).toList();
    }
}