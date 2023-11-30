package wanted.assignment.pmsystem.domain.planner.task.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String tag;
    private LocalDate dueDate;
    private String workHour;
    private String createdBy;
    private Long taskOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_box_id")
    private TaskBox taskBox;

    public TaskEditor.TaskEditorBuilder toUpdate() {
        return TaskEditor.builder()
                .taskTitle(title)
                .tag(tag)
                .dueDate(dueDate)
                .workHour(workHour)
                .createdBy(createdBy);
    }

    public void update(TaskEditor taskEditor) {
        title = taskEditor.getTaskTitle();
        tag = taskEditor.getTag();
        dueDate = taskEditor.getDueDate();
        workHour = taskEditor.getWorkHour();
        createdBy = taskEditor.getCreatedBy();
    }
}
