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
    <div class="lower-container table-wrap" v-if="mode === 'user'">
      <table class="table">
        <!-- <colgroup>
          <col />
          <col />
          <col />
          <col />
          <col />
        </colgroup> -->
        <thead>
          <tr>
            <th class="chkBox"></th>
            <th>아이디</th>
            <th>권한</th>
            <th></th>
            <th>사용여부 변경</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.id">
            <td class="chkBox"><input class="chkInput" type="checkbox" :name="user.id" :id="user.id" /></td>
            <td>{{ user.id }}</td>
            <td>{{ user.role === 'ROLE_USER' ? '유저' : '관리자' }}</td>
            <td><button class="btn" @click="changeRole(user)">권한변경</button></td>
            <td v-if="user.useYn === 'y'"><button class="btn" @click="changeUseYn(user)">중지</button></td>
            <td v-else><button class="btn" @click="changeUseYn(user)">사용</button></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="btn-groups">
      <button v-if="mode === 'user'" class="btn" @click="initializePw">비밀번호 초기화</button>
      <button v-if="mode === 'user'" class="btn" @click="changeRole">권한변경</button>
      <button v-if="mode === 'user'" class="btn" @click="changeUseYn">사용여부 변경</button>
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
import { mapGetters } from 'vuex';
import { getUserList, changeRole, changeUseYn, initPw } from '@/api/index';
export default {
  components: {
    modal,
  },
  data() {
    return {
      showModal: false,
      mode: '',
      modalTitle: '',
      userList: [],
      checkedUsers: [],
    };
  },
  computed: {
    ...mapGetters(['getUsername', 'getRole']),
  },
  created() {
    this.manageUser();
  },
  methods: {
    async manageUser() {
      this.userList = [];
      this.modeControl('user');
      const response = await getUserList({ pageIndex: 1 });
      const { status, data } = response;
      const list = data['userList'];
      if (status === 200) {
        list.forEach(e => {
          this.userList.push(e);
        });
      }
    },
    manageWord() {
      this.modeControl('word');
    },
    modeControl(mode) {
      this.mode = mode;
    },
    async changeUseYn(user) {
      const response = await changeUseYn(user);
      const { status, data } = response;
      const { result_description } = data;
      if (status === 200) {
        user['useYn'] = user['useYn'] === 'y' ? 'n' : 'y';
      }
      this.callToast(result_description);
    },
    async changeRole(user) {
      await this.getCheckedIds();
      if (this.checkedUsers.length > 0) {
        let filteredIds = this.checkedUsers.map(m => m.id);
        const response = await changeRole({ users: filteredIds });
        console.log(response);
        const { status, data } = response;
        const { result_description } = data;
        status, user;
        // if (status === 200) {
        //   user['role'] = user['role'] === 'ROLE_USER' ? 'ROLE_ADMIN' : 'ROLE_USER';
        // }
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
        const { data } = await initPw({ ids: filteredIds });
        const { result_description } = data;
        this.callToast(result_description);
      } else {
        this.callToast('비밀번호를 초기화할 대상을 선택해주세요');
      }
    },
    async getCheckedIds() {
      let checkBoxes = document.getElementsByClassName('chkInput');
      checkBoxes = Array.from(checkBoxes);
      this.checkedUsers = checkBoxes.filter(x => x.checked);
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
  margin: 0 0.5rem 0;
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
</style>
