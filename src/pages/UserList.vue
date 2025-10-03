<template>
  <div class="user-list">
    <div class="page-header">
      <h2>用户管理</h2>
      <div class="header-actions">
        <button class="btn-secondary" @click="loadUsers">
          🔄 刷新
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <div class="filter-row">
        <input
          v-model="filters.keyword"
          placeholder="搜索用户名或手机号"
          class="filter-input"
          @keyup.enter="loadUsers"
        />
        <select
          v-model="filters.status"
          class="filter-select"
          @change="loadUsers"
        >
          <option value="">用户状态</option>
          <option value="1">正常</option>
          <option value="0">禁用</option>
        </select>
        <input
          v-model="filters.dateRange"
          type="date"
          class="filter-input"
          @change="loadUsers"
        />
        <button class="btn-secondary" @click="loadUsers">
          🔍 搜索
        </button>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="user-table">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="users.length === 0" class="empty">暂无用户数据</div>
      <div v-else>
        <div v-for="user in users" :key="user.id" class="user-row">
          <div class="user-info">
            <div class="user-avatar">
              {{ user.username.charAt(0).toUpperCase() }}
            </div>
            <div class="user-details">
              <div class="user-name">{{ user.username }}</div>
              <div class="user-phone">{{ user.phone || '未绑定手机' }}</div>
            </div>
          </div>
          
          <div class="user-email">
            {{ user.email || '未绑定邮箱' }}
          </div>
          
          <div class="user-stats">
            <div class="stat-item">
              <span class="stat-label">订单数:</span>
              <span class="stat-value">{{ user.orderCount || 0 }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">消费:</span>
              <span class="stat-value">¥{{ user.totalSpent || 0 }}</span>
            </div>
          </div>
          
          <div class="user-status">
            <span :class="'status-tag ' + (user.status === 1 ? 'active' : 'inactive')">
              {{ user.status === 1 ? '正常' : '禁用' }}
            </span>
          </div>
          
          <div class="user-time">
            <div class="register-time">
              注册: {{ formatDate(user.createTime) }}
            </div>
            <div class="last-login">
              最后登录: {{ formatDate(user.lastLoginTime) }}
            </div>
          </div>
          
          <div class="user-actions">
            <button 
              v-if="user.status === 1"
              class="btn-small danger"
              @click="toggleUserStatus(user.id, 0)"
            >
              禁用
            </button>
            <button 
              v-else
              class="btn-small success"
              @click="toggleUserStatus(user.id, 1)"
            >
              启用
            </button>
            <button class="btn-small" @click="viewUser(user.id)">
              👁️
            </button>
            <button class="btn-small" @click="editUser(user.id)">
              ✏️
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <div class="pagination">
        <button 
          :disabled="pagination.current <= 1"
          @click="changePage(pagination.current - 1)"
        >
          上一页
        </button>
        <span class="page-info">
          第 {{ pagination.current }} 页，共 {{ Math.ceil(pagination.total / pagination.pageSize) }} 页
        </span>
        <button 
          :disabled="pagination.current >= Math.ceil(pagination.total / pagination.pageSize)"
          @click="changePage(pagination.current + 1)"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'UserList',
  setup() {
    const loading = ref(false)
    const users = ref([])
    
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0
    })
    
    const filters = reactive({
      keyword: '',
      status: '',
      dateRange: ''
    })

    onMounted(() => {
      loadUsers()
    })

    const loadUsers = async () => {
      loading.value = true
      try {
        // 模拟用户数据
        await new Promise(resolve => setTimeout(resolve, 500))
        
        users.value = [
          {
            id: 1,
            username: '张三',
            phone: '13800138001',
            email: 'zhangsan@example.com',
            status: 1,
            orderCount: 15,
            totalSpent: 580.50,
            createTime: '2025-09-01 10:30:00',
            lastLoginTime: '2025-09-29 14:30:00'
          },
          {
            id: 2,
            username: '李四',
            phone: '13800138002',
            email: 'lisi@example.com',
            status: 1,
            orderCount: 8,
            totalSpent: 320.00,
            createTime: '2025-09-05 15:20:00',
            lastLoginTime: '2025-09-28 16:45:00'
          },
          {
            id: 3,
            username: '王五',
            phone: '13800138003',
            email: '',
            status: 0,
            orderCount: 3,
            totalSpent: 75.00,
            createTime: '2025-09-10 09:15:00',
            lastLoginTime: '2025-09-25 11:20:00'
          },
          {
            id: 4,
            username: '赵六',
            phone: '',
            email: 'zhaoliu@example.com',
            status: 1,
            orderCount: 22,
            totalSpent: 890.00,
            createTime: '2025-08-20 14:00:00',
            lastLoginTime: '2025-09-29 12:10:00'
          }
        ]
        
        pagination.total = users.value.length
      } catch (error) {
        console.error('加载用户失败:', error)
        alert('加载用户失败')
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateString) => {
      if (!dateString) return '从未'
      const date = new Date(dateString)
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
    }

    const changePage = (page) => {
      pagination.current = page
      loadUsers()
    }

    const toggleUserStatus = async (userId, newStatus) => {
      const action = newStatus === 1 ? '启用' : '禁用'
      if (confirm(`确定要${action}这个用户吗？`)) {
        try {
          const user = users.value.find(u => u.id === userId)
          if (user) {
            user.status = newStatus
            alert(`用户${action}成功`)
          }
        } catch (error) {
          alert(`${action}失败`)
        }
      }
    }

    const viewUser = (id) => {
      alert('查看用户详情功能待开发')
    }

    const editUser = (id) => {
      alert('编辑用户功能待开发')
    }

    return {
      loading,
      users,
      pagination,
      filters,
      loadUsers,
      formatDate,
      changePage,
      toggleUserStatus,
      viewUser,
      editUser
    }
  }
}
</script>

