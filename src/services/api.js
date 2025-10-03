import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://127.0.0.1:8089',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { data } = response
    if (data.code === 200) {
      return data
    } else {
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 管理端相关API
export const adminAPI = {
  // 管理员登录
  login: (credentials) => 
    request.post('/admin/login', credentials),
  
  // 获取管理员信息
  getAdminInfo: () => 
    request.get('/admin/info'),
}

// 用户相关API（小程序端使用）
export const userAPI = {
  // 登录
  login: (credentials) => 
    request.post('/user/login', credentials),
  
  // 获取用户信息
  getUserInfo: () => 
    request.get('/user/info'),
}

// 商品相关API
export const productAPI = {
  // 获取商品分类
  getCategories: () => 
    request.get('/admin/categories'),
  
  // 获取商品列表
  getProducts: (params = {}) => 
    request.get('/admin/products', { params }),
  
  // 获取商品详情
  getProductDetail: (id) => 
    request.get(`/admin/products/${id}`),
  
  // 创建商品
  createProduct: (data) => 
    request.post('/admin/products', data),
  
  // 更新商品
  updateProduct: (data) => 
    request.put('/admin/products', data),
  
  // 删除商品
  deleteProduct: (id) => 
    request.delete(`/admin/products/${id}`),
}

// 订单相关API
export const orderAPI = {
  // 获取订单列表
  getOrders: (params = {}) => 
    request.get('/order/list', { params }),
  
  // 获取订单详情
  getOrderDetail: (id) => 
    request.get(`/order/${id}`),
  
  // 更新订单状态
  updateOrderStatus: (id, status) => 
    request.put(`/order/${id}/status`, { status }),
}

// AI相关API
export const aiAPI = {
  // AI对话
  chat: (params = {}) => 
    request.post('/ai/chat', params),
}

export default request