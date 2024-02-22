<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">로그인</h1>
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="username">id :</label>
          <input type="text" id="username" v-model="username" />
        </div>
        <div>
          <label for="password">pw :</label>
          <input type="password" id="password" v-model="password" />
        </div>
        <div class="btn-groups">
          <button class="btn" type="submit" :disabled="!isUsernameValid || !password">로그인</button>
          <router-link to="/signup" class="suggestJoin">아직 계정이 없으신가요?</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
// import { testAPI, loginUser } from '@/api/index';
import { mapActions } from 'vuex';
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
    ...mapActions(['LOGIN']),
    async submitForm() {
      const userData = { username: this.username, password: this.password };
      const result = await this.LOGIN(userData);
      console.log(result);
      // await loginTest(userData);
      this.$router.push('/main');
    },
    test() {
      // testAPI({ username: 123, password: 456 });
    },
  },
};
</script>

<style></style>
