<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">로그인</h1>
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="username">id</label>
          <input type="text" id="username" v-model="username" />
        </div>
        <div>
          <label for="password">pw</label>
          <input type="password" id="password" v-model="password" />
        </div>
        <div class="btn-groups">
          <button class="btn" type="submit" :disabled="!isUsernameValid || !password">로그인</button>
          <div class="link-narashi">
            <li class="suggestJoin" @click="fetchInfo('id')">아이디 찾기</li>
            <span class="gubun-bar">|</span>
            <li class="suggestJoin" @click="fetchInfo('pw')">비밀번호 찾기</li>
            <span class="gubun-bar">|</span>
            <router-link to="/signup" class="suggestJoin">회원가입</router-link>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
// import { testAPI, loginUser } from '@/api/index';
import { mapActions, mapMutations } from 'vuex';
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
    ...mapMutations(['setGubun']),
    ...mapActions(['LOGIN']),
    async submitForm() {
      const userData = { username: this.username, password: this.password };
      const response = await this.LOGIN(userData);
      const { state, token } = response;
      console.log(token);
      // await loginTest(userData);
      if (state === 200) {
        console.log('login');
        this.emitter.emit('show:toast', 'LOGIN SUCCESS');
        // this.$router.push('/main');
      } else if (state === 500) {
        this.emitter.emit('show:toast', 'PASSWORD IS NOT MATCHED');
      } else {
        this.emitter.emit('show:toast', 'ID IS NOT EXIST');
      }
    },
    fetchInfo(value) {
      this.setGubun(value);
      this.$router.push('/fetch');
      // testAPI({ username: 123, password: 456 });
    },
  },
};
</script>

<style scoped>
.btn-groups .link-narashi {
  display: flex;
  justify-content: center;
}
.gubun-bar {
  color: gray;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
}
.btn {
  background: #fe9616;
  padding: 0.5rem 1.5rem;
  font-weight: 700;
  border-radius: 0.25rem;
  border: 0 solid #dae1e7;
  color: white;
  width: 100%;
  margin-bottom: 0.5rem;
}
[class^='btn'] {
  margin-left: 0rem;
}
</style>
