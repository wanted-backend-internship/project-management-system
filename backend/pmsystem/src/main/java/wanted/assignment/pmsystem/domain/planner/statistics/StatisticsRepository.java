package wanted.assignment.pmsystem.domain.planner.statistics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.requests.StatisticsRequest;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.StatisticsResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<StatisticsResponse> generateMemberStatistics(StatisticsRequest request) {
        List<Long> taskBoxIds = request.getTaskBoxIds();
        List<String> memberNames = request.getMemberNames();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        List<StatisticsResponse> statisticsRespons = new ArrayList<>();

        for (String memberName : memberNames) {
            StatisticsResponse statisticsResponse;
            if (startDate == null && endDate == null) {
                statisticsResponse = calculateMemberWorkHourWhenDateNull(taskBoxIds, memberName);
            } else {
                statisticsResponse = calculateMemberWorkHourWhenDateNotNull(taskBoxIds, memberName, startDate, endDate);
            }
            if (!statisticsResponse.getResults().isEmpty()) {
                statisticsRespons.add(statisticsResponse);
            }
        }
        return statisticsRespons;
    }

    private StatisticsResponse calculateMemberWorkHourWhenDateNotNull(List<Long> taskBoxIds, String memberName, LocalDate startDate, LocalDate endDate) {
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.tag, SUM(CAST(t.workHour AS INTEGER)) as totalWorkHours " +
                        "FROM Task t " +
                        "JOIN t.taskBox tb " +
                        "WHERE tb.id IN (:taskBoxIds) " +
                        "AND t.createdBy = :memberName " +
                        "AND (t.dueDate BETWEEN :startDate AND :endDate OR t.dueDate IS NULL) " +
                        "GROUP BY t.tag", Object[].class);

        query.setParameter("taskBoxIds", taskBoxIds);
        query.setParameter("memberName", memberName);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Object[]> resultList = query.getResultList();

        StatisticsResponse statisticsResponse = StatisticsResponse.builder()
                .memberName(memberName)
                .results(resultList)
                .build();

        return statisticsResponse;
    }

    private StatisticsResponse calculateMemberWorkHourWhenDateNull(List<Long> taskBoxIds, String memberName) {
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT t.tag, SUM(CAST(t.workHour AS INTEGER)) as totalWorkHours " +
                        "FROM Task t " +
                        "JOIN t.taskBox tb " +
                        "WHERE tb.id IN (:taskBoxIds) " +
                        "AND t.createdBy = :memberName " +
                        "GROUP BY t.tag", Object[].class);

        query.setParameter("taskBoxIds", taskBoxIds);
        query.setParameter("memberName", memberName);

        List<Object[]> resultList = query.getResultList();

        StatisticsResponse statisticsResponse = StatisticsResponse.builder()
                .memberName(memberName)
                .results(resultList)
                .build();

        return statisticsResponse;
    }
}
