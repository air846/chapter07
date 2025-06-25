import request from '@/utils/request'
import { shouldUseMock, mockApiResponse, mockPageResponse, mockInventory, mockInventoryWarning } from '@/utils/mock'

// 获取库存列表
export function getInventoryList(params) {
  if (shouldUseMock()) {
    return mockPageResponse(mockInventory, mockInventory.length, params.page, params.pageSize)
  }
  return request({
    url: '/inventory/list',
    method: 'get',
    params
  })
}

// 添加库存
export function addInventory(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '添加成功')
  }
  return request({
    url: '/inventory',
    method: 'post',
    data
  })
}

// 更新库存
export function updateInventory(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '更新成功')
  }
  return request({
    url: '/inventory',
    method: 'put',
    data
  })
}

// 删除库存
export function deleteInventory(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '删除成功')
  }
  return request({
    url: `/inventory/${id}`,
    method: 'delete'
  })
}

// 库存入库
export function stockIn(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '入库成功')
  }
  return request({
    url: '/inventory/stock-in',
    method: 'post',
    data
  })
}

// 库存出库
export function stockOut(data) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '出库成功')
  }
  return request({
    url: '/inventory/stock-out',
    method: 'post',
    data
  })
}

// 获取库存预警列表
export function getInventoryWarning() {
  if (shouldUseMock()) {
    return mockApiResponse(mockInventoryWarning)
  }
  return request({
    url: '/inventory/warning',
    method: 'get'
  })
}
