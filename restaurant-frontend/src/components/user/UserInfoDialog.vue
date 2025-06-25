<template>
  <el-dialog v-model="visible" title="个人信息" width="420px">
    <el-descriptions :column="1" border v-if="userInfo">
      <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ userInfo.name }}</el-descriptions-item>
      <el-descriptions-item label="角色">{{ roleMap[userInfo.role] || userInfo.role }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ userInfo.phone }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
    </el-descriptions>
    <div v-else style="text-align:center; padding: 20px;">
      <el-icon class="is-loading"><Loading /></el-icon>
    </div>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/store/user'
import { Loading } from '@element-plus/icons-vue'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['update:modelValue'])
const visible = computed({
  get: () => props.modelValue,
  set: v => emit('update:modelValue', v)
})

const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)
const roleMap = { ADMIN: '管理员', MANAGER: '经理', STAFF: '员工' }

watch(visible, async (v) => {
  if (v && !userInfo.value) {
    await userStore.getUserInfo()
  }
})
</script> 