package com.eighttoten.schedule.domain.fschedule.repository;

import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFDetail;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FScheduleDetailRepository {
    void save(NewFDetail newFDetail);
    void update(FScheduleDetail fScheduleDetail);
    void deleteById(Long id);
    void deleteAllByIds(List<Long> ids);
    Optional<FScheduleDetail> findById(Long id);
    List<FScheduleDetail> findAllByMemberEmailBetweenStartAndEnd(String email, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<FScheduleDetail> findAllByMemberEmailAndParentIdGEStart(String email, Long parentId, LocalDateTime start);
    List<FDetailWithParent> findAllWithParentByMemberEmail(String email);
}