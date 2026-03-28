<!-- src/pages/life/LifePage.vue -->
<template>
  <div class="life-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回首页
    </a-button>

    <h2 class="page-title">便民生活</h2>

    <!-- 功能入口卡片 -->
    <div class="function-grid">
      <!-- 社区证明 -->
      <a-card hoverable class="function-card" @click="goToProof">
        <div class="card-icon" style="background: #1890ff">
          <file-text-outlined />
        </div>
        <div class="card-content">
          <h3>社区证明</h3>
          <p>开具入学居住证明、助学金证明等</p>
        </div>
      </a-card>

      <!-- 社区帮助 -->
      <a-card hoverable class="function-card" @click="goToHelp">
        <div class="card-icon" style="background: #52c41a">
          <heart-outlined />
        </div>
        <div class="card-content">
          <h3>社区帮助</h3>
          <p>物业报修、政策咨询、老人关怀等</p>
        </div>
      </a-card>

      <!-- 投诉建议 -->
      <a-card hoverable class="function-card" @click="goToComplaint">
        <div class="card-icon" style="background: #faad14">
          <message-outlined />
        </div>
        <div class="card-content">
          <h3>投诉建议</h3>
          <p>环境卫生、社区管理、设施维护等</p>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import {
  LeftOutlined,
  FileTextOutlined,
  HeartOutlined,
  MessageOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

// 跳转函数
const goBack = () => router.push('/')
const goToProof = () => router.push('/life/proof/apply')
const goToHelp = () => router.push('/life/help/apply')
const goToComplaint = () => router.push('/life/complaint/apply')
</script>

<style scoped>
.life-page {
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
  margin-bottom: 30px;
}

.function-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.function-card {
  display: flex;
  align-items: center;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.function-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 28px;
  color: white;
  flex-shrink: 0;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.card-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
}
</style>
