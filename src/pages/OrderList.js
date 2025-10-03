import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, Button, Space, Input, Select, DatePicker, message } from 'antd';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons';
import { orderAPI } from '../services/api';

const { Search } = Input;
const { Option } = Select;
const { RangePicker } = DatePicker;

const OrderList = () => {
  const [loading, setLoading] = useState(false);
  const [orders, setOrders] = useState([]);

  // 模拟订单数据
  const mockOrders = [
    {
      id: 1,
      orderNo: '20250929001',
      customer: '张三',
      phone: '13800138000',
      amount: 58.00,
      status: '已完成',
      createTime: '2025-09-29 14:30:00',
      items: [
        { name: '美式咖啡', quantity: 1, price: 25.00 },
        { name: '拿铁咖啡', quantity: 1, price: 33.00 }
      ]
    },
    {
      id: 2,
      orderNo: '20250929002',
      customer: '李四',
      phone: '13800138001',
      amount: 32.50,
      status: '待支付',
      createTime: '2025-09-29 14:20:00',
      items: [
        { name: '卡布奇诺', quantity: 1, price: 32.50 }
      ]
    },
    {
      id: 3,
      orderNo: '20250929003',
      customer: '王五',
      phone: '13800138002',
      amount: 75.00,
      status: '制作中',
      createTime: '2025-09-29 14:15:00',
      items: [
        { name: '摩卡咖啡', quantity: 2, price: 75.00 }
      ]
    },
    {
      id: 4,
      orderNo: '20250929004',
      customer: '赵六',
      phone: '13800138003',
      amount: 45.00,
      status: '已完成',
      createTime: '2025-09-29 13:45:00',
      items: [
        { name: '焦糖玛奇朵', quantity: 1, price: 45.00 }
      ]
    }
  ];

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    setLoading(true);
    try {
      const response = await orderAPI.getOrders({
        page: 1,
        size: 20
      });
      setOrders(response.data?.list || []);
    } catch (error) {
      console.error('加载订单失败:', error);
      // 如果API调用失败，使用模拟数据
      setOrders(mockOrders);
      message.warning('使用模拟数据，请检查后端服务');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = (orderId, newStatus) => {
    setOrders(prevOrders => 
      prevOrders.map(order => 
        order.id === orderId ? { ...order, status: newStatus } : order
      )
    );
    message.success('订单状态更新成功');
  };

  const columns = [
    {
      title: '订单号',
      dataIndex: 'orderNo',
      key: 'orderNo',
      width: 120,
    },
    {
      title: '客户信息',
      key: 'customer',
      render: (_, record) => (
        <div>
          <div>{record.customer}</div>
          <div style={{ color: '#666', fontSize: '12px' }}>{record.phone}</div>
        </div>
      ),
    },
    {
      title: '商品',
      key: 'items',
      render: (_, record) => (
        <div>
          {record.items.map((item, index) => (
            <div key={index} style={{ fontSize: '12px' }}>
              {item.name} x{item.quantity}
            </div>
          ))}
        </div>
      ),
    },
    {
      title: '金额',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount) => `¥${amount}`,
      sorter: (a, b) => a.amount - b.amount,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status, record) => {
        const statusConfig = {
          '已完成': { color: 'green', text: '已完成' },
          '制作中': { color: 'blue', text: '制作中' },
          '待支付': { color: 'orange', text: '待支付' },
          '已取消': { color: 'red', text: '已取消' }
        };
        const config = statusConfig[status] || { color: 'default', text: status };
        return <Tag color={config.color}>{config.text}</Tag>;
      },
      filters: [
        { text: '已完成', value: '已完成' },
        { text: '制作中', value: '制作中' },
        { text: '待支付', value: '待支付' },
        { text: '已取消', value: '已取消' },
      ],
      onFilter: (value, record) => record.status === value,
    },
    {
      title: '下单时间',
      dataIndex: 'createTime',
      key: 'createTime',
      sorter: (a, b) => new Date(a.createTime) - new Date(b.createTime),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="small">
          {record.status === '待支付' && (
            <Button 
              size="small" 
              type="primary"
              onClick={() => handleStatusChange(record.id, '制作中')}
            >
              开始制作
            </Button>
          )}
          {record.status === '制作中' && (
            <Button 
              size="small" 
              type="primary"
              onClick={() => handleStatusChange(record.id, '已完成')}
            >
              完成制作
            </Button>
          )}
          <Button size="small">查看详情</Button>
        </Space>
      ),
    },
  ];

  return (
    <div className="order-list">
      <Card>
        <div className="page-header">
          <h2>订单管理</h2>
          <div className="header-actions">
            <Space>
              <Search
                placeholder="搜索订单号或客户"
                style={{ width: 200 }}
                onSearch={(value) => console.log('搜索:', value)}
              />
              <Select placeholder="状态筛选" style={{ width: 120 }} allowClear>
                <Option value="已完成">已完成</Option>
                <Option value="制作中">制作中</Option>
                <Option value="待支付">待支付</Option>
                <Option value="已取消">已取消</Option>
              </Select>
              <RangePicker placeholder={['开始日期', '结束日期']} />
              <Button icon={<ReloadOutlined />} onClick={loadOrders}>
                刷新
              </Button>
            </Space>
          </div>
        </div>

        <Table
          columns={columns}
          dataSource={orders}
          rowKey="id"
          loading={loading}
          pagination={{
            total: orders.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => 
              `第 ${range[0]}-${range[1]} 条/共 ${total} 条`,
          }}
          scroll={{ x: 1000 }}
        />
      </Card>
    </div>
  );
};

export default OrderList;
