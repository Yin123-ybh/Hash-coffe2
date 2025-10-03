<template>
  <div class="login-container">
    <div class="login-background">
      <div class="login-content">
        <div class="login-card">
          <div class="login-header">
            <h1 class="login-title">智能咖啡管理系统</h1>
            <p class="login-subtitle">门店管理后台</p>
          </div>
          
          <div class="login-form">
            <div class="form-item">
              <input v-model="loginForm.username" placeholder="用户名" />
            </div>
            <div class="form-item">
              <input v-model="loginForm.password" type="password" placeholder="密码" />
            </div>
            <div class="form-item">
              <button @click="handleLogin" :disabled="loading">
                {{ loading ? '登录中...' : '登录' }}
              </button>
            </div>
          </div>
          
          <div class="login-footer">
            <p class="demo-account">
              演示账号：admin / 123456
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { adminAPI } from '@/services/api.js'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    const loginForm = reactive({
      username: '',
      password: ''
    })

    const handleLogin = async () => {
      loading.value = true
      
      try {
        // 调用管理端API进行登录
        const result = await adminAPI.login({
          username: loginForm.username,
          password: loginForm.password
        })
        
        console.log('登录响应:', result)
        
        if (result.code === 200 && result.data) {
          // 保存token和用户信息
          localStorage.setItem('token', result.data.token)
          localStorage.setItem('userInfo', JSON.stringify(result.data))
          
          console.log('登录成功，token已保存:', result.data.token)
          alert('登录成功！')
          
          // 跳转到仪表盘
          router.push('/dashboard')
        } else {
          throw new Error(result.message || '登录失败')
        }
      } catch (error) {
        console.error('登录错误:', error)
        alert('登录失败: ' + error.message)
      } finally {
        loading.value = false
      }
    }

    return {
      loginForm,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.login-background {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-content {
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  color: #333;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.login-subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.form-item {
  margin-bottom: 20px;
}

.form-item input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
}

.form-item button {
  width: 100%;
  padding: 12px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}

.form-item button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.demo-account {
  margin: 0;
  color: #909399;
  font-size: 12px;
}
</style>
