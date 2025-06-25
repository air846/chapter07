<template>
  <div class="login-container">
    <div class="login-background">
      <div class="bg-overlay"></div>
    </div>
    
    <div class="login-content">
      <div class="login-form-container">
        <div class="login-header">
          <div class="logo">
            <el-icon size="40" color="#409EFF">
              <Food />
            </el-icon>
          </div>
          <h1 class="title">
            <SplitText 
              text="餐饮管理系统" 
              :delay="100"
              :auto-play="true"
              @animation-complete="onTitleAnimationComplete"
            />
          </h1>
          <p class="subtitle" :class="{ 'fade-in': showSubtitle }">
            Restaurant Management System
          </p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          :class="{ 'form-visible': showForm }"
          size="large"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              clearable
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Food } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import SplitText from '@/components/common/SplitText.vue'

export default {
  name: 'LoginView',
  components: {
    SplitText,
    Food
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    const loginFormRef = ref(null)
    const loading = ref(false)
    const showSubtitle = ref(false)
    const showForm = ref(false)

    const loginForm = reactive({
      username: '',
      password: ''
    })

    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ]
    }

    const onTitleAnimationComplete = () => {
      showSubtitle.value = true
      setTimeout(() => {
        showForm.value = true
      }, 300)
    }

    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      try {
        const valid = await loginFormRef.value.validate()
        if (!valid) return

        loading.value = true
        
        await userStore.login(loginForm)
        
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      // 如果已经登录，直接跳转到首页
      if (userStore.token) {
        router.push('/dashboard')
      }
    })

    return {
      loginFormRef,
      loginForm,
      loginRules,
      loading,
      showSubtitle,
      showForm,
      handleLogin,
      onTitleAnimationComplete
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
  
  .bg-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
  }
}

.login-content {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 400px;
  padding: 0 20px;
}

.login-form-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    margin-bottom: 20px;
  }
  
  .title {
    font-size: 28px;
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 10px;
    min-height: 40px;
  }
  
  .subtitle {
    color: #7f8c8d;
    font-size: 14px;
    opacity: 0;
    transform: translateY(10px);
    transition: all 0.5s ease;
    
    &.fade-in {
      opacity: 1;
      transform: translateY(0);
    }
  }
}

.login-form {
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.5s ease;
  
  &.form-visible {
    opacity: 1;
    transform: translateY(0);
  }
  
  .el-form-item {
    margin-bottom: 24px;
  }
  
  .login-btn {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 500;
    border-radius: 8px;
    background: linear-gradient(135deg, #409EFF 0%, #5dade2 100%);
    border: none;
    
    &:hover {
      background: linear-gradient(135deg, #337ecc 0%, #4a90c2 100%);
    }
  }
}

// 响应式设计
@media (max-width: 480px) {
  .login-form-container {
    padding: 30px 20px;
    margin: 0 10px;
  }
  
  .login-header .title {
    font-size: 24px;
  }
}
</style>
