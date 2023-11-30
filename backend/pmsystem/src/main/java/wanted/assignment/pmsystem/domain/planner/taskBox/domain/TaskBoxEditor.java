package wanted.assignment.pmsystem.domain.planner.taskBox.domain;

import lombok.Getter;

@Getter
public class TaskBoxEditor {
    private Long boxOrder;
    private String boxTitle;

    public TaskBoxEditor(Long boxOrder, String boxTitle) {
        this.boxOrder = boxOrder;
        this.boxTitle = boxTitle;
    }

    public static TaskBoxEditorBuilder builder() {
        return new TaskBoxEditorBuilder();
    }

    public static class TaskBoxEditorBuilder {
        private Long boxOrder;
        private String boxTitle;

        TaskBoxEditorBuilder() {

        }

        public TaskBoxEditorBuilder boxOrder(final Long boxOrder) {
            if (boxOrder != null) {
                this.boxOrder = boxOrder;
            }
            return this;
        }

        public TaskBoxEditorBuilder boxTitle(final String boxTitle) {
            if (boxTitle != null && boxTitle.isEmpty()) {
                this.boxTitle = boxTitle;
            }
            return this;
        }

        public TaskBoxEditor build() {
            return new TaskBoxEditor(boxOrder, boxTitle);
        }
    }
}
