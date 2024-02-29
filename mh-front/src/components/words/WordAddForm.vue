<template>
  <div class="contents">
    <h1 class="page-header">{{ pageTitle }}</h1>
    <div class="form-wrapper">
      <!-- <form @submit.prevent="submitForm" class="form"> -->
      <form class="form" @submit.prevent>
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
        <div>
          <label for="contents">참고링크:</label>
          <input type="text" id="linkField" v-model="link" placeholder="참고할 링크가 있다면 입력해주세요" />
          <!-- <p class="validation-text warning" v-if="contents.length > 0 && !isContentsValid">Text is too long</p> -->
        </div>
        <div class="btn-groups">
          <button class="btn" :disabled="!title || !contents || !isContentsValid || !isTitleValid" v-if="mode === 'create'" @click="submitForm">Create</button>
          <button class="btn" :disabled="!title || !contents || !isContentsValid || !isTitleValid" v-if="mode === 'edit'" @click="submitForm">Edit</button>
          <button class="btn2" @click="goBack">돌아가기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { callApi } from '@/api/index';
import { mapGetters, mapMutations } from 'vuex';
import { sleep } from '@/utils/common';
export default {
  data() {
    return {
      pageTitle: '',
      title: '',
      contents: '',
      titleValid: false,
      contentsValid: false,
      mode: 'create',
      wordId: '',
      link: '',
    };
  },
  computed: {
    isContentsValid() {
      return this.contents.length < 200;
    },
    isTitleValid() {
      return this.title.length < 50;
    },
    fetchWordId() {
      return this.getWordId;
    },
    ...mapGetters(['getUsername', 'getWordId']),
  },
  async created() {
    // this.wordId = this.$route.params['id'];
    const thisRoute = this.$router.currentRoute;
    const isEditPage = thisRoute.value.fullPath.includes('edit');
    this.wordId = this.getWordId;
    if (isEditPage && !this.wordId) {
      this.callToast('비정상적 접근입니다. 메인페이지로 돌아갑니다.');
      await sleep(2000);
      this.$router.push('/main');
      return false;
    }
    if (this.wordId) {
      this.mode = 'edit';
      this.pageTitle = '수정';
      const { data } = await callApi('getWordById', { id: this.wordId });
      this.setOrigin(data);
    } else {
      console.log('here is create page');
      this.pageTitle = '생성';
    }
  },
  methods: {
    ...mapMutations(['clearWordId']),
    titleEvent() {
      if (!this.isTitleValid) {
        this.titleInvalid = false;
      } else {
        this.titleInvalid = true;
      }
    },
    async submitForm() {
      let result;
      try {
        this.link = this.link.length === 0 ? 'none' : this.link.startsWith('http') ? this.link : `http://${this.link}`;
        if (this.mode === 'edit') {
          let { data } = await callApi('edit', { editor: this.getUsername, title: this.title, contents: this.contents, wordId: this.wordId, link: this.link });
          result = data;
        } else {
          let { data } = await callApi('addWord', { id: this.getUsername, title: this.title, contents: this.contents, link: this.link });
          result = data;
        }
        this.callToast(result.result_description);
        if (result.result === 200) {
          this.clearWordId();
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
    setOrigin(data) {
      this.title = data.infokey;
      this.contents = data.description;
      this.link = data.link === 'none' ? '' : data.link;
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
