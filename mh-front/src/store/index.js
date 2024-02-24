import { createStore } from 'vuex';
import userStore from '@/store/modules/userStore';
import wordStore from '@/store/modules/wordStore';

const store = createStore({
  modules: { userStore, wordStore },
});

export default store;
