package wanted.assignment.pmsystem.domain.planner.statistics.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskBoxDetail {
    private String taskBoxTitle;
    private Long taskBoxId;
}
