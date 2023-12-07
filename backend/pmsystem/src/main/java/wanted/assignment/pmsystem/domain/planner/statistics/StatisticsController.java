package wanted.assignment.pmsystem.domain.planner.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.requests.StatisticsRequest;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.StatisticsResponse;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.StatisticBasicInfoResponse;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final StatisticsRepository statisticsRepository;

    @PostMapping(value = "/{boardId}/statistics")
    public ResponseEntity<?> displayBasicInfo (@PathVariable("boardId") Long boardId) {
        try {
            StatisticBasicInfoResponse response = statisticsService.displayBasicInfo(boardId);
            return ResponseEntity.ok(response);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }

    @PostMapping(value = "/statistics/report")
    public ResponseEntity<?> displayResults (@RequestBody StatisticsRequest request) {
        try {
            List<StatisticsResponse> responses = statisticsRepository.generateMemberStatistics(request);
            return ResponseEntity.ok(responses);

        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus()).body(apiException.getErrorType().getMessage());
        }
    }
}
