package wanted.assignment.pmsystem.domain.planner.statistics.dto.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatisticsResponse {
    private String memberName;
    private List<Object[]> results;
}
