<!-- src/pages/gov/GovApplicationDetail.vue -->
<template>
  <div class="application-detail-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <!-- 加载状态 -->
    <a-skeleton active v-if="loading" :paragraph="{ rows: 8 }" />

    <div v-else-if="application">
      <!-- 申请单号标题 -->
      <h2 class="page-title">
        申请详情
        <span class="application-no">{{ application.applicationNo }}</span>
      </h2>

      <!-- 核心：状态步骤条 -->
      <div class="status-steps-container">
        <!-- 步骤节点容器 -->
        <div class="status-steps">
          <!-- 步骤1：已提交 -->
          <div class="step-item">
            <div class="step-node" :class="{
              active: currentStatus >= 1,
              rejected: application.status === 4
            }"></div>
            <div class="step-label">已提交</div>
          </div>
          <!-- 线段1-2 -->
          <div class="step-line" :class="{
            active: currentStatus >= 2,
            rejected: application.status === 4
          }"></div>

          <!-- 步骤2：审核中 -->
          <div class="step-item">
            <div class="step-node" :class="{
              active: currentStatus >= 2,
              current: currentStatus === 2,
              rejected: application.status === 4
            }"></div>
            <div class="step-label">审核中</div>
          </div>
          <!-- 线段2-3 -->
          <div class="step-line" :class="{
            active: currentStatus >= 3,
            rejected: application.status === 4
          }"></div>

          <!-- 步骤3：已完成 -->
          <div class="step-item">
            <div class="step-node" :class="{
              active: currentStatus >= 3,
              current: currentStatus === 3,
              rejected: application.status === 4
            }"></div>
            <div class="step-label">已完成</div>
          </div>
          <!-- 线段3-4 -->
          <div class="step-line" :class="{
            active: currentStatus >= 4 && application.status !== 4,
            rejected: application.status === 4
          }"></div>

          <!-- 步骤4：已驳回 -->
          <div class="step-item">
            <div class="step-node" :class="{
              active: currentStatus >= 4,
              current: currentStatus === 4,
              rejected: application.status === 4
            }">
              <span v-if="application.status === 4" class="rejected-icon">✕</span>
            </div>
            <div class="step-label">已驳回</div>
          </div>
        </div>
      </div>

      <!-- 申请信息卡片 -->
      <a-card class="info-card" :title="`${application.serviceName} 服务申请信息`">
        <a-descriptions :column="1">
          <a-descriptions-item label="申请人姓名">{{ application.applicantName }}</a-descriptions-item>
          <a-descriptions-item label="联系电话">{{ application.applicantPhone }}</a-descriptions-item>
          <a-descriptions-item label="办理地区">{{ application.region }}</a-descriptions-item>
          <a-descriptions-item label="提交时间">{{ formatDate(application.submitTime) }}</a-descriptions-item>
          <a-descriptions-item label="申请时间">{{ formatDate(application.createTime) }}</a-descriptions-item>
          <a-descriptions-item v-if="application.remark" label="备注">{{ application.remark }}</a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>

    <!-- 申请不存在提示 -->
    <a-result v-else status="404" title="申请不存在" sub-title="抱歉，未找到该申请记录">
      <template #extra>
        <a-button type="primary" @click="goBack">返回</a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { getApplicationDetailUsingGet } from '@/api/govController'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const application = ref<any>(null)

// 当前状态值（1-4）
const currentStatus = computed(() => {
  return application.value?.status || 1
})

// 获取申请详情
const fetchApplicationDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getApplicationDetailUsingGet({ id })
    if (res.code === 0) {
      application.value = res.data
    } else {
      message.error(res.message || '获取申请详情失败')
    }
  } catch (error) {
    console.error('获取申请详情异常:', error)
    message.error('获取申请详情失败')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 格式化日期
const formatDate = (dateStr: string) => {
  return dateStr ? dayjs(dateStr).format('YYYY-MM-DD HH:mm') : '暂无'
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchApplicationDetail(id)
  } else {
    message.error('申请ID不存在')
    router.back()
  }
})
</script>

<style scoped>
.application-detail-page {
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
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-title .application-no {
  font-size: 14px;
  font-weight: normal;
  color: #999;
}

/* 状态步骤条核心样式 */
.status-steps-container {
  margin-bottom: 40px;
  width: 100%;
}

.status-steps {
  display: flex;
  align-items: center; /* 保持center，只靠margin-top调整线段 */
  justify-content: space-between;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

/* 步骤项（圆形+文字） */
.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
}

/* 圆形节点 */
.step-node {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #d9d9d9; /* 默认灰色 */
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  position: relative;
}

/* 激活状态（绿色） */
.step-node.active {
  background-color: #52c41a; /* 绿色 */
  box-shadow: 0 2px 8px rgba(82, 196, 26, 0.3);
}

/* 当前状态（放大+高亮） */
.step-node.current {
  transform: scale(1.1);
  background-color: #1890ff; /* 蓝色突出当前 */
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.3);
}

/* 驳回状态（红色） */
.step-node.rejected {
  background-color: #ff4d4f; /* 红色 */
  box-shadow: 0 2px 8px rgba(255, 77, 79, 0.3);
}

/* 驳回图标 */
.rejected-icon {
  color: white;
  font-size: 18px;
  font-weight: bold;
  line-height: 1;
}

/* 步骤文字 */
.step-label {
  font-size: 14px;
  color: #666;
  text-align: center;
  width: 80px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 线段样式 - 已调整margin-top */
.step-line {
  flex: 1;
  height: 3px;
  background-color: #d9d9d9; /* 默认灰色 */
  margin-bottom: 20px;
  border-radius: 2px;
  transition: all 0.3s ease;
}

/* 激活的线段（绿色） */
.step-line.active {
  background-color: #52c41a; /* 绿色 */
}

/* 驳回的线段（红色） */
.step-line.rejected {
  background-color: #ff4d4f; /* 红色 */
}

/* 信息卡片样式 */
.info-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.info-card :deep(.ant-card-head) {
  min-height: 48px;
  border-bottom: 1px solid #f0f0f0;
}

.info-card :deep(.ant-card-head-title) {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.info-card :deep(.ant-descriptions-item-label) {
  width: 100px;
  color: #666;
}

.info-card :deep(.ant-descriptions-item-content) {
  color: #333;
}
</style>
