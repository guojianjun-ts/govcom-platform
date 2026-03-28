<template>
  <div class="service-detail-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <!-- 加载状态 -->
    <a-skeleton active v-if="loading" :paragraph="{ rows: 8 }" />

    <div v-else-if="service">
      <!-- 服务标题 -->
      <div class="service-header">
        <div class="service-icon" :style="{ backgroundColor: getRandomColor() }">
          <i :class="service.icon || 'fas fa-file-alt'"></i>
        </div>
        <div class="service-title">
          <h1>{{ service.serviceName }}</h1>
          <p class="brief">{{ service.briefDesc }}</p>
          <div class="stats">
            <span><eye-outlined /> {{ service.viewCount || 0 }} 次浏览</span>
            <span><file-done-outlined /> {{ service.applyCount || 0 }} 人申请</span>
          </div>
        </div>
      </div>

      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="info-card">
        <a-descriptions :column="1">
          <a-descriptions-item label="办理时限">
            {{ service.baseInfo?.timeLimit || '无' }}
          </a-descriptions-item>
          <a-descriptions-item label="费用说明">
            {{ service.baseInfo?.fee || '免费' }}
          </a-descriptions-item>
          <a-descriptions-item label="咨询电话">
            <a :href="`tel:${service.baseInfo?.phone}`">{{ service.baseInfo?.phone || '无' }}</a>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 办理材料 -->
      <a-card title="办理材料" class="material-card">
        <ul v-if="service.materialList && service.materialList.length">
          <li v-for="(item, index) in service.materialList" :key="index">
            <file-text-outlined /> {{ item }}
          </li>
        </ul>
        <p v-else class="empty-text">暂无材料信息</p>
      </a-card>

      <!-- 办理流程 -->
      <a-card title="办理流程" class="process-card">
        <div v-if="service.processDesc" v-html="formatProcess(service.processDesc)"></div>
        <p v-else class="empty-text">暂无流程描述</p>
      </a-card>

      <!-- 在线办理按钮 -->
      <div class="apply-btn-wrapper">
        <a-button type="primary" size="large" @click="goToApply" block>
          在线办理
        </a-button>
      </div>
    </div>

    <!-- 服务不存在提示 -->
    <a-result v-else status="404" title="服务不存在" sub-title="抱歉，您访问的服务不存在">
      <template #extra>
        <a-button type="primary" @click="goBack">返回</a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  LeftOutlined,
  EyeOutlined,
  FileDoneOutlined,
  FileTextOutlined
} from '@ant-design/icons-vue'
import { getServiceDetailUsingGet } from '@/api/govController'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const service = ref<any>(null)

// 获取服务详情
const fetchServiceDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getServiceDetailUsingGet({ id })
    console.log('服务详情返回:', res)
    if (res.code === 0) {
      service.value = res.data
    } else {
      message.error(res.message || '获取服务详情失败')
    }
  } catch (error) {
    console.error('获取服务详情异常:', error)
    message.error('获取服务详情失败')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

const goToApply = () => {
  if (!service.value?.id) {
    message.error('服务信息不完整')
    return
  }
  router.push(`/gov/apply/${service.value.id}`)
}

// 格式化流程描述（将 \n 转为 <br>）
const formatProcess = (text: string) => {
  return text.replace(/\n/g, '<br>')
}

// 随机颜色（用于图标背景）
const getRandomColor = () => {
  const colors = ['#1890ff', '#52c41a', '#faad14', '#722ed1', '#eb2f96']
  return colors[Math.floor(Math.random() * colors.length)]
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchServiceDetail(id)
  } else {
    message.error('服务ID不存在')
  }
})
</script>

<style scoped>
.service-detail-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
.back-btn {
  margin-bottom: 20px;
  padding: 0;
}
.service-header {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}
.service-icon {
  width: 80px;
  height: 80px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  flex-shrink: 0;
}
.service-title {
  flex: 1;
}
.service-title h1 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}
.service-title .brief {
  font-size: 16px;
  color: #666;
  margin-bottom: 12px;
}
.stats {
  display: flex;
  gap: 24px;
  color: #999;
  font-size: 14px;
}
.info-card,
.material-card,
.process-card {
  margin-bottom: 24px;
}
.material-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.material-card li {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 8px;
}
.material-card li:last-child {
  border-bottom: none;
}
.empty-text {
  color: #999;
  text-align: center;
  padding: 20px 0;
}
.apply-btn-wrapper {
  margin-top: 40px;
  margin-bottom: 40px;
}
</style>
