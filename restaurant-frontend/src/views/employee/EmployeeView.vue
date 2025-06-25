<template>
  <div class="employee-container">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">员工管理</h1>
        <p class="page-description">管理系统用户和员工信息</p>
      </div>

      <!-- 搜索和操作栏 -->
      <div class="search-bar">
        <div class="search-form">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 200px; margin-right: 16px;"
          />
          <el-input
            v-model="searchForm.name"
            placeholder="请输入姓名"
            clearable
            style="width: 200px; margin-right: 16px;"
          />
          <el-select
            v-model="searchForm.role"
            placeholder="请选择角色"
            clearable
            style="width: 150px; margin-right: 16px;"
          >
            <el-option label="管理员" value="ADMIN" />
            <el-option label="经理" value="MANAGER" />
            <el-option label="员工" value="STAFF" />
          </el-select>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
        <div class="action-buttons">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增员工
          </el-button>
        </div>
      </div>

      <!-- 员工列表 -->
      <div class="table-container">
        <el-table
          :data="employeeList"
          v-loading="loading"
          style="width: 100%"
          row-key="id"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="role" label="角色" width="100">
            <template #default="scope">
              <el-tag :type="getRoleType(scope.row.role)">
                {{ getRoleText(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                {{ scope.row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                :type="scope.row.status === 1 ? 'warning' : 'success'"
                size="small"
                @click="handleToggleStatus(scope.row)"
              >
                {{ scope.row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- 新增/编辑员工对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="employeeFormRef"
        :model="employeeForm"
        :rules="employeeRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="employeeForm.username"
            :disabled="isEdit"
            placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input
            v-model="employeeForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="employeeForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="employeeForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="employeeForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="employeeForm.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="经理" value="MANAGER" />
            <el-option label="员工" value="STAFF" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="employeeForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, addUser, updateUser, deleteUser, toggleUserStatus } from '@/api/user'
import {
  Search,
  Refresh,
  Plus
} from '@element-plus/icons-vue'

export default {
  name: 'EmployeeView',
  components: {
    Search,
    Refresh,
    Plus
  },
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const employeeList = ref([])
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const employeeFormRef = ref(null)

    // 搜索表单
    const searchForm = reactive({
      username: '',
      name: '',
      role: ''
    })

    // 分页数据
    const pagination = reactive({
      page: 1,
      pageSize: 10,
      total: 0
    })

    // 员工表单
    const employeeForm = reactive({
      id: null,
      username: '',
      password: '',
      name: '',
      phone: '',
      email: '',
      role: 'STAFF',
      status: 1
    })

    // 表单验证规则
    const employeeRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
        { max: 50, message: '姓名长度不能超过 50 个字符', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
        { max: 100, message: '邮箱长度不能超过 100 个字符', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ]
    }

    // 计算属性
    const dialogTitle = ref('')

    // 获取员工列表
    const getEmployeeList = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.page,
          pageSize: pagination.pageSize,
          ...searchForm
        }

        // 过滤空值
        Object.keys(params).forEach(key => {
          if (params[key] === '' || params[key] === null || params[key] === undefined) {
            delete params[key]
          }
        })

        const response = await getUserList(params)
        if (response.code === 1) {
          employeeList.value = response.data.content || []
          pagination.total = response.data.totalElements || 0
        } else {
          ElMessage.error(response.msg || '获取员工列表失败')
        }
      } catch (error) {
        console.error('获取员工列表失败:', error)
        ElMessage.error('获取员工列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getEmployeeList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
      })
      pagination.page = 1
      getEmployeeList()
    }

    // 新增员工
    const handleAdd = () => {
      isEdit.value = false
      dialogTitle.value = '新增员工'
      resetForm()
      dialogVisible.value = true
    }

    // 编辑员工
    const handleEdit = (row) => {
      isEdit.value = true
      dialogTitle.value = '编辑员工'
      Object.keys(employeeForm).forEach(key => {
        if (key in row) {
          employeeForm[key] = row[key]
        }
      })
      dialogVisible.value = true
    }

    // 删除员工
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除员工 "${row.name}" 吗？`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await deleteUser(row.id)
        if (response.code === 1) {
          ElMessage.success('删除成功')
          getEmployeeList()
        } else {
          ElMessage.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除员工失败:', error)
          ElMessage.error('删除失败')
        }
      }
    }

    // 切换员工状态
    const handleToggleStatus = async (row) => {
      const newStatus = row.status === 1 ? 0 : 1
      const statusText = newStatus === 1 ? '启用' : '禁用'

      try {
        await ElMessageBox.confirm(
          `确定要${statusText}员工 "${row.name}" 吗？`,
          '确认操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await toggleUserStatus(row.id, newStatus)
        if (response.code === 1) {
          ElMessage.success(`${statusText}成功`)
          getEmployeeList()
        } else {
          ElMessage.error(response.msg || `${statusText}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('切换状态失败:', error)
          ElMessage.error(`${statusText}失败`)
        }
      }
    }

    // 提交表单
    const handleSubmit = async () => {
      if (!employeeFormRef.value) return

      try {
        await employeeFormRef.value.validate()
        submitLoading.value = true

        const formData = { ...employeeForm }
        let response

        if (isEdit.value) {
          response = await updateUser(formData)
        } else {
          response = await addUser(formData)
        }

        if (response.code === 1) {
          ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
          dialogVisible.value = false
          getEmployeeList()
        } else {
          ElMessage.error(response.msg || (isEdit.value ? '更新失败' : '新增失败'))
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
      } finally {
        submitLoading.value = false
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      resetForm()
    }

    // 重置表单
    const resetForm = () => {
      Object.keys(employeeForm).forEach(key => {
        if (key === 'role') {
          employeeForm[key] = 'STAFF'
        } else if (key === 'status') {
          employeeForm[key] = 1
        } else {
          employeeForm[key] = key === 'id' ? null : ''
        }
      })
      if (employeeFormRef.value) {
        employeeFormRef.value.clearValidate()
      }
    }

    // 分页处理
    const handleSizeChange = (size) => {
      pagination.pageSize = size
      pagination.page = 1
      getEmployeeList()
    }

    const handleCurrentChange = (page) => {
      pagination.page = page
      getEmployeeList()
    }

    // 工具方法
    const getRoleType = (role) => {
      const roleMap = {
        'ADMIN': 'danger',
        'MANAGER': 'warning',
        'STAFF': 'info'
      }
      return roleMap[role] || 'info'
    }

    const getRoleText = (role) => {
      const roleMap = {
        'ADMIN': '管理员',
        'MANAGER': '经理',
        'STAFF': '员工'
      }
      return roleMap[role] || '未知'
    }

    const formatDateTime = (dateTime) => {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }

    // 生命周期
    onMounted(() => {
      getEmployeeList()
    })

    return {
      loading,
      submitLoading,
      employeeList,
      searchForm,
      pagination,
      dialogVisible,
      dialogTitle,
      isEdit,
      employeeForm,
      employeeRules,
      employeeFormRef,
      getEmployeeList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleDelete,
      handleToggleStatus,
      handleSubmit,
      handleDialogClose,
      handleSizeChange,
      handleCurrentChange,
      getRoleType,
      getRoleText,
      formatDateTime
    }
  }
}
</script>

<style lang="scss" scoped>
.employee-container {
  height: 100%;
  background: $bg-color;
}

.page-header {
  margin-bottom: 24px;

  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 8px;
  }

  .page-description {
    color: $text-secondary;
    font-size: 14px;
  }
}

.search-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: $box-shadow-base;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .search-form {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 16px;
  }

  .action-buttons {
    display: flex;
    gap: 12px;
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: $box-shadow-base;
  overflow: hidden;

  .el-table {
    border-radius: 8px 8px 0 0;
  }
}

.pagination-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  background: white;
  border-top: 1px solid $border-lighter;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

// 响应式设计
@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;

    .search-form {
      justify-content: center;
    }

    .action-buttons {
      justify-content: center;
    }
  }

  .table-container {
    overflow-x: auto;
  }
}
</style>
