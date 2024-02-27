<template>
  <div class="search-form">
    <input type="text" @keypress.enter.prevent="searchKeyword" v-model="keyword" class="search-input" placeholder="검색어를 입력해주세요" />
    <i class="icon ion-md-search" @click="searchKeyword"></i>
  </div>
</template>

<script>
import { searchWord } from '@/api/index';
import { mapMutations } from 'vuex';
export default {
  data() {
    return {
      keyword: '',
    };
  },
  computed: {
    isPossibleSearch() {
      return this.keyword.length > 0;
    },
  },
  methods: {
    ...mapMutations(['clearWordList', 'pushToWordList', 'setKeyword']),
    async searchKeyword() {
      const res = await searchWord(this.keyword);
      const { data, status } = res;
      if (status === 200) {
        console.log(data);
        this.clearWordList();
        this.pushToWordList(data);
        this.setKeyword(this.keyword);
        this.callToast(`검색결과 : ${data.length}건`);
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
  },
};
</script>

<style scoped>
.search-form {
  text-align: center;
  height: 5rem;
}
.search-input {
  width: 50%;
  height: 2rem;
  margin-right: 0.5rem;
  font-weight: 700;
  border-radius: 0.25rem;
  padding: 1rem;
  background-color: white;
}
</style>
