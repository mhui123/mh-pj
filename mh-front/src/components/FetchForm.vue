<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">{{ getTitle }}</h1>
      <form @submit.prevent="submitForm" class="form">
        <div>
          <div>
            <label for="email">이메일 :</label>
            <input type="text" id="email" v-model="userEmail" placeholder="이메일" />
          </div>
          <div class="auth-field">
            <input type="text" placeholder="인증번호" v-model="inputKey" id="authKey" :disabled="state === 'before'" />
            <button class="btn" v-if="state === 'before'" @click="requestAuthNum">인증번호 요청</button>
            <button class="btn" v-if="state === 'after'" @click="authenticate" :disabled="inputKey.length === 0">인증</button>
            <button class="btn" v-if="state === 'fail'" @click="requestAuthNum('y')">인증번호 재요청</button>
          </div>
        </div>
        <div class="btn-groups">
          <button @click="goBack" class="btn2">돌아가기</button>
        </div>
      </form>
    </div>
    <Teleport to="body">
      <modal :showModal="showModal" @close="showModal = false">
        <template #header>
          <i class="icon ion-md-close closeModalBtn" @click="showModal = false"></i>
        </template>
        <template #body>
          <FetchResultPwForm v-if="getGubun === 'pw'" @closeModal="modalClose" :userEmail="userEmail"></FetchResultPwForm>
        </template>
      </modal>
    </Teleport>
  </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
import { mapMutations, mapGetters } from 'vuex';
import { callApi } from '@/api/index.js';
import modal from './common/ModalWin.vue';
import FetchResultPwForm from './FetchResultPwForm.vue';
export default {
  data() {
    return {
      username: '',
      userEmail: '',
      password: '',
      isLoading: false,
      state: 'before',
      inputKey: '',
      authSuccess: false,
      showModal: false,
      resultId: '',
    };
  },
  components: {
    modal,
    FetchResultPwForm,
  },
  computed: {
    isUsernameValid() {
      return validateEmail(this.userEmail);
    },
    getTitle() {
      return this.getGubun === 'id' ? '아이디 찾기' : '비밀번호 찾기';
    },
    getGubun() {
      return this.getGubun;
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
    async requestAuthNum(retry) {
      if (this.isUsernameValid) {
        const { data } = await callApi('generateAuthKey', { id: this.userEmail, retry: retry });
        const { result, result_description, authKey } = data;
        this.state = 'after';
        if (result === 200) {
          this.inputKey = authKey;
        } else {
          if (result === 908) {
            this.state = 'fail';
          } else {
            this.state = 'before';
          }
          this.callToast(result_description);
        }
      } else {
        this.callToast('이메일을 올바르게 입력해주세요');
      }
    },
    async authenticate() {
      if (this.isUsernameValid && this.inputKey.length > 0) {
        const { data } = await callApi('validateAuthKey', { id: this.userEmail, inputKey: this.inputKey });
        const { result, result_description } = data;
        result, result_description;
        this.callToast(result_description);
        if (result === 201) {
          this.showModal = true;
        }
      } else {
        this.callToast('이메일을 올바르게 입력해주세요');
      }
    },
    modalClose() {
      this.showModal = false;
      this.userEmail = '';
      this.inputKey = '';
      this.state = 'before';
      this.callToast('로그인페이지로 이동합니다.');
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
.gubun-bar {
  color: gray;
  margin-left: 0.1rem;
  margin-right: 0.1rem;
}
.btn-groups {
  width: 100%;
  margin-left: 0;
}
[class^='btn'] {
  width: 100%;
  margin-bottom: 1rem;
}

.auth-field {
  display: flex;
}
.auth-field input {
  margin-right: 2rem;
}
.auth-field .btn {
  width: 50%;
}
</style>
