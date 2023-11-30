package wanted.assignment.pmsystem.domain.planner.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    public BoardEditor.BoardEditorBuilder toUpdate() {
        return BoardEditor.builder()
                .boardTitle(title);
    }
    public void update(BoardEditor boardEditor) {
        title = boardEditor.getBoardTitle();
    }
}
