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
    <div class="lower-container table-wrap">
      <table>
        <colgroup>
          <col />
          <col />
          <!-- <col /> -->
          <col />
          <col />
        </colgroup>
        <thead>
          <td>아이디</td>
          <td>권한</td>
          <!-- <td>사용여부</td> -->
          <td></td>
          <td>사용여부 변경</td>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.role === 'ROLE_USER' ? '유저' : '관리자' }}</td>
            <!-- <td>{{ user.useYn }}</td> -->
            <td><button class="btn" @click="changeRole(user)">권한변경</button></td>
            <td v-if="user.useYn === 'y'"><button class="btn" @click="changeUseYn(user)">중지</button></td>
            <td v-else><button class="btn" @click="changeUseYn(user)">사용</button></td>
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
import { mapGetters } from 'vuex';
import { getUserList, changeRole, changeUseYn } from '@/api/index';
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
      console.log(mode);
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
      const response = await changeRole(user);
      const { status, data } = response;
      const { result_description } = data;
      if (status === 200) {
        user['role'] = user['role'] === 'ROLE_USER' ? 'ROLE_ADMIN' : 'ROLE_USER';
      }
      this.callToast(result_description);
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
  /* max-width: 500px; */
  /* margin: 40px auto; */
  /* max-width: 500px; */
  /* background-color: white; */
}
.lower-container {
  display: flex;
  justify-content: center;
}
colgroup {
  display: table-column-group;
}
thead {
  display: table-header-group;
  vertical-align: middle;
  /* border-color: inherit; */
}
tr {
  display: table-row;
}
th {
  display: table-cell;
  font-weight: bold;
  text-align: initial center;
}
tbody {
  display: table-row-group;
}
</style>
