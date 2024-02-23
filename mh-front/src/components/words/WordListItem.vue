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
        <i class="icon ion-md-trash"></i>
      </span>
    </div>
    <a @click="onClikeRedirect(item['link'], item['infokey'])" class="link">{{ item['link'] ?? 'wiki' }}</a>
  </li>
</template>

<script>
import { mapGetters } from 'vuex';
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
    ...mapGetters(['getUsername']),
    nowUser() {
      return this.getUsername;
    },
  },
  methods: {
    onClikeRedirect(link, keyword) {
      link = link ?? `https://ko.wikipedia.org/wiki/${keyword}`;
      window.open(link, '_blank');
    },
  },
};
</script>

<style></style>
