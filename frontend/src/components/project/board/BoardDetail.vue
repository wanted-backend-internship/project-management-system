<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {boardDetail} from "../../../api/projcet/BoardApi.ts";
import {useRoute} from "vue-router";
import {useProjectStore} from "../../../store/ProjectStore.ts";
import {checkMember, fetchMember} from "../../../api/projcet/MemberApi.ts";
import {BoardDetailResponse} from "../../../api/projcet/BoardDetailResponse.ts";
import {deleteTaskBox, moveTaskBox} from "../../../api/projcet/TaskBoxApi.ts";
import {deleteTask, moveTaskToOtherTaskBox, moveTaskToSameTaskBox} from "../../../api/projcet/TaskApi.ts";
import TheModal from "../../common/TheModal.vue";
import CreateTaskBox from "../taskBox/CreateTaskBox.vue";
import UpdateTaskBox from "../taskBox/UpdateTaskBox.vue";
import CreateTask from "../task/CreateTask.vue";
import UpdateTask from "../task/UpdateTask.vue";
import UserSearch from "../member/UserSearch.vue";
import DeleteMember from "../member/DeleteMember.vue";
import { library } from '@fortawesome/fontawesome-svg-core';
import { faPenToSquare,faCalendarCheck, faClock } from '@fortawesome/free-regular-svg-icons';
import { faTrashCan, faFileCirclePlus, faFolderPlus, faUserPlus, faUserMinus, faAddressCard, faUsersRectangle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import MemberReport from "../../statistic/MemberReport.vue";

library.add(faPenToSquare, faTrashCan, faFileCirclePlus, faCalendarCheck, faClock, faFolderPlus, faUserPlus, faUserMinus, faAddressCard, faUsersRectangle);

const projectStore = useProjectStore();
const boardTitle = computed(() => projectStore.currentBoardTitle);

const boardData = ref<BoardDetailResponse[]>([]);
const memberData = ref([]);
const route = useRoute();
const boardId = ref(route.params.boardId);
const isMemberHost = ref('');

const isCreateModalOpen = ref(false);
const isUpdateModalOpen = ref(false);
const isTaskCreateModalOpen = ref(false);
const isTaskUpdateModalOpen = ref(false);
const isMemberSearchModalOpen = ref(false);
const isMemberDeleteModalOpen = ref(false);
const isMemberReportModalOpen = ref(false);

const openCreateModal = () => {
  isCreateModalOpen.value = true;
};

const selectedTaskBoxId = ref('');
const selectedTaskId = ref('');

const openUpdateModal = (taskBoxId: string) => {
  selectedTaskBoxId.value = taskBoxId;
  isUpdateModalOpen.value = true;
}

const openTaskCreateModal = (taskBoxId: string) => {
  selectedTaskBoxId.value = taskBoxId;
  isTaskCreateModalOpen.value = true;
}

const openTaskUpdateModal = (taskId: string) => {
  selectedTaskId.value = taskId;
  isTaskUpdateModalOpen.value = true;
}

const openMemberSearchModal = () => {
  isMemberSearchModalOpen.value = true;
}

const openMemberSettingModal = () => {
  isMemberDeleteModalOpen.value = true;
}

const openMemberReportModal = () => {
  isMemberReportModalOpen.value = true;
}

const closeModal = () => {
  isCreateModalOpen.value = false;
  isUpdateModalOpen.value = false;
  isTaskCreateModalOpen.value = false;
  isTaskUpdateModalOpen.value = false;
  isMemberSearchModalOpen.value = false;
  isMemberDeleteModalOpen.value = false;
  isMemberReportModalOpen.value = false;
};

const getTagClass = (tag) => {
  return `kanban-badge-${tag.toLowerCase()}`;
};

const fetchBoard = () => {
  closeModal();
  setTimeout(fetchBoardDetail, 300);
}

const memberInfo = () => {
  closeModal();
  setTimeout(fetchMemberInfo, 300);
}

const handleDeleteTaskBox = async (taskBoxId: string) => {
  try {
    await deleteTaskBox(taskBoxId);
    await fetchBoardDetail();
  } catch (error) {
    console.error(error);
  }
}

const handleDeleteTask = async (taskId: string) => {
  try {
    await deleteTask(taskId);
    await fetchBoardDetail();
  } catch (error) {
    console.error(error);
  }
}

const fetchBoardDetail = async () => {
  try {
    const response = await boardDetail(boardId.value);
    boardData.value = response.data;

  } catch (error) {
    console.error(error);
  }
};

const fetchMemberInfo = async () => {
  try {
    const response = await fetchMember(boardId.value);
    memberData.value = response.data;
  } catch (error) {
    console.error(error)
  }
}

const checkMemberRole = async () => {
  try {
    const response = await checkMember();
    isMemberHost.value = response.data

  } catch (error) {
    console.error(error)
  }
}

onMounted(fetchBoardDetail);
onMounted(checkMemberRole);

const onDragStartTaskBox = (event, taskBoxId, taskBoxOrder) => {
  event.dataTransfer.setData('type', 'taskBox');
  event.dataTransfer.setData('id', taskBoxId.toString());
  event.dataTransfer.setData('order', taskBoxOrder.toString());
};

const onDragOver = (event) => {
  console.log('Dragging over', event.target);
  event.preventDefault();
};


const onDropTaskBox = async (event, targetOrder) => {
  event.preventDefault();
  const type = event.dataTransfer.getData('type');
  if (type === 'taskBox') {
    const sourceOrder = parseInt(event.dataTransfer.getData('order'));
    try {
      await moveTaskBox({
        boardId: boardId.value,
        prevTaskBoxOrder: sourceOrder.toString(),
        currentTaskBoxOrder: targetOrder.toString()
      });
      await fetchBoardDetail();
    } catch (error) {
      console.error(error);
    }
  }
}


const onDragStartTask = (event, taskId, taskOrder, taskBoxId) => {
  event.stopPropagation();
  event.dataTransfer.setData('type', 'task');
  event.dataTransfer.setData('id', taskId.toString());
  event.dataTransfer.setData('order', taskOrder.toString());
  event.dataTransfer.setData('taskBoxId', taskBoxId.toString());
};

const onDropTaskElement = async (event, targetTaskBoxId) => {
  event.preventDefault();

  const type = event.dataTransfer.getData('type');
  if (type !== 'task') return;

  const taskId = event.dataTransfer.getData('id');
  const sourceTaskBoxId = event.dataTransfer.getData('taskBoxId');
  const sourceTaskOrder = parseInt(event.dataTransfer.getData('order'), 10);

  let targetTaskOrder = 1;

  const targetTaskBox = boardData.value.find(box => box.taskBoxId === targetTaskBoxId);
  if (targetTaskBox && targetTaskBox.taskDetailResponses.length > 0) {
    targetTaskOrder = calculateTargetTaskOrder(0, targetTaskBoxId);
  }

  try {
    if (sourceTaskBoxId !== targetTaskBoxId) {
      await moveTaskToOtherTaskBox({
        prevTaskBoxId: sourceTaskBoxId,
        newTaskBoxId: targetTaskBoxId,
        prevTaskOrder: sourceTaskOrder.toString(),
        newTaskOrder: targetTaskOrder.toString(),
        taskId: taskId
      });
    } else {
      await moveTaskToSameTaskBox({
        taskBoxId: targetTaskBoxId,
        taskId: taskId,
        newTaskOrder: targetTaskOrder.toString()
      });
    }
    await fetchBoardDetail();
  } catch (error) {
    console.error(error);
  }
};

// TODO 계속 음수 나오는 원인 찾아야 함
function calculateTargetTaskOrder(dropPosition, targetTaskBoxId) {
  const targetTaskBox = boardData.value.find(box => box.taskBoxId === targetTaskBoxId);
  if (!targetTaskBox) {
    return 1;
  }

  const tasks = targetTaskBox.taskDetailResponses;
  if (tasks.length === 0) {
    return 1;
  }

  if (dropPosition >= tasks.length) {
    return tasks[tasks.length - 1].taskOrder + 1;
  } else if (dropPosition <= 0) {
    return 1;
  } else {
    const prevTaskOrder = tasks[dropPosition - 1].taskOrder;
    const nextTaskOrder = tasks[dropPosition] ? tasks[dropPosition].taskOrder : (prevTaskOrder + 1);
    return prevTaskOrder + Math.floor((nextTaskOrder - prevTaskOrder) / 2);
  }
}
</script>

<template>
  <div class="board-detail-container">
    <div class="board-detail-icon-container">
      <div class="board-detail-title">
        {{ boardTitle }}
      </div>
      <div>
        <font-awesome-icon style="margin-right: 15px; font-size: 23px" @click="openCreateModal" icon="fa-solid fa-folder-plus" />
        <font-awesome-icon style="margin-right: 10px; font-size: 19px" @click="openMemberSearchModal(boardId)" icon="fa-solid fa-user-plus" />
        <font-awesome-icon icon="fa-solid fa-user-minus" style="margin-right: 13px; font-size: 19px" @click="openMemberSettingModal"/>
        <font-awesome-icon v-if="isMemberHost" icon="fa-solid fa-address-card" style="margin-right: 13px; font-size: 22px" @click="openMemberReportModal"/>
        <font-awesome-icon v-if="isMemberHost" icon="fa-solid fa-users-rectangle" style="font-size: 21px"/>
      </div>
    </div>
    <div class="kanban-container">
      <div v-for="taskBox in boardData" :key="taskBox.taskBoxId"
            class="kanban-column"
            draggable="true"
            @dragstart="onDragStartTaskBox($event, taskBox.taskBoxId, taskBox.taskBoxOrder)"
            @dragover="onDragOver"
            @drop="onDropTaskElement($event, taskBox.taskBoxId)">
        <div class="kanban-column-header">
          <p>
            {{ taskBox.taskBoxTitle }}
          </p>
          <div class="board-button-container">
            <font-awesome-icon icon="fa-regular fa-pen-to-square" @click="openUpdateModal(taskBox.taskBoxId)" style="margin-right: 15px;" class="board-icon"/>
            <font-awesome-icon icon="fa-solid fa-file-circle-plus" @click="openTaskCreateModal(taskBox.taskBoxId, boardId)" style="margin-right: 15px;" class="board-icon"/>
            <font-awesome-icon icon="fa-solid fa-trash-can" @click="handleDeleteTaskBox(taskBox.taskBoxId)" class="board-icon"/>
          </div>
        </div>
        <div v-for="(task, taskIndex) in taskBox.taskDetailResponses" :key="task.id"
             class="kanban-row"
             draggable="true"
             @dragstart="onDragStartTask($event, task.id, task.taskOrder, taskBox.taskBoxId)"
             @dragover="onDragOver"
             @drop="onDropTaskElement($event, taskBox.taskBoxId, taskIndex)">
          <div class="kanban-row-header">
            <p>
              {{ task.title }}
            </p>
            <div class="kanban-button-container">
              <font-awesome-icon icon="fa-regular fa-pen-to-square" @click="openTaskUpdateModal(task.id)" style="margin-right: 12px;" class="kanban-row-icon"/>
              <font-awesome-icon icon="fa-solid fa-trash-can" @click="handleDeleteTask(task.id)" class="kanban-row-icon"/>
            </div>
          </div>
          <div class="kanban-row-content">
            <div class="kanban-row-content-icon">
              <font-awesome-icon style="margin-right: 5px" icon="fa-regular fa-calendar-check" />
              <span>{{ task.dueDate }}</span>
            </div>
            <div class="kanban-row-content-icon">
              <font-awesome-icon style="margin-right: 5px" icon="fa-regular fa-clock" />
              <span>{{ task.workHour }}</span>
            </div>
            <div class="kanban-row-footer" style="margin-top: 10px">
              <div class="kanban-badge-created">
                {{ task.createdBy }}
              </div>
              <div class="kanban-badge" :class="getTagClass(task.tag)">
                {{ task.tag }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 칼럼 추가 모달 -->
  <TheModal :is-open="isCreateModalOpen" @close="closeModal">
    <CreateTaskBox
        :boardId="boardId"
        @taskbox-created="fetchBoard"/>
  </TheModal>

  <TheModal :is-open="isUpdateModalOpen" @close="closeModal">
    <UpdateTaskBox
        :taskBoxId="selectedTaskBoxId"
        @taskbox-updated="fetchBoard"/>
  </TheModal>

  <TheModal :is-open="isTaskCreateModalOpen" @close="closeModal">
    <CreateTask
        :taskBoxId="selectedTaskBoxId"
        :boardId="boardId"
        @task-created="fetchBoard"/>
  </TheModal>

  <TheModal :is-open="isTaskUpdateModalOpen" @close="closeModal">
    <UpdateTask
        :taskId="selectedTaskId"
        @task-updated="fetchBoard"/>
  </TheModal>

  <TheModal :is-open="isMemberSearchModalOpen" @close="closeModal">
    <UserSearch
        :boardId="boardId"
        @member-added="memberInfo"/>
  </TheModal>

  <TheModal :is-open="isMemberDeleteModalOpen" @close="closeModal">
    <DeleteMember
        :boardId="boardId"
        @member-deleted="memberInfo"/>
  </TheModal>

  <TheModal :is-open="isMemberReportModalOpen" @close="closeModal">
    <MemberReport :boardId="boardId"/>
  </TheModal>
</template>

<style lang="scss">
@import "../../../styles/common/container";
@import "../../../styles/common/font";
@import "../../../styles/common/button";
@import "../../../styles/common/input";
@import "../../../styles/colors/color";
@import "../../../styles/kanban/badge";

.board-detail-container {
  @include container(column, flex-start, flex-start, 1000px, auto);
  max-height: 780px ;
  padding: 30px 0 30px 50px;
  overflow: hidden;
}

.board-detail-title {
  @include pre300(32px, $black);
  margin-top: 10px;
}

.board-detail-icon-container {
  @include container(row, space-between, flex-end, 100%, auto);
  font-size: 20px;
  color: $gray700;
  margin-bottom: 15px;
}

.kanban-container {
  @include container(row, flex-start, flex-start, 100%, 100%);
  overflow: scroll;
}

.kanban-column {
  @include container(column, flex-start, flex-start, 300px, 100%);
  border: 1px solid $black;

  flex-shrink: 0;
  margin-right: 15px;
  border-radius: 10px;
  background-color: white;
  padding: 30px 20px;

  overflow-y: scroll;
}

.kanban-column-header, .kanban-row-header, .kanban-row-footer {
  @include container(row, space-between, flex-start, 100%, auto);
}

.kanban-column-header p {
  @include pre300(24px, $black);
}

.board-button-container {
  @include container(row, flex-start, flex-start, auto, auto);
  margin-top: 8px;
}

.kanban-row {
  @include container(column, flex-start, flex-start, 100%, auto);
  border-radius: 10px;
  background: $gray100;
  padding: 20px;
  margin: 10px 0;
}

.kanban-row-header p {
  @include pre300(20px, $black);
  margin: 0;
}

.kanban-row-icon {
  color: $gray700;
  font-size: 16px;
}

.kanban-button-container {
  @include container(row, flex-start, flex-start, auto, auto);
  margin-top: 5px;
}

.kanban-row-content {
  @include container(column, flex-start, flex-start, 100%, auto);
  margin-top: 7px;
}

.kanban-row-content-icon {
  @include pre100(16px, $gray800);
  margin-left: 2px;
}

.kanban-badge-created {
  background-color: $gray500;
  padding: 2px 12px;
  border-radius: 15px;
  @include pre300(14px, $gray900)
}
</style>