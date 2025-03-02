package com.eighttoten.schedule.service;

import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDateTime;
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
    public List<ScheduleAble> findAllByMemberEmailBetweenStartAndEnd(String memberEmail, LocalDateTime start, LocalDateTime end) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmailBetweenStartAndEnd(memberEmail, start, end));
        allSchedules.addAll(fScheduleDetailRepository.findAllByMemberEmailBetweenStartAndEnd(memberEmail, start, end));
        allSchedules.addAll(nScheduleDetailRepository.findAllByMemberEmailBetweenStartAndEnd(memberEmail, start, end));
        return allSchedules;
    }

    @Transactional(readOnly = true)
    public List<ScheduleAble> findAllWithParentByMemberEmail(String memberEmail) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmail(memberEmail));
        allSchedules.addAll(fScheduleDetailRepository.findAllWithParentByMemberEmail(memberEmail));
        allSchedules.addAll(nScheduleDetailRepository.findAllWithParentByMemberEmail(memberEmail));
        return allSchedules;
    }
}