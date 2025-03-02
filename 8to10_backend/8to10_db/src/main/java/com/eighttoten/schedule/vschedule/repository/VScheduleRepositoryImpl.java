package com.eighttoten.schedule.vschedule.repository;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import com.eighttoten.schedule.vschedule.VScheduleEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VScheduleRepositoryImpl implements VScheduleRepository {
    private final VScheduleJpaRepository vScheduleRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public void save(NewVSchedule newVSchedule) {
        MemberEntity memberEntity = memberRepository.findById(newVSchedule.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        vScheduleRepository.save(VScheduleEntity.from(newVSchedule, memberEntity));
    }

    @Override
    public void update(VSchedule vSchedule) {
        VScheduleEntity entity = vScheduleRepository.findById(vSchedule.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_V_SCHEDULE));

        entity.update(vSchedule.getTitle(), vSchedule.getCommonDescription(),
                vSchedule.getStartDateTime(), vSchedule.getEndDateTime());
    }

    @Override
    public void deleteById(Long id) {
        vScheduleRepository.deleteById(id);
    }

    @Override
    public Optional<VSchedule> findById(Long id) {
        return vScheduleRepository.findById(id).map(VScheduleEntity::toVSchedule);
    }

    @Override
    public List<VSchedule> findAllByMemberEmail(String memberEmail) {
        List<VScheduleEntity> entities = vScheduleRepository.findAllByMemberEmail(memberEmail);
        return entities.stream().map(VScheduleEntity::toVSchedule).toList();
    }

    @Override
    public List<VSchedule> findAllByMemberEmailBetweenStartAndEnd(String email, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<VScheduleEntity> entities = vScheduleRepository.findAllByMemberEmailBetweenStartAndEnd(email, startDateTime, endDateTime);
        return entities.stream().map(VScheduleEntity::toVSchedule).toList();
    }
}