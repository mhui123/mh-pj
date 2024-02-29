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
    <SearchForm :mode="mode"></SearchForm>
    <div class="btn-groups">
      <div>
        <button class="btn" @click="deleteWords">삭제</button>
      </div>
    </div>
    <div class="lower-container table-wrap" v-if="mode === 'word'">
      <table class="table">
        <thead>
          <tr>
            <th class="chkBox"></th>
            <th>키워드</th>
            <th>작성자</th>
            <th>수정일시</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="word in wordList" :key="word.id">
            <td class="chkBox"><input class="chkInput" type="checkbox" :name="word.id" :id="word.id" /></td>
            <td>{{ word['infokey'] }} {{ word['info_kr'] }}</td>
            <td>{{ word['creator'] }}</td>
            <td>{{ this.$filters.dateFilter(word['updateDate']) }}</td>
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
import { mapGetters, mapMutations } from 'vuex';
import { callApi } from '@/api/index';
import SearchForm from './SearchForm.vue';
export default {
  components: {
    modal,
    SearchForm,
  },
  data() {
    return {
      asPw: '',
      newPw: '',
      newPw2: '',
      showModal: false,
      warnInput: false,
      pw2Warn: '',
      mode: 'word',
      checkedUsers: [],
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
    wordList() {
      return this.getWordList;
    },
    username() {
      return this.getUsername;
    },
    ...mapGetters(['getUsername', 'getWordList', 'getUsername']),
  },
  created() {
    this.fetchMyWords();
  },
  methods: {
    ...mapMutations(['setWordList', 'clearWordList', 'spliceWordList']),
    async deleteWords() {
      await this.getCheckedIds();
      if (this.checkedUsers.length > 0) {
        let filteredIds = this.checkedUsers.map(m => Number.parseInt(m.id));
        const { data } = await callApi(`delWords`, { ids: filteredIds });
        let result_description = data['result_description'];
        this.callToast(result_description);
        this.initCheckboxes();
        filteredIds.forEach(e => {
          let arr = this.getWordList;
          let idx = arr.map(m => m.id).indexOf(e);
          this.spliceWordList(idx);
        });
      } else {
        this.callToast('삭제할 대상을 선택해주세요');
      }
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
    async fetchMyWords() {
      this.clearWordList();
      const { data } = await callApi('getMyWordList', { id: this.username });
      const { list } = data;
      this.setWordList(list);
    },
    async manageWord() {
      this.clearWordList();
      this.modeControl('word');
      const { data } = await callApi('getAllWordList');
      const { list } = data;
      this.setWordList(list);
    },
    async changePw() {
      this.pw2Warn = '비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요';
      const userId = this.getUsername;
      let payload = { id: userId, asPw: this.asPw, newPw: this.newPw };
      if (this.chkPwState()) {
        if (this.isNewPwValid && this.isNewPw2Valid) {
          const { data } = await callApi('changePassword', payload);
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
    async getCheckedIds() {
      let checkBoxes = document.getElementsByClassName('chkInput');
      checkBoxes = Array.from(checkBoxes);
      this.checkedUsers = checkBoxes.filter(x => x.checked);
    },
    initCheckboxes() {
      let checkBoxes = document.getElementsByClassName('chkInput');
      checkBoxes = Array.from(checkBoxes);
      checkBoxes
        .filter(x => x.checked)
        .forEach(e => {
          e.checked = false;
        });
      this.checkedUsers = [];
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

.btn-groups {
  width: 100%;
  text-align: center;
  margin: 1rem auto auto;
}
.btn-groups [class^='btn'] {
  margin: 0 0.5rem 1rem;
}
.contents {
  padding-left: 83px;
}
.warn-input {
  border-color: red;
  border-width: 2px;
}
.table-wrap {
  overflow-y: auto;
  background-color: white;
  max-width: 500px;
  margin: 0 auto 0;
  box-shadow: 0 20px 20px rgba(0, 0, 0, 0.08);
}
.table-wrap .table {
  width: 100%;
  border-top: 0.1rem solid;
  border-color: #111;
  table-layout: fixed;
  border-collapse: collapse;
  border-spacing: 0;
  text-align: center;
}
.lower-container {
  display: flex;
  justify-content: center;
}
/* colgroup {
  display: table-column-group;
} */

thead {
  /* display: table-header-group; */
  vertical-align: middle;
  /* border-color: inherit; */
}
th {
  /* display: table-cell; */
  font-weight: bold;
  text-align: -initial -center;
}

tbody > tr {
  /* display: table-row-group; */
  font-size: 15px;
}
tr {
  /* display: table-row; */
}

.chkBox,
.chkInput {
  width: 2rem;
  margin: 0 0.1rem 0;
}
.userTable {
  table-layout: fixed;
}

.search-form {
  height: 3rem;
}
</style>
