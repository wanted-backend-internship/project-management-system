package wanted.assignment.pmsystem.domain.planner.taskBox.dto;

import lombok.Getter;

@Getter
public class CreateTaskBoxRequest {
    private String taskBoxTitle;
    private Long boardId;
}
