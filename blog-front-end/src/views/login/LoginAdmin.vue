<script setup>
  import { ref, reactive, onMounted } from 'vue'
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
    Message
  } from '@element-plus/icons-vue'
  import { ElMessage, ElNotification } from 'element-plus'
  import { loginAdmin } from '@/api/login' // 根据你的项目路径调整
  
  const router = useRouter()
  
  // 响应式数据
  const loginFormRef = ref()
  const loginLoading = ref(false)
  const rememberMe = ref(false)
  const currentUser = ref('h3o7')
  
  // 管理员表单数据
  const adminForm = reactive({
    adminId: '',
    adminName: '',
    adminPassword: ''
  })
  
  // 表单验证规则
  const loginRules = {
    adminId: [
      { required: true, message: '请输入管理员ID', trigger: 'blur' },
      { min: 3, max: 20, message: '管理员ID长度应为3-20个字符', trigger: 'blur' }
    ],
    adminPassword: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 30, message: '密码长度应为6-30个字符', trigger: 'blur' }
    ]
  }
  
  // 处理登录
  const handleLogin = async () => {
    try {
      // 表单验证
      await loginFormRef.value.validate()
      
      loginLoading.value = true
      
      // 发送登录请求
      const response = await loginAdmin(adminForm)
      
      if (response.code === 200) {
        // 登录成功
        
        // 存储到localStorage
        localStorage.setItem('login', JSON.stringify(response.data))
        
        // 如果勾选了记住我，存储登录信息（不包括密码）
        if (rememberMe.value) {
          localStorage.setItem('rememberedAdmin', JSON.stringify({
            adminId: adminForm.adminId,
            adminName: adminForm.adminName
          }))
        } else {
          localStorage.removeItem('rememberedAdmin')
        }
        
        // 显示成功消息
        ElMessage.success('登录成功！')
        
        // 显示欢迎通知
        ElNotification({
          title: '登录成功',
          message: `欢迎回来，${adminForm.adminName}！`,
          type: 'success',
          duration: 3000,
          position: 'top-right'
        })
        
        // 跳转到管理后台
        setTimeout(() => {
          router.push('/admin') // 根据你的路由配置调整
        }, 1000)
        
      } else {
        // 登录失败
        ElMessage.error(response.msg || '登录失败，请检查您的凭据')
      }
      
    } catch (error) {
      console.error('登录失败:', error)
      
      if (error === 'cancel') {
        // 表单验证失败，不显示错误消息
        return
      }
      
      // 处理网络错误或其他错误
      ElMessage.error('登录失败，请检查网络连接或稍后重试')
    } finally {
      loginLoading.value = false
    }
  }
  
  // 重置表单
  const resetForm = () => {
    loginFormRef.value?.resetFields()
    Object.assign(adminForm, {
      adminId: '',
      adminName: '',
      adminPassword: ''
    })
  }
  
  // 页面加载时的初始化

  
  // 暴露方法供外部调用（可选）
  defineExpose({
    resetForm,
    handleLogin
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
          <div class="login-form-container">
            <!-- 登录头部 -->
            <div class="login-header">
              <h2 class="login-title">管理员登录</h2>
              <p class="login-subtitle">请输入您的管理员凭据以访问系统</p>
            </div>
  
            <!-- 登录表单 -->
            <el-form
              ref="loginFormRef"
              :model="adminForm"
              :rules="loginRules"
              class="login-form"
              size="large"
              @keyup.enter="handleLogin"
            >
              <el-form-item prop="adminId">
                <el-input
                  v-model="adminForm.adminId"
                  placeholder="请输入管理员ID"
                  :prefix-icon="User"
                  clearable
                  class="login-input"
                />
              </el-form-item>
  
              <el-form-item prop="adminPassword">
                <el-input
                  v-model="adminForm.adminPassword"
                  type="password"
                  placeholder="请输入密码"
                  :prefix-icon="Lock"
                  show-password
                  clearable
                  class="login-input"
                />
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
              <el-divider>
                <span class="divider-text">其他登录方式</span>
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
              © 2025 智能博客系统. 当前登录用户: {{ currentUser }}
            </p>
            <div class="footer-links">
              <el-link type="info">服务条款</el-link>
              <el-divider direction="vertical" />
              <el-link type="info">隐私政策</el-link>
              <el-divider direction="vertical" />
              <el-link type="info">帮助中心</el-link>
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
  
  /* 登录卡片样式 */
  .login-card {
    display: flex;
    width: 100%;
    max-width: 1000px;
    min-height: 600px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(20px);
    border-radius: 20px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    position: relative;
    z-index: 2;
  }
  
  /* 左侧装饰区域 */
  .login-left {
    flex: 1;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 60px 40px;
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
  
  /* 右侧登录表单区域 */
  .login-right {
    flex: 1;
    padding: 60px 40px 40px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  
  .login-form-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  
  .login-header {
    text-align: center;
    margin-bottom: 40px;
  }
  
  .login-title {
    font-size: 2rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0 0 10px 0;
  }
  
  .login-subtitle {
    color: #7f8c8d;
    margin: 0;
    font-size: 0.9rem;
  }
  
  /* 登录表单样式 */
  .login-form {
    max-width: 400px;
    margin: 0 auto;
    width: 100%;
  }
  
  .login-input {
    margin-bottom: 20px;
  }
  
  :deep(.login-input .el-input__wrapper) {
    padding: 15px 16px;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
  }
  
  :deep(.login-input .el-input__wrapper:hover) {
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
  }
  
  :deep(.login-input .el-input__wrapper.is-focus) {
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
  
  .login-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px 0 30px;
  }
  
  .remember-checkbox {
    color: #7f8c8d;
  }
  
  .forgot-link {
    font-size: 0.9rem;
  }
  
  .login-button {
    width: 100%;
    height: 50px;
    font-size: 1rem;
    font-weight: 600;
    border-radius: 12px;
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
  
  /* 其他登录方式 */
  .other-login {
    margin-top: 30px;
  }
  
  .divider-text {
    color: #bdc3c7;
    font-size: 0.9rem;
  }
  
  .social-login {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 20px;
  }
  
  .social-btn {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    border: 2px solid #ecf0f1;
    color: #7f8c8d;
    transition: all 0.3s ease;
  }
  
  .social-btn:hover {
    border-color: #667eea;
    color: #667eea;
    transform: translateY(-2px);
  }
  
  /* 页脚信息 */
  .login-footer {
    text-align: center;
    margin-top: 30px;
  }
  
  .copyright {
    color: #bdc3c7;
    font-size: 0.8rem;
    margin: 0 0 10px 0;
  }
  
  .footer-links {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
  }
  
  .footer-links .el-link {
    font-size: 0.8rem;
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
    }
    
    .login-left {
      padding: 40px 30px;
      text-align: center;
    }
    
    .login-right {
      padding: 40px 30px 30px;
    }
    
    .system-title {
      font-size: 1.5rem;
    }
    
    .login-title {
      font-size: 1.5rem;
    }
    
    .feature-list {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 15px;
    }
    
    .feature-item {
      margin-bottom: 0;
      font-size: 0.9rem;
    }
  }
  
  @media (max-width: 480px) {
    .login-container {
      padding: 10px;
    }
    
    .login-left,
    .login-right {
      padding: 30px 20px;
    }
    
    .feature-list {
      grid-template-columns: 1fr;
    }
    
    .login-options {
      flex-direction: column;
      gap: 15px;
      align-items: flex-start;
    }
    
    .social-login {
      gap: 10px;
    }
    
    .social-btn {
      width: 40px;
      height: 40px;
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
    margin-top: 5px;
    font-size: 0.8rem;
  }
</style>