<template>
    <div class="following-list-page">
        <!-- 用户列表 -->
        <div class="user-list" v-loading="loading">
            <!-- 空状态 -->
            <div v-if="followingList.length === 0 && !loading" class="empty-state">
                <el-empty description="暂无粉丝" :image-size="80" />
            </div>

            <!-- 用户项 -->
            <div v-for="user in followingList" :key="user.userId" class="user-item">
                <!-- 用户信息 -->
                <div class="user-info" @click="goToUserProfile(user.userId)">
                    <el-avatar :size="60" :src="user.userAvatar" class="user-avatar">
                        {{ user.userName?.charAt(0) }}
                    </el-avatar>
                    <div class="user-details">
                        <div class="user-name">{{ user.userName }}</div>
                        <div class="user-id">@{{ user.userId }}</div>
                    </div>
                </div>

                <!-- 关注按钮 -->
                <div class="user-actions">
                    <el-button :type="user.following ? 'primary' : '#fff'" :loading="user.loading"
                        @click="toggleFollow(user)" class="follow-btn" size="large" plain>
                        {{ user.following ? '已关注' : '关注' }}
                    </el-button>
                </div>
            </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > pageSize" class="pagination-wrapper">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total"
                :page-sizes="[10, 20, 50]" layout="prev, pager, next" small @size-change="handlePageSizeChange"
                @current-change="handlePageChange" class="pagination" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { follower, followUser } from '@/api/user'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const followingList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取关注列表
const fetchFolloerList = async () => {
    loading.value = true
    try {
        console.log('获取粉丝列表:', { pageNum: currentPage.value, pageSize: pageSize.value })

        const response = await follower(currentPage.value, pageSize.value)

        if (response.code === 200) {
            // 为每个用户添加loading状态
            followingList.value = (response.data?.records || response.data || []).map(user => ({
                ...user,
                loading: false
            }))

            // 设置总数
            total.value = response.data?.total || response.total || followingList.value.length

            console.log('粉丝列表数据:', followingList.value)
            console.log('总数:', total.value)
        } else {
            ElMessage.error(response.msg || '获取粉丝列表失败')
        }
    } catch (error) {
        console.error('获取粉丝列表失败:', error)
        ElMessage.error('获取粉丝列表失败')
    } finally {
        loading.value = false
    }
}

// 切换关注状态
const toggleFollow = async (user) => {
    console.log('=== toggleFollow 开始 ===')
    console.log('用户信息:', JSON.stringify(user, null, 2))
    
    // 防止重复点击
    if (user.loading) {
        console.log('正在处理中，忽略重复点击')
        return
    }
    
    let needConfirm = user.following
    console.log('是否需要确认:', needConfirm)
    
    // 如果是取消关注，需要二次确认
    if (needConfirm) {
        let confirmed = false
        try {
            await ElMessageBox.confirm(
                `确定要取消关注 ${user.userName} 吗？`,
                '取消关注',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            )
            confirmed = true
            console.log('用户确认了操作')
        } catch (error) {
            console.log('用户取消了操作:', error)
            return
        }
        
        if (!confirmed) {
            console.log('未确认，退出操作')
            return
        }
    }

    console.log('=== 开始 API 调用 ===')
    
    // 设置加载状态
    user.loading = true
    
    try {
        console.log('调用 followUser，参数:', user.userId)
        const response = await followUser(user.userId)
        console.log('API 响应:', response)
        
        if (response && response.code === 200) {
            // 更新状态
            const oldStatus = user.following
            user.following = !user.following
            
            console.log('状态更新:', oldStatus, '->', user.following)
            
            // 显示成功消息
            const message = user.following ? `已关注 ${user.userName}` : `已取消关注 ${user.userName}`
            ElMessage.success(message)
            console.log('操作成功:', message)
        } else {
            console.error('API 错误响应:', response)
            ElMessage.error(response?.msg || '操作失败')
        }
    } catch (error) {
        console.error('API 调用异常:', error)
        ElMessage.error('操作失败，请重试')
    } finally {
        console.log('重置 loading 状态')
        user.loading = false
        console.log('=== toggleFollow 结束 ===')
    }
}

// 跳转到用户主页
const goToUserProfile = (userId) => {
    if (userId) {
        router.push(`/users/profile/${userId}`)
    }
}

