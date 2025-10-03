import { createRouter, createWebHistory } from 'vue-router'
import LearningCenter from '../views/LearningCenter.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/learning-center',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue'),
    },
    {
      path: '/learning-center',
      name: 'learning-center',
      component: LearningCenter,
    },
    {
      path: '/qa-system',
      name: 'qa-system',
      component: () => import('../views/QASystem.vue'),
    },
    {
      path: '/tools',
      name: 'tools',
      component: () => import('../views/Tools.vue'),
    },
    {
      path: '/projects',
      name: 'projects',
      component: () => import('../views/Projects.vue'),
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/Profile.vue'),
    },
  ],
})

export default router
