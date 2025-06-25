import request from '@/utils/request'
import { shouldUseMock, mockApiResponse, mockPageResponse, mockOrders } from '@/utils/mock'

// 获取订单列表
export function getOrderList(params) {
  if (shouldUseMock()) {
    return mockPageResponse(mockOrders, mockOrders.length, params.page, params.pageSize)
  }
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderDetail(id) {
  if (shouldUseMock()) {
    const order = mockOrders.find(o => o.id === id)
    return mockApiResponse(order)
  }
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

// 提交订单
export function submitOrder(data) {
  if (shouldUseMock()) {
    return mockApiResponse({ orderId: Date.now(), orderNumber: '202506251003' }, '下单成功')
  }
  return request({
    url: '/order/submit',
    method: 'post',
    data
  })
}

// 取消订单
export function cancelOrder(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '取消成功')
  }
  return request({
    url: `/order/${id}/cancel`,
    method: 'put'
  })
}

// 确认订单
export function confirmOrder(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '接单成功')
  }
  return request({
    url: `/order/${id}/confirm`,
    method: 'put'
  })
}

// 完成订单
export function completeOrder(id) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '订单完成')
  }
  return request({
    url: `/order/${id}/complete`,
    method: 'put'
  })
}

// 拒绝订单
export function rejectOrder(id, reason) {
  if (shouldUseMock()) {
    return mockApiResponse(null, '订单已拒绝')
  }
  return request({
    url: `/order/${id}/reject`,
    method: 'put',
    data: { reason }
  })
}

// 获取订单统计
export function getOrderStatistics(params) {
  return request({
    url: '/order/statistics',
    method: 'get',
    params
  })
}
