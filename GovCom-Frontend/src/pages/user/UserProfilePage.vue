<!-- src/pages/user/UserProfilePage.vue -->
<template>
  <div class="profile-container">
    <a-card title="个人中心">
      <template #extra>
        <a-button type="link" @click="goBack">返回</a-button>
      </template>

      <!-- 用户基本信息卡片（简洁版） -->
      <div class="user-summary">
        <a-avatar :size="64" :src="userInfo?.userAvatar" class="user-avatar">
          {{ userInfo?.userName?.charAt(0) || userInfo?.userAccount?.charAt(0) }}
        </a-avatar>
        <div class="user-info">
          <h2>{{ userInfo?.userName || userInfo?.userAccount }}</h2>
          <p><phone-outlined /> {{ userInfo?.phone || '未绑定手机' }}</p>
          <p><bank-outlined /> {{ getUserTypeText(userInfo?.userType) }}</p>
        </div>
      </div>

      <a-divider />

      <!-- 功能入口网格 -->
      <div class="function-grid">
        <div class="grid-title">我的服务</div>
        <a-row :gutter="[16, 16]">
          <a-col :span="8" v-for="item in functionEntries" :key="item.path">
            <a-card hoverable class="function-card" @click="goToPage(item.path)">
              <div class="card-content">
                <div class="card-icon" :style="{ backgroundColor: item.bgColor }">
                  <component :is="item.icon" :style="{ fontSize: '24px', color: '#fff' }" />
                </div>
                <div class="card-text">
                  <h4>{{ item.title }}</h4>
                  <p>{{ item.desc }}</p>
                </div>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  UserOutlined,
  FileTextOutlined,
  TeamOutlined,
  LockOutlined,
  PhoneOutlined,
  BankOutlined,
  ClockCircleOutlined  // 新增：用于社区服务申请图标
} from '@ant-design/icons-vue'

const router = useRouter()
const userInfo = ref<any>(null)

// 功能入口
const functionEntries = [
  {
    title: '个人信息',
    desc: '查看和修改个人资料',
    icon: UserOutlined,
    path: '/user/profile/info',
    bgColor: '#1890ff'
  },
  {
    title: '修改密码',
    desc: '更改登录密码',
    icon: LockOutlined,
    path: '/user/profile/password',
    bgColor: '#52c41a'
  },
  {
    title: '我的政务申请',
    desc: '查看已提交的政务服务申请',
    icon: FileTextOutlined,
    path: '/gov/application/my',
    bgColor: '#722ed1'
  },
  {
    title: '社区管理',
    desc: '加入/退出社区',
    icon: TeamOutlined,
    path: '/community/manage',
    bgColor: '#fa8c16'
  },
  {
    title: '社区服务申请',  // 新增
    desc: '查看我的证明、帮助、投诉',
    icon: ClockCircleOutlined,
    path: '/life/orders',
    bgColor: '#13c2c2'
  }
]

// 用户类型转换
const getUserTypeText = (type: number) => {
  const typeMap: Record<number, string> = {
    1: '普通用户',
    2: '政务人员',
    3: '社区管理员',
    4: '系统管理员'
  }
  return typeMap[type] || '未知'
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 跳转到对应页面
const goToPage = (path: string) => {
  router.push(path)
}

onMounted(() => {
  const cached = localStorage.getItem('userInfo')
  if (cached) {
    userInfo.value = JSON.parse(cached)
  }
})
</script>

<style scoped>
.profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

/* 用户摘要 */
.user-summary {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 12px;
  margin-bottom: 16px;
}

.user-avatar {
  background: #1890ff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.user-info h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.user-info p {
  margin: 4px 0;
  color: #666;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 功能入口网格 */
.function-grid {
  margin-top: 24px;
}

.grid-title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin-bottom: 16px;
  padding-left: 8px;
  border-left: 4px solid #1890ff;
}

.function-card {
  cursor: pointer;
  transition: all 0.3s;
  height: 100%;
}

.function-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.card-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-text {
  flex: 1;
}

.card-text h4 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px 0;
}

.card-text p {
  font-size: 12px;
  color: #999;
  margin: 0;
  line-height: 1.4;
}
</style>
