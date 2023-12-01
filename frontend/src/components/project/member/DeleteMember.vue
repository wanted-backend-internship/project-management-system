<script setup lang="ts">
import {onMounted, ref} from "vue";
import {deleteMember, fetchMember} from "../../../api/projcet/MemberApi.ts";
import { faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {library} from "@fortawesome/fontawesome-svg-core";

library.add(faCircleUser);

const props = defineProps({
  boardId: String
});

const bId = ref(props.boardId);
const memberData = ref([]);
const emits = defineEmits(['member-deleted']);

const fetchMemberList = async () => {
  try {
    const response = await fetchMember(bId.value);
    if (response.status == 200) {
      memberData.value = response.data.filter(member => member.role !== 'Host');
    }
  } catch (error) {
    console.error(error);
  }
};

onMounted(fetchMemberList);

const handleDeleteMember = async (memberId: string) => {
  try {
    await deleteMember(memberId);
    emits("member-deleted")
  } catch (error) {
    console.error(error);
  }
};

</script>

<template>
  <div class="board-modal-content">
    <div class="board-modal-title">팀원 관리</div>
    <div v-for="member in memberData" :key="member.memberId"  class="search-result">
      <div class="user-info-container">
        <font-awesome-icon v-if="member.username" icon="fa-solid fa-circle-user" class="user-search-icon"/>
        <div>{{ member.username }}</div>
      </div>
      <button @click="handleDeleteMember(member.memberId)" class="red-button">내보내기</button>
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

.red-button {
  @include button-gray(90px, 30px, 0);
  background-color: $red200;
  @include pre100(18px, $white);
  border: none;
}

.user-search-icon {
  font-size: 25px;
  margin-right: 8px;
  color: $gray700;
}
</style>