package com.eighttoten.schedule.fschedule.repository;

import com.eighttoten.schedule.fschedule.FScheduleDetailEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FScheduleDetailJpaRepository extends JpaRepository<FScheduleDetailEntity, Long> {
    @Modifying
    @Query("delete from FScheduleDetailEntity fd where fd.id in :ids")
    void deleteAllByIds(@Param(value = "ids") List<Long> ids);

    @Query("select fd from FScheduleDetailEntity fd where fd.startDateTime >= :start and fd.createdBy = :memberEmail and fd.fScheduleEntity.id = :parentId")
    List<FScheduleDetailEntity> findAllByMemberEmailAndParentIdGEStart(@Param(value = "memberEmail") String memberEmail,
                                                                       @Param(value = "parentId") Long parentId,
                                                                       @Param(value = "start") LocalDateTime start);

    @Query("select fd from FScheduleDetailEntity fd where fd.startDateTime >= :start and fd.endDateTime <= :end and fd.createdBy = :memberEmail")
    List<FScheduleDetailEntity> findAllByMemberEmailBetweenStartAndEnd(@Param(value = "memberEmail") String memberEmail,
                                                                       @Param(value = "start") LocalDateTime start,
                                                                       @Param(value = "end") LocalDateTime end);

    @EntityGraph(attributePaths = "fScheduleEntity")
    @Query("select fd from FScheduleDetailEntity fd where fd.startDateTime >= :start and fd.endDateTime <= :end and fd.createdBy = :memberEmail")
    List<FScheduleDetailEntity> findAllWithParentByMemberEmailInPeriod(@Param(value = "memberEmail") String memberEmail,
                                                                       @Param(value = "start") LocalDateTime start,
                                                                       @Param(value = "end") LocalDateTime end);
}