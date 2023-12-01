package wanted.assignment.pmsystem.domain.planner.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import wanted.assignment.pmsystem.domain.planner.task.domain.TaskEditor;
import wanted.assignment.pmsystem.domain.planner.task.requests.CreateTaskRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.DeleteTaskRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.MoveTaskOtherTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.MoveTaskSameTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.UpdateTaskRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.TaskBoxRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import wanted.assignment.pmsystem.domain.user.User;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;
import wanted.assignment.pmsystem.global.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskBoxRepository taskBoxRepository;
    private final AuthUtil authUtil;
    private final TaskMoveRepository taskMoveRepository;

    @Transactional
    public void createTask (CreateTaskRequest request) {
        User user = authUtil.getLoginUser();

        Long maxOrder = taskRepository.findMaxOrder(request.getTaskBoxId());
        long nextOrder = (maxOrder != null) ? maxOrder + 1 : 1L;

        TaskBox taskBox = taskBoxRepository.findById(request.getTaskBoxId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_BOX_NOT_EXIST));

        Task task = Task.builder()
                .taskOrder(nextOrder)
                .title(request.getTaskTitle())
                .tag(request.getTag())
                .createdBy(user.getUsername())
                .workHour(request.getWorkHour())
                .dueDate(request.getDueDate())
                .taskBox(taskBox)
                .build();

        taskRepository.save(task);
    }

    @Transactional
    public void moveTaskToOtherTaskBox (MoveTaskOtherTaskBoxRequest request) {
        taskMoveRepository.moveTaskToOtherTaskBox(request.getPrevTaskBoxId(), request.getNewTaskBoxId(), request.getTaskId(),
                request.getPrevTaskOrder(), request.getNewTaskOrder());
    }

    @Transactional
    public void moveTaskToSameTaskBox (MoveTaskSameTaskBoxRequest request) {
        taskMoveRepository.moveTaskToSameTaskBox(request.getTaskBoxId(), request.getTaskId(), request.getNewTaskOrder());
    }

    @Transactional
    public void updateTask(UpdateTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_NOT_EXIST));

        TaskEditor.TaskEditorBuilder taskEditorBuilder = task.toUpdate();
        TaskEditor taskEditor = taskEditorBuilder
                .taskTitle(request.getTitle())
                .tag(request.getTag())
                .dueDate(request.getDueDate())
                .workHour(request.getWorkHour())
                .build();

        task.update(taskEditor);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApiException(ErrorType.TASK_NOT_EXIST));

        taskRepository.delete(task);
    }
}
