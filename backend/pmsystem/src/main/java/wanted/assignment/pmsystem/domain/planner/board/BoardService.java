package wanted.assignment.pmsystem.domain.planner.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.board.domain.Board;
import wanted.assignment.pmsystem.domain.planner.board.domain.BoardEditor;
import wanted.assignment.pmsystem.domain.planner.board.dto.requests.CreateBoardRequest;
import wanted.assignment.pmsystem.domain.planner.board.dto.requests.DeleteBoardRequest;
import wanted.assignment.pmsystem.domain.planner.board.dto.requests.UpdateBoardRequest;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.BoardListResponse;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.CreateBoardResponse;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.UpdateBoardResponse;
import wanted.assignment.pmsystem.domain.planner.member.MemberService;
import wanted.assignment.pmsystem.domain.planner.task.domain.Task;
import wanted.assignment.pmsystem.domain.planner.task.TaskRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;
import wanted.assignment.pmsystem.domain.planner.taskBox.TaskBoxRepository;
import wanted.assignment.pmsystem.domain.planner.board.dto.responses.BoardDetailResponse;
import wanted.assignment.pmsystem.domain.planner.member.domain.Member;
import wanted.assignment.pmsystem.domain.planner.member.MemberRepository;
import wanted.assignment.pmsystem.domain.planner.member.domain.Role;
import wanted.assignment.pmsystem.domain.user.User;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;
import wanted.assignment.pmsystem.global.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final TaskBoxRepository taskBoxRepository;
    private final TaskRepository taskRepository;
    private final MemberService memberService;
    private final AuthUtil authUtil;

    @Transactional
    public CreateBoardResponse createBoard(CreateBoardRequest request) {
        User user = authUtil.getLoginUser();

        Board board = Board.builder()
                .title(request.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
        boardRepository.save(board);

        Member member = Member.builder()
                .role(Role.Host)
                .pending(false)
                .user(user)
                .board(board)
                .build();
        memberRepository.save(member);

        CreateBoardResponse createBoardResponse = CreateBoardResponse.builder()
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .build();

        return createBoardResponse;
    }

    @Transactional
    public UpdateBoardResponse updateBoard(UpdateBoardRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorType.BOARD_NOT_EXIST));

        BoardEditor.BoardEditorBuilder boardEditorBuilder = board.toUpdate();
        BoardEditor boardEditor = boardEditorBuilder
                .boardTitle(request.getBoardTitle())
                .build();

        board.update(boardEditor);

        UpdateBoardResponse updateBoardResponse = UpdateBoardResponse.builder()
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .build();

        return updateBoardResponse;
    }

    @Transactional
    public void deleteBoard(DeleteBoardRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorType.BOARD_NOT_EXIST));
        boardRepository.delete(board);

        List<Member> members = memberRepository.findByBoardId(request.getBoardId());
        memberRepository.deleteAll(members);

        List<TaskBox> taskBoxs = taskBoxRepository.findByBoardId(request.getBoardId());
        for (TaskBox taskBox : taskBoxs) {
            List<Task> task = taskRepository.findByTaskBoxId(taskBox.getId());
            taskRepository.deleteAll(task);
        }
        taskBoxRepository.deleteAll(taskBoxs);
    }

    public List<BoardListResponse> displayBoardList () {
        Long userId = authUtil.getLoginUserIndex();

        List<Member> members = memberRepository.findByUserId(userId);
        List<Board> boards= members.stream()
                .map(Member::getBoard)
                .toList();

        List<BoardListResponse> boardListResponses = new ArrayList<>();

        for (Board board : boards) {
            BoardListResponse boardListResponse = BoardListResponse.builder()
                    .boardId(board.getId())
                    .boardTitle(board.getTitle())
                    .members(memberService.displayMemberInfo(board.getId()))
                    .build();

            boardListResponses.add(boardListResponse);
        }
        return boardListResponses;
    }

    @Transactional
    public List<BoardDetailResponse> displayBoardDetail(Long boardId) {
        List<BoardDetailResponse> boardDetailResponses = new ArrayList<>();
        List<TaskBox> taskBoxes = taskBoxRepository.findByBoardId(boardId);
        for (TaskBox taskBox : taskBoxes) {
            List<Task> tasks = taskRepository.findByTaskBoxId(taskBox.getId());
            BoardDetailResponse boardDetailResponse = BoardDetailResponse.builder()
                    .taskBoxId(taskBox.getId())
                    .taskBoxOrder(taskBox.getBoxOrder())
                    .taskBoxTitle(taskBox.getTitle())
                    .taskDetailResponses(tasks)
                    .build();
            boardDetailResponses.add(boardDetailResponse);
        }
        return boardDetailResponses;
    }
}
