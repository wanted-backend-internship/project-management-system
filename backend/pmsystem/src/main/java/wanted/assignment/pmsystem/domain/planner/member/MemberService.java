package wanted.assignment.pmsystem.domain.planner.member;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.board.BoardRepository;
import wanted.assignment.pmsystem.domain.planner.board.domain.Board;
import wanted.assignment.pmsystem.domain.planner.member.domain.Member;
import wanted.assignment.pmsystem.domain.planner.member.domain.Role;
import wanted.assignment.pmsystem.domain.planner.member.dto.requests.SearchUserRequest;
import wanted.assignment.pmsystem.domain.planner.member.dto.requests.CreateMemberRequest;
import wanted.assignment.pmsystem.domain.planner.member.dto.requests.DeleteMemberRequest;
import wanted.assignment.pmsystem.domain.planner.member.dto.responses.MemberInfoResponse;
import wanted.assignment.pmsystem.domain.planner.member.dto.responses.SearchUserResponse;
import wanted.assignment.pmsystem.domain.user.User;
import wanted.assignment.pmsystem.domain.user.UserRepository;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public SearchUserResponse searchUser (SearchUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        SearchUserResponse searchUserResponse = SearchUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();

        return searchUserResponse;
    }

    @Transactional
    public void createMember (CreateMemberRequest request) {
        boolean memberCheck = memberRepository.findByUserIdAndBoardId(request.getUserId(), request.getBoardId()).isPresent();
        if (memberCheck) {
            throw new ApiException(ErrorType.MEMBER_ALREADY_EXIST);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorType.BOARD_NOT_EXIST));

        Member member = Member.builder()
                .role(Role.Member)
                .board(board)
                .user(user)
                .pending(true)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember (Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        memberRepository.delete(member);
    }

    public List<MemberInfoResponse> displayMemberInfo (Long boardId) {
        List<Member> members = memberRepository.findByBoardId(boardId);
        List<MemberInfoResponse> memberInfoResponses = new ArrayList<>();

        for (Member member : members) {
            MemberInfoResponse memberInfoResponse = MemberInfoResponse.builder()
                    .memberId(member.getId())
                    .username(member.getUser().getUsername())
                    .pending(member.getPending())
                    .role(member.getRole())
                    .build();
            memberInfoResponses.add(memberInfoResponse);
        }

        return memberInfoResponses;
    }
}
