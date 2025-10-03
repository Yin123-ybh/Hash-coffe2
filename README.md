# 智能咖啡管理系统 - 前端

这是智能咖啡管理系统的前端项目，基于Vue.js开发。

## 技术栈

- Vue.js 3
- Vue Router
- Axios
- Element Plus (可选)

## 项目结构

```
src/
├── components/          # 公共组件
│   ├── Layout.vue      # 布局组件
│   ├── ImageUpload.vue # 图片上传组件
│   └── ProtectedRoute.js # 路由守卫
├── pages/              # 页面组件
│   ├── Login.vue       # 登录页面
│   ├── Dashboard.vue  # 仪表盘
│   ├── ProductList.vue # 商品列表
│   ├── ProductForm.vue # 商品表单
│   ├── OrderList.vue   # 订单列表
│   ├── UserList.vue    # 用户列表
│   └── AiAnalysis.vue  # AI分析
├── router/             # 路由配置
│   └── index.js
├── services/           # API服务
│   └── api.js
└── assets/            # 静态资源
```

## 功能特性

- 🔐 用户认证与授权
- 📦 商品管理（增删改查）
- 🛒 订单管理
- 👥 用户管理
- 📊 数据统计与分析
- 🤖 AI智能分析
- 📸 图片上传（阿里云OSS）

## 安装与运行

### 环境要求

- Node.js >= 14.0
- npm >= 6.0 或 yarn >= 1.22

### 安装依赖

```bash
# 使用 npm
npm install

# 或使用 yarn
yarn install
```

### 开发环境运行

```bash
# 使用 npm
npm run serve

# 或使用 yarn
yarn serve
```

### 生产环境构建

```bash
# 使用 npm
npm run build

# 或使用 yarn
yarn build
```

## 配置说明

### 后端API地址

在 `src/services/api.js` 中配置后端API地址：

```javascript
const baseURL = 'http://127.0.0.1:8089'
```

### 图片上传配置

图片上传使用阿里云OSS，配置在 `src/components/ImageUpload.vue` 中。

## 开发指南

### 添加新页面

1. 在 `src/pages/` 目录下创建新的Vue组件
2. 在 `src/router/index.js` 中添加路由配置
3. 在 `src/components/Layout.vue` 中添加导航菜单

### API调用

使用 `src/services/api.js` 中定义的方法进行API调用：

```javascript
import { productAPI } from '@/services/api.js'

// 获取商品列表
const products = await productAPI.getProducts()
```

## 部署说明

### 开发环境

确保后端服务运行在 `http://127.0.0.1:8089`

### 生产环境

1. 构建项目：`npm run build`
2. 将 `dist` 目录部署到Web服务器
3. 配置Nginx反向代理（参考 `nginx-config/nginx.conf`）

## 常见问题

### 跨域问题

开发环境下，Vue CLI会自动处理跨域问题。如果遇到跨域错误，请检查：

1. 后端CORS配置
2. 前端API地址配置

### 图片上传失败

检查阿里云OSS配置：

1. AccessKey和SecretKey是否正确
2. Bucket名称是否正确
3. 网络连接是否正常

## 更新日志

- v1.0.0: 初始版本，包含基础功能
- 商品管理模块完善
- 图片上传功能集成
- 数据类型转换修复

## 许可证

MIT License