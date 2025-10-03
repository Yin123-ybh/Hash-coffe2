import React, { useState, useEffect } from 'react';
import { 
  Table, 
  Card, 
  Button, 
  Space, 
  Tag, 
  Input, 
  Select, 
  message, 
  Popconfirm,
  Image,
  Tooltip
} from 'antd';
import { 
  PlusOutlined, 
  EditOutlined, 
  DeleteOutlined, 
  SearchOutlined,
  EyeOutlined,
  ReloadOutlined
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { productAPI } from '../services/api';
import './ProductList.css';

const { Search } = Input;
const { Option } = Select;

const ProductList = () => {
  const [loading, setLoading] = useState(false);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0
  });
  const [filters, setFilters] = useState({
    keyword: '',
    categoryId: null,
    status: null
  });
  const navigate = useNavigate();

  useEffect(() => {
    loadCategories();
    loadProducts();
  }, [pagination.current, pagination.pageSize, filters]);

  const loadCategories = async () => {
    try {
      const response = await productAPI.getCategories();
      setCategories(response.data || []);
    } catch (error) {
      console.error('加载分类失败:', error);
    }
  };

  const loadProducts = async () => {
    setLoading(true);
    try {
      const params = {
        page: pagination.current,
        size: pagination.pageSize,
        keyword: filters.keyword || undefined,
        categoryId: filters.categoryId || undefined
      };

      const response = await productAPI.getProducts(params);
      const data = response.data || {};
      
      // 转换后端返回的整数为前端需要的布尔值
      const products = (data.records || []).map(item => ({
        ...item,
        isHot: item.isHot === 1,
        isRecommend: item.isRecommend === 1
      }));
      setProducts(products);
      setPagination(prev => ({
        ...prev,
        total: data.total || 0
      }));
    } catch (error) {
      console.error('加载商品失败:', error);
      message.error('加载商品失败');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (value) => {
    setFilters(prev => ({ ...prev, keyword: value }));
    setPagination(prev => ({ ...prev, current: 1 }));
  };

  const handleCategoryChange = (value) => {
    setFilters(prev => ({ ...prev, categoryId: value }));
    setPagination(prev => ({ ...prev, current: 1 }));
  };

  const handleStatusChange = (value) => {
    setFilters(prev => ({ ...prev, status: value }));
    setPagination(prev => ({ ...prev, current: 1 }));
  };

  const handleTableChange = (pagination) => {
    setPagination(pagination);
  };

  const handleDelete = async (id) => {
    try {
      await productAPI.deleteProduct(id);
      message.success('删除成功');
      loadProducts();
    } catch (error) {
      message.error('删除失败');
    }
  };

  const columns = [
    {
      title: '商品信息',
      key: 'product',
      width: 200,
      render: (_, record) => (
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <Image
            width={60}
            height={60}
            src={record.image || '/images/default-product.png'}
            fallback="/images/default-product.png"
            style={{ borderRadius: '8px', objectFit: 'cover' }}
          />
          <div style={{ marginLeft: '12px' }}>
            <div style={{ fontWeight: 'bold', marginBottom: '4px' }}>
              {record.name}
            </div>
            <div style={{ color: '#666', fontSize: '12px' }}>
              ID: {record.id}
            </div>
          </div>
        </div>
      ),
    },
    {
      title: '分类',
      dataIndex: 'categoryId',
      key: 'categoryId',
      width: 100,
      render: (categoryId) => {
        const category = categories.find(c => c.id === categoryId);
        return (
          <Tag color="blue">
            {category?.name || '未分类'}
          </Tag>
        );
      },
    },
    {
      title: '价格',
      key: 'price',
      width: 120,
      render: (_, record) => (
        <div>
          <div style={{ fontWeight: 'bold', color: '#f5222d' }}>
            ¥{record.price}
          </div>
          {record.originalPrice && record.originalPrice > record.price && (
            <div style={{ 
              fontSize: '12px', 
              color: '#999', 
              textDecoration: 'line-through' 
            }}>
              ¥{record.originalPrice}
            </div>
          )}
        </div>
      ),
    },
    {
      title: '库存',
      dataIndex: 'stock',
      key: 'stock',
      width: 80,
      render: (stock) => (
        <Tag color={stock > 10 ? 'green' : stock > 0 ? 'orange' : 'red'}>
          {stock}
        </Tag>
      ),
    },
    {
      title: '销量',
      dataIndex: 'sales',
      key: 'sales',
      width: 80,
      render: (sales) => (
        <Tag color="blue">{sales || 0}</Tag>
      ),
    },
    {
      title: '标签',
      key: 'tags',
      width: 120,
      render: (_, record) => (
        <div>
          {record.isHot && <Tag color="red" size="small">热销</Tag>}
          {record.isRecommend && <Tag color="green" size="small">推荐</Tag>}
        </div>
      ),
    },
    {
      title: '状态',
      key: 'status',
      width: 80,
      render: (_, record) => (
        <Tag color={record.stock > 0 ? 'green' : 'red'}>
          {record.stock > 0 ? '上架' : '缺货'}
        </Tag>
      ),
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space size="small">
          <Tooltip title="查看详情">
            <Button 
              size="small" 
              icon={<EyeOutlined />}
              onClick={() => navigate(`/products/${record.id}`)}
            />
          </Tooltip>
          <Tooltip title="编辑">
            <Button 
              size="small" 
              icon={<EditOutlined />}
              onClick={() => navigate(`/products/edit/${record.id}`)}
            />
          </Tooltip>
          <Tooltip title="删除">
            <Popconfirm
              title="确定要删除这个商品吗？"
              onConfirm={() => handleDelete(record.id)}
              okText="确定"
              cancelText="取消"
            >
              <Button 
                size="small" 
                danger 
                icon={<DeleteOutlined />}
              />
            </Popconfirm>
          </Tooltip>
        </Space>
      ),
    },
  ];

  return (
    <div className="product-list">
      <Card>
        <div className="page-header">
          <h2>商品管理</h2>
          <div className="header-actions">
            <Button 
              type="primary" 
              icon={<PlusOutlined />}
              onClick={() => navigate('/products/new')}
            >
              添加商品
            </Button>
          </div>
        </div>

        {/* 搜索和筛选 */}
        <div className="filter-section" style={{ marginBottom: '16px' }}>
          <Space wrap>
            <Search
              placeholder="搜索商品名称"
              style={{ width: 200 }}
              onSearch={handleSearch}
              enterButton={<SearchOutlined />}
            />
            <Select
              placeholder="选择分类"
              style={{ width: 120 }}
              allowClear
              onChange={handleCategoryChange}
            >
              {categories.map(category => (
                <Option key={category.id} value={category.id}>
                  {category.name}
                </Option>
              ))}
            </Select>
            <Select
              placeholder="商品状态"
              style={{ width: 120 }}
              allowClear
              onChange={handleStatusChange}
            >
              <Option value="1">上架</Option>
              <Option value="0">下架</Option>
            </Select>
            <Button 
              icon={<ReloadOutlined />} 
              onClick={loadProducts}
            >
              刷新
            </Button>
          </Space>
        </div>

        <Table
          columns={columns}
          dataSource={products}
          rowKey="id"
          loading={loading}
          pagination={{
            ...pagination,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => 
              `第 ${range[0]}-${range[1]} 条/共 ${total} 条`,
          }}
          onChange={handleTableChange}
          scroll={{ x: 1000 }}
        />
      </Card>
    </div>
  );
};

export default ProductList;
