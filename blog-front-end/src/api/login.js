import request from '@/utils/request'

export const loginAdmin = (loginForm) => request.post('/login/admin', loginForm)
export const loginUser = (loginForm) => request.post('/login/user', loginForm)

export const generateCaptcha = () => request.get('/login/captcha')
