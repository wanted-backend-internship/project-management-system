package wanted.assignment.pmsystem.domain.planner.board.dto.requests;

import lombok.Getter;

@Getter
public class UpdateBoardRequest {
    private Long boardId;
    private String boardTitle;
}
