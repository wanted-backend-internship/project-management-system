<script setup lang="ts">
import { ref } from "vue";
import {updateTask} from "../../../api/projcet/TaskApi";

const props = defineProps({
  taskId: String
});

const tId = ref(props.taskId);
const taskTitle = ref('');
const taskTag = ref('');
const taskDueDate = ref('');
const taskWorkHour = ref('');

const emits = defineEmits(['task-updated']);

const handleProject = async () => {
  try {
    const response = await updateTask({
      taskId: tId.value,
      title: taskTitle.value,
      tag: taskTag.value,
      dueDate: taskDueDate.value,
      workHour: taskWorkHour.value,
    });

    if (response.status === 200) {
      emits('task-updated');
    }
  } catch (error) {
    console.error(error);
  }
};

</script>

<template>
  <div class="board-modal-content">
    <div class="board-modal-title">
      작업 수정
    </div>
    <form>
      <div>
        <div style="margin-top: 10px">
          <div class="board-modal-label">작업 이름을 입력해주세요.</div>
          <input v-model="taskTitle" type="text">
        </div>
        <div style="margin-top: 10px">
          <div class="board-modal-label">작업 만료기한을 입력해주세요.</div>
          <input v-model="taskDueDate" type="date">
        </div>
        <div style="margin-top: 10px">
          <div class="board-modal-label">작업 시간을 입력해주세요.</div>
          <input v-model="taskWorkHour" type="text">
        </div>
        <div style="margin-top: 10px">
          <div class="board-modal-label">태그를 선택해주세요.</div>
          <select v-model="taskTag" class="select">
            <option value="Frontend">Frontend</option>
            <option value="Backend">Backend</option>
            <option value="Design">Design</option>
            <option value="QA">QA</option>
            <option value="PM">PM</option>
            <option value="Document">Document</option>
          </select>
        </div>
        <button @click.prevent="handleProject" class="board-modal-button" type="submit">수정 하기</button>
      </div>
    </form>
  </div>
</template>

<style scoped lang="scss">
@import "../../../styles/common/container";
@import "../../../styles/common/font";
@import "../../../styles/common/button";
@import "../../../styles/common/input";
@import "../../../styles/colors/color";

.board-modal-title {
  margin-top: 10px;
  @include pre300(30px, $black);
}

.board-modal-label {
  @include pre100(20px, $black);
}

.board-modal-button {
  @include button-blue(100px, 38px, 0);
  @include pre200(16px, white);
  margin-top: 15px;
  border: none;
}

.select {
  @include input(325px, 32px, 20px, 0 10px, 8px);
  margin: 10px 0 20px 0;
}
</style>