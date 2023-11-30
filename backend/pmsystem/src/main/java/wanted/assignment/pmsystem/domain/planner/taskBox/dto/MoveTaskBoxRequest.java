package wanted.assignment.pmsystem.domain.planner.taskBox.dto;

import lombok.Getter;

@Getter
public class MoveTaskBoxRequest {
    private Long boardId;
    private Long prevTaskBoxOrder;
    private Long currentTaskBoxOrder;
}