// 分页处理
const handlePageChange = () => {
    fetchFolloerList()
}

const handlePageSizeChange = () => {
    currentPage.value = 1
    fetchFolloerList()
}

// 组件挂载时获取数据
onMounted(() => {
    fetchFolloerList()
})
</script>

<style scoped>
.following-list-page {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: var(--el-bg-color);
}

/* 用户列表 */
.user-list {
    flex: 1;
    padding: 16px 0;
    overflow-y: auto;
}

.empty-state {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 300px;
    padding: 20px;
}

.empty-state .el-empty {
    padding: 20px;
}

/* 用户项 */
.user-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    border-bottom: 1px solid var(--el-border-color-extra-light);
    transition: background-color 0.3s ease;
}

.user-item:hover {
    background: var(--el-fill-color-extra-light);
}

.user-item:last-child {
    border-bottom: none;
}

/* 用户信息 */
.user-info {
    display: flex;
    align-items: center;
    gap: 16px;
    flex: 1;
    cursor: pointer;
    transition: all 0.3s ease;
}

.user-info:hover .user-name {
    color: var(--el-color-primary);
}

.user-avatar {
    flex-shrink: 0;
    transition: transform 0.3s ease;
}

.user-info:hover .user-avatar {
    transform: scale(1.05);
}

.user-details {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;
    /* 垂直居中对齐 */
    height: 60px;
    /* 与头像高度匹配 */
}

.user-name {
    font-size: 18px;
    font-weight: 600;
    color: var(--el-text-color-primary);
    margin-bottom: 4px;
    transition: color 0.3s ease;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    list-style: 1.4;
}

.user-id {
    font-size: 14px;
    color: var(--el-text-color-secondary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.3;
    /* 优化行高 */
}

/* 用户操作 */
.user-actions {
    flex-shrink: 0;
    margin-left: 12px;
}

.follow-btn {
    min-width: 80px;
    height: 36px;
    border-radius: 18px;
    font-size: 14px;
    font-weight: 500;
    /* 加粗字体 */
    transition: all 0.3s ease;
    margin-left: 16px;
    /* 增加与用户信息的间距 */
}

.follow-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 分页 */
.pagination-wrapper {
    padding: 16px 20px;
    border-top: 1px solid var(--el-border-color-extra-light);
    background: var(--el-bg-color);
}

.pagination {
    display: flex;
    justify-content: center;
}

.pagination :deep(.el-pagination) {
    padding: 0;
}

.pagination :deep(.el-pagination .el-pager li) {
    min-width: 28px;
    height: 28px;
    line-height: 28px;
    margin: 0 2px;
    border-radius: 4px;
}

.pagination :deep(.el-pagination .btn-prev),
.pagination :deep(.el-pagination .btn-next) {
    width: 28px;
    height: 28px;
    line-height: 28px;
    border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .user-item {
        padding: 10px 16px;
    }

    .user-info {
        gap: 10px;
    }

    .user-name {
        font-size: 13px;
    }

    .user-id {
        font-size: 11px;
    }

    .follow-btn {
        min-width: 60px;
        height: 28px;
        font-size: 11px;
    }

    .pagination-wrapper {
        padding: 12px 16px;
    }
}

@media (max-width: 480px) {
    .user-item {
        padding: 8px 12px;
    }

    .user-avatar {
        width: 36px !important;
        height: 36px !important;
    }

    .user-details {
        margin-left: 8px;
    }

    .follow-btn {
        min-width: 50px;
        height: 24px;
        font-size: 10px;
        padding: 0 8px;
    }
}

/* 滚动条样式 */
.user-list::-webkit-scrollbar {
    width: 4px;
}

.user-list::-webkit-scrollbar-track {
    background: var(--el-fill-color-extra-light);
}

.user-list::-webkit-scrollbar-thumb {
    background: var(--el-border-color);
    border-radius: 2px;
}

.user-list::-webkit-scrollbar-thumb:hover {
    background: var(--el-border-color-dark);
}

/* 加载状态 */
.user-item .follow-btn[loading] {
    pointer-events: none;
}

/* 动画效果 */
.user-item {
    animation: fadeInUp 0.3s ease-out;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>