import request from '@/utils/request'

export const queryArticleComments = (articleId,limit) => request.get(`/comment/list?articleId=${articleId}&limit=${limit}`)
export const queryReplyComments = (commentId,limit) => request.get(`/comment/reply/list?commentId=${commentId}&limit=${limit}`)

export const addArticleComment = (commentForm) => request.post('/comment/add', commentForm)
export const addReplyComment = (commentForm) => request.post('/comment/reply/add', commentForm)

export const likeComment = (commentId) => request.put(`/comment/like?commentId=${commentId}`)
export const deleteComment = (commentId) => request.delete(`/comment/delete?commentId=${commentId}`)