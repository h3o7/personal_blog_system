<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  House, 
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

const activeIndex = ref('/admins/dashboard')
const currentUser = ref({
  name: 'h3o7',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
})

const updateActiveIndex = () => {
  const path = route.path
  
  if (path.includes('/admins/user')) {
    activeIndex.value = '/admins/user'
  } else if (path.includes('/admins/article')) {
    activeIndex.value = 'article'  // 子菜单使用父级索引，不需要完整路径
  } else if (path.includes('/admins/link')) {
    activeIndex.value = '/admins/link'
  } else if (path.includes('/admins/ad')) {
    activeIndex.value = '/admins/ad'
  } else if (path.includes('/admins/admin')) {
    activeIndex.value = '/admins/admin'
  } else if (path.includes('/admins/dashboard') || path === '/admins') {
    activeIndex.value = '/admins/dashboard'
  } else {
    activeIndex.value = '/admins/dashboard'  // 默认激活首页
  }
}

// 处理菜单选择
const handleSelect = (index) => {
  // 如果是子菜单的父级索引，不做跳转
  if (index === 'article') {
    return
  }
  
  // 直接跳转到对应路由
  if (index.startsWith('/')) {
    router.push(index)
  }
}

// 处理用户下拉菜单
const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/admins/profile')  // 修改为带前缀的路径
      break
    case 'settings':
      router.push('/admins/settings')  // 修改为带前缀的路径
      break
    case 'theme':
      ElMessage.info('主题切换功能开发中')
      break
    case 'logout':
      localStorage.removeItem('login')
      router.push('/login')  // 登录页路径不变
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
  <div class="blog-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-content">
        <!-- Logo区域 -->
        <div class="logo">
          <el-icon class="logo-icon"><EditPen /></el-icon>
          <span class="logo-text">智能博客系统</span>
        </div>
        
        <!-- 导航菜单区域 -->
        <nav class="navigation">
          <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          @select="handleSelect"
          background-color="transparent"
          text-color="#0c0c0c"
          active-text-color="#00d4ff"
          class="nav-menu"
          :ellipsis="false"
          style="flex-wrap: nowrap;"
          >
            <!-- 首页 - 直接点击跳转 -->
            <el-menu-item index="/admins/admin" class="nav-item">
              <el-icon><House /></el-icon>
              <span>首页</span>
            </el-menu-item>

            <!-- 用户管理 - 直接点击跳转 -->
            <el-menu-item index="/admins/user" class="nav-item">
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </el-menu-item>

            <!-- 文章管理 - 有下拉菜单 -->
            <el-sub-menu index="article" class="nav-item">
              <template #title>
                <el-icon><Document /></el-icon>
                <span>文章管理</span>
              </template>
              <el-menu-item index="/admins/article">文章列表</el-menu-item>
              <el-menu-item index="/admins/article/type">文章类型</el-menu-item>
              <el-menu-item index="/admins/article/tag">文章标签</el-menu-item>
            </el-sub-menu>

            <!-- 友情链接 - 直接点击跳转 -->
            <el-menu-item index="/admins/link" class="nav-item">
              <el-icon><Connection /></el-icon>
              <span>友情链接</span>
            </el-menu-item>

            <!-- 广告管理 - 直接点击跳转 -->
            <el-menu-item index="/admins/ad" class="nav-item">
              <el-icon><Monitor /></el-icon>
              <span>广告管理</span>
            </el-menu-item>
          </el-menu>
        </nav>

        <!-- 右侧操作区域 -->
        <div class="header-actions">
          <!-- 搜索按钮 -->
          <el-tooltip content="搜索" placement="bottom">
            <el-button circle :icon="Search" class="action-btn" />
          </el-tooltip>
          
          <!-- 通知按钮 -->
          <el-tooltip content="通知" placement="bottom">
            <el-badge :value="5" class="notification-badge">
              <el-button circle :icon="Bell" class="action-btn" />
            </el-badge>
          </el-tooltip>
          
          <!-- 全屏按钮 -->
          <el-tooltip content="全屏" placement="bottom">
            <el-button circle :icon="FullScreen" class="action-btn" @click="toggleFullscreen" />
          </el-tooltip>
          
          <!-- 用户下拉菜单 -->
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
              <el-icon class="arrow-down"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  账户设置
                </el-dropdown-item>
                <el-dropdown-item command="theme">
                  <el-icon><Moon /></el-icon>
                  主题切换
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="content-wrapper">
        <router-view />
      </div>
    </main>
  </div>
