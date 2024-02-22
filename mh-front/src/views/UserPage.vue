<template>
  <div>
    <p>userpage</p>
    <button @click="test">test</button>
  </div>
</template>

<script>
import { testAPI } from '@/api/index';
export default {
  methods: {
    test() {
      testAPI();
    },
  },
};
</script>

<style></style>
