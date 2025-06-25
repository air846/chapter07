<template>
  <div class="order-container">
    <div class="page-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <h2>订单管理</h2>
        </div>
        <div class="toolbar-right">
          <el-button @click="getList">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :model="queryParams" inline>
          <el-form-item label="订单号">
            <el-input
              v-model="queryParams.orderNumber"
              placeholder="请输入订单号"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="订单状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择状态"
              clearable
              style="width: 150px"
            >
              <el-option label="待付款" :value="1" />
              <el-option label="待接单" :value="2" />
              <el-option label="已接单" :value="3" />
              <el-option label="派送中" :value="4" />
              <el-option label="已完成" :value="5" />
              <el-option label="已取消" :value="6" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="下单时间">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 订单列表 -->
      <div class="order-list">
        <el-table
          :data="orderList"
          v-loading="loading"
          style="width: 100%"
          row-key="id"
        >
          <el-table-column prop="orderNumber" label="订单号" width="180">
            <template #default="scope">
              <el-link type="primary" @click="handleViewDetail(scope.row)">
                {{ scope.row.orderNumber }}
              </el-link>
            </template>
          </el-table-column>
          
          <el-table-column prop="customerName" label="客户" width="120" />
          
          <el-table-column prop="amount" label="订单金额" width="120" align="center">
            <template #default="scope">
              <span class="amount">¥{{ scope.row.amount }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="订单状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="payMethod" label="支付方式" width="100" align="center">
            <template #default="scope">
              <el-tag v-if="scope.row.payMethod === 1" type="success">微信</el-tag>
              <el-tag v-else-if="scope.row.payMethod === 2" type="primary">支付宝</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="orderTime" label="下单时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.orderTime) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="remark" label="备注" min-width="150">
            <template #default="scope">
              <span class="remark">{{ scope.row.remark || '-' }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template #default="scope">
              <div class="btn-group">
                <el-button size="small" @click="handleViewDetail(scope.row)">
                  <el-icon><ViewIcon /></el-icon>
                  详情
                </el-button>
                
                <el-button 
                  v-if="scope.row.status === 2"
                  size="small" 
                  type="success"
                  @click="handleConfirm(scope.row)"
                >
                  <el-icon><Check /></el-icon>
                  接单
                </el-button>
                
                <el-button 
                  v-if="scope.row.status === 3"
                  size="small" 
                  type="primary"
                  @click="handleComplete(scope.row)"
                >
                  <el-icon><Finished /></el-icon>
                  完成
                </el-button>
                
                <el-button 
                  v-if="[2, 3].includes(scope.row.status)"
                  size="small" 
                  type="danger"
                  @click="handleReject(scope.row)"
                >
                  <el-icon><Close /></el-icon>
                  拒绝
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="800px"
    >
      <div v-if="orderDetail" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">
            {{ orderDetail.orderNumber }}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(orderDetail.status)">
              {{ getStatusText(orderDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="客户姓名">
            {{ orderDetail.customerName }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ orderDetail.phone || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="配送地址" :span="2">
            {{ orderDetail.address || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="支付方式">
            <el-tag v-if="orderDetail.payMethod === 1" type="success">微信支付</el-tag>
            <el-tag v-else-if="orderDetail.payMethod === 2" type="primary">支付宝</el-tag>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span class="amount">¥{{ orderDetail.amount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">
            {{ formatDate(orderDetail.orderTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">
            {{ orderDetail.remark || '无' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="order-items">
          <h3>订单明细</h3>
          <el-table :data="orderDetail.items" style="width: 100%">
            <el-table-column prop="dishName" label="菜品名称" />
            <el-table-column prop="price" label="单价" width="100" align="center">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="number" label="数量" width="80" align="center" />
            <el-table-column prop="amount" label="小计" width="100" align="center">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>

    <!-- 拒绝订单对话框 -->
    <el-dialog
      v-model="rejectVisible"
      title="拒绝订单"
      width="400px"
    >
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="rejectVisible = false">取消</el-button>
          <el-button type="danger" @click="handleRejectConfirm">确定拒绝</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Refresh,
  Search,
  View as ViewIcon,
  Check,
  Finished,
  Close
} from '@element-plus/icons-vue'
import {
  getOrderList,
  getOrderDetail,
  confirmOrder,
  completeOrder,
  rejectOrder
} from '@/api/order'

export default {
  name: 'OrderView',
  components: {
    Refresh,
    Search,
    ViewIcon,
    Check,
    Finished,
    Close
  },
  setup() {
    const loading = ref(false)
    const detailVisible = ref(false)
    const rejectVisible = ref(false)
    const orderList = ref([])
    const orderDetail = ref(null)
    const total = ref(0)
    const dateRange = ref([])
    const currentRejectOrder = ref(null)

    const queryParams = reactive({
      page: 1,
      pageSize: 10,
      orderNumber: '',
      status: null,
      startTime: '',
      endTime: ''
    })

    const rejectForm = reactive({
      reason: ''
    })

    // 获取订单列表
    const getList = async () => {
      try {
        loading.value = true

        // 处理日期范围
        if (dateRange.value && dateRange.value.length === 2) {
          queryParams.startTime = dateRange.value[0]
          queryParams.endTime = dateRange.value[1]
        } else {
          queryParams.startTime = ''
          queryParams.endTime = ''
        }

        const response = await getOrderList(queryParams)
        if (response.code === 1) {
          orderList.value = response.data.content || []
          total.value = response.data.totalElements || 0
        }
      } catch (error) {
        ElMessage.error('获取订单列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      queryParams.page = 1
      getList()
    }

    // 重置搜索
    const handleReset = () => {
      queryParams.orderNumber = ''
      queryParams.status = null
      dateRange.value = []
      queryParams.page = 1
      getList()
    }

    // 查看详情
    const handleViewDetail = async (row) => {
      try {
        const response = await getOrderDetail(row.id)
        if (response.code === 1) {
          orderDetail.value = response.data
          detailVisible.value = true
        }
      } catch (error) {
        ElMessage.error('获取订单详情失败')
      }
    }

    // 确认接单
    const handleConfirm = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要接受订单"${row.orderNumber}"吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
          }
        )

        const response = await confirmOrder(row.id)
        if (response.code === 1) {
          ElMessage.success('接单成功')
          getList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('接单失败')
        }
      }
    }

    // 完成订单
    const handleComplete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要完成订单"${row.orderNumber}"吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
          }
        )

        const response = await completeOrder(row.id)
        if (response.code === 1) {
          ElMessage.success('订单完成')
          getList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      }
    }

    // 拒绝订单
    const handleReject = (row) => {
      currentRejectOrder.value = row
      rejectForm.reason = ''
      rejectVisible.value = true
    }

    // 确认拒绝订单
    const handleRejectConfirm = async () => {
      if (!rejectForm.reason.trim()) {
        ElMessage.warning('请输入拒绝原因')
        return
      }

      try {
        const response = await rejectOrder(currentRejectOrder.value.id, rejectForm.reason)
        if (response.code === 1) {
          ElMessage.success('订单已拒绝')
          rejectVisible.value = false
          getList()
        }
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }

    // 分页大小改变
    const handleSizeChange = (size) => {
      queryParams.pageSize = size
      queryParams.page = 1
      getList()
    }

    // 当前页改变
    const handleCurrentChange = (page) => {
      queryParams.page = page
      getList()
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

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    onMounted(() => {
      getList()
    })

    return {
      loading,
      detailVisible,
      rejectVisible,
      orderList,
      orderDetail,
      total,
      dateRange,
      queryParams,
      rejectForm,
      getList,
      handleSearch,
      handleReset,
      handleViewDetail,
      handleConfirm,
      handleComplete,
      handleReject,
      handleRejectConfirm,
      handleSizeChange,
      handleCurrentChange,
      getStatusType,
      getStatusText,
      formatDate
    }
  }
}
</script>

<style lang="scss" scoped>
.order-container {
  height: 100%;
  background: $bg-color;
}

.search-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
}

.order-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
}

.amount {
  font-weight: 600;
  color: $danger-color;
  font-size: 16px;
}

.remark {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 150px;
  display: inline-block;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.order-detail {
  .order-items {
    margin-top: 20px;

    h3 {
      margin-bottom: 15px;
      color: $text-primary;
    }
  }
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: $text-primary;
      font-weight: 600;
    }
  }
}

:deep(.el-descriptions) {
  .el-descriptions__label {
    font-weight: 500;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .search-bar {
    .el-form {
      .el-form-item {
        margin-bottom: 15px;

        .el-input,
        .el-select,
        .el-date-picker {
          width: 100% !important;
        }
      }
    }
  }

  .order-list {
    :deep(.el-table) {
      .el-table__header,
      .el-table__body {
        min-width: 1000px;
      }
    }
  }
}
</style>
