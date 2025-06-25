<template>
  <el-dialog v-model="visible" title="修改密码" width="420px" @close="reset">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" placeholder="请输入旧密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入新密码" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { changePassword } from '@/api/user'
import { useUserStore } from '@/store/user'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['update:modelValue'])
const visible = computed({
  get:()=>props.modelValue,
  set:v=>emit('update:modelValue',v)
})
const userStore = useUserStore()
const router = useRouter()
const { userInfo } = storeToRefs(userStore)
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ oldPassword:'', newPassword:'', confirmPassword:'' })
const rules = {
  oldPassword:[{required:true,message:'请输入旧密码',trigger:'blur'}],
  newPassword:[{required:true,min:6,message:'请输入至少6位新密码',trigger:'blur'}],
  confirmPassword:[{validator:(rule,value,cb)=>{ if(value!==form.newPassword){cb(new Error('两次密码不一致'))} else cb()},trigger:'blur'}]
}
const reset=()=>{form.oldPassword='';form.newPassword='';form.confirmPassword=''}
const submit=async()=>{
  if(!formRef.value)return;
  const valid=await formRef.value.validate().catch(()=>false)
  if(!valid)return;
  try{
    loading.value=true
    const uid = userInfo.value?.id
    if(!uid){ ElMessage.error('无法获取用户ID，请重新登录'); return; }
    await changePassword({ userId:uid, oldPassword:form.oldPassword, newPassword:form.newPassword })
    ElMessage.success('修改成功，请重新登录')
    visible.value=false
    userStore.resetState()
    router.push('/login')
  }catch(e){ ElMessage.error(e.msg||'修改失败') }
  finally{loading.value=false}
}
</script> 