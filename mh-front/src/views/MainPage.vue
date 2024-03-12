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
          <p class="zero-result">등록된 용어가 없습니다.</p>
        </li>
      </ul>
    </div>
    <router-link to="/add" class="create-button" v-if="isLogin">
      <i class="ion-md-add"></i>
    </router-link>
    <button class="move-top-button" @click="goTop" v-if="topshow === true">
      <i class="icon ion-md-arrow-up"></i>
    </button>
    <button class="more-btn" @click="getMoreData" v-if="moreshow === true && isLastPage === false">더보기</button>
  </div>
</template>

<script>
//document.body.scrollTop
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
      topshow: false,
      moreshow: false,
      totalCnt: 0,
      isLastPage: false,
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
  mounted() {
    document.body.addEventListener('scroll', () => {
      let scrollPos = Math.floor((document.body.scrollTop / document.body.scrollHeight) * 100);
      if (document.body.scrollTop > 0) {
        if (scrollPos > 80) {
          this.moreshow = true;
        } else {
          // const ul = document.querySelector('.main.list-container.contents ul');
          this.moreshow = false;
        }
        this.topshow = true;

        let ul = document.querySelector('.main.list-container.contents ul');
        let listCnt = ul ? document.querySelector('.main.list-container.contents ul').childElementCount : 0;
        if (listCnt < this.totalCnt) {
          this.isLastPage = false;
        }
      } else {
        this.topshow = false;
        this.moreshow = false;
      }
    });
  },
  computed: {
    ...mapGetters(['getWordList', 'getKeyword', 'isLogin', 'getPageIdx']),
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
    ...mapMutations(['pushToWordList', 'clearWordList', 'setPageIdx']),
    async fetchData() {
      this.setPageIdx(0);
      this.isLoading = true;
      // const { data } = await callApi('getList');
      const { data } = await callApi('getMainWordList', { pageIdx: 0 });
      this.totalCnt = data.totalCnt;
      this.isLoading = false;
      this.clearWordList();
      this.pushToWordList(data.list);
    },
    onClikeRedirect(link, keyword) {
      link = link ?? `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
    goTop() {
      document.body.scrollTop = 0;
    },
    async getMoreData() {
      this.moreshow = false;
      this.isLoading = true;
      let pageIdx = this.getPageIdx + 1;
      this.setPageIdx(pageIdx);
      this.isLastPage = 20 * (pageIdx + 1) > this.totalCnt ? true : false;
      this.isLoading = false;
      const { data } = await callApi('getMainWordList', { pageIdx: pageIdx });
      // console.log(data.list);
      // this.clearWordList();
      this.pushToWordList(data.list);
    },
  },
};
</script>

<style scoped>
.search-icon {
  font-size: 2rem;
}
.nothing-icon {
  font-size: 4rem;
  text-align: center;
  display: block;
}
</style>
