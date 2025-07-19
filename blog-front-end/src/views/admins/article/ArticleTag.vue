<script setup>
  import { ref, reactive, onMounted, computed } from 'vue'
  import {
      Plus,
      Edit,
      Delete,
      Clock,
      Close,
      Refresh,
      CollectionTag,
      PriceTag
  } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {queryInfo,deleteById,editArticleTag,addArticleTag} from '@/api/articleTag'
  
  // 响应式数据
  const loading = ref(false)
  const submitLoading = ref(false)
  const dialogVisible = ref(false)
  const isEdit = ref(false)
  const articleTagFormRef = ref()
  
  // 模拟标签数据
  const articleTagList = ref([])
  
  // 标签表单数据
  const articleTag = reactive({
    articleTagId: '',
    articleTagName: '',
    articleTagAddTime: ''
  })
  
  // 表单验证规则
  const formRules = {
    articleTagName: [
      { required: true, message: '请输入标签名称', trigger: 'blur' },
      { min: 1, max: 30, message: '标签名称长度在 1 到 30 个字符', trigger: 'blur' },
      { 
        validator: (rule, value, callback) => {
          // 检查标签名称是否重复（排除当前编辑的标签）
          const isDuplicate = articleTagList.value.some(tag => 
            tag.articleTagName === value && tag.articleTagId !== articleTag.articleTagId
          )
          if (isDuplicate) {
            callback(new Error('标签名称已存在'))
          } else {
            callback()
          }
        }, 
        trigger: 'blur' 
      }
    ]
  }
  
  // 计算属性
  const dialogTitle = computed(() => isEdit.value ? '编辑标签' : '新增标签')
  
  // 格式化时间函数
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
  
  // 重置表单
  const resetForm = () => {
    Object.assign(articleTag, {
      articleTagId: '',
      articleTagName: '',
      articleTagAddTime: ''
    })
    articleTagFormRef.value?.clearValidate()
  }
  
  // 新增标签
  const handleAdd = () => {
    isEdit.value = false
    dialogVisible.value = true
    resetForm()
  }
  
  // 编辑标签
  const handleEdit = (tag) => {
    isEdit.value = true
    dialogVisible.value = true
    Object.assign(articleTag, { ...tag })
  }
  
  // 删除标签
  const handleDelete = async (tag) => {
    try {
      await ElMessageBox.confirm(
        `确认要删除标签 "${tag.articleTagName}" 吗？此操作不可恢复！`,
        '确认删除',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'error',
          center: true,
          customClass: 'delete-confirm-dialog'
        }
      )
      const response = await deleteById(tag.articleTagId);
        if (response.code === 200) {
            ElMessage.success('删除成功')
            getArticleTagList()
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
  
  // 刷新标签列表
  const refreshTags = () => {
    loading.value = true
    // 模拟刷新操作
    setTimeout(() => {
      loading.value = false
      ElMessage.success('刷新成功')
    }, 100)
  }
  
  // 关闭对话框
  const handleDialogClose = () => {
    dialogVisible.value = false
    resetForm()
  }
  
  // 提交表单
  const handleSubmit = async () => {
    try {
      await articleTagFormRef.value.validate()
      
      submitLoading.value = true
      
      // 模拟API请求延迟
      await new Promise(resolve => setTimeout(resolve, 100))
      
      if (isEdit.value) {
        // 编辑标签
        const response = await editArticleTag(articleTag)
        if (response.code === 200) {
          ElMessage.success('编辑成功')
          getArticleTagList()
        } else {
          ElMessage.error(response.msg || '编辑失败')
        }
      } else {
        // 新增标签
        const response = await addArticleTag(articleTag)
        if (response.code === 200) {
          ElMessage.success('新增成功')
          getArticleTagList()
        } else {
          ElMessage.error(response.msg || '新增失败')
        }
      }
      
      handleDialogClose()
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  }

  const getArticleTagList = async () => { 
    const response = await queryInfo()
    if (response.code === 200) {
      articleTagList.value = response.data
    } else {
      ElMessage.error(response.msg || '获取标签列表失败')
    }
  }

  onMounted(() => {
    getArticleTagList()
  })
</script>

<template>
    <div class="article-tags-page">
      <!-- 页面标题区域 -->
      <div class="page-header">
        <h1 class="page-title">文章标签管理</h1>
        <p class="page-subtitle">管理博客系统中的所有文章标签</p>
      </div>
  
      <!-- 操作按钮区域 -->
      <div class="action-section">
        <el-card class="action-card" shadow="hover">
          <div class="action-content">
            <div class="action-left">
              <el-icon class="section-icon"><CollectionTag /></el-icon>
              <span class="section-title">标签操作</span>
              <el-divider direction="vertical" />
              <el-tag type="info" effect="plain" size="large">
                共 {{ articleTagList.length }} 个标签
              </el-tag>
            </div>
            <div class="action-right">
              <el-button type="success" :icon="Plus" size="large" @click="handleAdd">
                新增标签
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
  
      <!-- 标签展示区域 -->
      <div class="tags-section">
        <el-card class="tags-card" shadow="hover">
          <template #header>
            <div class="tags-header">
              <div class="header-left">
                <el-icon class="header-icon"><CollectionTag /></el-icon>
                <span class="tags-title">标签列表</span>
              </div>
              <div class="header-right">
                <el-button 
                  :icon="Refresh" 
                  circle 
                  @click="refreshTags"
                  class="refresh-btn"
                  title="刷新标签"
                />
              </div>
            </div>
          </template>
  
          <div class="tags-content" v-loading="loading">
            <div class="tags-grid" v-if="articleTagList.length > 0">
              <div 
                v-for="tag in articleTagList" 
                :key="tag.articleTagId"
                class="tag-item"
              >
                <div class="tag-content">
                  <div class="tag-info">
                    <el-icon class="tag-icon"><PriceTag /></el-icon>
                    <div class="tag-details">
                      <span class="tag-name">{{ tag.articleTagName }}</span>
                      <span class="tag-time">{{ formatTime(tag.articleTagAddTime) }}</span>
                    </div>
                  </div>
                  <div class="tag-actions">
                    <el-tooltip content="编辑标签" placement="top">
                      <el-button 
                        type="primary" 
                        :icon="Edit" 
                        size="small" 
                        circle
                        @click="handleEdit(tag)"
                        class="action-btn edit-btn"
                      />
                    </el-tooltip>
                    <el-tooltip content="删除标签" placement="top">
                      <el-button 
                        type="danger" 
                        :icon="Delete" 
                        size="small" 
                        circle
                        @click="handleDelete(tag)"
                        class="action-btn delete-btn"
                      />
                    </el-tooltip>
                  </div>
                </div>
              </div>
            </div>
  
            <!-- 空状态 -->
            <div v-else class="empty-state">
              <el-empty 
                description="暂无标签数据"
                image-size="120"
              >
                <template #image>
                  <el-icon class="empty-icon"><CollectionTag /></el-icon>
                </template>
                <el-button type="primary" :icon="Plus" @click="handleAdd">
                  创建第一个标签
                </el-button>
              </el-empty>
            </div>
          </div>
        </el-card>
      </div>
  
      <!-- 编辑/新增标签对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="500px"
        :before-close="handleDialogClose"
        class="tag-dialog"
        center
        destroy-on-close
      >
        <!-- 对话框头部 -->
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
            ref="articleTagFormRef"
            :model="articleTag"
            :rules="formRules"
            label-width="80px"
            class="tag-form"
            label-position="left"
          >
            <!-- 标签名称 -->
            <el-form-item label="标签名称" prop="articleTagName" class="form-item">
              <el-input
                v-model="articleTag.articleTagName"
                placeholder="请输入标签名称"
                clearable
                :prefix-icon="PriceTag"
                class="form-input"
                maxlength="30"
                show-word-limit
              />
            </el-form-item>
  
            <!-- 添加时间（编辑时显示） -->
            <el-form-item 
              v-if="isEdit" 
              label="添加时间" 
              class="form-item readonly-item"
            >
              <el-input
                :model-value="formatTime(articleTag.articleTagAddTime)"
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
  /* 页面容器样式 */
  .article-tags-page {
    padding: 0; /* 移除默认内边距 */
    background: transparent; /* 设置背景透明 */
  }
  
  /* 页面标题区域 */
  .page-header {
    margin-bottom: 30px; /* 设置底部外边距 */
    padding: 25px 0; /* 设置上下内边距 */
    text-align: center; /* 文字居中对齐 */
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 249, 250, 0.8) 100%); /* 设置半透明渐变背景 */
    border-radius: 20px; /* 设置圆角 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05); /* 添加轻微阴影 */
  }
  
  /* 页面主标题样式 */
  .page-title {
    font-size: 2.2rem; /* 设置字体大小 */
    font-weight: 700; /* 设置字体粗细 */
    color: #2c3e50; /* 设置文字颜色 */
    margin: 0 0 10px 0; /* 设置外边距 */
    background: linear-gradient(135deg, #2c3e50 0%, #667eea 100%); /* 设置渐变文字效果 */
    background-clip: text; /* 将背景裁剪到文字 */
    -webkit-background-clip: text; /* Webkit浏览器兼容 */
    -webkit-text-fill-color: transparent; /* 设置文字透明显示背景 */
  }
  
  /* 页面副标题样式 */
  .page-subtitle {
    font-size: 1rem; /* 设置字体大小 */
    color: #7f8c8d; /* 设置文字颜色为灰色 */
    margin: 0; /* 移除默认外边距 */
    font-weight: 500; /* 设置字体粗细 */
  }
  
  /* 操作按钮区域 */
  .action-section {
    margin-bottom: 25px; /* 设置底部外边距 */
  }
  
  /* 操作卡片样式 */
  .action-card {
    border-radius: 15px; /* 设置圆角 */
    border: none; /* 移除边框 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); /* 设置阴影效果 */
    transition: all 0.3s ease; /* 设置过渡动画 */
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%); /* 设置渐变背景 */
  }
  
  /* 操作卡片悬停效果 */
  .action-card:hover {
    transform: translateY(-3px); /* 悬停时向上移动 */
    box-shadow: 0 8px 35px rgba(0, 0, 0, 0.12); /* 悬停时增强阴影 */
  }
  
  /* 操作内容容器 */
  .action-content {
    display: flex; /* 使用flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 15px 20px; /* 设置内边距 */
  }
  
  /* 操作区域左侧 */
  .action-left {
    display: flex; /* 使用flex布局 */
    align-items: center; /* 垂直居中 */
    gap: 12px; /* 设置元素间距 */
  }
  
  /* 区域图标样式 */
  .section-icon {
    font-size: 20px; /* 设置图标大小 */
    color: #667eea; /* 设置图标颜色 */
  }
  
  /* 区域标题样式 */
  .section-title {
    font-size: 1.1rem; /* 设置字体大小 */
    font-weight: 600; /* 设置字体粗细 */
    color: #2c3e50; /* 设置文字颜色 */
  }
  
  /* 操作区域右侧 */
  .action-right {
    display: flex; /* 使用flex布局 */
    gap: 15px; /* 设置按钮间距 */
  }
  
  /* 标签展示区域 */
  .tags-section {
    margin-bottom: 20px; /* 设置底部外边距 */
  }
  
  /* 标签卡片样式 */
  .tags-card {
    border-radius: 15px; /* 设置圆角 */
    border: none; /* 移除边框 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); /* 设置阴影效果 */
    transition: all 0.3s ease; /* 设置过渡动画 */
    overflow: hidden; /* 隐藏超出内容 */
  }
  
  /* 标签卡片悬停效果 */
  .tags-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12); /* 悬停时增强阴影 */
  }
  
  /* 标签头部样式 */
  .tags-header {
    display: flex; /* 使用flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 10px 0; /* 设置上下内边距 */
  }
  
  /* 标签头部左侧 */
  .header-left {
    display: flex; /* 使用flex布局 */
    align-items: center; /* 垂直居中 */
    gap: 10px; /* 设置图标和文字间距 */
  }
  
  /* 标签头部右侧 */
  .header-right {
    display: flex; /* 使用flex布局 */
    gap: 10px; /* 设置按钮间距 */
  }
  
  /* 标签头部图标 */
  .header-icon {
    font-size: 18px; /* 设置图标大小 */
    color: #667eea; /* 设置图标颜色 */
  }
  
  /* 标签标题样式 */
  .tags-title {
    font-size: 1.3rem; /* 设置字体大小 */
    font-weight: 600; /* 设置字体粗细 */
    color: #2c3e50; /* 设置文字颜色 */
  }
  
  /* 刷新按钮样式 */
  .refresh-btn {
    background: rgba(102, 126, 234, 0.1); /* 设置半透明背景 */
    border: 1px solid rgba(102, 126, 234, 0.2); /* 设置半透明边框 */
    color: #667eea; /* 设置按钮颜色 */
    transition: all 0.3s ease; /* 设置过渡动画 */
  }
  
  /* 刷新按钮悬停效果 */
  .refresh-btn:hover {
    background: rgba(102, 126, 234, 0.2); /* 悬停时增强背景 */
    transform: rotate(180deg); /* 悬停时旋转图标 */
  }
  
  /* 标签内容区域 */
  .tags-content {
    padding: 20px 0; /* 设置上下内边距 */
    min-height: 200px; /* 设置最小高度 */
  }
  
  /* 标签网格布局 */
  .tags-grid {
    display: grid; /* 使用grid布局 */
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); /* 自适应列数，每列最小300px */
    gap: 20px; /* 设置网格间距 */
    padding: 10px 0; /* 设置上下内边距 */
  }
  
  /* 标签项容器 */
  .tag-item {
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%); /* 设置渐变背景 */
    border-radius: 12px; /* 设置圆角 */
    border: 1px solid #e9ecef; /* 设置边框 */
    transition: all 0.3s ease; /* 设置过渡动画 */
    overflow: hidden; /* 隐藏超出内容 */
  }
  
  /* 标签项悬停效果 */
  .tag-item:hover {
    transform: translateY(-3px); /* 悬停时向上移动 */
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15); /* 悬停时添加阴影 */
    border-color: #667eea; /* 悬停时边框颜色变化 */
  }
  
  /* 标签内容容器 */
  .tag-content {
    display: flex; /* 使用flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 20px; /* 设置内边距 */
  }
  
  /* 标签信息区域 */
  .tag-info {
    display: flex; /* 使用flex布局 */
    align-items: center; /* 垂直居中 */
    gap: 12px; /* 设置图标和详情间距 */
    flex: 1; /* 占据剩余空间 */
  }
  
  /* 标签图标样式 */
  .tag-icon {
    width: 40px; /* 设置图标容器宽度 */
    height: 40px; /* 设置图标容器高度 */
    border-radius: 50%; /* 设置圆形背景 */
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 设置渐变背景 */
    color: white; /* 设置图标颜色为白色 */
    display: flex; /* 使用flex布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    font-size: 18px; /* 设置图标字体大小 */
    flex-shrink: 0; /* 防止图标被压缩 */
  }
  
  /* 标签详情区域 */
  .tag-details {
    display: flex; /* 使用flex布局 */
    flex-direction: column; /* 垂直排列 */
    gap: 5px; /* 设置标签名和时间间距 */
  }
  
  /* 标签名称样式 */
  .tag-name {
    font-size: 1.1rem; /* 设置字体大小 */
    font-weight: 600; /* 设置字体粗细 */
    color: #2c3e50; /* 设置文字颜色 */
    line-height: 1.2; /* 设置行高 */
  }
  
  /* 标签时间样式 */
  .tag-time {
    font-size: 0.85rem; /* 设置字体大小 */
    color: #95a5a6; /* 设置文字颜色为灰色 */
    font-weight: 500; /* 设置字体粗细 */
  }
  
  /* 标签操作按钮区域 */
  .tag-actions {
    display: flex; /* 使用flex布局 */
    gap: 8px; /* 设置按钮间距 */
  }
  
  /* 操作按钮基础样式 */
  .action-btn {
    width: 32px; /* 设置按钮宽度 */
    height: 32px; /* 设置按钮高度 */
    transition: all 0.3s ease; /* 设置过渡动画 */
  }
  
  /* 编辑按钮样式 */
  .edit-btn:hover {
    transform: scale(1.1); /* 悬停时放大 */
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4); /* 悬停时添加蓝色阴影 */
  }
  
  /* 删除按钮样式 */
  .delete-btn:hover {
    transform: scale(1.1); /* 悬停时放大 */
    box-shadow: 0 4px 12px rgba(245, 101, 101, 0.4); /* 悬停时添加红色阴影 */
  }
  
  /* 空状态样式 */
  .empty-state {
    text-align: center; /* 居中对齐 */
    padding: 40px 20px; /* 设置内边距 */
  }
  
  /* 空状态图标样式 */
  .empty-icon {
    font-size: 80px; /* 设置图标大小 */
    color: #ddd; /* 设置图标颜色为浅灰色 */
    margin-bottom: 20px; /* 设置底部外边距 */
  }
  
  /* === 对话框样式 === */
  
  /* 对话框整体样式 */
  :deep(.tag-dialog .el-dialog) {
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
    margin: -20px -20px 0 -20px; /* 扩展到对话框边缘 */
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
    font-size: 1.3rem; /* 设置标题字体大小 */
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
  
  /* 标签表单样式 */
  .tag-form {
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
  
  /* 删除确认对话框样式 */
  :deep(.delete-confirm-dialog) {
    border-radius: 15px; /* 设置圆角 */
  }
  
  :deep(.delete-confirm-dialog .el-message-box__header) {
    background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%); /* 设置删除确认对话框头部背景 */
    color: white; /* 设置文字颜色为白色 */
    border-radius: 15px 15px 0 0; /* 设置顶部圆角 */
  }
  
  /* 响应式设计 - 平板设备 */
  @media (max-width: 768px) {
    .page-title {
      font-size: 1.8rem; /* 减小标题字体大小 */
    }
    
    .action-content {
      flex-direction: column; /* 操作内容垂直排列 */
      gap: 15px; /* 设置间距 */
      text-align: center; /* 居中对齐 */
    }
    
    .tags-grid {
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); /* 调整网格列宽 */
      gap: 15px; /* 减少网格间距 */
    }
    
    .tag-content {
      padding: 15px; /* 减少标签项内边距 */
    }
    
    /* 对话框在平板上的适配 */
    :deep(.tag-dialog .el-dialog) {
      width: 90% !important; /* 对话框宽度调整为90% */
      margin: 5vh auto; /* 调整对话框边距 */
    }
    
    .dialog-content {
      padding: 20px; /* 减少对话框内容内边距 */
    }
    
    .dialog-footer {
      padding: 15px 20px 20px; /* 调整底部按钮区域内边距 */
      flex-direction: column; /* 按钮垂直排列 */
      gap: 15px; /* 调整按钮间距 */
    }
    
    .cancel-btn,
    .submit-btn {
      width: 100%; /* 按钮宽度设为100% */
    }
  }
  
  /* 响应式设计 - 手机设备 */
  @media (max-width: 480px) {
    .page-header {
      padding: 20px 15px; /* 减少页面头部内边距 */
    }
    
    .page-title {
      font-size: 1.5rem; /* 进一步减小标题字体 */
    }
    
    .action-content {
      padding: 12px 15px; /* 减少操作内容内边距 */
    }
    
    .tags-grid {
      grid-template-columns: 1fr; /* 手机上单列显示 */
      gap: 12px; /* 进一步减少网格间距 */
    }
    
    .tag-content {
      padding: 12px; /* 进一步减少标签项内边距 */
    }
    
    .tag-name {
      font-size: 1rem; /* 减小标签名称字体 */
    }
    
    /* 对话框在手机上的适配 */
    :deep(.tag-dialog .el-dialog) {
      width: 95% !important; /* 对话框宽度调整为95% */
      margin: 2vh auto; /* 进一步调整对话框边距 */
    }
    
    .dialog-header {
      padding: 15px 20px; /* 减少对话框头部内边距 */
    }
    
    .dialog-title {
      font-size: 1.1rem; /* 减小对话框标题字体 */
    }
    
    .dialog-content {
      padding: 15px; /* 进一步减少对话框内容内边距 */
    }
    
    .form-item {
      margin-bottom: 20px; /* 减少表单项间距 */
    }
  }
  </style>