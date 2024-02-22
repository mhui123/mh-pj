import { createStore } from 'vuex';
import userStore from '@/store/modules/userStore';

const store = createStore({
  modules: { userStore },
});

export default store;
