import { createWebHistory, createRouter } from 'vue-router';
import userStore from '@/store/modules/userStore';
import { getItem } from '@/utils/localstorageM';

const routes = [
  { path: '/', redirect: '/main' },
  { path: '/login', component: () => import('@/views/LoginPage.vue') },
  { path: '/signin', component: () => import('@/views/SigninPage.vue') },
  { path: '/user', component: () => import('@/views/UserPage.vue'), meta: { auth: true } },
  { path: '/main', component: () => import('@/views/MainPage.vue') },
  { path: '/add', component: () => import('@/views/WordAddPage.vue'), meta: { auth: true } },
  { path: '/fetch', component: () => import('@/views/FetchPage.vue') },
  { path: '/word/:id', component: () => import('@/views/WordAddPage.vue'), meta: { auth: true } },
  { path: '/mypage', component: () => import('@/views/MyPage.vue'), meta: { auth: true } },
  { path: '/adminPage', component: () => import('@/views/AdminPage.vue'), meta: { auth: true, role: 'ROLE_ADMIN' } },
  //fetchPage
  // { path: '*', component: () => import('@/views/NotFoundPage.vue') },
];

const router = createRouter({ history: createWebHistory(), routes });
router.beforeEach((to, from, next) => {
  if (to.meta.role && to.meta.role !== getItem('role')) {
    alert('해당 유저는 페이지 권한이 없습니다.');
    next('/main');
    return false;
  }

  if (to.meta.auth && !userStore.getters.getAccessToken) {
    console.log('인증이 필요한 페이지입니다.');
    next('/login');
    return false;
  }
  next();
});
export default router;
