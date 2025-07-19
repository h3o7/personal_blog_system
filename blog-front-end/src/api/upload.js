import request from '@/utils/request'

export const uploadAd = (file) => {
    const formData = new FormData()
    formData.append('file', file)
    
    return request.post('/upload/ad', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
}

export const uploadUser = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/upload/user', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}