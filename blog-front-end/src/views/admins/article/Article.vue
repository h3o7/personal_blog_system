<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import {
    Search,
    Refresh,
    Delete,
    User,
    Clock,
    Document,
    View,
    Star,
    Close
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryList, deleteArticle } from '@/api/article' // 假设你的API文件路径


// 响应式数据
const loading = ref(false)
const articleList = ref([])
const total = ref(0)
const contentDialogVisible = ref(false)
const currentArticle = ref({})

// 搜索表单
const searchForm = reactive({
    articleTitle: '',
    userName: ''
})

// 分页参数
const pagination = reactive({
    pageNumber: 1,
    pageSize: 10
})

// 文章对象（用于API请求）
const article = reactive({
    articleId: '',
    articleTypeName: '',
    userName: '',
    articleTitle: '',
    articleAddTime: '',
    articleContent: '',
    articleGoodNumber: '',
    articleLookNumber: '',
    articleCollectionNumber: '',
    pageNumber: 1,
    pageSize: 10
})

// 模拟数据
const mockArticles = []

// 获取文章列表
const getArticleList = async () => {
    loading.value = true
    try {
        // 构建请求参数
        Object.assign(article, {
            articleTitle: searchForm.articleTitle,
            userName: searchForm.userName,
            pageNumber: pagination.pageNumber,
            pageSize: pagination.pageSize
        })

        // 调用API
        const response = await queryList(article)

        if (response.code === 200) {
            articleList.value = response.data.rows || []
            total.value = response.data.total || 0
        } else {
            ElMessage.error(response.msg || '获取文章列表失败')
            // 使用模拟数据作为fallback
            articleList.value = mockArticles
            total.value = mockArticles.length
        }
    } catch (error) {
        console.error('获取文章列表失败:', error)
        ElMessage.error('获取文章列表失败，使用模拟数据')
        // 使用模拟数据作为fallback
        articleList.value = mockArticles
        total.value = mockArticles.length
    } finally {
        loading.value = false
    }
}

// 格式化时间
const formatTime = (timeString) => {
    if (!timeString) return '-'
    const date = new Date(timeString)
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    }).replace(/\//g, '-')
}

// 搜索
const handleSearch = () => {
    pagination.pageNumber = 1
    getArticleList()
}

// 重置
const handleReset = () => {
    searchForm.articleTitle = ''
    searchForm.userName = ''
    pagination.pageNumber = 1
    getArticleList()
}

// 查看文章内容
const handleViewContent = (row) => {
    currentArticle.value = { ...row }
    contentDialogVisible.value = true
}

// 删除文章
const handleDelete = async (row) => {
    try {
        await ElMessageBox.confirm(
            `确认要删除文章 "${row.articleTitle}" 吗？此操作不可恢复！`,
            '确认删除',
            {
                confirmButtonText: '确定删除',
                cancelButtonText: '取消',
                type: 'error',
                center: true
            }
        )

        // 调用删除API
        const response = await deleteArticle(row.articleId)

        if (response.code === 200) {
            ElMessage.success('删除成功')
            // 重新获取列表
            getArticleList()
        } else {
            ElMessage.error(response.msg || '删除失败')
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除文章失败:', error)
            ElMessage.error('删除失败')
        }
    }
}

// 分页大小改变
const handleSizeChange = (val) => {
    pagination.pageSize = val
    pagination.pageNumber = 1
    getArticleList()
}

// 当前页改变
const handleCurrentChange = (val) => {
    pagination.pageNumber = val
    getArticleList()
}

// 关闭内容对话框
const handleContentDialogClose = () => {
    contentDialogVisible.value = false
    currentArticle.value = {}
}

// 页面加载时获取数据
onMounted(() => {
    getArticleList()
})
</script>


