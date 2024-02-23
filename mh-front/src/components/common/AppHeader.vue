<template>
  <header>
    <div>
      <router-link :to="logoLink">MH</router-link>
    </div>
    <div class="navigation">
      <template v-if="isUserLogin">
        <span class="username">{{ getUsername }}님 환영합니다. </span>
        <a href="javascript:;" @click="logoutUser" class="logout-button">Logout</a>
      </template>
      <template v-else>
        <router-link to="/login">로그인</router-link>
        <!-- <router-link to="/signup">회원가입</router-link> -->
      </template>
    </div>
  </header>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex';
export default {
  computed: {
    isUserLogin() {
      return this.isLogin;
    },
    logoLink() {
      return '/main';
    },
    getUsername() {
      return this.getUsername;
    },
    ...mapGetters(['isLogin', 'getUsername']),
  },
  methods: {
    ...mapMutations(['']),
    ...mapActions(['LOGOUT']),
    async logoutUser() {
      await this.LOGOUT();
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
.username {
  color: white;
}
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #2e4cb1;
  z-index: 2;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
}
a {
  color: #dedede;
  font-size: 18px;
}
a.logo {
  font-size: 30px;
  font-weight: 900;
  color: white;
}
.logo > span {
  font-size: 14px;
  font-weight: normal;
}
.navigations a {
  margin-left: 10px;
}
.fixed {
  position: fixed;
  top: 0;
  width: 100%;
}
.logout-button {
  font-size: 14px;
}
a.router-link-exact-active {
  color: white;
  font-weight: bold;
}
</style>
