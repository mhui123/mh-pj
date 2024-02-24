<template>
  <div class="search-form">
    <input type="text" @keypress.enter.prevent="searchKeyword" v-model="keyword" class="search-input" placeholder="검색어를 입력해주세요" />
    <i class="icon ion-md-search" @click="searchKeyword"></i>
  </div>
</template>

<script>
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
    searchKeyword() {
      if (this.isPossibleSearch) {
        console.log(`키워드 ${this.keyword}로 검색합니다.`);
      }
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