<template>
    <div class="articles-page">
        <!-- 页面标题区域 -->
        <div class="page-header">
            <h1 class="page-title">文章管理</h1>
            <p class="page-subtitle">管理博客系统中的所有文章内容</p>
        </div>

        <!-- 搜索区域 -->
        <div class="search-section">
            <el-card class="search-card" shadow="hover">
                <div class="search-content">
                    <div class="search-layout">
                        <!-- 左侧搜索条件 -->
                        <div class="search-inputs">
                            <el-form :model="searchForm" inline class="search-form">
                                <el-form-item label="文章标题">
                                    <el-input v-model="searchForm.articleTitle" placeholder="请输入文章标题" clearable
                                        class="search-input" :prefix-icon="Document" />
                                </el-form-item>
                                <el-form-item label="作者姓名">
                                    <el-input v-model="searchForm.userName" placeholder="请输入作者姓名" clearable
                                        class="search-input" :prefix-icon="User" />
                                </el-form-item>
                            </el-form>
                        </div>

                        <!-- 右侧操作按钮 -->
                        <div class="search-actions">
                            <el-button type="primary" :icon="Search" @click="handleSearch">
                                搜索
                            </el-button>
                            <el-button :icon="Refresh" @click="handleReset">
                                重置
                            </el-button>
                        </div>
                    </div>
                </div>
            </el-card>
        </div>

        <!-- 文章列表 -->
        <div class="table-section">
            <el-card class="table-card" shadow="hover">
                <template #header>
                    <div class="table-header">
                        <div class="header-left">
                            <el-icon class="header-icon">
                                <Document />
                            </el-icon>
                            <span class="table-title">文章列表</span>
                        </div>
                        <div class="table-info">
                            <el-tag type="info" effect="plain" size="large">
                                共 {{ total }} 篇文章
                            </el-tag>
                        </div>
                    </div>
                </template>

                <el-table :data="articleList" v-loading="loading" class="article-table" stripe :header-cell-style="{
                    background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
                    color: '#2c3e50',
                    fontWeight: '600',
                    fontSize: '15px',
                    height: '55px'
                }" :row-style="{ height: '70px' }" :cell-style="{ padding: '15px 0' }">
                    <!-- 文章标题列 -->
                    <el-table-column prop="articleTitle" label="文章标题" width="270" show-overflow-tooltip>
                        <template #default="{ row }">
                            <div class="article-title-cell">
                                <el-icon class="title-icon">
                                    <Document />
                                </el-icon>
                                <div class="title-content">
                                    <span class="title-text">{{ row.articleTitle }}</span>
                                    <div class="title-meta">
                                        <el-tag size="small" type="primary" effect="plain">
                                            {{ row.articleTypeName }}
                                        </el-tag>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column prop="articleTypeName" label="文章类型" width="150" align="center">
                        <template #default="{ row }">
                            <el-tag size="small" type="success" effect="plain">
                                {{ row.articleTypeName }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <!-- 作者列 -->
                    <el-table-column prop="userName" label="作者" width="170" align="center">
                        <template #default="{ row }">
                            <div class="author-cell">
                                <el-avatar :size="32" class="author-avatar">
                                    {{ row.userName.charAt(0).toUpperCase() }}
                                </el-avatar>
                                <span class="author-name">{{ row.userName }}</span>
                            </div>
                        </template>
                    </el-table-column>

                    <!-- 统计数据列 -->
                    <el-table-column label="统计数据" min-width="200" align="center">
                        <template #default="{ row }">
                            <div class="stats-cell">
                                <div class="stat-item">
                                    <el-icon class="stat-icon good-icon"><Pointer /></el-icon>
                                    <span class="stat-number">{{ row.articleGoodNumber }}</span>
                                </div>
                                <div class="stat-item">
                                    <el-icon class="stat-icon view-icon">
                                        <View />
                                    </el-icon>
                                    <span class="stat-number">{{ row.articleLookNumber }}</span>
                                </div>
                                <div class="stat-item">
                                    <el-icon class="stat-icon collection-icon">
                                        <Star />
                                    </el-icon>
                                    <span class="stat-number">{{ row.articleCollectionNumber }}</span>
                                </div>
                            </div>
                        </template>
                    </el-table-column>

                    <!-- 发布时间列 -->
                    <el-table-column prop="articleAddTime" label="发布时间" width="180" align="center">
                        <template #default="{ row }">
                            <div class="time-info">
                                <el-icon class="time-icon">
                                    <Clock />
                                </el-icon>
                                <span class="time-text">{{ formatTime(row.articleAddTime) }}</span>
                            </div>
                        </template>
                    </el-table-column>

                    <!-- 操作列 -->
                    <el-table-column label="操作" width="180" align="center" fixed="right">
                        <template #default="{ row }">
                            <div class="action-buttons">
                                <el-tooltip content="查看内容" placement="top">
                                    <el-button type="info" :icon="View" size="default" circle
                                        class="action-btn view-btn" @click="handleViewContent(row)" />
                                </el-tooltip>
                                <el-tooltip content="删除文章" placement="top">
                                    <el-button type="danger" :icon="Delete" size="default" circle
                                        class="action-btn delete-btn" @click="handleDelete(row)" />
                                </el-tooltip>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>

                <!-- 分页 -->
                <div class="pagination-section">
                    <el-pagination v-model:current-page="pagination.pageNumber" v-model:page-size="pagination.pageSize"
                        :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        class="custom-pagination" />
                </div>
            </el-card>
        </div>

        <!-- 文章内容查看对话框 -->
        <el-dialog v-model="contentDialogVisible" :title="currentArticle.articleTitle" width="900px"
            :before-close="handleContentDialogClose" class="content-dialog" center destroy-on-close>
            <!-- 对话框头部 -->
            <template #header="{ close, titleId, titleClass }">
                <div class="content-dialog-header">
                    <div class="content-dialog-title-wrapper">
                        <el-icon class="content-dialog-icon">
                            <Document />
                        </el-icon>
                        <div class="content-title-info">
                            <span :id="titleId" :class="titleClass" class="content-dialog-title">
                                {{ currentArticle.articleTitle }}
                            </span>
                            <div class="content-meta">
                                <el-tag size="small" type="primary">{{ currentArticle.articleTypeName }}</el-tag>
                                <span class="content-author">作者：{{ currentArticle.userName }}</span>
                                <span class="content-time">{{ formatTime(currentArticle.articleAddTime) }}</span>
                            </div>
                        </div>
                    </div>
                    <el-button type="danger" :icon="Close" circle size="small" class="content-close-btn"
                        @click="close" />
                </div>
            </template>

            <!-- 文章内容 -->
            <div class="content-dialog-body">
                <div class="content-stats">
                    <div class="content-stat-item">
                        <el-icon class="content-stat-icon like-icon">
                            <Like />
                        </el-icon>
                        <span>{{ currentArticle.articleGoodNumber }} 点赞</span>
                    </div>
                    <div class="content-stat-item">
                        <el-icon class="content-stat-icon view-icon">
                            <View />
                        </el-icon>
                        <span>{{ currentArticle.articleLookNumber }} 浏览</span>
                    </div>
                    <div class="content-stat-item">
                        <el-icon class="content-stat-icon collection-icon">
                            <Star />
                        </el-icon>
                        <span>{{ currentArticle.articleCollectionNumber }} 收藏</span>
                    </div>
                </div>

                <el-divider />

                <div class="article-content" v-html="currentArticle.articleContent">
                </div>
            </div>
        </el-dialog>
    </div>
</template>



<style scoped>
/* 页面容器样式 */
.articles-page {
    padding: 0;
    /* 移除默认内边距 */
    background: transparent;
    /* 设置背景透明 */
}

/* 页面标题区域 */
.page-header {
    margin-bottom: 30px;
    /* 设置底部外边距 */
    padding: 25px 0;
    /* 设置上下内边距 */
    text-align: center;
    /* 文字居中对齐 */
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.8) 100%);
    /* 设置半透明渐变背景 */
    border-radius: 20px;
    /* 设置圆角 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
    /* 添加轻微阴影 */
}

