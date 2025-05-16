package com.eighttoten.schedule.fschedule.repository;

import com.eighttoten.schedule.fschedule.FScheduleDetailEntity;
import com.eighttoten.schedule.fschedule.projection.FScheduleDetailProjection;
import java.time.LocalDateTime;
import java.util.List;

public interface FScheduleDetailCustomRepository {

    List<FScheduleDetailProjection> findAllWithParentByMemberEmailInPeriod(String memberEmail, LocalDateTime start, LocalDateTime end);
}
