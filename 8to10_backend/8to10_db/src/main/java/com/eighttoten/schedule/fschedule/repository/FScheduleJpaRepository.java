package com.eighttoten.schedule.fschedule.repository;


import com.eighttoten.schedule.fschedule.FScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FScheduleJpaRepository extends JpaRepository<FScheduleEntity, Long> {
}