<script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter, useRoute } from 'vue-router'
  import { 
    House, 
    DataBoard, 
    UserFilled, 
    Document, 
    Connection, 
    Monitor,
    EditPen,
    Bell,
    ArrowDown,
    Search,
    FullScreen,
    User,
    Setting,
    Moon,
    SwitchButton
  } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  const route = useRoute()
  
  const activeIndex = ref('home')
  const currentUser = ref({
    name: 'h3o7',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  })
  
  // 根据当前路由设置激活的菜单项
  const updateActiveIndex = () => {
    const path = route.path
    if (path.includes('/user') || path.includes('/role') || path.includes('/permission')) {
      activeIndex.value = 'user'
    } else if (path.includes('/article') || path.includes('/comment')) {
      activeIndex.value = 'article'
    } else if (path.includes('/friend')) {
      activeIndex.value = 'friend'
    } else if (path.includes('/ad')) {
      activeIndex.value = 'ad'
    } else if (path.includes('/categories') || path.includes('/tags') || path.includes('/setting')) {
      activeIndex.value = 'data'
    } else {
      activeIndex.value = 'home'
    }
  }
  
  // 处理菜单选择
  const handleSelect = (index) => {
    if (index.startsWith('/')) {
      router.push(index)
    }
  }
  
  // 处理用户下拉菜单
  const handleUserCommand = (command) => {
    switch (command) {
      case 'profile':
        router.push('/profile')
        break
      case 'settings':
        router.push('/settings')
        break
      case 'theme':
        ElMessage.info('主题切换功能开发中')
        break
      case 'logout':
        ElMessage.success('退出登录成功')
        // 这里可以添加退出登录的逻辑
        break
    }
  }
  
  // 全屏切换
  const toggleFullscreen = () => {
    if (document.fullscreenElement) {
      document.exitFullscreen()
    } else {
      document.documentElement.requestFullscreen()
    }
  }
  
  onMounted(() => {
    updateActiveIndex()
  })
  
  // 监听路由变化
  router.afterEach(() => {
    updateActiveIndex()
  })
  </script>
  

<template>
  <div class="blog-system">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-content">
        <!-- Logo -->
        <div class="logo">
          <el-icon class="logo-icon">
            <EditPen />
          </el-icon>
          <span class="logo-text">智能博客系统</span>
        </div>

        <!-- 导航菜单 -->
        <nav class="navigation">
          <el-menu
            :default-active="activeIndex"
            mode="horizontal"
            @select="handleSelect"
            background-color="transparent"
            text-color="#ffffff"
            active-text-color="#00d4ff"
            class="nav-menu"
          >
            <el-sub-menu index="home">
              <template #title>
                <el-icon>
                  <House />
                </el-icon>
                <span>首页</span>
              </template>
              <el-menu-item index="/dashboard">数据概览</el-menu-item>
              <el-menu-item index="/welcome">欢迎页面</el-menu-item>
              <el-menu-item index="/quick-start">快速开始</el-menu-item>
              <el-menu-item index="/analytics">数据分析</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="data">
              <template #title>
                <el-icon>
                  <DataBoard />
                </el-icon>
                <span>基础数据</span>
              </template>
              <el-menu-item index="/categories">分类管理</el-menu-item>
              <el-menu-item index="/tags">标签管理</el-menu-item>
              <el-menu-item index="/settings">系统设置</el-menu-item>
              <el-menu-item index="/backup">数据备份</el-menu-item>
              <el-menu-item index="/import">数据导入</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="user">
              <template #title>
                <el-icon>
                  <UserFilled />
                </el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item index="/users">用户列表</el-menu-item>
              <el-menu-item index="/roles">角色管理</el-menu-item>
              <el-menu-item index="/permissions">权限管理</el-menu-item>
              <el-menu-item index="/profile">个人资料</el-menu-item>
              <el-menu-item index="/user-analytics">用户统计</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </nav>

        <!-- 用户信息 -->
        <div class="header-actions">
          <el-dropdown @command="handleUserCommand" class="user-dropdown">
            <div class="user-info">
              <el-avatar
                :size="36"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                class="user-avatar"
              />
              <div class="user-details">
                <span class="username">{{ currentUser.name }}</span>
                <span class="user-role">管理员</span>
              </div>
              <el-icon class="arrow-down">
                <ArrowDown />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon>
                    <User />
                  </el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon>
                    <Setting />
                  </el-icon>
                  账户设置
                </el-dropdown-item>
                <el-dropdown-item command="theme">
                  <el-icon>
                    <Moon />
                  </el-icon>
                  主题切换
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon>
                    <SwitchButton />
                  </el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
  </div>
</template>

<style scoped>
/* 顶部导航栏样式 */
.header {
  background: linear-gradient(135deg, #007bff, #00d4ff);
  height: 64px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

/* Logo 样式 */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 24px;
  color: #ffffff;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
  white-space: nowrap;
}

/* 导航菜单样式 */
.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu .el-menu-item,
.nav-menu .el-sub-menu__title {
  font-size: 16px;
  padding: 0 15px;
}

.nav-menu .el-menu-item:hover,
.nav-menu .el-sub-menu__title:hover {
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

/* 用户信息样式 */
.header-actions {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: #ffffff;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(255, 255, 255, 0.3);
}

.user-avatar {
  margin-right: 12px;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.user-details {
  display: flex;
  flex-direction: column;
  margin-right: 10px;
  text-align: left;
}

.username {
  font-weight: 600;
  color: #ffffff;
  font-size: 14px;
  line-height: 1.2;
}

.user-role {
  font-size: 12px;
  color: #00d4ff;
  line-height: 1.2;
}

.arrow-down {
  font-size: 14px;
  color: #ffffff;
  transition: transform 0.3s ease;
}

.user-info:hover .arrow-down {
  transform: rotate(180deg);
}
</style>