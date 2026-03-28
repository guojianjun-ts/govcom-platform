<!-- src/pages/life/LifeMyOrders.vue -->
<template>
  <div class="orders-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <h2 class="page-title">我的申请</h2>

    <!-- 状态筛选标签 -->
    <div class="status-tabs">
      <a-tabs v-model:activeKey="statusFilter" @change="handleStatusChange">
        <a-tab-pane key="all" tab="全部" />
        <a-tab-pane key="1" tab="待受理" />
        <a-tab-pane key="2" tab="处理中" />
        <a-tab-pane key="3" tab="已完成" />
        <a-tab-pane key="4" tab="已驳回" />
      </a-tabs>
    </div>

    <!-- 申请列表 -->
    <a-list
      :data-source="orderList"
      :loading="loading"
      :pagination="pagination"
      item-layout="vertical"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card hoverable class="order-card" @click="goToDetail(item.id)">
            <div class="order-header">
              <div>
                <span class="order-type">{{ item.typeName }}</span>
                <span class="order-subtype" v-if="item.subTypeName"> · {{ item.subTypeName }}</span>
              </div>
              <a-tag :color="getStatusColor(item.status)">{{ item.statusText }}</a-tag>
            </div>

            <h3 class="order-title">{{ item.title }}</h3>
            <p class="order-content">{{ item.content }}</p>

            <div class="order-meta">
              <span><file-text-outlined /> 单号：{{ item.orderNo }}</span>
              <span><clock-circle-outlined /> {{ formatTime(item.createTime) }}</span>
            </div>
          </a-card>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  LeftOutlined,
  FileTextOutlined,
  ClockCircleOutlined
} from '@ant-design/icons-vue'
import { getMyOrdersUsingPost } from '@/api/lifeController'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const statusFilter = ref('all')
const orderList = ref<any[]>([])

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.value.current = page
    fetchOrders()
  }
})

// 获取申请列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    }
    if (statusFilter.value !== 'all') {
      params.status = Number(statusFilter.value)
    }

    const res = await getMyOrdersUsingPost(params)
    if (res.code === 0) {
      orderList.value = res.data || []
      pagination.value.total = orderList.value.length * 5
    }
  } catch (error) {
    message.error('获取申请列表失败')
  } finally {
    loading.value = false
  }
}

// 切换状态筛选
const handleStatusChange = () => {
  pagination.value.current = 1
  fetchOrders()
}

// 获取状态颜色
const getStatusColor = (status: number) => {
  const colors: Record<number, string> = {
    1: 'orange',
    2: 'blue',
    3: 'green',
    4: 'red'
  }
  return colors[status] || 'default'
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 跳转到详情
const goToDetail = (id: number) => {
  router.push(`/life/order/detail/${id}`)
}

// 返回
const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.orders-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 24px;
}

.status-tabs {
  margin-bottom: 24px;
}

.order-card {
  cursor: pointer;
  transition: all 0.3s;
}

.order-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.order-type {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.order-subtype {
  color: #666;
  font-size: 14px;
}

.order-title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.order-content {
  color: #666;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.order-meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.order-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