<style scoped>
.user-list {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-secondary {
  background: #f4f4f5;
  color: #606266;
  border: 1px solid #dcdfe6;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-secondary:hover {
  background: #ecf5ff;
  color: #409eff;
}

.filter-section {
  background: #fafafa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.filter-input, .filter-select {
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.filter-input {
  flex: 1;
  max-width: 300px;
}

.filter-select {
  min-width: 120px;
}

.user-table {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.loading, .empty {
  padding: 40px;
  text-align: center;
  color: #909399;
}

.user-row {
  display: grid;
  grid-template-columns: 2fr 2fr 1fr 1fr 1fr 1fr;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
}

.user-row:last-child {
  border-bottom: none;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  margin-right: 12px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.user-phone {
  color: #909399;
  font-size: 12px;
}

.user-email {
  color: #606266;
  font-size: 14px;
}

.user-stats {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.stat-label {
  color: #909399;
}

.stat-value {
  color: #303133;
  font-weight: bold;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  text-align: center;
}

.status-tag.active {
  background: #f0f9ff;
  color: #67c23a;
}

.status-tag.inactive {
  background: #fef0f0;
  color: #f56c6c;
}

.user-time {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
}

.register-time, .last-login {
  color: #909399;
}

.user-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-small {
  padding: 6px 8px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.btn-small:hover {
  background: #f5f7fa;
}

.btn-small.success {
  color: #67c23a;
  border-color: #67c23a;
}

.btn-small.success:hover {
  background: #f0f9ff;
}

.btn-small.danger {
  color: #f56c6c;
  border-color: #f56c6c;
}

.btn-small.danger:hover {
  background: #fef0f0;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background: #f5f7fa;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #606266;
  font-size: 14px;
}

@media (max-width: 768px) {
  .user-row {
    grid-template-columns: 1fr;
    gap: 8px;
  }
  
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-input, .filter-select {
    max-width: none;
  }
}
</style>