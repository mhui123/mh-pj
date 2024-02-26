<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">마이페이지</h1>
      <table>
        <tbody>
          <tr>
            <td>
              <label for="pw">비밀번호 </label>
            </td>
            <td>
              <button class="btn" @click="showModal = true">비밀번호 변경</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <Teleport to="body">
      <modal :showModal="showModal" @close="showModal = false">
        <template #header>
          <i class="icon ion-md-close closeModalBtn" @click="showModal = false"></i>
        </template>
        <template #body>
          <div>
            <form @submit.prevent class="form">
              <label for="">기존 비밀번호</label>
              <input type="password" class="{warn-input : !warnInput}" v-model="asPw" id="asPw" @keypress="warnInput = true" />
              <p class="validation-text">
                <span class="warning" v-if="!isAsPwValid && asPw.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
              </p>
              <label for="">새 비밀번호</label>
              <input type="password" v-model="newPw" />
              <p class="validation-text">
                <span class="warning" v-if="!isNewPwValid && newPw.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
              </p>
              <label for="">새 비밀번호 확인</label>
              <input type="password" v-model="newPw2" />
              <p class="validation-text">
                <span class="warning" v-if="!isNewPw2Valid && newPw2.length > 3"> {{ pw2Warn }} </span>
              </p>
              <div>
                <button class="btn" @click="changePw">변경하기</button>
              </div>
            </form>
          </div>
        </template>
      </modal>
    </Teleport>
  </div>
</template>

<script>
import modal from './common/ModalWin.vue';
import { validatePw } from '@/utils/validation';
import { mapGetters } from 'vuex';
import { changePw } from '@/api/index';
export default {
  components: {
    modal,
  },
  data() {
    return {
      asPw: '',
      newPw: '',
      newPw2: '',
      showModal: false,
      warnInput: false,
      pw2Warn: '',
    };
  },
  computed: {
    isAsPwValid() {
      return validatePw(this.asPw);
    },
    isNewPwValid() {
      return validatePw(this.newPw);
    },
    isNewPw2Valid() {
      return validatePw(this.newPw2);
    },
    ...mapGetters(['getUsername']),
  },
  methods: {
    test() {
      document.getElementById('asPw').focus();
    },
    chkPwState() {
      if (this.asPw === this.newPw) {
        this.callToast('기존과 동일한 비밀번호 입니다.');
        return false;
      } else if (this.newPw !== this.newPw2) {
        this.callToast('비밀번호는 동일하게 입력해야 합니다.');
        return false;
      }
      return true;
    },
    async changePw() {
      this.pw2Warn = '비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요';
      const userId = this.getUsername;
      let payload = { id: userId, asPw: this.asPw, newPw: this.newPw };
      if (this.chkPwState()) {
        if (this.isNewPwValid && this.isNewPw2Valid) {
          const { data } = await changePw(payload);
          const { result, result_description } = data;
          if (result === 200) {
            this.asPw = '';
            this.newPw = '';
            this.newPw2 = '';
            this.showModal = false;
            this.callToast(result_description);
          } else if (result === 903) {
            document.getElementById('asPw').focus();
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
input {
  width: 15rem;
}
.form .validation-text {
  font-size: 1rem;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 14rem;
}
.contents {
  padding-left: 83px;
}
.warn-input {
  border-color: red;
  border-width: 2px;
}
</style>
