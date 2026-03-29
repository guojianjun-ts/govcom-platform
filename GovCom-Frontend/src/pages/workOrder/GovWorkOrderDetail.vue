<!-- src/pages/workOrder/GovWorkOrderDetail.vue -->
<template>
  <div class="detail-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <a-skeleton active v-if="loading" :paragraph="{ rows: 8 }" />

    <div v-else-if="order">
      <a-card class="detail-card">
        <template #title>
          <div class="card-title">
            <span>政务服务申请详情</span>
            <a-tag :color="getStatusColor(order.status)">{{ order.statusText }}</a-tag>
          </div>
        </template>

        <!-- 基本信息 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <a-descriptions :column="1">
            <a-descriptions-item label="申请单号">{{ order.applicationNo }}</a-descriptions-item>
            <a-descriptions-item label="服务名称">{{ order.serviceName }}</a-descriptions-item>
            <a-descriptions-item label="服务分类" v-if="order.categoryName">{{ order.categoryName }}</a-descriptions-item>
            <a-descriptions-item label="申请人">{{ order.applicantName }}</a-descriptions-item>
            <a-descriptions-item label="联系电话">{{ order.applicantPhone }}</a-descriptions-item>
            <a-descriptions-item label="办理地区">{{ order.region }}</a-descriptions-item>
            <a-descriptions-item label="提交时间">{{ formatTime(order.submitTime) }}</a-descriptions-item>
          </a-descriptions>
        </div>

        <!-- 处理按钮 -->
        <div class="action-buttons" v-if="order.status === 1 || order.status === 2">
          <a-button type="primary" @click="handleProcess" block>
            {{ order.status === 1 ? '受理工单' : '处理工单' }}
          </a-button>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { getGovWorkOrderDetailUsingGet } from '@/api/workOrderController'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<any>(null)

const fetchDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getGovWorkOrderDetailUsingGet({ id })
    if (res.code === 0) {
      order.value = res.data
    } else {
      message.error(res.message || '获取详情失败')
    }
  } catch (error) {
    message.error('获取详情失败')
  } finally {
    loading.value = false
  }
}

const getStatusColor = (status: number) => {
  const colors: Record<number, string> = {
    1: 'orange',
    2: 'blue',
    3: 'green',
    4: 'red'
  }
  return colors[status] || 'default'
}

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const handleProcess = () => {
  router.push(`/workOrder/gov/process/${order.value.id}`)
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchDetail(id)
  } else {
    message.error('工单ID不存在')
    router.back()
  }
})
</script>

<style scoped>
.detail-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.detail-card {
  border-radius: 12px;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.info-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.info-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.info-section h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.action-buttons {
  margin-top: 24px;
}
</style>
