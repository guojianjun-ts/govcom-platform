<!-- src/pages/workorder/WorkOrderIndex.vue -->
<template>
  <div></div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()

onMounted(() => {
  const userType = userStore.userInfo?.userType

  if (userType === 2) {
    router.replace('/workorder/gov')
  } else if (userType === 3) {
    router.replace('/workorder/community')
  } else if (userType === 4) {
    router.replace('/workorder/admin')
  } else {
    router.replace('/')
  }
})
</script>
