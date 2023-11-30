package wanted.assignment.pmsystem.domain.planner.taskBox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import wanted.assignment.pmsystem.domain.planner.task.TaskRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBoxEditor;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.CreateTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.DeleteTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.MoveTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.UpdateTaskBoxRequest;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;

@Service
@RequiredArgsConstructor
public class TaskBoxService {
    private final TaskRepository taskRepository;
    private final TaskBoxRepository taskBoxRepository;

    @Transactional
    public void createTaskBox(CreateTaskBoxRequest request) {
        TaskBox taskBox = TaskBox.builder()
                .title(request.getTaskBoxTitle())
                .boxOrder(request.getTaskBoxOrder())
                .build();
        taskBoxRepository.save(taskBox);
    }

    @Transactional
    public void updateTaskBox(UpdateTaskBoxRequest requests) { // 순서 변경 아님
        TaskBox taskBox = taskBoxRepository.findById(requests.getTaskBoxId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_BOX_NOT_EXIST));

        TaskBoxEditor.TaskBoxEditorBuilder taskBoxEditorBuilder = taskBox.toUpdate();
        TaskBoxEditor taskBoxEditor = taskBoxEditorBuilder
                .boxTitle(requests.getTaskBoxTitle())
                .build();

        taskBox.update(taskBoxEditor);
    }

    @Transactional
    public void moveTaskBox(MoveTaskBoxRequest request) {
        taskBoxRepository.updatePrevTaskBoxOrder(
                request.getBoardId(), request.getPrevTaskBoxOrder(), request.getCurrentTaskBoxOrder()
        );
        taskBoxRepository.updateCurrentTaskBoxOrder(
                request.getBoardId(), request.getCurrentTaskBoxOrder(), request.getPrevTaskBoxOrder()
        );
    }

    @Transactional
    public void deleteTaskBox(DeleteTaskBoxRequest request) {
        List<Task> tasks = taskRepository.findByTaskBoxId(request.getTaskBoxId());
        taskRepository.deleteAll(tasks);

        TaskBox taskBox = taskBoxRepository.findById(request.getTaskBoxId())
                .orElseThrow(() -> new ApiException(ErrorType.TASK_BOX_NOT_EXIST));
        taskBoxRepository.delete(taskBox);
    }
}
