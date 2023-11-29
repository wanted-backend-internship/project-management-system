<script setup lang="ts">
import { ref } from 'vue';
import {registerUser} from "../../api/AuthApi";
import router from "../../router";


const email = ref('');
const password = ref('');
const username = ref('');

function isPasswordValid(password) {
  const lengthRegex = /^.{10,}$/;
  const uppercaseRegex = /[A-Z]/;
  const specialCharRegex = /[!@#$%^&*]/;

  return (
      lengthRegex.test(password) &&
      uppercaseRegex.test(password) &&
      specialCharRegex.test(password)
  );
}

const handleRegister = async () => {
  if (!isPasswordValid(password.value)) {
    alert('비밀번호는 대문자와 특수문자를 포함하여 10 자 이상 이여야 합니다.');
    return;
  }

  const userData = {
    email: email.value,
    password: password.value,
    username: username.value,
  };

  try {
    const result = await registerUser(userData);
    if (result) {
      await router.push('/login');
    }
  } catch (error) {
    console.log(error);
  }
};
</script>

<template>
<div class="register-container">
  <div class="title">
    회원 가입
  </div>
  <form style="margin-top: 30px">
    <div>
      <div>
        <div class="label">이메일을 입력해주세요.</div>
        <input v-model="email" type="email">
      </div>
      <div>
        <div class="label">닉네임을 입력해주세요.</div>
        <input v-model="username" type="text">
      </div>
      <div>
        <div class="label">대문자와 특수문자를 포함, <br />
          10 자리 이상의 비밀번호를 입력해주세요.</div>
        <input v-model="password" type="password">
      </div>
      <div style="margin-top: 26px" class="button-container">
        <button @click.prevent="handleRegister" class="blue-button" type="submit" style="margin-right: 15px">회원 가입</button>
        <button class="gray-button" type="submit">
          <router-link to="/login" style="text-decoration: none; color: inherit">
            로그인
          </router-link>
        </button>
      </div>
    </div>
  </form>
</div>
</template>

<style lang="scss">
@import "../../styles/common/container.scss";
@import "../../styles/common/font.scss";
@import "../../styles/common/button.scss";
@import "../../styles/common/input.scss";
@import "../../styles/colors/_color.scss";

.register-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  overflow: hidden;
  padding: 60px 0 0 60px;
}

.title {
  @include pre400(40px, $black);
}

.label {
  @include pre100(20px, $black);
}

input {
  @include input(325px, 32px, 20px, 0 10px, 8px);
  margin: 10px 0 20px 0;
}

.button-container {
  @include container(row, flex-start, flex-start, 100%, auto);
}

.blue-button {
  @include button-blue(100px, 38px, 0);
  @include pre200(16px, white);
}

.gray-button {
  @include button-gray(100px, 38px, 0);
  @include pre200(16px, $black);
}
</style>