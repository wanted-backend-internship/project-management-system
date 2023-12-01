import { defineStore } from 'pinia';

export const useProjectStore = defineStore('projectStore', {
    state: () => ({
        currentBoardId: null,
        currentBoardTitle: ''
    }),
    actions: {
        setCurrentBoard(boardId, boardTitle) {
            this.currentBoardId = boardId;
            this.currentBoardTitle = boardTitle;
        }
    }
});