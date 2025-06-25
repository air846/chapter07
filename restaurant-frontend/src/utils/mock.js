// 开发环境模拟数据

// 模拟菜品分类数据
export const mockCategories = [
  { id: 1, name: '热菜', sort: 1, createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 2, name: '凉菜', sort: 2, createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 3, name: '汤类', sort: 3, createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 4, name: '主食', sort: 4, createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' }
]

// 模拟菜品数据
export const mockDishes = [
  {
    id: 1,
    name: '宫保鸡丁',
    categoryId: 1,
    categoryName: '热菜',
    price: 38.00,
    image: 'https://via.placeholder.com/150x150?text=宫保鸡丁',
    description: '传统川菜，鸡肉嫩滑，花生香脆',
    status: 1,
    createTime: '2025-06-25 10:00:00',
    updateTime: '2025-06-25 10:00:00'
  },
  {
    id: 2,
    name: '麻婆豆腐',
    categoryId: 1,
    categoryName: '热菜',
    price: 28.00,
    image: 'https://via.placeholder.com/150x150?text=麻婆豆腐',
    description: '经典川菜，麻辣鲜香',
    status: 1,
    createTime: '2025-06-25 10:00:00',
    updateTime: '2025-06-25 10:00:00'
  },
  {
    id: 3,
    name: '凉拌黄瓜',
    categoryId: 2,
    categoryName: '凉菜',
    price: 18.00,
    image: 'https://via.placeholder.com/150x150?text=凉拌黄瓜',
    description: '清爽开胃，夏日必备',
    status: 1,
    createTime: '2025-06-25 10:00:00',
    updateTime: '2025-06-25 10:00:00'
  }
]

// 模拟订单数据
export const mockOrders = [
  {
    id: 1,
    orderNumber: '202506251001',
    customerName: '张三',
    phone: '13800138001',
    address: '北京市朝阳区xxx街道xxx号',
    amount: 128.50,
    status: 2,
    payMethod: 1,
    payStatus: 1,
    orderTime: '2025-06-25 12:30:15',
    remark: '不要辣',
    items: [
      { dishId: 1, dishName: '宫保鸡丁', price: 38.00, number: 2, amount: 76.00 },
      { dishId: 2, dishName: '麻婆豆腐', price: 28.00, number: 1, amount: 28.00 },
      { dishId: 3, dishName: '凉拌黄瓜', price: 18.00, number: 1, amount: 18.00 }
    ]
  },
  {
    id: 2,
    orderNumber: '202506251002',
    customerName: '李四',
    phone: '13800138002',
    address: '北京市海淀区xxx街道xxx号',
    amount: 89.00,
    status: 3,
    payMethod: 2,
    payStatus: 1,
    orderTime: '2025-06-25 12:25:30',
    remark: '多放香菜',
    items: [
      { dishId: 1, dishName: '宫保鸡丁', price: 38.00, number: 1, amount: 38.00 },
      { dishId: 2, dishName: '麻婆豆腐', price: 28.00, number: 1, amount: 28.00 }
    ]
  }
]

// 模拟库存数据
export const mockInventory = [
  { id: 1, name: '鸡肉', quantity: 25.5, unit: '斤', createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 2, name: '豆腐', quantity: 8, unit: '盒', createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 3, name: '黄瓜', quantity: 15, unit: '斤', createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' },
  { id: 4, name: '花生米', quantity: 5, unit: '斤', createTime: '2025-06-25 10:00:00', updateTime: '2025-06-25 10:00:00' }
]

// 模拟库存预警数据
export const mockInventoryWarning = [
  { id: 4, name: '花生米', quantity: 5, unit: '斤' },
  { id: 2, name: '豆腐', quantity: 8, unit: '盒' }
]

// 模拟API响应
export function mockApiResponse(data, message = '操作成功') {
  return Promise.resolve({
    code: 1,
    msg: message,
    data: data
  })
}

// 模拟分页响应
export function mockPageResponse(data, total, page = 1, pageSize = 10) {
  const start = (page - 1) * pageSize
  const end = start + pageSize
  const records = data.slice(start, end)

  return mockApiResponse({
    records: records,
    total: total,
    page: page,
    pageSize: pageSize
  })
}

// 检查是否为开发环境且使用模拟数据
export function shouldUseMock() {
  // 开发环境下可以通过环境变量控制是否使用模拟数据
  // 设置 VUE_APP_USE_MOCK=true 使用模拟数据，否则使用真实API
  return process.env.VUE_APP_USE_MOCK === 'true'
}
