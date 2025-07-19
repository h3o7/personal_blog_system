<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
    Search,
    Refresh,
    Plus,
    Edit,
    Delete,
    User,
    Clock,
    Lock,
    Unlock
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {deleteById,freezeById,queryInfo,editUser, addUser} from '@/api/user'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const userList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const userFormRef = ref()
// const dialogTitle = ref('用户信息栏')

// 搜索表单
const searchForm = reactive({
    userId: '',
    userName: ''
})

// 分页参数
const pagination = reactive({
    currentPage: 1,
    pageSize: 10
})

// 用户表单
const userForm = reactive({
    userId: '',
    userName: '',
    userPassword: '',
    userFrozen: 0
})

// 表单验证规则
const userFormRules = {
    userId: [
        { required: true, message: '请输入用户ID', trigger: 'blur' }
    ],
    userName: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    userPassword: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
    ]
}


// 获取用户列表
const getUserList = async () => {
    loading.value = true
    try {
        const params = {
            pageNum: pagination.currentPage,
            pageSize: pagination.pageSize,
            ...searchForm
        }

        // 假设你的API接口是 /api/users
        const response = await queryInfo(params);

        if (response.code === 200) {
            userList.value = response.data.rows
            total.value = response.data.total
        } else {
            ElMessage.error(response.msg || '获取用户列表失败')
        }
    } catch (error) {
        console.error('获取用户列表失败:', error)
        ElMessage.error('获取用户列表失败')
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
    pagination.currentPage = 1
    getUserList()
}

// 重置
const handleReset = () => {
    searchForm.userId = ''
    searchForm.userName = ''
    pagination.currentPage = 1
    getUserList()
}

// 新增用户
const handleAdd = () => {
    isEdit.value = false
    dialogVisible.value = true
    resetUserForm()
}

// 编辑用户
const handleEdit = (row) => {
    isEdit.value = true
    dialogVisible.value = true
    Object.assign(userForm, {
        userId: row.userId,
        userName: row.userName,
        userPassword: row.userPassword,
        userFrozen: row.userFrozen
    })
}

// 切换冻结状态
const handleToggleFreeze = async (row) => {
    const action = row.userFrozen === 0 ? '冻结' : '解冻'
    try {
        await ElMessageBox.confirm(
            `确认要${action}用户 "${row.userName}" 吗？`,
            '确认操作',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )

        // 假设你的API接口是 /api/users/toggle-freeze
        const response = await freezeById(row.userId)

        if (response.code === 200) {
            ElMessage.success(`${action}成功`)
            getUserList()
        } else {
            ElMessage.error(response.msg || `${action}失败`)
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error(`${action}用户失败:`, error)
            ElMessage.error(`${action}失败`)
        }
    }
}

// 删除用户
const handleDelete = async (row) => {
    try {
        await ElMessageBox.confirm(
            `确认要删除用户 "${row.userName}" 吗？此操作不可恢复！`,
            '确认删除',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error',
            }
        )

        // 假设你的API接口是 /api/users
        const response = await deleteById(row.userId)

        if (response.code === 200) {
            ElMessage.success('删除成功')
            getUserList()
        } else {
            ElMessage.error(response.msg || '删除失败')
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除用户失败:', error)
            ElMessage.error('删除失败')
        }
    }
}

// 分页大小改变
const handleSizeChange = (val) => {
    pagination.pageSize = val
    pagination.currentPage = 1
    getUserList()
}

// 当前页改变
const handleCurrentChange = (val) => {
    pagination.currentPage = val
    getUserList()
}

// 重置用户表单
const resetUserForm = () => {
    Object.assign(userForm, {
        userId: '',
        userName: '',
        userPassword: '',
        userFrozen: 0
    })
    userFormRef.value?.clearValidate()
}

// 关闭对话框
const handleDialogClose = () => {
    dialogVisible.value = false
    resetUserForm()
}

// 提交表单
const handleSubmit = async () => {
    try {
        await userFormRef.value.validate()

        submitLoading.value = true

        let response
        if (isEdit.value) {
            // console.log(userForm)
            // 编辑用户
            response = await editUser(userForm)
        } else {
            // 新增用户
            
            response = await addUser(userForm)
        }

        if (response.code === 200) {
            ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
            handleDialogClose()
            getUserList()
        } else {
            ElMessage.error(response.msg || '操作失败')
        }
    } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('操作失败')
    } finally {
        submitLoading.value = false
    }
}

// 页面加载时获取数据
onMounted(() => {
    getUserList()
})
</script>

