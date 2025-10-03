import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Table, Tag, Progress, message } from 'antd';
import { 
  ShoppingOutlined, 
  UserOutlined, 
  DollarOutlined, 
  TrophyOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined
} from '@ant-design/icons';
import './Dashboard.css';

const Dashboard = () => {
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState({
    totalOrders: 0,
    totalUsers: 0,
    totalRevenue: 0,
    totalProducts: 0
  });
  const [recentOrders, setRecentOrders] = useState([]);
  const [topProducts, setTopProducts] = useState([]);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      // 模拟数据加载
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setStats({
        totalOrders: 156,
        totalUsers: 89,
        totalRevenue: 12580.50,
        totalProducts: 12
      });

      setRecentOrders([
        {
          id: 1,
          orderNo: '20250929001',
          customer: '张三',
          amount: 58.00,
          status: '已完成',
          createTime: '2025-09-29 14:30:00'
        },
        {
          id: 2,
          orderNo: '20250929002',
          customer: '李四',
          amount: 32.50,
          status: '制作中',
          createTime: '2025-09-29 14:20:00'
        },
        {
          id: 3,
          orderNo: '20250929003',
          customer: '王五',
          amount: 75.00,
          status: '待支付',
          createTime: '2025-09-29 14:15:00'
        }
      ]);

      setTopProducts([
        { name: '美式咖啡', sales: 45, revenue: 810.00 },
        { name: '拿铁咖啡', sales: 38, revenue: 836.00 },
        { name: '卡布奇诺', sales: 32, revenue: 1040.00 },
        { name: '摩卡咖啡', sales: 28, revenue: 1050.00 }
      ]);
    } catch (error) {
      message.error('加载数据失败');
    } finally {
      setLoading(false);
    }
  };

  const orderColumns = [
    {
      title: '订单号',
      dataIndex: 'orderNo',
      key: 'orderNo',
    },
    {
      title: '客户',
      dataIndex: 'customer',
      key: 'customer',
    },
    {
      title: '金额',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount) => `¥${amount}`,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusConfig = {
          '已完成': { color: 'green' },
          '制作中': { color: 'blue' },
          '待支付': { color: 'orange' },
          '已取消': { color: 'red' }
        };
        return <Tag color={statusConfig[status]?.color}>{status}</Tag>;
      },
    },
    {
      title: '时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
  ];

  const productColumns = [
    {
      title: '商品名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '销量',
      dataIndex: 'sales',
      key: 'sales',
      render: (sales) => (
        <div>
          <div>{sales}杯</div>
          <Progress 
            percent={(sales / 50) * 100} 
            size="small" 
            showInfo={false}
            strokeColor="#52c41a"
          />
        </div>
      ),
    },
    {
      title: '收入',
      dataIndex: 'revenue',
      key: 'revenue',
      render: (revenue) => `¥${revenue}`,
    },
  ];

  return (
    <div className="dashboard">
      <div className="page-header">
        <h2>仪表盘</h2>
        <p>欢迎使用智能咖啡管理系统</p>
      </div>

      {/* 统计卡片 */}
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总订单数"
              value={stats.totalOrders}
              prefix={<ShoppingOutlined />}
              suffix="单"
              valueStyle={{ color: '#3f8600' }}
            />
            <div style={{ marginTop: '8px', fontSize: '12px', color: '#666' }}>
              <ArrowUpOutlined style={{ color: '#3f8600' }} /> 12% 较昨日
            </div>
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总用户数"
              value={stats.totalUsers}
              prefix={<UserOutlined />}
              suffix="人"
              valueStyle={{ color: '#1890ff' }}
            />
            <div style={{ marginTop: '8px', fontSize: '12px', color: '#666' }}>
              <ArrowUpOutlined style={{ color: '#3f8600' }} /> 8% 较昨日
            </div>
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总收入"
              value={stats.totalRevenue}
              prefix={<DollarOutlined />}
              precision={2}
              valueStyle={{ color: '#cf1322' }}
            />
            <div style={{ marginTop: '8px', fontSize: '12px', color: '#666' }}>
              <ArrowUpOutlined style={{ color: '#3f8600' }} /> 15% 较昨日
            </div>
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="商品数量"
              value={stats.totalProducts}
              prefix={<TrophyOutlined />}
              suffix="种"
              valueStyle={{ color: '#722ed1' }}
            />
            <div style={{ marginTop: '8px', fontSize: '12px', color: '#666' }}>
              <ArrowDownOutlined style={{ color: '#cf1322' }} /> 2% 较昨日
            </div>
          </Card>
        </Col>
      </Row>

      {/* 内容区域 */}
      <Row gutter={[16, 16]}>
        <Col xs={24} lg={12}>
          <Card title="最近订单" loading={loading}>
            <Table
              columns={orderColumns}
              dataSource={recentOrders}
              rowKey="id"
              pagination={false}
              size="small"
            />
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="热销商品" loading={loading}>
            <Table
              columns={productColumns}
              dataSource={topProducts}
              rowKey="name"
              pagination={false}
              size="small"
            />
          </Card>
        </Col>
      </Row>

      {/* 快速操作 */}
      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col xs={24} lg={8}>
          <Card title="今日概览" size="small">
            <div style={{ textAlign: 'center', padding: '20px 0' }}>
              <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#1890ff' }}>
                今日订单: 23单
              </div>
              <div style={{ fontSize: '16px', color: '#666', marginTop: '8px' }}>
                今日收入: ¥1,245.50
              </div>
            </div>
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="系统状态" size="small">
            <div style={{ padding: '20px 0' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <span>系统运行</span>
                <Tag color="green">正常</Tag>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <span>数据库</span>
                <Tag color="green">正常</Tag>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>AI服务</span>
                <Tag color="green">正常</Tag>
              </div>
            </div>
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="快速操作" size="small">
            <div style={{ padding: '20px 0' }}>
              <div style={{ marginBottom: '12px' }}>
                <a href="/products/new">添加新商品</a>
              </div>
              <div style={{ marginBottom: '12px' }}>
                <a href="/orders">查看所有订单</a>
              </div>
              <div>
                <a href="/ai-analysis">AI数据分析</a>
              </div>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
