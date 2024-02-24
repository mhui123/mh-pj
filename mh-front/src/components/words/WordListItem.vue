<template>
  <li>
    <div class="post-title">{{ item['infokey'] }}</div>
    <!-- <div class="post-contents" value="item.contents">{{ item.contents }}</div> -->
    <textarea class="post-contents" disabled v-model="description"></textarea>
    <div class="post-time">
      <!-- {{ item['updateDate'] }} -->
      {{ this.$filters.dateFilter(item['updateDate']) }}
      <span v-show="getUsername === item['creator']">
        <i class="icon ion-md-create"></i>
        <i class="icon ion-md-trash" @click="deleteWord(item['id'])"></i>
      </span>
    </div>
    <a @click="onClikeRedirect(item['link'], item['infokey'])" class="link">{{ item['link'] ?? 'wiki' }}</a>
  </li>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import { removeWord } from '@/api/index';
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
    ...mapGetters(['getUsername', 'getWordList']),
    nowUser() {
      return this.getUsername;
    },
  },
  methods: {
    ...mapMutations(['spliceWordList']),
    onClikeRedirect(link, keyword) {
      link = link ?? `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
    async deleteWord(itemId) {
      itemId;
      // console.log(this.getWordList);
      let arr = this.getWordList;
      let idx = arr.map(m => m.id).indexOf(itemId);

      const { data } = await removeWord(itemId);
      if (data.result === 200) {
        console.log(data.result_description);
        this.spliceWordList(idx);
        this.$emit('change');
      }
    },
  },
};
</script>

<style></style>
