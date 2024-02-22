<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">회원가입</h1>
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="username">id :</label>
          <input type="text" id="username" name="username" v-model="username" />
          <p class="validation-text">
            <span class="warning" v-if="!isUsernameValid && username"> 이메일 형식으로 입력해주세요 </span>
          </p>
        </div>
        <div>
          <label for="password">pw :</label>
          <input type="password" id="password" name="password" v-model="password" @keyup.enter="submitForm" />
        </div>
        <div class="btn-groups">
          <button class="btn" type="submit" :disabled="!isUsernameValid || !password">회원가입</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
import { registerUser } from '@/api/index';
export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  computed: {
    isUsernameValid() {
      return validateEmail(this.username);
    },
  },
  methods: {
    submitForm() {
      if (this.isUsernameValid || this.password) {
        registerUser({ id: this.username, pw: this.password });
      }
    },
  },
};
</script>

<style></style>
