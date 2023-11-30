package wanted.assignment.pmsystem.domain.planner.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.board.dto.requests.CreateBoardRequest;
import wanted.assignment.pmsystem.domain.planner.board.dto.requests.UpdateBoardRequest;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.BoardListResponse;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.CreateBoardResponse;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.UpdateBoardResponse;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.BoardDetailResponse;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping(value = "")
    public ResponseEntity<?> createBoard (@RequestBody CreateBoardRequest request) {
        try {
            CreateBoardResponse response = boardService.createBoard(request);
            return ResponseEntity.ok(response);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PatchMapping(value = "")
    public ResponseEntity<?> updateBoard (@RequestBody UpdateBoardRequest request) {
        try {
            UpdateBoardResponse response = boardService.updateBoard(request);
            return ResponseEntity.ok(response);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("Board 삭제 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity<?> displayBoardDetail (@PathVariable("boardId") Long boardId) {
        try {
            List<BoardDetailResponse> boardDetailResponses = boardService.displayBoardDetail(boardId);
            return ResponseEntity.ok(boardDetailResponses);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @GetMapping(value = "/me")
    public ResponseEntity<?> displayBoards() {
        try {
            List<BoardListResponse> boardListResponses = boardService.displayBoardList();
            return ResponseEntity.ok(boardListResponses);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
