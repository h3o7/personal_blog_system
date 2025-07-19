<template>
  <div class="dashboard">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">数据概览</h1>
        <p class="page-subtitle">欢迎回来，{{ currentUser.name }}！今天是个美好的一天</p>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" size="large">
          新建内容
        </el-button>
        <el-button :icon="Refresh" size="large" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>
    
    <div class="dashboard-content">
      <!-- 统计卡片 -->
      <el-row :gutter="30" class="stats-row">
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card class="stat-card article-card" shadow="hover">
            <div class="stat-item">
              <div class="stat-icon article">
                <el-icon><Document /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">1,285</div>
                <div class="stat-label">文章总数</div>
                <div class="stat-trend">
                  <el-icon class="trend-up"><CaretTop /></el-icon>
                  <span>+12.5%</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card class="stat-card user-card" shadow="hover">
            <div class="stat-item">
              <div class="stat-icon user">
                <el-icon><UserFilled /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">18,524</div>
                <div class="stat-label">用户总数</div>
                <div class="stat-trend">
                  <el-icon class="trend-up"><CaretTop /></el-icon>
                  <span>+8.2%</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card class="stat-card view-card" shadow="hover">
            <div class="stat-item">
              <div class="stat-icon view">
                <el-icon><View /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">256,789</div>
                <div class="stat-label">浏览总数</div>
                <div class="stat-trend">
                  <el-icon class="trend-up"><CaretTop /></el-icon>
                  <span>+15.7%</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card class="stat-card comment-card" shadow="hover">
            <div class="stat-item">
              <div class="stat-icon comment">
                <el-icon><ChatDotSquare /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">8,924</div>
                <div class="stat-label">评论总数</div>
                <div class="stat-trend">
                  <el-icon class="trend-up"><CaretTop /></el-icon>
                  <span>+6.3%</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表和信息区域 -->
      <el-row :gutter="30" class="charts-row">
        <el-col :xs="24" :lg="16">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span class="card-title">流量趋势</span>
                <el-button-group size="small">
                  <el-button>今日</el-button>
                  <el-button type="primary">本周</el-button>
                  <el-button>本月</el-button>
                </el-button-group>
              </div>
            </template>
            <div class="chart-placeholder">
              <el-icon class="chart-icon"><TrendCharts /></el-icon>
              <p>图表数据加载中...</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :lg="8">
          <el-card class="activity-card" shadow="hover">
            <template #header>
              <span class="card-title">最新动态</span>
            </template>
            <div class="activity-list">
              <div class="activity-item" v-for="item in activities" :key="item.id">
                <el-avatar :size="32" :src="item.avatar" />
                <div class="activity-content">
                  <p class="activity-text">{{ item.text }}</p>
                  <span class="activity-time">{{ item.time }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  Document, 
  UserFilled, 
  View, 
  ChatDotSquare, 
  Plus, 
  Refresh, 
  CaretTop,
  TrendCharts
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const currentUser = ref({
  name: 'h3o7'
})

const activities = ref([
  {
    id: 1,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    text: '用户张三发布了新文章《Vue3实战指南》',
    time: '5分钟前'
  },
  {
    id: 2,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    text: '李四评论了文章《ElementPlus组件库》',
    time: '15分钟前'
  },
  {
    id: 3,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    text: '系统自动备份数据成功',
    time: '1小时前'
  },
  {
    id: 4,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    text: '新用户王五注册成功',
    time: '2小时前'
  }
])

const refreshData = () => {
  ElMessage.success('数据刷新成功')
}
</script>

<style scoped>
.dashboard {
  padding: 0;
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  padding: 30px 0;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding-left: 40px;
  padding-right: 40px;
}

.header-left {
  flex: 1;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: #2c3e50;
  margin: 0 0 10px 0;
  background: linear-gradient(135deg, #2c3e50 0%, #667eea 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin: 0;
  font-weight: 500;
}

.header-right {
  display: flex;
  gap: 15px;
}

.stats-row {
  margin-bottom: 40px;
}

.stat-card {
  border-radius: 20px;
  transition: all 0.3s ease;
  border: none;
  overflow: hidden;
  position: relative;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.article-card::before {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.user-card::before {
  background: linear-gradient(90deg, #43e97b 0%, #38f9d7 100%);
}

.view-card::before {
  background: linear-gradient(90deg, #f093fb 0%, #f5576c 100%);
}

.comment-card::before {
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 25px 20px;
}

.stat-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 32px;
  color: white;
  position: relative;
  overflow: hidden;
}

.stat-icon::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transform: rotate(45deg);
  transition: all 0.5s ease;
  opacity: 0;
}

.stat-card:hover .stat-icon::before {
  opacity: 1;
  animation: shine 0.8s ease-in-out;
}

@keyframes shine {
  0% {
    transform: translateX(-100%) translateY(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) translateY(100%) rotate(45deg);
  }
}

.stat-icon.article {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.user {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon.view {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.comment {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 800;
  color: #2c3e50;
  margin-bottom: 8px;
  line-height: 1;
}

.stat-label {
  font-size: 1rem;
  color: #7f8c8d;
  font-weight: 600;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
  font-weight: 600;
  color: #27ae60;
}

.trend-up {
  color: #27ae60;
}

.charts-row {
  margin-bottom: 40px;
}

.chart-card,
.activity-card {
  border-radius: 20px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.chart-card:hover,
.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: #2c3e50;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #95a5a6;
}

.chart-icon {
  font-size: 80px;
  margin-bottom: 20px;
  opacity: 0.5;
}

.activity-list {
  max-height: 350px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 15px 0;
  border-bottom: 1px solid #f1f2f6;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-content {
  margin-left: 15px;
  flex: 1;
}

.activity-text {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 0.9rem;
  line-height: 1.5;
}

.activity-time {
  color: #95a5a6;
  font-size: 0.8rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
    padding: 20px;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .page-subtitle {
    font-size: 1rem;
  }
  
  .header-right {
    width: 100%;
    justify-content: center;
  }
  
  .stat-item {
    padding: 20px 15px;
  }
  
  .stat-icon {
    width: 60px;
    height: 60px;
    font-size: 24px;
  }
  
  .stat-number {
    font-size: 2rem;
  }
}
</style>