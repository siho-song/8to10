package com.eighttoten.schedule.domain.scheduleable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleAbleRepository {
    List<ScheduleAble> findAllByMemberEmailInPeriod(String memberEmail, LocalDateTime start, LocalDateTime end);
    ScheduleAbles findAllWithParentFromRecentMonth(String memberEmail, int year, int month);
}
