package com.eighttoten.community.repository;

import com.eighttoten.community.ReplyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {
    @Modifying
    @Query("delete from ReplyEntity r where r in :replies")
    void deleteAllByReplyIds(@Param(value = "replies") List<Long> replies);

    List<ReplyEntity> findAllByMemberEntityId(Long memberId);
    List<ReplyEntity> findAllByPostEntityId(Long postId);

    @Query("select r from ReplyEntity r where r.parentId = :parentReplyId")
    List<ReplyEntity> findAllByParentReplyId(@Param(value = "parentReplyId") Long parentReplyId);
}