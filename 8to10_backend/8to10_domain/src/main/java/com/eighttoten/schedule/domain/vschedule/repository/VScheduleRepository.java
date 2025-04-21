package com.eighttoten.schedule.domain.vschedule.repository;

import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VScheduleRepository {
    void save(NewVSchedule newVSchedule);
    void update(VSchedule vSchedule);
    void deleteById(Long id);

    Optional<VSchedule> findById(Long id);

    List<VSchedule> findAllByEmailInPeriod(String memberEmail);
    List<VSchedule> findAllByMemberEmailInPeriod(String email, LocalDateTime startDateTime, LocalDateTime endDateTime);
}