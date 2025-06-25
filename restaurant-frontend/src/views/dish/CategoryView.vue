<template>
  <div class="category-container">
    <div class="page-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <h2>菜品分类管理</h2>
        </div>
        <div class="toolbar-right">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加分类
          </el-button>
        </div>
      </div>

      <!-- 分类列表 -->
      <div class="category-list">
        <el-table
          :data="categoryList"
          v-loading="loading"
          style="width: 100%"
          row-key="id"
        >
          <el-table-column prop="name" label="分类名称" min-width="200">
            <template #default="scope">
              <div class="category-name">
                <el-icon size="16" color="#409EFF">
                  <Collection />
                </el-icon>
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="sort" label="排序" width="100" align="center">
            <template #default="scope">
              <el-tag type="info">{{ scope.row.sort }}</el-tag>
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
      width="500px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入分类名称"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="form.sort"
            :min="0"
            :max="999"
            placeholder="请输入排序值"
            style="width: 100%"
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
  Plus,
  Collection,
  Edit,
  Delete
} from '@element-plus/icons-vue'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/category'

export default {
  name: 'CategoryView',
  components: {
    Plus,
    Collection,
    Edit,
    Delete
  },
  setup() {
    const formRef = ref(null)
    const loading = ref(false)
    const submitLoading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const categoryList = ref([])
    const total = ref(0)

    const queryParams = reactive({
      page: 1,
      pageSize: 10
    })

    const form = reactive({
      id: null,
      name: '',
      sort: 0
    })

    const rules = {
      name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { min: 2, max: 20, message: '分类名称长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      sort: [
        { required: true, message: '请输入排序值', trigger: 'blur' },
        { type: 'number', min: 0, max: 999, message: '排序值范围为 0-999', trigger: 'blur' }
      ]
    }

    // 获取分类列表
    const getList = async () => {
      try {
        loading.value = true
        const response = await getCategoryList(queryParams)
        if (response.code === 1) {
          categoryList.value = response.data.content || []
          total.value = response.data.totalElements || 0
        }
      } catch (error) {
        ElMessage.error('获取分类列表失败')
      } finally {
        loading.value = false
      }
    }

    // 重置表单
    const resetForm = () => {
      form.id = null
      form.name = ''
      form.sort = 0
      if (formRef.value) {
        formRef.value.resetFields()
      }
    }

    // 添加分类
    const handleAdd = () => {
      resetForm()
      dialogTitle.value = '添加分类'
      dialogVisible.value = true
    }

    // 编辑分类
    const handleEdit = (row) => {
      resetForm()
      form.id = row.id
      form.name = row.name
      form.sort = row.sort
      dialogTitle.value = '编辑分类'
      dialogVisible.value = true
    }

    // 删除分类
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除分类"${row.name}"吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await deleteCategory(row.id)
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

    // 提交表单
    const handleSubmit = async () => {
      if (!formRef.value) return

      try {
        const valid = await formRef.value.validate()
        if (!valid) return

        submitLoading.value = true

        let response
        if (form.id) {
          response = await updateCategory(form)
        } else {
          response = await addCategory(form)
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
    })

    return {
      formRef,
      loading,
      submitLoading,
      dialogVisible,
      dialogTitle,
      categoryList,
      total,
      queryParams,
      form,
      rules,
      getList,
      handleAdd,
      handleEdit,
      handleDelete,
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
.category-container {
  height: 100%;
  background: $bg-color;
}

.category-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
}

.category-name {
  display: flex;
  align-items: center;
  gap: 8px;
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
</style>
