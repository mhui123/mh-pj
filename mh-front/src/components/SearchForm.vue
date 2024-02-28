<template>
  <div class="search-form">
    <input type="text" @keypress.enter.prevent="searchKeyword" v-model="keyword" class="search-input" placeholder="검색어를 입력해주세요" id="searchBar" />
    <i class="icon ion-md-search" @click="searchKeyword"></i>
  </div>
</template>

<script>
import { callApi } from '@/api/index';
import { mapMutations } from 'vuex';
import { getCurrentRoute } from '@/utils/common';
export default {
  props: {
    mode: String,
  },
  data() {
    return {
      keyword: '',
      currentRoute: getCurrentRoute(),
    };
  },
  computed: {
    isPossibleSearch() {
      return this.keyword.length > 0;
    },
  },
  updated() {
    if (this.currentRoute.includes('admin')) {
      document.getElementById('searchBar').placeholder = this.mode === 'user' ? '검색할 사용자명을 입력해주세요' : '키워드나 작성자를 입력해주세요';
    }
  },
  methods: {
    ...mapMutations(['clearWordList', 'pushToWordList', 'setKeyword', 'setUserList', 'setWordList']),
    async searchKeyword() {
      const destinations = {
        main: 'getWordListByKeyword',
        admin: { user: 'getUserListByKeyword', word: 'getWordListByKeyword' },
      };

      let route = this.currentRoute.includes('main') ? 'main' : 'admin';
      let path = route === 'admin' ? destinations[route][this.mode] : destinations[route];
      const res = await callApi(path, { keyword: this.keyword });
      const { data, status } = res;
      const { list, result_description } = data;

      if (status === 200) {
        if (route === 'main') {
          this.clearWordList();
          this.pushToWordList(list);
          this.setKeyword(this.keyword);
          this.callToast(`검색결과 : ${list.length}건`);
        } else {
          if (this.mode === 'user') {
            this.setUserList(list);
          } else {
            this.setWordList(list);
          }
          this.callToast(result_description);
        }
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
