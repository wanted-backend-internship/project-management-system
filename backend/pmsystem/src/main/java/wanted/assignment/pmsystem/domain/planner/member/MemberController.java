package wanted.assignment.pmsystem.domain.planner.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.member.dto.requests.SearchUserRequest;
import wanted.assignment.pmsystem.domain.planner.member.dto.requests.CreateMemberRequest;
import wanted.assignment.pmsystem.domain.planner.member.dto.responses.MemberInfoResponse;
import wanted.assignment.pmsystem.domain.planner.member.dto.responses.SearchUserResponse;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/members/search")
    public ResponseEntity<?> searchUser (@RequestBody SearchUserRequest request) {
        try {
            SearchUserResponse searchUserResponse = memberService.searchUser(request);
            return ResponseEntity.ok(searchUserResponse);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/members")
    public ResponseEntity<?> createMember (@RequestBody CreateMemberRequest request) {
        try {
            memberService.createMember(request);
            return ResponseEntity.ok("member 생성 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @DeleteMapping(value = "/members/{memberId}")
    public ResponseEntity<?> deleteMember (@PathVariable("memberId") Long memberId) {
        try {
            memberService.deleteMember(memberId);
            return ResponseEntity.ok("member 삭제 성공!");

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @GetMapping(value = "/{boardId}/members")
    public ResponseEntity<?> displayMemberInfo (@PathVariable("boardId") Long boardId) {
        try {
            List<MemberInfoResponse> memberInfoResponses = memberService.displayMemberInfo(boardId);
            return ResponseEntity.ok(memberInfoResponses);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/members/check")
    public ResponseEntity<?> checkLoginUserHost () {
        try {
            boolean response = memberService.isCheckMemberRole();
            return ResponseEntity.ok(response);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
