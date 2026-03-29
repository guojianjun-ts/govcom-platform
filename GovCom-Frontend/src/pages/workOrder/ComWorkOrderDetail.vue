<!-- src/pages/workOrder/ComWorkOrderDetail.vue -->
<template>
  <div class="detail-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <a-skeleton active v-if="loading" :paragraph="{ rows: 8 }" />

    <div v-else-if="order">
      <a-card class="detail-card">
        <template #title>
          <div class="card-title">
            <span>工单详情</span>
            <a-tag :color="getStatusColor(order.status)">{{ order.statusText }}</a-tag>
          </div>
        </template>

        <!-- 基本信息 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <a-descriptions :column="1">
            <a-descriptions-item label="工单编号">{{ order.orderNo }}</a-descriptions-item>
            <a-descriptions-item label="工单类型">{{ order.typeName }}</a-descriptions-item>
            <a-descriptions-item label="子类型" v-if="order.subTypeName">{{ order.subTypeName }}</a-descriptions-item>
            <a-descriptions-item label="申请人">{{ order.applicantName }}</a-descriptions-item>
            <a-descriptions-item label="联系电话">{{ order.applicantPhone }}</a-descriptions-item>
            <a-descriptions-item label="提交时间">{{ formatTime(order.createTime) }}</a-descriptions-item>
          </a-descriptions>
        </div>

        <!-- 申请内容 -->
        <div class="info-section">
          <h3>申请内容</h3>
          <div class="content-box">
            <h4>{{ order.title }}</h4>
            <p class="content-text">{{ order.content }}</p>
          </div>
        </div>

        <!-- 表单数据（各类型特有字段） -->
        <div class="info-section" v-if="order.formData && Object.keys(order.formData).length > 0">
          <h3>详细信息</h3>
          <a-descriptions :column="1">
            <a-descriptions-item
              v-for="(value, key) in order.formData"
              :key="key"
              :label="getFieldLabel(key)"
            >
              {{ value }}
            </a-descriptions-item>
          </a-descriptions>
        </div>

        <!-- 附件 -->
        <div class="info-section" v-if="order.attachments && order.attachments.length > 0">
          <h3>附件材料</h3>
          <div class="attachments">
            <div v-for="(url, index) in order.attachments" :key="index" class="attachment-item">
              <img :src="url" @click="previewImage(url)" />
            </div>
          </div>
        </div>

        <!-- 处理结果 -->
        <div class="info-section" v-if="order.status >= 3">
          <h3>处理结果</h3>
          <div class="result-box">
            <p>{{ order.result || '暂无处理结果' }}</p>
            <div v-if="order.resultFiles && order.resultFiles.length > 0" class="result-files">
              <div v-for="(file, index) in order.resultFiles" :key="index">
                <a :href="file" target="_blank">📎 结果文件{{ index + 1 }}</a>
              </div>
            </div>
          </div>
        </div>

        <!-- 处理按钮 -->
        <div class="action-buttons" v-if="order.status === 1 || order.status === 2">
          <a-button type="primary" @click="handleProcess" block>
            {{ order.status === 1 ? '受理工单' : '处理工单' }}
          </a-button>
        </div>
      </a-card>
    </div>

    <!-- 图片预览 -->
    <a-modal
      v-model:open="previewVisible"
      :footer="null"
      @cancel="previewVisible = false"
    >
      <img :src="previewUrl" style="width: 100%;" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { getWorkOrderDetailUsingGet } from '@/api/workOrderController'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<any>(null)
const previewVisible = ref(false)
const previewUrl = ref('')

const fetchDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getWorkOrderDetailUsingGet({ id })
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

const getFieldLabel = (key: string) => {
  const labels: Record<string, string> = {
    studentName: '学生姓名',
    school: '就读学校',
    purpose: '用途说明',
    description: '详细描述',
    appointmentTime: '预约时间',
    address: '具体地址',
    location: '发生地点'
  }
  return labels[key] || key
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

const previewImage = (url: string) => {
  previewUrl.value = url
  previewVisible.value = true
}

const handleProcess = () => {
  router.push(`/workOrder/process/${order.value.id}`)
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

.content-box {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
}

.content-box h4 {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
}

.content-text {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
}

.attachments {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
}

.attachment-item {
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  aspect-ratio: 1;
}

.attachment-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.attachment-item:hover img {
  transform: scale(1.05);
}

.result-box {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
}

.result-files {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.action-buttons {
  margin-top: 24px;
}
</style>
