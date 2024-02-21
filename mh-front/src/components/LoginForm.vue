<template>
  <div>
    <h1>로그인</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="username">id :</label>
        <input type="text" id="username" v-model="username" />
      </div>
      <div>
        <label for="password">pw :</label>
        <input type="text" id="password" v-model="password" />
      </div>
      <button type="submit" :disabled="!isUsernameValid || !password">로그인</button>
      <button @click="test">test</button>
    </form>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
import { testAPI, loginUser } from '@/api/index';
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
    async submitForm() {
      const userData = { id: this.username, pw: this.password };
      await loginUser(userData);
    },
    test() {
      testAPI({ username: 123, password: 456 });
    },
  },
};
</script>

<style></style>
