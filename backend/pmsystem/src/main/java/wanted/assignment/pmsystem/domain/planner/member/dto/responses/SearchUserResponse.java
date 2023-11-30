package wanted.assignment.pmsystem.domain.planner.member.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponse {
    private String username;
    private Long userId;
}
