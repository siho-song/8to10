package com.eighttoten.schedule.vschedule.repository;

import com.eighttoten.schedule.vschedule.VScheduleEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VScheduleJpaRepository extends JpaRepository<VScheduleEntity, Long> {
    @Query("select v from VScheduleEntity v where v.startDateTime >= :startDateTime and v.endDateTime <= :endDateTime and v.createdBy = :memberEmail")
    List<VScheduleEntity> findAllByMemberEmailBetweenStartAndEnd(@Param(value = "memberEmail") String memberEmail,
                                                                 @Param(value = "startDateTime") LocalDateTime startDateTime,
                                                                 @Param(value = "endDateTime") LocalDateTime endDateTime);

    @Query("select v from VScheduleEntity v where v.createdBy = :memberEmail")
    List<VScheduleEntity> findAllByMemberEmail(@Param(value = "memberEmail") String memberEmail);
}
