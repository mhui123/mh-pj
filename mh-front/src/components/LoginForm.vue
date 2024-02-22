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
      isLoading: false,
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
      const { state } = await this.LOGIN(userData);
      // await loginTest(userData);
      if (state === 200) {
        console.log('login');
        this.emitter.emit('show:toast', 'LOGIN SUCCESS');
        this.$router.push('/main');
      } else if (state === 500) {
        this.emitter.emit('show:toast', 'PASSWORD IS NOT MATCHED');
      } else {
        this.emitter.emit('show:toast', 'ID IS NOT EXIST');
      }
    },
    test() {
      // testAPI({ username: 123, password: 456 });
    },
  },
};
</script>

<style></style>
