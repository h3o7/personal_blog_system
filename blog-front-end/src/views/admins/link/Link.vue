<script setup>
  import { ref, reactive, onMounted, computed } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { 
    Plus, View, Edit, Delete, Link, Grid, Refresh, Clock, 
    Document, Picture, InfoFilled, Close
  } from '@element-plus/icons-vue'
  
  // 假设这些API已经封装好
  import { queryInfo, deleteById, addLink, editLink } from '@/api/link'
  
  // 响应式数据
  const linkList = ref([])
  const loading = ref(false)
  const dialogVisible = ref(false)
  const submitting = ref(false)
  const isEdit = ref(false)
  const formRef = ref(null)
  
  // 表单数据
  const linkForm = reactive({
    linkId: '',
    linkTitle: '',
    linkUrl: '',
    linkLogoUrl: '',
    linkSort: 0,
    linkAddTime: ''
  })
  
  // 表单验证规则
  const formRules = {
    linkTitle: [
      { required: true, message: '请输入链接标题', trigger: 'blur' },
      { min: 1, max: 50, message: '标题长度在1到50个字符', trigger: 'blur' }
    ],
    linkUrl: [
      { required: true, message: '请输入链接地址', trigger: 'blur' },
      { type: 'url', message: '请输入正确的URL格式', trigger: 'blur' }
    ],
    linkLogoUrl: [
      { required: true, message: '请输入Logo地址', trigger: 'blur' },
      { type: 'url', message: '请输入正确的URL格式', trigger: 'blur' }
    ],
    linkSort: [
      { required: true, message: '请输入排序号', trigger: 'blur' },
      { type: 'number', min: 0, message: '排序号不能小于0', trigger: 'blur' }
    ]
  }
  
  // 计算属性
  const dialogTitle = computed(() => isEdit.value ? '编辑友情链接' : '添加友情链接')
  
  // 表格样式
  const tableHeaderStyle = {
    backgroundColor: '#fafbfc',
    color: '#333',
    fontSize: '14px',
    fontWeight: '600',
    height: '56px'
  }
  
  const tableRowStyle = {
    height: '64px'
  }
  
  // 生命周期
  onMounted(() => {
    loadData()
  })
  
  // 方法
  const loadData = async () => {
    try {
      loading.value = true
      const response = await queryInfo()
      linkList.value = response.data || []
    } catch (error) {
      ElMessage.error('获取数据失败：' + error.message)
    } finally {
      loading.value = false
    }
  }
  
  const resetForm = () => {
    Object.assign(linkForm, {
      linkId: '',
      linkTitle: '',
      linkUrl: '',
      linkLogoUrl: '',
      linkSort: 0,
      linkAddTime: ''
    })
    if (formRef.value) {
      formRef.value.clearValidate()
    }
  }
  
  const handleAdd = () => {
    resetForm()
    isEdit.value = false
    dialogVisible.value = true
  }
  
  const handleEdit = (row) => {
    resetForm()
    Object.assign(linkForm, { ...row })
    isEdit.value = true
    dialogVisible.value = true
  }
  
  const handleView = (row) => {
    if (row.linkUrl) {
      window.open(row.linkUrl, '_blank')
    } else {
      ElMessage.warning('链接地址无效')
    }
  }
  
  const handleDelete = async (row) => {
    try {
      await ElMessageBox.confirm(
        `确定要删除友情链接"${row.linkTitle}"吗？`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          customClass: 'beautiful-message-box'
        }
      )
      
      await deleteById(row.linkId)
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除失败：' + error.message)
      }
    }
  }
  
  const handleSubmit = async () => {
    if (!formRef.value) return
    
    try {
      await formRef.value.validate()
      submitting.value = true
      
      const formData = { ...linkForm }
      delete formData.linkAddTime
      
      if (isEdit.value) {
        await editLink(formData)
        ElMessage.success('更新成功')
      } else {
        delete formData.linkId
        await addLink(formData)
        ElMessage.success('添加成功')
      }
      
      dialogVisible.value = false
      await loadData()
    } catch (error) {
      ElMessage.error(isEdit.value ? '更新失败：' + error.message : '添加失败：' + error.message)
    } finally {
      submitting.value = false
    }
  }
  
  const formatTime = (timeString) => {
    if (!timeString) return ''
    const date = new Date(timeString)
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    }).replace(/\//g, '-')
  }
  
  const handleImageError = (event) => {
    event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiBmaWxsPSIjRjVGNUY1IiByeD0iOCIvPgo8cGF0aCBkPSJNMTIgMTZMMjAgMjRMMjggMTYiIHN0cm9rZT0iI0NDQyIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz4KPC9zdmc+Cg=='
  }
