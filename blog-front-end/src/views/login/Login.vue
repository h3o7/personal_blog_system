<script setup>
import { ref, reactive, onMounted,onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  User,
  Lock,
  Avatar,
  Right,
  EditPen,
  Check,
  Monitor,
  Iphone,
  Message,
  Refresh,
  Picture
} from '@element-plus/icons-vue'
import { ElMessage, ElNotification } from 'element-plus'
import { loginAdmin, loginUser} from '@/api/login'
import axios from 'axios'

const router = useRouter()

// 响应式数据
const loginType = ref('user') // 'admin' 或 'user'
const loginLoading = ref(false)
const rememberMe = ref(false)
const captchaUrl = ref('')
const currentUser = ref('h3o7')

// 登录表单数据
const loginForm = ref({
  id: '',
  password: '',
  captchaId: '',
  captchaCode: ''
})

// 表单引用
const loginFormRef = ref()

// 表单验证规则
const loginRules = reactive({
  id: [
    { required: true, message: `请输入${loginType.value === 'admin' ? '管理员ID' : '用户名'}`, trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  captchaCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码长度为4位', trigger: 'blur' }
  ]
})

// 获取验证码
const getCaptcha = async () => {
  try {
    // 直接使用axios而不是封装的service
    const response = await axios({
      url: '/api/login/captcha',
      method: 'get',
      responseType: 'blob'
    })
    
    console.log('验证码响应:', response)
    console.log('响应头:', response.headers)
    
    // 从响应头获取 captchaId
    const captchaId = response.headers['captcha-id']
    console.log('Captcha-Id', captchaId)
    if (captchaId) {
      loginForm.value.captchaId = captchaId
      console.log('获取到的验证码ID:', captchaId)
    } else {
      console.warn('未找到验证码ID')
      // 可以使用你之前看到的ID作为备用
      loginForm.value.captchaId = '4d81af5ee69048ec885de667b97ce13b'
    }
    
    // 创建图片URL
    const imageBlob = new Blob([response.data], { type: 'image/png' })
    if (captchaUrl.value) {
      URL.revokeObjectURL(captchaUrl.value) // 释放旧的URL
    }
    captchaUrl.value = URL.createObjectURL(imageBlob)
    // 清空验证码输入
    loginForm.value.captchaCode = ''
    
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败，请重试')
  }
}

// 刷新验证码
const refreshCaptcha = () => {
  getCaptcha()
}

// 切换登录类型
const switchLoginType = (type) => {
  loginType.value = type
  // 清空表单
  loginForm.value.id = ''
  loginForm.value.password = ''
  loginForm.value.captchaCode = ''
  
  // 更新验证规则
  loginRules.id[0].message = `请输入${type === 'admin' ? '管理员ID' : '用户名'}`
  
  // 刷新验证码
  refreshCaptcha()
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    // 表单验证
    await loginFormRef.value.validate()
    
    loginLoading.value = true
    
    let response
    if (loginType.value === 'admin') {
      response = await loginAdmin(loginForm.value)
    } else {
      response = await loginUser(loginForm.value)
    }
    // console.log('登录响应:', response)
    // console.log('登录响应:', response.code)
    // 登录成功
    if (response.code === 200) {
      // 存储登录信息到 localStorage
      localStorage.setItem('login', JSON.stringify(response.data))
      
      ElNotification({
        title: '登录成功',
        message: `欢迎回来，${loginType.value === 'admin' ? '管理员' : '用户'}！`,
        type: 'success',
        duration: 3000
      })
      
      // 根据登录类型跳转
      if (loginType.value === 'admin') {
        router.push('/admins')
      } else {
        router.push('/users')
      }
    } else {
      console.error('错误信息:', response.msg) // 专门打印错误信息
      throw new Error(response.msg)
    }
    
  } catch (error) {
    // console.error('登录失败:', error)
    const errorMessage = error.msg || error.message || (error.response && error.response.data && error.response.data.msg)
    ElMessage.error(errorMessage)
    // 登录失败后刷新验证码
    refreshCaptcha()
  } finally {
    loginLoading.value = false
  }
}

// 页面挂载时获取验证码
onMounted(() => {
  getCaptcha()
})

// 页面卸载时清理资源
onUnmounted(() => {
  if (captchaUrl.value) {
    URL.revokeObjectURL(captchaUrl.value)
  }
})
</script>

