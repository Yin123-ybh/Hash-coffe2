import React, { useState } from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined, CoffeeOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setLoading(true);
    
    try {
      // 登录请求
      const response = await fetch('http://127.0.0.1:8089/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: values.username,
          password: values.password,
        }),
      });

      const data = await response.json();
      console.log('登录响应:', data);

      if (data.code === 200) {
        // 保存token和用户信息
        localStorage.setItem('token', data.data.token);
        localStorage.setItem('userInfo', JSON.stringify(data.data.userInfo));
        
        console.log('保存token成功:', data.data.token);
        console.log('保存用户信息成功:', data.data.userInfo);
        
        message.success('登录成功！');
        
        // 延迟跳转，确保消息显示
        setTimeout(() => {
          console.log('准备跳转到dashboard');
          navigate('/dashboard');
        }, 1000);
      } else {
        message.error(data.message || '登录失败');
      }
    } catch (error) {
      console.error('登录错误:', error);
      message.error('网络错误，请检查后端服务是否启动');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-background">
        <div className="login-content">
          <Card className="login-card">
            <div className="login-header">
              <CoffeeOutlined className="login-icon" />
              <h1 className="login-title">智能咖啡管理系统</h1>
              <p className="login-subtitle">门店管理后台</p>
            </div>
            
            <Form
              name="login"
              onFinish={onFinish}
              autoComplete="off"
              size="large"
            >
              <Form.Item
                name="username"
                rules={[
                  { required: true, message: '请输入用户名!' },
                ]}
              >
                <Input
                  prefix={<UserOutlined />}
                  placeholder="用户名"
                />
              </Form.Item>

              <Form.Item
                name="password"
                rules={[
                  { required: true, message: '请输入密码!' },
                ]}
              >
                <Input.Password
                  prefix={<LockOutlined />}
                  placeholder="密码"
                />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-button"
                  loading={loading}
                  block
                >
                  登录
                </Button>
              </Form.Item>
            </Form>
            
            <div className="login-footer">
              <p className="demo-account">
                演示账号：admin / 123456
              </p>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Login;
