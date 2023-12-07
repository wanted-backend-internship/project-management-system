<script setup lang="ts">
import {onMounted, ref} from "vue";
import {deleteBoard, displayBoards} from "../../../api/projcet/BoardApi.ts";
import TheModal from "../../common/TheModal.vue";
import CreateBoard from "./CreateBoard.vue";
import { library } from '@fortawesome/fontawesome-svg-core';
import { faPenToSquare, faSquarePlus } from '@fortawesome/free-regular-svg-icons';
import { faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import UpdateBoard from "./UpdateBoard.vue";
import {useProjectStore} from "../../../store/ProjectStore.ts";

library.add(faPenToSquare, faTrashCan, faSquarePlus);

const projectStore = useProjectStore();
const selectProject = (boardId, boardTitle) => {
  projectStore.setCurrentBoard(boardId, boardTitle);
};

const boardsData = ref([]);

const isCreateModalOpen = ref(false);
const isUpdateModalOpen = ref(false);

const openCreateModal = () => {
  isCreateModalOpen.value = true;
};


const selectedBoardId = ref('');
const selectedBoardTitle = ref('');

const openUpdateModal = (boardId, boardTitle) => {
  selectedBoardId.value = boardId;
  selectedBoardTitle.value = boardTitle;
  isUpdateModalOpen.value = true;
};

const closeModal = () => {
  isCreateModalOpen.value = false;
  isUpdateModalOpen.value = false;
};

const onBoardCreated = () => {
  closeModal();
  setTimeout(fetchBoards, 300);
};

const handleDeleteBoard = async (boardId: string) => {
  try {
    await deleteBoard(boardId);
    await fetchBoards();
  } catch (error) {
    console.error(error);
  }
};

const fetchBoards = async () => {
  // console.log('Fetching boards...');
  try {
    const response = await displayBoards();
    boardsData.value = response.data;
  } catch (error) {
    console.error(error);
  }
};

onMounted(fetchBoards);
</script>

<template>
  <div class="project-container">
    <div class="title">
      프로젝트
    </div>
    <div class="button-container" @click="openCreateModal">
      <font-awesome-icon icon="fa-regular fa-square-plus" style="margin-right: 8px"/>
      <span>프로젝트 생성</span>
    </div>
    <div class="projects-list">
      <!-- 프로젝트 목록을 표시합니다 -->
      <div class="project" v-for="board in boardsData" :key="board.boardId" @click="selectProject(board.boardId, board.boardTitle)">
        <div class="project-content">
          <router-link :to="`/project/${board.boardId}`" div class="project-title">{{ board.boardTitle }}</router-link>
          <div class="project-member-container">
            <div v-for="member in board.members" :key="member.memberId" class="badge-created">
              {{ member.username }}
            </div>
          </div>
        </div>
        <div class="board-button-container">
          <font-awesome-icon icon="fa-regular fa-pen-to-square" @click="openUpdateModal(board.boardId, board.boardTitle)" style="margin-right: 15px;" class="board-icon"/>
          <font-awesome-icon icon="fa-solid fa-trash-can" @click="handleDeleteBoard(board.boardId)" class="board-icon"/>
        </div>
      </div>
    </div>
  </div>

  <TheModal :is-open="isCreateModalOpen" @close="closeModal">
    <CreateBoard @board-created="onBoardCreated"/>
  </TheModal>

  <TheModal :is-open="isUpdateModalOpen" @close="closeModal">
    <UpdateBoard
        :boardId="selectedBoardId"
        :boardTitle="selectedBoardTitle"
        @board-created="onBoardCreated"/>
  </TheModal>
</template>

<style lang="scss">
@import "../../../styles/common/container";
@import "../../../styles/common/font";
@import "../../../styles/common/button";
@import "../../../styles/common/input";
@import "../../../styles/colors/color";


.project-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  overflow: hidden;
  padding: 60px 0 0 60px;
}

.project-title {
  @include pre300(26px, $black);
  color: inherit;
  text-decoration: none;
}

.title {
  @include pre400(40px, $black);
}

.button-container {
  @include container(row, flex-start, center, 100%, auto);
  margin: 10px 0 0 3px;
  @include pre200(20px, $gray700)
}

.projects-list {
  @include container(row, flex-start, flex-start, 100%, 500px);
  overflow-y: scroll;
  flex-wrap: wrap;
  margin-top: 10px;
}

.project {
  @include container(row, flex-start, flex-start, 400px, 155px);
  background-color: $white;
  margin: 10px 20px 0 0;
  border-radius: 12px;
  border: 1px solid $black;
  padding: 30px 20px 30px 30px;
}

.project-content {
  @include container(column, flex-start, flex-start, 100%, 100%);
}

.board-button-container {
  @include container(row, flex-start, flex-start, auto, auto);
}

.board-icon {
  font-size: 18px;
  color: $gray600;
}

.badge-created {
  border: 1px solid $gray900;
  background-color: $gray200;
  padding: 2px 12px;
  border-radius: 15px;
  margin-right: 10px;
  @include pre300(14px, $gray900)
}

.project-member-container {
  @include container(row, flex-start, flex-start, 100%, auto);
  margin-top: 20px;
  flex-wrap: wrap;
}
</style>