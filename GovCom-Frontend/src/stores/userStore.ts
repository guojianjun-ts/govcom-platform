// src/stores/userStore.ts
import { ref } from 'vue'

// 立即执行，从 localStorage 加载数据
const loadInitialUserInfo = () => {
  const cached = localStorage.getItem('userInfo')
  if (cached) {
    try {
      return JSON.parse(cached)
    } catch (e) {
      console.error('解析用户信息失败', e)
    }
  }
  return null
}

const userInfo = ref<any>(loadInitialUserInfo())

console.log('userStore 初始化，用户信息:', userInfo.value)

export function useUserStore() {
  const loadUserInfo = () => {
    const cached = localStorage.getItem('userInfo')
    if (cached) {
      try {
        userInfo.value = JSON.parse(cached)
        console.log('loadUserInfo 加载:', userInfo.value)
      } catch (e) {
        console.error('解析用户信息失败', e)
      }
    }
    return userInfo.value
  }

  const updateUserInfo = (newInfo: any) => {
    const updated = {
      ...userInfo.value,
      ...newInfo
    }
    userInfo.value = updated
    localStorage.setItem('userInfo', JSON.stringify(updated))
    console.log('用户信息已更新:', updated)
    return updated
  }

  const clearUserInfo = () => {
    userInfo.value = null
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
    console.log('用户信息已清除')
  }

  // 确保每次调用都返回最新的值
  return {
    userInfo,
    loadUserInfo,
    updateUserInfo,
    clearUserInfo
  }
}
