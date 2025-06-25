import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: null,
    roles: []
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userName: (state) => state.userInfo?.name || '',
    userRole: (state) => state.userInfo?.role || ''
  },

  actions: {
    // 登录
    async login(loginForm) {
      try {
        // 调用真实API
        const response = await login(loginForm)
        if (response.code === 1) {
          const { token, user } = response.data
          this.token = token
          this.userInfo = user
          setToken(token)
          return Promise.resolve(response)
        } else {
          return Promise.reject(new Error(response.msg || '登录失败'))
        }
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const response = await getUserInfo()
        if (response.code === 1) {
          this.userInfo = response.data
          this.roles = [response.data.role]
          return response.data
        } else {
          throw new Error(response.msg || '获取用户信息失败')
        }
      } catch (error) {
        this.logout()
        throw error
      }
    },

    // 登出
    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('登出请求失败:', error)
      } finally {
        this.token = ''
        this.userInfo = null
        this.roles = []
        removeToken()
      }
    },

    // 重置状态
    resetState() {
      this.token = ''
      this.userInfo = null
      this.roles = []
      removeToken()
    }
  }
})
