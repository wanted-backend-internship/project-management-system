package wanted.assignment.pmsystem.domain.planner.member.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.assignment.pmsystem.domain.planner.member.domain.Role;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {
    private Long memberId;
    private String username;
    private Role role;
    private boolean pending;
}
