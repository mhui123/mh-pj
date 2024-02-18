<template>
  <div>
    <h1>회원가입</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="username">id :</label>
        <input type="text" id="username" v-model="username" />
      </div>
      <div>
        <label for="password">pw :</label>
        <input type="text" id="password" v-model="password" />
      </div>
      <button type="submit" :disabled="!isUsernameValid || !password">회원가입</button>
    </form>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
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
    submitForm() {},
  },
};
</script>

<style></style>
