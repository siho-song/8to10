package show.schedulemanagement.service.achievement;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.repository.achievement.AchievementRepository;
import show.schedulemanagement.repository.schedule.nschedule.NScheduleDetailRepository;
import show.schedulemanagement.service.event.ProgressUpdatedEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final NScheduleDetailRepository nScheduleDetailRepository;

    public Achievement findByMemberAndDateIfExists(Member member, LocalDate date){
        return achievementRepository.findByMemberAndAchievementDate(member, date)
                .orElse(null);
    }
    
    public List<Achievement> findAllBetweenYearAndMonth(Member member, int year, int month){
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());

        return achievementRepository.findAllBetweenStartAndEnd(member, start, end);
    }

    public List<Achievement> findAllByDateWithMember(LocalDate date) {
        return achievementRepository.findAllByDateWithMember(date);
    }

    @EventListener
    public void handleProgressUpdate(ProgressUpdatedEvent event) {
        LocalDate date = event.getDate();
        Member member = event.getMember();

        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByDateAndEmail(
                date,
                member.getEmail());

        Achievement achievement = achievementRepository.findByMemberAndAchievementDate(member, date)
                .orElse(Achievement.createAchievement(member, date, getAchievementRate(nScheduleDetails)));

        achievementRepository.save(achievement);
    }

    private double getAchievementRate(List<NScheduleDetail> nScheduleDetails){
        double achievementSum = nScheduleDetails.stream()
                .mapToDouble(NScheduleDetail::getAchievementRate)
                .sum();

        int size = nScheduleDetails.size();

        return achievementSum / size;
    }
}
