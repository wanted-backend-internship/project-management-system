package wanted.assignment.pmsystem.domain.planner.board.dto.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.member.dto.responses.MemberInfoResponse;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponse {
    private String boardTitle;
    private Long boardId;
    private List<MemberInfoResponse> members;
}
