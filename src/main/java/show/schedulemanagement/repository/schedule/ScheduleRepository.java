package show.schedulemanagement.repository.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    //findScheduleByMemberId
    List<Schedule> findByMemberId(Long memberId);

}