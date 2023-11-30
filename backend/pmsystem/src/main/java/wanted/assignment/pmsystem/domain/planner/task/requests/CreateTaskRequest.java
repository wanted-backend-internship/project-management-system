package wanted.assignment.pmsystem.domain.planner.task.requests;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateTaskRequest {
    private Long boardId;
    private Long taskBoxId;
    private String taskTitle;
    private String tag;
    private LocalDate dueDate;
    private String workHour;
    private String createdBy;
    private Long taskOrder;
}
