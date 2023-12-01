<script setup lang="ts">
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faCircleXmark);

const props = defineProps({
  isOpen: Boolean
});

const emits = defineEmits(['close']);

const handleClose = () => {
  emits('close');
};

</script>

<template>
  <div v-if="props.isOpen">
    <div class="modal-overlay">
      <div class="modal-content">
        <div class="modal-button">
          <div class="modal-button">
            <button @click="handleClose">
              <font-awesome-icon icon="fa-solid fa-circle-xmark" class="close-icon"/>
            </button>
          </div>
        </div>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
@import "../../styles/common/container.scss";
@import "../../styles/common/button.scss";
@import "../../styles/colors/_color.scss";

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-start;
  justify-content: center;
}

.modal-content {
  margin-top: 100px;
  background: white;
  padding: 35px 40px 60px 40px;
  border-radius: 12px;
  min-width: 350px;
  width: auto;
  max-height: 700px;
  overflow-y: scroll;
}

.modal-button {
  @include container(row, flex-end, center, 100%, 100%)
}

.modal-button button {
  border: none;
  background-color: transparent;
  padding: 0;
  margin: 0;
}

.close-icon{
  font-size: 23px;
  color: $gray600;
}
</style>