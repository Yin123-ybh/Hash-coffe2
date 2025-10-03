import React, { useState } from 'react';
import { Card, Button, Input, message, Spin } from 'antd';
import { RobotOutlined, SendOutlined } from '@ant-design/icons';
import { aiAPI } from '../services/api';

const { TextArea } = Input;

const AiAnalysis = () => {
  const [loading, setLoading] = useState(false);
  const [question, setQuestion] = useState('');
  const [analysis, setAnalysis] = useState('');

  const handleAnalyze = async () => {
    if (!question.trim()) {
      message.warning('请输入分析问题');
      return;
    }

    setLoading(true);
    try {
      // 调用AI接口
      const response = await aiAPI.chat({
        message: question,
        sessionId: 'analysis_' + Date.now()
      });
      
      if (response.data?.response) {
        setAnalysis(response.data.response);
      } else {
        // 如果API调用失败，使用模拟数据
        const mockAnalysis = generateMockAnalysis(question);
        setAnalysis(mockAnalysis);
        message.warning('使用模拟分析，请检查后端服务');
      }
    } catch (error) {
      console.error('AI分析失败:', error);
      // 如果API调用失败，使用模拟数据
      const mockAnalysis = generateMockAnalysis(question);
      setAnalysis(mockAnalysis);
      message.warning('使用模拟分析，请检查后端服务');
    } finally {
      setLoading(false);
    }
  };

  const generateMockAnalysis = (question) => {
    const analyses = {
      '销售情况': '根据数据分析，本周销售额比上周增长了15%，主要增长来源于美式咖啡和拿铁咖啡。建议继续推广这两款产品。',
      '用户行为': '用户活跃度分析显示，下午2-4点是用户下单高峰期，建议在这个时间段增加库存和人员配置。',
      '商品推荐': '基于用户购买历史，推荐以下商品组合：美式咖啡+牛角包，拿铁咖啡+蛋糕，这些组合的转化率较高。',
      '库存管理': '当前库存预警：卡布奇诺库存不足，建议及时补货。摩卡咖啡库存过多，建议进行促销活动。'
    };

    for (const [key, value] of Object.entries(analyses)) {
      if (question.includes(key)) {
        return value;
      }
    }

    return '基于您的数据分析需求，我为您提供以下建议：\n\n1. 销售趋势：近期销售额呈上升趋势\n2. 用户偏好：美式咖啡和拿铁最受欢迎\n3. 优化建议：建议在高峰期增加人员配置\n4. 营销策略：可以推出限时优惠活动';
  };

  const quickQuestions = [
    '本周销售情况如何？',
    '用户购买行为分析',
    '商品推荐策略',
    '库存管理建议'
  ];

  return (
    <div className="ai-analysis">
      <Card>
        <div className="page-header">
          <h2>AI数据分析</h2>
          <p>智能分析您的业务数据，提供专业建议</p>
        </div>

        <div className="analysis-content">
          <div className="input-section">
            <TextArea
              placeholder="请输入您想要分析的问题，例如：本周销售情况如何？用户购买行为分析等..."
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              rows={4}
              style={{ marginBottom: '16px' }}
            />
            
            <div className="quick-questions">
              <p>快速问题：</p>
              <div className="question-tags">
                {quickQuestions.map((q, index) => (
                  <Button
                    key={index}
                    size="small"
                    onClick={() => setQuestion(q)}
                    style={{ margin: '4px' }}
                  >
                    {q}
                  </Button>
                ))}
              </div>
            </div>

            <Button
              type="primary"
              icon={<SendOutlined />}
              onClick={handleAnalyze}
              loading={loading}
              size="large"
            >
              开始分析
            </Button>
          </div>

          {loading && (
            <div className="loading-section">
              <Spin size="large" />
              <p>AI正在分析中，请稍候...</p>
            </div>
          )}

          {analysis && !loading && (
            <div className="result-section">
              <h3>分析结果</h3>
              <Card>
                <div style={{ whiteSpace: 'pre-line' }}>
                  {analysis}
                </div>
              </Card>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default AiAnalysis;
