import request from '@/utils/request'

export const queryInfo = () => request.get('/link/list')
export const deleteById = (linkId) => request.delete(`/link?linkId=${linkId}`)
export const addLink = (linkForm) => request.post('/link/add', linkForm)
export const editLink = (linkForm) => request.put('/link/edit', linkForm)