<template>
    <div class="users-page">
        <!-- 页面标题 -->


        <!-- 查询区域 -->
        <div class="search-section">
            <el-card class="search-card" shadow="hover">
                <div class="search-content">
                    <div class="search-layout">
                        <!-- 左侧搜索条件 -->
                        <div class="search-inputs">
                            <el-form :model="searchForm" inline class="search-form">
                                <el-form-item value="用户ID">
                                    <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable
                                        class="search-input" :prefix-icon="User" />
                                </el-form-item>
                                <el-form-item value="用户名">
                                    <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable
                                        class="search-input" :prefix-icon="Search" />
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
                            <el-button type="success" :icon="Plus" @click="handleAdd">
                                新增用户
                            </el-button>
                        </div>
                    </div>
                </div>
            </el-card>
        </div>


        <!-- 用户列表 -->
        <div class="table-section">
            <el-card class="table-card" shadow="hover">
                <template #header>
                    <div class="table-header">
                        <span class="table-title">用户列表</span>
                        <div class="table-info">
                            <el-tag type="info" effect="plain">
                                共 {{ total }} 条记录
                            </el-tag>
                        </div>
                    </div>
                </template>

                <el-table :data="userList" v-loading="loading" class="user-table" stripe
                    :header-cell-style="{ background: '#f8f9fa', color: '#2c3e50', fontWeight: '600' }"
                    :row-style="{ height: '60px' }">
                    <el-table-column type="index" label="序号" width="80" align="center" />

                    <el-table-column prop="userId" label="用户ID" min-width="120" show-overflow-tooltip>
                        <template #default="{ row }">
                            <el-tag type="primary" effect="plain" size="small">
                                {{ row.userId }}
                            </el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column prop="userName" label="用户名" min-width="150" show-overflow-tooltip>
                        <template #default="{ row }">
                            <div class="user-info">
                                <el-avatar :size="32" class="user-avatar">
                                    {{ row.userName.charAt(0).toUpperCase() }}
                                </el-avatar>
                                <span class="user-name">{{ row.userName }}</span>
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column prop="userFrozen" label="状态" width="120" align="center">
                        <template #default="{ row }">
                            <el-tag :type="row.userFrozen === 0 ? 'success' : 'danger'" effect="dark" size="small">
                                {{ row.userFrozen === 0 ? '正常' : '冻结' }}
                            </el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column prop="userRegisterTime" label="注册时间" width="180" align="center">
                        <template #default="{ row }">
                            <div class="time-info">
                                <el-icon class="time-icon">
                                    <Clock />
                                </el-icon>
                                <span>{{ formatTime(row.userRegisterTime) }}</span>
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column label="操作" width="200" align="center" fixed="right">
                        <template #default="{ row }">
                            <div class="action-buttons">
                                <el-tooltip content="编辑用户" placement="top">
                                    <el-button type="primary" :icon="Edit" size="small" circle
                                        @click="handleEdit(row)" />
                                </el-tooltip>
                                <el-tooltip :content="row.userFrozen === 0 ? '冻结用户' : '解冻用户'" placement="top">
                                    <el-button :type="row.userFrozen === 0 ? 'warning' : 'success'"
                                        :icon="row.userFrozen === 0 ? Lock : Unlock" size="small" circle
                                        @click="handleToggleFreeze(row)" />
                                </el-tooltip>
                                <el-tooltip content="删除用户" placement="top">
                                    <el-button type="danger" :icon="Delete" size="small" circle
                                        @click="handleDelete(row)" />
                                </el-tooltip>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>

                <!-- 分页 -->
                <div class="pagination-section">
                    <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
                        :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        class="custom-pagination" />
                </div>
            </el-card>
        </div>

        <!-- 编辑用户对话框 -->
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" :before-close="handleDialogClose"
            class="user-dialog">
            <el-form ref="userFormRef" :model="userForm" :rules="userFormRules" value-width="80px" class="user-form">
                <el-form-item value="用户ID" prop="userId">
                    <el-input v-model="userForm.userId" :disabled="isEdit" placeholder="请输入用户ID" />
                </el-form-item>
                <el-form-item value="用户名" prop="userName">
                    <el-input v-model="userForm.userName" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item value="密码" prop="userPassword">
                    <el-input v-model="userForm.userPassword" type="password" placeholder="请输入密码" show-password />
                </el-form-item>
                <el-form-item value="状态" prop="userFrozen">
                    <el-radio-group v-model="userForm.userFrozen">
                        <el-radio :value="0">正常</el-radio>
                        <el-radio :value="1">冻结</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="handleDialogClose">取消</el-button>
                    <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
                        确定
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>



<style scoped>
/* 页面容器 - 设置整体布局和背景 */
.users-page {
    padding: 0;
    /* 移除默认内边距 */
    background: transparent;
    /* 设置背景透明，使用父组件背景 */
}

