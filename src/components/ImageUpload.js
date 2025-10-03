import React, { useState, useRef } from 'react';
import './ImageUpload.css';

const ImageUpload = ({ value, onChange, maxSize = 2 * 1024 * 1024 }) => {
  const [uploading, setUploading] = useState(false);
  const fileInputRef = useRef(null);

  const triggerUpload = (event) => {
    if (uploading) return;
    event.preventDefault(); // 防止触发表单提交
    event.stopPropagation(); // 阻止事件冒泡
    fileInputRef.current.click();
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // 文件大小检查
    if (file.size > maxSize) {
      alert(`文件大小不能超过 ${maxSize / 1024 / 1024}MB`);
      return;
    }

    // 文件类型检查
    if (!file.type.startsWith('image/')) {
      alert('请选择图片文件');
      return;
    }

    setUploading(true);

    try {
      const formData = new FormData();
      formData.append('file', file);

      const token = localStorage.getItem('token');
      const response = await fetch('http://127.0.0.1:8089/admin/common/upload', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: formData
      });

      if (response.ok) {
        const result = await response.json();
        if (result.code === 200) {
          onChange(result.data);
        } else {
          throw new Error(result.msg || '上传失败');
        }
      } else {
        throw new Error('上传失败');
      }
    } catch (error) {
      console.error('上传失败:', error);
      alert('上传失败: ' + error.message);
    } finally {
      setUploading(false);
      event.target.value = '';
    }
  };

  const removeImage = () => {
    onChange('');
  };

  return (
    <div className="image-upload">
      {!value ? (
        <div className="upload-area" onClick={triggerUpload}>
          <div className="upload-content">
            <div className="upload-icon">📷</div>
            <p>点击上传图片</p>
            <p className="upload-tip">支持 JPG、PNG 格式，大小不超过 2MB</p>
          </div>
        </div>
      ) : (
        <div className="image-preview">
          <img src={value} alt="预览图片" />
          <div className="image-actions">
            <button onClick={triggerUpload} className="btn-change">
              更换
            </button>
            <button onClick={removeImage} className="btn-remove">
              删除
            </button>
          </div>
        </div>
      )}

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        onChange={handleFileChange}
        style={{ display: 'none' }}
      />

      {/* 上传进度 */}
      {uploading && (
        <div className="upload-progress">
          <div className="progress-bar">
            <div className="progress-fill"></div>
          </div>
          <p>上传中...</p>
        </div>
      )}
    </div>
  );
};

export default ImageUpload;
