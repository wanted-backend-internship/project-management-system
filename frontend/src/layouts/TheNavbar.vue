<script setup lang="ts">
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTableColumns, faChartSimple, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faTableColumns, faChartSimple, faArrowRightFromBracket);
</script>

<template>
  <div class="nav-container">
    <div class="logo-container">
      <img src="../assets/image/big_logo.png" class="logo-image" />
      <div class="line"></div>
    </div>

    <!-- 메뉴 리스트 -->
    <div class="menu-container">
      <div class="menu-contents">
        <router-link to="/post" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-solid fa-table-columns" class="icon"/>
          <span>프로젝트</span>
        </router-link>

        <router-link to="/message" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-solid fa-chart-simple" class="icon"/>
          <span>통계</span>
        </router-link>
      </div>
      <router-link to="/logout" class="logout-btn"  style="margin-top: 40px;">
        <font-awesome-icon icon="fa-solid fa-arrow-right-from-bracket" class="icon"/>
        <span>로그 아웃</span>
      </router-link>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../styles/common/container.scss';
@import '../styles/common/image.scss';
@import '../styles/colors/_color.scss';
@import "../styles/common/font.scss";

.nav-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  background-color: $white;
  border-right: 1px solid $black;
  backdrop-filter: blur(20px);
}

.logo-container {
  @include container(column, flex-start, flex-start, 100%, auto);
}

.logo-image {
  @include image(180px, 20px 0 20px 20px);
}

.img-container,
.menu-container {
  @include container(column, center, center, 100%, auto);
  margin-top: 40px;
}

.menu-contents {
  @include container(column, center, center, 100%, auto);
}

.menu-lists {
  @include container(row, flex-start, center, 70%, auto);
  @include pre200(20px, #1E1E1C);
  height: 48px;
  padding: 0 20px;
  margin-bottom: 5px;
  border-radius: 8px;
  text-decoration: none;
}

.logout-btn {
  @include container(row, flex-start, center, 70%, auto);
  @include pre200(20px, $gray600);
  height: 48px;
  padding: 0 20px;
  margin-bottom: 5px;
  border-radius: 8px;
  text-decoration: none;
}

.menu-lists:hover {
  background-color: $blue100;
  color: white;
  text-decoration: none;
}

.profile-image {
  @include image(123px, 85px 0 50px 0);
}

.line {
  width: 100%;
  height: 1px;
  border-top: 1px $black solid;
}

.icon {
  margin-right: 10px;
}

</style>