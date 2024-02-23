<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">{{ getTitle }}</h1>
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
          <button @click="goBack" class="btn2">돌아가기</button>
          <button class="btn" type="submit" :disabled="!isUsernameValid || !password">로그인</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
// import { testAPI, loginUser } from '@/api/index';
import { mapMutations, mapGetters } from 'vuex';
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
    getTitle() {
      console.log(this.getGubun);
      return this.getGubun === 'id' ? '아이디 찾기' : '비밀번호 찾기';
    },
    ...mapGetters(['getGubun']),
  },
  methods: {
    ...mapMutations(['clearGubun']),
    async submitForm() {},
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
.btn-groups .link-narashi {
  display: flex;
  justify-content: center;
}
.gubun-bar {
  color: gray;
  margin-left: 0.1rem;
  margin-right: 0.1rem;
}
</style>