/* 页面主标题样式 */
.page-title {
    font-size: 2.2rem;
    /* 设置字体大小 */
    font-weight: 700;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
    margin: 0 0 10px 0;
    /* 设置外边距 */
    background: linear-gradient(135deg, #2c3e50 0%, #667eea 100%);
    /* 设置渐变文字效果 */
    background-clip: text;
    /* 将背景裁剪到文字 */
    -webkit-background-clip: text;
    /* Webkit浏览器兼容 */
    -webkit-text-fill-color: transparent;
    /* 设置文字透明显示背景 */
}

/* 页面副标题样式 */
.page-subtitle {
    font-size: 1rem;
    /* 设置字体大小 */
    color: #7f8c8d;
    /* 设置文字颜色为灰色 */
    margin: 0;
    /* 移除默认外边距 */
    font-weight: 500;
    /* 设置字体粗细 */
}

/* 搜索区域样式 */
.search-section {
    margin-bottom: 20px;
    /* 设置底部外边距 */
}

/* 搜索卡片样式 */
.search-card {
    border-radius: 15px;
    /* 设置圆角 */
    border: none;
    /* 移除边框 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    /* 设置阴影效果 */
    transition: all 0.3s ease;
    /* 设置过渡动画 */
}

/* 搜索卡片悬停效果 */
.search-card:hover {
    transform: translateY(-2px);
    /* 悬停时向上移动 */
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    /* 悬停时增强阴影 */
}

/* 搜索内容容器 */
.search-content {
    padding: 15px 0;
    /* 设置上下内边距 */
}

/* 搜索布局容器 */
.search-layout {
    display: flex;
    /* 使用flex布局 */
    justify-content: space-between;
    /* 两端对齐 */
    align-items: center;
    /* 垂直居中 */
    gap: 20px;
    /* 设置左右间距 */
    flex-wrap: wrap;
    /* 允许换行 */
}

/* 左侧搜索输入区域 */
.search-inputs {
    flex: 1;
    /* 占据剩余空间 */
    min-width: 300px;
    /* 设置最小宽度 */
}

/* 右侧操作按钮区域 */
.search-actions {
    display: flex;
    /* 使用flex布局 */
    gap: 12px;
    /* 设置按钮间距 */
    flex-shrink: 0;
    /* 防止按钮被压缩 */
}

/* 搜索表单样式 */
.search-form {
    margin: 0;
    /* 移除默认外边距 */
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    flex-wrap: wrap;
    /* 允许换行 */
    gap: 15px;
    /* 设置表单项间距 */
}

/* 搜索输入框样式 */
.search-input {
    width: 200px;
    /* 设置输入框宽度 */
}

/* 表格区域样式 */
.table-section {
    margin-bottom: 20px;
    /* 设置底部外边距 */
}

/* 表格卡片样式 */
.table-card {
    border-radius: 15px;
    /* 设置圆角 */
    border: none;
    /* 移除边框 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    /* 设置阴影效果 */
    transition: all 0.3s ease;
    /* 设置过渡动画 */
    overflow: hidden;
    /* 隐藏超出内容 */
}

/* 表格卡片悬停效果 */
.table-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    /* 悬停时增强阴影 */
}