</template>



<style scoped>
/* 整体布局容器 - 设置为全屏高度和宽度，flex垂直布局 */
.blog-layout {
  height: 100vh; /* 占满视口高度 */
  width: 100vw; /* 占满视口宽度 */
  display: flex; /* 使用flex布局 */
  flex-direction: column; /* 垂直方向布局 */
  background: linear-gradient(135deg, #0c0c0c 0%, #764ba2 100%); /* 渐变背景 */
}

/* 顶部导航栏样式 */
.header {
  background: linear-gradient(135deg, rgba(44, 62, 80, 0.95) 0%, rgba(52, 73, 94, 0.95) 100%); /* 半透明深色渐变背景 */
  backdrop-filter: blur(20px); /* 毛玻璃效果 */
  border-bottom: 1px solid rgba(255, 255, 255, 0.1); /* 半透明底边框 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2); /* 阴影效果 */
  position: sticky; /* 粘性定位 */
  top: 0; /* 顶部固定 */
  z-index: 1000; /* 层级最高 */
}

/* 头部内容容器 */
.header-content {
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between; /* 两端对齐 */
  padding: 0 40px; /* 左右内边距 */
  height: 80px; /* 固定高度 */
}

/* Logo区域样式 */
.logo {
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  font-size: 22px; /* 字体大小 */
  font-weight: 800; /* 字体粗细 */
  color: #ffffff; /* 白色文字 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3); /* 文字阴影 */
}

/* Logo图标样式 */
.logo-icon {
  font-size: 28px; /* 图标大小 */
  margin-right: 12px; /* 右边距 */
  color: #00d4ff; /* 青色图标 */
  filter: drop-shadow(0 0 10px rgba(0, 212, 255, 0.5)); /* 发光效果 */
}

/* Logo文字样式 */
.logo-text {
  background: linear-gradient(135deg, #00d4ff 0%, #ffffff 50%, #00d4ff 100%); /* 渐变文字 */
  background-clip: text; /* 文字裁剪 */
  -webkit-background-clip: text; /* Webkit兼容 */
  -webkit-text-fill-color: transparent; /* 透明填充 */
  letter-spacing: 1px; /* 字母间距 */
}

/* 导航区域样式 */
.navigation {
  flex: 1; /* 占据剩余空间 */
  display: flex; /* flex布局 */
  justify-content: center; /* 水平居中 */
  margin: 0 60px; /* 左右外边距 */
}

/* 导航菜单样式 */
.nav-menu {
  border-bottom: none !important; /* 移除底边框 */
  background: transparent !important; /* 透明背景 */
}

/* 普通菜单项样式 */
:deep(.nav-item.el-menu-item) {
  height: 80px; /* 菜单项高度 */
  line-height: 80px; /* 行高 */
  font-weight: 600; /* 字体粗细 */
  font-size: 16px; /* 字体大小 */
  transition: all 0.3s ease; /* 过渡动画 */
  border-radius: 12px; /* 圆角 */
  margin: 0 8px; /* 左右外边距 */
  padding: 0 20px; /* 左右内边距 */
  color: #ffffff !important; /* 白色文字 */
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  gap: 8px; /* 图标与文字间距 */
}

/* 子菜单标题样式 */
:deep(.nav-item.el-sub-menu .el-sub-menu__title) {
  height: 80px; /* 菜单项高度 */
  line-height: 80px; /* 行高 */
  font-weight: 600; /* 字体粗细 */
  font-size: 16px; /* 字体大小 */
  transition: all 0.3s ease; /* 过渡动画 */
  border-radius: 12px; /* 圆角 */
  margin: 0 8px; /* 左右外边距 */
  padding: 0 20px; /* 左右内边距 */
  color: #ffffff !important; /* 白色文字 */
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  gap: 8px; /* 图标与文字间距 */
}

/* 普通菜单项悬停效果 */
:deep(.nav-item.el-menu-item:hover) {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%); /* 悬停背景 */
  transform: translateY(-3px); /* 向上移动 */
  box-shadow: 0 8px 25px rgba(0, 212, 255, 0.3); /* 阴影效果 */
}

/* 子菜单悬停效果 */
:deep(.nav-item.el-sub-menu:hover .el-sub-menu__title) {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%); /* 悬停背景 */
  transform: translateY(-3px); /* 向上移动 */
  box-shadow: 0 8px 25px rgba(0, 212, 255, 0.3); /* 阴影效果 */
}

