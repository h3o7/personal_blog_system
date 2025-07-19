<template>
    <div class="user-layout">
      <div class="user-container">
        <!-- 左侧菜单栏 -->
        <div class="sidebar">
          <div class="sidebar-header">
            <h2>个人中心</h2>
          </div>
          <el-menu
            :default-active="activeMenu"
            router
            class="user-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item :index="`/users/userLayout/profile/${currentUserId}`">
              <el-icon><User /></el-icon>
              <span>我的主页</span>
            </el-menu-item>
            <el-menu-item :index="`/users/userLayout/following/${currentUserId}`">
              <el-icon><Plus /></el-icon>
              <span>我的关注</span>
            </el-menu-item>
            <el-menu-item :index="`/users/userLayout/followers/${currentUserId}`">
              <el-icon><UserFilled /></el-icon>
              <span>我的粉丝</span>
            </el-menu-item>
            <el-menu-item :index="`/users/userLayout/myArticles/${currentUserId}`">
              <el-icon><Edit /></el-icon>
              <span>我的文章</span>
            </el-menu-item>
          </el-menu>
        </div>
  
        <!-- 主要内容区域（全宽，无右侧边栏） -->
        <div class="main-content">
          <router-view />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRoute } from 'vue-router'
  import { User, UserFilled, Plus, Edit } from '@element-plus/icons-vue'
  
  const route = useRoute()
  
  const currentUserId = ref('')
  const activeMenu = computed(() => route.path)
  
  // 获取当前用户ID
  const getCurrentUserId = () => {
    try {
      const loginData = localStorage.getItem('login')
      if (loginData) {
        const login = JSON.parse(loginData)
        currentUserId.value = login.id
      }
    } catch (error) {
      console.error('获取用户ID失败:', error)
    }
  }
  
  const handleMenuSelect = (index) => {
    console.log('选择菜单:', index)
  }
  
  onMounted(() => {
    getCurrentUserId()
  })
  </script>
  
  <style scoped>
  .user-layout {
    min-height: calc(100vh - 140px);
    background: var(--el-bg-color-page);
  }
  
  .user-container {
    max-width: 1400px;
    margin: 0 auto;
    display: flex;
    gap: 24px;
    padding: 24px;
    min-height: calc(100vh - 140px);
  }
  
  /* 左侧菜单栏 */
  .sidebar {
    width: 240px;
    background: var(--el-bg-color);
    border-radius: 12px;
    box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    height: fit-content;
    position: sticky;
    top: 24px;
  }
  
  .sidebar-header {
    padding: 24px 20px;
    border-bottom: 1px solid var(--el-border-color-light);
    background: linear-gradient(135deg, var(--el-color-primary), var(--el-color-primary-dark-2));
    color: white;
    text-align: center;
  }
  
  .sidebar-header h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  
  .user-menu {
    border: none;
    padding: 12px 0;
  }
  
  .user-menu .el-menu-item {
    padding: 16px 24px;
    margin: 4px 12px;
    border-radius: 8px;
    transition: all 0.3s ease;
    font-weight: 500;
    position: relative;
  }
  
  .user-menu .el-menu-item:hover {
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
    transform: translateX(4px);
  }
  
  .user-menu .el-menu-item.is-active {
    background: var(--el-color-primary-light-8);
    color: var(--el-color-primary);
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  }
  
  .user-menu .el-menu-item.is-active::before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 4px;
    height: 20px;
    background: var(--el-color-primary);
    border-radius: 0 2px 2px 0;
  }
  
  .user-menu .el-menu-item .el-icon {
    margin-right: 12px;
    font-size: 18px;
  }
  
  /* 主要内容区域（占据剩余全部空间） */
  .main-content {
    flex: 1;
    background: var(--el-bg-color);
    border-radius: 16px;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
    overflow: hidden;
    min-height: 600px;
  }
  
  /* 响应式设计 */
  @media (max-width: 1200px) {
    .user-container {
      max-width: 100%;
      padding: 16px;
    }
    
    .sidebar {
      width: 220px;
    }
  }
  
  @media (max-width: 768px) {
    .user-container {
      flex-direction: column;
      padding: 16px;
    }
    
    .sidebar {
      width: 100%;
      position: static;
      order: -1;
    }
    
    .sidebar-header {
      text-align: left;
      padding: 20px;
    }
    
    .user-menu .el-menu-item {
      padding: 14px 20px;
      margin: 2px 8px;
    }
    
    .main-content {
      min-height: 400px;
    }
  }
  
  @media (max-width: 480px) {
    .user-container {
      padding: 12px;
      gap: 16px;
    }
    
    .sidebar-header h2 {
      font-size: 16px;
    }
    
    .user-menu .el-menu-item {
      padding: 12px 16px;
      font-size: 14px;
    }
    
    .user-menu .el-menu-item .el-icon {
      font-size: 16px;
      margin-right: 8px;
    }
  }
  </style>