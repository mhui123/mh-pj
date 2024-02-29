<template>
  <div>
    <form @submit.prevent class="form">
      <label for="">새 비밀번호</label>
      <input type="password" v-model="newPw" />
      <p class="validation-text">
        <span class="warning" v-if="!isNewPwValid && newPw.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
      </p>
      <label for="">새 비밀번호 확인</label>
      <input type="password" v-model="newPw2" />
      <p class="validation-text">
        <span class="warning" v-if="!isNewPw2Valid && newPw2.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
      </p>
      <div>
        <button class="btn" @click="changePw">변경하기</button>
      </div>
    </form>
  </div>
</template>

<script>
import { validatePw } from '@/utils/validation';
import { callApi } from '@/api/index';
export default {
  data() {
    return {
      newPw: '',
      newPw2: '',
    };
  },
  props: {
    userEmail: String,
  },
  computed: {
    isNewPwValid() {
      return validatePw(this.newPw);
    },
    isNewPw2Valid() {
      return validatePw(this.newPw2);
    },
  },
  methods: {
    chkPwState() {
      if (this.newPw !== this.newPw2) {
        this.callToast('비밀번호는 동일하게 입력해야 합니다.');
        return false;
      }
      return true;
    },
    async changePw() {
      const userId = this.userEmail;
      let payload = { id: userId, newPw: this.newPw, mode: 'fetch' };
      if (this.chkPwState()) {
        if (this.isNewPwValid && this.isNewPw2Valid) {
          const { data } = await callApi('changePassword', payload);
          const { result, result_description } = data;
          if (result === 202) {
            this.newPw = '';
            this.newPw2 = '';
            this.$emit('closeModal');
            // this.showModal = false;
            this.callToast(result_description);
          } else {
            this.callToast(result_description);
          }
        }
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
  },
};
</script>

<style scoped>
.form {
  width: 100%;
}
</style>
