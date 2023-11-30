package wanted.assignment.pmsystem.domain.planner.member.dto.requests;

import lombok.Getter;

@Getter
public class CreateMemberRequest {
    private Long boardId;
    private Long userId;
}