/* 表格头部样式 */
.table-header {
    display: flex;
    /* 使用flex布局 */
    justify-content: space-between;
    /* 两端对齐 */
    align-items: center;
    /* 垂直居中 */
    padding: 10px 0;
    /* 设置上下内边距 */
}

/* 表格头部左侧 */
.header-left {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 10px;
    /* 设置图标和文字间距 */
}

/* 表格头部图标 */
.header-icon {
    font-size: 18px;
    /* 设置图标大小 */
    color: #667eea;
    /* 设置图标颜色 */
}

/* 表格标题样式 */
.table-title {
    font-size: 1.3rem;
    /* 设置字体大小 */
    font-weight: 600;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
}

/* 表格信息区域 */
.table-info {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 10px;
    /* 设置子元素间距 */
}

/* 文章标题单元格样式 */
.article-title-cell {
    display: flex;
    /* 使用flex布局 */
    align-items: flex-start;
    /* 顶部对齐 */
    gap: 12px;
    /* 设置图标和内容间距 */
}

/* 标题图标样式 */
.title-icon {
    font-size: 20px;
    /* 设置图标大小 */
    color: #667eea;
    /* 设置图标颜色 */
    margin-top: 2px;
    /* 设置顶部间距 */
    flex-shrink: 0;
    /* 防止图标被压缩 */
}

