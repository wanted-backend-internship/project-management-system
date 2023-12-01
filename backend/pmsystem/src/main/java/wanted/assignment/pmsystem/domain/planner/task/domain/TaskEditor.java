package wanted.assignment.pmsystem.domain.planner.task.domain;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class TaskEditor {
    private String taskTitle;
    private String tag;
    private LocalDate dueDate;
    private String workHour;

    public TaskEditor(String taskTitle,
     String tag,
     LocalDate dueDate,
     String workHour
    ) {
        this.taskTitle = taskTitle;
        this.tag = tag;
        this.dueDate = dueDate;
        this.workHour = workHour;
    }

    public static TaskEditorBuilder builder() {
        return new TaskEditorBuilder();
    }

    public static class TaskEditorBuilder {
        private String taskTitle;
        private String tag;
        private LocalDate dueDate;
        private String workHour;

        TaskEditorBuilder() {
        }

        public TaskEditorBuilder taskTitle(final String taskTitle) {
            if (taskTitle != null && !taskTitle.isEmpty()) {
                this.taskTitle = taskTitle;
            }
            return this;
        }

        public TaskEditorBuilder tag(final String tag) {
            if (tag != null && !tag.isEmpty()) {
                this.tag = tag;
            }
            return this;
        }

        public TaskEditorBuilder dueDate(final LocalDate dueDate) {
            if (dueDate != null) {
                this.dueDate = dueDate;
            }
            return this;
        }

        public TaskEditorBuilder workHour(final String workHour) {
            if (workHour != null && !workHour.isEmpty()) {
                this.workHour = workHour;
            }
            return this;
        }

        public TaskEditor build() {
            return new TaskEditor(
                    taskTitle,
                    tag,
                    dueDate,
                    workHour
            );
        }
    }
}
