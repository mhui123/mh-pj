<template>
  <div class="contents">
    <h1 class="page-header">생성페이지</h1>
    <div class="form-wrapper">
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="titme">Title:</label>
          <input type="text" id="title" v-model="title" @keyup="titleEvent" />
          <p class="validation-text warning" v-if="titleInvalid">제목을 입력해주세요</p>
        </div>
        <div>
          <label for="contents">Contents:</label>
          <textarea type="text" id="contents" v-model="contents" rows="5" />
          <p class="validation-text warning" v-if="!isContentsValid">Text is too long</p>
        </div>
        <button type="submit" class="btn" :disabled="!title || !contents">Create</button>
      </form>
    </div>
  </div>
</template>

<script>
import { addWord } from '@/api/index';
import { mapGetters } from 'vuex';
export default {
  data() {
    return {
      title: '',
      contents: '',
      titleInvalid: false,
    };
  },
  computed: {
    isContentsValid() {
      return this.contents.length > 0 && this.contents.length < 200;
    },
    isTitleValid() {
      return this.title.length > 0 && this.title.length < 50;
    },
    ...mapGetters(['getUsername']),
  },
  methods: {
    titleEvent() {
      if (!this.isTitleValid) {
        this.titleInvalid = false;
      } else {
        this.titleInvalid = true;
      }
    },
    async submitForm() {
      try {
        const response = await addWord({ id: this.getUsername, title: this.title, contents: this.contents });
        console.log(`[addWord] : ${response}`);
        // this.callToast('작성완료');
      } catch (err) {
        this.callToast(err.response.data.message);
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
  },
};
</script>

<style></style>
