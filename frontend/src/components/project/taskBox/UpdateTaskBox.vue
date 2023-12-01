<script setup lang="ts">
import {updateTaskBox} from "../../../api/projcet/TaskBoxApi";
import { ref } from "vue";

const props = defineProps({
  taskBoxId: String
})

const title = ref('');
const id = ref(props.taskBoxId);

const emits = defineEmits(['taskbox-updated']);

const handleProject = async () => {
  try {
    const response = await updateTaskBox({
      taskBoxTitle: title.value,
      taskBoxId: id.value
    });

    if (response.status === 200) {
      emits('taskbox-updated');
    }
  } catch (error) {
    console.error(error);
  }
};

</script>

<template>
  <div class="board-modal-content">
    <div class="board-modal-title">
      작업 박스 수정
    </div>
    <form>
      <div>
        <div style="margin-top: 10px">
          <div class="board-modal-label">작업 박스 이름을 입력해주세요.</div>
          <input v-model="title" type="text">
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
</style>