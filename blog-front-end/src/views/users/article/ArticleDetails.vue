<template>
  <div class="article-detail-page">
    <div class="article-container" v-loading="loading">
      <!-- 头部导航 -->
      <div class="article-header">
        <div class="header-left">
          <el-button 
            text 
            @click="goBack" 
            class="back-btn"
            size="large"
          >
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          
          <div class="author-info">
            <el-avatar 
              :size="40" 
              :src="article.userAvatar"
              @click="goToUserProfile"
              class="author-avatar"
            >
              {{ article.userName?.charAt(0) }}
            </el-avatar>
            <div class="author-details">
              <div class="author-name" @click="goToUserProfile">{{ article.userName }}</div>
              <div class="publish-time">{{ formatTime(article.articleAddTime) }}</div>
            </div>
          </div>
        </div>
        
        <div class="header-right">
          <el-button
            v-if="!isCurrentUser"
            :type="isFollowing ? 'primary' : '#fff'"
            :loading="followLoading"
            @click="toggleFollow"
            class="follow-btn"
            size="large"
          >
            {{ isFollowing ? '已关注' : '关注' }}
          </el-button>
        </div>
      </div>

      <!-- 文章内容区域 -->
      <div class="article-content" v-if="!loading && article.articleId">
        <!-- 文章标题 -->
        <h1 class="article-title">{{ article.articleTitle }}</h1>

        <!-- 文章标签 -->
        <div class="article-tags" v-if="article.articleTags && article.articleTags.length > 0">
          <el-tag
            v-for="tag in article.articleTags"
            :key="tag"
            size="small"
            effect="plain"
            class="tag-item"
          >
            # {{ tag }}
          </el-tag>
        </div>

        <!-- 文章正文 -->
        <div class="article-text">
          <p>{{ article.articleContent }}</p>
        </div>

        <!-- 文章操作区 -->
        <div class="article-actions">
          <div class="action-buttons">
            <el-button
              text
              :class="{ 'liked': article.liked }"
              @click="toggleLike"
              class="action-btn like-btn"
              size="large"
            >
              <el-icon :size="20">
                <svg v-if="article.liked" viewBox="0 0 1024 1024" width="20" height="20">
                  <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4z" fill="#ff4757"/>
                </svg>
                <svg v-else viewBox="0 0 1024 1024" width="20" height="20">
                  <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4zM307.2 267.2c-38.4 0-76.8 14.4-105.6 43.2-57.6 57.6-57.6 153.6 0 211.2L512 832l310.4-310.4c57.6-57.6 57.6-153.6 0-211.2-57.6-57.6-153.6-57.6-211.2 0L512 409.6 413.6 310.4c-28.8-28.8-67.2-43.2-106.4-43.2z" fill="currentColor"/>
                </svg>
              </el-icon>
              <span>{{ article.liked ? '已点赞' : '点赞' }}</span>
            </el-button>
            
            <el-button
              text
              :class="{ 'collected': article.collected }"
              @click="toggleCollect"
              class="action-btn collect-btn"
              size="large"
            >
              <el-icon :size="20">
                <StarFilled v-if="article.collected" style="color: #ffd700"/>
                <Star v-else />
              </el-icon>
              <span>{{ article.collected ? '已收藏' : '收藏' }}</span>
            </el-button>

            <el-button
              text
              class="action-btn comment-btn"
              size="large"
              @click="scrollToComments"
            >
              <el-icon :size="20"><ChatDotRound /></el-icon>
              <span>评论 ({{ totalComments }})</span>
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 评论区域 -->
      <div class="comments-section" ref="commentsSection" v-if="!loading && article.articleId">
        <div class="comments-header">
          <h3>评论 ({{ totalComments }})</h3>
        </div>

        <!-- 评论发表表单 -->
        <div class="comment-form-container">
          <div class="comment-form">
            <div class="comment-input-area">
              <el-avatar :size="36" :src="currentUser.userAvatar" class="user-avatar">
                {{ currentUser.userName?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="input-wrapper">
                <el-input
                  v-model="newCommentContent"
                  type="textarea"
                  :rows="3"
                  placeholder="写下你的评论..."
                  maxlength="500"
                  show-word-limit
                  resize="none"
                  class="comment-textarea"
                />
                <div class="comment-actions">
                  <el-button
                    type="primary"
                    :disabled="!newCommentContent.trim()"
                    :loading="submittingComment"
                    @click="submitComment"
                    size="small"
                  >
                    发表评论
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list" v-loading="commentsLoading && comments.length === 0">
          <div 
            v-for="comment in comments" 
            :key="comment.commentId"
            class="comment-item"
          >
            <!-- 主评论 -->
            <div class="comment-main">
              <el-avatar 
                :size="40" 
                :src="comment.userAvatar"
                @click="goToUserProfile(comment.userId)"
                class="comment-avatar"
              >
                {{ comment.userName?.charAt(0) }}
              </el-avatar>
              
              <div class="comment-content-area">
                <div class="comment-header">
                  <span 
                    class="comment-username"
                    @click="goToUserProfile(comment.userId)"
                  >
                    {{ comment.userName }}
                  </span>
                  <span class="comment-time">{{ formatTime(comment.commentTime) }}</span>
                </div>
                
                <div class="comment-text">{{ comment.commentContent }}</div>
                
                <div class="comment-footer">
                  <el-button
                    text
                    size="small"
                    :class="{ 'liked': comment.liked }"
                    @click="toggleCommentLike(comment)"
                    class="comment-like-btn"
                  >
                    <el-icon :size="16">
                      <svg v-if="comment.liked" viewBox="0 0 1024 1024" width="16" height="16">
                        <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4z" fill="#ff4757"/>
                      </svg>
                      <svg v-else viewBox="0 0 1024 1024" width="16" height="16">
                        <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4zM307.2 267.2c-38.4 0-76.8 14.4-105.6 43.2-57.6 57.6-57.6 153.6 0 211.2L512 832l310.4-310.4c57.6-57.6 57.6-153.6 0-211.2-57.6-57.6-153.6-57.6-211.2 0L512 409.6 413.6 310.4c-28.8-28.8-67.2-43.2-106.4-43.2z" fill="currentColor"/>
                      </svg>
                    </el-icon>
                    {{ formatNumber(comment.commentGoodNumber) }}
                  </el-button>
                  
                  <el-button
                    text
                    size="small"
                    @click="showReplyForm(comment, comment)"
                    class="comment-reply-btn"
                  >
                    回复
                  </el-button>

                  <el-button
                    v-if="comment.hasReplies"
                    text
                    size="small"
                    @click="toggleReplies(comment)"
                    class="comment-expand-btn"
                  >
                    {{ comment.showReplies ? '收起回复' : '展开回复' }}
                    <el-icon :class="{ 'rotated': comment.showReplies }">
                      <ArrowDown />
                    </el-icon>
                  </el-button>

                  <el-button
                    v-if="canDeleteComment(comment)"
                    text
                    size="small"
                    type="danger"
                    @click="deleteCommentHandler(comment)"
                    class="comment-delete-btn"
                  >
                    删除
                  </el-button>
                </div>

                <!-- 主评论回复表单 -->
                <div v-if="comment.showReplyForm" class="reply-form">
                  <div class="reply-input-area">
                    <el-avatar :size="32" :src="currentUser.userAvatar" class="user-avatar">
                      {{ currentUser.userName?.charAt(0) || 'U' }}
                    </el-avatar>
                    <div class="input-wrapper">
                      <el-input
                        v-model="comment.replyContent"
                        type="textarea"
                        :rows="2"
                        :placeholder="`回复 @${comment.targetUserName || comment.userName}:`"
                        maxlength="300"
                        show-word-limit
                        resize="none"
                        class="reply-textarea"
                      />
                      <div class="reply-actions">
                        <el-button
                          size="small"
                          @click="cancelReply(comment)"
                        >
                          取消
                        </el-button>
                        <el-button
                          type="primary"
                          size="small"
                          :disabled="!comment.replyContent?.trim()"
                          :loading="comment.submittingReply"
                          @click="submitReply(comment)"
                        >
                          回复
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 回复列表 -->
                <div 
                  v-if="comment.showReplies && comment.replies && comment.replies.length > 0"
                  class="replies-list"
                >
                  <div 
                    v-for="reply in comment.replies"
                    :key="reply.commentId"
                    class="reply-item"
                  >
                    <el-avatar 
                      :size="32" 
                      :src="reply.userAvatar"
                      @click="goToUserProfile(reply.userId)"
                      class="reply-avatar"
                    >
                      {{ reply.userName?.charAt(0) }}
                    </el-avatar>
                    
                    <div class="reply-content-area">
                      <div class="reply-header">
                        <span 
                          class="reply-username"
                          @click="goToUserProfile(reply.userId)"
                        >
                          {{ reply.userName }}
                        </span>
                        <span v-if="reply.repliedName" class="reply-target">
                          回复 @{{ reply.repliedName }}
                        </span>
                        <span class="reply-time">{{ formatTime(reply.commentTime) }}</span>
                      </div>
                      
                      <div class="reply-text">{{ reply.commentContent }}</div>
                      
                      <div class="reply-footer">
                        <el-button
                          text
                          size="small"
                          :class="{ 'liked': reply.liked }"
                          @click="toggleCommentLike(reply)"
                          class="reply-like-btn"
                        >
                          <el-icon :size="14">
                            <svg v-if="reply.liked" viewBox="0 0 1024 1024" width="14" height="14">
                              <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4z" fill="#ff4757"/>
                            </svg>
                            <svg v-else viewBox="0 0 1024 1024" width="14" height="14">
                              <path d="M512 896c-12.8 0-25.6-4.8-35.2-14.4L166.4 563.2c-76.8-76.8-76.8-201.6 0-278.4 38.4-38.4 89.6-57.6 140.8-57.6s102.4 19.2 140.8 57.6L512 348.8l64-64c76.8-76.8 201.6-76.8 278.4 0 76.8 76.8 76.8 201.6 0 278.4L547.2 881.6c-9.6 9.6-22.4 14.4-35.2 14.4zM307.2 267.2c-38.4 0-76.8 14.4-105.6 43.2-57.6 57.6-57.6 153.6 0 211.2L512 832l310.4-310.4c57.6-57.6 57.6-153.6 0-211.2-57.6-57.6-153.6-57.6-211.2 0L512 409.6 413.6 310.4c-28.8-28.8-67.2-43.2-106.4-43.2z" fill="currentColor"/>
                            </svg>
                          </el-icon>
                          {{ formatNumber(reply.commentGoodNumber) }}
                        </el-button>

                        <!-- 回复按钮 - 回复二级评论 -->
                        <el-button
                          text
                          size="small"
                          @click="showReplyForm(comment, reply)"
                          class="reply-reply-btn"
                        >
                          回复
                        </el-button>

                        <el-button
                          v-if="canDeleteComment(reply)"
                          text
                          size="small"
                          type="danger"
                          @click="deleteCommentHandler(reply)"
                          class="reply-delete-btn"
                        >
                          删除
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 加载更多回复 -->
                <div 
                  v-if="comment.showReplies && comment.hasMoreReplies"
                  class="load-more-replies"
                >
                  <el-button
                    text
                    size="small"
                    :loading="comment.loadingReplies"
                    @click="loadMoreReplies(comment)"
                    class="load-more-btn"
                  >
                    加载更多回复
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 加载更多评论 -->
          <div v-if="loadingMoreComments" class="loading-more-comments">
            <el-icon class="is-loading"><Loading /></el-icon>
            <span>加载更多评论...</span>
          </div>

          <!-- 没有更多评论 -->
          <div v-if="noMoreComments && comments.length > 0" class="no-more-comments">
            <el-divider>没有更多评论了</el-divider>
          </div>

          <!-- 空状态 -->
          <div v-if="comments.length === 0 && !commentsLoading" class="empty-comments">
            <el-empty description="暂无评论，快来抢沙发吧！" :image-size="80" />
          </div>
        </div>
      </div>
      
      <!-- 文章不存在或加载失败 -->
      <div v-else-if="!loading" class="article-not-found">
        <el-empty description="文章不存在或已被删除">
          <el-button type="primary" @click="goBack">返回上一页</el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, 
  View, 
  Star, 
  StarFilled, 
  ChatDotRound,
  ArrowDown,
  Loading
} from '@element-plus/icons-vue'
import { getArticle, likeArticle, collectArticle } from '@/api/article'
import { followUser, isFollow, queryUserById } from '@/api/user'
import { 
  queryArticleComments, 
  queryReplyComments,
  addArticleComment, 
  addReplyComment,
  likeComment,
  deleteComment
} from '@/api/comment'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const followLoading = ref(false)
const isFollowing = ref(false)
const article = ref({})

// 评论相关数据
const comments = ref([])
const commentsLoading = ref(false)
const loadingMoreComments = ref(false)
const noMoreComments = ref(false)
const totalComments = ref(0)
const commentsSection = ref()

// 评论表单数据
const newCommentContent = ref('')
const submittingComment = ref(false)

// 当前用户信息
const currentUser = ref({})

// 分页配置
const commentsLimit = 10
const repliesLimit = 5

// 计算当前用户是否为文章作者
const isCurrentUser = computed(() => {
  const loginData = localStorage.getItem('login')
  if (!loginData || !article.value.userId) return false
  const login = JSON.parse(loginData)
  return login.id === article.value.userId
})

// 获取当前用户信息
const getCurrentUser = async () => {
  const loginData = localStorage.getItem('login')
  if (loginData) {
    const login = JSON.parse(loginData)
    try {
      // 通过API获取完整的用户信息，包括头像
      const response = await queryUserById(login.id)
      if (response.code === 200 && response.data) {
        currentUser.value = {
          userId: login.id,
          userName: response.data.userName || login.userName || 'h3o7',
          userAvatar: response.data.userAvatar || login.userAvatar || ''
        }
      } else {
        // 如果API调用失败，使用本地存储的信息
        currentUser.value = {
          userId: login.id,
          userName: login.userName || 'h3o7',
          userAvatar: login.userAvatar || ''
        }
      }
    } catch (error) {
      console.error('获取当前用户信息失败:', error)
      // 如果API调用失败，使用本地存储的信息
      currentUser.value = {
        userId: login.id,
        userName: login.userName || 'h3o7',
        userAvatar: login.userAvatar || ''
      }
    }
  }
}

// 检查是否可以删除评论
const canDeleteComment = (comment) => {
  const loginData = localStorage.getItem('login')
  if (!loginData) return false
  const login = JSON.parse(loginData)
  return login.id === comment.userId || login.id === article.value.userId
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
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
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

// 滚动到评论区
const scrollToComments = () => {
  nextTick(() => {
    commentsSection.value?.scrollIntoView({ 
      behavior: 'smooth',
      block: 'start'
    })
  })
}

// 获取文章详情
const fetchArticleDetail = async () => {
  const articleId = route.params.articleId
  if (!articleId) {
    ElMessage.error('文章ID不存在')
    return
  }

  loading.value = true
  try {
    console.log('获取文章详情，ID:', articleId)
    const response = await getArticle(articleId)
    
    if (response.code === 200 && response.data) {
      article.value = response.data
      console.log('文章详情:', article.value)
      
      // 获取文章后检查是否关注作者
      if (!isCurrentUser.value && article.value.userId) {
        await checkFollowStatus()
      }
      
      // 加载评论
      await loadComments()
    } else {
      ElMessage.error(response.msg || '获取文章详情失败')
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

// 加载评论列表
const loadComments = async (isLoadMore = false) => {
  if (commentsLoading.value || loadingMoreComments.value || (isLoadMore && noMoreComments.value)) {
    return
  }

  try {
    if (isLoadMore) {
      loadingMoreComments.value = true
    } else {
      commentsLoading.value = true
      comments.value = []
      noMoreComments.value = false
    }

    const currentLimit = isLoadMore ? comments.value.length + commentsLimit : commentsLimit
    const response = await queryArticleComments(article.value.articleId, currentLimit)

    if (response.code === 200 && response.data) {
      const newComments = response.data
      
      if (newComments.length > 0) {
        // 为每个评论添加额外属性
        const processedComments = newComments.map(comment => ({
          ...comment,
          showReplies: false,
          showReplyForm: false,
          replies: [],
          hasReplies: false,
          hasMoreReplies: false,
          loadingReplies: false,
          submittingReply: false,
          replyContent: '',
          targetUserId: '', // 目标用户ID（用于回复）
          targetUserName: '' // 目标用户名（用于回复）
        }))

        if (isLoadMore) {
          // 只添加新的评论，避免重复
          const existingIds = new Set(comments.value.map(c => c.commentId))
          const newItems = processedComments.filter(c => !existingIds.has(c.commentId))
          comments.value.push(...newItems)
        } else {
          comments.value = processedComments
        }

        // 检查每个评论是否有回复
        await checkCommentsForReplies()

        // 如果返回的数据少于请求的数量，说明没有更多数据了
        if (newComments.length < currentLimit) {
          noMoreComments.value = true
        }
      } else {
        if (isLoadMore) {
          noMoreComments.value = true
        }
      }

      totalComments.value = newComments.length
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    ElMessage.error('加载评论失败')
  } finally {
    commentsLoading.value = false
    loadingMoreComments.value = false
  }
}

// 检查评论是否有回复
const checkCommentsForReplies = async () => {
  for (const comment of comments.value) {
    try {
      const response = await queryReplyComments(comment.commentId, 1)
      if (response.code === 200 && response.data && response.data.length > 0) {
        comment.hasReplies = true
      }
    } catch (error) {
      console.error('检查回复失败:', error)
    }
  }
}

// 切换回复显示状态
const toggleReplies = async (comment) => {
  if (!comment.showReplies) {
    // 展开回复，加载回复列表
    await loadReplies(comment)
  }
  comment.showReplies = !comment.showReplies
}

// 加载回复列表
const loadReplies = async (comment, isLoadMore = false) => {
  if (comment.loadingReplies) return

  comment.loadingReplies = true
  try {
    const currentLimit = isLoadMore ? comment.replies.length + repliesLimit : repliesLimit
    const response = await queryReplyComments(comment.commentId, currentLimit)

    if (response.code === 200 && response.data) {
      if (isLoadMore) {
        // 只添加新的回复
        const existingIds = new Set(comment.replies.map(r => r.commentId))
        const newReplies = response.data.filter(r => !existingIds.has(r.commentId))
        comment.replies.push(...newReplies)
      } else {
        comment.replies = response.data
      }

      // 检查是否还有更多回复
      comment.hasMoreReplies = response.data.length >= currentLimit
    }
  } catch (error) {
    console.error('加载回复失败:', error)
    ElMessage.error('加载回复失败')
  } finally {
    comment.loadingReplies = false
  }
}

// 加载更多回复
const loadMoreReplies = async (comment) => {
  await loadReplies(comment, true)
}

// 显示回复表单
const showReplyForm = (parentComment, targetComment) => {
  // 隐藏其他评论的回复表单
  comments.value.forEach(c => {
    c.showReplyForm = false
  })
  
  // 设置回复目标
  parentComment.targetUserId = targetComment.userId
  parentComment.targetUserName = targetComment.userName
  parentComment.showReplyForm = true
  parentComment.replyContent = ''
}

// 取消回复
const cancelReply = (comment) => {
  comment.showReplyForm = false
  comment.replyContent = ''
  comment.targetUserId = ''
  comment.targetUserName = ''
}

// 提交评论
const submitComment = async () => {
  if (!newCommentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submittingComment.value = true
  try {
    const commentData = {
      articleId: article.value.articleId,
      commentContent: newCommentContent.value.trim()
    }

    const response = await addArticleComment(commentData)
    if (response.code === 200) {
      ElMessage.success('评论发表成功')
      newCommentContent.value = ''
      // 重新加载评论列表
      await loadComments()
    } else {
      ElMessage.error(response.msg || '评论发表失败')
    }
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    submittingComment.value = false
  }
}

// 提交回复
const submitReply = async (comment) => {
  if (!comment.replyContent?.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  comment.submittingReply = true
  try {
    const replyData = {
      commentId: comment.commentId, // 一级评论的ID
      userId: comment.targetUserId || comment.userId, // 被回复用户的ID
      commentContent: comment.replyContent.trim()
    }

    console.log('提交回复数据:', replyData)
    const response = await addReplyComment(replyData)
    if (response.code === 200) {
      ElMessage.success('回复发表成功')
      comment.showReplyForm = false
      comment.replyContent = ''
      comment.targetUserId = ''
      comment.targetUserName = ''
      
      // 如果回复列表已展开，重新加载回复
      if (comment.showReplies) {
        await loadReplies(comment)
      } else {
        // 标记有回复并展开
        comment.hasReplies = true
        comment.showReplies = true
        await loadReplies(comment)
      }
    } else {
      ElMessage.error(response.msg || '回复发表失败')
    }
  } catch (error) {
    console.error('发表回复失败:', error)
    ElMessage.error('发表回复失败')
  } finally {
    comment.submittingReply = false
  }
}

// 切换评论点赞
const toggleCommentLike = async (comment) => {
  try {
    const response = await likeComment(comment.commentId)
    if (response.code === 200) {
      comment.liked = !comment.liked
      comment.commentGoodNumber += comment.liked ? 1 : -1
      ElMessage.success(comment.liked ? '点赞成功' : '取消点赞')
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('点赞评论失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除评论
const deleteCommentHandler = async (comment) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？删除后无法恢复。',
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteComment(comment.commentId)
    if (response.code === 200) {
      ElMessage.success('评论删除成功')
      // 重新加载评论列表
      await loadComments()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 检查关注状态
const checkFollowStatus = async () => {
  try {
    const response = await isFollow(article.value.userId)
    if (response.code === 200) {
      isFollowing.value = response.data
    }
  } catch (error) {
    console.error('检查关注状态失败:', error)
  }
}

// 切换关注状态
const toggleFollow = async () => {
  followLoading.value = true
  try {
    const response = await followUser(article.value.userId)
    if (response.code === 200) {
      isFollowing.value = !isFollowing.value
      ElMessage.success(isFollowing.value ? '关注成功' : '取消关注成功')
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('关注操作失败:', error)
    ElMessage.error('操作失败，请重试')
  } finally {
    followLoading.value = false
  }
}

// 处理点赞
const toggleLike = async () => {
  try {
    const response = await likeArticle(article.value.articleId)
    if (response.code === 200) {
      article.value.liked = !article.value.liked
      article.value.articleGoodNumber += article.value.liked ? 1 : -1
      ElMessage.success(article.value.liked ? '点赞成功' : '取消点赞')
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 处理收藏
const toggleCollect = async () => {
  try {
    const response = await collectArticle(article.value.articleId)
    if (response.code === 200) {
      article.value.collected = !article.value.collected
      article.value.articleCollectionNumber += article.value.collected ? 1 : -1
      ElMessage.success(article.value.collected ? '收藏成功' : '取消收藏')
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 跳转到用户主页
const goToUserProfile = (userId) => {
  if (userId) {
    router.push(`/users/profile/${userId}`)
  }
}

// 滚动监听
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight
  
  // 当滚动到距离底部200px时开始加载更多评论
  if (scrollTop + windowHeight >= documentHeight - 200) {
    loadComments(true)
  }
}

// 组件挂载时获取文章详情
onMounted(async () => {
  await getCurrentUser()
  await fetchArticleDetail()
  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除滚动监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.article-detail-page {
  min-height: 100vh;
  background: var(--el-bg-color-page);
}

.article-container {
  max-width: 800px;
  margin: 0 auto;
  background: var(--el-bg-color);
  min-height: 100vh;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
}

/* 头部导航 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-btn {
  color: var(--el-text-color-primary);
  padding: 8px;
}

.back-btn:hover {
  background: var(--el-fill-color-light);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  cursor: pointer;
  transition: transform 0.3s ease;
}

.author-avatar:hover {
  transform: scale(1.05);
}

.author-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.author-name {
  font-weight: 600;
  color: var(--el-text-color-primary);
  font-size: 16px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.author-name:hover {
  color: var(--el-color-primary);
}

.publish-time {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.header-right {
  display: flex;
  align-items: center;
}

.follow-btn {
  min-width: 80px;
  border-radius: 20px;
  font-weight: 500;
}

/* 文章内容 */
.article-content {
  padding: 32px 24px;
}

.article-title {
  margin: 0 0 24px 0;
  font-size: 28px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  line-height: 1.3;
  word-break: break-word;
}

.article-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 32px;
}

.tag-item {
  transition: all 0.3s ease;
  cursor: pointer;
}

.tag-item:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.article-text {
  margin-bottom: 40px;
  padding: 24px 0;
  border-top: 1px solid var(--el-border-color-extra-light);
  border-bottom: 1px solid var(--el-border-color-extra-light);
}

.article-text p {
  font-size: 16px;
  line-height: 1.8;
  color: var(--el-text-color-primary);
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 文章操作区 */
.article-actions {
  padding-top: 24px;
  border-top: 2px solid var(--el-border-color-light);
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 20px;
  border-radius: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 80px;
}

.action-btn:hover {
  background: var(--el-fill-color-light);
  transform: translateY(-2px);
}

.like-btn.liked {
  color: #ff4757;
  background: rgba(255, 71, 87, 0.1);
}

.like-btn.liked:hover {
  background: rgba(255, 71, 87, 0.15);
}

.collect-btn.collected {
  color: var(--el-color-warning);
  background: var(--el-color-warning-light-9);
}

.collect-btn.collected:hover {
  background: var(--el-color-warning-light-8);
}

.comment-btn {
  color: var(--el-color-primary);
}

.comment-btn:hover {
  background: var(--el-color-primary-light-9);
}

/* 评论区域 */
.comments-section {
  padding: 32px 24px;
  border-top: 8px solid var(--el-border-color-extra-light);
  background: var(--el-bg-color);
}

.comments-header h3 {
  margin: 0 0 24px 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 评论表单 */
.comment-form-container {
  margin-bottom: 32px;
}

.comment-form {
  background: var(--el-fill-color-extra-light);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid var(--el-border-color-lighter);
}

.comment-input-area,
.reply-input-area {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.user-avatar {
  flex-shrink: 0;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.input-wrapper {
  flex: 1;
}

.comment-textarea,
.reply-textarea {
  margin-bottom: 12px;
}

.comment-textarea :deep(.el-textarea__inner),
.reply-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid var(--el-border-color-light);
  font-size: 14px;
  line-height: 1.6;
  resize: none;
}

.comment-actions,
.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 评论列表 */
.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  background: var(--el-bg-color);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid var(--el-border-color-lighter);
  transition: all 0.3s ease;
}

.comment-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-color: var(--el-border-color-light);
}

.comment-main {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.comment-avatar,
.reply-avatar {
  flex-shrink: 0;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.comment-avatar:hover,
.reply-avatar:hover {
  transform: scale(1.05);
}

.comment-content-area,
.reply-content-area {
  flex: 1;
  min-width: 0;
}

.comment-header,
.reply-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.comment-username,
.reply-username {
  font-weight: 600;
  color: var(--el-text-color-primary);
  cursor: pointer;
  transition: color 0.3s ease;
}

.comment-username:hover,
.reply-username:hover {
  color: var(--el-color-primary);
}

.reply-target {
  color: var(--el-color-primary);
  font-size: 13px;
}

.comment-time,
.reply-time {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.comment-text,
.reply-text {
  margin-bottom: 12px;
  font-size: 15px;
  line-height: 1.6;
  color: var(--el-text-color-primary);
  word-break: break-word;
  white-space: pre-wrap;
}

.comment-footer,
.reply-footer {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.comment-like-btn,
.reply-like-btn,
.comment-reply-btn,
.reply-reply-btn,
.comment-expand-btn,
.comment-delete-btn,
.reply-delete-btn {
  font-size: 13px;
  padding: 4px 8px;
  height: auto;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.comment-like-btn.liked,
.reply-like-btn.liked {
  color: #ff4757;
}

.comment-expand-btn .el-icon {
  transition: transform 0.3s ease;
}

.comment-expand-btn .el-icon.rotated {
  transform: rotate(180deg);
}

/* 回复表单 */
.reply-form {
  margin-top: 16px;
  padding: 16px;
  background: var(--el-fill-color-extra-light);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
}

/* 回复列表 */
.replies-list {
  margin-top: 16px;
  padding: 16px;
  background: var(--el-fill-color-extra-light);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
}

.reply-item {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 16px;
}

.reply-item:last-child {
  margin-bottom: 0;
}

/* 加载更多 */
.load-more-replies {
  margin-top: 12px;
  text-align: center;
}

.load-more-btn {
  font-size: 13px;
  color: var(--el-color-primary);
}

.loading-more-comments {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  padding: 32px 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.no-more-comments {
  padding: 20px 0;
  text-align: center;
}

.empty-comments {
  padding: 40px 0;
  text-align: center;
}

.article-not-found {
  padding: 60px 24px;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-container {
    margin: 0;
    box-shadow: none;
  }
  
  .article-header {
    padding: 12px 16px;
  }
  
  .header-left {
    gap: 12px;
  }
  
  .article-content {
    padding: 24px 16px;
  }
  
  .comments-section {
    padding: 24px 16px;
  }
  
  .article-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .action-buttons {
    flex-direction: row;
    justify-content: space-around;
    gap: 16px;
  }
  
  .action-btn {
    flex: 1;
    min-width: auto;
    padding: 12px 16px;
  }
  
  .comment-form {
    padding: 16px;
  }
  
  .comment-item {
    padding: 16px;
  }
  
  .comment-input-area,
  .reply-input-area {
    gap: 10px;
  }
  
  .comment-header,
  .reply-header {
    gap: 8px;
  }
  
  .comment-footer,
  .reply-footer {
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .article-header {
    padding: 10px 12px;
  }
  
  .article-content {
    padding: 20px 12px;
  }
  
  .comments-section {
    padding: 20px 12px;
  }
  
  .article-title {
    font-size: 20px;
  }
  
  .action-buttons {
    gap: 8px;
  }
  
  .action-btn {
    padding: 10px 12px;
    font-size: 14px;
  }
  
  .comment-form {
    padding: 12px;
  }
  
  .comment-item {
    padding: 12px;
  }
  
  .comment-input-area,
  .reply-input-area {
    flex-direction: column;
    gap: 8px;
  }
  
  .user-avatar {
    align-self: flex-start;
  }
}
</style>