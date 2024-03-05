<template>
  <li>
    <div class="post-title">{{ item['infokey'] }}</div>
    <!-- <div class="post-contents" value="item.contents">{{ item.contents }}</div> -->
    <div class="post-creator">{{ item['updator'] }}</div>
    <textarea class="post-contents" disabled v-model="description"></textarea>
    <div class="post-time">
      <!-- {{ item['updateDate'] }} -->
      {{ this.$filters.dateFilter(item['updateDate']) }}
      <span v-show="getUsername === item['creator'] || getRole === 'ROLE_ADMIN'">
        <i class="icon ion-md-create" @click="callModal('edit', item['id'])" v-if="getRole === 'ROLE_USER'"></i>
        <i class="icon ion-md-trash" @click="callModal('delete', item['id'])"></i>
      </span>
    </div>
    <a @click="onClikeRedirect(item['link'], item['infokey'])" class="link">
      {{ item['link'] === 'none' ? '위키에서 찾기' : '참고링크' }}
      <i class="icon ion-md-search"></i>
    </a>
    <Teleport to="body">
      <ConfirmModal :showModal="showModal" @ok="modalOk" @no="modalNo" :modalMsg="modalMsg"></ConfirmModal>
    </Teleport>
  </li>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import { callApi } from '@/api/index';
import { validateURL } from '@/utils/validation';
import { sleep } from '@/utils/common';
// import modal from '@/components/common/ModalWin.vue';
import ConfirmModal from '../common/ConfirmModal.vue';
export default {
  props: {
    item: Object,
  },
  components: {
    // modal,
    ConfirmModal,
  },
  data() {
    return {
      description: this.item.description,
      showModal: false,
      modalMsg: '',
      callItemId: '',
      reason: '',
    };
  },
  computed: {
    ...mapGetters(['getUsername', 'getWordList', 'isLogin', 'getRole']),
    isLogin() {
      return this.isLogin;
    },
  },
  methods: {
    ...mapMutations(['spliceWordList', 'setWordId']),
    async onClikeRedirect(link, keyword) {
      let isValidURL = validateURL(link);
      if (link !== 'none' && !isValidURL) {
        this.callToast('유효하지 않은 링크입니다. 위키에서 검색합니다.');
        await sleep(2000);
      }
      link = isValidURL ? `${link}` : `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
    async deleteWord(itemId) {
      let arr = this.getWordList;
      let idx = arr.map(m => m.id).indexOf(itemId);

      const { data } = await callApi(`delete/${itemId}`);
      if (data.result === 200) {
        this.spliceWordList(idx);
        this.callToast(data.result_description);
      }
    },
    callModal(reason, itemId) {
      this.showModal = true;
      this.reason = reason;
      this.callItemId = itemId;
      const reasons = {
        edit: '수정하시겠습니까?',
        delete: '삭제하시겠습니까?',
      };
      this.modalMsg = reasons[reason];
    },
    modalOk() {
      if (this.reason === 'edit') {
        this.editWord(this.callItemId);
      } else {
        this.deleteWord(this.callItemId);
      }
    },
    modalNo() {
      this.reason = '';
      this.callItemId = '';
      this.showModal = false;
    },
    editWord(itemId) {
      this.setWordId(itemId);
      this.$router.push(`/edit`);
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
  },
};
</script>

<style></style>
