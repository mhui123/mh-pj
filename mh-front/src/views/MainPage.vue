<template>
  <div>
    <div class="main list-container contents">
      <h1 class="page-header">용어사전</h1>
      <SearchForm></SearchForm>
      <LoadingSpinner v-if="isLoading"></LoadingSpinner>
      <ul v-else>
        <WordListItem v-for="info in infoList" :key="info.id" :item="info" @change="changeData"></WordListItem>
      </ul>
    </div>
    <router-link to="/add" class="create-button">
      <i class="ion-md-add"></i>
    </router-link>
  </div>
</template>

<script>
import { getList } from '@/api/index';
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
    ...mapGetters(['getWordList']),
    infoList() {
      return this.getWordList;
    },
  },
  methods: {
    ...mapMutations(['pushToWordList', 'clearWordList']),
    async fetchData() {
      this.isLoading = true;
      const { data } = await getList();
      console.log('listData : ', data);
      this.isLoading = false;
      this.clearWordList();
      this.pushToWordList(data);
    },
    changeData() {
      console.log('changeData', this.getWordList);
    },
  },
};
</script>

<style></style>
