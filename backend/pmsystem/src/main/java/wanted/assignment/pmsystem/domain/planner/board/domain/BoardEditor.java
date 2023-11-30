package wanted.assignment.pmsystem.domain.planner.board.domain;

import lombok.Getter;

@Getter
public class BoardEditor {
    private String boardTitle;

    public BoardEditor(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public static BoardEditorBuilder builder() {
        return new BoardEditorBuilder();
    }

    public static class BoardEditorBuilder {
        private String boardTitle;

        BoardEditorBuilder() {
        }

        public BoardEditorBuilder boardTitle(final String boardTitle) {
            if (boardTitle != null && !boardTitle.isEmpty()) {
                this.boardTitle = boardTitle;
            }
            return this;
        }

        public BoardEditor build() {
            return new BoardEditor(boardTitle);
        }
    }
}
