package wanted.assignment.pmsystem.domain.planner.task.requests;

import lombok.Getter;

@Getter
public class MoveTaskOtherTaskBoxRequest {
    private Long prevTaskBoxId;
    private Long newTaskBoxId;
    private Long prevTaskOrder;
    private Long newTaskOrder;
    private Long taskId;
}
