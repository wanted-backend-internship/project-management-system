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
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskBoxRepository taskBoxRepository;

    @Transactional
    public void createTask (CreateTaskRequest request) {
        TaskBox taskBox = taskBoxRepository.findById(request.getTaskBoxId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_BOX_NOT_EXIST));

        Task task = Task.builder()
                .taskOrder(request.getTaskOrder())
                .title(request.getTaskTitle())
                .tag(request.getTag())
                .createdBy(request.getCreatedBy())
                .workHour(request.getWorkHour())
                .dueDate(request.getDueDate())
                .taskBox(taskBox)
                .build();

        taskRepository.save(task);
    }

    @Transactional
    public void moveTaskToOtherTaskBox (MoveTaskOtherTaskBoxRequest request) {
        taskRepository.moveTaskToNewTaskBox(
                request.getTaskId(), request.getNewTaskBoxId(), request.getTaskOrder()
        );
        taskRepository.incrementTaskOrderInTaskBox(request.getNewTaskBoxId(), request.getTaskOrder());
    }

    @Transactional
    public void moveTaskToSameTaskBox (MoveTaskSameTaskBoxRequest request) {
        taskRepository.incrementTaskOrderInTaskBox(request.getTaskBoxId(), request.getNewTaskOrder());
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
                .createdBy(request.getCreatedBy())
                .build();

        task.update(taskEditor);
    }

    @Transactional
    public void deleteTask(DeleteTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_NOT_EXIST));

        taskRepository.delete(task);
    }
}
