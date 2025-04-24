package com.eighttoten.community.repository.post;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.QPostEntity;
import com.eighttoten.community.QPostHeartEntity;
import com.eighttoten.community.QPostScrapEntity;
import com.eighttoten.community.QReplyEntity;
import com.eighttoten.community.QReplyHeartEntity;
import com.eighttoten.community.ReplyEntity;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchCond;
import com.eighttoten.community.domain.post.SearchPostPage;
import com.eighttoten.community.domain.post.SortCondition;
import com.eighttoten.member.QMemberEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final EntityManager em;

    private JPAQueryFactory query;
    private QPostEntity qPost;
    private QMemberEntity qMember;
    private QReplyEntity qReply;
    private QPostHeartEntity qPostHeart;
    private QPostScrapEntity qPostScrap;
    private QReplyHeartEntity qReplyHeart;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        init();
    }

    @Override
    public Pagination<PostPreview> searchPostPreviewPages(SearchPostPage cond) {
        String keyword = cond.getKeyword();
        String searchCond = cond.getSearchCond();

        List<PostPreview> contents = query
                .select(Projections.constructor(PostPreview.class,
                        qPost.id,
                        qPost.title,
                        qPost.contents,
                        qPost.memberEntity.createdBy.as("writer"),
                        qPost.memberEntity.nickname,
                        qPost.createdAt,
                        qPost.updatedAt,
                        qPost.totalLike,
                        qPost.totalScrap
                ))
                .from(qPost)
                .leftJoin(qPost.memberEntity, qMember)
                .where(containTitle(searchCond, keyword),
                        containContents(searchCond, keyword),
                        containWriter(searchCond, keyword)
                )
                .offset(cond.getOffset())
                .limit(cond.getPageSize())
                .orderBy(sortConds(cond.getSortCond(), cond.getSortDirection())).fetch();

        Long totalElements = query
                .select(qPost.count())
                .from(qPost)
                .leftJoin(qPost.memberEntity, qMember)
                .where(containTitle(searchCond, keyword),
                        containContents(searchCond, keyword),
                        containWriter(searchCond, keyword))
                .fetchOne();

        return new Pagination<>(contents, cond.getPageNum(), cond.getPageSize(), totalElements);
    }

    @Override
    public Optional<PostDetailInfo> searchPostDetailInfo(Long memberId, Long postId) {
        List<ReplyEntity> postReplies = query.select(qReply)
                .from(qReply)
                .leftJoin(qReply.memberEntity)
                .fetchJoin()
                .where(qReply.postEntity.id.eq(postId))
                .fetch();

        //클라이언트가 좋아요한 댓글들의 id
        List<Long> likedReplyIds = query.select(qReplyHeart.replyEntity.id)
                .from(qReplyHeart)
                .where(qReplyHeart.replyEntity.in(postReplies)
                        .and(qReplyHeart.memberEntity.id.eq(memberId)))
                .fetch();

        PostDetailInfo postDetailInfo = query.select(
                        Projections.fields(PostDetailInfo.class,
                                qPost.id,
                                qPost.title,
                                qPost.contents,
                                qMember.nickname,
                                qMember.email.as("writer"),
                                qPost.createdAt,
                                qPost.updatedAt,
                                qPost.totalLike,
                                qPost.totalScrap,
                                hasLike(memberId, postId).as("hasLike"),
                                hasScrap(memberId, postId).as("hasScrap")
                        )
                ).from(qPost, qPost)
                .leftJoin(qPost.memberEntity, qMember)
                .on(qPost.memberEntity.id.eq(qMember.id))
                .where(qPost.id.eq(postId))
                .fetchOne();

        if (postDetailInfo != null){
            postDetailInfo.setLikedReplyIds(likedReplyIds);
            postDetailInfo.setReplies(postReplies.stream().map(ReplyEntity::toReplyDetailInfo).toList());
        }

        return Optional.ofNullable(postDetailInfo);
    }

    private BooleanExpression hasScrap(Long memberId, Long postId) {
        return JPAExpressions.selectOne()
                .from(qPostScrap)
                .where(qPostScrap.postEntity.id.eq(postId)
                        .and(qPostScrap.memberEntity.id.eq(memberId))
                ).exists();
    }

    private BooleanExpression hasLike(Long memberId, Long postId) {
        return JPAExpressions.selectOne()
                .from(qPostHeart)
                .where(qPostHeart.postEntity.id.eq(postId)
                        .and(qPostHeart.memberEntity.id.eq(memberId))
                ).exists();
    }

    private OrderSpecifier<?>[] sortConds(String sortCond, String sortDirection){
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        Order direction = sortDirection.equals("ASC") ? Order.ASC : Order.DESC;

        if(sortCond.equals(SortCondition.LIKE.name())){
            orderSpecifiers.add(new OrderSpecifier<>(direction , qPost.totalLike));
        }
        else if (sortCond.equals(SortCondition.SCRAP.name())) {
            orderSpecifiers.add(new OrderSpecifier<>(direction, qPost.totalScrap));
        }
        else if (sortCond.equals(SortCondition.DATE.name())) {
            orderSpecifiers.add(new OrderSpecifier<>(direction, qPost.createdAt));
        }

        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, qPost.createdAt));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }


    private BooleanExpression containTitle(String cond, String title){
        if(cond.equals(SearchCond.TITLE.name()) && title != null){
            return qPost.title.contains(title);
        }
        return null;
    }

    private BooleanExpression containContents(String cond, String contents){
        if(cond.equals(SearchCond.CONTENTS.name()) && contents != null){
            return qPost.contents.contains(contents);
        }
        return null;
    }

    private BooleanExpression containWriter(String cond, String writer){
        if(cond.equals(SearchCond.WRITER.name()) && writer != null){
            return qPost.memberEntity.nickname.contains(writer);
        }
        return null;
    }

    private void init() {
        query = new JPAQueryFactory(em);
        qPost = QPostEntity.postEntity;
        qMember = QMemberEntity.memberEntity;
        qReply = QReplyEntity.replyEntity;
        qPostHeart = QPostHeartEntity.postHeartEntity;
        qPostScrap = QPostScrapEntity.postScrapEntity;
        qReplyHeart = QReplyHeartEntity.replyHeartEntity;
    }
}