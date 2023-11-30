package wanted.assignment.pmsystem.domain.planner.board.dto.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {
    private Long taskBoxId;
    private Long taskBoxOrder;
    private String taskBoxTitle;
    private List<Task> taskDetailResponses;
}