/* 页面标题区域 */
.page-header {
    margin-bottom: 30px;
    /* 设置底部外边距 */
    padding: 20px 0;
    /* 设置上下内边距 */
    text-align: center;
    /* 文字居中对齐 */
}

/* 页面主标题样式 */
.page-title {
    font-size: 2.2rem;
    /* 设置字体大小 */
    font-weight: 700;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色为深蓝灰色 */
    margin: 0 0 10px 0;
    /* 设置外边距 */
    background: linear-gradient(135deg, #2c3e50 0%, #667eea 100%);
    /* 设置渐变背景 */
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

.search-layout {
    display: flex;
    /* 使用flex布局 */
    justify-content: space-between;
    /* 两端对齐 */
    align-items: center;
    /* 垂直居中 */
    gap: 20px;
    /* 左右间距 */
    flex-wrap: wrap;
    /* 响应式换行 */
}

.search-actions {
    display: flex;
    /* 按钮横向排列 */
    gap: 12px;
    /* 按钮间距 */
    flex-shrink: 0;
    /* 防止被压缩 */
    align-items: center;
    /* 垂直居中 */
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
    padding: 10px 0;
    /* 设置上下内边距 */
}

/* 搜索表单样式 */
.search-form {
    margin: 0;
    /* 移除默认外边距 */
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
}

/* 表格标题样式 */
.table-title {
    font-size: 1.2rem;
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

/* 用户信息展示区域 */
.user-info {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 10px;
    /* 设置头像和姓名间距 */
}

/* 用户头像样式 */
.user-avatar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    /* 设置渐变背景 */
    color: white;
    /* 设置文字颜色为白色 */
    font-weight: 600;
    /* 设置字体粗细 */
}

/* 用户名样式 */
.user-name {
    font-weight: 500;
    /* 设置字体粗细 */
    color: #2c3e50;
    /* 设置文字颜色 */
}

/* 时间信息展示区域 */
.time-info {
    display: flex;
    /* 使用flex布局 */
    align-items: center;
    /* 垂直居中 */
    gap: 5px;
    /* 设置图标和文字间距 */
    color: #7f8c8d;
    /* 设置文字颜色为灰色 */
    font-size: 0.9rem;
    /* 设置字体大小 */
}

/* 时间图标样式 */
.time-icon {
    font-size: 14px;
    /* 设置图标大小 */
}

/* 操作按钮容器 */
.action-buttons {
    display: flex;
    /* 使用flex布局 */
    gap: 8px;
    /* 设置按钮间距 */
    justify-content: center;
    /* 水平居中 */
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

/* 用户对话框样式 */
:deep(.user-dialog .el-dialog) {
    border-radius: 15px;
    /* 设置对话框圆角 */
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    /* 设置对话框阴影 */
}

/* 用户表单样式 */
.user-form {
    padding: 20px 0;
    /* 设置上下内边距 */
}

/* 对话框底部样式 */
.dialog-footer {
    text-align: right;
    /* 右对齐 */
}

/* 用户表格自定义样式 */
:deep(.user-table) {
    border-radius: 10px;
    /* 设置表格圆角 */
    overflow: hidden;
    /* 隐藏超出部分 */
}

/* 表格行悬停效果 */
:deep(.user-table .el-table__body-wrapper .el-table__row:hover) {
    background-color: rgba(102, 126, 234, 0.05);
    /* 悬停时设置背景色 */
}

/* 响应式设计 - 平板设备 */
@media (max-width: 768px) {
    .page-title {
        font-size: 1.8rem;
        /* 减小标题字体大小 */
    }

    .search-input {
        width: 150px;
        /* 减小输入框宽度 */
    }

    .search-form {
        flex-direction: column;
        /* 搜索表单垂直排列 */
        gap: 10px;
        /* 设置表单项间距 */
    }

    .action-buttons {
        flex-direction: column;
        /* 操作按钮垂直排列 */
        gap: 5px;
        /* 减小按钮间距 */
    }
}

/* 响应式设计 - 手机设备 */
@media (max-width: 480px) {
    .page-header {
        padding: 15px 0;
        /* 减少页面头部内边距 */
    }

    .page-title {
        font-size: 1.5rem;
        /* 进一步减小标题字体 */
    }

    .search-content {
        padding: 5px 0;
        /* 减少搜索内容内边距 */
    }

    .table-header {
        flex-direction: column;
        /* 表格头部垂直排列 */
        gap: 10px;
        /* 设置间距 */
        text-align: center;
        /* 居中对齐 */
    }
}
</style>