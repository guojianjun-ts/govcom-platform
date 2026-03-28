<!-- src/pages/community/CommunityManage.vue -->
<template>
  <div class="community-manage-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回个人中心
    </a-button>

    <h2 class="page-title">我的社区管理</h2>

    <!-- 已加入社区 -->
    <div class="my-communities-section">
      <h3 class="section-title">
        <team-outlined /> 已加入的社区
        <span class="sub-title">（共 {{ myCommunities.length }} 个）</span>
      </h3>

      <a-row :gutter="[16, 16]" v-if="myCommunities.length > 0">
        <a-col :span="8" v-for="community in myCommunities" :key="community.id">
          <a-card hoverable class="community-card joined">
            <div class="community-card-content">
              <div class="community-info">
                <h4>{{ community.communityName }}</h4>
                <p class="community-address"><environment-outlined /> {{ community.address || '地址待完善' }}</p>
                <p class="community-phone"><phone-outlined /> {{ community.contactPhone || '暂无电话' }}</p>
                <p class="community-intro">{{ community.intro || '暂无简介' }}</p>
              </div>
              <div class="community-actions">
                <a-tag color="green">已加入</a-tag>
                <a-button danger size="small" @click.stop="showLeaveConfirm(community)">
                  退出
                </a-button>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-card v-else class="empty-card">
        <div class="empty-content">
          <team-outlined :style="{ fontSize: '48px', color: '#ccc' }" />
          <p>您还没有加入任何社区</p>
        </div>
      </a-card>
    </div>

    <!-- 所有社区列表 -->
    <div class="all-communities-section">
      <h3 class="section-title">
        <bank-outlined /> 所有社区
        <span class="sub-title">（可加入多个社区）</span>
      </h3>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索社区名称、地址..."
          @search="fetchAllCommunities"
          allow-clear
        />
      </div>

      <a-list
        :data-source="allCommunities"
        :loading="loading"
        :pagination="pagination"
        item-layout="vertical"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <a-card class="community-card">
              <div class="community-card-content">
                <div class="community-info">
                  <h4>{{ item.communityName }}</h4>
                  <p class="community-address"><environment-outlined /> {{ item.address || '地址待完善' }}</p>
                  <p class="community-phone"><phone-outlined /> {{ item.contactPhone || '暂无电话' }}</p>
                  <p class="community-intro">{{ item.intro || '暂无简介' }}</p>
                </div>
                <div class="community-actions">
                  <a-tag v-if="isJoined(item.id)" color="green">已加入</a-tag>
                  <a-tag v-else color="default">未加入</a-tag>
                  <a-button
                    v-if="!isJoined(item.id)"
                    type="primary"
                    size="small"
                    @click.stop="handleJoin(item)"
                    :loading="joiningId === item.id"
                  >
                    加入
                  </a-button>
                  <a-button
                    v-else
                    danger
                    size="small"
                    @click.stop="showLeaveConfirm(item)"
                    :loading="leavingId === item.id"
                  >
                    退出
                  </a-button>
                </div>
              </div>
            </a-card>
          </a-list-item>
        </template>
      </a-list>
    </div>

    <!-- 退出确认弹窗 -->
    <a-modal
      v-model:open="leaveModalVisible"
      title="确认退出社区"
      @ok="handleLeave"
      @cancel="leaveModalVisible = false"
      okText="确认退出"
      cancelText="取消"
      okButtonProps="{ danger: true }"
    >
      <p>确定要退出 <strong>{{ selectedCommunity?.communityName }}</strong> 吗？</p>
      <p class="warning-text">退出后您将无法查看该社区的资讯和参与社区互动。</p>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  LeftOutlined,
  TeamOutlined,
  BankOutlined,
  EnvironmentOutlined,
  PhoneOutlined
} from '@ant-design/icons-vue'
import { getCommunityListUsingGet, getMyCommunitiesUsingGet } from '@/api/communityController'
import { joinCommunityUsingPost, leaveCommunityUsingPost } from '@/api/communityController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const searchKeyword = ref('')
const loading = ref(false)
const joiningId = ref<number | null>(null)
const leavingId = ref<number | null>(null)
const leaveModalVisible = ref(false)
const selectedCommunity = ref<any>(null)

