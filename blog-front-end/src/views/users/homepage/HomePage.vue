<template>
  <div class="home-page">
    <!-- 轮播图/横幅 -->
    <el-carousel v-if="bannerArticles.length > 0" class="banner-carousel" height="350px" indicator-position="outside"
      :interval="5000">
      <el-carousel-item v-for="article in bannerArticles" :key="article.id" @click="goToArticle(article.id)">
        <div class="banner-item" :style="{ backgroundImage: `url(${article.coverImage})` }">
          <div class="banner-content">
            <h2>{{ article.title }}</h2>
            <p>{{ article.summary }}</p>
            <div class="article-meta">
              <span>{{ formatDate(article.publishTime) }}</span>
              <span>{{ article.category }}</span>
            </div>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 文章列表 -->
    <div class="article-section">
      <div class="section-header">
        <h2>{{ sectionTitle }}</h2>
        <el-radio-group v-model="sortType" @change="handleSortChange">
          <el-radio-button label="latest">最新</el-radio-button>
          <el-radio-button label="popular">热门</el-radio-button>
        </el-radio-group>
      </div>

      <div class="article-list" v-loading="loading && articles.length === 0" ref="articleListRef">
        <el-card v-for="article in articles" :key="`${article.userName}-${article.articleAddTime}`" class="article-card"
          shadow="hover">
          <div class="article-header">
            <div class="author-info">
              <el-avatar :size="40" :src="article.userAvatar">
                {{ article.userName?.charAt(0) }}
              </el-avatar>
              <div class="author-details">
                <div class="author-name">{{ article.userName }}</div>
                <div class="publish-time">{{ formatTime(article.articleAddTime) }}</div>
              </div>

            </div>


            <el-tag v-if="article.articleType" size="large" background-color="#0d65fe"
              :type="getTagType(article.articleType)">
              {{ article.articleType }}
            </el-tag>
          </div>

          <div class="article-title-section" @click="goToArticleDetail(article.articleId)">
            <h3 class="article-title">{{ article.articleTitle }}</h3>
          </div>

          <!-- 文章内容 - 添加点击事件 -->
          <div class="article-content" @click="goToArticleDetail(article.articleId)">
            <p class="article-text">{{ article.articleContent }}</p>
          </div>



          <div class="article-tags" v-if="article.articleTags && article.articleTags.length > 0">
            <el-tag v-for="tag in article.articleTags" :key="tag" size="small" effect="plain" class="tag-item">
              {{ tag }}
            </el-tag>
          </div>

          <div class="article-actions">
            <div class="action-left">
              <span class="view-count">
                <el-icon :size="20">
                  <View />
                </el-icon>
                {{ formatNumber(article.articleLookNumber) }}
              </span>
            </div>

            <div class="action-buttons">
              <el-button text :class="{ 'liked': article.liked }" @click.stop="toggleLike(article)"
                class="action-btn like-btn">
                <el-icon :size="20">
                  <svg v-if="article.liked" viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                      d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4z"
                      fill="#ff4757" />
                  </svg>
                  <svg v-else viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                      d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4zM307.2 267.2c-38.4 0-76.8 14.4-105.6 43.2-57.6 57.6-57.6 153.6 0 211.2L512 832l310.4-310.4c57.6-57.6 57.6-153.6 0-211.2-57.6-57.6-153.6-57.6-211.2 0L512 409.6 413.6 310.4c-28.8-28.8-67.2-43.2-106.4-43.2z"
                      fill="currentColor" />
                  </svg>
                </el-icon>
                {{ formatNumber(article.articleGoodNumber) }}
              </el-button>

              <el-button text :class="{ 'collected': article.collected }" @click.stop="toggleCollect(article)"
                class="action-btn collect-btn">
                <el-icon :size="20">
                  <StarFilled v-if="article.collected" style="color: #ffd700" />
                  <Star v-else />
                </el-icon>
                {{ formatNumber(article.articleCollectionNumber) }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 加载更多指示器 -->
      <div v-if="loadingMore" class="loading-more">
        <el-icon class="is-loading">
          <Loading />
        </el-icon>
        <span>加载更多文章...</span>
      </div>

      <!-- 没有更多数据提示 -->
      <div v-if="noMoreData && articles.length > 0" class="no-more-data">
        <el-divider>已经到底了~</el-divider>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { View, Star, StarFilled, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getArticleList, likeArticle, collectArticle, getHotArticle } from '@/api/article'

const router = useRouter()

// 响应式数据
const bannerArticles = ref([])
const articles = ref([])
const loading = ref(false)
const loadingMore = ref(false)
const sortType = ref('latest')
const noMoreData = ref(false)
const articleListRef = ref()
const sectionTitle = ref('最新文章')

// 分页相关
const limit = 15 // 每次加载15篇文章，适合当前布局
const lastArticleTime = ref('')

// 获取文章列表
// 获取文章列表
const loadArticles = async (isLoadMore = false) => {
  if (loading.value || loadingMore.value || (isLoadMore && noMoreData.value)) {
    return
  }

  try {
    if (isLoadMore) {
      loadingMore.value = true
    } else {
      loading.value = true
      articles.value = []
      noMoreData.value = false
    }

    let response

    // 根据排序类型调用不同的 API
    if (sortType.value === 'latest') {
      // 最新文章 - 使用原有的分页逻辑
      const addTime = isLoadMore ? lastArticleTime.value : getCurrentTime()
      console.log('请求最新文章参数:', { addTime, limit })
      response = await getArticleList(addTime, limit)
    } else if (sortType.value === 'popular') {
      // 热门文章 - 热门文章可能不需要时间参数
      console.log('请求热门文章参数:', { limit })
      response = await getHotArticle(limit)
    }

    if (response && response.code === 200 && response.data && response.data.length > 0) {
      if (isLoadMore) {
        articles.value.push(...response.data)
      } else {
        articles.value = response.data
      }

      // 如果是最新文章，更新最后一篇文章的时间用于分页
      if (sortType.value === 'latest' && response.data.length > 0) {
        lastArticleTime.value = response.data[response.data.length - 1].articleAddTime
      }

      // 如果返回的数据少于请求的数量，说明没有更多数据了
      if (response.data.length < limit) {
        noMoreData.value = true
      }
    } else {
      if (isLoadMore) {
        noMoreData.value = true
      } else {
        ElMessage.info('暂无文章数据')
      }
    }
  } catch (error) {
    console.error('加载文章失败:', error)
    ElMessage.error('加载文章失败，请重试')
    if (isLoadMore) {
      noMoreData.value = true
    }
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}
// 获取当前时间（ISO 8601格式）
const getCurrentTime = () => {
  return new Date().toISOString()
}

// 滚动监听
// 滚动监听
const handleScroll = () => {
  if (!articleListRef.value || noMoreData.value) return
  
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight
  
  // 当滚动到距离底部200px时开始加载更多
  if (scrollTop + windowHeight >= documentHeight - 200) {
    // 对于热门文章，可能不支持分页加载或者分页逻辑不同
    if (sortType.value === 'popular') {
      // 如果后端不支持热门文章分页，则不执行加载更多
      return
    }
    loadArticles(true)
  }
}

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

// 格式化数字
const formatNumber = (num) => {
  if (!num) return 0
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num
}

// 获取标签类型
const getTagType = (type) => {
  const typeMap = {
    '技术': 'success',
    '生活': 'info',
    '随笔': 'warning',
    '教程': 'primary'
  }
  return typeMap[type] || 'info'
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

// 跳转到文章详情
const goToArticle = (id) => {
  router.push(`/users/article/${id}`)
}

const goToArticleDetail = (articleId) => {
  if (!articleId) {
    ElMessage.warning('文章ID不存在')
    return
  }
  console.log('跳转到文章详情，ID:', articleId)
  router.push(`/users/article/${articleId}`)
}

// 排序改变处理
// 排序改变处理
const handleSortChange = () => {
  lastArticleTime.value = '' // 重置时间
  noMoreData.value = false   // 重置无更多数据状态

  // 更新页面标题
  sectionTitle.value = sortType.value === 'latest' ? '最新文章' : '热门文章'

  // 加载文章
  loadArticles()
}

// 切换点赞状态
const toggleLike = async (article) => {
  try {
    // 获取正确的文章ID
    const articleId = article.articleId || article.id

    if (!articleId) {
      ElMessage.error('文章ID不存在，无法点赞')
      return
    }

    // 调用后端API
    const response = await likeArticle(articleId)

    // 如果API调用成功
    if (response.code === 200) {
      // 切换前端显示状态
      article.liked = !article.liked
      article.articleGoodNumber += article.liked ? 1 : -1

      // 提示用户
      ElMessage.success(article.liked ? '点赞成功' : '取消点赞')
    } else {
      // API调用失败，显示错误信息
      ElMessage.error(response.msg || '操作失败，请重试')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  }
}

// 切换收藏状态
const toggleCollect = async (article) => {
  try {
    // 获取正确的文章ID
    const articleId = article.articleId || article.id

    if (!articleId) {
      ElMessage.error('文章ID不存在，无法点赞')
      return
    }

    // 调用后端API
    const response = await collectArticle(articleId)

    // 如果API调用成功
    if (response.code === 200) {
      // 切换前端显示状态
      article.collected = !article.collected
      article.articleCollectionNumber += article.collected ? 1 : -1

      // 提示用户
      ElMessage.success(article.collected ? '收藏成功' : '取消收藏')
    } else {
      // API调用失败，显示错误信息
      ElMessage.error(response.msg || '操作失败，请重试')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  }
}

// 组件挂载
onMounted(() => {
  // 加载轮播图文章
  bannerArticles.value = [
    {
      id: 1,
      title: '欢迎来到我的博客',
      summary: '这里分享技术心得与生活感悟',
      coverImage: 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
      publishTime: new Date().toISOString(),
      category: '公告'
    },
    {
      id: 2,
      title: 'Vue 3 + Element Plus 实战',
      summary: '构建现代化的前端应用程序',
      coverImage: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
      publishTime: new Date().toISOString(),
      category: '技术分享'
    }
  ]

  // 加载文章列表
  loadArticles()

  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 32px;
  padding: 0 0 40px 0;
}

/* 轮播图样式 */
.banner-carousel {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.banner-item {
  position: relative;
  background-size: cover;
  background-position: center;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
}

.banner-item:hover {
  transform: scale(1.02);
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg,
      rgba(0, 0, 0, 0.6) 0%,
      rgba(0, 0, 0, 0.2) 50%,
      rgba(0, 0, 0, 0.6) 100%);
}

.banner-content {
  position: relative;
  color: white;
  text-align: center;
  padding: 40px;
  z-index: 1;
  max-width: 600px;
}

.banner-content h2 {
  font-size: 32px;
  margin-bottom: 16px;
  font-weight: 700;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

.banner-content p {
  font-size: 18px;
  margin-bottom: 20px;
  opacity: 0.95;
  line-height: 1.6;
}

/* 文章区域样式 */
.article-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 4px;
}

.section-header h2 {
  margin: 0;
  color: var(--el-text-color-primary);
  font-size: 24px;
  font-weight: 600;
}

/* 文章列表样式 */
.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid var(--el-border-color-lighter);
  overflow: hidden;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1);
  border-color: var(--el-color-primary-light-5);
}

/* 文章头部 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.author-name {
  font-weight: 600;
  color: var(--el-text-color-primary);
  font-size: 14px;
  line-height: 1;
}

.publish-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.article-title-section {
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-title-section:hover {
  transform: translateX(4px);
}

.article-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  line-height: 1.4;
  transition: color 0.3s ease;
  text-overflow: ellipsis;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-title-section:hover .article-title {
  color: var(--el-color-primary);
}


/* 文章内容 */
.article-content {
  margin-bottom: 16px;
}

.article-text {
  color: var(--el-text-color-regular);
  line-height: 1.7;
  margin: 0;
  font-size: 15px;
  word-break: break-word;
}

/* 文章标签 */
.article-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.tag-item {
  transition: all 0.3s ease;
  cursor: pointer;
}

.tag-item:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

/* 文章操作区 */
.article-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--el-border-color-lighter);
}

.action-left {
  display: flex;
  gap: 16px;
}

.view-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 13px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-btn:hover {
  background: var(--el-fill-color-light);
}

/* 点赞按钮样式 */
.like-btn.is-liked {
  color: #ff4757;
  background: rgba(255, 71, 87, 0.1);
}

.like-btn.is-liked:hover {
  background: rgba(255, 71, 87, 0.15);
}

/* 收藏按钮样式 */
.collect-btn.is-collected {
  color: var(--el-color-warning);
  background: var(--el-color-warning-light-9);
}

.collect-btn.is-collected:hover {
  background: var(--el-color-warning-light-8);
}

/* 加载状态 */
.loading-more {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  padding: 32px 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.no-more-data {
  padding: 20px 0;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-page {
    gap: 24px;
    padding-bottom: 20px;
  }

  .banner-content h2 {
    font-size: 24px;
  }

  .banner-content p {
    font-size: 16px;
  }

  .section-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .section-header h2 {
    font-size: 20px;
  }

  .article-card {
    border-radius: 8px;
  }

  .article-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .article-actions {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .action-buttons {
    justify-content: space-around;
  }
}

@media (max-width: 480px) {
  .article-list {
    gap: 16px;
  }

  .article-tags {
    gap: 6px;
  }

  .action-btn {
    padding: 6px 10px;
    font-size: 12px;
  }
}
</style>