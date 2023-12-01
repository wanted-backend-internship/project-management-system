package wanted.assignment.pmsystem.domain.planner.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.task.requests.CreateTaskRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.DeleteTaskRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.MoveTaskOtherTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.MoveTaskSameTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.task.requests.UpdateTaskRequest;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards/taskBox/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping(value = "")
    public ResponseEntity<?> createTask (@RequestBody CreateTaskRequest request) {
        try {
            taskService.createTask(request);
            return ResponseEntity.ok("task 생성 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PatchMapping(value = "")
    public ResponseEntity<?> updateTask (@RequestBody UpdateTaskRequest request) {
        try {
            taskService.updateTask(request);
            return ResponseEntity.ok("task 수정 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/move/other")
    public ResponseEntity<?> moveTaskToOtherTaskBox (@RequestBody MoveTaskOtherTaskBoxRequest request) {
        try {
            taskService.moveTaskToOtherTaskBox(request);
            return ResponseEntity.ok("다른 taskBox 로 task 이동 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/move/same")
    public ResponseEntity<?> moveTaskToOtherTaskBox (@RequestBody MoveTaskSameTaskBoxRequest request) {
        try {
            taskService.moveTaskToSameTaskBox(request);
            return ResponseEntity.ok("같은 taskBox 로 task 이동 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<?> moveTaskToOtherTaskBox (@PathVariable("taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("task 삭제 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
