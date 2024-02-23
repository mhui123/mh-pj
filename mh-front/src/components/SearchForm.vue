<template>
  <div>
    <input type="text" v-model="keyword" @keyup:enter="searchKeyword" />
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

<style></style>
