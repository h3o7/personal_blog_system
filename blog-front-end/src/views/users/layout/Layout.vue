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
            
            <!-- 分类下拉菜单 -->
            <el-sub-menu index="/users/categories" popper-class="categories-dropdown">
              <template #title>
                <el-icon><Folder /></el-icon>
                <span>分类</span>
              </template>
              <el-menu-item-group v-if="articleTypes.length > 0">
                <el-menu-item 
                  v-for="category in articleTypes" 
                  :key="category.articleTypeId"
                  :index="`/users/categories/${category.articleTypeId}`"
                  @click="goToCategory(category.articleTypeId)"
                >
                  {{ category.articleTypeName }}
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item v-else disabled>
                <span v-if="categoriesLoading">加载中...</span>
                <span v-else>暂无分类</span>
              </el-menu-item>
            </el-sub-menu>
            
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
            <template #suffix>
              <el-button
                :icon="Search"
                text
                @click="handleSearch"
                class="search-button"
                title="搜索"
              />
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
import { queryInfo as getArticleTypes } from '@/api/articleType'

const route = useRoute()
const router = useRouter()
const { isDark, toggleTheme, initTheme } = useTheme()

const searchKeyword = ref('')
const activeMenu = computed(() => route.path)
const userId = ref('')

// 文章类型相关状态
const articleTypes = ref([])
const categoriesLoading = ref(false)

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

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const loginData = localStorage.getItem('login')
    const login = JSON.parse(loginData)
    const usersId = login.id;
    userId.value = usersId;

    const response = await queryUserById(usersId)
    userInfo.value = response.data
    
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 获取文章类型列表
const fetchArticleTypes = async () => {
  categoriesLoading.value = true
  try {
    const response = await getArticleTypes()
    // if (response.code === 200) {
      articleTypes.value = response.data || []
      console.log('文章类型列表:', articleTypes.value)
    // } else {
      // console.error('获取文章类型失败:', response.msg)
    // }
  } catch (error) {
    console.error('获取文章类型失败:', error)
  } finally {
    categoriesLoading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/users/search',
      query: { keyword: searchKeyword.value.trim() }
    })
    // 清空搜索框（可选）
    // searchKeyword.value = ''
  } else {
    ElMessage.warning('请输入搜索关键词')
  }
}

// 跳转到分类页面
const goToCategory = (typeId: string) => {
  router.push(`/users/categories/${typeId}`)
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
    
    // 清除本地存储的用户信息
    localStorage.removeItem('login')
    
    // 跳转到登录页面
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
  // 获取文章类型
  fetchArticleTypes()
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
  width: 280px;
  transition: width 0.3s ease;
}

.search-input:focus-within {
  width: 320px;
}

.search-button {
  padding: 0;
  margin: 0;
  color: var(--el-text-color-secondary);
  transition: color 0.3s ease;
}

.search-button:hover {
  color: var(--el-color-primary);
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

/* 分类下拉菜单样式 */
:deep(.categories-dropdown) {
  max-height: 300px;
  overflow-y: auto;
}

:deep(.categories-dropdown .el-menu-item) {
  padding-left: 40px !important;
  transition: all 0.3s ease;
}

:deep(.categories-dropdown .el-menu-item:hover) {
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

/* 搜索输入框样式优化 */
.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
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
    width: 200px;
  }
  
  .search-input:focus-within {
    width: 220px;
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
  
  .search-input:focus-within {
    width: 180px;
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
  
  .nav-menu {
    margin: 0 8px;
  }
  
  .header-actions {
    gap: 8px;
  }
}
</style>