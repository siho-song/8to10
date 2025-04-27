package com.eighttoten.schedule.scheduleable;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAbleRepository;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAbles;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleAbleRepositoryImpl implements ScheduleAbleRepository {
    private final VScheduleRepository vScheduleRepository;
    private final FScheduleDetailRepository fScheduleDetailRepository;
    private final NScheduleDetailRepository nScheduleDetailRepository;

    @Override
    public List<ScheduleAble> findAllByMemberEmailInPeriod(String memberEmail, LocalDateTime start, LocalDateTime end) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(fScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(nScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail, start, end));
        return allSchedules;
    }

    @Cacheable(cacheNames = "scheduleAbles", key = "'schedule:' + #memberEmail + ':' + #year + ':' + #month")
    public ScheduleAbles findAllWithParentFromRecentMonth(String memberEmail, int year, int month) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        LocalDateTime end = LocalDateTime.of(LocalDate.of(year, 1, 1).plusMonths(month), LocalTime.of(0, 0));
        LocalDateTime start = end.minusMonths(3);
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(fScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(nScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail, start, end));
        return new ScheduleAbles(allSchedules);
    }
}