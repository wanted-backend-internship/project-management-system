package wanted.assignment.pmsystem.domain.planner.task.requests;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UpdateTaskRequest {
    private Long taskId;
    private String title;
    private String tag;
    private LocalDate dueDate;
    private String workHour;
}
