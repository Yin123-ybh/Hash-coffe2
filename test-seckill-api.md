# 秒杀活动API测试指南

## 测试环境
- 后端服务：http://localhost:8084
- 前端代理：http://localhost:8083
- 微信小程序：通过微信开发者工具

## API接口测试

### 1. 获取当前进行中的秒杀活动
```bash
curl -X GET "http://localhost:8083/user/seckill/activity/current"
```

### 2. 获取秒杀活动详情
```bash
curl -X GET "http://localhost:8083/user/seckill/activity/detail/1"
```

### 3. 参与秒杀活动
```bash
curl -X POST "http://localhost:8083/user/seckill/activity/participate/1?quantity=1"
```

## 管理端API测试

### 1. 创建秒杀活动
```bash
curl -X POST "http://localhost:8083/admin/seckill/activity" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "美式咖啡秒杀",
    "description": "限时秒杀，数量有限",
    "dishId": 1,
    "seckillPrice": 10.00,
    "originalPrice": 15.00,
    "stock": 100,
    "perUserLimit": 2,
    "startTime": "2024-01-01T10:00:00",
    "endTime": "2024-01-01T18:00:00",
    "status": 1
  }'
```

### 2. 获取活动列表
```bash
curl -X GET "http://localhost:8083/admin/seckill/activity/page?page=1&pageSize=10"
```

## 数据库测试数据

```sql
-- 插入测试秒杀活动
INSERT INTO seckill_activity (
    name, description, dish_id, seckill_price, original_price, 
    stock, sold_count, per_user_limit, start_time, end_time, status
) VALUES (
    '美式咖啡秒杀', '限时秒杀，数量有限', 1, 10.00, 15.00, 
    100, 0, 2, '2024-01-01 10:00:00', '2024-01-01 18:00:00', 1
);
```

## 微信小程序测试步骤

1. 启动后端服务（端口8084）
2. 启动nginx代理（端口8083）
3. 在微信开发者工具中打开小程序项目
4. 点击"🔥 秒杀活动"按钮
5. 查看秒杀活动列表
6. 点击活动进入详情页
7. 测试参与秒杀功能

## 常见问题排查

### 1. 前端错误：_vm.goToSeckill is not a function
- 已修复：将方法定义移到Vue组件的methods中

### 2. API请求失败
- 检查nginx配置是否正确
- 检查后端服务是否启动
- 检查API路径是否正确

### 3. 数据库连接问题
- 检查数据库配置
- 确保相关表已创建
- 检查数据是否正确插入
