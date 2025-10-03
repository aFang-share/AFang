import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface UserInfo {
  id: string
  username: string
  avatar?: string
  email?: string
}

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const isLoggedIn = ref(false)

  // 登录
  const login = (user: UserInfo) => {
    userInfo.value = user
    isLoggedIn.value = true
    // 保存到 localStorage
    localStorage.setItem('userInfo', JSON.stringify(user))
  }

  // 退出登录
  const logout = () => {
    userInfo.value = null
    isLoggedIn.value = false
    localStorage.removeItem('userInfo')
  }

  // 初始化：从 localStorage 恢复用户信息
  const initUser = () => {
    const savedUser = localStorage.getItem('userInfo')
    if (savedUser) {
      try {
        userInfo.value = JSON.parse(savedUser)
        isLoggedIn.value = true
      } catch (e) {
        console.error('Failed to parse user info:', e)
      }
    }
  }

  return {
    userInfo,
    isLoggedIn,
    login,
    logout,
    initUser,
  }
})
