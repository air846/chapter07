import request from '@/utils/request'

// 获取仪表盘统计数据
export function getDashboardStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

// 获取最近订单
export function getRecentOrders() {
  return request({
    url: '/dashboard/recent-orders',
    method: 'get'
  })
}

// 获取销售趋势数据
export function getSalesTrend(params) {
  return request({
    url: '/dashboard/sales-trend',
    method: 'get',
    params
  })
}

// 获取订单状态分布
export function getOrderStatusDistribution() {
  return request({
    url: '/dashboard/order-status-distribution',
    method: 'get'
  })
}