// 我的社区列表
const myCommunities = ref<any[]>([])

// 所有社区列表
const allCommunities = ref<any[]>([])

// 分页配置
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.value.current = page
    fetchAllCommunities()
  }
})

// 获取我的社区
const fetchMyCommunities = async () => {
  try {
    const res = await getMyCommunitiesUsingGet()
    if (res.code === 0) {
      myCommunities.value = res.data || []
    }
  } catch (error) {
    console.error('获取我的社区失败', error)
    message.error('获取我的社区失败')
  }
}

// 获取所有社区
const fetchAllCommunities = async () => {
  loading.value = true
  try {
    const res = await getCommunityListUsingGet()
    if (res.code === 0) {
      allCommunities.value = res.data || []
      pagination.value.total = allCommunities.value.length
    }
  } catch (error) {
    console.error('获取社区列表失败', error)
    message.error('获取社区列表失败')
  } finally {
    loading.value = false
  }
}

// 判断是否已加入
const isJoined = (communityId: number) => {
  return myCommunities.value.some(c => c.id === communityId)
}

// 加入社区
const handleJoin = async (community: any) => {
  joiningId.value = community.id
  try {
    const res = await joinCommunityUsingPost({ communityId: community.id })
    if (res.code === 0 && res.data === true) {
      message.success(`成功加入 ${community.communityName}`)
      await fetchMyCommunities()  // 刷新我的社区列表
    } else {
      message.error(res.message || '加入失败')
    }
  } catch (error) {
    message.error('加入失败，请稍后重试')
  } finally {
    joiningId.value = null
  }
}

// 显示退出确认
const showLeaveConfirm = (community: any) => {
  selectedCommunity.value = community
  leaveModalVisible.value = true
}

// 退出社区
const handleLeave = async () => {
  if (!selectedCommunity.value) return

  leavingId.value = selectedCommunity.value.id
  try {
    const res = await leaveCommunityUsingPost({ communityId: selectedCommunity.value.id })
    if (res.code === 0 && res.data === true) {
      message.success(`已退出 ${selectedCommunity.value.communityName}`)
      await fetchMyCommunities()  // 刷新我的社区列表
      leaveModalVisible.value = false
    } else {
      message.error(res.message || '退出失败')
    }
  } catch (error) {
    message.error('退出失败，请稍后重试')
  } finally {
    leavingId.value = null
    selectedCommunity.value = null
  }
}

// 返回
const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchMyCommunities()
  fetchAllCommunities()
})
</script>

<style scoped>
.community-manage-page {
  max-width: 1200px;
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

.section-title {
  font-size: 20px;
  font-weight: 500;
  color: #333;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title .sub-title {
  font-size: 14px;
  font-weight: normal;
  color: #999;
  margin-left: 8px;
}

/* 我的社区区域 */
.my-communities-section {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
}

/* 社区卡片 */
.community-card {
  height: 100%;
  transition: all 0.3s;
}

.community-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.community-card.joined {
  border-left: 4px solid #52c41a;
}

.community-card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.community-info h4 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.community-info p {
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.community-info .community-intro {
  color: #999;
  font-size: 13px;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px dashed #f0f0f0;
}

.community-actions {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 搜索栏 */
.search-bar {
  margin-bottom: 20px;
  max-width: 400px;
}

/* 空状态卡片 */
.empty-card {
  text-align: center;
  padding: 40px;
  background: #fafafa;
  border: 1px dashed #d9d9d9;
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.empty-content p {
  color: #666;
  margin: 0;
}

/* 警告文字 */
.warning-text {
  color: #ff4d4f;
  font-size: 13px;
  margin-top: 8px;
}

/* 响应式 */
@media (max-width: 768px) {
  .page-title {
    font-size: 20px;
  }

  .section-title {
    font-size: 18px;
  }
}
</style>
