<template>
  <div>
    <AppHeader></AppHeader>
    <router-view></router-view>
    <ToastPopup></ToastPopup>
  </div>
</template>

<script>
import AppHeader from '@/components/common/AppHeader.vue';
import ToastPopup from '@/components/common/ToastPopup.vue';
export default {
  components: { AppHeader, ToastPopup },
};
</script>

<style>
@import './css/reset.css';
@import './css/common.css';
</style>
