import request from '@/utils/request'

export const queryInfo = () => request.get('/articleType/getAll')

export const addArticleType = (articleTypeForm) => request.post('/articleType/add', articleTypeForm)
export const editArticleType = (articleTypeForm) => request.put('/articleType/edit', articleTypeForm)

export const deleteById = (articleTypeId) => request.delete(`/articleType?articleTypeId=${articleTypeId}`)