/* 标题内容区域 */
.title-content {
    display: flex;
    /* 使用flex布局 */
    flex-direction: column;
    /* 垂直排列 */
    gap: 6px;
    /* 设置标题和标签间距 */
}

/* 标题文字样式 */
.title-text {
    font-weight: 600;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
    font-size: 0.95rem;
    /* 设置字体大小 */
    line-height: 1.4;
    /* 设置行高 */
}

/* 标题元信息区域 */
.title-meta {
    display: flex;
    /* 使用flex布局 */
    gap: 8px;
    /* 设置标签间距 */
}

/* 作者单元格样式 */
.author-cell {
    display: flex;
    /* 使用flex布局 */
    flex-direction: column;
    /* 垂直排列 */
    align-items: center;
    /* 居中对齐 */
    gap: 6px;
    /* 设置头像和姓名间距 */
}

/* 作者头像样式 */
.author-avatar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    /* 设置渐变背景 */
    color: white;
    /* 设置文字颜色为白色 */
    font-weight: 600;
    /* 设置字体粗细 */
}

/* 作者姓名样式 */
.author-name {
    font-weight: 500;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
    font-size: 0.9rem;
    /* 设置字体大小 */
}

/* 统计数据单元格样式 */
.stats-cell {
    display: flex;
    /* 使用flex布局 */
    justify-content: center;
    /* 水平居中 */
    gap: 12px;
    /* 设置统计项间距 */
}

/* 统计项样式 */
.stat-item {
    display: flex;
    /* 使用flex布局 */
    flex-direction: column;
    /* 垂直排列 */
    align-items: center;
    /* 居中对齐 */
    gap: 6px;
    /* 设置图标和数字间距 */
}

/* 统计图标基础样式 */
.stat-icon {
    font-size: 20px;
    /* 设置图标大小 */
}

/* 点赞图标样式 */
.like-icon {
    color: #ff6b6b;
    /* 设置点赞图标颜色为红色 */
}

/* 浏览图标样式 */
.view-icon {
    color: #4ecdc4;
    /* 设置浏览图标颜色为青色 */
}

/* 收藏图标样式 */
.collection-icon {
    color: #feca57;
    /* 设置收藏图标颜色为黄色 */
}

/* 统计数字样式 */
.stat-number {
    font-weight: 600;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
    font-size: 0.85rem;
    /* 设置字体大小 */
}

/* 时间信息展示区域 */
.time-info {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */
    gap: 6px;
    /* 设置图标和文字间距 */
    color: #7f8c8d;
    /* 设置文字颜色为灰色 */
    font-size: 0.9rem;
    /* 设置字体大小 */
    white-space: nowrap;
    /* 强制单行显示 */
}

/* 时间图标样式 */
.time-icon {
    font-size: 14px;
    /* 设置图标大小 */
    color: #95a5a6;
    /* 设置图标颜色 */
    flex-shrink: 0;
    /* 防止图标被压缩 */
}

/* 时间文字样式 */
.time-text {
    font-weight: 500;
    /* 设置字体粗细 */
    white-space: nowrap;
    /* 防止文字换行 */
}

/* 操作按钮容器 */
.action-buttons {
    display: flex;
    /* 使用flex布局 */
    gap: 12px;
    /* 设置按钮间距 */
    justify-content: center;
    /* 水平居中 */
}

