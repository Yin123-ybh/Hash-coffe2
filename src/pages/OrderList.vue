<template>
  <div class="order-list">
    <div class="page-header">
      <h2>订单管理</h2>
      <div class="header-actions">
        <button class="btn-secondary" @click="loadOrders">
          🔄 刷新
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <div class="filter-row">
        <input
          v-model="filters.keyword"
          placeholder="搜索订单号或客户名"
          class="filter-input"
          @keyup.enter="loadOrders"
        />
        <select
          v-model="filters.status"
          class="filter-select"
          @change="loadOrders"
        >
          <option value="">订单状态</option>
          <option value="待支付">待支付</option>
          <option value="制作中">制作中</option>
          <option value="已完成">已完成</option>
          <option value="已取消">已取消</option>
        </select>
        <input
          v-model="filters.dateRange"
          type="date"
          class="filter-input"
          @change="loadOrders"
        />
        <button class="btn-secondary" @click="loadOrders">
          🔍 搜索
        </button>
      </div>
    </div>

    <!-- 订单列表 -->
    <div class="order-table">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="orders.length === 0" class="empty">暂无订单数据</div>
      <div v-else>
        <div v-for="order in orders" :key="order.id" class="order-row">
          <div class="order-info">
            <div class="order-no">{{ order.orderNo }}</div>
            <div class="order-customer">{{ order.customerName }}</div>
            <div class="order-time">{{ order.createTime }}</div>
          </div>
          
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <span class="item-name">{{ item.productName }}</span>
              <span class="item-quantity">x{{ item.quantity }}</span>
            </div>
          </div>
          
          <div class="order-amount">
            <div class="total-amount">¥{{ order.totalAmount }}</div>
            <div v-if="order.discount" class="discount">
              优惠: -¥{{ order.discount }}
            </div>
          </div>
          
          <div class="order-status">
            <span :class="'status-tag ' + getStatusClass(order.status)">
              {{ order.status }}
            </span>
          </div>
          
          <div class="order-actions">
            <button 
              v-if="order.status === '待支付'"
              class="btn-small success"
              @click="updateOrderStatus(order.id, '制作中')"
            >
              接单
            </button>
            <button 
              v-if="order.status === '制作中'"
              class="btn-small primary"
              @click="updateOrderStatus(order.id, '已完成')"
            >
              完成
            </button>
            <button 
              v-if="order.status === '待支付'"
              class="btn-small danger"
              @click="updateOrderStatus(order.id, '已取消')"
            >
              取消
            </button>
            <button class="btn-small" @click="viewOrder(order.id)">
              👁️
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
  name: 'OrderList',
  setup() {
    const loading = ref(false)
    const orders = ref([])
    
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
      // 检查用户是否已登录
      const token = localStorage.getItem('token')
      if (!token) {
        alert('请先登录')
        router.push('/login')
        return
      }
      
      loadOrders()
    })

    const loadOrders = async () => {
      loading.value = true
      try {
        // 调用真实的后端API
        const token = localStorage.getItem('token')
        const headers = {
          'Content-Type': 'application/json'
        }
        
        // 如果有token，添加Authorization头
        if (token) {
          headers['Authorization'] = `Bearer ${token}`
        }
        
        const response = await fetch('http://127.0.0.1:8089/order/list', {
          method: 'GET',
          headers: headers
        })
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        const result = await response.json()
        console.log('从后端获取的订单数据:', result)
        
        if (result.code === 200 && result.data) {
          orders.value = result.data.map(item => ({
            id: item.id,
            orderNo: item.order_no || item.orderNo,
            customerName: item.customer_name || item.customerName || '未知客户',
            createTime: item.create_time || item.createTime,
            totalAmount: item.total_amount || item.totalAmount || 0,
            discount: item.discount || 0,
            status: item.status || '待支付',
            items: item.items || []
          }))
          pagination.total = orders.value.length
        } else {
          throw new Error(result.message || '获取订单数据失败')
        }
      } catch (error) {
        console.error('加载订单失败:', error)
        alert('加载订单失败: ' + error.message)
        // 如果API调用失败，显示空列表
        orders.value = []
        pagination.total = 0
      } finally {
        loading.value = false
      }
    }

    const getStatusClass = (status) => {
      const statusMap = {
        '待支付': 'pending',
        '制作中': 'processing',
        '已完成': 'completed',
        '已取消': 'cancelled'
      }
      return statusMap[status] || 'pending'
    }

    const changePage = (page) => {
      pagination.current = page
      loadOrders()
    }

    const updateOrderStatus = async (orderId, newStatus) => {
      if (confirm(`确定要将订单状态更新为"${newStatus}"吗？`)) {
        try {
          const order = orders.value.find(o => o.id === orderId)
          if (order) {
            order.status = newStatus
            alert('订单状态更新成功')
          }
        } catch (error) {
          alert('更新失败')
        }
      }
    }

    const viewOrder = (id) => {
      alert('查看订单详情功能待开发')
    }

    return {
      loading,
      orders,
      pagination,
      filters,
      loadOrders,
      getStatusClass,
      changePage,
      updateOrderStatus,
      viewOrder
    }
  }
}
</script>

<style scoped>
.order-list {
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

.order-table {
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

.order-row {
  display: grid;
  grid-template-columns: 2fr 2fr 1fr 1fr 1fr;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  align-items: start;
}

.order-row:last-child {
  border-bottom: none;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-no {
  font-weight: bold;
  color: #303133;
  font-size: 16px;
}

.order-customer {
  color: #606266;
  font-size: 14px;
}

.order-time {
  color: #909399;
  font-size: 12px;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.item-name {
  color: #303133;
}

.item-quantity {
  color: #909399;
  font-size: 12px;
}

.order-amount {
  text-align: right;
}

.total-amount {
  font-weight: bold;
  color: #f56c6c;
  font-size: 16px;
}

.discount {
  color: #67c23a;
  font-size: 12px;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  text-align: center;
}

.status-tag.pending {
  background: #fff7e6;
  color: #fa8c16;
}

.status-tag.processing {
  background: #e6f7ff;
  color: #409eff;
}

.status-tag.completed {
  background: #f0f9ff;
  color: #67c23a;
}

.status-tag.cancelled {
  background: #fef0f0;
  color: #f56c6c;
}

.order-actions {
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

.btn-small.primary {
  color: #409eff;
  border-color: #409eff;
}

.btn-small.primary:hover {
  background: #ecf5ff;
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
  .order-row {
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