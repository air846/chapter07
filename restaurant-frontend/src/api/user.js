import request from '@/utils/request'
import { shouldUseMock, mockApiResponse } from '@/utils/mock'

// 用户登录
export function login(data) {
  if (shouldUseMock()) {
    // 模拟登录逻辑在store中处理
    return Promise.resolve()
  }
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 用户登出
export function logout() {
  if (shouldUseMock()) {
    return mockApiResponse(null, '退出成功')
  }
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getUserInfo(params) {
  if (shouldUseMock()) {
    return mockApiResponse({
      id: 1,
      username: 'admin',
      name: '管理员',
      role: 'ADMIN'
    })
  }
  return request({
    url: '/user/info',
    method: 'get',
    params
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

// 根据ID获取用户详情
export function getUserDetail(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

// 启用/禁用用户
export function toggleUserStatus(id, status) {
  return request({
    url: `/user/${id}/status`,
    method: 'put',
    data: { status }
  })
}
