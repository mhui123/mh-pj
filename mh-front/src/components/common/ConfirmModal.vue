<template>
  <modal :showModal="showModal" @close="modalNo" @update:showModal="modalNo">
    <template #header>
      <i class="icon ion-md-alert"> 확인 </i>
    </template>
    <template #body>
      <p>{{ modalMsg }}</p>
      <div class="btn-groups">
        <button class="btn" @click="modalOk">확인</button>
        <button class="btn2" @click="modalNo">취소</button>
      </div>
    </template>
  </modal>
</template>

<script>
import modal from '@/components/common/ModalWin.vue';
export default {
  components: { modal },
  props: {
    modalMsg: String,
    showModal: Boolean,
  },
  methods: {
    modalOk() {
      this.$emit('ok');
    },
    modalNo() {
      this.$emit('no');
    },
  },
};
</script>

<style></style>
vue
