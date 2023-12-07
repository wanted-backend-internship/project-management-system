<script setup lang="ts">
import {displayBasicInfo, displayTeamResults} from "../../api/statistics/Statistics.ts";
import {nextTick, onMounted, ref, watchEffect} from "vue";
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

const props = defineProps({
  boardId: String
});

const boardId = ref(props.boardId);
const basicInfo = ref([]);
const taskBoxIdList = ref([]);
const memberNameList = ref([]);
const start = ref('');
const end = ref('');
const isAllSelected = ref(false);

const isSelectedTaskBox = (boxId) => taskBoxIdList.value.includes(boxId);

const handleSubmit = (event) => {
  event.preventDefault();
};

function handleTaskBoxIdList(boxId) {
  const index = taskBoxIdList.value.indexOf(boxId);

  if (index === -1) {
    taskBoxIdList.value.push(boxId);
  } else {
    taskBoxIdList.value.splice(index, 1);
  }
  console.log(taskBoxIdList.value);
}

const toggleAllSelection = () => {
  isAllSelected.value = !isAllSelected.value;

  if (isAllSelected.value) {
    taskBoxIdList.value = basicInfo.value.taskBoxDetails.map(box => box.taskBoxId);
  } else {
    taskBoxIdList.value = [];
  }
};

const fetchBasicInfo = async () => {
  try {
    const response = await displayBasicInfo(boardId.value);
    basicInfo.value = response.data;
    console.log(response.data);

  } catch (error) {
    console.error(error);
  }
};

onMounted(fetchBasicInfo);

const handleMemberReport = async () => {
  try{
    const response = await displayTeamResults({
      taskBoxIds: taskBoxIdList.value,
      memberNames: basicInfo.value.memberDetails.map(member => member.membername),
      startDate: start.value,
      endDate: end.value
    });

    chartData.value = response.data;
    isChartDataLoaded.value = true;
    console.log(response.data);

    taskBoxIdList.value = [];
    memberNameList.value = [];

  } catch (error) {
    console.error(error);
  }
};

const chartData = ref([]);
const isChartDataLoaded = ref(false);
const categoryData = ref({});

const createPieChart = () => {
  nextTick(() => {
    Object.entries(categoryData.value).forEach(([category, data], index) => {
      const canvasId = `chart-${index}`;
      const canvas = document.getElementById(canvasId);
      if (canvas) {
        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
          type: 'pie',
          data: {
            labels: data.map(d => d.memberName),
            datasets: [{
              data: data.map(d => d.value),
              backgroundColor: [
                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'
              ]
            }]
          },
          options: {
            responsive: true,
            title: {
              display: true,
              text: `${category} Contributions`
            }
          }
        });
      }
    });
  });
};

watchEffect(() => {
  // 카테고리별로 데이터 분류
  if (chartData.value && chartData.value.length > 0) {
    const newCategoryData = {};
    chartData.value.forEach(member => {
      member.results.forEach(([category, value]) => {
        if (!newCategoryData[category]) {
          newCategoryData[category] = [];
        }
        newCategoryData[category].push({ memberName: member.memberName, value });
      });
    });
    categoryData.value = newCategoryData;
  }

  if (isChartDataLoaded.value && Object.keys(categoryData.value).length > 0) {
    createPieChart();
  }
});
</script>

<template>
  <div class="report-container">
    <div class="report-title">
      전체 통계
      <p>
        ✅️ 멤버들의 작업 시간을 태그별로 확인할 수 있습니다. <br>
        ✅️ 날짜를 선택 하지 않으면 전체 날짜로 조회 됩니다. <br>
        ✅️ 모든 작업 박스의 통계를 보고 싶을 경우 전체를 선택해주세요. <br>
      </p>
    </div>
    <form @submit="handleSubmit">
      <div class="report-form-label" style="margin-top: 15px">작업 박스 선택 하기</div>
      <div class="response-container">
        <div
            :class="['badge', { 'badge-selected': isAllSelected }]"
            @click="toggleAllSelection">전체</div>
        <div v-for="taskBox in basicInfo.taskBoxDetails"
             :key="taskBox.taskBoxId"
             :class="['badge', { 'badge-selected': isSelectedTaskBox(taskBox.taskBoxId) }]"
             @click="handleTaskBoxIdList(taskBox.taskBoxId)">
          {{ taskBox.taskBoxTitle }}
        </div>
      </div>
      <div class="report-form-label" style="margin-top: 25px">날짜 선택 하기</div>
      <div class="input-container">
        <p>
          ⏰ 시작 날짜와 끝 날짜를 순서대로 선택해주세요.
        </p>
        <input v-model="start" type="date">
        <input v-model="end" type="date">
      </div>
      <button class="board-modal-button" type="submit" @click="handleMemberReport">결과 확인 하기</button>
    </form>
    <div v-if="isChartDataLoaded" style="margin-top: 70px" class="chart-container">
      <div v-for="(category, index) in Object.keys(categoryData)" :key="index" class="chart-container" style="width: 60%; margin-bottom: 70px">
        <p v-if="index != null">{{ category }}</p>
        <canvas :id="'chart-' + index"></canvas>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
@import "../../styles/common/container";
@import "../../styles/common/font";
@import "../../styles/common/button";
@import "../../styles/common/input";
@import "../../styles/colors/color";

.report-container {
  @include container(column, flex-start, flex-start, auto , 100%);
  overflow-y: scroll;
}

.report-title {
  @include pre300(30px, $black)
}

.report-title p, .input-container p {
  @include pre100(16px, $black);
  margin: 5px 0 10px 2px;
}

.response-container {
  @include container(row, flex-start, center, 100%, auto);
}

.report-form-label {
  @include pre300(24px, $black);
  margin-bottom: 5px;
}

.badge {
  @include pre100(16px, $black);
  border-radius: 20px;
  border: 1px solid $black;
  margin-right: 8px;
  background-color: $white;
}

.badge-selected {
  background-color: $gray200;
}

.input-container {
  @include container(column, flex-start, flex-start, 100%, auto);
}

.input-container input {
  margin: 0 0 10px 0;
  height: 30px;
  @include pre100(17px, $black)
}

.board-modal-button {
  @include button-blue(120px, 34px, 0);
  @include pre200(16px, white);
  margin-top: 20px;
  border: none;
}

.chart-container {
  @include container(column, flex-start, center, 100%, auto);
}

.chart-container p {
  @include pre300(24px, $black);
}
</style>