/* 操作按钮基础样式 */
.action-btn {
    width: 38px;
    /* 设置按钮宽度 */
    height: 38px;
    /* 设置按钮高度 */
    font-size: 16px;
    /* 设置图标字体大小 */
    transition: all 0.3s ease;
    /* 设置过渡动画 */
}

/* 查看按钮悬停效果 */
.view-btn:hover {
    transform: scale(1.1);
    /* 悬停时放大 */
    box-shadow: 0 4px 12px rgba(144, 147, 153, 0.4);
    /* 悬停时添加灰色阴影 */
}

/* 删除按钮悬停效果 */
.delete-btn:hover {
    transform: scale(1.1);
    /* 悬停时放大 */
    box-shadow: 0 4px 12px rgba(245, 101, 101, 0.4);
    /* 悬停时添加红色阴影 */
}

/* 分页区域样式 */
.pagination-section {
    margin-top: 20px;
    /* 设置顶部外边距 */
    display: flex;
    /* 使用flex布局 */
    justify-content: center;
    /* 水平居中 */
}

/* 自定义分页样式 */
.custom-pagination {
    --el-pagination-font-size: 14px;
    /* 设置分页字体大小 */
    --el-pagination-button-width: 35px;
    /* 设置分页按钮宽度 */
    --el-pagination-button-height: 35px;
    /* 设置分页按钮高度 */
}

/* 表格整体样式优化 */
:deep(.article-table) {
    border-radius: 10px;
    /* 设置表格圆角 */
    overflow: hidden;
    /* 隐藏超出部分 */
}

/* 表格行悬停效果 */
:deep(.article-table .el-table__body-wrapper .el-table__row:hover) {
    background-color: rgba(102, 126, 234, 0.05);
    /* 悬停时设置淡紫色背景 */
    transition: background-color 0.3s ease;
    /* 设置背景色过渡动画 */
}

/* 表格单元格内边距优化 */
:deep(.article-table .el-table__cell) {
    padding: 15px 12px;
    /* 设置单元格内边距 */
}

/* === 内容查看对话框样式 === */

/* 内容对话框整体样式 */
:deep(.content-dialog .el-dialog) {
    border-radius: 20px;
    /* 设置对话框圆角 */
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
    /* 添加深度阴影 */
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
    /* 设置渐变背景 */
    overflow: hidden;
    /* 隐藏超出内容 */
}

/* 内容对话框头部样式 */
.content-dialog-header {
    display: flex;
    /* 使用flex布局 */
    justify-content: space-between;
    /* 两端对齐 */
    align-items: flex-start;
    /* 顶部对齐 */
    padding: 25px 30px;
    /* 设置内边距 */
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    /* 设置头部渐变背景 */
    margin: -20px -20px 0 -20px;
    /* 扩展到对话框边缘 */
}

/* 内容对话框标题包装器 */
.content-dialog-title-wrapper {
    display: flex;
    /* 使用flex布局 */
    align-items: flex-start;
    /* 顶部对齐 */
    gap: 15px;
    /* 设置图标和标题间距 */
    flex: 1;
    /* 占据剩余空间 */
}

/* 内容对话框图标样式 */
.content-dialog-icon {
    width: 40px;
    /* 设置图标宽度 */
    height: 40px;
    /* 设置图标高度 */
    border-radius: 50%;
    /* 设置圆形背景 */
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */
    font-size: 20px;
    /* 设置图标字体大小 */
    color: #ffffff;
    /* 设置图标颜色为白色 */
    background: rgba(255, 255, 255, 0.2);
    /* 设置半透明白色背景 */
    backdrop-filter: blur(10px);
    /* 添加毛玻璃效果 */
    flex-shrink: 0;
    /* 防止图标被压缩 */
}

/* 内容标题信息区域 */
.content-title-info {
    display: flex;
    /* 使用flex布局 */
    flex-direction: column;
    /* 垂直排列 */
    gap: 8px;
    /* 设置标题和元信息间距 */
}

