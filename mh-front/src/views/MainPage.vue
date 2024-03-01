<template>
  <div>
    <div class="main list-container contents">
      <h1 class="page-header">용어사전</h1>
      <SearchForm></SearchForm>
      <LoadingSpinner v-if="isLoading"></LoadingSpinner>
      <ul v-else>
        <WordListItem v-for="info in infoList" :key="info.id" :item="info"></WordListItem>
        <li v-if="infoList.length === 0 && chkKeyword">
          <i class="icon ion-md-alert nothing-icon"></i>
          <input value="검색결과가 없습니다." class="zero-result" />
          <a @click="onClikeRedirect(null, getKeyword)" class="zero-result link">
            {{ '위키에서 키워드로 검색하기' }}
            <i class="icon ion-md-search search-icon"></i>
          </a>
        </li>
        <li v-if="infoList.length === 0 && !chkKeyword">
          <i class="icon ion-md-alert nothing-icon"></i>
          <input value="등록된 용어가 없습니다." class="zero-result" />
        </li>
      </ul>
    </div>
    <router-link to="/add" class="create-button" v-if="isLogin">
      <i class="ion-md-add"></i>
    </router-link>
  </div>
</template>

<script>
import { callApi } from '@/api/index';
import WordListItem from '@/components/words/WordListItem.vue';
import LoadingSpinner from '@/components/common/LoadingSpinner.vue';
import SearchForm from '@/components/SearchForm.vue';
import { mapGetters, mapMutations } from 'vuex';
export default {
  data() {
    return {
      isLoading: false,
      // infoList: [],
    };
  },
  components: {
    WordListItem,
    LoadingSpinner,
    SearchForm,
  },
  created() {
    this.fetchData();
  },
  computed: {
    ...mapGetters(['getWordList', 'getKeyword', 'isLogin']),
    infoList() {
      return this.getWordList;
    },
    chkKeyword() {
      return this.getKeyword === '' ? false : true;
    },
    isUserLogin() {
      return this.isLogin;
    },
  },
  methods: {
    ...mapMutations(['pushToWordList', 'clearWordList']),
    async fetchData() {
      this.isLoading = true;
      const { data } = await callApi('getList');
      this.isLoading = false;
      this.clearWordList();
      this.pushToWordList(data);
    },
    onClikeRedirect(link, keyword) {
      link = link ?? `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
  },
};
</script>

<style scoped>
.zero-result {
  text-align: center;
  width: 100%;
  height: 40%;
}
.zero-result .link {
  text-align: center;
  color: lightblue;
  display: flow;
  width: 100%;
  font-size: larger;
}
.search-icon {
  font-size: 2rem;
}
.nothing-icon {
  font-size: 4rem;
  text-align: center;
  display: block;
}
</style>
