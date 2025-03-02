package com.eighttoten.community.domain.post.repository;

import com.eighttoten.community.domain.post.NewPostHeart;

public interface PostHeartRepository {
    void save(NewPostHeart newPostHeart);
    void deleteAllByPostId(Long postId);
    long deleteByMemberIdAndPostId(Long memberId, Long postId);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}