package com.eighttoten.schedule.nschedule.repository;

import com.eighttoten.schedule.nschedule.NScheduleDetailEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NScheduleDetailJpaRepository extends JpaRepository<NScheduleDetailEntity, Long> {
    @Modifying
    @Query("delete from NScheduleDetailEntity nd where nd in :ids")
    void deleteAllByIds(@Param(value = "ids") List<Long> ids);

    @EntityGraph(attributePaths = "nScheduleEntity")
    @Query("select nd from NScheduleDetailEntity nd where nd.id = :id")
    Optional<NScheduleDetailEntity> findByIdWithParent(@Param(value = "id") Long id);

    @Query("select nd from NScheduleDetailEntity nd where nd.startDateTime >= :start and nd.nScheduleEntity.id = :parentId and nd.createdBy = :memberEmail")
    List<NScheduleDetailEntity> findAllByMemberEmailAndParentIdGEStart(@Param(value = "memberEmail") String memberEmail,
                                                                       @Param(value = "parentId") Long parentId,
                                                                       @Param(value = "start") LocalDateTime start);

    @Query("select nd from NScheduleDetailEntity nd where nd.createdBy = :memberEmail and DATE(nd.startDateTime) = :date")
    List<NScheduleDetailEntity> findAllByMemberEmailAndDate(@Param(value = "memberEmail") String memberEmail,
                                                            @Param(value = "date") LocalDate date);

    @Query("select nd from NScheduleDetailEntity nd where nd.id in :ids order by nd.id")
    List<NScheduleDetailEntity> findAllByIds(@Param(value = "ids") List<Long> ids);

    @Query("select nd from NScheduleDetailEntity nd where nd.startDateTime >= :startDateTime and nd.endDateTime <= :endDateTime and nd.createdBy = :memberEmail")
    List<NScheduleDetailEntity> findAllByMemberEmailInPeriod(@Param(value = "memberEmail") String memberEmail,
                                                             @Param(value = "startDateTime") LocalDateTime startDateTime,
                                                             @Param(value = "endDateTime") LocalDateTime endDateTime);

    @EntityGraph(attributePaths = "nScheduleEntity")
    @Query("select nd from NScheduleDetailEntity nd where nd.startDateTime >= :startDateTime and nd.endDateTime <= :endDateTime and nd.createdBy = :memberEmail")
    List<NScheduleDetailEntity> findAllWithParentByMemberEmailInPeriod(@Param(value = "memberEmail") String memberEmail,
                                                                       @Param(value = "startDateTime") LocalDateTime startDateTime,
                                                                        @Param(value = "endDateTime") LocalDateTime endDateTime);
}