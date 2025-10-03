<template>
  <div class="product-list">
    <div class="page-header">
      <h2>商品管理</h2>
      <div class="header-actions">
        <button class="btn-primary" @click="goToAdd">
          ➕ 添加商品
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <div class="filter-row">
        <input
          v-model="filters.keyword"
          placeholder="搜索商品名称"
          class="filter-input"
          @keyup.enter="() => {}"
        />
        <select
          v-model="filters.categoryId"
          class="filter-select"
        >
          <option value="">选择分类</option>
          <option
            v-for="category in categories"
            :key="category.id"
            :value="category.id"
          >
            {{ category.name }}
          </option>
        </select>
        <select
          v-model="filters.status"
          class="filter-select"
        >
          <option value="">商品状态</option>
          <option value="1">上架</option>
          <option value="0">下架</option>
        </select>
        <button class="btn-secondary" @click="loadProducts">
          🔄 刷新
        </button>
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="product-table">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="products.length === 0" class="empty">暂无商品数据</div>
      <div v-else>
        <div v-for="product in products" :key="product.id" class="product-row">
          <div class="product-info">
            <img
              :src="product.image || '/images/default-product.png'"
              :alt="product.name"
              class="product-image"
              @error="handleImageError"
            />
            <div class="product-details">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
            </div>
          </div>
          
          <div class="product-category">
            <span class="category-tag">
              {{ getCategoryName(product.categoryId) }}
            </span>
          </div>
          
          <div class="product-price">
            <div class="current-price">¥{{ product.price }}</div>
            <div 
              v-if="product.originalPrice && product.originalPrice > product.price"
              class="original-price"
            >
              ¥{{ product.originalPrice }}
            </div>
          </div>
          
          <div class="product-stock">
            <span :class="'stock-tag ' + getStockClass(product.stock)">
              {{ product.stock }}
            </span>
          </div>
          
          <div class="product-sales">
            <span class="sales-tag">{{ product.sales || 0 }}</span>
          </div>
          
          <div class="product-tags">
            <span v-if="product.isHot" class="tag hot">热销</span>
            <span v-if="product.isRecommend" class="tag recommend">推荐</span>
          </div>
          
          <div class="product-status">
            <span :class="'status-tag ' + (product.stock > 0 ? 'active' : 'inactive')">
              {{ product.stock > 0 ? '上架' : '缺货' }}
            </span>
          </div>
          
          <div class="product-actions">
            <button class="btn-small" @click="viewProduct(product.id)">
              👁️
            </button>
            <button class="btn-small" @click="editProduct(product.id)">
              ✏️
            </button>
            <button class="btn-small danger" @click="deleteProduct(product.id)">
              🗑️
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
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'ProductList',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const products = ref([])
    const categories = ref([])
    
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0
    })
    
    const filters = reactive({
      keyword: '',
      categoryId: '',
      status: ''
    })

    onMounted(() => {
      // 检查用户是否已登录
      const token = localStorage.getItem('token')
      if (!token) {
        alert('请先登录')
        router.push('/login')
        return
      }
      
      loadCategories()
      loadProducts()
    })

    // 监听筛选条件变化
    watch([() => filters.categoryId, () => filters.status, () => filters.keyword], () => {
      pagination.current = 1 // 重置到第一页
      loadProducts()
    }, { deep: true })

    const loadCategories = async () => {
      try {
        // 调用真实的后端API获取分类
        const token = localStorage.getItem('token')
        console.log('分类加载 - 获取到的token:', token)
        
        const headers = {
          'Content-Type': 'application/json'
        }
        
        // 如果有token，添加Authorization头
        if (token) {
          headers['Authorization'] = `Bearer ${token}`
          console.log('分类加载 - 发送的Authorization头:', headers['Authorization'])
        } else {
          console.warn('分类加载 - 没有找到token')
        }
        
        const response = await fetch('http://127.0.0.1:8089/admin/categories', {
          method: 'GET',
          headers: headers
        })
        
        if (response.ok) {
          const result = await response.json()
          console.log('分类API返回数据:', result)
          if (result.code === 200 && result.data && Array.isArray(result.data)) {
            categories.value = result.data
            console.log('成功加载分类数据:', categories.value)
            return // 成功获取数据，直接返回
          }
        }
        
        // 只有在API调用失败或没有数据时才使用默认分类
        console.warn('API调用失败或无数据，使用默认分类')
        categories.value = [
          { id: 1, name: '咖啡' },
          { id: 2, name: '茶饮' },
          { id: 3, name: '甜品' },
          { id: 4, name: '小食' }
        ]
      } catch (error) {
        console.error('加载分类失败:', error)
        // 使用默认分类
        categories.value = [
          { id: 1, name: '咖啡' },
          { id: 2, name: '茶饮' },
          { id: 3, name: '甜品' },
          { id: 4, name: '小食' }
        ]
      }
    }

    const loadProducts = async () => {
      loading.value = true
      try {
        // 调用真实的后端API
        const token = localStorage.getItem('token')
        console.log('获取到的token:', token)
        
        const headers = {
          'Content-Type': 'application/json'
        }
        
        // 如果有token，添加Authorization头
        if (token) {
          headers['Authorization'] = `Bearer ${token}`
          console.log('发送的Authorization头:', headers['Authorization'])
        } else {
          console.warn('没有找到token')
        }
        
        // 构建查询参数
        const params = new URLSearchParams()
        params.append('page', pagination.current)
        params.append('pageSize', pagination.pageSize)
        
        if (filters.keyword && filters.keyword.trim()) {
          params.append('name', filters.keyword.trim())
        }
        if (filters.categoryId) {
          params.append('categoryId', filters.categoryId)
        }
        if (filters.status !== '') {
          params.append('status', filters.status)
        }
        
        console.log('发送的查询参数:', Object.fromEntries(params))
        
        const response = await fetch(`http://127.0.0.1:8089/admin/products?${params.toString()}`, {
          method: 'GET',
          headers: headers
        })
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        const result = await response.json()
        console.log('从后端获取的商品数据:', result)
        
        if (result.code === 200 && result.data && result.data.records) {
          products.value = result.data.records.map(item => ({
            id: item.id,
            name: item.name,
            categoryId: item.categoryId,
            price: item.price,
            originalPrice: item.originalPrice,
            stock: item.stock || 0,
            sales: item.sales || 0,
            isHot: item.isHot === 1,
            isRecommend: item.isRecommend === 1,
            image: item.image
          }))
          pagination.total = result.data.total
        } else {
          throw new Error(result.message || '获取商品数据失败')
        }
      } catch (error) {
        console.error('加载商品失败:', error)
        alert('加载商品失败: ' + error.message)
        // 如果API调用失败，显示空列表
        products.value = []
        pagination.total = 0
      } finally {
        loading.value = false
      }
    }

    const getCategoryName = (categoryId) => {
      const category = categories.value.find(c => c.id === categoryId)
      return category?.name || '未分类'
    }

    const getStockClass = (stock) => {
      if (stock > 10) return 'high'
      if (stock > 0) return 'medium'
      return 'low'
    }

    const handleImageError = (event) => {
      event.target.src = '/images/default-product.png'
    }

    const changePage = (page) => {
      pagination.current = page
      loadProducts()
    }

    const goToAdd = () => {
      router.push('/products/new')
    }

    const viewProduct = (id) => {
      alert('查看商品详情功能待开发')
    }

    const editProduct = (id) => {
      router.push(`/products/edit/${id}`)
    }

    const deleteProduct = async (id) => {
      if (confirm('确定要删除这个商品吗？')) {
        try {
          // 模拟删除
          products.value = products.value.filter(p => p.id !== id)
          alert('删除成功')
        } catch (error) {
          alert('删除失败')
        }
      }
    }

    return {
      loading,
      products,
      categories,
      pagination,
      filters,
      loadProducts,
      getCategoryName,
      getStockClass,
      handleImageError,
      changePage,
      goToAdd,
      viewProduct,
      editProduct,
      deleteProduct
    }
  }
}
</script>

