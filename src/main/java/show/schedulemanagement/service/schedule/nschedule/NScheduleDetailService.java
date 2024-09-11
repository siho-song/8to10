package show.schedulemanagement.service.schedule.nschedule;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.repository.schedule.nschedule.NScheduleDetailRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NScheduleDetailService {

    private final NScheduleDetailRepository nScheduleDetailRepository;

    public NScheduleDetail findById(Long id){
        return nScheduleDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    public NScheduleDetail findByIdWithParent(Long id){
        return nScheduleDetailRepository.findByIdWithParent(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public void update(Member member, NScheduleDetailUpdate nScheduleDetailUpdate){
        NScheduleDetail nScheduleDetail = findById(nScheduleDetailUpdate.getId());
        if(!member.getEmail().equals(nScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
        }
        nScheduleDetail.update(nScheduleDetailUpdate);
    }

    @Transactional
    public void delete(NScheduleDetail nScheduleDetail){
        nScheduleDetailRepository.delete(nScheduleDetail);

    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NScheduleDetail nScheduleDetail = findByIdWithParent(id);
        if(!member.getEmail().equals(nScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
        }

        NSchedule parent = nScheduleDetail.getNSchedule();
        parent.updateTotalAmount(true, nScheduleDetail.getDailyAmount());

        delete(nScheduleDetail);
    }
}