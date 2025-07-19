import request from '@/utils/request'

export const queryList = (article) => request.get('/articleList/list', { params: article })
export const deleteArticle = (articleId) => request.delete(`/articleList?articleId=${articleId}`)

export const getArticleList = (addTime,limit) => request.get(`/article/list?addTime=${addTime}&limit=${limit}`)

export const likeArticle = (articleId) => request.put(`/article/like?articleId=${articleId}`)
export const collectArticle = (articleId) => request.put(`/article/collect?articleId=${articleId}`)
export const getArticle = (articleId) => request.get(`/article/detail?articleId=${articleId}`)

export const addArticle = (articleForm) => request.post('/article/add', articleForm) // 添加文章

export const saveArticle = (articleForm) => request.post('/article/save', articleForm) // 保存文章（保存草稿）

export const articleDraft = () => request.get(`/article/draft`) // 获取草稿

export const getHotArticle = (limit) => request.get(`/article/hot/list?limit=${limit}`) // 获取热门文章

export const getArticleByType = (typeId,limit) => request.get(`/article/type/list?typeId=${typeId}&limit=${limit}`) // 获取分类文章

export const searchArticle  = (keyword,limit) => request.get(`/article/search?keyword=${keyword}&limit=${limit}`)



