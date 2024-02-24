<template>
  <div class="contents">
    <h1 class="page-header">생성페이지</h1>
    <div class="form-wrapper">
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="titme">Title:</label>
          <input type="text" id="title" v-model="title" @keyup="titleEvent" />
          <p class="validation-text warning" v-if="title.length > 0 && !isTitleValid">제목을 입력해주세요</p>
        </div>
        <div>
          <label for="contents">Contents:</label>
          <textarea type="text" id="contents" v-model="contents" rows="5" />
          <p class="validation-text warning" v-if="contents.length > 0 && !isContentsValid">Text is too long</p>
        </div>
        <div class="btn-groups">
          <button type="submit" class="btn" :disabled="!title || !contents">Create</button>
          <button class="btn2" @click="goBack">돌아가기</button>
        </div>
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
      titleValid: false,
      contentsValid: false,
    };
  },
  computed: {
    isContentsValid() {
      return this.contents.length < 200;
    },
    isTitleValid() {
      return this.title.length < 50;
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
        const { data } = await addWord({ id: this.getUsername, title: this.title, contents: this.contents });
        this.callToast(data.result_description);
        if (data.result === 200) {
          this.$router.push('/main');
        }
        // this.callToast('작성완료');
      } catch (err) {
        this.callToast(err.message);
      }
    },
    callToast(msg) {
      this.emitter.emit('show:toast', msg);
    },
    goBack() {
      this.$router.push('/main');
    },
  },
};
</script>

<style scoped>
form {
  width: 100%;
}
[class^='btn'] {
  width: 100%;
  margin-bottom: 0.5rem;
}
</style>
