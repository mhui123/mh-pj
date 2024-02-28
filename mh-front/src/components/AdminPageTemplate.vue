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
        <button class="btn" @click="changeStatus('initPw')">비밀번호 초기화</button>
        <button class="btn" @click="changeStatus('role')">권한변경</button>
        <button class="btn" @click="changeStatus('useYn')">사용여부 변경</button>
      </div>
      <div v-else>
        <button class="btn" @click="changeStatus('delWord')">삭제</button>
      </div>
    </div>
    <SearchForm :mode="mode"></SearchForm>
    <div class="lower-container table-wrap" v-if="mode === 'user'">
      <table class="table">
        <thead>
          <tr>
            <th class="chkBox"></th>
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
              <h3>{{ modalTitle }}</h3>
            </form>
          </div>
        </template>
      </modal>
      <!-- <AdmUserList :showModal="showModal" @close="showModal = false"></AdmUserList> -->
    </Teleport>
  </div>
</template>

<script>
import modal from './common/ModalWin.vue';
// import AdmUserList from './AdminUserListForm.vue';
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
      showModal: false,
      mode: '',
      modalTitle: '',
      // userList: [],
      // wordList: [],
      checkedUsers: [],
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
      await this.getCheckedIds();
      let result_description = '';
      if (this.checkedUsers.length > 0) {
        let filteredIds = this.checkedUsers.map(m => m.id);
        if (mode === 'useYn') {
          const response = await callApi('changeUseYn', { users: filteredIds });
          const { status, data } = response;
          result_description = data['result_description'];
          if (status === 200) {
            filteredIds.forEach(e => {
              let user = this.userList.filter(x => x.id === e)[0];
              user['useYn'] = user['useYn'] === 'y' ? 'n' : 'y';
              this.initCheckboxes();
            });
          }
        } else if (mode === 'role') {
          const response = await callApi('changeRole', { users: filteredIds });
          const { status, data } = response;
          result_description = data['result_description'];
          if (status === 200) {
            filteredIds.forEach(e => {
              let user = this.userList.filter(x => x.id === e)[0];
              user['role'] = user['role'] === 'ROLE_USER' ? 'ROLE_ADMIN' : 'ROLE_USER';
              this.initCheckboxes();
            });
          }
        } else if (mode === 'initPw') {
          const { data } = await callApi('initializePw', { ids: filteredIds });
          result_description = data['result_description'];
          this.callToast(result_description);
          this.initCheckboxes();
        } else if (mode === 'delWord') {
          const { data } = await callApi(`delWords`, { ids: filteredIds });
          result_description = data['result_description'];
          this.callToast(result_description);
          this.initCheckboxes();
          filteredIds.forEach(e => {
            let arr = this.getWordList;
            let idx = arr.map(m => m.id).indexOf(e);
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
      await this.getCheckedIds();
      let filteredIds = this.checkedUsers.map(m => m.id);
      if (filteredIds.length > 0) {
        const { data } = await callApi('initializePw', { ids: filteredIds });
        const { result_description } = data;
        this.callToast(result_description);
        this.initCheckboxes();
      } else {
        this.callToast('비밀번호를 초기화할 대상을 선택해주세요');
      }
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
    async delWord() {
      await this.getCheckedIds();
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
