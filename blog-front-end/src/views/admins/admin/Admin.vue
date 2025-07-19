<script setup>
import { onMounted, ref } from 'vue'
import { 
  Document, 
  PriceTag, 
  Monitor, 
  User, 
  Setting, 
  Platform, 
  Connection, 
  Lightning, 
  Plus,
  DataAnalysis
} from '@element-plus/icons-vue'
import request from '@/utils/request.js'

const data = ref({
  articleCount: 0,
  articleTagCount: 0,
  adTypeCount: 0,
  userCount: 0,
  systemName: '',
  systemIP: ''
})

onMounted(async () => {
  try {
    const response = await request.get('/admin')
    data.value = response.data
  } catch (error) {
    console.error('数据获取失败:', error)
    // 如果请求失败，使用你提供的示例数据
    data.value = {
      articleCount: 0,
      articleTagCount: 0,
      adTypeCount: 0,
      userCount: 5,
      systemName: "Mac OS X 14.2.1",
      systemIP: "10.203.233.87"
    }
  }
})
</script>

<template>
  <div class="admin-dashboard">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="title">管理员控制台</h1>
      <p class="subtitle">系统概览与数据统计</p>
    </div>

    <!-- 统计卡片区域 -->
    <div class="stats-grid">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon article">
                <el-icon><Document /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ data.articleCount }}</div>
                <div class="stat-">文章数量</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon tag">
                <el-icon><PriceTag /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ data.articleTagCount }}</div>
                <div class="stat-label">文章标签</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon ad">
                <el-icon><Monitor /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ data.adTypeCount }}</div>
                <div class="stat-">广告类型</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon user">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ data.userCount }}</div>
                <div class="stat-label">用户数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 系统信息区域 -->
    <div class="system-info-section">
      <el-card class="system-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon class="header-icon"><Setting /></el-icon>
            <span class="header-title">系统信息</span>
          </div>
        </template>
        
        <div class="system-info">
          <el-row :gutter="20">
            <el-col :xs="24" :md="12">
              <div class="info-item">
                <div class="info-label">
                  <el-icon><Platform /></el-icon>
                  <span>操作系统</span>
                </div>
                <div class="info-value">{{ data.systemName }}</div>
              </div>
            </el-col>
            
            <el-col :xs="24" :md="12">
              <div class="info-item">
                <div class="info-label">
                  <el-icon><Connection /></el-icon>
                  <span>系统IP</span>
                </div>
                <div class="info-value">{{ data.systemIP }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <el-card class="actions-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon class="header-icon"><Lightning /></el-icon>
            <span class="header-title">快速操作</span>
          </div>
        </template>
        
        <div class="actions-content">
          <el-button type="primary" size="large" :icon="Plus">
            新建文章
          </el-button>
          <el-button type="success" size="large" :icon="User">
            用户管理
          </el-button>
          <el-button type="warning" size="large" :icon="Setting">
            系统设置
          </el-button>
          <el-button type="info" size="large" :icon="DataAnalysis">
            数据分析
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>



<style scoped>
.admin-dashboard {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
}

.title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 10px 0;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
}

.subtitle {
  font-size: 1.1rem;
  color: #7f8c8d;
  margin: 0;
}

.stats-grid {
  margin-bottom: 30px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  border: none;
  margin-bottom: 20px;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.article {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.tag {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.ad {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.user {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 2.2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.9rem;
  color: #7f8c8d;
  font-weight: 500;
}

.system-info-section,
.quick-actions {
  margin-bottom: 30px;
}

.system-card,
.actions-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #2c3e50;
}

.header-icon {
  margin-right: 8px;
  font-size: 18px;
}

.header-title {
  font-size: 1.1rem;
}

.system-info {
  padding: 10px 0;
}

.info-item {
  padding: 15px 0;
  border-bottom: 1px solid #ecf0f1;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #7f8c8d;
  font-weight: 500;
}

.info-label .el-icon {
  margin-right: 8px;
}

.info-value {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  padding-left: 26px;
}

.actions-content {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  justify-content: center;
  padding: 10px 0;
}

.actions-content .el-button {
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.actions-content .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-dashboard {
    padding: 15px;
  }
  
  .title {
    font-size: 2rem;
  }
  
  .actions-content {
    flex-direction: column;
    align-items: center;
  }
  
  .actions-content .el-button {
    width: 100%;
    max-width: 200px;
  }
}
</style>