<style scoped>
.product-list {
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

.btn-primary {
  background: #409eff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary:hover {
  background: #66b1ff;
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

.product-table {
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

.product-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
}

.product-row:last-child {
  border-bottom: none;
}

.product-info {
  display: flex;
  align-items: center;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  margin-right: 12px;
  object-fit: cover;
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: bold;
  margin-bottom: 4px;
  color: #303133;
}

.product-id {
  color: #909399;
  font-size: 12px;
}

.category-tag {
  background: #e6f7ff;
  color: #409eff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.current-price {
  font-weight: bold;
  color: #f56c6c;
  font-size: 16px;
}

.original-price {
  font-size: 12px;
  color: #c0c4cc;
  text-decoration: line-through;
}

.stock-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.stock-tag.high {
  background: #f0f9ff;
  color: #67c23a;
}

.stock-tag.medium {
  background: #fdf6ec;
  color: #e6a23c;
}

.stock-tag.low {
  background: #fef0f0;
  color: #f56c6c;
}

.sales-tag {
  background: #f4f4f5;
  color: #909399;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.product-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.tag {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 10px;
}

.tag.hot {
  background: #fef0f0;
  color: #f56c6c;
}

.tag.recommend {
  background: #f0f9ff;
  color: #67c23a;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-tag.active {
  background: #f0f9ff;
  color: #67c23a;
}

.status-tag.inactive {
  background: #fef0f0;
  color: #f56c6c;
}

.product-actions {
  display: flex;
  gap: 8px;
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
  .product-row {
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