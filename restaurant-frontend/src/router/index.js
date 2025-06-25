import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/layout/LayoutView.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'DashboardHome',
        component: () => import('@/views/dashboard/DashboardView.vue')
      },
      {
        path: '/dish',
        name: 'DishManagement',
        component: () => import('@/views/dish/DishView.vue')
      },
      {
        path: '/category',
        name: 'CategoryManagement',
        component: () => import('@/views/dish/CategoryView.vue')
      },
      {
        path: '/order',
        name: 'OrderManagement',
        component: () => import('@/views/order/OrderView.vue')
      },
      {
        path: '/inventory',
        name: 'InventoryManagement',
        component: () => import('@/views/inventory/InventoryView.vue')
      },
      {
        path: '/employee',
        name: 'EmployeeManagement',
        component: () => import('@/views/employee/EmployeeView.vue')
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/StatisticsView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else if (to.path === '/login' && userStore.token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
