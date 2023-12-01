package wanted.assignment.pmsystem.domain.planner.taskBox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.board.BoardRepository;
import wanted.assignment.pmsystem.domain.planner.board.domain.Board;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import wanted.assignment.pmsystem.domain.planner.task.TaskRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBoxEditor;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.CreateTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.MoveTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.UpdateTaskBoxRequest;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;

@Service
@RequiredArgsConstructor
public class TaskBoxService {
    private final TaskRepository taskRepository;
    private final TaskBoxRepository taskBoxRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void createTaskBox(CreateTaskBoxRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() ->  new ApiException(ErrorType.BOARD_NOT_EXIST));

        Long maxOrder = taskBoxRepository.findMaxOrder();
        long nextOrder = (maxOrder != null) ? maxOrder + 1 : 1L;

        TaskBox taskBox = TaskBox.builder()
                .title(request.getTaskBoxTitle())
                .boxOrder(nextOrder)
                .board(board)
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
        Long temporaryOrder = -1L;

        taskBoxRepository.updateTaskBoxOrderToTemporary(
                request.getBoardId(), request.getCurrentTaskBoxOrder(), temporaryOrder);

        taskBoxRepository.updatePrevTaskBoxOrder(
                request.getBoardId(), request.getPrevTaskBoxOrder(), request.getCurrentTaskBoxOrder());

        taskBoxRepository.updateCurrentTaskBoxOrder(
                request.getBoardId(), temporaryOrder, request.getPrevTaskBoxOrder());
    }

    @Transactional
    public void deleteTaskBox(Long taskBoxId) {
        List<Task> tasks = taskRepository.findByTaskBoxId(taskBoxId);
        taskRepository.deleteAll(tasks);

        TaskBox taskBox = taskBoxRepository.findById(taskBoxId)
                .orElseThrow(() -> new ApiException(ErrorType.TASK_BOX_NOT_EXIST));
        taskBoxRepository.delete(taskBox);
    }
}
