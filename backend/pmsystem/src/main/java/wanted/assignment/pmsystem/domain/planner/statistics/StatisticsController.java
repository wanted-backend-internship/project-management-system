package wanted.assignment.pmsystem.domain.planner.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.requests.MemberStatisticsRequest;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.MemberStatisticsResponse;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.StatisticBasicInfoResponse;
import wanted.assignment.pmsystem.domain.planner.statistics.repository.MemberStatisticsRepository;
import wanted.assignment.pmsystem.domain.planner.statistics.service.MemberStatisticsService;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class StatisticsController {
    private final MemberStatisticsService memberStatisticsService;
    private final MemberStatisticsRepository memberStatisticsRepository;

    @PostMapping(value = "/{boardId}/statistics/member")
    public ResponseEntity<?> displayBasicInfo (@PathVariable("boardId") Long boardId) {
        try {
            StatisticBasicInfoResponse response = memberStatisticsService.displayBasicInfo(boardId);
            return ResponseEntity.ok(response);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/statistics/member/report")
    public ResponseEntity<?> displayResults (@RequestBody MemberStatisticsRequest request) {
        try {
            List<MemberStatisticsResponse> responses = memberStatisticsRepository.generateMemberStatistics(request);
            return ResponseEntity.ok(responses);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
