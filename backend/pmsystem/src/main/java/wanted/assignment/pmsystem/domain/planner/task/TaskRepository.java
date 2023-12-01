package wanted.assignment.pmsystem.domain.planner.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskBoxId(Long taskBoxId);

    List<Task> findByTaskBoxIdOrderByTaskOrderAsc(Long taskBoxId);

    @Query("SELECT MAX(t.taskOrder) FROM Task t WHERE t.taskBox.id = :taskBoxId")
    Long findMaxOrder(@Param("taskBoxId") Long taskBoxId);
}