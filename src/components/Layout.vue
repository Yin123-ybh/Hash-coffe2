<template>
  <div class="layout">
    <div class="sidebar">
      <div class="logo">
        ☕
        <span>咖啡管理</span>
      </div>
      <nav class="sidebar-menu">
        <a href="/dashboard" :class="{ active: $route.path === '/dashboard' }">
          📊 仪表盘
        </a>
        <a href="/products" :class="{ active: $route.path === '/products' }">
          🛍️ 商品管理
        </a>
        <a href="/orders" :class="{ active: $route.path === '/orders' }">
          📦 订单管理
        </a>
        <a href="/users" :class="{ active: $route.path === '/users' }">
          👥 用户管理
        </a>
        <a href="/ai-analysis" :class="{ active: $route.path === '/ai-analysis' }">
          🤖 AI分析
        </a>
      </nav>
    </div>
    
    <div class="main-container">
      <header class="header">
        <div class="header-left">
          <h1>智能咖啡管理系统</h1>
        </div>
        <div class="header-right">
          <div class="user-dropdown">
            <span>👤 {{ userInfo.username || '用户' }}</span>
            <button @click="handleLogout">退出登录</button>
          </div>
        </div>
      </header>
      
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Layout',
  setup() {
    const router = useRouter()
    const userInfo = ref({})

    onMounted(() => {
      // 获取用户信息
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        try {
          userInfo.value = JSON.parse(storedUserInfo)
        } catch (error) {
          console.error('解析用户信息失败:', error)
        }
      }
    })

    const handleLogout = () => {
      // 清除本地数据
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      
      // 跳转到登录页
      router.push('/login')
      alert('已退出登录')
    }

    return {
      userInfo,
      handleLogout
    }
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #304156;
  color: white;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4b;
  font-size: 18px;
  font-weight: bold;
  gap: 8px;
}

.sidebar-menu {
  padding: 20px 0;
}

.sidebar-menu a {
  display: block;
  padding: 12px 20px;
  color: #bfcbd9;
  text-decoration: none;
  transition: all 0.3s;
}

.sidebar-menu a:hover {
  background-color: #263445;
  color: #409eff;
}

.sidebar-menu a.active {
  background-color: #409eff;
  color: white;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.header-left h1 {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-dropdown button {
  padding: 6px 12px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.user-dropdown button:hover {
  background: #f78989;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
  overflow-y: auto;
}
</style>