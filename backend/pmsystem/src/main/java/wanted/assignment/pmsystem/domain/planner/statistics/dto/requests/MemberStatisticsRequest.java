package wanted.assignment.pmsystem.domain.planner.statistics.dto.requests;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class MemberStatisticsRequest {
    private List<Long> taskBoxIds;
    private List<String> memberNames;
    @Nullable
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
}
