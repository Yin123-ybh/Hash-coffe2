<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>仪表盘</h2>
      <p>欢迎使用智能咖啡管理系统</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon orders">
            📦
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalOrders }}</div>
            <div class="stat-label">总订单数</div>
            <div class="stat-trend up">
              ↗️ 12% 较昨日
            </div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon users">
            👥
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalUsers }}</div>
            <div class="stat-label">总用户数</div>
            <div class="stat-trend up">
              ↗️ 8% 较昨日
            </div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon revenue">
            💰
          </div>
          <div class="stat-info">
            <div class="stat-value">¥{{ stats.totalRevenue }}</div>
            <div class="stat-label">总收入</div>
            <div class="stat-trend up">
              ↗️ 15% 较昨日
            </div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon products">
            🏆
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalProducts }}</div>
            <div class="stat-label">商品数量</div>
            <div class="stat-trend down">
              ↘️ 2% 较昨日
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-row">
      <div class="content-card">
        <h3>最近订单</h3>
        <div v-if="loading">加载中...</div>
        <div v-else>
          <div v-for="order in recentOrders" :key="order.orderNo" class="order-item">
            <div class="order-info">
              <div class="order-no">{{ order.orderNo }}</div>
              <div class="order-customer">{{ order.customer }}</div>
            </div>
            <div class="order-amount">¥{{ order.amount }}</div>
            <div class="order-status" :class="'status-' + order.status">
              {{ order.status }}
            </div>
          </div>
        </div>
      </div>
      
      <div class="content-card">
        <h3>热销商品</h3>
        <div v-if="loading">加载中...</div>
        <div v-else>
          <div v-for="product in topProducts" :key="product.name" class="product-item">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-sales">{{ product.sales }}杯</div>
            <div class="product-revenue">¥{{ product.revenue }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions-row">
      <div class="quick-card">
        <h3>今日概览</h3>
        <div class="quick-content">
          <div class="quick-stat">
            <div class="quick-value">今日订单: 23单</div>
            <div class="quick-desc">今日收入: ¥1,245.50</div>
          </div>
        </div>
      </div>
      
      <div class="quick-card">
        <h3>系统状态</h3>
        <div class="quick-content">
          <div class="status-item">
            <span>系统运行</span>
            <span class="status-tag success">正常</span>
          </div>
          <div class="status-item">
            <span>数据库</span>
            <span class="status-tag success">正常</span>
          </div>
          <div class="status-item">
            <span>AI服务</span>
            <span class="status-tag success">正常</span>
          </div>
        </div>
      </div>
      
      <div class="quick-card">
        <h3>快速操作</h3>
        <div class="quick-content">
          <div class="action-item">
            <a href="/products/new">添加新商品</a>
          </div>
          <div class="action-item">
            <a href="/orders">查看所有订单</a>
          </div>
          <div class="action-item">
            <a href="/ai-analysis">AI数据分析</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'Dashboard',
  setup() {
    const loading = ref(false)
    const stats = reactive({
      totalOrders: 156,
      totalUsers: 89,
      totalRevenue: 12580.50,
      totalProducts: 12
    })
    const recentOrders = ref([
      {
        orderNo: '20250929001',
        customer: '张三',
        amount: 58.00,
        status: '已完成',
        createTime: '2025-09-29 14:30:00'
      },
      {
        orderNo: '20250929002',
        customer: '李四',
        amount: 32.50,
        status: '制作中',
        createTime: '2025-09-29 14:20:00'
      },
      {
        orderNo: '20250929003',
        customer: '王五',
        amount: 75.00,
        status: '待支付',
        createTime: '2025-09-29 14:15:00'
      }
    ])
    const topProducts = ref([
      { name: '美式咖啡', sales: 45, revenue: 810.00 },
      { name: '拿铁咖啡', sales: 38, revenue: 836.00 },
      { name: '卡布奇诺', sales: 32, revenue: 1040.00 },
      { name: '摩卡咖啡', sales: 28, revenue: 1050.00 }
    ])

    onMounted(() => {
      console.log('Dashboard组件已挂载')
    })

    return {
      loading,
      stats,
      recentOrders,
      topProducts
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
}

.stat-icon.orders {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.users {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.revenue {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.products {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  font-size: 12px;
}

.stat-trend.up {
  color: #67c23a;
}

.stat-trend.down {
  color: #f56c6c;
}

.content-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.content-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.content-card h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 18px;
}

.order-item, .product-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child, .product-item:last-child {
  border-bottom: none;
}

.order-info {
  flex: 1;
}

.order-no, .product-name {
  font-weight: bold;
  margin-bottom: 4px;
}

.order-customer {
  color: #666;
  font-size: 12px;
}

.order-amount, .product-revenue {
  color: #f56c6c;
  font-weight: bold;
}

.order-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-已完成 {
  background: #f0f9ff;
  color: #67c23a;
}

.status-制作中 {
  background: #e6f7ff;
  color: #409eff;
}

.status-待支付 {
  background: #fff7e6;
  color: #fa8c16;
}

.quick-actions-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.quick-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.quick-card h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 18px;
}

.quick-stat {
  text-align: center;
}

.quick-value {
  font-size: 20px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 8px;
}

.quick-desc {
  font-size: 14px;
  color: #666;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.status-tag {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-tag.success {
  background: #f0f9ff;
  color: #67c23a;
}

.action-item {
  margin-bottom: 12px;
}

.action-item a {
  color: #409eff;
  text-decoration: none;
}

.action-item a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .content-row {
    grid-template-columns: 1fr;
  }
  
  .stats-row {
    grid-template-columns: 1fr;
  }
  
  .quick-actions-row {
    grid-template-columns: 1fr;
  }
}
</style>
