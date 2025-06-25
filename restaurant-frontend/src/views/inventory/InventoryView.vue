<template>
  <div class="inventory-container">
    <div class="page-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <h2>库存管理</h2>
        </div>
        <div class="toolbar-right">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加库存
          </el-button>
          <el-button type="success" @click="handleStockIn">
            <el-icon><Top /></el-icon>
            入库
          </el-button>
          <el-button type="warning" @click="handleStockOut">
            <el-icon><Bottom /></el-icon>
            出库
          </el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :model="queryParams" inline>
          <el-form-item label="物品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入物品名称"
              clearable
              style="width: 200px"
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

      <!-- 库存预警 -->
      <div v-if="warningList.length > 0" class="warning-section">
        <el-alert
          title="库存预警"
          type="warning"
          :closable="false"
          show-icon
        >
          <template #default>
            <div class="warning-content">
              <span>以下物品库存不足：</span>
              <el-tag
                v-for="item in warningList"
                :key="item.id"
                type="warning"
                size="small"
                style="margin-left: 8px"
              >
                {{ item.name }}({{ item.quantity }}{{ item.unit }})
              </el-tag>
            </div>
          </template>
        </el-alert>
      </div>

      <!-- 库存列表 -->
      <div class="inventory-list">
        <el-table
          :data="inventoryList"
          v-loading="loading"
          style="width: 100%"
          row-key="id"
        >
          <el-table-column prop="name" label="物品名称" min-width="150">
            <template #default="scope">
              <div class="item-name">
                <el-icon size="16" color="#409EFF">
                  <Box />
                </el-icon>
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="quantity" label="当前库存" width="120" align="center">
            <template #default="scope">
              <span 
                class="quantity" 
                :class="{ 'low-stock': scope.row.quantity < 10 }"
              >
                {{ scope.row.quantity }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column prop="unit" label="单位" width="80" align="center" />
          
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="updateTime" label="更新时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.updateTime) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="320" align="center">
            <template #default="scope">
              <div class="btn-group">
                <el-button size="small" @click="handleEdit(scope.row)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button 
                  size="small" 
                  type="success"
                  @click="handleStockInItem(scope.row)"
                >
                  <el-icon><Top /></el-icon>
                  入库
                </el-button>
                <el-button 
                  size="small" 
                  type="warning"
                  @click="handleStockOutItem(scope.row)"
                >
                  <el-icon><Bottom /></el-icon>
                  出库
                </el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  @click="handleDelete(scope.row)"
                >
                  <el-icon><Delete /></el-icon>
                  删除
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

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="物品名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入物品名称"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="库存数量" prop="quantity">
          <el-input-number
            v-model="form.quantity"
            :min="0"
            :precision="2"
            placeholder="请输入库存数量"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="单位" prop="unit">
          <el-select
            v-model="form.unit"
            placeholder="请选择单位"
            style="width: 100%"
          >
            <el-option label="个" value="个" />
            <el-option label="斤" value="斤" />
            <el-option label="公斤" value="公斤" />
            <el-option label="袋" value="袋" />
            <el-option label="箱" value="箱" />
            <el-option label="瓶" value="瓶" />
            <el-option label="包" value="包" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 入库/出库对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      :title="stockDialogTitle"
      width="400px"
    >
      <el-form
        ref="stockFormRef"
        :model="stockForm"
        :rules="stockRules"
        label-width="80px"
      >
        <el-form-item label="物品名称">
          <el-input v-model="stockForm.itemName" disabled />
        </el-form-item>
        
        <el-form-item label="当前库存">
          <el-input v-model="stockForm.currentStock" disabled />
        </el-form-item>
        
        <el-form-item :label="stockType === 'in' ? '入库数量' : '出库数量'" prop="quantity">
          <el-input-number
            v-model="stockForm.quantity"
            :min="1"
            :max="stockType === 'out' ? stockForm.currentStock : 9999"
            :precision="2"
            placeholder="请输入数量"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input
            v-model="stockForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button 
            :type="stockType === 'in' ? 'success' : 'warning'" 
            @click="handleStockSubmit" 
            :loading="stockLoading"
          >
            确定{{ stockType === 'in' ? '入库' : '出库' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Top,
  Bottom,
  Search,
  Refresh,
  Box,
  Edit,
  Delete
} from '@element-plus/icons-vue'
import {
  getInventoryList,
  addInventory,
  updateInventory,
  deleteInventory,
  stockIn,
  stockOut,
  getInventoryWarning
} from '@/api/inventory'

export default {
  name: 'InventoryView',
  components: {
    Plus,
    Top,
    Bottom,
    Search,
    Refresh,
    Box,
    Edit,
    Delete
  },
  setup() {
    const formRef = ref(null)
    const stockFormRef = ref(null)
    const loading = ref(false)
    const submitLoading = ref(false)
    const stockLoading = ref(false)
    const dialogVisible = ref(false)
    const stockDialogVisible = ref(false)
    const dialogTitle = ref('')
    const stockDialogTitle = ref('')
    const stockType = ref('in') // 'in' 入库, 'out' 出库
    const inventoryList = ref([])
    const warningList = ref([])
    const total = ref(0)

    const queryParams = reactive({
      page: 1,
      pageSize: 10,
      name: ''
    })

    const form = reactive({
      id: null,
      name: '',
      quantity: 0,
      unit: ''
    })

    const stockForm = reactive({
      id: null,
      itemName: '',
      currentStock: 0,
      quantity: 1,
      remark: ''
    })

    const rules = {
      name: [
        { required: true, message: '请输入物品名称', trigger: 'blur' },
        { min: 2, max: 50, message: '物品名称长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      quantity: [
        { required: true, message: '请输入库存数量', trigger: 'blur' },
        { type: 'number', min: 0, message: '库存数量不能小于0', trigger: 'blur' }
      ],
      unit: [
        { required: true, message: '请选择单位', trigger: 'change' }
      ]
    }

    const stockRules = {
      quantity: [
        { required: true, message: '请输入数量', trigger: 'blur' },
        { type: 'number', min: 1, message: '数量不能小于1', trigger: 'blur' }
      ]
    }

    // 获取库存列表
    const getList = async () => {
      try {
        loading.value = true
        const response = await getInventoryList(queryParams)
        if (response.code === 1) {
          inventoryList.value = response.data.content || []
          total.value = response.data.totalElements || 0
        }
      } catch (error) {
        ElMessage.error('获取库存列表失败')
      } finally {
        loading.value = false
      }
    }

    // 获取库存预警
    const getWarningList = async () => {
      try {
        const response = await getInventoryWarning()
        if (response.code === 1) {
          warningList.value = response.data || []
        }
      } catch (error) {
        console.error('获取库存预警失败:', error)
      }
    }

    // 重置表单
    const resetForm = () => {
      form.id = null
      form.name = ''
      form.quantity = 0
      form.unit = ''
      if (formRef.value) {
        formRef.value.resetFields()
      }
    }

    // 重置库存表单
    const resetStockForm = () => {
      stockForm.id = null
      stockForm.itemName = ''
      stockForm.currentStock = 0
      stockForm.quantity = 1
      stockForm.remark = ''
      if (stockFormRef.value) {
        stockFormRef.value.resetFields()
      }
    }

    // 搜索
    const handleSearch = () => {
      queryParams.page = 1
      getList()
    }

    // 重置搜索
    const handleReset = () => {
      queryParams.name = ''
      queryParams.page = 1
      getList()
    }

    // 添加库存
    const handleAdd = () => {
      resetForm()
      dialogTitle.value = '添加库存'
      dialogVisible.value = true
    }

    // 编辑库存
    const handleEdit = (row) => {
      resetForm()
      form.id = row.id
      form.name = row.name
      form.quantity = row.quantity
      form.unit = row.unit
      dialogTitle.value = '编辑库存'
      dialogVisible.value = true
    }

    // 删除库存
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除库存"${row.name}"吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await deleteInventory(row.id)
        if (response.code === 1) {
          ElMessage.success('删除成功')
          getList()
          getWarningList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    // 批量入库
    const handleStockIn = () => {
      ElMessage.info('请选择具体物品进行入库操作')
    }

    // 批量出库
    const handleStockOut = () => {
      ElMessage.info('请选择具体物品进行出库操作')
    }

    // 单个物品入库
    const handleStockInItem = (row) => {
      resetStockForm()
      stockForm.id = row.id
      stockForm.itemName = row.name
      stockForm.currentStock = row.quantity
      stockType.value = 'in'
      stockDialogTitle.value = '入库操作'
      stockDialogVisible.value = true
    }

    // 单个物品出库
    const handleStockOutItem = (row) => {
      if (row.quantity <= 0) {
        ElMessage.warning('当前库存为0，无法出库')
        return
      }

      resetStockForm()
      stockForm.id = row.id
      stockForm.itemName = row.name
      stockForm.currentStock = row.quantity
      stockType.value = 'out'
      stockDialogTitle.value = '出库操作'
      stockDialogVisible.value = true
    }

    // 提交表单
    const handleSubmit = async () => {
      if (!formRef.value) return

      try {
        const valid = await formRef.value.validate()
        if (!valid) return

        submitLoading.value = true

        let response
        if (form.id) {
          response = await updateInventory(form)
        } else {
          response = await addInventory(form)
        }

        if (response.code === 1) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          getList()
          getWarningList()
        }
      } catch (error) {
        ElMessage.error(form.id ? '更新失败' : '添加失败')
      } finally {
        submitLoading.value = false
      }
    }

    // 提交库存操作
    const handleStockSubmit = async () => {
      if (!stockFormRef.value) return

      try {
        const valid = await stockFormRef.value.validate()
        if (!valid) return

        stockLoading.value = true

        const data = {
          id: stockForm.id,
          quantity: stockForm.quantity,
          remark: stockForm.remark
        }

        let response
        if (stockType.value === 'in') {
          response = await stockIn(data)
        } else {
          response = await stockOut(data)
        }

        if (response.code === 1) {
          ElMessage.success(stockType.value === 'in' ? '入库成功' : '出库成功')
          stockDialogVisible.value = false
          getList()
          getWarningList()
        }
      } catch (error) {
        ElMessage.error(stockType.value === 'in' ? '入库失败' : '出库失败')
      } finally {
        stockLoading.value = false
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      resetForm()
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

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    onMounted(() => {
      getList()
      getWarningList()
    })

    return {
      formRef,
      stockFormRef,
      loading,
      submitLoading,
      stockLoading,
      dialogVisible,
      stockDialogVisible,
      dialogTitle,
      stockDialogTitle,
      stockType,
      inventoryList,
      warningList,
      total,
      queryParams,
      form,
      stockForm,
      rules,
      stockRules,
      getList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleDelete,
      handleStockIn,
      handleStockOut,
      handleStockInItem,
      handleStockOutItem,
      handleSubmit,
      handleStockSubmit,
      handleDialogClose,
      handleSizeChange,
      handleCurrentChange,
      formatDate
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-container {
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

.warning-section {
  margin-bottom: 20px;

  .warning-content {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
  }
}

.inventory-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
}

.item-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quantity {
  font-weight: 600;
  font-size: 16px;

  &.low-stock {
    color: $danger-color;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
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

:deep(.el-alert) {
  .el-alert__content {
    .el-alert__title {
      font-weight: 600;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .toolbar {
    .toolbar-right {
      .el-button {
        margin-bottom: 8px;
      }
    }
  }

  .search-bar {
    .el-form {
      .el-form-item {
        margin-bottom: 15px;

        .el-input {
          width: 100% !important;
        }
      }
    }
  }

  .inventory-list {
    :deep(.el-table) {
      .el-table__header,
      .el-table__body {
        min-width: 900px;
      }
    }
  }

  .warning-section {
    .warning-content {
      flex-direction: column;
      align-items: flex-start;
    }
  }
}
</style>
