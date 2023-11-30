package wanted.assignment.pmsystem.domain.planner.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.assignment.pmsystem.domain.planner.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long boardId);
}
