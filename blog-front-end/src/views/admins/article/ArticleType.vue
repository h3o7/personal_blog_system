<script setup>
import { ref, reactive, onMounted,computed} from 'vue'
import {
  Search,
  Refresh,
  Plus,
  Edit,
  Delete,
  User,
  Clock,
  Lock,
  Unlock,
  Document,
  List,
  Folder
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryInfo,addArticleType,editArticleType,deleteById} from '@/api/articleType'
// 响应式数据

const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const articleTypeFormRef = ref()
const loading = ref(false)
const total = ref(0)

const articleType = reactive({
  articleTypeId: '',
  articleTypeSort: '',
  articleTypeName: '',
  articleNum: '',
  articleTypeAddTime: ''
})

const articleTypeList = ref([
  {
    articleTypeSort: 1,
    articleTypeName: '文章类型1',
    articleNum: 6,
    articleTypeAddTime: '2016-05-03'
  }
])
const formRules = {
  articleTypeName: [
    { required: true, message: '请输入文章类型名称', trigger: 'blur' },
    { min: 2, max: 50, message: '类型名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  articleTypeSort: [
    { required: true, message: '请输入排序号', trigger: 'blur' },
    { type: 'number', min: 1, max: 999, message: '排序号必须在 1-999 之间', trigger: 'blur' }
  ]
}
const dialogTitle = computed(() => isEdit.value ? '编辑文章类型' : '新增文章类型')
// 格式化时间函数 - 调整为单行显示
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

const resetForm = () => {
  Object.assign(articleType, {
    articleTypeId: '',
    articleTypeSort: '',
    articleTypeName: '',
    articleNum: '',
    articleTypeAddTime: ''
  })
  articleTypeFormRef.value?.clearValidate()
}

// 处理函数
const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
  resetForm()
  // 设置默认排序号为当前最大排序号+1
  const maxSort = Math.max(...articleTypeList.value.map(item => item.articleTypeSort || 0))
  articleType.articleTypeSort = maxSort + 1
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(articleType, { ...row })
}

