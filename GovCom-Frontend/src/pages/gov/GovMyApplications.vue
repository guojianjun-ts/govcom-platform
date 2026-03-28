<!-- src/pages/gov/GovMyApplications.vue -->
<template>
  <div class="my-applications-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <h2 class="page-title">我的申请</h2>

    <a-list
      :data-source="applicationList"
      :loading="loading"
      item-layout="vertical"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card hoverable @click="goToDetail(item.id)">
            <div class="application-item">
              <div class="application-header">
                <h3>{{ item.serviceName }}</h3>
                <a-tag :color="getStatusColor(item.status)">{{ item.statusText }}</a-tag>
              </div>
              <div class="application-info">
                <p><clock-circle-outlined /> 申请时间：{{ formatDate(item.createTime) }}</p>
                <p><environment-outlined /> 办理地区：{{ item.region }}</p>
                <p><user-outlined /> 申请人：{{ item.applicantName }}</p>
                <p><phone-outlined /> 联系电话：{{ item.applicantPhone }}</p>
              </div>
              <div class="application-footer">
                <span class="application-no">申请单号：{{ item.applicationNo }}</span>
                <a-button type="link" size="small">查看详情</a-button>
              </div>
            </div>
          </a-card>
        </a-list-item>
      </template>
      <template v-if="applicationList.length === 0 && !loading">
        <div class="empty-state">
          <a-empty description="暂无申请记录" />
          <a-button type="primary" @click="goToGov">去办理服务</a-button>
        </div>
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
  ClockCircleOutlined,
  EnvironmentOutlined,
  UserOutlined,
  PhoneOutlined
} from '@ant-design/icons-vue'
import { getMyApplicationsUsingGet } from '@/api/govController'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const applicationList = ref<any[]>([])

const fetchMyApplications = async () => {
  loading.value = true
  try {
    const res = await getMyApplicationsUsingGet()
    if (res.code === 0) {
      applicationList.value = res.data || []
    } else {
      message.error(res.message || '获取申请列表失败')
    }
  } catch (error) {
    message.error('获取申请列表失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const goToDetail = (id: number) => {
  router.push(`/gov/application/detail/${id}`)
}

const goToGov = () => {
  router.push('/gov')
}

const getStatusColor = (status: number) => {
  const colors: Record<number, string> = {
    1: 'processing',  // 已提交
    2: 'warning',     // 审核中
    3: 'success',     // 已完成
    4: 'error'        // 已驳回
  }
  return colors[status] || 'default'
}

const formatDate = (dateStr: string) => {
  return dayjs(dateStr).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchMyApplications()
})
</script>

<style scoped>
.my-applications-page {
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
  font-weight: 500;
  margin-bottom: 30px;
  color: #333;
}
.application-item {
  padding: 16px;
}
.application-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.application-header h3 {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin: 0;
}
.application-info p {
  margin-bottom: 8px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 8px;
}
.application-footer {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.application-no {
  color: #999;
  font-size: 13px;
}
.empty-state {
  text-align: center;
  padding: 60px 0;
}
.empty-state .ant-btn {
  margin-top: 20px;
}
</style>
