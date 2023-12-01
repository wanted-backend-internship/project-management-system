package wanted.assignment.pmsystem.domain.planner.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TaskMoveRepository {
    // TODO 뭔가 로직 잘못 세운듯 계속 음수나옴 -> 수정하기

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void moveTaskToSameTaskBox(Long taskBoxId, Long taskId, Long newTaskOrder) {
        em.createQuery(
                        "UPDATE Task t SET t.taskOrder = t.taskOrder - 1 WHERE t.taskBox.id = :taskBoxId AND t.taskOrder <= :newTaskOrder")
                .setParameter("taskBoxId", taskBoxId)
                .setParameter("newTaskOrder", newTaskOrder)
                .executeUpdate();

        em.createQuery("UPDATE Task t SET t.taskOrder = :newTaskOrder WHERE t.id = :taskId")
                .setParameter("newTaskOrder", newTaskOrder)
                .setParameter("taskId", taskId)
                .executeUpdate();
    }

    @Transactional
    public void moveTaskToOtherTaskBox(Long prevTaskBoxId, Long newTaskBoxId, Long taskId, Long prevTaskOrder, Long newTaskOrder) {
        // 새로운 TaskBox로 Task를 이동시키기 전에, 새로운 TaskBox 내에서 순서를 조정합니다.
        em.createQuery(
                        "UPDATE Task t SET t.taskOrder = t.taskOrder - 1 WHERE t.taskBox.id = :newTaskBoxId AND t.taskOrder <= :newTaskOrder")
                .setParameter("newTaskBoxId", newTaskBoxId)
                .setParameter("newTaskOrder", newTaskOrder)
                .executeUpdate();

        // Task를 새로운 TaskBox로 이동시키고 순서를 업데이트합니다.
        em.createQuery(
                        "UPDATE Task t SET t.taskBox.id = :newTaskBoxId, t.taskOrder = :newTaskOrder WHERE t.id = :taskId")
                .setParameter("newTaskBoxId", newTaskBoxId)
                .setParameter("newTaskOrder", newTaskOrder)
                .setParameter("taskId", taskId)
                .executeUpdate();

        // 이전 TaskBox 내에서 순서를 재조정합니다.
        em.createQuery(
                        "UPDATE Task t SET t.taskOrder = t.taskOrder - 1 WHERE t.taskBox.id = :prevTaskBoxId AND t.taskOrder > :prevTaskOrder")
                .setParameter("prevTaskBoxId", prevTaskBoxId)
                .setParameter("prevTaskOrder", prevTaskOrder)
                .executeUpdate();
    }
}
