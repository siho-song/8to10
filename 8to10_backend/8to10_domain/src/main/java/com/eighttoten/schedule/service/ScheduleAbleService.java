package com.eighttoten.schedule.service;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.scheduleable.ScheduleAbleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleAbleService {
    private final ScheduleAbleRepository scheduleAbleRepository;

    public List<ScheduleAble> findAllByMemberEmailInPeriod(String memberEmail, LocalDateTime start, LocalDateTime end) {
        return scheduleAbleRepository.findAllByMemberEmailInPeriod(memberEmail,start,end);
    }

    public List<ScheduleAble> findAllWithParentFromRecentMonth(String memberEmail, int year, int month) {
        return scheduleAbleRepository.findAllWithParentFromRecentMonth(
                memberEmail, year, month).getScheduleAbles();
    }
}