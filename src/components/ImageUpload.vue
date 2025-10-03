<template>
  <div class="image-upload">
    <div v-if="!imageUrl" class="upload-area" @click="triggerUpload">
      <div class="upload-content">
        <div class="upload-icon">📷</div>
        <p>点击上传图片</p>
        <p class="upload-tip">支持 JPG、PNG 格式，大小不超过 2MB</p>
      </div>
    </div>
    
    <div v-else class="image-preview">
      <img :src="imageUrl" alt="预览图片" />
      <div class="image-actions">
        <button @click="triggerUpload" class="btn-change">更换</button>
        <button @click="removeImage" class="btn-remove">删除</button>
      </div>
    </div>
    
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      @change="handleFileChange"
      style="display: none"
    />
    
    <!-- 上传进度 -->
    <div v-if="uploading" class="upload-progress">
      <div class="progress-bar">
        <div class="progress-fill"></div>
      </div>
      <p>上传中...</p>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'ImageUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    maxSize: {
      type: Number,
      default: 2 * 1024 * 1024 // 2MB
    }
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const fileInput = ref(null)
    const imageUrl = ref(props.value)
    const uploading = ref(false)

    // 监听外部值变化
    watch(() => props.value, (newValue) => {
      imageUrl.value = newValue
    })

    const triggerUpload = (event) => {
      if (uploading.value) return
      event.preventDefault() // 防止触发表单提交
      event.stopPropagation() // 阻止事件冒泡
      fileInput.value.click()
    }

    const handleFileChange = async (event) => {
      const file = event.target.files[0]
      if (!file) return

      // 文件大小检查
      if (file.size > props.maxSize) {
        alert(`文件大小不能超过 ${props.maxSize / 1024 / 1024}MB`)
        return
      }

      // 文件类型检查
      if (!file.type.startsWith('image/')) {
        alert('请选择图片文件')
        return
      }

      uploading.value = true
      
      try {
        const formData = new FormData()
        formData.append('file', file)
        
        const token = localStorage.getItem('token')
        const response = await fetch('http://127.0.0.1:8089/admin/common/upload', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`
          },
          body: formData
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.code === 200) {
            imageUrl.value = result.data
            emit('update:value', result.data)
          } else {
            throw new Error(result.msg || '上传失败')
          }
        } else {
          throw new Error('上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        alert('上传失败: ' + error.message)
      } finally {
        uploading.value = false
        // 清空input，允许重复选择同一文件
        event.target.value = ''
      }
    }

    const removeImage = () => {
      imageUrl.value = ''
      emit('update:value', '')
    }

    return {
      fileInput,
      imageUrl,
      uploading,
      triggerUpload,
      handleFileChange,
      removeImage
    }
  }
}
</script>

<style scoped>
.image-upload {
  width: 200px;
  position: relative;
}

.upload-area {
  width: 200px;
  height: 200px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  background: #fafafa;
}

.upload-area:hover {
  border-color: #1890ff;
  background: #f0f8ff;
}

.upload-content {
  text-align: center;
}

.upload-icon {
  font-size: 48px;
  color: #d9d9d9;
  margin-bottom: 8px;
}

.upload-content p {
  margin: 4px 0;
  color: #666;
  font-size: 14px;
}

.upload-tip {
  font-size: 12px !important;
  color: #999 !important;
}

.image-preview {
  position: relative;
  width: 200px;
  height: 200px;
  border-radius: 6px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .image-actions {
  opacity: 1;
}

.btn-change, .btn-remove {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.btn-change {
  background: #1890ff;
  color: white;
}

.btn-change:hover {
  background: #40a9ff;
}

.btn-remove {
  background: #ff4d4f;
  color: white;
}

.btn-remove:hover {
  background: #ff7875;
}

.upload-progress {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
}

.progress-bar {
  width: 80%;
  height: 4px;
  background: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background: #1890ff;
  border-radius: 2px;
  animation: progress 2s infinite;
}

@keyframes progress {
  0% { width: 0%; }
  50% { width: 70%; }
  100% { width: 100%; }
}

.upload-progress p {
  margin: 0;
  color: #666;
  font-size: 14px;
}
</style>