/* 内容对话框标题文字样式 */
.content-dialog-title {
    font-size: 1.4rem;
    /* 设置标题字体大小 */
    font-weight: 700;
    /* 设置字体粗细 */
    color: #ffffff;
    /* 设置文字颜色为白色 */
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    /* 添加文字阴影 */
    line-height: 1.3;
    /* 设置行高 */
}

/* 内容元信息样式 */
.content-meta {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 15px;
    /* 设置元素间距 */
    font-size: 0.9rem;
    /* 设置字体大小 */
    color: rgba(255, 255, 255, 0.9);
    /* 设置半透明白色文字 */
}

/* 内容作者样式 */
.content-author {
    font-weight: 500;
    /* 设置字体粗细 */
}

/* 内容时间样式 */
.content-time {
    font-weight: 500;
    /* 设置字体粗细 */
}

/* 内容关闭按钮样式 */
.content-close-btn {
    background: rgba(255, 255, 255, 0.15);
    /* 设置半透明背景 */
    border: 1px solid rgba(255, 255, 255, 0.3);
    /* 设置半透明边框 */
    color: #ffffff;
    /* 设置图标颜色为白色 */
    backdrop-filter: blur(10px);
    /* 添加毛玻璃效果 */
    transition: all 0.3s ease;
    /* 设置过渡动画 */
    margin-top: 5px;
    /* 设置顶部间距 */
}

/* 内容关闭按钮悬停效果 */
.content-close-btn:hover {
    background: rgba(255, 255, 255, 0.25);
    /* 悬停时增强背景 */
    transform: scale(1.1);
    /* 悬停时放大 */
}

/* 内容对话框主体样式 */
.content-dialog-body {
    padding: 30px;
    /* 设置内边距 */
    background: #ffffff;
    /* 设置背景为白色 */
    max-height: 70vh;
    /* 设置最大高度 */
    overflow-y: auto;
    /* 设置垂直滚动 */
}

/* 内容统计区域样式 */
.content-stats {
    display: flex;
    /* 使用flex布局 */
    justify-content: center;
    /* 水平居中 */
    gap: 30px;
    /* 设置统计项间距 */
    margin-bottom: 20px;
    /* 设置底部外边距 */
}

/* 内容统计项样式 */
.content-stat-item {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 8px;
    /* 设置图标和文字间距 */
    font-size: 0.95rem;
    /* 设置字体大小 */
    font-weight: 500;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
}

/* 内容统计图标样式 */
.content-stat-icon {
    font-size: 18px;
    /* 设置图标大小 */
}

/* 文章内容样式 */
.article-content {
    font-size: 1rem;
    /* 设置字体大小 */
    line-height: 1.8;
    /* 设置行高 */
    color: #2c3e50;
    /* 设置文字颜色 */
    text-align: left;
    /* 设置文字左对齐 */
}

/* 文章内容标题样式 */
.article-content h2 {
    color: #2c3e50;
    /* 设置标题颜色 */
    font-size: 1.4rem;
    /* 设置标题字体大小 */
    margin: 20px 0 15px 0;
    /* 设置标题外边距 */
    font-weight: 600;
    /* 设置字体粗细 */
}

.article-content h3 {
    color: #34495e;
    /* 设置小标题颜色 */
    font-size: 1.2rem;
    /* 设置小标题字体大小 */
    margin: 15px 0 10px 0;
    /* 设置小标题外边距 */
    font-weight: 600;
    /* 设置字体粗细 */
}

/* 文章内容段落样式 */
.article-content p {
    margin: 12px 0;
    /* 设置段落外边距 */
    text-indent: 0;
    /* 移除段落缩进 */
}

/* 文章内容列表样式 */
.article-content ul,
.article-content ol {
    margin: 12px 0;
    /* 设置列表外边距 */
    padding-left: 25px;
    /* 设置列表左内边距 */
}

