<template>
  <div class="article-editor-page">
    <div class="editor-container">
      <!-- 头部标题栏 -->
      <div class="editor-header">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Edit /></el-icon>
            文章编辑器
          </h1>
          <el-tag v-if="isDraft" type="info" effect="plain">草稿</el-tag>
        </div>
        
        <div class="header-actions">
          <el-button 
            :loading="saveLoading" 
            @click="saveDraft"
            class="save-btn"
          >
            <el-icon><DocumentCopy /></el-icon>
            保存草稿
          </el-button>
          <el-button 
            type="primary" 
            :loading="publishLoading"
            @click="publishArticle"
            class="publish-btn"
          >
            <el-icon><Promotion /></el-icon>
            发布文章
          </el-button>
        </div>
      </div>

      <!-- 主要编辑区域 -->
      <div class="editor-content">
        <el-form
          ref="articleFormRef"
          :model="articleForm"
          :rules="formRules"
          label-position="top"
          class="article-form"
        >
          <!-- 文章标题 -->
          <el-form-item label="文章标题" prop="articleTitle" class="title-form-item">
            <el-input
              v-model="articleForm.articleTitle"
              placeholder="请输入文章标题..."
              size="large"
              maxlength="100"
              show-word-limit
              class="title-input"
            />
          </el-form-item>

          <!-- 文章元信息 -->
          <div class="meta-info-row">
            <el-form-item label="文章类型" prop="articleType" class="type-form-item">
              <el-select
                v-model="articleForm.articleType"
                placeholder="选择文章类型"
                size="large"
                class="type-select"
                :loading="typeLoading"
                clearable
                filterable
              >
                <el-option
                  v-for="type in articleTypes"
                  :key="type.articleTypeId || type.id"
                  :label="type.articleTypeName"
                  :value="type.articleTypeName"
                >
                  <span>{{ type.articleTypeName }}</span>
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="文章标签" prop="articleTags" class="tags-form-item">
              <el-select
                v-model="articleForm.articleTags"
                multiple
                placeholder="选择文章标签（可多选）"
                size="large"
                class="tags-select"
                :loading="tagsLoading"
                collapse-tags
                collapse-tags-tooltip
                :max-collapse-tags="3"
                clearable
                filterable
              >
                <el-option
                  v-for="tag in articleTags"
                  :key="tag.articleTagId || tag.id"
                  :label="tag.articleTagName"
                  :value="tag.articleTagName"
                >
                  <span>{{ tag.articleTagName }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </div>

          <!-- 文章内容 -->
          <el-form-item label="文章内容" prop="articleContent" class="content-form-item">
            <el-input
              v-model="articleForm.articleContent"
              type="textarea"
              :rows="20"
              placeholder="开始写作您的精彩内容..."
              maxlength="50000"
              show-word-limit
              class="content-textarea"
            />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 快捷操作悬浮球 -->
    <div class="floating-actions">
      <el-tooltip content="手动加载草稿" placement="left">
        <el-button
          circle
          @click="loadDraft"
          :loading="draftLoading"
          class="float-btn"
        >
          <el-icon><Folder /></el-icon>
        </el-button>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Edit, 
  DocumentCopy, 
  Promotion, 
  Folder 
} from '@element-plus/icons-vue'
import { queryInfo as getArticleTypes } from '@/api/articleType'
import { queryInfo as getArticleTags } from '@/api/articleTag'
import { addArticle, saveArticle, articleDraft } from '@/api/article'

const router = useRouter()

// 表单引用
const articleFormRef = ref()

// 响应式数据
const typeLoading = ref(false)
const tagsLoading = ref(false)
const saveLoading = ref(false)
const publishLoading = ref(false)
const draftLoading = ref(false)
const isDraft = ref(false)

// 文章类型和标签数据
const articleTypes = ref([])
const articleTags = ref([])

// 表单数据
const articleForm = reactive({
  articleTitle: '',
  articleContent: '',
  articleType: '',
  articleTags: []
})

// 表单验证规则
const formRules = {
  articleTitle: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  articleContent: [
    { required: true, message: '请输入文章内容', trigger: 'blur' },
    { min: 10, message: '文章内容至少需要 10 个字符', trigger: 'blur' }
  ],
  articleType: [
    { required: true, message: '请选择文章类型', trigger: 'change' }
  ],
  articleTags: [
    { 
      type: 'array', 
      required: true, 
      message: '请至少选择一个标签', 
      trigger: 'change' 
    }
  ]
}

