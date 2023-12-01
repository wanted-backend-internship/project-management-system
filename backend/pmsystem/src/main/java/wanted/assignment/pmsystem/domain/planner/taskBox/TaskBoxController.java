package wanted.assignment.pmsystem.domain.planner.taskBox;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.CreateTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.MoveTaskBoxRequest;
import wanted.assignment.pmsystem.domain.planner.taskBox.dto.UpdateTaskBoxRequest;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards/taskBox")
@RequiredArgsConstructor
public class TaskBoxController {
    private final TaskBoxService taskBoxService;

    @PostMapping(value = "")
    public ResponseEntity<?> createTaskBox (@RequestBody CreateTaskBoxRequest request) {
        try {
            taskBoxService.createTaskBox(request);
            return ResponseEntity.ok("taskBox 생성 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PatchMapping(value = "")
    public ResponseEntity<?> updateTaskBox (@RequestBody UpdateTaskBoxRequest request) {
        try {
            taskBoxService.updateTaskBox(request);
            return ResponseEntity.ok("taskBox 수정 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @DeleteMapping(value = "/{taskBoxId}")
    public ResponseEntity<?> deleteTaskBox (@PathVariable("taskBoxId") Long taskBoxId) {
        try {
            taskBoxService.deleteTaskBox(taskBoxId);
            return ResponseEntity.ok("taskBox 삭제 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value="/move")
    public ResponseEntity<?> moveTaskBox (@RequestBody MoveTaskBoxRequest moveTaskBoxRequest) {
        try {
            taskBoxService.moveTaskBox(moveTaskBoxRequest);
            return ResponseEntity.ok("taskBox 드래그 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
