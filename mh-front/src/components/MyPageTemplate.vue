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
              <button class="btn" @click="(showModal = true) && (modalstate = 'change')">비밀번호 변경</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <SearchForm :mode="mode"></SearchForm>
    <div class="btn-groups">
      <div>
        <button class="btn" @click="callModal('edit')">수정</button>
        <button class="btn" @click="callModal('delete')">삭제</button>
      </div>
    </div>
    <div class="lower-container table-wrap" v-if="mode === 'mypage'">
      <table class="table">
        <thead>
          <tr>
            <th class="chkBox"></th>
            <th>키워드</th>
            <!-- <th>작성자</th> -->
            <th>수정일시</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="word in wordList" :key="word.id">
            <td class="chkBox"><input class="chkInput" type="checkbox" :name="word.id" :id="word.id" /></td>
            <td>{{ word['infokey'] }} {{ word['info_kr'] }}</td>
            <!-- <td>{{ word['creator'] }}</td> -->
            <td>{{ this.$filters.dateFilter(word['updateDate']) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <Teleport to="body">
      <modal :showModal="showModal" @close="showModal = false" @update:showModal="showModal = false" v-if="modalstate === 'change'">
        <template #header>
          <i class="icon ion-md-close closeModalBtn" @click="showModal = false"></i>
        </template>
        <template #body>
          <div>
            <form @submit.prevent class="form">
              <label for="">기존 비밀번호</label>
              <input type="password" class="{warn-input : !warnInput}" v-model="asPw" id="asPw" @keypress="warnInput = true" />
              <p class="validation-text">
                <span class="warning" v-if="!isAsPwValid && asPw.length > 1"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
              </p>
              <label for="">새 비밀번호</label>
              <input type="password" v-model="newPw" />
              <p class="validation-text">
                <span class="warning" v-if="!isNewPwValid && this.newPw.length > 1"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
              </p>
              <label for="">새 비밀번호 확인</label>
              <input type="password" v-model="newPw2" />
              <p class="validation-text">
                <span class="warning" v-if="!isNewPw2Valid && this.newPw2.length > 1"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
              </p>
              <div>
                <button class="btn" @click="changePw" :disabled="!isAsPwValid || !isNewPwValid || !isNewPw2Valid">변경하기</button>
              </div>
            </form>
          </div>
        </template>
      </modal>
      <ConfirmModal :showModal="showModal" @ok="modalOk" @no="modalNo" :modalMsg="modalMsg" v-if="modalstate === 'confirm'"></ConfirmModal>
    </Teleport>
  </div>
</template>

<script>
import ConfirmModal from './common/ConfirmModal.vue';
import modal from './common/ModalWin.vue';
import { validatePw } from '@/utils/validation';
import { getCheckedIds, initCheckboxes } from '@/utils/common';
import { mapGetters, mapMutations } from 'vuex';
import { callApi } from '@/api/index';
import SearchForm from './SearchForm.vue';
export default {
  components: {
    modal,
    SearchForm,
    ConfirmModal,
  },
  data() {
    return {
      asPw: '',
      newPw: '',
      newPw2: '',
      showModal: false,
      warnInput: false,
      mode: 'mypage',
      checkedUsers: [],
      reason: '',
      modalMsg: '',
      modalstate: '',
    };
  },
  computed: {
    isAsPwValid() {
      return validatePw(this.asPw) && this.asPw.length > 0;
    },
    isNewPwValid() {
      return validatePw(this.newPw) && this.newPw.length > 0;
    },
    isNewPw2Valid() {
      return validatePw(this.newPw2) && this.newPw2.length > 0;
    },
    wordList() {
      return this.getWordList;
    },
    username() {
      return this.getUsername;
    },
    getWordId() {
      return this.getWordId;
    },
    ...mapGetters(['getUsername', 'getWordList', 'getUsername', 'getWordId']),
  },
  created() {
    this.setPageFrom('');
    this.fetchMyWords();
  },
  methods: {
    ...mapMutations(['setWordList', 'clearWordList', 'spliceWordList', 'setWordId', 'setPageFrom']),
    async deleteWords() {
      const filteredIds = await getCheckedIds();
      if (filteredIds.length === 0) {
        this.callToast('삭제할 대상을 선택해주세요.');
        return false;
      }
      if (filteredIds.length > 0) {
        const { data } = await callApi(`delWords`, { ids: filteredIds });
        let result_description = data['result_description'];
        this.callToast(result_description);
        initCheckboxes();
        filteredIds.forEach(e => {
          let arr = this.getWordList;
          let idx = arr.map(m => m.id).indexOf(Number.parseInt(e));
          this.spliceWordList(idx);
        });
      }
    },
    async editWords() {
      const filteredIds = await getCheckedIds();
      if (filteredIds.length === 0) {
        this.callToast('수정할 대상을 선택해주세요.');
      } else if (filteredIds.length > 1) {
        this.callToast('수정은 1건만 진행할 수 있습니다.');
        initCheckboxes();
      } else {
        this.setPageFrom('/mypage');
        this.setWordId(filteredIds[0]);
        this.$router.push(`/edit`);
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
    async changePw() {
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
          } else if (result === 902) {
            document.getElementById('asPw').focus();
            this.callToast(result_description);
          }
        }
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
    callModal(reason) {
      this.modalstate = 'confirm';
      this.showModal = true;
      this.reason = reason;
      const reasons = {
        edit: '수정하시겠습니까?',
        delete: '삭제하시겠습니까?',
      };
      this.modalMsg = reasons[reason];
    },
    modalOk() {
      if (this.reason != '') {
        if (this.reason === 'edit') {
          this.editWords();
        } else if (this.reason === 'delete') {
          this.deleteWords();
        }
        this.showModal = false;
        this.reason = '';
      }
    },
    modalNo() {
      this.showModal = false;
      this.reason = '';
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
