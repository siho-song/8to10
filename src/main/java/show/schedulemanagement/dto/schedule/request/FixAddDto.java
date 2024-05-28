package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import show.schedulemanagement.validator.schedule.filedError.UniqueDayList;
import show.schedulemanagement.validator.schedule.objectError.StartBeforeEnd;

@SuperBuilder
@Getter
@StartBeforeEnd
@NoArgsConstructor
@ToString(callSuper = true)
@Slf4j
public class FixAddDto extends ScheduleAddDto implements DateRangeValidatable{
    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private List<FixDetailAddDto> events;

    @Override
    public LocalDateTime takeStartDateTime() {
        return startDate.atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDate.atStartOfDay();
    }
}