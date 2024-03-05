<template>
  <div class="contents" v-if="getRole === 'ROLE_ADMIN'">
    <div class="form-wrapper form-wrapper-sm">
      <h1 class="page-header">관리자페이지</h1>
      <table>
        <tbody>
          <tr>
            <td>
              <button class="btn" @click="manageUser">유저관리</button>
            </td>
            <td>
              <button class="btn" @click="manageWord">용어관리</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="btn-groups">
      <div v-if="mode === 'user'">
        <button class="btn" @click="callModal('initPw')">비밀번호 초기화</button>
        <button class="btn" @click="callModal('role')">권한변경</button>
        <button class="btn" @click="callModal('useYn')">사용여부 변경</button>
      </div>
      <div v-else>
        <button class="btn" @click="callModal('delWord')">삭제</button>
      </div>
    </div>
    <SearchForm :mode="mode"></SearchForm>
    <div class="lower-container table-wrap" v-if="mode === 'user'">
      <table class="table">
        <thead>
          <tr>
            <th class="chkBox"><input class="checkInput2" type="checkbox" name="checkAll" id="checkAll" @click="checkAll" /></th>
            <th>아이디</th>
            <th>권한</th>
            <th>사용여부</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.id">
            <td class="chkBox"><input class="chkInput" type="checkbox" :name="user.id" :id="user.id" /></td>
            <td>{{ user['id'] }}</td>
            <td>{{ user['role'] === 'ROLE_USER' ? '유저' : '관리자' }}</td>
            <td>{{ user['useYn'] === 'y' ? '사용' : '중지' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="lower-container table-wrap" v-if="mode === 'word'">
      <table class="table">
        <thead>
          <tr>
            <th class="chkBox"><input class="checkInput2" type="checkbox" name="checkAll" id="checkAll" @click="checkAll" /></th>
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
    <button class="move-top-button" @click="goTop" v-if="topshow === true">
      <i class="icon ion-md-arrow-up"></i>
    </button>
    <Teleport to="body">
      <ConfirmModal :showModal="showModal" @ok="modalOk" @no="modalNo" :modalMsg="modalMsg"></ConfirmModal>
    </Teleport>
  </div>
</template>

<script>
import ConfirmModal from './common/ConfirmModal.vue';
import { mapGetters, mapMutations } from 'vuex';
import { getCheckedIds, initCheckboxes, checkAll } from '@/utils/common';
import { callApi } from '@/api/index';
import SearchForm from './SearchForm.vue';

export default {
  components: {
    SearchForm,
    ConfirmModal,
  },
  data() {
    return {
      showModal: false,
      modalMsg: '',
      reason: '',
      mode: '',
      checkedUsers: [],
      topshow: false,
    };
  },
  computed: {
    ...mapGetters(['getUsername', 'getRole', 'getWordList', 'getUserList']),
    wordList() {
      return this.getWordList;
    },
    userList() {
      return this.getUserList;
    },
  },
  created() {
    this.manageUser();
  },
  mounted() {
    document.body.addEventListener('scroll', () => {
      if (document.body.scrollTop > 0) {
        this.topshow = true;
      } else this.topshow = false;
    });
  },
  methods: {
    ...mapMutations(['spliceWordList', 'clearWordList', 'setUserList', 'setWordList']),
    async manageUser() {
      this.setUserList([]);
      this.modeControl('user');
      const response = await callApi('getUserList', { pageIndex: 1 });
      const { status, data } = response;
      const list = data['userList'];
      if (status === 200) {
        this.setUserList(list);
      }
    },
    async manageWord() {
      this.clearWordList();
      this.modeControl('word');
      const { data } = await callApi('getAllWordList');
      const { list } = data;
      this.setWordList(list);
    },
    modeControl(mode) {
      this.mode = mode;
    },
    async changeStatus(mode) {
      const filteredIds = await getCheckedIds();
      let result_description = '';
      if (filteredIds.length > 0) {
        if (mode === 'useYn') {
          const response = await callApi('changeUseYn', { users: filteredIds });
          const { status, data } = response;
          result_description = data['result_description'];
          if (status === 200) {
            filteredIds.forEach(e => {
              let user = this.userList.filter(x => x.id === e)[0];
              user['useYn'] = user['useYn'] === 'y' ? 'n' : 'y';
            });
            initCheckboxes();
          }
        } else if (mode === 'role') {
          const response = await callApi('changeRole', { users: filteredIds });
          const { status, data } = response;
          result_description = data['result_description'];
          if (status === 200) {
            filteredIds.forEach(e => {
              let user = this.userList.filter(x => x.id === e)[0];
              user['role'] = user['role'] === 'ROLE_USER' ? 'ROLE_ADMIN' : 'ROLE_USER';
            });
            initCheckboxes();
          }
        } else if (mode === 'initPw') {
          const { data } = await callApi('initializePw', { ids: filteredIds });
          result_description = data['result_description'];
          this.callToast(result_description);
          initCheckboxes();
        } else if (mode === 'delWord') {
          const { data } = await callApi(`delWords`, { ids: filteredIds });
          result_description = data['result_description'];
          this.callToast(result_description);
          initCheckboxes();
          filteredIds.forEach(e => {
            let arr = this.getWordList;
            let idx = arr.map(m => m.id).indexOf(Number.parseInt(e));
            this.spliceWordList(idx);
          });
        }
        this.callToast(result_description);
      } else {
        this.callToast('변경할 대상을 선택해주세요');
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
    async initializePw() {
      let filteredIds = await getCheckedIds();
      if (filteredIds.length > 0) {
        const { data } = await callApi('initializePw', { ids: filteredIds });
        const { result_description } = data;
        this.callToast(result_description);
        initCheckboxes();
      } else {
        this.callToast('비밀번호를 초기화할 대상을 선택해주세요');
      }
    },
    async delWord() {
      await this.getCheckedIds();
    },
    callModal(reason) {
      this.showModal = true;
      this.reason = reason;
      const reasons = {
        initPw: '비밀번호를 초기화하시겠습니까?',
        role: '권한을 변경하시겠습니까?',
        useYn: '사용여부를 변경하시겠습니까?',
        delWord: '삭제하시겠습니까?',
      };
      this.modalMsg = reasons[reason];
    },
    modalOk() {
      if (this.reason != '') {
        this.changeStatus(this.reason);
        this.showModal = false;
        this.reason = '';
      }
    },
    modalNo() {
      this.showModal = false;
      this.reason = '';
    },
    goTop() {
      document.body.scrollTop = 0;
    },
    checkAll() {
      checkAll();
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

.userTable {
  table-layout: fixed;
}

.search-form {
  height: 3rem;
}
.chkBox,
.chkInput,
[class^='checkInput'] {
  width: 2rem;
  margin: 0 0.1rem 0;
  text-align: left;
}
</style>
