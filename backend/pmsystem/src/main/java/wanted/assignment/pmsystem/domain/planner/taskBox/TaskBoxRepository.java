package wanted.assignment.pmsystem.domain.planner.taskBox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskBoxRepository extends JpaRepository<TaskBox, Long> {
    List<TaskBox> findByBoardId(Long boardId);
    TaskBox findByBoxOrder(Long boxOrder);

    @Modifying
    @Query("UPDATE TaskBox tb SET tb.boxOrder = :currentTaskBoxOrder WHERE tb.boxOrder = :prevTaskBoxOrder AND tb.board.id = :boardId")
    void updatePrevTaskBoxOrder(Long boardId, Long prevTaskBoxOrder, Long currentTaskBoxOrder);

    @Modifying
    @Query("UPDATE TaskBox tb SET tb.boxOrder = :prevTaskBoxOrder WHERE tb.boxOrder = :currentTaskBoxOrder AND tb.board.id = :boardId")
    void updateCurrentTaskBoxOrder(Long boardId, Long currentTaskBoxOrder, Long prevTaskBoxOrder);
}
