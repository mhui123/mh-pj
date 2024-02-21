<template>
  <div>
    <h1>회원가입</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="username">id :</label>
        <input type="text" id="username" name="username" v-model="username" />
      </div>
      <div>
        <label for="password">pw :</label>
        <input type="password" id="password" name="password" v-model="password" />
      </div>
      <button type="submit" :disabled="!isUsernameValid || !password">회원가입</button>
    </form>
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
      registerUser({ id: this.username, pw: this.password });
    },
  },
};
</script>

<style></style>
