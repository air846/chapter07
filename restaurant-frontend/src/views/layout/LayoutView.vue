<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <el-icon size="32" color="#409EFF">
            <Restaurant />
          </el-icon>
          <span v-show="!isCollapsed" class="logo-text">餐饮管理</span>
        </div>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapsed"
        :unique-opened="true"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>控制台</template>
        </el-menu-item>
        
        <el-sub-menu index="dish">
          <template #title>
            <el-icon><Food /></el-icon>
            <span>菜品管理</span>
          </template>
          <el-menu-item index="/category">
            <el-icon><Collection /></el-icon>
            <template #title>菜品分类</template>
          </el-menu-item>
          <el-menu-item index="/dish">
            <el-icon><Food /></el-icon>
            <template #title>菜品列表</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/order">
          <el-icon><List /></el-icon>
          <template #title>订单管理</template>
        </el-menu-item>
        
        <el-menu-item index="/inventory">
          <el-icon><Box /></el-icon>
          <template #title>库存管理</template>
        </el-menu-item>
        
        <el-menu-item index="/employee">
          <el-icon><User /></el-icon>
          <template #title>员工管理</template>
        </el-menu-item>
        
        <el-menu-item index="/statistics">
          <el-icon><TrendCharts /></el-icon>
          <template #title>统计分析</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <div class="header">
        <div class="header-left">
          <el-button
            type="text"
            @click="toggleSidebar"
            class="collapse-btn"
          >
            <el-icon size="20">
              <Expand v-if="isCollapsed" />
              <Fold v-else />
            </el-icon>
          </el-button>
          
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
              v-for="item in breadcrumbs"
              :key="item.path"
              :to="item.path"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar size="small" :src="userAvatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ userStore.userName || '管理员' }}</span>
              <el-icon class="arrow-down"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人信息
                </el-dropdown-item>
                <el-dropdown-item command="password">
                  <el-icon><Lock /></el-icon>
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <user-info-dialog v-model="showProfile" />
      <password-dialog v-model="showPwd" />

      <!-- 页面内容 -->
      <div class="content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Food as Restaurant,
  Odometer,
  Food,
  Collection,
  List,
  Box,
  User,
  TrendCharts,
  Expand,
  Fold,
  ArrowDown,
  Lock,
  SwitchButton
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import UserInfoDialog from '@/components/user/UserInfoDialog.vue'
import PasswordDialog from '@/components/user/PasswordDialog.vue'

export default {
  name: 'LayoutView',
  components: {
    Restaurant,
    Odometer,
    Food,
    Collection,
    List,
    Box,
    User,
    TrendCharts,
    Expand,
    Fold,
    ArrowDown,
    Lock,
    SwitchButton,
    UserInfoDialog,
    PasswordDialog
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    
    const isCollapsed = ref(false)
    const userAvatar = ref('')
    const showProfile = ref(false)
    const showPwd = ref(false)

    // 当前激活的菜单
    const activeMenu = computed(() => {
      return route.path
    })

    // 面包屑导航
    const breadcrumbs = computed(() => {
      const matched = route.matched.filter(item => item.meta && item.meta.title)
      const breadcrumbList = []
      
      // 根据路由路径生成面包屑
      switch (route.path) {
        case '/dashboard':
          breadcrumbList.push({ title: '控制台', path: '/dashboard' })
          break
        case '/category':
          breadcrumbList.push({ title: '菜品管理', path: '' })
          breadcrumbList.push({ title: '菜品分类', path: '/category' })
          break
        case '/dish':
          breadcrumbList.push({ title: '菜品管理', path: '' })
          breadcrumbList.push({ title: '菜品列表', path: '/dish' })
          break
        case '/order':
          breadcrumbList.push({ title: '订单管理', path: '/order' })
          break
        case '/inventory':
          breadcrumbList.push({ title: '库存管理', path: '/inventory' })
          break
        case '/employee':
          breadcrumbList.push({ title: '员工管理', path: '/employee' })
          break
        case '/statistics':
          breadcrumbList.push({ title: '统计分析', path: '/statistics' })
          break
        default:
          breadcrumbList.push({ title: '控制台', path: '/dashboard' })
      }
      
      return breadcrumbList
    })

    // 切换侧边栏
    const toggleSidebar = () => {
      isCollapsed.value = !isCollapsed.value
    }

    // 处理用户下拉菜单命令
    const handleCommand = async (command) => {
      switch (command) {
        case 'profile':
          showProfile.value = true
          break
        case 'password':
          showPwd.value = true
          break
        case 'logout':
          try {
            await ElMessageBox.confirm(
              '确定要退出登录吗？',
              '提示',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            
            await userStore.logout()
            ElMessage.success('退出登录成功')
            router.push('/login')
          } catch (error) {
            if (error !== 'cancel') {
              ElMessage.error('退出登录失败')
            }
          }
          break
      }
    }

    return {
      isCollapsed,
      activeMenu,
      breadcrumbs,
      userAvatar,
      showProfile,
      showPwd,
      UserInfoDialog,
      PasswordDialog,
      userStore,
      toggleSidebar,
      handleCommand
    }
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  height: 100vh;
  width: 100vw;
}

.sidebar {
  width: $sidebar-width;
  background: #001529;
  transition: width 0.3s ease;
  overflow: hidden;
  
  &.collapsed {
    width: $sidebar-collapsed-width;
  }
  
  .sidebar-header {
    height: $header-height;
    display: flex;
    align-items: center;
    padding: 0 20px;
    border-bottom: 1px solid #1f2937;
    
    .logo {
      display: flex;
      align-items: center;
      color: white;
      
      .logo-text {
        margin-left: 12px;
        font-size: 18px;
        font-weight: 600;
        white-space: nowrap;
      }
    }
  }
  
  .sidebar-menu {
    border: none;
    background: transparent;
    
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      color: rgba(255, 255, 255, 0.8);
      
      &:hover {
        background-color: #1f2937 !important;
        color: #409EFF !important;
      }
    }
    
    :deep(.el-menu-item.is-active) {
      background-color: #409EFF !important;
      color: white !important;
    }
    
    :deep(.el-sub-menu .el-menu-item) {
      background-color: #0f1419 !important;
      
      &:hover {
        background-color: #1f2937 !important;
      }
      
      &.is-active {
        background-color: #409EFF !important;
      }
    }
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: $header-height;
  background: white;
  border-bottom: 1px solid $border-base;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: $box-shadow-base;
  
  .header-left {
    display: flex;
    align-items: center;
    
    .collapse-btn {
      margin-right: 20px;
      padding: 8px;
      
      &:hover {
        background-color: #f5f5f5;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 8px 12px;
      border-radius: 4px;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: #f5f5f5;
      }
      
      .username {
        margin: 0 8px;
        font-size: 14px;
        color: $text-primary;
      }
      
      .arrow-down {
        font-size: 12px;
        color: $text-secondary;
      }
    }
  }
}

.content {
  flex: 1;
  overflow-y: auto;
  background: $bg-color;
}

// 页面切换动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
