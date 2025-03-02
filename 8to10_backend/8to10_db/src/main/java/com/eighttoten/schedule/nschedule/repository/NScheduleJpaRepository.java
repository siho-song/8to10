package com.eighttoten.schedule.nschedule.repository;

import com.eighttoten.schedule.nschedule.NScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NScheduleJpaRepository extends JpaRepository<NScheduleEntity, Long> {

}