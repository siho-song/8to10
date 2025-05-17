package com.eighttoten.schedule.service.nschedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.eighttoten.schedule.domain.scheduleable.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.TimeSlot;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("TimeSlot 서비스 단위 테스트")
public class TimeSlotServiceTest {

    @Autowired
    TimeSlotService timeSlotService;

    @Test
    @DisplayName("기간 내 날짜별로 일과 시간 사이에 일정이 들어갈수 있는 시간 슬롯들을 조회한다")
    void findAllBetweenStartAndEnd(){
        //given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 3, 0, 0);

        Map<LocalDate, List<ScheduleAble>> scheduleAbleMap = new HashMap<>();
        scheduleAbleMap.put(LocalDate.of(2024, 1, 1), new ArrayList<>());

        List<ScheduleAble> scheduleAbles = new ArrayList<>();
        scheduleAbles.add(createNDetail(LocalDateTime.of(2024, 1, 2, 10, 0), LocalDateTime.of(2024, 1, 2, 12, 0),
                LocalTime.of(1, 0)));
        scheduleAbles.add(createFDetail(LocalDateTime.of(2024, 1, 2, 13, 0), LocalDateTime.of(2024, 1, 2, 14, 0)));
        scheduleAbles.add(createVSchedule(LocalDateTime.of(2024, 1, 2, 15, 0), LocalDateTime.of(2024, 1, 2, 17, 0)));
        scheduleAbles.add(createFDetail(LocalDateTime.of(2024, 1, 2, 16, 30), LocalDateTime.of(2024, 1, 2, 17, 30)));

        scheduleAbleMap.put(LocalDate.of(2024, 1, 2), scheduleAbles);

        //when
        Map<LocalDate, List<TimeSlot>> slots = timeSlotService.findAllBetweenStartAndEnd(scheduleAbleMap, start, end);

        //then
        assertThat(slots).containsExactlyInAnyOrderEntriesOf(Map.of(
                LocalDate.of(2024, 1, 2), List.of(
                        new TimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0)),
                        new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                        new TimeSlot(LocalTime.of(14, 0), LocalTime.of(15, 0)),
                        new TimeSlot(LocalTime.of(17, 30), LocalTime.of(22, 0))
                ),
                LocalDate.of(2024, 1, 1), List.of(
                        new TimeSlot(LocalTime.of(8, 0), LocalTime.of(22, 0))
                )
        ));
    }

    @Test
    @DisplayName("일정이 들어갈 수 있도록 충분한 시간을 가진 요일별 공통 타임슬롯들을 찾는다.")
    void findCommonSlotsForEachDay(){
        //given
        Duration necessaryTime = Duration.ofHours(0).plusMinutes(20);
        Map<LocalDate, List<TimeSlot>> slotsForPeriodByDate = Map.of(
                LocalDate.of(2024, 1, 2), List.of(
                        new TimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0)),
                        new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                        new TimeSlot(LocalTime.of(14, 0), LocalTime.of(15, 30)),
                        new TimeSlot(LocalTime.of(17, 30), LocalTime.of(21, 0))
                ),
                LocalDate.of(2024, 1, 9), List.of(
                        new TimeSlot(LocalTime.of(8, 40), LocalTime.of(9, 30)),
                        new TimeSlot(LocalTime.of(12, 0), LocalTime.of(12, 30)),
                        new TimeSlot(LocalTime.of(15, 0), LocalTime.of(16, 0)),
                        new TimeSlot(LocalTime.of(21, 0), LocalTime.of(21, 34))
                ),
                LocalDate.of(2024, 1, 16), List.of(
                        new TimeSlot(LocalTime.of(8, 40), LocalTime.of(10, 0)),
                        new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                        new TimeSlot(LocalTime.of(14, 30), LocalTime.of(16, 0)),
                        new TimeSlot(LocalTime.of(21, 0), LocalTime.of(21, 34))
                )
        );

        //when
        Map<DayOfWeek, List<TimeSlot>> slots = timeSlotService.findCommonSlotsForEachDay(slotsForPeriodByDate, necessaryTime);

        //then
        assertThat(slots).containsExactlyInAnyOrderEntriesOf(Map.of(
                DayOfWeek.TUESDAY, List.of(
                        new TimeSlot(LocalTime.of(8, 40), LocalTime.of(9, 0)),
                        new TimeSlot(LocalTime.of(12, 0), LocalTime.of(12, 30)),
                        new TimeSlot(LocalTime.of(15, 0), LocalTime.of(15, 30))
                )));
    }

    @Test
    @DisplayName("각 요일별로 타임슬롯들 중 가장 빠른 타임슬롯을 선택한다.")
    void selectEarlierTimeSlot(){
        //given
        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
        Map<DayOfWeek, List<TimeSlot>> slotsByDay = new HashMap<>();
        slotsByDay.put(DayOfWeek.MONDAY, List.of(new TimeSlot(LocalTime.of(8, 30), LocalTime.of(9, 30)),
                new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 30))));

        slotsByDay.put(DayOfWeek.TUESDAY, List.of(new TimeSlot(LocalTime.of(10, 30), LocalTime.of(10, 40)),
                new TimeSlot(LocalTime.of(10, 40), LocalTime.of(10, 50))));

        //when
        Map<DayOfWeek, TimeSlot> dayOfWeekTimeSlotMap = timeSlotService.selectEarlierTimeSlot(days, slotsByDay);

        //then
        assertThat(dayOfWeekTimeSlotMap).containsExactlyInAnyOrderEntriesOf(Map.of(
                DayOfWeek.TUESDAY, new TimeSlot(LocalTime.of(10, 30), LocalTime.of(10, 40)),
                DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(8, 30), LocalTime.of(9, 30))));
    }

    private ScheduleAble createNDetail(LocalDateTime start, LocalDateTime end, LocalTime bufferTime){
        return new NScheduleDetail(null, null, null, start, end,
                bufferTime, null, false, 0, 0);
    }

    private ScheduleAble createFDetail(LocalDateTime start, LocalDateTime end){
        return new FScheduleDetail(null, null, null, start, end,
                null);
    }

    private ScheduleAble createVSchedule(LocalDateTime start, LocalDateTime end){
        return new VSchedule(null, null, null, start,
                end,null);
    }
}