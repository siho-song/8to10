package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostEntity;
import com.eighttoten.community.PostHeartEntity;
import com.eighttoten.community.domain.post.NewPostHeart;
import com.eighttoten.community.domain.post.repository.PostHeartRepository;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostHeartRepositoryImpl implements PostHeartRepository {
    private final PostHeartJpaRepository postHeartRepository;
    private final PostJpaRepository postRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public void save(NewPostHeart newPostHeart) {
        MemberEntity memberEntity = memberRepository.findById(newPostHeart.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        PostEntity postEntity = postRepository.findById(newPostHeart.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_POST));

        postHeartRepository.save(PostHeartEntity.from(memberEntity, postEntity));
    }

    @Override
    public long deleteByMemberIdAndPostId(Long memberId, Long postId) {
        return postHeartRepository.deleteByMemberEntityIdAndPostEntityId(memberId, postId);
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        postHeartRepository.deleteAllByPostId(postId);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return postHeartRepository.existsByMemberEntityIdAndPostEntityId(memberId, postId);
    }
}
