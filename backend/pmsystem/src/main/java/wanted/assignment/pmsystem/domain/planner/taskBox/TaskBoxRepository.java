package wanted.assignment.pmsystem.domain.planner.taskBox;

import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskBoxRepository extends JpaRepository<TaskBox, Long> {
    List<TaskBox> findByBoardId(Long boardId);
    List<TaskBox> findByBoardIdOrderByBoxOrderAsc(Long boardId);
    @Query("SELECT MAX(t.boxOrder) FROM TaskBox t")
    Long findMaxOrder();

    @Modifying
    @Query("UPDATE TaskBox tb SET tb.boxOrder = :temporaryOrder WHERE tb.boxOrder = :currentOrder AND tb.board.id = :boardId")
    void updateTaskBoxOrderToTemporary(@Param("boardId") Long boardId, @Param("currentOrder") Long currentOrder, @Param("temporaryOrder") Long temporaryOrder);

    @Modifying
    @Query("UPDATE TaskBox tb SET tb.boxOrder = :prevTaskBoxOrder WHERE tb.boxOrder = :currentTaskBoxOrder AND tb.board.id = :boardId")
    void updatePrevTaskBoxOrder(@Param("boardId") Long boardId, @Param("currentTaskBoxOrder") Long currentTaskBoxOrder, @Param("prevTaskBoxOrder") Long prevTaskBoxOrder);

    @Modifying
    @Query("UPDATE TaskBox tb SET tb.boxOrder = :currentTaskBoxOrder WHERE tb.boxOrder = :prevTaskBoxOrder AND tb.board.id = :boardId")
    void updateCurrentTaskBoxOrder(@Param("boardId") Long boardId, @Param("prevTaskBoxOrder") Long prevTaskBoxOrder, @Param("currentTaskBoxOrder") Long currentTaskBoxOrder);
}
