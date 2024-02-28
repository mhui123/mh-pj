<template>
  <div class="form-container">
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
          <input type="password" id="password" name="password" v-model="password" />
          <p class="validation-text">
            <span class="warning" v-if="!isPwValid && password.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
          </p>
        </div>
        <div class="btn-groups">
          <button class="btn" type="submit" :disabled="!isUsernameValid || !password">회원가입</button>
          <button @click="goBack" class="btn2">돌아가기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { validateEmail, validatePw } from '@/utils/validation';
import { callApi } from '@/api/index';
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
    isPwValid() {
      return validatePw(this.password);
    },
  },
  methods: {
    async submitForm() {
      if (this.isUsernameValid || this.password) {
        const { data } = await callApi('signin', { id: this.username, pw: this.password });
        console.log(data);
        const { result, result_description } = data;
        if (result === 200) {
          this.$router.push('/login');
        }
        this.callToast(result_description);
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
    goBack() {
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
[class^='btn'] {
  width: 100%;
  margin-top: 1rem;
}
</style>
