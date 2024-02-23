import { createApp } from 'vue';
import App from './App.vue';
import router from '@/routes/index';
import store from '@/store/index';
import mitt from 'mitt';
import { formatDate } from '@/utils/filters';

const emitter = mitt();
const app = createApp(App);
app.config.globalProperties.emitter = emitter;
app.config.globalProperties.$filters = {
  dateFilter(date) {
    return formatDate(date);
  },
};

app.use(router).use(store).mount('#app');
