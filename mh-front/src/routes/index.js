import { createWebHistory, createRouter } from 'vue-router';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('@/views/LoginPage.vue') },
  { path: '/signup', component: () => import('@/views/SignupPage.vue') },
  // { path: '*', component: () => import('@/views/NotFoundPage.vue') },
];

const router = createRouter({ history: createWebHistory(), routes });
export default router;
