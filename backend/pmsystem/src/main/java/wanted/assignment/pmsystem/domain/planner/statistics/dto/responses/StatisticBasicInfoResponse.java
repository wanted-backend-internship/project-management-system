package wanted.assignment.pmsystem.domain.planner.statistics.dto.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.detail.MemberDetail;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.detail.TaskBoxDetail;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticBasicInfoResponse {
    private List<TaskBoxDetail> taskBoxDetails;
    private List<MemberDetail> memberDetails;
}
