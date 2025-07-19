import request from '@/utils/request'

// 广告操作
export const queryAdInfo = (adTypeId) => request.get(`/ad/list?adTypeId=${adTypeId}`)
export const deleteAd = (adId) => request.delete(`/ad?adId=${adId}`)
export const addAd = (adForm) => request.post('/ad/add', adForm)
export const editAd = (adForm) => request.put('/ad/edit', adForm)

// 广告类型操作
export const queryAdTypeInfo = () => request.get('/ad/type/list')
export const deleteAdType = (adTypeId) => request.delete(`/ad/type?adTypeId=${adTypeId}`)
export const addAdType = (adTypeForm) => request.post('/ad/type/add', adTypeForm)
export const editAdType = (adTypeForm) => request.put('/ad/type/edit', adTypeForm)

// export const uploadAdImage = (file) => request.upload('/upload', file)