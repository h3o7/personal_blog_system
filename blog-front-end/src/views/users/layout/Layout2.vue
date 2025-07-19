<template>
    <div class="user-layout">
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <router-link to="/users">
              <h2>{{ blogConfig.title }}</h2>
            </router-link>
          </div>
          
          <nav class="nav-menu">
            <el-menu
              mode="horizontal"
              :default-active="activeMenu"
              router
              background-color="transparent"
              text-color="var(--el-text-color-primary)"
              active-text-color="var(--el-color-primary)"
            >
              <el-menu-item index="/users">
                <el-icon><House /></el-icon>
                <span>首页</span>
              </el-menu-item>
              <el-menu-item index="/users/categories">
                <el-icon><Folder /></el-icon>
                <span>分类</span>
              </el-menu-item>
              <el-menu-item index="/users/about">
                <el-icon><InfoFilled /></el-icon>
                <span>关于</span>
              </el-menu-item>
            </el-menu>
          </nav>
          
          <div class="header-actions">
            <!-- 搜索 -->
            <el-input
              v-model="searchKeyword"
              placeholder="搜索文章..."
              class="search-input"
              clearable
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            
            <!-- 主题切换 -->
            <el-button
              :icon="isDark ? Sunny : Moon"
              circle
              @click="toggleTheme"
              class="theme-toggle"
              :title="isDark ? '切换到浅色模式' : '切换到深色模式'"
            />
            
            <!-- 用户菜单 -->
            <el-dropdown @command="handleUserMenuCommand" placement="bottom-end">
              <div class="user-menu-trigger">
                <el-avatar :size="32" :src="userInfo.userAvatar">
                  {{ userInfo.userName?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="username">{{ userInfo.userName || 'User' }}</span>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="userLayout" :icon="User">
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="write" :icon="EditPen">
                    写文章
                  </el-dropdown-item>
                  <el-dropdown-item command="myArticles" :icon="Document">
                    我的文章
                  </el-dropdown-item>
                  
                  <el-dropdown-item divided command="logout" :icon="SwitchButton">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      
      <!-- 主要内容区域 -->
      <el-container class="main-container">
        <el-main class="main-content">
          <router-view />
        </el-main>
        
        <!-- 侧边栏 -->
        
      </el-container>
      
      <!-- 底部 --> 
      <el-footer class="footer">
        <div class="footer-content">
          <p>&copy; 2025 {{ blogConfig.title }}. All rights reserved.</p>
          <p>Powered by Vue 3 & Element Plus</p>
        </div>
      </el-footer>
      
      <!-- 快速发布按钮 -->
      <el-button
        type="primary"
        :icon="EditPen"
        circle
        size="large"
        class="fab-write"
        @click="goToWrite"
        v-if="!isWritePage"
        title="写文章"
      />
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { 
    Search, Sunny, Moon, House, Folder, InfoFilled, 
    User, EditPen, Document, Star, Setting, SwitchButton, ArrowDown 
  } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { useTheme } from '@/composables/useTheme'
  import { queryUserById } from '@/api/user'
  
  const route = useRoute()
  const router = useRouter()
  const { isDark, toggleTheme, initTheme } = useTheme()
  
  const searchKeyword = ref('')
  const activeMenu = computed(() => route.path)
  const userId = ref('')
  
  // 控制侧边栏显示
  const showSidebar = computed(() => {
    const hideSidebarRoutes = ['/users/write', '/users/profile', '/users/settings','users/sidebar']
    return !hideSidebarRoutes.some(route_path => route.path.startsWith(route_path))
  })
  
  // 判断是否为写文章页面
  const isWritePage = computed(() => route.path.startsWith('/users/write'))
  
  const blogConfig = {
    title: 'My Blog'
  }
  
  // 模拟用户信息 - 您可以后续替换为真实的用户状态管理
  const userInfo = ref({
    userName: 'h3o7',
    userAvatar: '',
    email: 'user@example.com'
  })

  const fetchUserInfo = async () => {
  try {
    const loginData = localStorage.getItem('login')
    const login = JSON.parse(loginData)
    const usersId = login.id;
    userId.value = usersId;

    // 这里需要导入您的API函数
    // import { getUser } from '@/api/user'
    const response = await queryUserById(usersId)
    userInfo.value = response.data

    // 临时模拟数据，请替换为实际API调用
    
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}
  
  const handleSearch = () => {
    if (searchKeyword.value.trim()) {
      router.push({
        path: '/users/search',
        query: { q: searchKeyword.value }
      })
    }
  }
  
  const handleUserMenuCommand = async (command: string) => {
    switch (command) {
      case 'userLayout':
        router.push('/users/userLayout')
        break
      case 'write':
        router.push('/users/write')
        break
      case 'myArticles':
        router.push(`/users/userLayout/myArticles/${userId.value}`)
        break
      
      case 'logout':
        await handleLogout()
        break
    }
  }
  
  const handleLogout = async () => {
    try {
      await ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
      
      // 这里可以添加退出登录的逻辑
      router.push('/login')
      ElMessage.success('已退出登录')
    } catch {
      // 用户取消
    }
  }
  
  const goToWrite = () => {
    router.push('/users/write')
  }
  
  onMounted(() => {
    // 初始化主题
    initTheme()
    // 获取用户信息
    fetchUserInfo()
  })
  </script>
  
  <style scoped>
  .user-layout {
    min-height: 100vh;
    background: var(--el-bg-color);
    transition: all 0.3s ease;
  }
  
  .header {
    background: var(--el-bg-color);
    border-bottom: 1px solid var(--el-border-color);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    position: sticky;
    top: 0;
    z-index: 1000;
  }
  
  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 100%;
    padding: 0 20px;
  }
  
  .logo h2 {
    margin: 0;
    color: var(--el-color-primary);
    font-weight: 600;
    font-size: 24px;
  }
  
  .logo a {
    text-decoration: none;
  }
  
  .nav-menu {
    flex: 1;
    margin: 0 40px;
  }
  
  .header-actions {
    display: flex;
    align-items: center;
    gap: 16px;
  }
  
  .search-input {
    width: 250px;
  }
  
  .theme-toggle {
    color: var(--el-text-color-primary);
    transition: all 0.3s ease;
  }
  
  .theme-toggle:hover {
    transform: scale(1.1);
  }
  
  .user-menu-trigger {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 12px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .user-menu-trigger:hover {
    background: var(--el-fill-color-light);
  }
  
  .username {
    font-size: 14px;
    color: var(--el-text-color-primary);
    font-weight: 500;
  }
  
  .dropdown-icon {
    font-size: 12px;
    color: var(--el-text-color-secondary);
    transition: transform 0.3s;
  }
  
  .main-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 24px 20px;
    gap: 24px;
    min-height: calc(100vh - 140px);
  }
  
  .main-content {
    background: var(--el-bg-color);
    border-radius: 8px;
  }
  
  .sidebar {
    background: var(--el-bg-color);
  }
  
  .sidebar-placeholder {
    background: var(--el-bg-color-page);
    border-radius: 8px;
    padding: 24px;
    text-align: center;
    color: var(--el-text-color-secondary);
    border: 1px dashed var(--el-border-color);
  }
  
  .footer {
    background: var(--el-bg-color-page);
    border-top: 1px solid var(--el-border-color);
    text-align: center;
    color: var(--el-text-color-secondary);
    margin-top: auto;
  }
  
  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
  }
  
  /* 悬浮写作按钮 */
  .fab-write {
    position: fixed;
    right: 24px;
    bottom: 24px;
    width: 56px;
    height: 56px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    z-index: 1000;
    transition: transform 0.3s ease;
  }
  
  .fab-write:hover {
    transform: scale(1.1);
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .header-content {
      padding: 0 16px;
    }
    
    .nav-menu {
      margin: 0 16px;
    }
    
    .search-input {
      width: 180px;
    }
    
    .username {
      display: none;
    }
    
    .main-container {
      padding: 16px;
      flex-direction: column;
    }
    
    .sidebar {
      width: 100% !important;
      order: 2;
    }
    
    .main-content {
      order: 1;
    }
  }
  
  @media (max-width: 480px) {
    .search-input {
      width: 150px;
    }
    
    .logo h2 {
      font-size: 20px;
    }
    
    .fab-write {
      right: 16px;
      bottom: 16px;
      width: 48px;
      height: 48px;
    }
  }
  </style>