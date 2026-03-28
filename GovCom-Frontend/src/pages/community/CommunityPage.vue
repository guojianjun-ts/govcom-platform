<!-- src/pages/community/CommunityPage.vue -->
<template>
  <div class="community-page">
    <!-- 顶部操作栏 -->
    <div class="action-bar">
      <h2 class="page-title">
        <team-outlined /> 社区互动
      </h2>
      <a-button type="primary" @click="goToCreate">
        <plus-outlined /> 发布帖子
      </a-button>
    </div>

    <!-- 分类筛选 -->
    <div class="filter-section">
      <a-tabs v-model:activeKey="activeType" @change="handleTypeChange">
        <a-tab-pane key="all" tab="全部" />
        <a-tab-pane key="1" tab="邻里互助" />
        <a-tab-pane key="2" tab="二手交易" />
        <a-tab-pane key="3" tab="社区活动" />
        <a-tab-pane key="4" tab="闲聊交流" />
      </a-tabs>
    </div>

    <!-- 帖子列表 -->
    <a-list
      :data-source="postList"
      :loading="loading"
      :pagination="pagination"
      item-layout="vertical"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card hoverable class="post-card" @click="goToDetail(item.id)">
            <div class="post-header">
              <div class="user-info">
                <a-avatar :src="item.userAvatar" size="small">
                  {{ item.userName?.charAt(0) }}
                </a-avatar>
                <span class="user-name">{{ item.userName }}</span>
                <span class="community-tag" v-if="item.communityName">
                  <environment-outlined /> {{ item.communityName }}
                </span>
              </div>
              <a-tag :color="getTypeColor(item.type)">{{ item.typeName }}</a-tag>
            </div>

            <div class="post-content">
              <h3 class="post-title">{{ item.title }}</h3>
              <p class="post-summary">{{ item.content }}</p>

              <!-- 图片预览（如果有） -->
              <div class="post-images" v-if="item.images && item.images.length > 0">
                <img
                  v-for="(img, index) in item.images.slice(0, 3)"
                  :key="index"
                  :src="img"
                  class="preview-image"
                  @click.stop="previewImage(img)"
                />
                <span v-if="item.images.length > 3" class="more-images">
                  +{{ item.images.length - 3 }}
                </span>
              </div>
            </div>

            <div class="post-stats">
              <span><eye-outlined /> {{ item.viewCount }}</span>
              <span><like-outlined /> {{ item.likeCount }}</span>
              <span><message-outlined /> {{ item.commentCount }}</span>
              <span class="post-time"><clock-circle-outlined /> {{ formatTime(item.createTime) }}</span>
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
  TeamOutlined,
  PlusOutlined,
  EyeOutlined,
  LikeOutlined,
  MessageOutlined,
  ClockCircleOutlined,
  EnvironmentOutlined
} from '@ant-design/icons-vue'
import { getPostListUsingGet } from '@/api/communityPostController'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const activeType = ref('all')
const postList = ref<any[]>([])

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.value.current = page
    fetchPostList()
  }
})

// 获取帖子列表
const fetchPostList = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    }
    if (activeType.value !== 'all') {
      params.type = Number(activeType.value)
    }

    const res = await getPostListUsingGet(params)
    if (res.code === 0) {
      postList.value = res.data || []
      pagination.value.total = postList.value.length * 5
    }
  } catch (error) {
    message.error('获取帖子列表失败')
  } finally {
    loading.value = false
  }
}

// 切换分类
const handleTypeChange = () => {
  pagination.value.current = 1
  fetchPostList()
}

// 获取类型颜色
const getTypeColor = (type: number) => {
  const colors: Record<number, string> = {
    1: 'blue',
    2: 'orange',
    3: 'green',
    4: 'purple'
  }
  return colors[type] || 'default'
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).format('MM-DD HH:mm')
}

// 预览图片
const previewImage = (url: string) => {
  window.open(url, '_blank')
}

// 跳转到发布页
const goToCreate = () => {
  router.push('/community/post/create')
}

// 跳转到详情页
const goToDetail = (id: number) => {
  router.push(`/community/post/${id}`)
}

onMounted(() => {
  fetchPostList()
})
</script>

<style scoped>
.community-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-section {
  margin-bottom: 24px;
}

.post-card {
  cursor: pointer;
  transition: all 0.3s;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-weight: 500;
  color: #333;
}

.community-tag {
  color: #1890ff;
  font-size: 12px;
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.post-summary {
  color: #666;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.post-images {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.preview-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
}

.more-images {
  width: 80px;
  height: 80px;
  background: #f5f5f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 14px;
}

.post-stats {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-time {
  margin-left: auto;
}
</style>
