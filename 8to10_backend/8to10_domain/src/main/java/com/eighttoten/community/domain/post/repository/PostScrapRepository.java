package com.eighttoten.community.domain.post.repository;

import com.eighttoten.community.domain.post.NewPostScrap;
import com.eighttoten.community.domain.post.PostScrap;
import java.util.List;

public interface PostScrapRepository {
    void save(NewPostScrap newPostScrap);
    void deleteAllByPostId(Long postId);
    long deleteByMemberIdAndPostId(Long memberId, Long postId);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
    List<PostScrap> findAllByMemberIdWithPost(Long memberId);
}