<template>
  <li>
    <div class="post-title">{{ item['infokey'] }}</div>
    <!-- <div class="post-contents" value="item.contents">{{ item.contents }}</div> -->
    <textarea class="post-contents" disabled v-model="description"></textarea>
    <div class="post-time">
      <!-- {{ item['updateDate'] }} -->
      {{ this.$filters.dateFilter(item['updateDate']) }}
      <i class="icon ion-md-create"></i>
      <i class="icon ion-md-trash"></i>
    </div>
  </li>
</template>

<script>
export default {
  props: {
    item: Object,
  },
  data() {
    return {
      description: this.item.description,
    };
  },
};
</script>

<style></style>
