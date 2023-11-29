<script setup lang="ts">
import TheNavbar from "./TheNavbar.vue";
import TheContent from "./TheContent.vue";
</script>

<template>
  <div class="container">
    <TheNavbar/>
    <TheContent/>
  </div>
</template>

<style lang="scss">
.container {
  width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;

  display: grid;
  grid-template-columns: 1fr 4fr;
}
</style>