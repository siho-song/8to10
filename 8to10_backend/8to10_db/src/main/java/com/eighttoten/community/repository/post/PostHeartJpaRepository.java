package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostHeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostHeartJpaRepository extends JpaRepository<PostHeartEntity, Long> {
    @Modifying
    @Query("delete from PostHeartEntity h where h.postEntity.id = :postId")
    void deleteAllByPostId(@Param(value = "postId") Long postId);

    long deleteByMemberEntityIdAndPostEntityId(Long memberId, Long postId);
    boolean existsByMemberEntityIdAndPostEntityId(Long memberId, Long postId);
}