// 获取文章类型列表
const fetchArticleTypes = async () => {
  typeLoading.value = true
  try {
    const response = await getArticleTypes()
    if (response.code === 200) {
      articleTypes.value = response.data || []
      console.log('文章类型列表:', articleTypes.value)
    } else {
      ElMessage.error(response.msg || '获取文章类型失败')
    }
  } catch (error) {
    console.error('获取文章类型失败:', error)
    ElMessage.error('获取文章类型失败')
  } finally {
    typeLoading.value = false
  }
}

// 获取文章标签列表
const fetchArticleTags = async () => {
  tagsLoading.value = true
  try {
    const response = await getArticleTags()
    if (response.code === 200) {
      articleTags.value = response.data || []
      console.log('文章标签列表:', articleTags.value)
    } else {
      ElMessage.error(response.msg || '获取文章标签失败')
    }
  } catch (error) {
    console.error('获取文章标签失败:', error)
    ElMessage.error('获取文章标签失败')
  } finally {
    tagsLoading.value = false
  }
}

// 自动加载草稿（页面初始化时调用）
const autoLoadDraft = async () => {
  try {
    const response = await articleDraft()
    
    if (response.code === 200 && response.data) {
      const draft = response.data
      
      // 检查草稿是否有有效内容
      if (draft.articleTitle || draft.articleContent || draft.articleType || (draft.articleTags && draft.articleTags.length > 0)) {
        articleForm.articleTitle = draft.articleTitle || ''
        articleForm.articleContent = draft.articleContent || ''
        articleForm.articleType = draft.articleType || ''
        articleForm.articleTags = draft.articleTags || []
        
        isDraft.value = true
        console.log('自动加载草稿成功:', draft)
      }
    }
    // 如果没有草稿或获取失败，不显示任何提示信息
  } catch (error) {
    console.error('自动加载草稿失败:', error)
    // 静默处理错误，不显示错误信息
  }
}

// 手动加载草稿
const loadDraft = async () => {
  try {
    await ElMessageBox.confirm(
      '加载草稿将覆盖当前内容，确定要继续吗？',
      '加载草稿',
      {
        confirmButtonText: '确定加载',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
  } catch {
    return
  }

  draftLoading.value = true
  try {
    const response = await articleDraft()
    
    if (response.code === 200 && response.data) {
      const draft = response.data
      
      // 检查草稿是否有有效内容
      if (draft.articleTitle || draft.articleContent || draft.articleType || (draft.articleTags && draft.articleTags.length > 0)) {
        articleForm.articleTitle = draft.articleTitle || ''
        articleForm.articleContent = draft.articleContent || ''
        articleForm.articleType = draft.articleType || ''
        articleForm.articleTags = draft.articleTags || []
        
        isDraft.value = true
        ElMessage.success('草稿加载成功')
      } else {
        ElMessage.info('暂无草稿内容')
      }
    } else {
      ElMessage.info('暂无草稿内容')
    }
  } catch (error) {
    console.error('加载草稿失败:', error)
    ElMessage.error('加载草稿失败')
  } finally {
    draftLoading.value = false
  }
}

// 保存草稿
const saveDraft = async () => {
  // 简单验证标题是否存在
  if (!articleForm.articleTitle.trim()) {
    ElMessage.warning('请先输入文章标题')
    return
  }

  saveLoading.value = true
  try {
    const formData = {
      articleTitle: articleForm.articleTitle,
      articleContent: articleForm.articleContent,
      articleType: articleForm.articleType,
      articleTags: articleForm.articleTags
    }

    console.log('保存草稿数据:', formData)
    const response = await saveArticle(formData)
    
    if (response.code === 200) {
      isDraft.value = true
      ElMessage.success('草稿保存成功')
    } else {
      ElMessage.error(response.msg || '保存草稿失败')
    }
  } catch (error) {
    console.error('保存草稿失败:', error)
    ElMessage.error('保存草稿失败')
  } finally {
    saveLoading.value = false
  }
}

// 发布文章
const publishArticle = async () => {
  // 表单验证
  const valid = await articleFormRef.value?.validate().catch(() => false)
  if (!valid) {
    ElMessage.warning('请完善文章信息')
    return
  }

  // 二次确认
  try {
    await ElMessageBox.confirm(
      '确定要发布这篇文章吗？发布后将对所有用户可见。',
      '确认发布',
      {
        confirmButtonText: '确定发布',
        cancelButtonText: '再想想',
        type: 'info',
        confirmButtonClass: 'el-button--primary'
      }
    )
  } catch {
    return
  }

  publishLoading.value = true
  try {
    const formData = {
      articleTitle: articleForm.articleTitle,
      articleContent: articleForm.articleContent,
      articleType: articleForm.articleType,
      articleTags: articleForm.articleTags
    }

    console.log('发布文章数据:', formData)
    const response = await addArticle(formData)
    
    if (response.code === 200) {
      ElMessage.success('文章发布成功！')
      
      // 询问是否跳转到文章详情
      try {
        await ElMessageBox.confirm(
          '文章发布成功！是否要查看发布的文章？',
          '发布成功',
          {
            confirmButtonText: '查看文章',
            cancelButtonText: '继续写作',
            type: 'success'
          }
        )
        
        // 如果后端返回文章ID，可以跳转到文章详情
        if (response.data?.articleId) {
          router.push(`/users/article/detail/?articleId=${response.data.articleId}`)
        } else {
          router.push('/users') // 跳转到首页
        }
      } catch {
        // 用户选择继续写作，清空表单
        resetForm()
      }
    } else {
      ElMessage.error(response.msg || '发布失败')
    }
  } catch (error) {
    console.error('发布文章失败:', error)
    ElMessage.error('发布文章失败')
  } finally {
    publishLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  articleForm.articleTitle = ''
  articleForm.articleContent = ''
  articleForm.articleType = ''
  articleForm.articleTags = []
  isDraft.value = false
  articleFormRef.value?.clearValidate()
}

// 组件挂载时初始化
onMounted(async () => {
  // 并行获取类型和标签数据
  await Promise.all([
    fetchArticleTypes(),
    fetchArticleTags()
  ])
  
  // 数据加载完成后自动加载草稿
  await autoLoadDraft()
})
</script>

<style scoped>
.article-editor-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  background: var(--el-bg-color);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 头部样式 */
.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 32px;
  background: linear-gradient(135deg, var(--el-color-primary) 0%, var(--el-color-primary-dark-2) 100%);
  color: white;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.save-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  backdrop-filter: blur(10px);
}

.save-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.publish-btn {
  background: var(--el-color-success);
  border-color: var(--el-color-success);
}

.publish-btn:hover {
  background: var(--el-color-success-dark-2);
  border-color: var(--el-color-success-dark-2);
}

/* 编辑区域样式 */
.editor-content {
  padding: 32px;
}

.article-form {
  max-width: 100%;
}

/* 标题输入框样式 */
.title-form-item {
  margin-bottom: 32px;
}

.title-input :deep(.el-input__wrapper) {
  padding: 16px 20px;
  font-size: 18px;
  font-weight: 500;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.title-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.title-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.2);
}

/* 元信息行样式 */
.meta-info-row {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 24px;
  margin-bottom: 32px;
}

.type-select,
.tags-select {
  width: 100%;
}

.type-select :deep(.el-input__wrapper),
.tags-select :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid var(--el-border-color-light);
  transition: all 0.3s ease;
}

