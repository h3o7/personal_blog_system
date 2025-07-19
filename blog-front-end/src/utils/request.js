// 用于封装axios
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // 基础请求地址
  timeout: 10000, // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (congig) => {
    // 给请求头添加token令牌
    const login = JSON.parse(localStorage.getItem('login'))
    if(login && login.token) {
      congig.headers.token = login.token
    }
    return congig
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => { //成功回调
    if (response.config.headers['X-Get-Headers'] === 'true') {
      return response
    }
    return response.data
  },
  (error) => { //失败回调
    if(error.response.status === 401){
      // 删除token
      localStorage.removeItem('login')
      // 跳转到登录页
      router.push('/login/admin')
      console.log('123456')
      // 提示信息
      ElMessage.error('登录过时，请重新登录')
      
    }else {
      ElMessage.error('接口获取异常')
    }
    return Promise.reject(error)
  }
)

export default service
