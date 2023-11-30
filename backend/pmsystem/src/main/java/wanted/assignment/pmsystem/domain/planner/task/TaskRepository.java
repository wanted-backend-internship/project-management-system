package wanted.assignment.pmsystem.domain.planner.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskBoxId(Long taskBoxId);

    @Modifying
    @Query("UPDATE Task t SET t.taskBox.id = :newTaskBoxId, t.taskOrder = :taskOrder WHERE t.id = :taskId")
    void moveTaskToNewTaskBox(Long taskId, Long newTaskBoxId, Long taskOrder);

    @Modifying
    @Query("UPDATE Task t SET t.taskOrder = t.taskOrder + 1 WHERE t.taskBox.id = :taskBoxId AND t.taskOrder >= :taskOrder")
    void incrementTaskOrderInTaskBox(Long taskBoxId, Long taskOrder);
}