const handleDelete = async(row) => {
  try {
    await ElMessageBox.confirm(
      `确认要删除文章类型 "${row.articleTypeName}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error',
        center: true
      }
    )
    const response = await deleteById(row.articleTypeId);
    if (response.code === 200) {
            ElMessage.success('删除成功')
            getArticleTypeList()
        } else {
            ElMessage.error(response.msg || '删除失败')
        }
      
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handleSubmit = async () => {
  try {
    await articleTypeFormRef.value.validate()
    
    submitLoading.value = true
    
    if (isEdit.value) {
      // 编辑文章类型
      const response = await editArticleType(articleType)
      if (response.code === 200) {
        ElMessage.success('编辑成功')
        handleDialogClose()
        getArticleTypeList()
        console.log('编辑成功:', response)
      } else {
        ElMessage.error(response.msg || '编辑失败')
      }
    } else {
      // 新增文章类型
      const response = await addArticleType(articleType)
      if (response.code === 200) {
        ElMessage.success('新增成功')
        handleDialogClose()
        getArticleTypeList()
        console.log('新增成功:', response)
      } else {
        ElMessage.error(response.msg || '新增失败')
      }
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

const getArticleTypeList = async () => {
  try {
    const response = await queryInfo();
    if (response.code === 200) {
      articleTypeList.value = response.data
    } else {
      ElMessage.error(response.msg || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  }
}

onMounted(() => {
  getArticleTypeList()
})
</script>

<template>
  <div class="articles-page">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <h1 class="page-title">文章类型管理</h1>
      <p class="page-subtitle">管理博客系统中的所有文章分类</p>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <el-card class="action-card" shadow="hover">
        <div class="action-content">
          <div class="action-left">
            <el-icon class="section-icon">
              <Document />
            </el-icon>
            <span class="section-title">文章类型操作</span>
          </div>
          <div class="action-right">
            <el-button type="success" :icon="Plus" size="large" @click="handleAdd">
              新增文章类型
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 表格区域 -->
    <div class="table-section">
      <el-card class="table-card" shadow="hover">
        <template #header>
          <div class="table-header">
            <div class="header-left">
              <el-icon class="header-icon">
                <List />
              </el-icon>
              <span class="table-title">文章类型列表</span>
            </div>
            <div class="table-info">
              <el-tag type="info" effect="plain" size="large">
                共 {{ total || articleTypeList.length }} 条记录
              </el-tag>
            </div>
          </div>
        </template>

        <el-table :data="articleTypeList" v-loading="loading" class="articleType-table" stripe :header-cell-style="{
          background: 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
          color: '#2c3e50',
          fontWeight: '600',
          fontSize: '15px',
          height: '55px'
        }" :row-style="{ height: '65px' }" :cell-style="{ padding: '15px 0' }">
          <!-- 序号列 -->
          <el-table-column type="index" label="序号" width="200" align="center" />
          <!-- 类型名称列 - 调整最小宽度，减少与文章数量的间距 -->
          <el-table-column prop="articleTypeName" label="类型名称" width="250" align="center">
            <template #default="{ row }">
              <div class="type-name-cell">
                <span class="type-name">{{ row.articleTypeName }}</span>
              </div>
            </template>
          </el-table-column>

          <!-- 文章数列 - 调整宽度，增大标签尺寸 -->
          <el-table-column prop="articleNum" label="文章数量" width="250" align="center">
            <template #default="{ row }">
              <el-tag :type="row.articleNum > 0 ? 'success' : 'info'" effect="dark" size="default" round
                class="article-count-tag">
                {{ row.articleNum }} 篇
              </el-tag>
            </template>
          </el-table-column>

          <!-- 添加时间列 - 调整为单行显示，移除背景色 -->
          <el-table-column prop="articleTypeAddTime" label="添加时间" min-width="250" align="center">
            <template #default="{ row }">
              <div class="time-info">
                <el-icon class="time-icon">
                  <Clock />
                </el-icon>
                <span class="time-text">{{ formatTime(row.articleTypeAddTime) }}</span>
              </div>
            </template>
          </el-table-column>

          <!-- 操作列 - 增大按钮尺寸 -->
          <el-table-column label="操作" width="250" align="center" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-tooltip content="编辑类型" placement="top">
                  <el-button type="primary" :icon="Edit" size="default" circle class="action-btn"
                    @click="handleEdit(row)" />
                </el-tooltip>
                <el-tooltip content="删除类型" placement="top">
                  <el-button type="danger" :icon="Delete" size="default" circle class="action-btn"
                    @click="handleDelete(row)" />
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 对话框区域 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleDialogClose"
      class="article-type-dialog"
      center
      destroy-on-close
    >
      <!-- 对话框头部图标 -->
      <template #header="{ close, titleId, titleClass }">
        <div class="dialog-header">
          <div class="dialog-title-wrapper">
            <el-icon class="dialog-icon" :class="isEdit ? 'edit-icon' : 'add-icon'">
              <component :is="isEdit ? Edit : Plus" />
            </el-icon>
            <span :id="titleId" :class="titleClass" class="dialog-title">
              {{ dialogTitle }}
            </span>
          </div>
          <el-button 
            type="danger" 
            :icon="Close" 
            circle 
            size="small" 
            class="close-btn"
            @click="close"
          />
        </div>
      </template>

      <!-- 表单内容 -->
      <div class="dialog-content">
        <el-form
          ref="articleTypeFormRef"
          :model="articleType"
          :rules="formRules"
          label-width="100px"
          class="article-type-form"
          label-position="left"
        >
          <!-- 类型名称 -->
          <el-form-item label="类型名称" prop="articleTypeName" class="form-item">
            <el-input
              v-model="articleType.articleTypeName"
              placeholder="请输入文章类型名称"
              clearable
              :prefix-icon="Folder"
              class="form-input"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>

          <!-- 排序号 -->
          <el-form-item label="排序号" prop="articleTypeSort" class="form-item">
            <el-input-number
              v-model="articleType.articleTypeSort"
              :min="1"
              :max="999"
              placeholder="请输入排序号"
              class="form-number"
              controls-position="right"
            />
            <div class="form-tip">
              <el-icon class="tip-icon"><InfoFilled /></el-icon>
              <span>数字越小排序越靠前</span>
            </div>
          </el-form-item>

          <!-- 文章数量（编辑时显示，新增时隐藏） -->
          <el-form-item 
            v-if="isEdit" 
            label="文章数量" 
            class="form-item readonly-item"
          >
            <el-input
              :model-value="articleType.articleNum + ' 篇'"
              readonly
              :prefix-icon="Document"
              class="form-input readonly-input"
            />
            <div class="form-tip">
              <el-icon class="tip-icon"><InfoFilled /></el-icon>
              <span>文章数量为只读字段，系统自动统计</span>
            </div>
          </el-form-item>

          <!-- 添加时间（编辑时显示，新增时隐藏） -->
          <el-form-item 
            v-if="isEdit" 
            label="添加时间" 
            class="form-item readonly-item"
          >
            <el-input
              :model-value="formatTime(articleType.articleTypeAddTime)"
              readonly
              :prefix-icon="Clock"
              class="form-input readonly-input"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 对话框底部按钮 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button 
            size="large" 
            @click="handleDialogClose"
            class="cancel-btn"
          >
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button 
            type="primary" 
            size="large" 
            @click="handleSubmit" 
            :loading="submitLoading"
            class="submit-btn"
          >
            <el-icon><component :is="isEdit ? Edit : Plus" /></el-icon>
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>


</template>



<style scoped>

/* 对话框整体样式 */
:deep(.article-type-dialog .el-dialog) {
  border-radius: 20px; /* 设置对话框圆角 */
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15); /* 添加深度阴影 */
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%); /* 设置渐变背景 */
  overflow: hidden; /* 隐藏超出内容 */
}

/* 对话框头部样式 */
.dialog-header {
  display: flex; /* 使用flex布局 */
  justify-content: space-between; /* 两端对齐 */
  align-items: center; /* 垂直居中 */
  padding: 20px 30px; /* 设置内边距 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 设置头部渐变背景 */
  margin: 0px 0px 0 0px; /* 扩展到对话框边缘 */
}

/* 对话框标题包装器 */
.dialog-title-wrapper {
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  gap: 12px; /* 设置图标和标题间距 */
}

/* 对话框图标样式 */
.dialog-icon {
  width: 32px; /* 设置图标宽度 */
  height: 32px; /* 设置图标高度 */
  border-radius: 50%; /* 设置圆形背景 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  font-size: 16px; /* 设置图标字体大小 */
  color: #ffffff; /* 设置图标颜色为白色 */
  background: rgba(255, 255, 255, 0.2); /* 设置半透明白色背景 */
  backdrop-filter: blur(10px); /* 添加毛玻璃效果 */
}

/* 新增图标特殊样式 */
.add-icon {
  background: rgba(46, 204, 113, 0.3); /* 绿色半透明背景 */
}

/* 编辑图标特殊样式 */
.edit-icon {
  background: rgba(52, 152, 219, 0.3); /* 蓝色半透明背景 */
}

/* 对话框标题文字样式 */
.dialog-title {
  font-size: 1.4rem; /* 设置标题字体大小 */
  font-weight: 700; /* 设置字体粗细 */
  color: #ffffff; /* 设置文字颜色为白色 */
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 添加文字阴影 */
}

/* 关闭按钮样式 */
.close-btn {
  background: rgba(255, 255, 255, 0.15); /* 设置半透明背景 */
  border: 1px solid rgba(255, 255, 255, 0.3); /* 设置半透明边框 */
  color: #ffffff; /* 设置图标颜色为白色 */
  backdrop-filter: blur(10px); /* 添加毛玻璃效果 */
  transition: all 0.3s ease; /* 设置过渡动画 */
}

/* 关闭按钮悬停效果 */
.close-btn:hover {
  background: rgba(255, 255, 255, 0.25); /* 悬停时增强背景 */
  transform: scale(1.1); /* 悬停时放大 */
}

/* 对话框内容区域 */
.dialog-content {
  padding: 30px; /* 设置内边距 */
  background: #ffffff; /* 设置背景为白色 */
}

/* 表单样式 */
.article-type-form {
  max-width: 100%; /* 设置最大宽度 */
}

/* 表单项样式 */
.form-item {
  margin-bottom: 25px; /* 设置表单项底部间距 */
}

/* 只读表单项样式 */
.readonly-item {
  opacity: 0.8; /* 设置透明度，显示只读状态 */
}

/* 表单输入框样式 */
.form-input {
  font-size: 15px; /* 设置输入框字体大小 */
}

/* 只读输入框样式 */
.readonly-input {
  background-color: #f8f9fa; /* 设置只读背景色 */
}

/* 数字输入框样式 */
.form-number {
  width: 100%; /* 设置宽度为100% */
  font-size: 15px; /* 设置字体大小 */
}

/* 表单提示信息样式 */
.form-tip {
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  gap: 6px; /* 设置图标和文字间距 */
  margin-top: 8px; /* 设置顶部间距 */
  font-size: 13px; /* 设置字体大小 */
  color: #909399; /* 设置文字颜色为灰色 */
}

/* 提示图标样式 */
.tip-icon {
  font-size: 14px; /* 设置图标大小 */
  color: #409eff; /* 设置图标颜色为蓝色 */
}

/* 对话框底部按钮区域 */
.dialog-footer {
  display: flex; /* 使用flex布局 */
  justify-content: center; /* 水平居中 */
  gap: 20px; /* 设置按钮间距 */
  padding: 20px 30px 30px; /* 设置内边距 */
  background: linear-gradient(to bottom, #ffffff 0%, #f8f9fa 100%); /* 设置底部渐变背景 */
}

/* 取消按钮样式 */
.cancel-btn {
  min-width: 120px; /* 设置最小宽度 */
  font-weight: 600; /* 设置字体粗细 */
  border: 2px solid #dcdfe6; /* 设置边框 */
  transition: all 0.3s ease; /* 设置过渡动画 */
}

/* 取消按钮悬停效果 */
.cancel-btn:hover {
  border-color: #909399; /* 悬停时边框颜色变深 */
  transform: translateY(-2px); /* 悬停时向上移动 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 悬停时添加阴影 */
}

/* 提交按钮样式 */
.submit-btn {
  min-width: 120px; /* 设置最小宽度 */
  font-weight: 600; /* 设置字体粗细 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 设置渐变背景 */
  border: none; /* 移除边框 */
  transition: all 0.3s ease; /* 设置过渡动画 */
}

/* 提交按钮悬停效果 */
.submit-btn:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%); /* 悬停时背景变深 */
  transform: translateY(-2px); /* 悬停时向上移动 */
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4); /* 悬停时添加彩色阴影 */
}

/* 表单验证错误样式优化 */
:deep(.el-form-item__error) {
  font-size: 13px; /* 设置错误信息字体大小 */
  color: #f56565; /* 设置错误信息颜色 */
  margin-top: 6px; /* 设置错误信息顶部间距 */
}

/* 输入框获得焦点时的样式 */
:deep(.el-input__wrapper:focus-within) {
  box-shadow: 0 0 0 1px #667eea inset; /* 设置焦点时的边框效果 */
}

/* 数字输入框样式优化 */
:deep(.el-input-number) {
  width: 100%; /* 设置数字输入框宽度为100% */
}

:deep(.el-input-number .el-input__wrapper) {
  padding-right: 50px; /* 为控制按钮预留空间 */
}
/* 页面容器 - 设置整体布局和背景 */
.articles-page {
  padding: 0;
  /* 移除默认内边距 */
  background: transparent;
  /* 设置背景透明，使用父组件背景 */
}

/* 页面标题区域 */
.page-header {
  margin-bottom: 30px;
  /* 设置底部外边距，与下方内容分离 */
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

/* 操作按钮区域 */
.action-section {
  margin-bottom: 25px;
  /* 设置底部外边距，与表格保持合适距离 */
}

/* 操作卡片样式 */
.action-card {
  border-radius: 15px;
  /* 设置圆角 */
  border: none;
  /* 移除边框 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  /* 设置阴影效果 */
  transition: all 0.3s ease;
  /* 设置过渡动画 */
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  /* 设置渐变背景 */
}

/* 操作卡片悬停效果 */
.action-card:hover {
  transform: translateY(-3px);
  /* 悬停时向上移动 */
  box-shadow: 0 8px 35px rgba(0, 0, 0, 0.12);
  /* 悬停时增强阴影 */
}

/* 操作内容容器 */
.action-content {
  display: flex;
  /* 使用flex布局 */
  justify-content: space-between;
  /* 两端对齐 */
  align-items: center;
  /* 垂直居中 */
  padding: 15px 20px;
  /* 设置内边距 */
}

/* 操作区域左侧 */
.action-left {
  display: flex;
  /* 使用flex布局 */
  align-items: center;
  /* 垂直居中 */
  gap: 12px;
  /* 设置图标和文字间距 */
}

/* 区域图标样式 */
.section-icon {
  font-size: 20px;
  /* 设置图标大小 */
  color: #667eea;
  /* 设置图标颜色 */
}

/* 区域标题样式 */
.section-title {
  font-size: 1.1rem;
  /* 设置字体大小 */
  font-weight: 600;
  /* 设置字体粗细 */
  color: #2c3e50;
  /* 设置文字颜色 */
}

/* 操作区域右侧 */
.action-right {
  display: flex;
  /* 使用flex布局 */
  gap: 15px;
  /* 设置按钮间距 */
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

/* 类型图标样式 */
.type-icon {
  font-size: 16px;
  /* 设置图标大小 */
  color: #f39c12;
  /* 设置图标颜色为橙色 */
}

/* 类型名称文字样式 */
.type-name {
  font-weight: 500;
  /* 设置字体粗细 */
  color: #2c3e50;
  /* 设置文字颜色 */
  font-size: 0.95rem;
  /* 设置字体大小 */
}

/* 文章数量标签样式 - 增大尺寸 */
.article-count-tag {
  font-size: 0.9rem;
  /* 增大字体 */
  padding: 6px 12px;
  /* 增大内边距 */
  font-weight: 600;
  /* 增加字体粗细 */
}

/* 时间信息展示区域 - 移除背景色，调整为单行 */
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
  /* 强制单行显示，不换行 */
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
  /* 增大按钮间距 */
  justify-content: center;
  /* 水平居中 */
}

/* 操作按钮样式 - 增大按钮尺寸 */
.action-btn {
  width: 38px;
  /* 设置按钮宽度 */
  height: 38px;
  /* 设置按钮高度 */
  font-size: 16px;
  /* 增大图标字体 */
  transition: all 0.3s ease;
  /* 添加过渡动画 */
}

/* 操作按钮悬停效果 */
.action-btn:hover {
  transform: scale(1.1);
  /* 悬停时放大 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  /* 添加阴影 */
}

/* 表格整体样式优化 */
:deep(.articleType-table) {
  border-radius: 10px;
  /* 设置表格圆角 */
  overflow: hidden;
  /* 隐藏超出部分 */
}

/* 表格行悬停效果 */
:deep(.articleType-table .el-table__body-wrapper .el-table__row:hover) {
  background-color: rgba(102, 126, 234, 0.05);
  /* 悬停时设置淡紫色背景 */
  transition: background-color 0.3s ease;
  /* 设置背景色过渡动画 */
}

/* 表格单元格内边距优化 */
:deep(.articleType-table .el-table__cell) {
  padding: 15px 12px;
  /* 设置单元格内边距，增加垂直空间 */
}

/* 响应式设计 - 平板设备 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.8rem;
    /* 减小标题字体大小 */
  }

  .action-content {
    flex-direction: column;
    /* 操作内容垂直排列 */
    gap: 15px;
    /* 设置间距 */
    text-align: center;
    /* 居中对齐 */
  }

  .table-header {
    flex-direction: column;
    /* 表格头部垂直排列 */
    gap: 10px;
    /* 设置间距 */
    text-align: center;
    /* 居中对齐 */
  }

  .action-btn {
    width: 35px;
    /* 在平板上稍微减小按钮 */
    height: 35px;
    /* 在平板上稍微减小按钮 */
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

  .action-content {
    padding: 12px 15px;
    /* 减少操作内容内边距 */
  }

  .action-buttons {
    gap: 10px;
    /* 在手机上适当减少按钮间距 */
  }

  .time-info {
    font-size: 0.8rem;
    /* 在手机上减小时间字体 */
  }
}
</style>