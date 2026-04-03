import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/resident/create',
    name: 'ResidentCreate',
    component: () => import('@/views/ResidentCreate.vue')
  },
  {
    path: '/resident/:id',
    name: 'ResidentDetail',
    component: () => import('@/views/ResidentDetail.vue'),
    props: true
  },
  {
    path: '/treatment/create',
    name: 'TreatmentCreate',
    component: () => import('@/views/TreatmentCreate.vue')
  },
  {
    path: '/treatment/list/:id',
    name: 'TreatmentList',
    component: () => import('@/views/TreatmentList.vue'),
    props: true
  },
  {
    path: '/stats',
    name: 'Stats',
    component: () => import('@/views/StatsView.vue')
  },
  {
    path: '/trend/:id',
    name: 'Trend',
    component: () => import('@/views/TrendView.vue'),
    props: true
  },
  {
    path: '/report/:id',
    name: 'Report',
    component: () => import('@/views/ReportView.vue'),
    props: true
  },
  {
    path: '/daily',
    name: 'Daily',
    component: () => import('@/views/DailyView.vue')
  },
  {
    path: '/mine',
    name: 'Mine',
    component: () => import('@/views/MineView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
