<template>
    <div class="user-sidebar">
      <!-- 博主信息 -->
      <el-card class="author-card" shadow="hover">
        <div class="author-info">
          <el-avatar :size="80" :src="authorInfo.avatar" />
          <h3>{{ authorInfo.name }}</h3>
          <p class="bio">{{ authorInfo.bio }}</p>
          <div class="stats">
            <div class="stat-item">
              <span class="number">{{ statistics.articleCount }}</span>
              <span class="label">文章</span>
            </div>
            <div class="stat-item">
              <span class="number">{{ statistics.categoryCount }}</span>
              <span class="label">分类</span>
            </div>
            <div class="stat-item">
              <span class="number">{{ statistics.tagCount }}</span>
              <span class="label">标签</span>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 热门文章 -->
      <el-card class="hot-articles" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><TrendCharts /></el-icon>
            <span>热门文章</span>
          </div>
        </template>
        <div class="article-list">
          <div
            v-for="article in hotArticles"
            :key="article.id"
            class="article-item"
            @click="goToArticle(article.id)"
          >
            <div class="article-title">{{ article.title }}</div>
            <div class="article-meta">
              <el-icon><View /></el-icon>
              <span>{{ article.viewCount }}</span>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 分类 -->
      <el-card class="categories" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><Folder /></el-icon>
            <span>分类</span>
          </div>
        </template>
        <div class="category-list">
          <el-tag
            v-for="category in categories"
            :key="category.id"
            class="category-tag"
            @click="goToCategory(category.id)"
            effect="plain"
          >
            {{ category.name }} ({{ category.count }})
          </el-tag>
        </div>
      </el-card>
      
      <!-- 标签云 -->
      <el-card class="tags" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><CollectionTag /></el-icon>
            <span>标签云</span>
          </div>
        </template>
        <div class="tag-cloud">
          <el-tag
            v-for="tag in tags"
            :key="tag.id"
            :size="getTagSize(tag.count)"
            :type="getTagType(tag.count)"
            class="tag-item"
            @click="goToTag(tag.id)"
            effect="plain"
          >
            {{ tag.name }}
          </el-tag>
        </div>
      </el-card>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { TrendCharts, View, Folder, CollectionTag } from '@element-plus/icons-vue'
  
  const router = useRouter()
  
  const authorInfo = ref({
    name: '博主名称',
    bio: '这里是博主的个人简介，可以介绍自己的技术方向和兴趣爱好。',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  })
  
  const statistics = ref({
    articleCount: 0,
    categoryCount: 0,
    tagCount: 0
  })
  
  const hotArticles = ref([])
  const categories = ref([])
  const tags = ref([])
  
  const goToArticle = (id: number) => {
    router.push(`/users/article/${id}`)
  }
  
  const goToCategory = (id: number) => {
    router.push(`/users/categories/${id}`)
  }
  
  const goToTag = (id: number) => {
    router.push(`/users/tags/${id}`)
  }
  
  const getTagSize = (count: number) => {
    if (count > 10) return 'large'
    if (count > 5) return 'default'
    return 'small'
  }
  
  const getTagType = (count: number) => {
    const types = ['', 'success', 'info', 'warning', 'danger']
    return types[count % types.length]
  }
  
  // 模拟数据加载
  onMounted(() => {
    // 这里应该调用API获取真实数据
    statistics.value = {
      articleCount: 42,
      categoryCount: 8,
      tagCount: 25
    }
    
    hotArticles.value = [
      { id: 1, title: 'Vue 3 Composition API 深度解析', viewCount: 1234 },
      { id: 2, title: 'TypeScript 最佳实践指南', viewCount: 987 },
      { id: 3, title: 'Element Plus 组件库使用技巧', viewCount: 756 },
      { id: 4, title: '前端性能优化实战', viewCount: 654 },
      { id: 5, title: 'Webpack 5 配置详解', viewCount: 543 }
    ]
    
    categories.value = [
      { id: 1, name: '前端开发', count: 15 },
      { id: 2, name: 'Vue.js', count: 12 },
      { id: 3, name: 'TypeScript', count: 8 },
      { id: 4, name: '工具分享', count: 7 }
    ]
    
    tags.value = [
      { id: 1, name: 'Vue3', count: 12 },
      { id: 2, name: 'TypeScript', count: 8 },
      { id: 3, name: 'JavaScript', count: 15 },
      { id: 4, name: 'CSS', count: 6 },
      { id: 5, name: 'Webpack', count: 4 },
      { id: 6, name: '性能优化', count: 7 },
      { id: 7, name: 'Element Plus', count: 5 }
    ]
  })
  </script>
  
  <style scoped>
  .user-sidebar {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .author-card .author-info {
    text-align: center;
  }
  
  .author-info h3 {
    margin: 16px 0 8px;
    color: var(--el-text-color-primary);
  }
  
  .bio {
    color: var(--el-text-color-secondary);
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 20px;
  }
  
  .stats {
    display: flex;
    justify-content: space-around;
    border-top: 1px solid var(--el-border-color);
    padding-top: 16px;
  }
  
  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
  }
  
  .number {
    font-size: 20px;
    font-weight: 600;
    color: var(--el-color-primary);
  }
  
  .label {
    font-size: 12px;
    color: var(--el-text-color-secondary);
  }
  
  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
  }
  
  .article-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .article-item {
    padding: 12px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .article-item:hover {
    background: var(--el-bg-color-page);
  }
  
  .article-title {
    font-size: 14px;
    color: var(--el-text-color-primary);
    margin-bottom: 6px;
    line-height: 1.4;
  }
  
  .article-meta {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: var(--el-text-color-secondary);
  }
  
  .category-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .category-tag {
    cursor: pointer;
    justify-content: space-between;
    width: 100%;
  }
  
  .tag-cloud {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .tag-item {
    cursor: pointer;
  }
  </style>