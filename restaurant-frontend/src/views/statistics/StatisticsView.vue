<template>
  <div class="statistics-container">
    <div class="page-container">
      <!-- 统计概览 -->
      <div class="stats-overview">
        <div class="stat-card" v-for="stat in overviewStats" :key="stat.title">
          <div class="stat-icon" :style="{ backgroundColor: stat.color }">
            <el-icon size="24">
              <component :is="stat.icon" />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-title">{{ stat.title }}</div>
          </div>
          <div class="stat-trend" :class="stat.trend">
            <el-icon size="16">
              <TrendCharts />
            </el-icon>
            <span>{{ stat.change }}</span>
          </div>
        </div>
      </div>

      <!-- 图表区域 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-header">
                <h3>销售趋势</h3>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="small"
                />
              </div>
              <div class="chart-content">
                <div ref="salesChartRef" class="sales-chart" style="width:100%;height:300px;"></div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-header">
                <h3>热门菜品</h3>
              </div>
              <div class="chart-content">
                <div class="popular-dishes">
                  <div 
                    class="dish-item" 
                    v-for="(dish, index) in popularDishes" 
                    :key="dish.id"
                  >
                    <div class="rank">{{ index + 1 }}</div>
                    <div class="dish-info">
                      <div class="dish-name">{{ dish.name }}</div>
                      <div class="dish-sales">销量: {{ dish.sales }}</div>
                    </div>
                    <div class="dish-amount">¥{{ dish.amount }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="8">
            <div class="chart-card">
              <div class="chart-header">
                <h3>订单状态分布</h3>
              </div>
              <div class="chart-content">
                <div ref="orderStatusChartRef" class="order-status-chart" style="width:100%;height:300px;"></div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="chart-card">
              <div class="chart-header">
                <h3>支付方式统计</h3>
              </div>
              <div class="chart-content">
                <div class="payment-stats">
                  <div class="payment-item">
                    <el-icon size="20" color="#67C23A">
                      <Money />
                    </el-icon>
                    <span class="payment-name">微信支付</span>
                    <span class="payment-percent">65%</span>
                  </div>
                  <div class="payment-item">
                    <el-icon size="20" color="#409EFF">
                      <Money />
                    </el-icon>
                    <span class="payment-name">支付宝</span>
                    <span class="payment-percent">35%</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="chart-card">
              <div class="chart-header">
                <h3>库存预警</h3>
              </div>
              <div class="chart-content">
                <div class="inventory-warnings">
                  <div 
                    class="warning-item" 
                    v-for="item in inventoryWarnings" 
                    :key="item.id"
                  >
                    <el-icon size="16" color="#F56C6C">
                      <Warning />
                    </el-icon>
                    <span class="item-name">{{ item.name }}</span>
                    <span class="item-stock">{{ item.quantity }}{{ item.unit }}</span>
                  </div>
                  <div v-if="inventoryWarnings.length === 0" class="no-warnings">
                    <el-icon size="20" color="#67C23A">
                      <Check />
                    </el-icon>
                    <span>库存充足</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="data-tables">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="今日订单" name="orders">
            <el-table :data="todayOrders" style="width: 100%">
              <el-table-column prop="orderNumber" label="订单号" width="180" />
              <el-table-column prop="customerName" label="客户" width="120" />
              <el-table-column prop="amount" label="金额" width="100">
                <template #default="scope">
                  ¥{{ scope.row.amount }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="orderTime" label="下单时间" />
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="热门菜品" name="dishes">
            <el-table :data="popularDishes" style="width: 100%">
              <el-table-column type="index" label="排名" width="80" />
              <el-table-column prop="name" label="菜品名称" />
              <el-table-column prop="sales" label="销量" width="100" />
              <el-table-column prop="amount" label="销售额" width="120">
                <template #default="scope">
                  ¥{{ scope.row.amount }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import {
  TrendCharts,
  PieChart,
  Money,
  Warning,
  Check,
  List,
  User,
  Food
} from '@element-plus/icons-vue'
import { getSalesTrend, getOrderStatusDistribution, getDashboardStats } from '@/api/dashboard'

export default {
  name: 'StatisticsView',
  components: {
    TrendCharts,
    PieChart,
    Money,
    Warning,
    Check,
    List,
    User,
    Food
  },
  setup() {
    const activeTab = ref('orders')
    const dateRange = ref([
      new Date(Date.now() - 6 * 24 * 3600 * 1000),
      new Date()
    ])

    const salesChartRef = ref(null)
    let salesChartInstance = null
    const orderStatusChartRef = ref(null)
    let orderStatusChartInstance = null

    // 渲染销售趋势图表
    const renderSalesChart = (trendData) => {
      if (!salesChartInstance) {
        salesChartInstance = echarts.init(salesChartRef.value)
        window.addEventListener('resize', salesChartInstance.resize)
      }

      const dates = trendData.map(item => item.date)
      const amounts = trendData.map(item => item.amount)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            data: amounts,
            type: 'line',
            smooth: true,
            areaStyle: {
              color: '#409EFF20'
            },
            lineStyle: {
              color: '#409EFF'
            }
          }
        ]
      }

      salesChartInstance.setOption(option)
    }

    // 加载销售趋势数据
    const loadSalesTrend = async () => {
      const [start, end] = dateRange.value || []
      const params = {}
      if (start && end) {
        params.startDate = start.toISOString().slice(0, 10)
        params.endDate = end.toISOString().slice(0, 10)
      }
      try {
        const res = await getSalesTrend(params)
        if (res.code === 1) {
          renderSalesChart(res.data)
        }
      } catch (e) {
        console.error('获取销售趋势失败', e)
      }
    }

    // 渲染订单状态分布饼图
    const renderOrderStatusChart = (data) => {
      if (!orderStatusChartInstance) {
        orderStatusChartInstance = echarts.init(orderStatusChartRef.value)
        window.addEventListener('resize', orderStatusChartInstance.resize)
      }

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            type: 'pie',
            radius: '70%',
            data: data.map(i => ({ name: i.label, value: i.count })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      orderStatusChartInstance.setOption(option)
    }

    // 加载订单状态分布
    const loadOrderStatusDistribution = async () => {
      try {
        const res = await getOrderStatusDistribution()
        if (res.code === 1) {
          renderOrderStatusChart(res.data)
        }
      } catch (e) {
        console.error('获取订单状态分布失败', e)
      }
    }

    // 概览统计
    const overviewStats = ref([
      { title: '今日订单', value: '0', change: '', trend: 'stable', color: '#409EFF', icon: 'List' },
      { title: '今日营业额', value: '¥0', change: '', trend: 'stable', color: '#67C23A', icon: 'Money' },
      { title: '菜品总数', value: '0', change: '', trend: 'stable', color: '#E6A23C', icon: 'Food' },
      { title: '用户总数', value: '0', change: '', trend: 'stable', color: '#F56C6C', icon: 'User' }
    ])

    const loadOverviewStats = async () => {
      try {
        const res = await getDashboardStats()
        if (res.code === 1) {
          const data = res.data
          overviewStats.value[0].value = data.todayOrderCount.toString()
          overviewStats.value[1].value = `¥${Number(data.todayRevenue).toLocaleString()}`
          overviewStats.value[2].value = data.dishCount.toString()
          overviewStats.value[3].value = data.userCount.toString()
        }
      } catch (e) {
        console.error('获取概览统计失败', e)
      }
    }

    // 热门菜品
    const popularDishes = ref([
      { id: 1, name: '宫保鸡丁', sales: 45, amount: 1710 },
      { id: 2, name: '麻婆豆腐', sales: 38, amount: 1140 },
      { id: 3, name: '红烧肉', sales: 32, amount: 1600 },
      { id: 4, name: '糖醋里脊', sales: 28, amount: 1120 },
      { id: 5, name: '鱼香肉丝', sales: 25, amount: 875 }
    ])

    // 库存预警
    const inventoryWarnings = ref([
      { id: 1, name: '鸡肉', quantity: 5, unit: '斤' },
      { id: 2, name: '土豆', quantity: 8, unit: '斤' },
      { id: 3, name: '豆腐', quantity: 3, unit: '盒' }
    ])

    // 今日订单
    const todayOrders = ref([
      {
        orderNumber: '202506251001',
        customerName: '张三',
        amount: 128.50,
        status: 5,
        orderTime: '2025-06-25 12:30:15'
      },
      {
        orderNumber: '202506251002',
        customerName: '李四',
        amount: 89.00,
        status: 3,
        orderTime: '2025-06-25 12:25:30'
      },
      {
        orderNumber: '202506251003',
        customerName: '王五',
        amount: 156.80,
        status: 5,
        orderTime: '2025-06-25 12:20:45'
      }
    ])

    // 获取订单状态类型
    const getStatusType = (status) => {
      const statusMap = {
        1: 'warning',
        2: 'info',
        3: 'primary',
        4: 'warning',
        5: 'success',
        6: 'danger'
      }
      return statusMap[status] || 'info'
    }

    // 获取订单状态文本
    const getStatusText = (status) => {
      const statusMap = {
        1: '待付款',
        2: '待接单',
        3: '已接单',
        4: '派送中',
        5: '已完成',
        6: '已取消'
      }
      return statusMap[status] || '未知'
    }

    onMounted(() => {
      loadOverviewStats()
      loadSalesTrend()
      loadOrderStatusDistribution()
    })

    // 监听日期变化
    watch(dateRange, () => {
      loadSalesTrend()
    })

    onBeforeUnmount(() => {
      if (salesChartInstance) {
        window.removeEventListener('resize', salesChartInstance.resize)
        salesChartInstance.dispose()
      }
      if (orderStatusChartInstance) {
        window.removeEventListener('resize', orderStatusChartInstance.resize)
        orderStatusChartInstance.dispose()
      }
    })

    return {
      activeTab,
      dateRange,
      overviewStats,
      popularDishes,
      inventoryWarnings,
      todayOrders,
      getStatusType,
      getStatusText,
      salesChartRef,
      orderStatusChartRef
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-container {
  height: 100%;
  background: $bg-color;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: $box-shadow-base;
  display: flex;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: $box-shadow-light;
  }
  
  .stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    margin-right: 16px;
  }
  
  .stat-content {
    flex: 1;
    
    .stat-value {
      font-size: 24px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: 4px;
    }
    
    .stat-title {
      font-size: 14px;
      color: $text-secondary;
    }
  }
  
  .stat-trend {
    display: flex;
    align-items: center;
    font-size: 12px;
    
    &.up {
      color: $success-color;
    }
    
    span {
      margin-left: 4px;
    }
  }
}

.charts-section {
  margin-bottom: 24px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  box-shadow: $box-shadow-base;
  overflow: hidden;
  
  .chart-header {
    padding: 20px;
    border-bottom: 1px solid $border-light;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: $text-primary;
    }
  }
  
  .chart-content {
    position: relative;
    min-height: 300px;
  }

  .sales-chart {
    width: 100%;
    height: 300px;
  }

  .order-status-chart {
    width: 100%;
    height: 300px;
  }
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  text-align: center;
  
  p {
    margin: 10px 0 5px;
    color: $text-secondary;
    font-size: 16px;
  }
  
  small {
    color: $text-placeholder;
    font-size: 12px;
  }
}

.popular-dishes {
  .dish-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid $border-extra-light;
    
    &:last-child {
      border-bottom: none;
    }
    
    .rank {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background: $primary-color;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      font-weight: 600;
      margin-right: 12px;
    }
    
    .dish-info {
      flex: 1;
      
      .dish-name {
        font-weight: 500;
        color: $text-primary;
        margin-bottom: 2px;
      }
      
      .dish-sales {
        font-size: 12px;
        color: $text-secondary;
      }
    }
    
    .dish-amount {
      font-weight: 600;
      color: $danger-color;
    }
  }
}

.payment-stats {
  .payment-item {
    display: flex;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid $border-extra-light;
    
    &:last-child {
      border-bottom: none;
    }
    
    .payment-name {
      flex: 1;
      margin-left: 8px;
      color: $text-primary;
    }
    
    .payment-percent {
      font-weight: 600;
      color: $primary-color;
    }
  }
}

.inventory-warnings {
  .warning-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid $border-extra-light;
    
    &:last-child {
      border-bottom: none;
    }
    
    .item-name {
      flex: 1;
      margin-left: 8px;
      color: $text-primary;
    }
    
    .item-stock {
      font-weight: 500;
      color: $danger-color;
    }
  }
  
  .no-warnings {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px 0;
    color: $success-color;
    
    span {
      margin-left: 8px;
    }
  }
}

.data-tables {
  background: white;
  border-radius: 12px;
  box-shadow: $box-shadow-base;
  padding: 20px;
}

// 响应式设计
@media (max-width: 768px) {
  .stats-overview {
    grid-template-columns: 1fr;
  }
  
  .charts-section {
    .el-col {
      margin-bottom: 20px;
    }
  }
}
</style>
