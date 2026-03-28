<!-- src/pages/workOrder/WorkOrderDashboard.vue -->
<template>
  <div></div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const router = useRouter()

onMounted(() => {
  // 直接从 localStorage 获取用户信息
  const userInfoStr = localStorage.getItem('userInfo')
  console.log('WorkOrderDashboard - localStorage:', userInfoStr)

  if (!userInfoStr) {
    message.error('用户未登录')
    router.push('/user/login')
    return
  }

  const userInfo = JSON.parse(userInfoStr)
  const userType = userInfo.userType
  console.log('当前用户类型:', userType)

  switch (userType) {
    case 2: // 政务人员
      router.replace('/workOrder/gov')
      break
    case 3: // 社区工作人员
      router.replace('/workOrder/community')
      break
    case 4: // 系统管理员
      router.replace('/workOrder/gov') // 管理员默认进政务工单
      break
    default:
      message.error('您无权访问工单处理模块')
      router.back()
  }
})
</script>
