import React, { useState, useEffect } from 'react';
import { 
  Form, 
  Input, 
  Button, 
  Card, 
  Select, 
  InputNumber, 
  Switch, 
  message,
  Space,
  Divider,
  Row,
  Col
} from 'antd';
import { 
  SaveOutlined, 
  ArrowLeftOutlined 
} from '@ant-design/icons';
import { useNavigate, useParams } from 'react-router-dom';
import { productAPI } from '../services/api';
import ImageUpload from '../components/ImageUpload';

const { TextArea } = Input;
const { Option } = Select;

const ProductForm = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [isEdit, setIsEdit] = useState(false);
  const [productId, setProductId] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    loadCategories();
    if (id && id !== 'new') {
      setIsEdit(true);
      setProductId(id);
      loadProduct(id);
    }
  }, [id]);

  const loadCategories = async () => {
    try {
      const response = await productAPI.getCategories();
      setCategories(response.data || []);
    } catch (error) {
      console.error('加载分类失败:', error);
    }
  };

  const loadProduct = async (id) => {
    setLoading(true);
    try {
      const response = await productAPI.getProductDetail(id);
      const product = response.data;
      
      form.setFieldsValue({
        name: product.name,
        description: product.description,
        categoryId: product.categoryId,
        price: product.price,
        originalPrice: product.originalPrice,
        stock: product.stock,
        tags: product.tags,
        isHot: product.isHot,
        isRecommend: product.isRecommend,
        image: product.image
      });
    } catch (error) {
      message.error('加载商品信息失败');
      navigate('/products');
    } finally {
      setLoading(false);
    }
  };

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const productData = {
        ...values,
        image: values.image || '/images/default-product.png',
        isHot: values.isHot ? 1 : 0,
        isRecommend: values.isRecommend ? 1 : 0
      };

      if (isEdit) {
        await productAPI.updateProduct(productId, productData);
        message.success('商品更新成功');
      } else {
        await productAPI.createProduct(productData);
        message.success('商品创建成功');
      }
      
      navigate('/products');
    } catch (error) {
      message.error(isEdit ? '更新失败' : '创建失败');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    navigate('/products');
  };


  return (
    <div className="product-form">
      <Card>
        <div className="page-header">
          <div style={{ display: 'flex', alignItems: 'center', marginBottom: '16px' }}>
            <Button 
              icon={<ArrowLeftOutlined />} 
              onClick={handleBack}
              style={{ marginRight: '12px' }}
            >
              返回
            </Button>
            <h2>{isEdit ? '编辑商品' : '添加商品'}</h2>
          </div>
        </div>

        <Form
          form={form}
          layout="vertical"
          onFinish={onFinish}
          initialValues={{
            isHot: false,
            isRecommend: false,
            stock: 0,
            price: 0,
            originalPrice: 0
          }}
        >
          <Row gutter={24}>
            <Col xs={24} lg={16}>
              {/* 基本信息 */}
              <Card title="基本信息" style={{ marginBottom: '16px' }}>
                <Form.Item
                  name="name"
                  label="商品名称"
                  rules={[{ required: true, message: '请输入商品名称' }]}
                >
                  <Input placeholder="请输入商品名称" />
                </Form.Item>

                <Form.Item
                  name="description"
                  label="商品描述"
                  rules={[{ required: true, message: '请输入商品描述' }]}
                >
                  <TextArea 
                    rows={4} 
                    placeholder="请输入商品描述" 
                  />
                </Form.Item>

                <Form.Item
                  name="categoryId"
                  label="商品分类"
                  rules={[{ required: true, message: '请选择商品分类' }]}
                >
                  <Select placeholder="请选择商品分类">
                    {categories.map(category => (
                      <Option key={category.id} value={category.id}>
                        {category.name}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>

                <Form.Item
                  name="tags"
                  label="商品标签"
                >
                  <Input placeholder="请输入标签，用逗号分隔" />
                </Form.Item>
              </Card>

              {/* 价格和库存 */}
              <Card title="价格和库存" style={{ marginBottom: '16px' }}>
                <Row gutter={16}>
                  <Col span={12}>
                    <Form.Item
                      name="price"
                      label="售价"
                      rules={[{ required: true, message: '请输入售价' }]}
                    >
                      <InputNumber
                        style={{ width: '100%' }}
                        placeholder="请输入售价"
                        min={0}
                        precision={2}
                        addonBefore="¥"
                      />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      name="originalPrice"
                      label="原价"
                    >
                      <InputNumber
                        style={{ width: '100%' }}
                        placeholder="请输入原价"
                        min={0}
                        precision={2}
                        addonBefore="¥"
                      />
                    </Form.Item>
                  </Col>
                </Row>

                <Form.Item
                  name="stock"
                  label="库存数量"
                  rules={[{ required: true, message: '请输入库存数量' }]}
                >
                  <InputNumber
                    style={{ width: '100%' }}
                    placeholder="请输入库存数量"
                    min={0}
                  />
                </Form.Item>
              </Card>

              {/* 商品设置 */}
              <Card title="商品设置">
                <Row gutter={16}>
                  <Col span={12}>
                    <Form.Item
                      name="isHot"
                      label="是否热销"
                      valuePropName="checked"
                    >
                      <Switch />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      name="isRecommend"
                      label="是否推荐"
                      valuePropName="checked"
                    >
                      <Switch />
                    </Form.Item>
                  </Col>
                </Row>
              </Card>
            </Col>

            <Col xs={24} lg={8}>
              {/* 商品图片 */}
              <Card title="商品图片">
                <Form.Item
                  name="image"
                  label="商品主图"
                >
                  <ImageUpload 
                    value={form.getFieldValue('image') || ''}
                    onChange={(value) => form.setFieldsValue({ image: value })}
                  />
                </Form.Item>
              </Card>
            </Col>
          </Row>

          <Divider />

          {/* 操作按钮 */}
          <div style={{ textAlign: 'center' }}>
            <Space>
              <Button onClick={handleBack}>
                取消
              </Button>
              <Button 
                type="primary" 
                htmlType="submit" 
                loading={loading}
                icon={<SaveOutlined />}
              >
                {isEdit ? '更新商品' : '创建商品'}
              </Button>
            </Space>
          </div>
        </Form>
      </Card>
    </div>
  );
};

export default ProductForm;
