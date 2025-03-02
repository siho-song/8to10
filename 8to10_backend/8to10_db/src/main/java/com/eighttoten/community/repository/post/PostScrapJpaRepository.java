package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostScrapEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostScrapJpaRepository extends JpaRepository<PostScrapEntity, Long> {
    boolean existsByMemberEntityIdAndPostEntityId(Long memberId, Long postId);
    long deleteByMemberEntityIdAndPostEntityId(Long memberId, Long postId);

    @Modifying
    @Query("delete from PostScrapEntity s where s.postEntity.id = :postId")
    void deleteAllByPostId(@Param(value = "postId") Long postId);

    @EntityGraph(attributePaths = "postEntity")
    @Query("select s from PostScrapEntity s where s.memberEntity.id = :memberId")
    List<PostScrapEntity> findAllByMemberIdWithPost(@Param(value = "memberId") Long memberId);
}