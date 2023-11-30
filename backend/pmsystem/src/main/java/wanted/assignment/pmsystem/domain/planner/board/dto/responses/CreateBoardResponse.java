package wanted.assignment.pmsystem.domain.planner.board.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoardResponse {
    private Long boardId;
    private String boardTitle;
}
