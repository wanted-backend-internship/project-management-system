package wanted.assignment.pmsystem.domain.planner.board.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoardResponse {
    Long boardId;
    String boardTitle;
}
