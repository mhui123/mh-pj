<template>
  <div>
    <AppHeader></AppHeader>
    <AppSideBar></AppSideBar>
    <router-view></router-view>
    <ToastPopup></ToastPopup>
  </div>
</template>

<script>
import AppHeader from '@/components/common/AppHeader.vue';
import ToastPopup from '@/components/common/ToastPopup.vue';
import AppSideBar from '@/components/common/AppSideBar.vue';
export default {
  components: { AppHeader, ToastPopup, AppSideBar },
};
</script>

<style lang="scss">
@import './css/reset.css';
@import './css/common.css';
@import './css/sidebar.scss';
</style>
