package wanted.assignment.pmsystem.domain.planner.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wanted.assignment.pmsystem.domain.planner.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByBoardId(Long boardId);
    Optional<Member> findByUserIdAndBoardId(Long userId, Long boardId);
    List<Member> findByUserId(Long userId);
    Member findMemberByUserId(Long userId);
}
