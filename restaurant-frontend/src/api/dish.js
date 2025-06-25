import request from '@/utils/request'
import { shouldUseMock, mockApiResponse, mockPageResponse, mockDishes } from '@/utils/mock'

// 获取菜品列表
export function getDishList(params) {
  if (shouldUseMock()) {
    return mockPageResponse(mockDishes, mockDishes.length, params.page, params.pageSize)
  }
  return request({
    url: '/dish/list',
    method: 'get',
    params
  })
}

// 获取菜品详情
export function getDishDetail(id) {
  if (shouldUseMock()) {
    const dish = mockDishes.find(d => d.id === id)
    return mockApiResponse(dish)
  }
  return request({
    url: `/dish/${id}`,
    method: 'get'
  })
}

// 添加菜品
export function addDish(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '添加成功')
  }
  return request({
    url: '/dish',
    method: 'post',
    data
  })
}

// 更新菜品
export function updateDish(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '更新成功')
  }
  return request({
    url: '/dish',
    method: 'put',
    data
  })
}

// 删除菜品
export function deleteDish(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '删除成功')
  }
  return request({
    url: `/dish/${id}`,
    method: 'delete'
  })
}

// 启用/停售菜品
export function toggleDishStatus(id, status) {
  if (shouldUseMock()) {
    return mockApiResponse(null, status ? '启用成功' : '停用成功')
  }
  return request({
    url: `/dish/status/${id}`,
    method: 'put',
    data: { status }
  })
}

// 批量删除菜品
export function batchDeleteDish(ids) {
  return request({
    url: '/dish/batch',
    method: 'delete',
    data: { ids }
  })
}

// 上传菜品图片
export function uploadDishImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/dish/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
