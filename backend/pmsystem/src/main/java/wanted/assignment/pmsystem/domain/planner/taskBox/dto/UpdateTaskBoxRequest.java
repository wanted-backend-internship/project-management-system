package wanted.assignment.pmsystem.domain.planner.taskBox.dto;

import lombok.Getter;

@Getter
public class UpdateTaskBoxRequest {
    private Long taskBoxId;
    private String taskBoxTitle;
}
