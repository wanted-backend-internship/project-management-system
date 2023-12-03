<script setup lang="ts">
import {displayBasicInfo, displayResults} from "../../api/statistics/MemberStatistics.ts";
import {onMounted, ref, watchEffect} from "vue";
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
const isSelectedMember = (memberName) => memberNameList.value.includes(memberName);


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

function handleMemberIdList(membername) {
  const index = memberNameList.value.indexOf(membername);

  if (index === -1) {
    memberNameList.value.push(membername);
  } else {
    memberNameList.value.splice(index, 1);
  }
  console.log(memberNameList.value);
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
    const response = await displayResults({
      taskBoxIds: taskBoxIdList.value,
      memberNames: memberNameList.value,
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
const chartRef = ref(null);
const isChartDataLoaded = ref(false);
const chartInstance = ref(null);

const createBarChart = () => {
  const hexToRgba = (hex, alpha) => {
    const r = parseInt(hex.slice(1, 3), 16),
        g = parseInt(hex.slice(3, 5), 16),
        b = parseInt(hex.slice(5, 7), 16);
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
  };

  const backgroundColors = {
    'Backend': hexToRgba('#FF2D55', 0.4),
    'Frontend': hexToRgba('#5856D6', 0.4),
    'Design': hexToRgba('#FF9500', 0.4),
    'QA': hexToRgba('#34C759', 0.4),
    'PM': hexToRgba('#FFCC00', 0.4),
    'Document': hexToRgba('#007AFF', 0.4),
  };

  const borderColors = {
    'Backend': '#B3224A',
    'Frontend': '#3F3D91',
    'Design': '#B36800',
    'QA': '#2A7F36',
    'PM': '#B38B00',
    'Document': '#0056B3',
  };

  const labels = chartData.value.flatMap(item => item.results.map(result => result[0]));
  const uniqueLabels = [...new Set(labels)];

  const datasets = uniqueLabels.map(label => {
    return {
      label: label,
      data: chartData.value.map(item => {
        const entry = item.results.find(result => result[0] === label);
        return entry ? entry[1] : 0;
      }),
      backgroundColor: backgroundColors[label],
      borderColor: borderColors[label],
      borderWidth: 1
    };
  });

  if (chartInstance.value) {
    chartInstance.value.destroy();
    chartInstance.value = null;
  }

  if (chartRef.value) {
    const ctx = chartRef.value.getContext('2d');
    chartInstance.value = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: chartData.value.map(item => item.memberName),
        datasets: datasets
      },
    });
  } else {
    console.error('Canvas element is not available.');
  }
};

watchEffect(() => {
  if (isChartDataLoaded.value && chartData.value.length > 0) {
    createBarChart();
  }
});

</script>

<template>
<div class="report-container">
  <div class="report-title">
    멤버 통계
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
    <div class="report-form-label" style="margin-top: 25px">멤버 선택 하기</div>
    <div class="response-container">
      <div v-for="member in basicInfo.memberDetails"
           :key="member.memberId"
           :class="['badge', { 'badge-selected': isSelectedMember(member.membername) }]"
           @click="handleMemberIdList(member.membername)">
        {{ member.membername }}
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
  <div v-if="isChartDataLoaded" style="margin-top: 70px">
    <canvas ref="chartRef"></canvas>
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
</style>