</script>

<template>
    <div class="friendly-links-container">
      <!-- 装饰性背景元素 -->
      <div class="bg-decoration">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
      </div>
  
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <div class="title-section">
            <div class="title-icon">
              <el-icon size="28" color="#409EFF">
                <Link />
              </el-icon>
            </div>
            <div class="title-text">
              <h2 class="page-title">友情链接管理</h2>
              <p class="page-subtitle">管理您的友情链接，建立更好的网站关系</p>
            </div>
          </div>
          <div class="header-actions">
            <div class="stats-info">
              <span class="stats-text">总计 {{ linkList.length }} 个链接</span>
            </div>
            <el-button 
              type="primary" 
              size="large"
              @click="handleAdd"
              :icon="Plus"
              class="add-button"
            >
              <span>添加链接</span>
            </el-button>
          </div>
        </div>
      </div>
  
      <!-- 友情链接表格 -->
      <el-card class="table-card" shadow="hover">
        <div class="table-header">
          <div class="table-title">
            <el-icon color="#409EFF" size="20">
              <Grid />
            </el-icon>
            <span>链接列表</span>
          </div>
          <div class="table-actions">
            <el-button 
              :icon="Refresh" 
              circle 
              @click="loadData"
              :loading="loading"
              title="刷新数据"
            />
          </div>
        </div>
        
        <el-table 
          :data="linkList" 
          v-loading="loading"
          stripe
          style="width: 100%"
          :header-cell-style="tableHeaderStyle"
          :row-style="tableRowStyle"
          class="beautiful-table"
        >
          <el-table-column prop="index" label="序号" width="120" align="center">
            <template #default="{ $index }">
              <div class="index-cell">
                <span class="index-number">{{ $index + 1 }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column label="链接Logo" width="250" align="center">
            <template #default="{ row }">
              <div class="logo-container">
                <div class="logo-wrapper">
                  <img 
                    :src="row.linkLogoUrl" 
                    :alt="row.linkTitle"
                    class="link-logo"
                    @error="handleImageError"
                  />
                  <div class="logo-overlay"></div>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="linkTitle" label="链接标题" width="250" align="center">
            <template #default="{ row }">
              <div class="title-cell">
                <div class="link-title">{{ row.linkTitle }}</div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="linkAddTime" label="添加时间" width="160" align="center">
            <template #default="{ row }">
              <div class="time-cell">
                <el-icon size="20" color="#0e0e0f">
                  <Clock />
                </el-icon>
                <span>{{ formatTime(row.linkAddTime) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column label="友情链接相关操作" min-width="220" align="center" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button 
                  type="success" 
                  size="small" 
                  @click="handleView(row)"
                  :icon="View"
                  plain
                  class="action-btn"
                >
                  查看
                </el-button>
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleEdit(row)"
                  :icon="Edit"
                  plain
                  class="action-btn"
                >
                  编辑
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="handleDelete(row)"
                  :icon="Delete"
                  plain
                  class="action-btn"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
  
        <!-- 空状态 -->
        <div v-if="!loading && linkList.length === 0" class="empty-state">
          <el-empty 
            description="暂无友情链接数据"
            :image-size="120"
          >
            <el-button type="primary" @click="handleAdd" :icon="Plus">
              添加第一个链接
            </el-button>
          </el-empty>
        </div>
      </el-card>
  
      <!-- 添加/编辑对话框 -->
      <el-dialog 
        v-model="dialogVisible" 
        :title="dialogTitle"
        width="600px"
        :close-on-click-modal="false"
        destroy-on-close
        class="beautiful-dialog"
      >
       
        
        <el-form 
          :model="linkForm" 
          :rules="formRules" 
          ref="formRef"
          label-width="100px"
          class="dialog-form"
        >
          <el-form-item label="链接标题" prop="linkTitle">
            <el-input 
              v-model="linkForm.linkTitle" 
              placeholder="请输入链接标题"
              clearable
              :prefix-icon="Document"
            />
          </el-form-item>
          
          <el-form-item label="链接地址" prop="linkUrl">
            <el-input 
              v-model="linkForm.linkUrl" 
              placeholder="请输入链接地址（如：https://example.com）"
              clearable
              :prefix-icon="Link"
            />
          </el-form-item>
          
          <el-form-item label="Logo地址" prop="linkLogoUrl">
            <el-input 
              v-model="linkForm.linkLogoUrl" 
              placeholder="请输入Logo图片地址"
              clearable
              :prefix-icon="Picture"
            />
          </el-form-item>
          
          <el-form-item label="排序" prop="linkSort">
            <el-input-number 
              v-model="linkForm.linkSort" 
              :min="0" 
              :max="9999"
              placeholder="请输入排序号"
              style="width: 100%"
              controls-position="right"
            />
            <div class="form-tip">
              <el-icon size="12" color="#909399">
                <InfoFilled />
              </el-icon>
              <span>数字越小排序越靠前</span>
            </div>
          </el-form-item>
        </el-form>
        
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false" size="large">
              <el-icon><Close /></el-icon>
              <span>取消</span>
            </el-button>
            <el-button 
              type="primary" 
              @click="handleSubmit" 
              :loading="submitting"
              size="large"
            >
              <el-icon><component :is="isEdit ? Edit : Plus" /></el-icon>
              <span>{{ isEdit ? '更新' : '添加' }}</span>
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
</template>
  
<style scoped>
  .friendly-links-container {
    padding: 32px;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    min-height: 100vh;
    position: relative;
    overflow: hidden;
  }
  
  /* 装饰性背景元素 */
  .bg-decoration {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 0;
  }
  
  .decoration-circle {
    position: absolute;
    border-radius: 50%;
    background: linear-gradient(45deg, rgba(64, 158, 255, 0.1), rgba(103, 194, 58, 0.1));
    animation: float 6s ease-in-out infinite;
  }
  
  .circle-1 {
    width: 200px;
    height: 200px;
    top: -100px;
    right: -100px;
    animation-delay: 0s;
  }
  
  .circle-2 {
    width: 150px;
    height: 150px;
    bottom: -75px;
    left: -75px;
    animation-delay: 2s;
  }
  
  .circle-3 {
    width: 100px;
    height: 100px;
    top: 50%;
    right: 10%;
    animation-delay: 4s;
  }
  
  @keyframes float {
    0%, 100% { transform: translateY(0px) rotate(0deg); }
    33% { transform: translateY(-20px) rotate(120deg); }
    66% { transform: translateY(10px) rotate(240deg); }
  }
  
  /* 页面头部 */
  .page-header {
    position: relative;
    z-index: 1;
    margin-bottom: 32px;
  }
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    padding: 24px 32px;
    border-radius: 16px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
  }
  
  .title-section {
    display: flex;
    align-items: center;
    gap: 16px;
  }
  
  .title-icon {
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #409EFF, #67C23A);
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 16px rgba(64, 158, 255, 0.3);
  }
  
  .title-text {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }
  
  .page-title {
    font-size: 28px;
    font-weight: 700;
    color: #303133;
    margin: 0;
    background: linear-gradient(135deg, #303133, #409EFF);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
  
  .page-subtitle {
    font-size: 14px;
    color: #909399;
    margin: 0;
  }
  
  .header-actions {
    display: flex;
    align-items: center;
    gap: 20px;
  }
  
  .stats-info {
    padding: 8px 16px;
    background: rgba(64, 158, 255, 0.1);
    border-radius: 20px;
    border: 1px solid rgba(64, 158, 255, 0.2);
  }
  
  .stats-text {
    font-size: 14px;
    color: #409EFF;
    font-weight: 500;
  }
  
  .add-button {
    padding: 12px 24px;
    border-radius: 12px;
    font-weight: 600;
    box-shadow: 0 4px 16px rgba(64, 158, 255, 0.3);
    transition: all 0.3s ease;
  }
  
  .add-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(64, 158, 255, 0.4);
  }
  
  /* 表格卡片 */
  .table-card {
    position: relative;
    z-index: 1;
    border-radius: 20px;
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    overflow: hidden;
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background: linear-gradient(135deg, #fafbfc, #f0f2f5);
    border-bottom: 1px solid #e4e7ed;
  }
  
  .table-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  .table-actions {
    display: flex;
    gap: 8px;
  }
  
  /* 表格样式 */
  .beautiful-table {
    background: transparent;
  }
  
  .index-cell {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .index-number {
    width: 28px;
    height: 28px;
    background: linear-gradient(135deg, #409EFF, #67C23A);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: 600;
  }
  
  .logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .logo-wrapper {
    position: relative;
    width: 130px;
    height: 70px;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
  }
  
  .logo-wrapper:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
  }
  
  .link-logo {
    width: 100%;
    height: 100%;
    object-fit: contain;
    background-color: #f0f0f0;
    transition: all 0.3s ease;
  }
  
  .logo-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1));
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  .logo-wrapper:hover .logo-overlay {
    opacity: 1;
  }
  
  .title-cell {
    display: flex;
    align-items: center;
    flex-direction: column;
    gap: 4px;
    padding: 8px 0;
  }
  
  .link-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
    line-height: 1.4;
  }
  
  .link-url {
    font-size: 12px;
    color: #909399;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 200px;
  }
  
  .time-cell {
    display: flex;
    align-items: center;
    gap: 6px;
    justify-content: center;
    font-size: 17px;
    color: #0e0e0f;
  }
  
  .action-buttons {
    display: flex;
    gap: 6px;
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .action-btn {
    padding: 6px 12px;
    border-radius: 8px;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.3s ease;
  }
  
  .action-btn:hover {
    transform: translateY(-1px);
  }
  
  /* 空状态 */
  .empty-state {
    padding: 60px 20px;
    text-align: center;
  }
  
  /* 对话框样式 */
  .beautiful-dialog {
    border-radius: 16px;
    overflow: hidden;
  }
  
  .dialog-icon {
    text-align: center;
    margin-bottom: 20px;
    padding: 16px;
    background: linear-gradient(135deg, #f0f9ff, #e0f2fe);
    border-radius: 12px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 24px;
  }
  
  .dialog-form {
    padding: 0 8px;
  }
  
  .form-tip {
    display: flex;
    align-items: center;
    gap: 4px;
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: center;
    gap: 16px;
    padding-top: 24px;
    border-top: 1px solid #e4e7ed;
  }
  
  /* 全局样式覆盖 */
  :deep(.el-table) {
    background: transparent;
  }
  
  :deep(.el-table tr) {
    background: rgba(255, 255, 255, 0.8);
    transition: all 0.3s ease;
  }
  
  :deep(.el-table tr:hover) {
    background: rgba(64, 158, 255, 0.05);
    transform: translateY(-1px);
  }
  
  :deep(.el-table td) {
    padding: 20px 0;
    border-bottom: 1px solid rgba(228, 231, 237, 0.6);
  }
  
  :deep(.el-table th) {
    padding: 18px 0;
    border-bottom: 2px solid #e4e7ed;
    background: transparent !important;
  }
  
  :deep(.el-dialog__header) {
    padding: 24px 24px 0;
    border-bottom: 1px solid #e4e7ed;
  }
  
  :deep(.el-dialog__title) {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }
  
  :deep(.el-dialog__body) {
    padding: 24px;
  }
  
  :deep(.el-form-item__label) {
    font-weight: 600;
    color: #606266;
  }
  
  :deep(.el-input__wrapper) {
    border-radius: 10px;
    transition: all 0.3s ease;
  }
  
  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  }
  
  :deep(.el-button) {
    border-radius: 10px;
    font-weight: 500;
    transition: all 0.3s ease;
  }
  
  :deep(.el-card__body) {
    padding: 0;
  }
  
  /* 响应式设计 */
  @media (max-width: 1200px) {
    .friendly-links-container {
      padding: 24px;
    }
    
    .header-content {
      padding: 20px 24px;
    }
    
    .page-title {
      font-size: 24px;
    }
  }
  
  @media (max-width: 768px) {
    .friendly-links-container {
      padding: 16px;
    }
    
    .header-content {
      flex-direction: column;
      gap: 20px;
      align-items: flex-start;
    }
    
    .header-actions {
      width: 100%;
      justify-content: space-between;
    }
    
    .page-title {
      font-size: 20px;
    }
    
    .action-buttons {
      flex-direction: column;
      gap: 4px;
    }
    
    .action-btn {
      width: 100%;
    }
  }
</style>