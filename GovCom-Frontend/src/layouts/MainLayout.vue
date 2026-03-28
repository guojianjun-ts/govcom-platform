<!-- src/layouts/MainLayout.vue -->
<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="logo">
        <span class="logo-text">普渝惠生</span>
      </div>

      <!-- 主导航菜单 - 根据用户类型动态显示 -->
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="horizontal"
        class="nav-menu"
        @click="handleMenuClick"
      >
        <!-- 所有用户都能看到的首页 -->
        <a-menu-item key="home">
          <home-outlined />首页
        </a-menu-item>

        <!-- 根据用户类型显示不同菜单 -->
        <!-- 未登录或普通用户 (userType=1) 显示常规菜单 -->
        <template v-if="!currentUser || currentUser.userType === 1">
          <a-menu-item key="info">
            <info-circle-outlined />资讯中心
          </a-menu-item>
          <a-menu-item key="gov">
            <bank-outlined />政务服务
          </a-menu-item>
          <a-menu-item key="community">
            <team-outlined />社区互动
          </a-menu-item>
          <a-menu-item key="life">
            <shop-outlined />便民生活
          </a-menu-item>
        </template>

        <!-- 政务人员 (userType=2) -->
        <template v-else-if="currentUser?.userType === 2">
          <a-menu-item key="workOrder">
            <file-text-outlined />政务工单处理
          </a-menu-item>
        </template>

        <!-- 社区工作人员 (userType=3) -->
        <template v-else-if="currentUser?.userType === 3">
          <a-menu-item key="workOrder">
            <team-outlined />社区工单处理
          </a-menu-item>
        </template>

        <!-- 管理员 (userType=4) -->
        <template v-else-if="currentUser?.userType === 4">
          <a-menu-item key="info">资讯中心</a-menu-item>
          <a-menu-item key="gov">政务服务</a-menu-item>
          <a-menu-item key="community">社区互动</a-menu-item>
          <a-menu-item key="life">便民生活</a-menu-item>
          <a-menu-item key="workOrder">工单处理</a-menu-item>
        </template>
      </a-menu>

      <!-- 右侧用户信息 -->
      <div class="user-info">
        <template v-if="currentUser">
          <a-dropdown>
            <div class="user-info-trigger">
              <a-avatar :src="currentUser.userAvatar" size="small">
                {{ currentUser.userName?.charAt(0) || currentUser.userAccount?.charAt(0) || '用' }}
              </a-avatar>
              <span class="username">{{ currentUser.userName || currentUser.userAccount }}</span>
              <down-outlined />
            </div>
            <template #overlay>
              <a-menu>
                <a-menu-item key="profile" @click="goToProfile">
                  <user-outlined />个人中心
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="handleLogout">
                  <logout-outlined />退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <template v-else>
          <a-button type="link" @click="goToLogin">登录/注册</a-button>
        </template>
      </div>
    </a-layout-header>

    <!-- 内容区域 -->
    <a-layout-content class="content">
      <router-view />
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  HomeOutlined,
  InfoCircleOutlined,
  BankOutlined,
  TeamOutlined,
  ShopOutlined,
  FileTextOutlined,
  SettingOutlined,
  UserOutlined,
  LogoutOutlined,
  DownOutlined
} from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/userStore'
import { userLogoutUsingPost } from '@/api/userController'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 使用计算属性获取用户信息，自动解包 ref
const currentUser = computed(() => userStore.userInfo?.value || null)

// 当前选中的菜单项
const selectedKeys = ref<string[]>([])

// 根据当前路由设置选中菜单
const updateSelectedKeys = () => {
  const path = route.path
  if (path === '/') {
    selectedKeys.value = ['home']
  } else if (path.startsWith('/info')) {
    selectedKeys.value = ['info']
  } else if (path.startsWith('/gov')) {
    selectedKeys.value = ['gov']
  } else if (path.startsWith('/community')) {
    selectedKeys.value = ['community']
  } else if (path.startsWith('/life')) {
    selectedKeys.value = ['life']
  } else if (path.startsWith('/workOrder')) {
    selectedKeys.value = ['workOrder']
  }
}

// 菜单点击处理
const handleMenuClick = ({ key }: { key: string }) => {
  const routes: Record<string, string> = {
    home: '/',
    info: '/info',
    gov: '/gov',
    community: '/community',
    life: '/life',
    workOrder: '/workOrder'
  }
  router.push(routes[key])
}

// 跳转到个人中心
const goToProfile = () => {
  router.push('/user/profile')
}

// 跳转到登录
const goToLogin = () => {
  router.push('/user/login')
}

// 退出登录
const handleLogout = async () => {
  try {
    await userLogoutUsingPost()
    userStore.clearUserInfo()
    message.success('已退出登录')
    router.push('/user/login')
  } catch (error) {
    message.error('退出失败')
  }
}

// 监听路由变化
router.afterEach(() => {
  updateSelectedKeys()
})

onMounted(() => {
  updateSelectedKeys()
  console.log('当前用户信息:', currentUser.value)
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  background: white;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.logo {
  margin-right: 40px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
  line-height: 64px;
}

.user-info {
  margin-left: auto;
}

.user-info-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 12px;
}

.username {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.content {
  padding: 24px;
  background: #f0f2f5;
}

.footer {
  text-align: center;
  background: white;
  padding: 16px;
  color: #999;
}

:deep(.ant-menu-horizontal) {
  border-bottom: none;
}

:deep(.ant-menu-item) {
  padding: 0 20px;
}
</style>
