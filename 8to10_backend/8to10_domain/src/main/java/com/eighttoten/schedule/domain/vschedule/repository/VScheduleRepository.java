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

    List<VSchedule> findAllByMemberEmail(String memberEmail);
    List<VSchedule> findAllByMemberEmailBetweenStartAndEnd(String email, LocalDateTime startDateTime, LocalDateTime endDateTime);
}