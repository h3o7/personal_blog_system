<template>
    <div class="blog-list" v-loading="loading">
      <div v-if="blogs.length === 0 && !loading" class="empty-state">
        <el-empty description="暂无内容" />
      </div>
      
      <div v-else class="blog-items">
        <el-card
          v-for="blog in blogs"
          :key="blog.userId + blog.articleAddTime"
          class="blog-card"
          shadow="hover"
        >
          <div class="blog-header">
            <div class="author-info">
              <el-avatar :size="40" :src="blog.userAvatar">
                {{ blog.userName?.charAt(0) }}
              </el-avatar>
              <div class="author-details">
                <div class="author-name">{{ blog.userName }}</div>
                <div class="publish-time">{{ formatTime(blog.articleAddTime) }}</div>
              </div>
            </div>
          </div>
          
          <div class="blog-content">
            <h3 class="blog-title">{{ blog.articleTitle }}</h3>
            <p class="blog-text">{{ blog.articleContent }}</p>
          </div>
          
          <div class="blog-actions">
            <div class="action-stats">
              <span class="stat-item">
                <el-icon><View /></el-icon>
                {{ blog.articleLookNumber || 0 }}
              </span>
            </div>
            
            <div class="action-buttons">
              <el-button
                text
                :type="blog.isLiked ? 'primary' : ''"
                :class="{ 'is-active': blog.isLiked }"
                @click="toggleLike(blog)"
              >
                <el-icon><StarFilled v-if="blog.isLiked" /><Star v-else /></el-icon>
                {{ blog.articleGoodNumber || 0 }}
              </el-button>
              
              <el-button
                text
                :type="blog.isCollected ? 'warning' : ''"
                :class="{ 'is-active': blog.isCollected }"
                @click="toggleCollect(blog)"
              >
                <el-icon><CollectionTag /></el-icon>
                {{ blog.articleCollectionNumber || 0 }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ElMessage } from 'element-plus'
  import { View, Star, StarFilled, CollectionTag } from '@element-plus/icons-vue'
  
  const props = defineProps({
    blogs: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  })
  
  const emit = defineEmits(['refresh'])
  
  // 格式化时间
  const formatTime = (time) => {
    if (!time) return ''
    const date = new Date(time)
    const now = new Date()
    const diff = now - date
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    
    if (days === 0) {
      const hours = Math.floor(diff / (1000 * 60 * 60))
      if (hours === 0) {
        const minutes = Math.floor(diff / (1000 * 60))
        return minutes === 0 ? '刚刚' : `${minutes}分钟前`
      }
      return `${hours}小时前`
    } else if (days < 7) {
      return `${days}天前`
    } else {
      return date.toLocaleDateString('zh-CN')
    }
  }
  
  // 切换点赞状态
  const toggleLike = (blog) => {
    // 这里应该调用点赞API
    blog.isLiked = !blog.isLiked
    blog.articleGoodNumber += blog.isLiked ? 1 : -1
    ElMessage.success(blog.isLiked ? '点赞成功' : '取消点赞')
  }
  
  // 切换收藏状态
  const toggleCollect = (blog) => {
    // 这里应该调用收藏API
    blog.isCollected = !blog.isCollected
    blog.articleCollectionNumber += blog.isCollected ? 1 : -1
    ElMessage.success(blog.isCollected ? '收藏成功' : '取消收藏')
  }
  </script>
  
  <style scoped>
  .blog-list {
    min-height: 200px;
  }
  
  .blog-items {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  
  .blog-card {
    transition: transform 0.3s ease;
  }
  
  .blog-card:hover {
    transform: translateY(-2px);
  }
  
  .blog-header {
    margin-bottom: 16px;
  }
  
  .author-info {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .author-details {
    flex: 1;
  }
  
  .author-name {
    font-weight: 600;
    color: var(--el-text-color-primary);
    font-size: 14px;
  }
  
  .publish-time {
    font-size: 12px;
    color: var(--el-text-color-secondary);
    margin-top: 2px;
  }
  
  .blog-content {
    margin-bottom: 16px;
  }
  
  .blog-title {
    font-size: 16px;
    font-weight: 600;
    color: var(--el-text-color-primary);
    margin: 0 0 8px 0;
    line-height: 1.4;
  }
  
  .blog-text {
    color: var(--el-text-color-regular);
    line-height: 1.6;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  .blog-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid var(--el-border-color-lighter);
  }
  
  .action-stats {
    display: flex;
    gap: 16px;
  }
  
  .stat-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    color: var(--el-text-color-secondary);
  }
  
  .action-buttons {
    display: flex;
    gap: 8px;
  }
  
  .action-buttons .el-button.is-active {
    color: var(--el-color-primary);
  }
  
  .action-buttons .el-button.is-active:nth-child(2) {
    color: var(--el-color-warning);
  }
  
  .empty-state {
    padding: 60px 0;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .blog-actions {
      flex-direction: column;
      gap: 12px;
      align-items: stretch;
    }
    
    .action-buttons {
      justify-content: space-around;
    }
  }
  </style>