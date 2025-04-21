package com.eighttoten.schedule.service;

import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleAbleService {
    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final FScheduleDetailRepository fScheduleDetailRepository;
    private final VScheduleRepository vScheduleRepository;

    @Transactional(readOnly = true)
    public List<ScheduleAble> findAllByMemberEmailInPeriod(String memberEmail, LocalDateTime start, LocalDateTime end) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(fScheduleDetailRepository.findAllByMemberEmailInPeriod(memberEmail, start, end));
        allSchedules.addAll(nScheduleDetailRepository.findAllByMemberEmailInPeriod(memberEmail, start, end));
        return allSchedules;
    }

    @Transactional(readOnly = true)
    public List<ScheduleAble> findAllWithParentFromRecentMonth(String memberEmail, int year, int month) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        LocalDateTime end = LocalDateTime.of(LocalDate.of(year, 1, 1).plusMonths(month), LocalTime.of(0, 0));
        LocalDateTime start = end.minusMonths(3);
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmailInPeriod(memberEmail,start,end));
        allSchedules.addAll(fScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail,start,end));
        allSchedules.addAll(nScheduleDetailRepository.findAllWithParentByMemberEmailInPeriod(memberEmail,start,end));
        return allSchedules;
    }
}