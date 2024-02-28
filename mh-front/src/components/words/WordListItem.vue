<template>
  <li>
    <div class="post-title">{{ item['infokey'] }}</div>
    <!-- <div class="post-contents" value="item.contents">{{ item.contents }}</div> -->
    <div class="post-creator">{{ item['updator'] }}</div>
    <textarea class="post-contents" disabled v-model="description"></textarea>
    <div class="post-time">
      <!-- {{ item['updateDate'] }} -->
      {{ this.$filters.dateFilter(item['updateDate']) }}
      <span v-show="getUsername === item['creator']">
        <i class="icon ion-md-create" @click="editWord(item['id'])"></i>
        <i class="icon ion-md-trash" @click="deleteWord(item['id'])"></i>
      </span>
    </div>
    <a @click="onClikeRedirect(item['link'], item['infokey'])" class="link">
      {{ item['link'] === 'none' ? '위키에서 찾기' : item['link'] }}
      <i class="icon ion-md-search"></i>
    </a>
  </li>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import { callApi } from '@/api/index';
import { validateURL } from '@/utils/validation';
import { sleep } from '@/utils/common';
export default {
  props: {
    item: Object,
  },
  data() {
    return {
      description: this.item.description,
    };
  },
  computed: {
    ...mapGetters(['getUsername', 'getWordList', 'isLogin']),
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
      link = isValidURL ? link : `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
    async deleteWord(itemId) {
      itemId;
      // console.log(this.getWordList);
      let arr = this.getWordList;
      let idx = arr.map(m => m.id).indexOf(itemId);

      const { data } = await callApi(`delete/${itemId}`);
      if (data.result === 200) {
        console.log(data.result_description);
        this.spliceWordList(idx);
        console.log(this.getWordList);
        this.callToast(data.result_description);
      }
    },
    async editWord(itemId) {
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
