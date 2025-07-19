<template>
    <div class="profile-page">
      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <!-- 背景图片区域 -->
        <div class="cover-section">
          <div class="cover-image-container">
            <img 
              :src="coverImage" 
              alt="封面图片" 
              class="cover-image"
            />
            <div class="cover-overlay"></div>
          </div>
          
          <!-- 用户信息区域 -->
          <div class="user-info-section">
            <div class="avatar-container">
              <el-upload
                v-if="isCurrentUser"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="customUpload"
                class="avatar-uploader"
              >
                <el-avatar :size="120" :src="userInfo.userAvatar" class="user-avatar">
                  <el-icon size="40"><User /></el-icon>
                </el-avatar>
                <div class="avatar-edit-overlay" v-if="isCurrentUser">
                  <el-icon><Camera /></el-icon>
                </div>
              </el-upload>
              <el-avatar v-else :size="120" :src="userInfo.userAvatar" class="user-avatar">
                <el-icon size="40"><User /></el-icon>
              </el-avatar>
            </div>
            
            <div class="user-details">
              <div class="username-section">
                <el-input
                  v-if="isEditingName && isCurrentUser"
                  v-model="editUserName"
                  @blur="saveUserName"
                  @keyup.enter="saveUserName"
                  class="username-input"
                  size="large"
                />
                <h1 v-else @click="startEditName" :class="{ 'editable': isCurrentUser }">
                  {{ userInfo.userName || '未设置用户名' }}
                  <el-icon v-if="isCurrentUser" class="edit-icon"><Edit /></el-icon>
                </h1>
              </div>
              
              <div class="user-stats">
                <div class="stat-item" @click="goToFollowing">
                  <div class="stat-number">{{ userInfo.userFollow || 0 }}</div>
                  <div class="stat-label">关注</div>
                </div>
                <div class="stat-divider"></div>
                <div class="stat-item" @click="goToFollowers">
                  <div class="stat-number">{{ userInfo.userFans || 0 }}</div>
                  <div class="stat-label">粉丝</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 内容区域 -->
      <div class="content-section">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="content-tabs">
          <el-tab-pane name="myArticles">
            <template #label>
              <div class="tab-label">
                <el-icon><Edit /></el-icon>
                <span>我的文章 ({{ myArticleList.length }})</span>
              </div>
            </template>
            <div 
              class="article-list" 
              v-loading="myArticleLoading && myArticleList.length === 0"
              ref="myArticleContainerRef"
            >
              <el-card
                v-for="article in myArticleList"
                :key="`${article.userName}-${article.articleAddTime}`"
                class="article-card"
                shadow="hover"
              >
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
                  <el-tag 
                    v-if="article.articleType" 
                    size="large" 
                    background-color="#0d65fe"
                    :type="getTagType(article.articleType)"
                  >
                    {{ article.articleType }}
                  </el-tag>
                </div>
  
                <div class="article-title-section" @click="goToArticleDetail(article.articleId)">
                  <h3 class="article-title">{{ article.articleTitle }}</h3>
                </div>
  
                <div class="article-content" @click="goToArticleDetail(article.articleId)">
                  <p class="article-text">{{ article.articleContent }}</p>
                </div>
  
                <div class="article-tags" v-if="article.articleTags && article.articleTags.length > 0">
                  <el-tag
                    v-for="tag in article.articleTags"
                    :key="tag"
                    size="small"
                    effect="plain"
                    class="tag-item"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
                
                <div class="article-actions">
                  <div class="action-left">
                    <span class="view-count">
                      <el-icon :size="20"><View /></el-icon>
                      {{ formatNumber(article.articleLookNumber) }}
                    </span>
                  </div>
                  
                  <div class="action-buttons">
                    <el-button
                      text
                      :class="{ 'liked': article.liked }"
                      @click.stop="toggleLike(article)"
                      class="action-btn like-btn"
                    >
                      <el-icon :size="20">
                        <svg v-if="article.liked" viewBox="0 0 1024 1024" width="20" height="20">
                          <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4z" fill="#ff4757"/>
                        </svg>
                        <svg v-else viewBox="0 0 1024 1024" width="20" height="20">
                          <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4zM307.2 267.2c-38.4 0-76.8 14.4-105.6 43.2-57.6 57.6-57.6 153.6 0 211.2L512 832l310.4-310.4c57.6-57.6 57.6-153.6 0-211.2-57.6-57.6-153.6-57.6-211.2 0L512 409.6 413.6 310.4c-28.8-28.8-67.2-43.2-106.4-43.2z" fill="currentColor"/>
                        </svg>
                      </el-icon>
                      {{ formatNumber(article.articleGoodNumber) }}
                    </el-button>
                    
                    <el-button
                      text
                      :class="{ 'collected': article.collected }"
                      @click.stop="toggleCollect(article)"
                      class="action-btn collect-btn"
                    >
                      <el-icon :size="20">
                        <StarFilled v-if="article.collected" style="color: #ffd700"/>
                        <Star v-else />
                      </el-icon>
                      {{ formatNumber(article.articleCollectionNumber) }}
                    </el-button>
                  </div>
                </div>
              </el-card>
              
              <!-- 加载更多指示器 -->
              <div v-if="myArticleLoadingMore" class="loading-more">
                <el-icon class="is-loading"><Loading /></el-icon>
                <span>加载更多文章...</span>
              </div>
              
              <!-- 没有更多数据提示 -->
              <div v-if="myArticleNoMoreData && myArticleList.length > 0" class="no-more-data">
                <el-divider>已经到底了~</el-divider>
              </div>
              
              <!-- 空状态 -->
              <div v-if="myArticleList.length === 0 && !myArticleLoading" class="empty-state">
                <el-empty description="还没有发布任何文章" />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted, onUnmounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { User, Edit, Camera, Star, Loading, View, StarFilled } from '@element-plus/icons-vue'
  import { queryUserById, editUser, getMyArticles } from '@/api/user'
  import { likeArticle, collectArticle } from '@/api/article'
  import { uploadUser } from '@/api/upload'
  
  const route = useRoute()
  const router = useRouter()
  
  // 使用更美观的背景图片
  const coverImage = ref('https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/photo-1506905925346-21bda4d32df4.avif')
  
  // 响应式数据
  const activeTab = ref('myArticles')
  const isEditingName = ref(false)
  const editUserName = ref('')
  const uploading = ref(false)
  
  // 用户信息
  const userInfo = ref({
    userId: '',
    userName: '',
    userAvatar: '',
    userFans: '',
    userFollow: ''
  })
  
  // 我的文章相关状态
  const myArticleList = ref([])
  const myArticleLoading = ref(false)
  const myArticleLoadingMore = ref(false)
  const myArticleNoMoreData = ref(false)
  const myArticleCurrentPage = ref(1)
  const myArticleContainerRef = ref()
  
  // 分页配置
  const pageSize = 10
  
  // 计算属性
  const isCurrentUser = computed(() => {
    const loginData = localStorage.getItem('login')
    if (!loginData) return false
    const login = JSON.parse(loginData)
    const targetUserId = route.params.userId
    return login.id === targetUserId
  })
  
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
  
  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const targetUserId = route.params.userId
      const response = await queryUserById(targetUserId)
      userInfo.value = response.data
      userInfo.value.userId = targetUserId
    } catch (error) {
      console.error('获取用户信息失败:', error)
      ElMessage.error('获取用户信息失败')
    }
  }
  
  // 加载我的文章
  const loadMyArticles = async (isLoadMore = false) => {
    if (myArticleLoading.value || myArticleLoadingMore.value || (isLoadMore && myArticleNoMoreData.value)) {
      return
    }
  
    try {
      if (isLoadMore) {
        myArticleLoadingMore.value = true
      } else {
        myArticleLoading.value = true
        myArticleList.value = []
        myArticleNoMoreData.value = false
        myArticleCurrentPage.value = 1
      }
  
      const pageNum = isLoadMore ? myArticleCurrentPage.value + 1 : 1
      
      console.log('加载我的文章:', { pageNum, pageSize })
      
      const response = await getMyArticles(pageNum, pageSize)
      
      if (response.code === 200) {
        const data = response.data?.records || response.data || []
        
        if (data.length > 0) {
          if (isLoadMore) {
            myArticleList.value.push(...data)
            myArticleCurrentPage.value = pageNum
          } else {
            myArticleList.value = data
            myArticleCurrentPage.value = 1
          }
          
          // 如果返回的数据少于请求的数量，说明没有更多数据了
          if (data.length < pageSize) {
            myArticleNoMoreData.value = true
          }
        } else {
          if (isLoadMore) {
            myArticleNoMoreData.value = true
          }
        }
      } else {
        ElMessage.error(response.msg || '获取我的文章失败')
        if (isLoadMore) {
          myArticleNoMoreData.value = true
        }
      }
    } catch (error) {
      console.error('获取我的文章失败:', error)
      ElMessage.error('获取我的文章失败')
      if (isLoadMore) {
        myArticleNoMoreData.value = true
      }
    } finally {
      myArticleLoading.value = false
      myArticleLoadingMore.value = false
    }
  }
  
  // 滚动监听
  const handleScroll = () => {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop
    const windowHeight = window.innerHeight
    const documentHeight = document.documentElement.scrollHeight
    
    // 当滚动到距离底部200px时开始加载更多
    if (scrollTop + windowHeight >= documentHeight - 200) {
      if (activeTab.value === 'myArticles') {
        loadMyArticles(true)
      }
    }
  }
  
  // 跳转到文章详情
  const goToArticleDetail = (articleId) => {
    if (!articleId) {
      ElMessage.warning('文章ID不存在')
      return
    }
    console.log('跳转到文章详情，ID:', articleId)
    router.push(`/users/article/${articleId}`)
  }
  
  // 处理点赞
  const toggleLike = async (article) => {
    try {
      const response = await likeArticle(article.articleId)
      if (response.code === 200) {
        // 更新本地状态
        article.liked = !article.liked
        article.articleGoodNumber += article.liked ? 1 : -1
        ElMessage.success(article.liked ? '点赞成功' : '取消点赞')
      } else {
        ElMessage.error(response.msg || '操作失败')
      }
    } catch (error) {
      console.error('点赞操作失败:', error)
      ElMessage.error('操作失败，请重试')
    }
  }
  
  // 处理收藏
  const toggleCollect = async (article) => {
    try {
      const response = await collectArticle(article.articleId)
      if (response.code === 200) {
        // 更新本地状态
        article.collected = !article.collected
        article.articleCollectionNumber += article.collected ? 1 : -1
        ElMessage.success(article.collected ? '收藏成功' : '取消收藏')
      } else {
        ElMessage.error(response.msg || '操作失败')
      }
    } catch (error) {
      console.error('收藏操作失败:', error)
      ElMessage.error('操作失败，请重试')
    }
  }
  
  // 导航函数
  const goToFollowing = () => {
    router.push(`/users/following/${route.params.userId}`)
  }
  
  const goToFollowers = () => {
    router.push(`/users/followers/${route.params.userId}`)
  }
  
  // 标签页切换处理
  const handleTabClick = (tab) => {
    console.log('切换标签页:', tab.name)
    if (tab.name === 'myArticles') {
      // 如果我的文章列表为空，则加载
      if (myArticleList.value.length === 0) {
        loadMyArticles()
      }
    }
  }
  
  // 开始编辑用户名
  const startEditName = () => {
    if (!isCurrentUser.value) return
    isEditingName.value = true
    editUserName.value = userInfo.value.userName
  }
  
  // 保存用户名
  const saveUserName = async () => {
    if (!editUserName.value.trim()) {
      ElMessage.error('用户名不能为空')
      return
    }
    
    try {
      userInfo.value.userName = editUserName.value
      const response = await editUser(userInfo.value)
      isEditingName.value = false
      if (response.code !== 200) {
        ElMessage.error('修改用户名失败')
        return
      }
      ElMessage.success('用户名修改成功')
    } catch (error) {
      console.error('修改用户名失败:', error)
      ElMessage.error('修改用户名失败')
    }
  }
  
  // 头像上传前的验证
  const beforeAvatarUpload = (file) => {
    const isImage = file.type.startsWith('image/')
    if (!isImage) {
      ElMessage.error('头像只能是图片格式!')
      return false
    }
  
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
      ElMessage.error('头像大小不能超过 2MB!')
      return false
    }
  
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp']
    if (!allowedTypes.includes(file.type)) {
      ElMessage.error('只支持 JPG、PNG、GIF、WEBP 格式的图片!')
      return false
    }
  
    return true
  }
  
  // 自定义上传函数
  const customUpload = async (options) => {
    const { file } = options
    
    try {
      uploading.value = true
      ElMessage.info('正在上传头像...')
      
      const response = await uploadUser(file)
      
      if (response.code === 200 && response.data) {
        await handleAvatarSuccess(response.data)
      } else {
        throw new Error(response.msg || '上传失败')
      }
      
    } catch (error) {
      console.error('头像上传失败:', error)
      ElMessage.error(error.message || '头像上传失败，请重试')
    } finally {
      uploading.value = false
    }
  }
  
  // 处理头像上传成功
  const handleAvatarSuccess = async (avatarUrl) => {
    try {
      const updatedUserInfo = {
        ...userInfo.value,
        userAvatar: avatarUrl
      }
      
      const response = await editUser(updatedUserInfo)
      
      if (response.code === 200) {
        userInfo.value.userAvatar = avatarUrl
        ElMessage.success('头像更新成功')
      } else {
        throw new Error(response.msg || '更新头像失败')
      }
      
    } catch (error) {
      console.error('更新头像失败:', error)
      ElMessage.error(error.message || '更新头像失败，请重试')
    }
  }
  
  // 组件挂载时初始化
  onMounted(() => {
    fetchUserInfo()
    // 默认加载我的文章
    loadMyArticles()
    // 添加滚动监听
    window.addEventListener('scroll', handleScroll)
  })
  
  // 组件卸载
  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
  </script>
  
  <style scoped>
  /* 样式保持不变... */
  .profile-page {
    padding: 0;
    height: 100%;
  }
  
  /* 个人信息卡片 */
  .profile-card {
    position: relative;
    overflow: hidden;
  }
  
  .cover-section {
    position: relative;
    height: 320px;
  }
  
  .cover-image-container {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
  
  .cover-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .cover-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      to bottom,
      rgba(0, 0, 0, 0.1) 0%,
      rgba(0, 0, 0, 0.3) 70%,
      rgba(0, 0, 0, 0.7) 100%
    );
  }
  
  .user-info-section {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 32px;
    color: white;
    display: flex;
    align-items: flex-end;
    gap: 24px;
    z-index: 2;
  }
  
  .avatar-container {
    position: relative;
    flex-shrink: 0;
  }
  
  .avatar-uploader {
    position: relative;
    cursor: pointer;
  }
  
  .user-avatar {
    border: 4px solid rgba(255, 255, 255, 0.9);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    transition: transform 0.3s ease;
  }
  
  .avatar-uploader:hover .user-avatar {
    transform: scale(1.05);
  }
  
  .avatar-edit-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.6);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    color: white;
    font-size: 24px;
  }
  
  .avatar-uploader:hover .avatar-edit-overlay {
    opacity: 1;
  }
  
  .avatar-uploader.uploading .avatar-edit-overlay {
    opacity: 1;
    background: rgba(64, 158, 255, 0.8);
  }
  
  .avatar-uploader.uploading .user-avatar {
    opacity: 0.7;
  }
  
  .user-details {
    flex: 1;
    min-width: 0;
  }
  
  .username-section {
    margin-bottom: 20px;
  }
  
  .username-section h1 {
    margin: 0;
    font-size: 32px;
    font-weight: 700;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .username-section h1.editable {
    cursor: pointer;
    transition: all 0.3s ease;
  }
  
  .username-section h1.editable:hover {
    transform: translateY(-2px);
  }
  
  .edit-icon {
    font-size: 18px;
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  .username-section h1.editable:hover .edit-icon {
    opacity: 1;
  }
  
  .username-input {
    max-width: 400px;
  }
  
  .username-input :deep(.el-input__wrapper) {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
  }
  
  .user-stats {
    display: flex;
    align-items: center;
    gap: 24px;
  }
  
  .stat-item {
    text-align: center;
    cursor: pointer;
    transition: transform 0.3s ease;
  }
  
  .stat-item:hover {
    transform: translateY(-2px);
  }
  
  .stat-number {
    font-size: 24px;
    font-weight: 700;
    line-height: 1;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  }
  
  .stat-label {
    font-size: 14px;
    margin-top: 4px;
    opacity: 0.9;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }
  
  .stat-divider {
    width: 1px;
    height: 36px;
    background: rgba(255, 255, 255, 0.3);
  }
  
  /* 内容区域 */
  .content-section {
    padding: 32px;
  }
  
  .content-tabs :deep(.el-tabs__header) {
    margin-bottom: 32px;
    border-bottom: 2px solid var(--el-border-color-light);
  }
  
  .content-tabs :deep(.el-tabs__nav-wrap) {
    padding: 0;
  }
  
  .content-tabs :deep(.el-tabs__item) {
    padding: 16px 24px;
    font-size: 16px;
    font-weight: 500;
    color: var(--el-text-color-secondary);
    transition: all 0.3s ease;
  }
  
  .content-tabs :deep(.el-tabs__item.is-active) {
    color: var(--el-color-primary);
    font-weight: 600;
  }
  
  .content-tabs :deep(.el-tabs__active-bar) {
    height: 3px;
    border-radius: 2px;
  }
  
  .tab-label {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  /* 文章列表样式 */
  .article-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
    min-height: 200px;
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
  
  /* 文章标题样式 */
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
    cursor: pointer;
    transition: all 0.3s ease;
    padding: 8px;
    border-radius: 8px;
  }
  
  .article-content:hover {
    background: var(--el-fill-color-extra-light);
    transform: translateX(2px);
  }
  
  .article-text {
    color: var(--el-text-color-regular);
    line-height: 1.7;
    margin: 0;
    font-size: 15px;
    word-break: break-word;
    transition: color 0.3s ease;
  }
  
  .article-content:hover .article-text {
    color: var(--el-text-color-primary);
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
  .like-btn.liked {
    color: #ff4757;
    background: rgba(255, 71, 87, 0.1);
  }
  
  .like-btn.liked:hover {
    background: rgba(255, 71, 87, 0.15);
  }
  
  /* 收藏按钮样式 */
  .collect-btn.collected {
    color: var(--el-color-warning);
    background: var(--el-color-warning-light-9);
  }
  
  .collect-btn.collected:hover {
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
  
  .empty-state {
    padding: 60px 0;
    text-align: center;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .cover-section {
      height: 280px;
    }
    
    .user-info-section {
      padding: 20px;
      flex-direction: column;
      align-items: center;
      text-align: center;
      gap: 16px;
    }
    
    .username-section h1 {
      font-size: 28px;
    }
    
    .user-stats {
      gap: 20px;
    }
    
    .stat-number {
      font-size: 20px;
    }
    
    .content-section {
      padding: 20px;
    }
    
    .content-tabs :deep(.el-tabs__item) {
      padding: 12px 16px;
      font-size: 14px;
    }
    
    .article-title {
      font-size: 18px;
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
    .cover-section {
      height: 240px;
    }
    
    .user-info-section {
      padding: 16px;
    }
    
    .username-section h1 {
      font-size: 24px;
    }
    
    .user-avatar {
      width: 100px !important;
      height: 100px !important;
    }
    
    .content-section {
      padding: 16px;
    }
    
    .article-title {
      font-size: 16px;
    }
    
    .action-btn {
      padding: 6px 10px;
      font-size: 12px;
    }
  }
  </style>