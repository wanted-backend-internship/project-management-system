package wanted.assignment.pmsystem.domain.planner.task.requests;

import lombok.Getter;

@Getter
public class MoveTaskSameTaskBoxRequest {
    private Long taskBoxId;
    private Long taskId;
    private Long newTaskOrder;
}