<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
      <div class="decoration-wave wave-1"></div>
      <div class="decoration-wave wave-2"></div>
    </div>

    <!-- 主登录卡片 -->
    <div class="login-card">
      <!-- 左侧装饰区域 -->
      <div class="login-left">
        <div class="logo-section">
          <div class="logo-icon">
            <el-icon class="main-logo"><EditPen /></el-icon>
          </div>
          <h1 class="system-title">智能博客管理系统</h1>
          <p class="system-subtitle">现代化的博客内容管理平台</p>
        </div>
        
        <div class="feature-list">
          <div class="feature-item">
            <el-icon class="feature-icon"><Check /></el-icon>
            <span>文章内容管理</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Check /></el-icon>
            <span>用户权限控制</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Check /></el-icon>
            <span>数据统计分析</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Check /></el-icon>
            <span>系统安全保障</span>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单区域 -->
      <div class="login-right">
        <div class="login-form-wrapper">
          <!-- 登录类型切换 -->
          <div class="login-type-switch">
            <el-segmented 
              v-model="loginType" 
              :options="[
                { label: '用户登录', value: 'user' },
                { label: '管理员登录', value: 'admin' }
              ]"
              size="default"
              @change="switchLoginType"
            />
          </div>

          <!-- 登录头部 -->
          <div class="login-header">
            <h2 class="login-title">
              {{ loginType === 'admin' ? '管理员登录' : '用户登录' }}
            </h2>
            <p class="login-subtitle">
              {{ loginType === 'admin' ? '输入管理员凭据访问系统' : '输入用户凭据访问系统' }}
            </p>
          </div>

          <!-- 登录表单 -->
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
            size="default"
            @keyup.enter="handleLogin"
          >
            <el-form-item prop="id">
              <el-input
                v-model="loginForm.id"
                :placeholder="`请输入${loginType === 'admin' ? '管理员ID' : '用户名'}`"
                :prefix-icon="loginType === 'admin' ? Avatar : User"
                clearable
                class="login-input"
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                :prefix-icon="Lock"
                show-password
                clearable
                class="login-input"
              />
            </el-form-item>

            <!-- 验证码输入 -->
            <el-form-item prop="captchaCode">
              <div class="captcha-container">
                <el-input
                  v-model="loginForm.captchaCode"
                  placeholder="验证码"
                  :prefix-icon="Picture"
                  clearable
                  class="captcha-input"
                  maxlength="4"
                />
                <div class="captcha-wrapper">
                  <img 
                    v-if="captchaUrl" 
                    :src="captchaUrl" 
                    alt="验证码" 
                    class="captcha-image"
                    @click="refreshCaptcha"
                    title="点击刷新验证码"
                  >
                  <el-button 
                    :icon="Refresh" 
                    @click="refreshCaptcha"
                    class="refresh-btn"
                    size="small"
                    type="primary"
                    text
                    title="刷新验证码"
                  />
                </div>
              </div>
            </el-form-item>

            <!-- 附加选项 -->
            <div class="login-options">
              <el-checkbox v-model="rememberMe" class="remember-checkbox">
                记住我
              </el-checkbox>
              <el-link type="primary" class="forgot-link">
                忘记密码？
              </el-link>
            </div>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                class="login-button"
                :loading="loginLoading"
                @click="handleLogin"
              >
                <el-icon v-if="!loginLoading"><Right /></el-icon>
                {{ loginLoading ? '登录中...' : '立即登录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 其他登录方式 -->
          <div class="other-login">
            <el-divider class="login-divider">
              <span class="divider-text">其他方式</span>
            </el-divider>
            <div class="social-login">
              <el-tooltip content="扫码登录" placement="top">
                <el-button circle class="social-btn">
                  <el-icon><Monitor /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="手机登录" placement="top">
                <el-button circle class="social-btn">
                  <el-icon><Iphone /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="邮箱登录" placement="top">
                <el-button circle class="social-btn">
                  <el-icon><Message /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </div>

        <!-- 页脚信息 -->
        <div class="login-footer">
          <p class="copyright">
            © 2025 智能博客系统 · 当前用户: {{ currentUser }}
          </p>
          <div class="footer-links">
            <el-link type="info" size="small">服务条款</el-link>
            <el-divider direction="vertical" />
            <el-link type="info" size="small">隐私政策</el-link>
            <el-divider direction="vertical" />
            <el-link type="info" size="small">帮助中心</el-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 版本信息 -->
    <div class="version-info">
      <el-tag size="small" type="info">v2.1.0</el-tag>
    </div>
  </div>
</template>

<style scoped>
/* 主容器样式 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  left: -150px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  top: 20%;
  right: -100px;
  animation-delay: 2s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  bottom: -75px;
  left: 20%;
  animation-delay: 4s;
}

.decoration-wave {
  position: absolute;
  width: 200%;
  height: 200px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  animation: wave 8s ease-in-out infinite;
}

.wave-1 {
  top: -100px;
  left: -50%;
  animation-delay: 0s;
}

.wave-2 {
  bottom: -100px;
  right: -50%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(180deg); }
}

/* 登录卡片样式 - 调整比例 */
.login-card {
  display: flex;
  width: 100%;
  max-width: 950px; /* 稍微缩小整体宽度 */
  min-height: 580px; /* 稍微降低高度 */
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 2;
}

/* 左侧装饰区域 - 保持原样 */
.login-left {
  flex: 1.2; /* 稍微增加左侧比例 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 50px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  position: relative;
}

.login-left::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grid" width="10" height="10" patternUnits="userSpaceOnUse"><path d="M 10 0 L 0 0 0 10" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
  opacity: 0.3;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.logo-icon {
  margin-bottom: 20px;
}

.main-logo {
  font-size: 4rem;
  color: white;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
}

.system-title {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 10px 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.system-subtitle {
  font-size: 1rem;
  opacity: 0.9;
  margin: 0;
  font-weight: 300;
}

.feature-list {
  position: relative;
  z-index: 1;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 1rem;
  opacity: 0.9;
}

.feature-icon {
  font-size: 1.2rem;
  margin-right: 15px;
  color: #a8e6cf;
}

/* 右侧登录表单区域 - 重新设计 */
.login-right {
  flex: 1; /* 减少右侧比例 */
  padding: 30px 25px 20px; /* 减少内边距 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.login-form-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 320px; /* 减少最大宽度 */
  margin: 0 auto;
  width: 100%;
}

/* 登录类型切换 - 优化样式 */
.login-type-switch {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

:deep(.el-segmented) {
  background-color: #f8f9fa;
  border-radius: 10px;
  padding: 3px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.el-segmented__item) {
  border-radius: 7px;
  font-weight: 500;
  font-size: 0.9rem;
  padding: 8px 16px;
}

:deep(.el-segmented__item.is-selected) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

/* 登录头部 - 更紧凑 */
.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-title {
  font-size: 1.6rem; /* 减小字体 */
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px 0;
}

.login-subtitle {
  color: #7f8c8d;
  margin: 0;
  font-size: 0.85rem; /* 减小字体 */
  line-height: 1.4;
}

/* 登录表单 - 优化间距 */
.login-form {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 18px; /* 减少间距 */
}

.login-input {
  width: 100%;
}

:deep(.login-input .el-input__wrapper) {
  padding: 12px 14px; /* 稍微减少内边距 */
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
}

:deep(.login-input .el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
  border-color: #c0c4cc;
}

:deep(.login-input .el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.25);
  border-color: #667eea;
}

/* 验证码容器 - 优化布局 */
.captcha-container {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  width: 100%;
}

.captcha-input {
  flex: 1;
}

.captcha-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.captcha-image {
  width: 100px;
  height: 40px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fafafa;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.captcha-image:hover {
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
  transform: translateY(-1px);
}

.refresh-btn {
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 4px;
  height: 22px;
}

/* 登录选项 - 优化布局 */
.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0 20px;
}

.remember-checkbox {
  color: #7f8c8d;
}

:deep(.remember-checkbox .el-checkbox__label) {
  font-size: 0.9rem;
}

.forgot-link {
  font-size: 0.85rem;
}

/* 登录按钮 - 优化尺寸 */
.login-button {
  width: 100%;
  height: 42px; /* 稍微减少高度 */
  font-size: 0.95rem;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.login-button:active {
  transform: translateY(0);
}

/* 其他登录方式 - 更紧凑 */
.other-login {
  margin-top: 20px;
}

.login-divider {
  margin: 16px 0 12px;
}

.divider-text {
  color: #bdc3c7;
  font-size: 0.8rem;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.social-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid #ecf0f1;
  color: #7f8c8d;
  transition: all 0.3s ease;
}

.social-btn:hover {
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(102, 126, 234, 0.2);
}

/* 页脚信息 - 更紧凑 */
.login-footer {
  text-align: center;
  margin-top: 16px;
}

.copyright {
  color: #bdc3c7;
  font-size: 0.75rem;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 3px;
}

.footer-links .el-link {
  font-size: 0.75rem;
}

/* 版本信息 */
.version-info {
  position: absolute;
  bottom: 20px;
  right: 20px;
  z-index: 3;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    flex-direction: column;
    margin: 10px;
    min-height: auto;
    max-width: none;
  }
  
  .login-left {
    padding: 30px 25px;
    text-align: center;
    flex: none;
  }
  
  .login-right {
    padding: 30px 25px 20px;
    flex: none;
  }
  
  .login-form-wrapper {
    max-width: none;
  }
  
  .system-title {
    font-size: 1.4rem;
  }
  
  .login-title {
    font-size: 1.4rem;
  }
  
  .feature-list {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
  }
  
  .feature-item {
    margin-bottom: 0;
    font-size: 0.9rem;
  }
  
  .captcha-container {
    flex-direction: column;
    gap: 12px;
  }
  
  .captcha-wrapper {
    flex-direction: row;
    justify-content: center;
  }
  
  .captcha-image {
    width: 120px;
    height: 48px;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 10px;
  }
  
  .login-left,
  .login-right {
    padding: 25px 20px;
  }
  
  .login-form-wrapper {
    max-width: none;
  }
  
  .feature-list {
    grid-template-columns: 1fr;
  }
  
  .login-options {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .social-login {
    gap: 10px;
  }
}

/* 动画效果 */
.login-card {
  animation: slideIn 0.6s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 表单验证错误样式优化 */
:deep(.el-form-item.is-error .el-input__wrapper) {
  border-color: #f56c6c;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.2);
}

:deep(.el-form-item__error) {
  margin-top: 4px;
  font-size: 0.75rem;
}

/* 验证码输入框样式 */
:deep(.captcha-input .el-input__wrapper) {
  padding: 12px 14px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
}

:deep(.captcha-input .el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
  border-color: #c0c4cc;
}

:deep(.captcha-input .el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.25);
  border-color: #667eea;
}
</style>