/* 普通菜单项激活状态 */
:deep(.nav-item.el-menu-item.is-active) {
  color: #00d4ff !important; /* 激活状态青色文字 */
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%); /* 激活背景 */
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.3); /* 阴影效果 */
}

/* 子菜单激活状态 */
:deep(.nav-item.el-sub-menu.is-active .el-sub-menu__title) {
  color: #00d4ff !important; /* 激活状态青色文字 */
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%); /* 激活背景 */
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.3); /* 阴影效果 */
}

/* 菜单项图标样式 */
:deep(.nav-item .el-icon) {
  font-size: 20px; /* 图标大小 */
}

/* 隐藏子菜单下拉箭头 */
:deep(.el-sub-menu__icon-arrow) {
  display: none !important; /* 完全隐藏 */
}

/* 弹出式菜单样式 */
:deep(.el-menu--popup) {
  background: rgba(255, 255, 255, 0.95) !important; /* 半透明白色背景 */
  backdrop-filter: blur(20px); /* 毛玻璃效果 */
  border: 1px solid rgba(0, 0, 0, 0.08) !important; /* 半透明边框 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15) !important; /* 阴影效果 */
  border-radius: 12px !important; /* 圆角 */
  padding: 8px !important; /* 内边距 */
}

/* 弹出式菜单项样式 */
:deep(.el-menu--popup .el-menu-item) {
  color: #2c3e50 !important; /* 深色文字 */
  transition: all 0.3s ease; /* 过渡动画 */
  font-weight: 500; /* 字体粗细 */
  padding: 0 20px !important; /* 左右内边距 */
  height: 45px !important; /* 菜单项高度 */
  line-height: 45px !important; /* 行高 */
  border-radius: 8px !important; /* 圆角 */
  margin: 2px 0 !important; /* 上下外边距 */
}

/* 弹出式菜单项悬停效果 */
:deep(.el-menu--popup .el-menu-item:hover) {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%) !important; /* 悬停背景 */
  color: #667eea !important; /* 悬停文字颜色 */
  transform: translateX(5px); /* 向右移动 */
}

/* 弹出式菜单项激活状态 */
:deep(.el-menu--popup .el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important; /* 激活背景 */
  color: #ffffff !important; /* 激活文字颜色 */
}

/* 右侧操作按钮区域 */
.header-actions {
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  gap: 15px; /* 子元素间距 */
}

/* 操作按钮样式 */
.action-btn {
  background: rgba(255, 255, 255, 0.1); /* 半透明白色背景 */
  border: 1px solid rgba(255, 255, 255, 0.2); /* 半透明边框 */
  color: #ffffff; /* 白色图标 */
  transition: all 0.3s ease; /* 过渡动画 */
}

/* 操作按钮悬停效果 */
.action-btn:hover {
  background: rgba(0, 212, 255, 0.3); /* 悬停背景 */
  border-color: #00d4ff; /* 悬停边框颜色 */
  transform: translateY(-2px); /* 向上移动 */
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.4); /* 阴影效果 */
}

/* 通知徽章样式 */
.notification-badge {
  margin-right: 5px; /* 右边距 */
}

