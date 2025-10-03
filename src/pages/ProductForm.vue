<template>
  <div class="product-form">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑商品' : '添加商品' }}</h2>
      <button class="btn-secondary" @click="goBack">
        ← 返回列表
      </button>
    </div>

    <div class="form-container">
      <form @submit.prevent="submitForm">
        <div class="form-row">
          <div class="form-group">
            <label>商品名称 *</label>
            <input
              v-model="form.name"
              type="text"
              placeholder="请输入商品名称"
              required
            />
          </div>
          <div class="form-group">
            <label>商品分类 *</label>
            <select v-model="form.categoryId" required>
              <option value="">请选择分类</option>
              <option
                v-for="category in categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>商品价格 *</label>
            <input
              v-model="form.price"
              type="number"
              step="0.01"
              placeholder="0.00"
              required
            />
          </div>
          <div class="form-group">
            <label>原价</label>
            <input
              v-model="form.originalPrice"
              type="number"
              step="0.01"
              placeholder="0.00"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>库存数量 *</label>
            <input
              v-model="form.stock"
              type="number"
              placeholder="0"
              required
            />
          </div>
          <div class="form-group">
            <label>商品图片</label>
            <ImageUpload v-model:value="form.image" />
          </div>
        </div>

        <div class="form-group">
          <label>商品描述</label>
          <textarea
            v-model="form.description"
            placeholder="请输入商品描述"
            rows="4"
          ></textarea>
        </div>

        <div class="form-row">
          <div class="form-group checkbox-group">
            <label>
              <input
                v-model="form.isHot"
                type="checkbox"
              />
              热销商品
            </label>
          </div>
          <div class="form-group checkbox-group">
            <label>
              <input
                v-model="form.isRecommend"
                type="checkbox"
              />
              推荐商品
            </label>
          </div>
        </div>

        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="goBack">
            取消
          </button>
          <button type="submit" class="btn-primary" :disabled="loading">
            {{ loading ? '保存中...' : '保存' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import ImageUpload from '../components/ImageUpload.vue'

export default {
  name: 'ProductForm',
  components: {
    ImageUpload
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const loading = ref(false)
    const categories = ref([])
    
    const form = reactive({
      name: '',
      categoryId: '',
      price: '',
      originalPrice: '',
      stock: '',
      image: '',
      description: '',
      isHot: false,
      isRecommend: false
    })

    const isEdit = computed(() => {
      // 如果路由名称是 ProductNew，说明是新增
      if (route.name === 'ProductNew') return false
      // 如果路由名称是 ProductEdit，说明是编辑
      if (route.name === 'ProductEdit') return true
      // 兜底逻辑：检查参数id
      return route.params.id && route.params.id !== 'new'
    })

    onMounted(() => {
      loadCategories()
      if (isEdit.value) {
        loadProduct()
      }
    })

    const loadCategories = async () => {
      try {
        // 模拟分类数据
        categories.value = [
          { id: 1, name: '咖啡' },
          { id: 2, name: '茶饮' },
          { id: 3, name: '甜品' },
          { id: 4, name: '小食' }
        ]
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    }

    const loadProduct = async () => {
      if (!isEdit.value) return
      
      try {
        const token = localStorage.getItem('token')
        const response = await fetch(`http://127.0.0.1:8089/admin/products/${route.params.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.code === 200) {
            // 转换后端返回的整数为前端需要的布尔值
            const productData = {
              ...result.data,
              isHot: result.data.isHot === 1,
              isRecommend: result.data.isRecommend === 1
            }
            Object.assign(form, productData)
          }
        }
      } catch (error) {
        console.error('加载商品失败:', error)
        alert('加载商品失败')
      }
    }

    const submitForm = async () => {
      loading.value = true
      try {
        const token = localStorage.getItem('token')
        
        // 调试信息
        console.log('=== 开始调试商品保存 ===')
        console.log('当前路由信息:', {
          name: route.name,
          params: route.params,
          path: route.path
        })
        console.log('isEdit值:', isEdit.value)
        
        // 强制判断：如果URL包含 '/new' 就是新增，否则是编辑
        const isNewProduct = route.path.includes('/new') || route.name === 'ProductNew'
        const method = isNewProduct ? 'POST' : 'PUT'
        console.log('使用的方法:', method)
        console.log('是否新增商品:', isNewProduct)
        
        const url = `http://127.0.0.1:8089/admin/products`
        console.log('请求URL:', url)
        
        // 转换布尔值为整数
        const formData = {
          ...form,
          isHot: form.isHot ? 1 : 0,
          isRecommend: form.isRecommend ? 1 : 0
        }
        
        console.log('发送的数据:', formData)
        console.log('请求方法:', method)
        
        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(formData)
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.code === 200) {
            alert(isEdit.value ? '商品更新成功' : '商品添加成功')
            router.push('/products')
          } else {
            throw new Error(result.msg || '操作失败')
          }
        } else {
          throw new Error('操作失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        alert('保存失败: ' + error.message)
      } finally {
        loading.value = false
      }
    }

    const goBack = () => {
      router.push('/products')
    }

    return {
      loading,
      categories,
      form,
      isEdit,
      submitForm,
      goBack
    }
  }
}
</script>

<style scoped>
.product-form {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.btn-secondary {
  background: #f4f4f5;
  color: #606266;
  border: 1px solid #dcdfe6;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  text-decoration: none;
  display: inline-block;
}

.btn-secondary:hover {
  background: #ecf5ff;
  color: #409eff;
}

.form-container {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  color: #303133;
  font-weight: 500;
  font-size: 14px;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #409eff;
}

.checkbox-group {
  flex-direction: row;
  align-items: center;
}

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 0;
  cursor: pointer;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
  margin: 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-primary {
  background: #409eff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover:not(:disabled) {
  background: #66b1ff;
}

.btn-primary:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style>