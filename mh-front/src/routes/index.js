import { createWebHistory, createRouter } from 'vue-router';

const routes = [
  { path: '/', redirect: '/main' },
  { path: '/login', component: () => import('@/views/LoginPage.vue') },
  { path: '/signup', component: () => import('@/views/SignupPage.vue') },
  { path: '/user', component: () => import('@/views/UserPage.vue'), meta: { auth: true } },
  { path: '/main', component: () => import('@/views/MainPage.vue') },
  // { path: '*', component: () => import('@/views/NotFoundPage.vue') },
];

const router = createRouter({ history: createWebHistory(), routes });
router.beforeEach((to, from, next) => {
  if (to.meta.auth) {
    console.log('인증이 필요한 페이지입니다.');
    next('/login');
    return false;
  }
  next();
});
export default router;
