import request from '@/utils/request'
import { shouldUseMock, mockApiResponse, mockPageResponse, mockCategories } from '@/utils/mock'

// 获取分类列表
export function getCategoryList(params) {
  if (shouldUseMock()) {
    return mockPageResponse(mockCategories, mockCategories.length, params.page, params.pageSize)
  }
  return request({
    url: '/category/list',
    method: 'get',
    params
  })
}

// 获取所有分类（不分页）
export function getAllCategories() {
  if (shouldUseMock()) {
    return mockApiResponse(mockCategories)
  }
  return request({
    url: '/category/all',
    method: 'get'
  })
}

// 添加分类
export function addCategory(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '添加成功')
  }
  return request({
    url: '/category',
    method: 'post',
    data
  })
}

// 更新分类
export function updateCategory(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '更新成功')
  }
  return request({
    url: '/category',
    method: 'put',
    data
  })
}

// 删除分类
export function deleteCategory(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '删除成功')
  }
  return request({
    url: `/category/${id}`,
    method: 'delete'
  })
}

// 更新分类排序
export function updateCategorySort(data) {
  return request({
    url: '/category/sort',
    method: 'put',
    data
  })
}
