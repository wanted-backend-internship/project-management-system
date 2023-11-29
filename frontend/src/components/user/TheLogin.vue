<script setup lang="ts">
import { ref } from 'vue';
import {useAuthStore} from "../../store/AuthStore";
import {localLogin} from "../../api/AuthApi";
import router from "../../router";
import {loginUserInfo} from "../../api/UserInfo";

const email = ref('');
const password = ref('');
const authStore = useAuthStore();

const handleLogin = async () => {
  const userData = {
    email: email.value,
    password: password.value,
  };

  try {
    const response = await localLogin(userData);
    if (response.status == 200) {
      authStore.setLoginStatus(true);
      console.log(authStore.isLoggedIn);

      const userInfoResponse = await loginUserInfo();
      authStore.setUserInfo(userInfoResponse);
      // console.log(authStore.userInfo);

      await router.push('/project');
    }
  } catch (error) {
    console.log(error);
  }
};
</script>

<template>
  <div class="register-container">
    <div class="title">
      로그인
    </div>
    <form style="margin-top: 30px">
      <div>
        <div>
          <div class="label">이메일을 입력해주세요.</div>
          <input v-model="email" type="email">
        </div>
        <div>
          <div class="label">비밀번호를 입력해주세요.</div>
          <input v-model="password" type="password">
        </div>
        <div style="margin-top: 26px" class="button-container">
          <button @click.prevent="handleLogin" class="blue-button" type="submit" style="margin-right: 15px">로그인</button>
          <button class="gray-button" type="submit">
            <router-link to="/register" style="text-decoration: none; color: inherit">
              회원 가입
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