import request from '@/utils/request'

export const queryInfo = () => request.get('/articleTag/list')
export const deleteById = (articleTagId) => request.delete(`/articleTag?articleTagId=${articleTagId}`)

export const addArticleTag = (articleTagForm) => request.post('/articleTag/add', articleTagForm)
export const editArticleTag = (articleTagForm) => request.put('/articleTag/update', articleTagForm)