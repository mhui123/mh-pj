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
export default {
  components: {
    modal,
  },
  data() {
    return {
      showModal: false,
      mode: '',
      modalTitle: '',
    };
  },
  computed: {
    ...mapGetters(['getUsername', 'getRole']),
  },
  methods: {
    manageUser() {
      this.modeControl('user');
    },
    manageWord() {
      this.modeControl('word');
    },
    modeControl(mode) {
      console.log(mode);
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
