import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'
import Login from '@/pages/Login.vue'
import Dashboard from '@/pages/Dashboard.vue'
import ProductList from '@/pages/ProductList.vue'
import ProductForm from '@/pages/ProductForm.vue'
import OrderList from '@/pages/OrderList.vue'
import UserList from '@/pages/UserList.vue'
import AiAnalysis from '@/pages/AiAnalysis.vue'
import { adminAPI } from '@/services/api.js'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'products',
        name: 'ProductList',
        component: ProductList
      },
      {
        path: 'products/new',
        name: 'ProductNew',
        component: ProductForm
      },
      {
        path: 'products/edit/:id',
        name: 'ProductEdit',
        component: ProductForm
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: OrderList
      },
      {
        path: 'users',
        name: 'UserList',
        component: UserList
      },
      {
        path: 'ai-analysis',
        name: 'AiAnalysis',
        component: AiAnalysis
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  console.log('路由守卫触发:', to.path)
  const token = localStorage.getItem('token')
  console.log('当前token:', token)
  
  if (to.path === '/login') {
    console.log('访问登录页')
    // 如果已经登录，访问登录页时跳转到dashboard
    if (token) {
      console.log('已登录，跳转到dashboard')
      next('/dashboard')
    } else {
      console.log('未登录，显示登录页')
      next()
    }
  } else if (!token) {
    console.log('没有token，跳转到登录页')
    // 没有token，跳转到登录页
    next('/login')
  } else {
    console.log('有token，验证token有效性')
    // 有token，验证token是否有效
    try {
      const response = await adminAPI.getAdminInfo()
      console.log('token验证成功，放行')
      // token有效，放行
      next()
    } catch (error) {
      console.log('token验证失败:', error)
      // token无效，清除并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      next('/login')
    }
  }
})

export default router
