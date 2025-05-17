package com.eighttoten.schedule.fschedule.repository;

import com.eighttoten.schedule.fschedule.QFScheduleDetailEntity;
import com.eighttoten.schedule.fschedule.QFScheduleEntity;
import com.eighttoten.schedule.fschedule.projection.FScheduleDetailProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class FScheduleDetailCustomRepositoryImpl implements FScheduleDetailCustomRepository {
    private final EntityManager em;

    private JPAQueryFactory query;
    private QFScheduleDetailEntity fd;
    private QFScheduleEntity f;

    public FScheduleDetailCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        init();
    }

    @Override
    public List<FScheduleDetailProjection> findAllWithParentByMemberEmailInPeriod(String memberEmail, LocalDateTime start,
                                                                                  LocalDateTime end) {

        return query.select(Projections.constructor(FScheduleDetailProjection.class,
                        fd.id,
                        f.id.as("parentId"),
                        f.title,
                        f.commonDescription,
                        fd.startDateTime,
                        fd.endDateTime,
                        fd.detailDescription))
                .from(fd)
                .join(fd.fScheduleEntity, f)
                .where(
                        fd.createdBy.eq(memberEmail),
                        fd.startDateTime.goe(start),
                        fd.endDateTime.loe(end)
                ).fetch();
    }

    private void init(){

        query = new JPAQueryFactory(em);
        fd = QFScheduleDetailEntity.fScheduleDetailEntity;
        f = QFScheduleEntity.fScheduleEntity;

    }

}
