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
        <div class="btn-groups">
          <button class="btn" :disabled="!title || !contents" v-if="mode === 'create'" @click="submitForm">Create</button>
          <button class="btn" v-if="mode === 'edit'" @click="editWord">Edit</button>
          <button class="btn2" @click="goBack">돌아가기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { addWord, getWord } from '@/api/index';
import { mapGetters } from 'vuex';
export default {
  data() {
    return {
      pageTitle: '',
      title: '',
      contents: '',
      titleValid: false,
      contentsValid: false,
      mode: 'create',
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
  async created() {
    const id = this.$route.params['id'];
    if (id) {
      this.mode = 'edit';
      this.pageTitle = '수정';
      const { data } = await getWord(id);
      this.setOrigin(data);
    } else {
      console.log('here is create page');
      this.pageTitle = '생성';
    }
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
    setOrigin(data) {
      this.title = data.infokey;
      this.contents = data.description;
    },
    editWord() {
      console.log('edit');
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
