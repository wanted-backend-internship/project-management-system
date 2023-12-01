<script setup lang="ts">
import { ref } from "vue";
import {addMember, searchUser} from "../../../api/projcet/MemberApi.ts";
import { faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {library} from "@fortawesome/fontawesome-svg-core";

library.add(faCircleUser);

const props = defineProps({
  boardId: String
});

const bId = ref(props.boardId);
const userEmail = ref('');
const searchResult = ref('');
const emits = defineEmits(['member-added']);

const handleProject = async () => {
  try {
    const response = await searchUser({
      email: userEmail.value
    });

    if (response.status == 200) {
      searchResult.value = response.data;
    }

  } catch (error) {
    console.error(error);
  }
};

const handleAddMember = async () => {
  try {
    const response = await addMember({
      boardId: bId.value,
      userId: searchResult.value.userId
    })

    if (response.status == 200) {
      emits("member-added");
    }
  } catch (error) {
    console.error(error);
  }
};

</script>

<template>
  <div class="board-modal-content">
    <div class="board-modal-title">
      유저 검색
    </div>
    <form>
        <div style="margin-top: 10px">
          <div class="board-modal-label">유저 이메일을 입력해 주세요.</div>
          <div class="flex-low-content">
            <input v-model="userEmail" type="text">
            <button @click.prevent="handleProject" class="board-modal-button" type="submit">검색</button>
          </div>
        </div>
    </form>
    <div class="search-result">
      <div class="user-info-container">
        <font-awesome-icon v-if="searchResult.username" icon="fa-solid fa-circle-user" class="user-search-icon"/>
        <div>{{ searchResult.username }}</div>
      </div>
      <div>
      <button v-if="searchResult.username" class="gray-button" @click="handleAddMember">멤버로 추가하기</button>
      </div>
    </div>
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
  @include button-blue(70px, 34px, 0);
  @include pre200(16px, white);
  border: none;
  margin: 8px 0 0 10px;
}

.flex-low-content {
  @include container(row, flex-start, flex-start, 100%, auto);
}

.search-result {
  @include container(row, space-between, center, 100%, auto);
  @include pre200(22px, $black);
  margin-top: 20px;
}

.user-info-container {
  @include container(row, flex-start, center, auto, auto);
}

.gray-button {
  @include button-gray(120px, 34px, 0);
  border: none;
}

.user-search-icon {
  font-size: 25px;
  margin-right: 8px;
  color: $gray700;
}
</style>