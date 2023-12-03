package wanted.assignment.pmsystem.domain.planner.statistics.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wanted.assignment.pmsystem.domain.planner.member.MemberRepository;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.requests.MemberStatisticsRequest;
import wanted.assignment.pmsystem.domain.planner.statistics.dto.responses.MemberStatisticsResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberStatisticsRepository {
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<MemberStatisticsResponse> generateMemberStatistics(MemberStatisticsRequest request) {
        List<Long> taskBoxIds = request.getTaskBoxIds();
        List<String> memberNames = request.getMemberNames();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        log.info("Generating member statistics with request: MemberIds={}, TaskBoxIds={}, StartDate={}, EndDate={}",
                memberNames, taskBoxIds, startDate, endDate);

        List<MemberStatisticsResponse> memberStatisticsResponses = new ArrayList<>();

        for (String memberName : memberNames) {
            MemberStatisticsResponse memberStatisticsResponse;
            if (startDate == null && endDate == null) {
                memberStatisticsResponse = calculateMemberWorkHourWhenDateNull(taskBoxIds, memberName);
            } else {
                memberStatisticsResponse = calculateMemberWorkHourWhenDateNotNull(taskBoxIds, memberName, startDate, endDate);
            }
            if (memberStatisticsResponse.getResults().isEmpty()) {
                log.warn("No statistics found for member with name: {}", memberName);
            } else {
                memberStatisticsResponses.add(memberStatisticsResponse);
            }
        }

        log.info("Member statistics responses: {}", memberStatisticsResponses);
        return memberStatisticsResponses;
    }

    private MemberStatisticsResponse calculateMemberWorkHourWhenDateNotNull(List<Long> taskBoxIds, String memberName, LocalDate startDate, LocalDate endDate) {
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
        log.debug("Query results: {}", resultList);

        MemberStatisticsResponse memberStatisticsResponse = MemberStatisticsResponse.builder()
                .memberName(memberName)
                .results(resultList)
                .build();

        return memberStatisticsResponse;
    }

    private MemberStatisticsResponse calculateMemberWorkHourWhenDateNull(List<Long> taskBoxIds, String memberName) {
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
        log.debug("Query results for null dates: {}", resultList);

        MemberStatisticsResponse memberStatisticsResponse = MemberStatisticsResponse.builder()
                .memberName(memberName)
                .results(resultList)
                .build();

        return memberStatisticsResponse;
    }
}