.article-content li {
    margin: 6px 0;
    /* 设置列表项外边距 */
}

/* 文章内容代码样式 */
.article-content pre {
    background: #f8f9fa;
    /* 设置代码块背景色 */
    border: 1px solid #e9ecef;
    /* 设置代码块边框 */
    border-radius: 8px;
    /* 设置代码块圆角 */
    padding: 15px;
    /* 设置代码块内边距 */
    margin: 15px 0;
    /* 设置代码块外边距 */
    overflow-x: auto;
    /* 设置水平滚动 */
    font-family: 'Courier New', monospace;
    /* 设置等宽字体 */
    font-size: 0.9rem;
    /* 设置字体大小 */
}

.article-content code {
    background: #f1f3f4;
    /* 设置行内代码背景色 */
    padding: 2px 6px;
    /* 设置行内代码内边距 */
    border-radius: 4px;
    /* 设置行内代码圆角 */
    font-family: 'Courier New', monospace;
    /* 设置等宽字体 */
    font-size: 0.9rem;
    /* 设置字体大小 */
}

/* 响应式设计 - 平板设备 */
@media (max-width: 768px) {
    .page-title {
        font-size: 1.8rem;
        /* 减小标题字体大小 */
    }

    .search-layout {
        flex-direction: column;
        /* 搜索布局垂直排列 */
        align-items: stretch;
        /* 拉伸对齐 */
        gap: 15px;
        /* 调整间距 */
    }

    .search-inputs {
        min-width: auto;
        /* 移除最小宽度限制 */
    }

    .search-actions {
        justify-content: center;
        /* 按钮居中 */
    }

    .search-form {
        justify-content: center;
        /* 搜索表单居中 */
    }

    .search-input {
        width: 180px;
        /* 减小输入框宽度 */
    }

    .stats-cell {
        gap: 8px;
        /* 减少统计项间距 */
    }

    .action-btn {
        width: 35px;
        /* 减小按钮尺寸 */
        height: 35px;
        /* 减小按钮尺寸 */
    }

    /* 内容对话框在平板上的适配 */
    :deep(.content-dialog .el-dialog) {
        width: 95% !important;
        /* 对话框宽度调整为95% */
        margin: 3vh auto;
        /* 调整对话框边距 */
    }

    .content-dialog-body {
        padding: 20px;
        /* 减少对话框内容内边距 */
    }

    .content-stats {
        gap: 20px;
        /* 减少统计项间距 */
    }
}

/* 响应式设计 - 手机设备 */
@media (max-width: 480px) {
    .page-header {
        padding: 20px 15px;
        /* 减少页面头部内边距 */
    }

    .page-title {
        font-size: 1.5rem;
        /* 进一步减小标题字体 */
    }

    .search-content {
        padding: 10px 0;
        /* 减少搜索内容内边距 */
    }

    .search-form {
        flex-direction: column;
        /* 搜索表单垂直排列 */
        align-items: center;
        /* 居中对齐 */
    }

    .search-input {
        width: 160px;
        /* 进一步减小输入框宽度 */
    }

    .stats-cell {
        flex-direction: column;
        /* 统计数据垂直排列 */
        gap: 6px;
        /* 调整间距 */
    }

    .stat-item {
        flex-direction: row;
        /* 统计项水平排列 */
        gap: 6px;
        /* 调整间距 */
    }

    /* 内容对话框在手机上的适配 */
    .content-dialog-header {
        padding: 20px;
        /* 减少对话框头部内边距 */
    }

    .content-dialog-title {
        font-size: 1.2rem;
        /* 减小对话框标题字体 */
    }

    .content-dialog-body {
        padding: 15px;
        /* 进一步减少对话框内容内边距 */
    }

    .content-stats {
        flex-direction: column;
        /* 统计项垂直排列 */
        gap: 15px;
        /* 调整间距 */
    }

    .article-content {
        font-size: 0.95rem;
        /* 减小文章内容字体 */
    }
}
</style>