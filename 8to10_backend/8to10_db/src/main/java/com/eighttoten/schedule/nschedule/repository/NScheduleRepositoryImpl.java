package com.eighttoten.schedule.nschedule.repository;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.nschedule.NScheduleEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NScheduleRepositoryImpl implements NScheduleRepository {
    private final NScheduleJpaRepository nScheduleRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public long save(NewNSchedule newNSchedule) {
        MemberEntity memberEntity = memberRepository.findById(newNSchedule.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        return nScheduleRepository.save(NScheduleEntity.from(newNSchedule, memberEntity)).getId();
    }

    @Override
    public void update(NSchedule nSchedule) {
        NScheduleEntity nScheduleEntity = nScheduleRepository.findById(nSchedule.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE));
        nScheduleEntity.update(nSchedule);
    }

    @Override
    public void deleteById(Long id) {
        nScheduleRepository.deleteById(id);
    }

    @Override
    public Optional<NSchedule> findById(Long id) {
        return nScheduleRepository.findById(id).map(NScheduleEntity::toNSchedule);
    }
}