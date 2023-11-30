package wanted.assignment.pmsystem.domain.planner.member.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.assignment.pmsystem.domain.planner.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByBoardId(Long boardId);
    Optional<Member> findByUserId(Long UserId);
}