/* 用户下拉菜单容器 */
.user-dropdown {
  margin-left: 10px; /* 左边距 */
}

/* 用户信息区域样式 */
.user-info {
  display: flex; /* flex布局 */
  align-items: center; /* 垂直居中 */
  cursor: pointer; /* 手型指针 */
  padding: 8px 16px; /* 内边距 */
  border-radius: 25px; /* 圆角 */
  transition: all 0.3s ease; /* 过渡动画 */
  background: rgba(255, 255, 255, 0.1); /* 半透明背景 */
  border: 1px solid rgba(255, 255, 255, 0.2); /* 半透明边框 */
}

/* 用户信息悬停效果 */
.user-info:hover {
  background: rgba(0, 212, 255, 0.2); /* 悬停背景 */
  border-color: #00d4ff; /* 悬停边框颜色 */
  transform: translateY(-2px); /* 向上移动 */
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.3); /* 阴影效果 */
}

/* 用户头像样式 */
.user-avatar {
  margin-right: 12px; /* 右边距 */
  border: 2px solid rgba(255, 255, 255, 0.3); /* 半透明头像边框 */
}

/* 用户详细信息容器 */
.user-details {
  display: flex; /* flex布局 */
  flex-direction: column; /* 垂直排列 */
  margin-right: 10px; /* 右边距 */
  text-align: left; /* 左对齐 */
}

/* 用户名样式 */
.username {
  font-weight: 600; /* 字体粗细 */
  color: #ffffff; /* 白色文字 */
  font-size: 14px; /* 字体大小 */
  line-height: 1.2; /* 行高 */
}

/* 用户角色样式 */
.user-role {
  font-size: 12px; /* 字体大小 */
  color: #00d4ff; /* 青色文字 */
  line-height: 1.2; /* 行高 */
}

/* 下拉箭头样式 */
.arrow-down {
  font-size: 14px; /* 箭头大小 */
  color: #ffffff; /* 白色箭头 */
  transition: transform 0.3s ease; /* 旋转动画 */
}

/* 用户信息悬停时箭头旋转效果 */
.user-info:hover .arrow-down {
  transform: rotate(180deg); /* 旋转180度 */
}

/* 主要内容区域样式 */
.main-content {
  flex: 1; /* 占据剩余空间 */
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%); /* 浅色渐变背景 */
  overflow-y: auto; /* 垂直滚动 */
  position: relative; /* 相对定位 */
}

/* 主内容区域装饰性背景 */
.main-content::before {
  content: ''; /* 伪元素内容 */
  position: absolute; /* 绝对定位 */
  top: 0; /* 顶部对齐 */
  left: 0; /* 左侧对齐 */
  right: 0; /* 右侧对齐 */
  bottom: 0; /* 底部对齐 */
  background: 
    radial-gradient(circle at 20% 20%, rgba(102, 126, 234, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.1) 0%, transparent 50%); /* 径向渐变装饰 */
  pointer-events: none; /* 不响应鼠标事件 */
}

/* 内容包装器 */
.content-wrapper {
  padding: 40px; /* 内边距 */
  min-height: 100%; /* 最小高度 */
  position: relative; /* 相对定位 */
  z-index: 1; /* 层级 */
}

/* 主内容区域滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

/* 滚动条轨道样式 */
.main-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1); /* 半透明轨道背景 */
}

/* 滚动条滑块样式 */
.main-content::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 渐变滑块背景 */
  border-radius: 4px; /* 滑块圆角 */
}

/* 滚动条滑块悬停效果 */
.main-content::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%); /* 悬停时加深颜色 */
}

/* 响应式设计 - 大屏幕设备 (1200px以下) */
@media (max-width: 1200px) {
  .header-content {
    padding: 0 30px; /* 减少左右内边距 */
  }
  
  .navigation {
    margin: 0 40px; /* 减少左右外边距 */
  }
}

/* 响应式设计 - 平板设备 (768px以下) */


/* 响应式设计 - 手机设备 (480px以下) */

</style>