<script setup lang="ts">

</script>

<template>
  <div class="content-container">
    <router-view />
  </div>
</template>

<style lang="scss">
@import '../styles/common/container.scss';
@import '../styles/colors/_color.scss';

.content-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  background-color: transparent;
}
</style>