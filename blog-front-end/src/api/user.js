import request from '@/utils/request';

export const queryInfo = (params) => request.get(`/user/list?pageNum=${params.pageNum}&pageSize=${params.pageSize}&userId=${params.userId}&userName=${params.userName}`);
export const deleteById = (userId) => request.delete(`/user?userId=${userId}`);
export const freezeById = (userId) => request.put(`/user/freeze?userId=${userId}`);

// 编辑用户

export const addUser = (userForm) => request.post('/user/add', userForm);

export const queryUserById = (userId) => request.get(`/user/current?userId=${userId}`);
export const editUser = (userForm) => request.put('/user/edit', userForm);
export const likedBlogs = (addTime,limit,userId) => request.get(`/user/liked?addTime=${addTime}&limit=${limit}&userId=${userId}`);
export const collectBlogs = (addTime,limit,userId) => request.get(`/user/collected?addTime=${addTime}&limit=${limit}&userId=${userId}`);

export const followUser = (userId) => request.put(`/user/follow?userId=${userId}`);
export const isFollow = (userId) => request.get(`/user/isFollow?userId=${userId}`);

export const following = (pageNum,pageSize) => request.get(`/user/following?pageNum=${pageNum}&pageSize=${pageSize}`);
export const follower = (pageNum,pageSize) => request.get(`/user/followers?pageNum=${pageNum}&pageSize=${pageSize}`);

export const getMyArticles  = (pageNum,pageSize) => request.get(`/user/myArticles?pageNum=${pageNum}&pageSize=${pageSize}`);


