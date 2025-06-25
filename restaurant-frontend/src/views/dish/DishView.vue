<template>
  <div class="dish-container">
    <div class="page-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <h2>菜品管理</h2>
        </div>
        <div class="toolbar-right">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加菜品
          </el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :model="queryParams" inline>
          <el-form-item label="菜品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入菜品名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="菜品分类">
            <el-select
              v-model="queryParams.categoryId"
              placeholder="请选择分类"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="category in categoryOptions"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="起售" :value="1" />
              <el-option label="停售" :value="0" />
            </el-select>
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

      <!-- 菜品列表 -->
      <div class="dish-list">
        <el-table
          :data="dishList"
          v-loading="loading"
          style="width: 100%"
          row-key="id"
        >
          <el-table-column prop="name" label="菜品名称" min-width="150">
            <template #default="scope">
              <div class="dish-name">
                <span class="name">{{ scope.row.name }}</span>
                <p class="description">{{ scope.row.description || '暂无描述' }}</p>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="categoryName" label="分类" width="120" />
          
          <el-table-column prop="price" label="价格" width="100" align="center">
            <template #default="scope">
              <span class="price">¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                active-text="起售"
                inactive-text="停售"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200" align="center">
            <template #default="scope">
              <div class="btn-group">
                <el-button size="small" @click="handleEdit(scope.row)">
                  <el-icon><Edit /></el-icon>
                  编辑
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
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜品名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入菜品名称"
                clearable
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="菜品分类" prop="categoryId">
              <el-select
                v-model="form.categoryId"
                placeholder="请选择分类"
                style="width: 100%"
              >
                <el-option
                  v-for="category in categoryOptions"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜品价格" prop="price">
              <el-input-number
                v-model="form.price"
                :min="0"
                :precision="2"
                placeholder="请输入价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="菜品状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">起售</el-radio>
                <el-radio :label="0">停售</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="菜品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入菜品描述"
          />
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
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Edit,
  Delete
} from '@element-plus/icons-vue'
import { getDishList, addDish, updateDish, deleteDish, toggleDishStatus } from '@/api/dish'
import { getAllCategories } from '@/api/category'

export default {
  name: 'DishView',
  components: {
    Search,
    Refresh,
    Edit,
    Delete
  },
  setup() {
    const formRef = ref(null)
    const loading = ref(false)
    const submitLoading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const dishList = ref([])
    const categoryOptions = ref([])
    const total = ref(0)

    const queryParams = reactive({
      page: 1,
      pageSize: 10,
      name: '',
      categoryId: null,
      status: null
    })

    const form = reactive({
      id: null,
      name: '',
      categoryId: null,
      price: 0,
      image: '',
      description: '',
      status: 1
    })

    const rules = {
      name: [
        { required: true, message: '请输入菜品名称', trigger: 'blur' },
        { min: 2, max: 50, message: '菜品名称长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      categoryId: [
        { required: true, message: '请选择菜品分类', trigger: 'change' }
      ],
      price: [
        { required: true, message: '请输入菜品价格', trigger: 'blur' },
        { type: 'number', min: 0, message: '价格不能小于0', trigger: 'blur' }
      ]
    }

    // 获取菜品列表
    const getList = async () => {
      try {
        loading.value = true
        const response = await getDishList(queryParams)
        if (response.code === 1) {
          dishList.value = response.data.content || []
          total.value = response.data.totalElements || 0
        }
      } catch (error) {
        ElMessage.error('获取菜品列表失败')
      } finally {
        loading.value = false
      }
    }

    // 获取分类选项
    const getCategoryOptions = async () => {
      try {
        const response = await getAllCategories()
        if (response.code === 1) {
          categoryOptions.value = response.data || []
        }
      } catch (error) {
        ElMessage.error('获取分类列表失败')
      }
    }

    // 重置表单
    const resetForm = () => {
      form.id = null
      form.name = ''
      form.categoryId = null
      form.price = 0
      form.image = ''
      form.description = ''
      form.status = 1
      if (formRef.value) {
        formRef.value.resetFields()
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
      queryParams.categoryId = null
      queryParams.status = null
      queryParams.page = 1
      getList()
    }

    // 添加菜品
    const handleAdd = () => {
      resetForm()
      dialogTitle.value = '添加菜品'
      dialogVisible.value = true
    }

    // 编辑菜品
    const handleEdit = (row) => {
      resetForm()
      form.id = row.id
      form.name = row.name
      form.categoryId = row.categoryId
      form.price = row.price
      form.image = row.image
      form.description = row.description
      form.status = row.status
      dialogTitle.value = '编辑菜品'
      dialogVisible.value = true
    }

    // 删除菜品
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除菜品"${row.name}"吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await deleteDish(row.id)
        if (response.code === 1) {
          ElMessage.success('删除成功')
          getList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    // 状态切换
    const handleStatusChange = async (row) => {
      try {
        const response = await toggleDishStatus(row.id, row.status)
        if (response.code === 1) {
          ElMessage.success(row.status ? '启用成功' : '停用成功')
        } else {
          // 恢复原状态
          row.status = row.status ? 0 : 1
        }
      } catch (error) {
        ElMessage.error('状态更新失败')
        // 恢复原状态
        row.status = row.status ? 0 : 1
      }
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
          response = await updateDish(form)
        } else {
          response = await addDish(form)
        }

        if (response.code === 1) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          getList()
        }
      } catch (error) {
        ElMessage.error(form.id ? '更新失败' : '添加失败')
      } finally {
        submitLoading.value = false
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
      getCategoryOptions()
    })

    return {
      formRef,
      loading,
      submitLoading,
      dialogVisible,
      dialogTitle,
      dishList,
      categoryOptions,
      total,
      queryParams,
      form,
      rules,
      getList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleDelete,
      handleStatusChange,
      handleSubmit,
      handleDialogClose,
      handleSizeChange,
      handleCurrentChange,
      formatDate
    }
  }
}
</script>

<style lang="scss" scoped>
.dish-container {
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

.dish-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
}

.dish-name {
  .name {
    font-weight: 500;
    color: $text-primary;
    display: block;
    margin-bottom: 4px;
  }

  .description {
    font-size: 12px;
    color: $text-secondary;
    margin: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.price {
  font-weight: 600;
  color: $danger-color;
  font-size: 16px;
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

:deep(.el-switch) {
  .el-switch__label {
    font-size: 12px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .search-bar {
    .el-form {
      .el-form-item {
        margin-bottom: 15px;

        .el-input,
        .el-select {
          width: 100% !important;
        }
      }
    }
  }

  .dish-list {
    :deep(.el-table) {
      .el-table__header,
      .el-table__body {
        min-width: 800px;
      }
    }
  }
}
</style>
