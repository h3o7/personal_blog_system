<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import {
  Plus,
  Edit,
  Delete,
  View,
  Clock,
  Picture,
  Grid,
  Loading,
  Upload,
  InfoFilled,  // 添加信息图标
  Link
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { uploadAd } from '@/api/upload'
import {
  queryAdInfo,
  deleteAd,
  addAd,
  editAd,
  queryAdTypeInfo,
  deleteAdType,
  addAdType,
  editAdType
} from '@/api/ad' // 根据你的项目结构调整路径

// 响应式数据
const adTypeLoading = ref(false)
const adLoading = ref(false)
const adTypeSubmitLoading = ref(false)
const adSubmitLoading = ref(false)

const adTypeList = ref([])
const adList = ref([])
const allAdList = ref([]) // 存储所有广告数据
const selectedAdTypeId = ref('')

const adTypeDialogVisible = ref(false)
const adDialogVisible = ref(false)
const isEditAdType = ref(false)
const isEditAd = ref(false)

const adTypeFormRef = ref()
const adFormRef = ref()

const imageUploading = ref(false)
const uploadRef = ref()
const previewDialogVisible = ref(false)
const previewImageUrl = ref('')

// 广告类型表单
const adTypeForm = reactive({
  adTypeId: '',
  adTypeTitle: '',
  adTypeTag: '',
  adTypeSort: 1
})

// 广告表单
const adForm = reactive({
  adId: '',
  adTypeId: '',
  adTypeTitle: '',
  adTitle: '',
  adImgUrl: '',
  adLinkUrl: '',
  adSort: 1,
  adBeginTime: '',
  adEndTime: ''
})

// 表单验证规则
const adTypeFormRules = {
  adTypeTag: [
    { required: true, message: '请输入类型标识', trigger: 'blur' }
  ],
  adTypeTitle: [
    { required: true, message: '请输入类型主题', trigger: 'blur' }
  ],
  adTypeSort: [
    { required: true, message: '请输入排序号', trigger: 'blur' }
  ]
}

const adFormRules = {
  adTypeId: [
    { required: true, message: '请选择广告类型', trigger: 'change' }
  ],
  adTitle: [
    { required: true, message: '请输入广告标题', trigger: 'blur' }
  ],
  adImgUrl: [
    { required: true, message: '请上传广告图片或输入图片URL', trigger: 'blur' }
  ],
  adLinkUrl: [
    { required: true, message: '请输入广告链接网址', trigger: 'blur' },
    { type: 'url', message: '请输入正确的网址格式', trigger: 'blur' }
  ],
  adSort: [
    { required: true, message: '请输入广告序号', trigger: 'blur' }
  ],
  adBeginTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  adEndTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

// 计算属性
const adTypeDialogTitle = computed(() => isEditAdType.value ? '编辑广告类型' : '新增广告类型')
const adDialogTitle = computed(() => isEditAd.value ? '编辑广告' : '新增广告')

// 当前选中类型名称
const selectedTypeName = computed(() => {
  if (selectedAdTypeId.value === '') {
    return '全部广告'
  }
  const selectedType = adTypeList.value.find(type => type.adTypeId === selectedAdTypeId.value)
  return selectedType ? selectedType.adTypeTitle : '未知类型'
})

// 总广告数量
const totalAdCount = computed(() => allAdList.value.length)

// 根据类型获取广告数量
const getAdCountByType = (typeId) => {
  return allAdList.value.filter(ad => ad.adTypeId === typeId).length
}

// 获取广告类型列表
const getAdTypeList = async () => {
  adTypeLoading.value = true
  try {
    const response = await queryAdTypeInfo()
    if (response.code === 200) {
      adTypeList.value = response.data || []
      // 按排序字段排序
      adTypeList.value.sort((a, b) => a.adTypeSort - b.adTypeSort)
    } else {
      ElMessage.error(response.msg || '获取广告类型列表失败')
    }
  } catch (error) {
    console.error('获取广告类型列表失败:', error)
    ElMessage.error('获取广告类型列表失败')
  } finally {
    adTypeLoading.value = false
  }
}

// 获取所有广告数据
const getAllAdList = async () => {
  try {
    const response = await queryAdInfo('')
    if (response.code === 200) {
      allAdList.value = response.data || []
      // 为每个广告添加对应的类型标题
      allAdList.value.forEach(ad => {
        const adType = adTypeList.value.find(type => type.adTypeId === ad.adTypeId)
        if (adType) {
          ad.adTypeTitle = adType.adTypeTitle
        }
      })
    }
  } catch (error) {
    console.error('获取所有广告列表失败:', error)
  }
}

// 获取广告列表
const getAdList = async (adTypeId = '') => {
  adLoading.value = true
  try {
    if (adTypeId === '') {
      // 显示所有广告
      adList.value = [...allAdList.value]
    } else {
      // 根据类型筛选广告
      adList.value = allAdList.value.filter(ad => ad.adTypeId === adTypeId)
    }
  } catch (error) {
    console.error('筛选广告列表失败:', error)
    ElMessage.error('筛选广告列表失败')
  } finally {
    adLoading.value = false
  }
}

// 选择广告类型
const handleSelectAdType = async (typeId) => {
  selectedAdTypeId.value = typeId
  await getAdList(typeId)
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

// 广告类型操作
const handleAddAdType = () => {
  isEditAdType.value = false
  adTypeDialogVisible.value = true
  resetAdTypeForm()
}

const handleEditAdType = (row) => {
  isEditAdType.value = true
  adTypeDialogVisible.value = true
  Object.assign(adTypeForm, row)
}

const handleDeleteAdType = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确认要删除广告类型 "${row.adTypeTitle}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
      }
    )

    const response = await deleteAdType(row.adTypeId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await getAdTypeList()
      await getAllAdList()
      await getAdList(selectedAdTypeId.value)
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除广告类型失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleAdTypeSubmit = async () => {
  try {
    await adTypeFormRef.value.validate()

    adTypeSubmitLoading.value = true

    let response
    if (isEditAdType.value) {
      response = await editAdType(adTypeForm)
    } else {
      response = await addAdType(adTypeForm)
    }

    if (response.code === 200) {
      ElMessage.success(isEditAdType.value ? '编辑成功' : '新增成功')
      handleAdTypeDialogClose()
      await getAdTypeList()
      await getAllAdList()
      await getAdList(selectedAdTypeId.value)
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    adTypeSubmitLoading.value = false
  }
}

const handleAdTypeDialogClose = () => {
  adTypeDialogVisible.value = false
  resetAdTypeForm()
}

const resetAdTypeForm = () => {
  Object.assign(adTypeForm, {
    adTypeId: '',
    adTypeTitle: '',
    adTypeTag: '',
    adTypeSort: 1
  })
  adTypeFormRef.value?.clearValidate()
}

// 广告操作
const handleAddAd = () => {
  isEditAd.value = false
  adDialogVisible.value = true
  resetAdForm()
  // 如果当前选中了某个类型，默认选择该类型
  if (selectedAdTypeId.value) {
    adForm.adTypeId = selectedAdTypeId.value
  }
}

const handleEditAd = (row) => {
  isEditAd.value = true
  adDialogVisible.value = true
  Object.assign(adForm, row)
}

const handleDeleteAd = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确认要删除广告 "${row.adTitle}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
      }
    )

    const response = await deleteAd(row.adId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await getAllAdList()
      await getAdList(selectedAdTypeId.value)
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除广告失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleViewAd = (row) => {
  if (row.adLinkUrl) {
    window.open(row.adLinkUrl, '_blank')
  } else {
    ElMessage.warning('该广告没有设置链接地址')
  }
}

const handleAdSubmit = async () => {
  try {
    await adFormRef.value.validate()

    adSubmitLoading.value = true

    let response
    if (isEditAd.value) {
      response = await editAd(adForm)
    } else {
      response = await addAd(adForm)
    }

    if (response.code === 200) {
      ElMessage.success(isEditAd.value ? '编辑成功' : '新增成功')
      handleAdDialogClose()
      await getAllAdList()
      await getAdList(selectedAdTypeId.value)
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    adSubmitLoading.value = false
  }
}

const handleAdDialogClose = () => {
  adDialogVisible.value = false
  resetAdForm()
}

const resetAdForm = () => {
  Object.assign(adForm, {
    adId: '',
    adTypeId: '',
    adTypeTitle: '',
    adTitle: '',
    adImgUrl: '',
    adLinkUrl: '',
    adSort: 1,
    adBeginTime: '',
    adEndTime: ''
  })
  imageUploading.value = false
  previewDialogVisible.value = false
  previewImageUrl.value = ''
  adFormRef.value?.clearValidate()
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片格式的文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
    return false
  }

  imageUploading.value = true
  return true
}

// 自定义上传方法
const customUpload = async (options) => {
  try {
    const response = await uploadAd(options.file)

    if (response.code === 200) {
      // 根据你的后端返回格式调整
      adForm.adImgUrl = response.data.url || response.data
      ElMessage.success('图片上传成功')

      // 触发表单验证
      adFormRef.value?.validateField('adImgUrl')
    } else {
      throw new Error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('图片上传失败:', error)
    ElMessage.error(error.message || '图片上传失败')
  } finally {
    imageUploading.value = false
  }
}

// 上传成功回调（如果使用默认上传方式）
const handleImageSuccess = (response) => {
  imageUploading.value = false
  if (response.code === 200) {
    adForm.adImgUrl = response.data.url || response.data
    ElMessage.success('图片上传成功')
    adFormRef.value?.validateField('adImgUrl')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

// 上传失败回调
const handleImageError = (error) => {
  imageUploading.value = false
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败，请重试')
}

// 预览图片
const handlePreviewImage = () => {
  if (adForm.adImgUrl) {
    previewImageUrl.value = adForm.adImgUrl
    previewDialogVisible.value = true
  } else {
    ElMessage.warning('没有可预览的图片')
  }
}
const handlePreviewClose = () => {
  previewDialogVisible.value = false
  previewImageUrl.value = ''
}

// 在新窗口打开图片
const openImageInNewTab = () => {
  if (previewImageUrl.value) {
    window.open(previewImageUrl.value, '_blank')
  }
}

// 删除图片
const removeImage = () => {
  ElMessageBox.confirm(
    '删除后可以重新上传新的图片，确认删除这张图片吗？',
    '确认删除',
    {
      type: 'warning',
      confirmButtonText: '确认删除',
      cancelButtonText: '取消'
    }
  ).then(() => {
    adForm.adImgUrl = ''
    ElMessage.success('图片已删除，可以重新上传')
    adFormRef.value?.validateField('adImgUrl')
  }).catch(() => {
    // 用户取消删除
  })
}

// 处理手动输入URL


// 页面加载时获取数据
onMounted(async () => {
  await getAdTypeList()
  await getAllAdList()
  await getAdList('')
})
</script>

<template>
  <div class="ad-management">
    <!-- 页面标题 -->


    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧广告类型区域 -->
      <div class="left-sidebar">
        <el-card class="type-sidebar-card" shadow="hover">
          <template #header>
            <div class="sidebar-header">
              <span class="sidebar-title">广告类型</span>
              <el-button type="primary" :icon="Plus" size="small" @click="handleAddAdType">
                新增
              </el-button>
            </div>
          </template>

          <div class="type-list" v-loading="adTypeLoading">
            <!-- 全部广告选项 -->
            <div class="type-item all-type" :class="{ active: selectedAdTypeId === '' }"
              @click="handleSelectAdType('')">
              <div class="type-content">
                <el-icon class="type-icon">
                  <Grid />
                </el-icon>
                <div class="type-info">
                  <span class="type-title">全部广告</span>
                  <span class="type-count">{{ totalAdCount }}</span>
                </div>
              </div>
            </div>

            <!-- 广告类型列表 -->
            <div v-for="type in adTypeList" :key="type.adTypeId" class="type-item"
              :class="{ active: selectedAdTypeId === type.adTypeId }" @click="handleSelectAdType(type.adTypeId)">
              <div class="type-content">
                <div class="type-tag">
                  {{ type.adTypeTag }}
                </div>
                <div class="type-info">
                  <span class="type-title">{{ type.adTypeTitle }}</span>
                  <span class="type-count">{{ getAdCountByType(type.adTypeId) }}</span>
                </div>
              </div>
              <div class="type-actions">
                <el-button type="primary" :icon="Edit" size="small" circle @click.stop="handleEditAdType(type)" />
                <el-button type="danger" :icon="Delete" size="small" circle @click.stop="handleDeleteAdType(type)" />
              </div>
            </div>

            <!-- 空状态 -->
            <div v-if="adTypeList.length === 0 && !adTypeLoading" class="empty-state">
              <el-icon class="empty-icon">
                <DocumentEmpty />
              </el-icon>
              <p class="empty-text">暂无广告类型</p>
              <el-button type="primary" @click="handleAddAdType">创建第一个类型</el-button>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧广告管理区域 -->
      <div class="right-content">
        <el-card class="ad-content-card" shadow="hover">
          <template #header>
            <div class="content-header">
              <div class="header-left">
                <span class="content-title">
                  {{ selectedTypeName }}
                </span>
                <el-tag type="info" effect="plain" size="small">
                  共 {{ adList.length }} 条广告
                </el-tag>
              </div>
              <el-button type="success" :icon="Plus" @click="handleAddAd">
                新增广告
              </el-button>
            </div>
          </template>

          <!-- 广告表格 -->
          <el-table :data="adList" v-loading="adLoading" class="ad-table"
            :header-cell-style="{ background: '#f8f9fa', color: '#2c3e50', fontWeight: '600' }"
            :row-style="{ height: '80px' }" empty-text="暂无广告数据">
            <el-table-column type="index" label="序号" width="70" align="center" />

            <el-table-column label="广告图片" width="100" align="center">
              <template #default="{ row }">
                <el-image :src="row.adImgUrl" :preview-src-list="[row.adImgUrl]" class="ad-image" fit="cover"
                  :lazy="true">
                  <template #error>
                    <div class="image-slot">
                      <el-icon>
                        <Picture />
                      </el-icon>
                    </div>
                  </template>
                </el-image>
              </template>
            </el-table-column>

            <el-table-column prop="adTypeTitle" label="广告类型" width="120" align="center">
              <template #default="{ row }">
                <el-tag type="success" size="small">{{ row.adTypeTitle }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="adTitle" label="广告标题" min-width="200" show-overflow-tooltip />

            <el-table-column prop="adBeginTime" label="开始时间" width="180" align="center">
              <template #default="{ row }">
                <div class="time-info">
                  <el-icon class="time-icon">
                    <Clock />
                  </el-icon>
                  <span>{{ formatTime(row.adBeginTime) }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="adEndTime" label="结束时间" width="180" align="center">
              <template #default="{ row }">
                <div class="time-info">
                  <el-icon class="time-icon">
                    <Clock />
                  </el-icon>
                  <span>{{ formatTime(row.adEndTime) }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="200" align="center" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-tooltip content="查看广告" placement="top">
                    <el-button type="info" :icon="View" size="small" color="#409eff" circle
                      @click="handleViewAd(row)" />
                  </el-tooltip>
                  <el-tooltip content="编辑广告" placement="top">
                    <el-button type="primary" :icon="Edit" size="small" circle @click="handleEditAd(row)" />
                  </el-tooltip>
                  <el-tooltip content="删除广告" placement="top">
                    <el-button type="danger" :icon="Delete" size="small" circle @click="handleDeleteAd(row)" />
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <!-- 广告类型对话框 -->
    <el-dialog v-model="adTypeDialogVisible" :title="adTypeDialogTitle" width="500px"
      :before-close="handleAdTypeDialogClose">
      <el-form ref="adTypeFormRef" :model="adTypeForm" :rules="adTypeFormRules" label-width="100px">
        <el-form-item label="类型标识" prop="adTypeTag">
          <el-input v-model="adTypeForm.adTypeTag" placeholder="请输入类型标识" />
        </el-form-item>
        <el-form-item label="类型主题" prop="adTypeTitle">
          <el-input v-model="adTypeForm.adTypeTitle" placeholder="请输入类型主题" />
        </el-form-item>
        <el-form-item label="排序" prop="adTypeSort">
          <el-input-number v-model="adTypeForm.adTypeSort" :min="1" :max="999" placeholder="请输入排序号"
            style="width: 100%" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleAdTypeDialogClose">取消</el-button>
          <el-button type="primary" @click="handleAdTypeSubmit" :loading="adTypeSubmitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>


    <!-- 广告对话框 - 完善版本 -->
    <el-dialog v-model="adDialogVisible" :title="adDialogTitle" width="600px" :before-close="handleAdDialogClose">
      <el-form ref="adFormRef" :model="adForm" :rules="adFormRules" label-width="120px">
        <el-form-item label="广告类型" prop="adTypeId">
          <el-select v-model="adForm.adTypeId" placeholder="请选择广告类型" style="width: 100%">
            <el-option v-for="type in adTypeList" :key="type.adTypeId" :label="type.adTypeTitle"
              :value="type.adTypeId" />
          </el-select>
        </el-form-item>

        <el-form-item label="广告标题" prop="adTitle">
          <el-input v-model="adForm.adTitle" placeholder="请输入广告标题" />
        </el-form-item>

        <!-- 完善：广告图片上传 -->
        <el-form-item label="广告图片" prop="adImgUrl">
          <div class="upload-section">
            <el-upload ref="uploadRef" class="image-uploader" :action="uploadAction" :headers="uploadHeaders"
              :show-file-list="false" :on-success="handleImageSuccess" :on-error="handleImageError"
              :before-upload="beforeImageUpload" :http-request="customUpload" accept="image/*"
              :disabled="!!adForm.adImgUrl">
              <div v-if="adForm.adImgUrl" class="image-preview-container">
                <div class="image-preview">
                  <el-image :src="adForm.adImgUrl" class="uploaded-image" fit="contain" :preview-disabled="true">
                    <template #error>
                      <div class="image-error">
                        <el-icon>
                          <Picture />
                        </el-icon>
                        <span>加载失败</span>
                      </div>
                    </template>
                  </el-image>
                  <div class="image-overlay">
                    <el-tooltip content="预览图片" placement="top">
                      <el-button type="primary" :icon="View" circle size="small" @click.stop="handlePreviewImage" />
                    </el-tooltip>
                    <el-tooltip content="删除图片" placement="top">
                      <el-button type="danger" :icon="Delete" circle size="small" @click.stop="removeImage" />
                    </el-tooltip>
                  </div>
                </div>
                <!-- 图片信息显示 -->
                <div class="image-info">
                  <el-text type="info" size="small">
                    <el-icon>
                      <InfoFilled />
                    </el-icon>
                  </el-text>
                </div>
              </div>
              <div v-else class="upload-placeholder">
                <el-icon class="upload-main-icon" :class="{ 'is-loading': imageUploading }">
                  <component :is="imageUploading ? 'Loading' : 'Upload'" />
                </el-icon>
                <div class="upload-tip" align="center">
                  {{ imageUploading ? '上传中...' : '点击上传图片' }}<br>
                  支持 jpg/png/gif 格式，大小不超过 2MB
                </div>
              </div>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="广告链接URL" prop="adLinkUrl">
          <el-input v-model="adForm.adLinkUrl" placeholder="请输入广告链接网址" />
        </el-form-item>

        <el-form-item label="广告序号" prop="adSort">
          <el-input-number v-model="adForm.adSort" :min="1" :max="999" placeholder="请输入广告序号" style="width: 100%" />
        </el-form-item>

        <el-form-item label="开始时间" prop="adBeginTime">
          <el-date-picker v-model="adForm.adBeginTime" type="datetime" placeholder="选择开始时间" style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>

        <el-form-item label="结束时间" prop="adEndTime">
          <el-date-picker v-model="adForm.adEndTime" type="datetime" placeholder="选择结束时间" style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleAdDialogClose">取消</el-button>
          <el-button type="primary" @click="handleAdSubmit" :loading="adSubmitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewDialogVisible" title="图片预览" width="80%" :before-close="handlePreviewClose"
      class="preview-dialog">
      <div class="preview-container">
        <el-image :src="previewImageUrl" class="preview-image" fit="contain" :zoom-rate="1.2" :max-scale="7"
          :min-scale="0.2">
          <template #error>
            <div class="preview-error">
              <el-icon>
                <Picture />
              </el-icon>
              <span>图片加载失败</span>
            </div>
          </template>
        </el-image>
      </div>

      <template #footer>
        <div class="preview-footer">
          <el-button @click="handlePreviewClose">关闭</el-button>
          <el-button type="primary" @click="openImageInNewTab">
            <el-icon>
              <Link />
            </el-icon>
            在新窗口打开
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>



<style scope>
/* 上传组件样式优化 */
.upload-section {
  width: 100%;
  display: flex;
  justify-content: center; /* 让整个上传区域居中 */
}

.image-uploader {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  background: #fafafa;
  display: flex;
  justify-content: center; /* 内容居中 */
  align-items: center; /* 垂直居中 */
}

.image-uploader:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.image-uploader.is-disabled {
  cursor: not-allowed;
  border-color: #e4e7ed;
  background: #f5f7fa;
}

/* 上传占位符样式 - 居中显示 */
.upload-placeholder {
  width: 300px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  text-align: center; /* 文字居中 */
}

.upload-main-icon {
  font-size: 48px;
  color: #8c939d;
  margin-bottom: 16px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center; /* 图标居中 */
}

.image-uploader:hover .upload-main-icon {
  color: #409eff;
  transform: scale(1.1);
}

.upload-main-icon.is-loading {
  animation: rotating 2s linear infinite;
  color: #409eff;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  text-align: center;
  line-height: 1.4;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 图片预览容器优化 - 居中显示 */
.image-preview-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center; /* 垂直居中 */
}

.image-preview {
  position: relative;
  width: 300px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center; /* 确保图片在容器内居中 */
}

.uploaded-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 确保图片完全适应容器并居中 */
:deep(.uploaded-image .el-image__inner) {
  width: 100% !important;
  height: 100% !important;
  object-fit: contain !important;
  object-position: center !important; /* 图片居中定位 */
  background: #fff;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

.image-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
  text-align: center;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 8px;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.image-info {
  margin-top: 12px;
  text-align: center;
  display: flex;
  justify-content: center; /* 提示信息居中 */
}

.image-info .el-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 12px;
}

/* 预览对话框样式优化 */
.preview-dialog {
  .el-dialog__body {
    padding: 20px;
    background: #f5f7fa;
  }
  
  .el-dialog {
    max-width: 90vw; /* 限制最大宽度 */
    max-height: 90vh; /* 限制最大高度 */
  }
  
  .el-dialog__body {
    max-height: 70vh; /* 限制内容区域高度 */
    overflow: auto; /* 如果内容过多允许滚动 */
  }
}

.preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh; /* 使用视口高度而不是固定高度 */
  max-height: 70vh; /* 限制最大高度 */
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  overflow: hidden; /* 防止内容溢出 */
}

.preview-image {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  object-fit: contain; /* 确保完整显示 */
}

/* 确保预览图片完整显示 */
:deep(.preview-image .el-image__inner) {
  max-width: 100% !important;
  max-height: 100% !important;
  width: auto !important;
  height: auto !important;
  object-fit: contain !important;
  object-position: center !important;
  border-radius: 8px;
}

.preview-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 200px;
  height: 200px;
  color: #909399;
  font-size: 16px;
  text-align: center;
}

.preview-error .el-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.preview-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  padding: 10px 0;
}

/* 响应式设计优化 */
@media (max-width: 768px) {
  .upload-placeholder,
  .image-preview {
    width: 100%;
    max-width: 280px;
    height: 180px;
  }

  .upload-main-icon {
    font-size: 36px;
  }

  .preview-container {
    min-height: 50vh;
    max-height: 60vh;
    padding: 15px;
  }

  .preview-image {
    max-height: 50vh;
  }
  
  .preview-dialog .el-dialog {
    max-width: 95vw;
    margin: 5vh auto;
  }
}

@media (max-width: 480px) {
  .image-preview {
    height: 150px;
  }

  .upload-placeholder {
    height: 150px;
  }

  .image-overlay {
    gap: 12px;
  }

  .image-overlay .el-button {
    padding: 8px;
  }

  .preview-container {
    min-height: 40vh;
    max-height: 50vh;
    padding: 10px;
  }
  
  .preview-image {
    max-height: 40vh;
  }
}

/* 其他样式保持不变... */
.ad-management {
  padding: 0;
  background: transparent;
}

.page-header {
  margin-bottom: 30px;
  padding: 20px 0;
  text-align: center;
}

.page-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 10px 0;
  background: linear-gradient(135deg, #2c3e50 0%, #667eea 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 1rem;
  color: #7f8c8d;
  margin: 0;
  font-weight: 500;
}

/* 主要内容布局 */
.main-content {
  display: flex;
  gap: 20px;
  height: calc(100vh - 200px);
}

/* 左侧边栏 */
.left-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.type-sidebar-card {
  border-radius: 15px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sidebar-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
}

.type-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: calc(100vh - 320px);
  overflow-y: auto;
}

.type-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.type-item:hover {
  background: rgba(102, 126, 234, 0.05);
  border-color: rgba(102, 126, 234, 0.2);
}

.type-item.active {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
}

.all-type .type-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.type-icon {
  font-size: 20px;
  color: #667eea;
}

.type-content {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.type-tag {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}

.type-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.type-title {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  line-height: 1.2;
}

.type-count {
  font-size: 12px;
  color: #7f8c8d;
}

.type-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.type-item:hover .type-actions {
  opacity: 1;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #7f8c8d;
}

.empty-icon {
  font-size: 48px;
  color: #d3d4d6;
  margin-bottom: 16px;
}

.empty-text {
  margin: 0 0 16px 0;
  font-size: 14px;
}

/* 右侧内容区域 */
.right-content {
  flex: 1;
  min-width: 0;
}

.ad-content-card {
  border-radius: 15px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.content-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
}

.ad-table {
  border-radius: 10px;
  overflow: hidden;
}

.ad-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}

.time-info {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.time-icon {
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}

/* 滚动条样式 */
.type-list::-webkit-scrollbar {
  width: 6px;
}

.type-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.type-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.type-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .left-sidebar {
    width: 250px;
  }
}

@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .left-sidebar {
    width: 100%;
    order: 2;
  }

  .right-content {
    order: 1;
  }

  .type-list {
    max-height: 300px;
  }

  .page-title {
    font-size: 1.8rem;
  }

  .content-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .header-left {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .main-content {
    gap: 15px;
  }

  .type-item {
    padding: 10px 12px;
  }

  .type-tag {
    width: 32px;
    height: 32px;
    font-size: 10px;
  }

  .type-title {
    font-size: 13px;
  }

  .type-count {
    font-size: 11px;
  }
}
</style>