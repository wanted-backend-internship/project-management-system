package wanted.assignment.pmsystem.domain.planner.statistics.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.member.MemberRepository;
import wanted.assignment.pmsystem.domain.planner.member.domain.Member;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.detail.MemberDetail;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.detail.TaskBoxDetail;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.StatisticBasicInfoResponse;
import wanted.assignment.pmsystem.domain.planner.task.TaskRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.TaskBoxRepository;
import wanted.assignment.pmsystem.domain.planner.taskBox.domain.TaskBox;

@Service
@RequiredArgsConstructor
public class MemberStatisticsService {
    private final TaskBoxRepository taskBoxRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StatisticBasicInfoResponse displayBasicInfo (Long boardId) {
        List<TaskBox> taskBoxes = taskBoxRepository.findByBoardId(boardId);
        List<Member> members = memberRepository.findByBoardId(boardId);

        List<TaskBoxDetail> taskBoxDetails = new ArrayList<>();
        List<MemberDetail> memberDetails = new ArrayList<>();

        for (TaskBox taskBox: taskBoxes) {
            TaskBoxDetail taskBoxDetail = TaskBoxDetail.builder()
                    .taskBoxId(taskBox.getId())
                    .taskBoxTitle(taskBox.getTitle())
                    .build();

            taskBoxDetails.add(taskBoxDetail);
        }

        for (Member member: members) {
            MemberDetail memberDetail = MemberDetail.builder()
                    .memberId(member.getId())
                    .membername(member.getUser().getUsername())
                    .build();

            memberDetails.add(memberDetail);
        }

        StatisticBasicInfoResponse statisticBasicInfoResponse = StatisticBasicInfoResponse.builder()
                .taskBoxDetails(taskBoxDetails)
                .memberDetails(memberDetails)
                .build();

        return statisticBasicInfoResponse;
    }
}
