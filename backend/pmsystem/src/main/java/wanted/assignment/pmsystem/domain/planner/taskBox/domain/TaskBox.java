package wanted.assignment.pmsystem.domain.planner.taskBox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.board.domain.Board;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class TaskBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long boxOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    public TaskBoxEditor.TaskBoxEditorBuilder toUpdate() {
        return TaskBoxEditor.builder()
                .boxTitle(title)
                .boxOrder(boxOrder);
    }

    public void update(TaskBoxEditor taskBoxEditor) {
        title = taskBoxEditor.getBoxTitle();
        boxOrder = taskBoxEditor.getBoxOrder();
    }
}