.type-select :deep(.el-input__wrapper:hover),
.tags-select :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

/* 多选标签样式优化 */
.tags-select :deep(.el-select__tags) {
  max-width: 100%;
  overflow: hidden;
}

.tags-select :deep(.el-tag) {
  margin: 2px 4px 2px 0;
  max-width: 120px;
}

.tags-select :deep(.el-tag__content) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 内容输入框样式 */
.content-form-item {
  margin-bottom: 0;
}

.content-textarea :deep(.el-textarea__inner) {
  border-radius: 12px;
  border: 2px solid var(--el-border-color-light);
  font-size: 16px;
  line-height: 1.8;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  resize: vertical;
  min-height: 400px;
}

.content-textarea :deep(.el-textarea__inner:hover) {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.content-textarea :deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.2);
}

/* 表单标签样式 */
:deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--el-text-color-primary);
  font-size: 16px;
  margin-bottom: 8px;
}

/* 悬浮操作按钮 */
.floating-actions {
  position: fixed;
  right: 32px;
  bottom: 32px;
  z-index: 1000;
}

.float-btn {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  background: var(--el-color-info);
  border-color: var(--el-color-info);
  color: white;
  transition: all 0.3s ease;
}

.float-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-editor-page {
    padding: 12px;
  }
  
  .editor-header {
    flex-direction: column;
    gap: 16px;
    padding: 20px;
    text-align: center;
  }
  
  .header-actions {
    width: 100%;
    justify-content: center;
  }
  
  .editor-content {
    padding: 20px;
  }
  
  .meta-info-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .floating-actions {
    right: 20px;
    bottom: 20px;
  }
  
  .float-btn {
    width: 48px;
    height: 48px;
  }
}

@media (max-width: 480px) {
  .article-editor-page {
    padding: 8px;
  }
  
  .editor-header {
    padding: 16px;
  }
  
  .editor-content {
    padding: 16px;
  }
  
  .header-actions {
    flex-direction: column;
    gap: 8px;
  }
  
  .save-btn,
  .publish-btn {
    width: 100%;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.editor-container {
  animation: fadeInUp 0.6s ease-out;
}

.floating-actions {
  animation: fadeInUp 0.8s ease-out;
}
</style>