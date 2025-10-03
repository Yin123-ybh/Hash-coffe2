<template>
  <div class="ai-analysis">
    <div class="page-header">
      <h2>AI数据分析</h2>
      <p>智能分析您的业务数据，提供专业建议</p>
    </div>

    <!-- 分析类型选择 -->
    <div class="analysis-types">
      <div class="type-card" :class="{ active: selectedType === 'sales' }" @click="selectType('sales')">
        <div class="type-icon">📊</div>
        <div class="type-title">销售分析</div>
        <div class="type-desc">分析销售趋势和热门商品</div>
      </div>
      <div class="type-card" :class="{ active: selectedType === 'customer' }" @click="selectType('customer')">
        <div class="type-icon">👥</div>
        <div class="type-title">客户分析</div>
        <div class="type-desc">分析客户行为和偏好</div>
      </div>
      <div class="type-card" :class="{ active: selectedType === 'inventory' }" @click="selectType('inventory')">
        <div class="type-icon">📦</div>
        <div class="type-title">库存分析</div>
        <div class="type-desc">优化库存管理和采购建议</div>
      </div>
      <div class="type-card" :class="{ active: selectedType === 'custom' }" @click="selectType('custom')">
        <div class="type-icon">💡</div>
        <div class="type-title">自定义分析</div>
        <div class="type-desc">提出您的问题，获得专业建议</div>
      </div>
    </div>

    <!-- 问题输入区域 -->
    <div class="question-section">
      <div class="question-input">
        <textarea
          v-model="question"
          placeholder="请输入您想要分析的问题，例如：为什么最近销量下降？如何提高客户满意度？"
          class="question-textarea"
          rows="4"
        ></textarea>
        <div class="input-actions">
          <button 
            class="btn-primary"
            :disabled="!question.trim() || loading"
            @click="askQuestion"
          >
            {{ loading ? '分析中...' : '开始分析' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 分析结果 -->
    <div v-if="analysisResult" class="analysis-result">
      <div class="result-header">
        <h3>分析结果</h3>
        <div class="result-meta">
          <span class="analysis-type">{{ getTypeName(selectedType) }}</span>
          <span class="analysis-time">{{ formatTime(analysisResult.timestamp) }}</span>
        </div>
      </div>
      
      <div class="result-content">
        <div class="result-summary">
          <h4>核心洞察</h4>
          <p>{{ analysisResult.summary }}</p>
        </div>
        
        <div v-if="analysisResult.details" class="result-details">
          <h4>详细分析</h4>
          <div class="detail-item" v-for="(detail, index) in analysisResult.details" :key="index">
            <div class="detail-title">{{ detail.title }}</div>
            <div class="detail-content">{{ detail.content }}</div>
          </div>
        </div>
        
        <div v-if="analysisResult.recommendations" class="result-recommendations">
          <h4>建议措施</h4>
          <ul class="recommendation-list">
            <li v-for="(rec, index) in analysisResult.recommendations" :key="index">
              {{ rec }}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 历史分析记录 -->
    <div class="history-section">
      <h3>历史分析记录</h3>
      <div v-if="history.length === 0" class="empty-history">
        暂无历史分析记录
      </div>
      <div v-else class="history-list">
        <div v-for="item in history" :key="item.id" class="history-item" @click="loadHistoryItem(item)">
          <div class="history-question">{{ item.question }}</div>
          <div class="history-meta">
            <span class="history-type">{{ getTypeName(item.type) }}</span>
            <span class="history-time">{{ formatTime(item.timestamp) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'AiAnalysis',
  setup() {
    const loading = ref(false)
    const selectedType = ref('custom')
    const question = ref('')
    const analysisResult = ref(null)
    const history = ref([])

    onMounted(() => {
      loadHistory()
    })

    const selectType = (type) => {
      selectedType.value = type
      // 根据类型设置默认问题
      const defaultQuestions = {
        sales: '请分析最近一个月的销售趋势，哪些商品最受欢迎？',
        customer: '请分析客户购买行为，如何提高客户满意度？',
        inventory: '请分析库存状况，哪些商品需要补货？',
        custom: ''
      }
      question.value = defaultQuestions[type]
    }

    const askQuestion = async () => {
      if (!question.value.trim()) return

      loading.value = true
      try {
        // 模拟AI分析
        await new Promise(resolve => setTimeout(resolve, 2000))
        
        const result = generateAnalysisResult(selectedType.value, question.value)
        analysisResult.value = result
        
        // 保存到历史记录
        const historyItem = {
          id: Date.now(),
          type: selectedType.value,
          question: question.value,
          result: result,
          timestamp: new Date().toISOString()
        }
        history.value.unshift(historyItem)
        
        // 保存到本地存储
        localStorage.setItem('ai-analysis-history', JSON.stringify(history.value))
        
      } catch (error) {
        console.error('分析失败:', error)
        alert('分析失败，请重试')
      } finally {
        loading.value = false
      }
    }

    const generateAnalysisResult = (type, question) => {
      const results = {
        sales: {
          summary: '根据销售数据分析，美式咖啡和拿铁咖啡是最受欢迎的商品，占总销量的60%。建议增加这两款商品的库存。',
          details: [
            {
              title: '热销商品排行',
              content: '1. 美式咖啡 (45杯) 2. 拿铁咖啡 (38杯) 3. 卡布奇诺 (32杯)'
            },
            {
              title: '销售趋势',
              content: '最近一周销量呈上升趋势，特别是下午时段销量增长明显。'
            }
          ],
          recommendations: [
            '增加美式咖啡和拿铁咖啡的库存',
            '在下午时段推出促销活动',
            '考虑推出新的咖啡品种'
          ]
        },
        customer: {
          summary: '客户分析显示，大部分客户是回头客，平均消费金额为45元。建议通过会员制度提高客户忠诚度。',
          details: [
            {
              title: '客户构成',
              content: '新客户占比30%，回头客占比70%，客户忠诚度较高。'
            },
            {
              title: '消费习惯',
              content: '客户偏好在工作日购买咖啡，平均消费金额45元。'
            }
          ],
          recommendations: [
            '推出会员积分制度',
            '增加工作日促销活动',
            '提供个性化推荐服务'
          ]
        },
        inventory: {
          summary: '库存分析显示，卡布奇诺库存不足，需要及时补货。建议建立自动补货机制。',
          details: [
            {
              title: '库存状况',
              content: '卡布奇诺库存为0，美式咖啡库存充足(50杯)，拿铁咖啡库存中等(30杯)。'
            },
            {
              title: '补货建议',
              content: '建议立即补货卡布奇诺，预计需要50杯库存。'
            }
          ],
          recommendations: [
            '立即补货卡布奇诺',
            '建立库存预警机制',
            '优化采购计划'
          ]
        },
        custom: {
          summary: '根据您的问题，我分析了相关数据并提供了专业建议。',
          details: [
            {
              title: '问题分析',
              content: '基于当前业务数据，我为您提供了针对性的分析。'
            }
          ],
          recommendations: [
            '建议定期进行数据分析',
            '关注客户反馈',
            '持续优化业务流程'
          ]
        }
      }
      
      return {
        ...results[type],
        timestamp: new Date().toISOString()
      }
    }

    const getTypeName = (type) => {
      const typeNames = {
        sales: '销售分析',
        customer: '客户分析',
        inventory: '库存分析',
        custom: '自定义分析'
      }
      return typeNames[type] || '未知类型'
    }

    const formatTime = (timestamp) => {
      const date = new Date(timestamp)
      return date.toLocaleString()
    }

    const loadHistory = () => {
      try {
        const stored = localStorage.getItem('ai-analysis-history')
        if (stored) {
          history.value = JSON.parse(stored)
        }
      } catch (error) {
        console.error('加载历史记录失败:', error)
      }
    }

    const loadHistoryItem = (item) => {
      selectedType.value = item.type
      question.value = item.question
      analysisResult.value = item.result
    }

    return {
      loading,
      selectedType,
      question,
      analysisResult,
      history,
      selectType,
      askQuestion,
      getTypeName,
      formatTime,
      loadHistoryItem
    }
  }
}
</script>

<style scoped>
.ai-analysis {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.analysis-types {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 30px;
}

.type-card {
  background: white;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.type-card:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.type-card.active {
  border-color: #409eff;
  background: #f0f9ff;
}

.type-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.type-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.type-desc {
  font-size: 12px;
  color: #909399;
}

.question-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.question-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  min-height: 100px;
}

.question-textarea:focus {
  outline: none;
  border-color: #409eff;
}

.input-actions {
  margin-top: 16px;
  text-align: right;
}

.btn-primary {
  background: #409eff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover:not(:disabled) {
  background: #66b1ff;
}

.btn-primary:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

.analysis-result {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.result-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
}

.result-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

.analysis-type {
  background: #e6f7ff;
  color: #409eff;
  padding: 4px 8px;
  border-radius: 4px;
}

.result-content {
  line-height: 1.6;
}

.result-summary {
  margin-bottom: 20px;
}

.result-summary h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.result-summary p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.result-details {
  margin-bottom: 20px;
}

.result-details h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.detail-item {
  margin-bottom: 16px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.detail-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.detail-content {
  color: #606266;
  font-size: 14px;
}

.result-recommendations h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.recommendation-list {
  margin: 0;
  padding-left: 20px;
}

.recommendation-list li {
  color: #606266;
  font-size: 14px;
  margin-bottom: 8px;
}

.history-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.history-section h3 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 18px;
}

.empty-history {
  text-align: center;
  color: #909399;
  padding: 40px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.history-item:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.history-question {
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.history-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

.history-type {
  background: #e6f7ff;
  color: #409eff;
  padding: 2px 6px;
  border-radius: 4px;
}

@media (max-width: 768px) {
  .analysis-types {
    grid-template-columns: 1fr;
  }
  
  .result-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .result-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>