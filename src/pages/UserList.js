import React, { useState, useEffect } from 'react';
import { Table, Card, Button, Space, Tag, Avatar, message } from 'antd';
import { UserOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { userAPI } from '../services/api';

const UserList = () => {
  const [loading, setLoading] = useState(false);
  const [users, setUsers] = useState([]);

  // 模拟用户数据
  const mockUsers = [
    {
      id: 1,
      username: 'admin',
      nickname: '管理员',
      phone: '13800138000',
      email: 'admin@coffee.com',
      status: '正常',
      createTime: '2025-01-01 10:00:00',
      lastLogin: '2025-09-29 14:30:00'
    },
    {
      id: 2,
      username: 'user001',
      nickname: '张三',
      phone: '13800138001',
      email: 'zhangsan@coffee.com',
      status: '正常',
      createTime: '2025-01-15 09:30:00',
      lastLogin: '2025-09-29 13:45:00'
    },
    {
      id: 3,
      username: 'user002',
      nickname: '李四',
      phone: '13800138002',
      email: 'lisi@coffee.com',
      status: '禁用',
      createTime: '2025-02-01 14:20:00',
      lastLogin: '2025-09-28 16:15:00'
    }
  ];

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    setLoading(true);
    try {
      // 由于后端没有用户列表接口，暂时使用模拟数据
      // 实际项目中需要后端提供用户管理接口
      await new Promise(resolve => setTimeout(resolve, 1000));
      setUsers(mockUsers);
    } catch (error) {
      message.error('加载用户失败');
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: '用户信息',
      key: 'user',
      render: (_, record) => (
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <Avatar icon={<UserOutlined />} />
          <div style={{ marginLeft: '12px' }}>
            <div style={{ fontWeight: 'bold' }}>{record.nickname}</div>
            <div style={{ color: '#666', fontSize: '12px' }}>@{record.username}</div>
          </div>
        </div>
      ),
    },
    {
      title: '联系方式',
      key: 'contact',
      render: (_, record) => (
        <div>
          <div>{record.phone}</div>
          <div style={{ color: '#666', fontSize: '12px' }}>{record.email}</div>
        </div>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={status === '正常' ? 'green' : 'red'}>
          {status}
        </Tag>
      ),
    },
    {
      title: '注册时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '最后登录',
      dataIndex: 'lastLogin',
      key: 'lastLogin',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="small">
          <Button size="small" icon={<EditOutlined />}>
            编辑
          </Button>
          <Button 
            size="small" 
            danger 
            icon={<DeleteOutlined />}
            disabled={record.username === 'admin'}
          >
            删除
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div className="user-list">
      <Card>
        <div className="page-header">
          <h2>用户管理</h2>
          <div className="header-actions">
            <Button type="primary">
              添加用户
            </Button>
          </div>
        </div>

        <Table
          columns={columns}
          dataSource={users}
          rowKey="id"
          loading={loading}
          pagination={{
            total: users.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => 
              `第 ${range[0]}-${range[1]} 条/共 ${total} 条`,
          }}
        />
      </Card>
    </div>
  );
};

export default UserList;
