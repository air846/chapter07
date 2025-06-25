<template>
  <div class="dashboard-container">
    <div class="page-container">
      <!-- 欢迎区域 -->
      <div class="welcome-section">
        <div class="welcome-content">
          <h1 class="welcome-title">
            <SplitText 
              text="欢迎使用餐饮管理系统" 
              :delay="80"
              :auto-play="true"
            />
          </h1>
          <p class="welcome-subtitle">今天是个美好的一天，让我们开始工作吧！</p>
        </div>
        <div class="welcome-time">
          <div class="current-time">{{ currentTime }}</div>
          <div class="current-date">{{ currentDate }}</div>
        </div>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="stat-card" v-for="stat in stats" :key="stat.title">
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

      <!-- 快捷操作 -->
      <div class="quick-actions">
        <h2 class="section-title">快捷操作</h2>
        <div class="actions-grid">
          <div 
            class="action-card" 
            v-for="action in quickActions" 
            :key="action.title"
            @click="handleQuickAction(action.path)"
          >
            <div class="action-icon" :style="{ backgroundColor: action.color }">
              <el-icon size="20">
                <component :is="action.icon" />
              </el-icon>
            </div>
            <div class="action-title">{{ action.title }}</div>
            <div class="action-desc">{{ action.description }}</div>
          </div>
        </div>
      </div>

      <!-- 最近订单 -->
      <div class="recent-orders">
        <h2 class="section-title">最近订单</h2>
        <div class="orders-table">
          <el-table :data="recentOrders" style="width: 100%">
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
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboardStats, getRecentOrders } from '@/api/dashboard'
import {
  Food,
  List,
  User,
  Money,
  TrendCharts,
  Plus,
  View as ViewIcon,
  Setting
} from '@element-plus/icons-vue'
import SplitText from '@/components/common/SplitText.vue'

export default {
  name: 'DashboardView',
  components: {
    SplitText,
    Food,
    List,
    User,
    Money,
    TrendCharts,
    Plus,
    ViewIcon,
    Setting
  },
  setup() {
    const router = useRouter()
    
    const currentTime = ref('')
    const currentDate = ref('')
    let timeInterval = null

    // 统计数据
    const stats = ref([
      {
        title: '今日订单',
        value: '0',
        change: '+0%',
        trend: 'up',
        color: '#409EFF',
        icon: 'List'
      },
      {
        title: '今日营业额',
        value: '¥0',
        change: '+0%',
        trend: 'up',
        color: '#67C23A',
        icon: 'Money'
      },
      {
        title: '菜品总数',
        value: '0',
        change: '+0',
        trend: 'up',
        color: '#E6A23C',
        icon: 'Food'
      },
      {
        title: '员工总数',
        value: '0',
        change: '0',
        trend: 'stable',
        color: '#F56C6C',
        icon: 'User'
      }
    ])

    // 快捷操作
    const quickActions = ref([
      {
        title: '添加菜品',
        description: '快速添加新菜品',
        icon: 'Plus',
        color: '#409EFF',
        path: '/dish'
      },
      {
        title: '查看订单',
        description: '查看最新订单',
        icon: 'ViewIcon',
        color: '#67C23A',
        path: '/order'
      },
      {
        title: '库存管理',
        description: '管理库存信息',
        icon: 'Setting',
        color: '#E6A23C',
        path: '/inventory'
      }
    ])

    // 最近订单
    const recentOrders = ref([])

    // 加载仪表盘数据
    const loadDashboardData = async () => {
      try {
        // 加载统计数据
        const statsRes = await getDashboardStats()
        if (statsRes.code === 1) {
          const data = statsRes.data
          stats.value[0].value = data.todayOrderCount.toString()
          stats.value[1].value = `¥${data.todayRevenue.toLocaleString()}`
          stats.value[2].value = data.dishCount.toString()
          stats.value[3].value = data.userCount.toString()
        }

        // 加载最近订单
        const ordersRes = await getRecentOrders()
        if (ordersRes.code === 1) {
          recentOrders.value = ordersRes.data.records || []
        }
      } catch (error) {
        console.error('加载仪表盘数据失败:', error)
      }
    }

    // 更新时间
    const updateTime = () => {
      const now = new Date()
      currentTime.value = now.toLocaleTimeString('zh-CN', { 
        hour12: false,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
      currentDate.value = now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      })
    }

    // 处理快捷操作
    const handleQuickAction = (path) => {
      router.push(path)
    }

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
      updateTime()
      timeInterval = setInterval(updateTime, 1000)
      loadDashboardData()
    })

    onUnmounted(() => {
      if (timeInterval) {
        clearInterval(timeInterval)
      }
    })

    return {
      currentTime,
      currentDate,
      stats,
      quickActions,
      recentOrders,
      handleQuickAction,
      getStatusType,
      getStatusText,
      loadDashboardData
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  height: 100%;
  background: $bg-color;
}

.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px;
  border-radius: 12px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .welcome-content {
    .welcome-title {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 8px;
      min-height: 40px;
    }
    
    .welcome-subtitle {
      font-size: 16px;
      opacity: 0.9;
    }
  }
  
  .welcome-time {
    text-align: right;
    
    .current-time {
      font-size: 28px;
      font-weight: 600;
      margin-bottom: 4px;
    }
    
    .current-date {
      font-size: 14px;
      opacity: 0.8;
    }
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
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
    
    &.down {
      color: $danger-color;
    }
    
    &.stable {
      color: $text-secondary;
    }
    
    span {
      margin-left: 4px;
    }
  }
}

.quick-actions {
  margin-bottom: 32px;
  
  .section-title {
    font-size: 20px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 16px;
  }
  
  .actions-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }
  
  .action-card {
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: $box-shadow-base;
    text-align: center;
    cursor: pointer;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: $box-shadow-light;
    }
    
    .action-icon {
      width: 40px;
      height: 40px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      margin: 0 auto 12px;
    }
    
    .action-title {
      font-size: 16px;
      font-weight: 500;
      color: $text-primary;
      margin-bottom: 4px;
    }
    
    .action-desc {
      font-size: 12px;
      color: $text-secondary;
    }
  }
}

.recent-orders {
  .section-title {
    font-size: 20px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 16px;
  }
  
  .orders-table {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: $box-shadow-base;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .welcome-section {
    flex-direction: column;
    text-align: center;
    
    .welcome-time {
      margin-top: 20px;
      text-align: center;
    }
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>
