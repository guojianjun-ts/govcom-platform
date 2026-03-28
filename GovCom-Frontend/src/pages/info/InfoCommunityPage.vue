<!-- src/pages/info/InfoCommunityPage.vue -->
<template>
  <div class="info-page">
    <!-- 搜索栏 -->
    <div class="search-section">
      <a-input-search
        v-model:value="searchKeyword"
        placeholder="搜索资讯、政策..."
        size="large"
        @search="handleSearch"
      >
        <template #enterButton>
          <a-button type="primary">搜索</a-button>
        </template>
      </a-input-search>
    </div>

    <!-- 左右分栏布局 6:4 -->
    <a-row :gutter="24">
      <!-- 左侧：政务资讯 (60%) -->
      <a-col :span="14">
        <div class="gov-section">
          <!-- 政务资讯标题 -->
          <div class="section-title-wrapper">
            <h2 class="section-title">
              <bank-outlined /> 政务资讯
            </h2>
          </div>

          <!-- 政务分类标签 -->
          <div class="category-tabs-wrapper">
            <a-tabs
              v-model:activeKey="govActiveTab"
              @change="handleGovTabChange"
              size="middle"
              class="category-tabs"
            >
              <a-tab-pane key="all" tab="全部" />
              <a-tab-pane
                v-for="item in govCategoryList"
                :key="item.id"
                :tab="item.categoryName"
              />
            </a-tabs>
          </div>

          <!-- 政务资讯列表 -->
          <a-list
            :data-source="govArticleList"
            :loading="govLoading"
            :pagination="govPagination"
            item-layout="vertical"
            class="gov-list"
          >
            <template #renderItem="{ item }">
              <a-list-item>
                <a-card hoverable @click="goToDetail(item.id)" class="news-card">
                  <div class="news-item">
                    <div class="news-info">
                      <div class="news-header">
                        <h3>{{ item.title }}</h3>
                      </div>
                      <div class="tag-list" v-if="item.tag">
                        <a-tag
                          v-for="(tag, index) in splitTags(item.tag)"
                          :key="index"
                          :color="getTagColor(tag)"
                        >
                          {{ tag }}
                        </a-tag>
                      </div>
                      <p class="summary">{{ item.summary }}</p>
                      <div class="meta">
                        <span><calendar-outlined /> {{ formatDate(item.publishTime) }}</span>
                        <span><eye-outlined /> {{ item.viewCount }}阅读</span>
                        <span><user-outlined /> {{ item.author || '系统' }}</span>
                      </div>
                    </div>
                    <div class="news-image" v-if="item.cover">
                      <img :src="item.cover" :alt="item.title" />
                    </div>
                  </div>
                </a-card>
              </a-list-item>
            </template>
          </a-list>
        </div>
      </a-col>

      <!-- 右侧：社区资讯 (40%) -->
      <a-col :span="10">
        <div class="community-section">
          <!-- 社区资讯标题 + 当前社区 -->
          <div class="section-title-wrapper">
            <div class="title-row">
              <h2 class="section-title">
                <team-outlined /> 社区资讯
                <span class="sub-title" v-if="!userInfo">（登录后可查看）</span>
              </h2>
              <div class="current-community-tag" v-if="userInfo && myCommunities.length > 0">
                <environment-outlined /> {{ myCommunities[0].communityName }}
                <a-button
                  v-if="myCommunities.length > 1"
                  type="link"
                  size="small"
                  @click="showCommunitySelector = true"
                  class="switch-btn"
                >
                  切换
                </a-button>
              </div>
            </div>
          </div>

          <!-- 未登录提示 -->
          <a-card v-if="!userInfo" class="tip-card">
            <div class="tip-content">
              <team-outlined :style="{ fontSize: '48px', color: '#ccc' }" />
              <p>请登录后查看您所在社区的资讯</p>
              <a-button type="primary" @click="goToLogin">立即登录</a-button>
            </div>
          </a-card>

          <!-- 已登录但没有加入社区 -->
          <a-card v-else-if="myCommunities.length === 0" class="tip-card">
            <div class="tip-content">
              <environment-outlined :style="{ fontSize: '48px', color: '#ccc' }" />
              <p>您还没有加入任何社区</p>
              <a-button type="primary" @click="goToJoinCommunity">去加入社区</a-button>
            </div>
          </a-card>

          <!-- 已登录且有社区 -->
          <div v-else>
            <!-- 社区分类标签 -->
            <div class="category-tabs-wrapper" v-if="communityCategoryList.length > 0">
              <a-tabs
                v-model:activeKey="communityActiveTab"
                @change="handleCommunityTabChange"
                size="middle"
                class="category-tabs"
              >
                <a-tab-pane key="all" tab="全部" />
                <a-tab-pane
                  v-for="item in communityCategoryList"
                  :key="item.id"
                  :tab="item.categoryName"
                />
              </a-tabs>
            </div>

            <!-- 社区资讯列表 -->
            <a-list
              :data-source="communityArticleList"
              :loading="communityLoading"
              :pagination="communityPagination"
              item-layout="vertical"
              class="community-list"
            >
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-card hoverable @click="goToDetail(item.id)" class="community-news-card">
                    <div class="community-news-item">
                      <h4>{{ item.title }}</h4>
                      <div class="tag-list" v-if="item.tag">
                        <a-tag
                          v-for="(tag, index) in splitTags(item.tag)"
                          :key="index"
                          :color="getTagColor(tag)"
                          size="small"
                        >
                          {{ tag }}
                        </a-tag>
                      </div>
                      <p class="community-summary">{{ item.summary }}</p>
                      <div class="community-meta">
                        <span><calendar-outlined /> {{ formatDate(item.publishTime) }}</span>
                        <span><eye-outlined /> {{ item.viewCount }}阅读</span>
                      </div>
                    </div>
                  </a-card>
                </a-list-item>
              </template>
            </a-list>
          </div>
        </div>
      </a-col>
    </a-row>

    <!-- 社区切换模态框（如果有多个社区） -->
    <a-modal
      v-model:open="showCommunitySelector"
      title="选择社区"
      @ok="handleCommunityChange"
      @cancel="showCommunitySelector = false"
    >
      <a-radio-group v-model:value="selectedCommunityId">
        <a-radio
          v-for="community in myCommunities"
          :key="community.id"
          :value="community.id"
          style="display: block; margin-bottom: 12px;"
        >
          {{ community.communityName }}
        </a-radio>
      </a-radio-group>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  CalendarOutlined,
  EyeOutlined,
  TeamOutlined,
  EnvironmentOutlined,
  BankOutlined,
  UserOutlined
} from '@ant-design/icons-vue'
import { getInfoCategoryListUsingGet } from '@/api/infoController'
import { getArticleListUsingGet, getCommunityArticlesUsingGet } from '@/api/infoController'
import { getMyCommunitiesUsingGet } from '@/api/communityController'
import { useUserStore } from '@/stores/userStore'
import dayjs from 'dayjs'

const router = useRouter()
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const searchKeyword = ref('')

// 政务资讯相关
const govLoading = ref(false)
const govActiveTab = ref('all')
const govCategoryList = ref<any[]>([])
const govArticleList = ref<any[]>([])

// 社区资讯相关
const communityLoading = ref(false)
const communityActiveTab = ref('all')
const communityCategoryList = ref<any[]>([])
const communityArticleList = ref<any[]>([])
const myCommunities = ref<any[]>([])
const activeCommunityId = ref<number>()

// 政务资讯分页
const govPagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    govPagination.value.current = page
    fetchGovArticleList()
  }
})

// 社区资讯分页
const communityPagination = ref({
  current: 1,
  pageSize: 5,
  total: 0,
  onChange: (page: number) => {
    communityPagination.value.current = page
    fetchCommunityArticleList()
  }
})

// 获取分类列表
const fetchCategoryList = async () => {
  try {
    const res = await getInfoCategoryListUsingGet()
    if (res.code === 0) {
      const allCategories = res.data || []
      // 区分政务分类和社区分类（根据 scope 字段）
      govCategoryList.value = allCategories.filter((c: any) => c.scope === 1 || c.scope === 3)
      communityCategoryList.value = allCategories.filter((c: any) => c.scope === 2 || c.scope === 3)
    }
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

// 获取我的社区
const fetchMyCommunities = async () => {
  if (!userInfo.value) return
  try {
    const res = await getMyCommunitiesUsingGet()
    if (res.code === 0) {
      myCommunities.value = res.data || []
      if (myCommunities.value.length > 0) {
        activeCommunityId.value = myCommunities.value[0].id
        fetchCommunityArticleList()
      }
    }
  } catch (error) {
    console.error('获取我的社区失败', error)
  }
}

// 获取政务资讯列表
const fetchGovArticleList = async () => {
  govLoading.value = true
  try {
    const params: any = {
      pageNum: govPagination.value.current,
      pageSize: govPagination.value.pageSize,
      scope: 1  // 只获取政务资讯
    }
    if (govActiveTab.value !== 'all') {
      params.categoryId = govActiveTab.value
    }

    const res = await getArticleListUsingGet(params)
    if (res.code === 0) {
      govArticleList.value = res.data || []
      govPagination.value.total = govArticleList.value.length * 5
    }
  } catch (error) {
    message.error('获取政务资讯失败')
  } finally {
    govLoading.value = false
  }
}

// 获取社区资讯列表
const fetchCommunityArticleList = async () => {
  if (!activeCommunityId.value) return

  communityLoading.value = true
  try {
    const params: any = {
      communityId: activeCommunityId.value,
      pageNum: communityPagination.value.current,
      pageSize: communityPagination.value.pageSize
    }
    if (communityActiveTab.value !== 'all') {
      params.categoryId = communityActiveTab.value
    }

    const res = await getCommunityArticlesUsingGet(params)
    if (res.code === 0) {
      communityArticleList.value = res.data || []
      communityPagination.value.total = communityArticleList.value.length * 5
    }
  } catch (error) {
    message.error('获取社区资讯失败')
  } finally {
    communityLoading.value = false
  }
}

// 切换政务分类
const handleGovTabChange = () => {
  govPagination.value.current = 1
  fetchGovArticleList()
}

// 切换社区分类
const handleCommunityTabChange = () => {
  communityPagination.value.current = 1
  fetchCommunityArticleList()
}

// 切换社区
const showCommunitySelector = ref(false)
const selectedCommunityId = ref<number>()

const handleCommunityChange = () => {
  if (selectedCommunityId.value) {
    activeCommunityId.value = selectedCommunityId.value
    showCommunitySelector.value = false
    communityPagination.value.current = 1
    fetchCommunityArticleList()
  } else {
    // 原来的逻辑（如果有的话）
    communityPagination.value.current = 1
    fetchCommunityArticleList()
  }
}

// 搜索
const handleSearch = () => {
  if (!searchKeyword.value.trim()) {
    message.warning('请输入搜索关键词')
    return
  }
  router.push(`/info/search?keyword=${searchKeyword.value}`)
}

// 跳转到详情
const goToDetail = (id: number) => {
  router.push(`/info/detail/${id}`)
}

// 跳转到加入社区
const goToJoinCommunity = () => {
  router.push('/community/list')
}

// 跳转到登录
const goToLogin = () => {
  router.push('/user/login')
}

// 拆分标签
const splitTags = (tagStr: string) => {
  if (!tagStr) return []
  return tagStr.split(/[,，]/).filter(t => t.trim() !== '')
}

// 获取标签颜色
const getTagColor = (tag: string) => {
  const colors: Record<string, string> = {
    '重要': 'red',
    '热点': 'orange',
    '政策': 'blue',
    '活动': 'green',
    '紧急': 'purple',
    '通知': 'cyan',
    '公告': 'gold'
  }
  return colors[tag] || 'default'
}

// 格式化日期
const formatDate = (dateStr: string) => {
  return dayjs(dateStr).format('YYYY-MM-DD')
}

// 监听登录状态变化
watch(() => userInfo.value, (newVal) => {
  if (newVal) {
    fetchMyCommunities()
  } else {
    myCommunities.value = []
    communityArticleList.value = []
  }
})

onMounted(() => {
  fetchCategoryList()
  fetchGovArticleList()
  if (userInfo.value) {
    fetchMyCommunities()
  }
})
</script>

<style scoped>
.info-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin-bottom: 30px;
}

/* 标题区域 - 单独一行 */
.section-title-wrapper {
  margin-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 8px;
}

/* 标题行 - 左右布局 */
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
}

.section-title :deep(.anticon) {
  color: #1890ff;
}

.sub-title {
  font-size: 14px;
  font-weight: normal;
  color: #999;
  margin-left: 12px;
}

/* 当前社区标签 */
.current-community-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 12px;
  background: #e6f7ff;
  border-radius: 20px;
  color: #0050b3;
  font-size: 14px;
  border: 1px solid #91d5ff;
}

.current-community-tag .switch-btn {
  padding: 0 4px;
  height: auto;
  font-size: 12px;
}

/* 分类标签区域 - 统一样式 */
.category-tabs-wrapper {
  margin-bottom: 20px;
  background: #f5f5f5;
  padding: 6px 12px;
  border-radius: 6px;
}

.category-tabs :deep(.ant-tabs-nav) {
  margin-bottom: 0;
}

.category-tabs :deep(.ant-tabs-tab) {
  padding: 4px 12px !important;  /* 统一为较小的内边距 */
  margin: 0 4px !important;      /* 统一标签间距 */
  font-size: 13px !important;    /* 统一字体大小 */
  transition: all 0.3s;
}

.category-tabs :deep(.ant-tabs-tab:hover) {
  color: #1890ff;
}

.category-tabs :deep(.ant-tabs-tab-active) {
  font-weight: 500;
}

/* 新闻卡片样式 */
.news-card {
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.news-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.news-item {
  display: flex;
  gap: 20px;
}

.news-info {
  flex: 1;
}

.news-header {
  margin-bottom: 8px;
}

.news-header h3 {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.tag-list {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  margin-bottom: 8px;
}

.tag-list :deep(.ant-tag) {
  margin-right: 0;
}

.summary {
  color: #666;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.6;
}

.meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
}

.meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.news-image {
  width: 120px;
  height: 80px;
  flex-shrink: 0;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

/* 右侧社区资讯样式 */
.community-news-card {
  transition: all 0.3s;
  border-radius: 6px;
}

.community-news-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.community-news-item {
  padding: 4px 0;
}

.community-news-item h4 {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.community-summary {
  color: #666;
  font-size: 13px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.community-meta {
  display: flex;
  gap: 12px;
  color: #999;
  font-size: 12px;
}

.community-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 提示卡片 */
.tip-card {
  text-align: center;
  padding: 40px 20px;
  background: #fafafa;
  border: 1px dashed #d9d9d9;
}

.tip-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.tip-content p {
  color: #666;
  margin: 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .section-title-wrapper {
    flex-direction: column;
    align-items: flex-start;
  }

  .category-tabs-wrapper {
    overflow-x: auto;
    white-space: nowrap;
  }

  .news-item {
    flex-direction: column;
  }

  .news-image {
    width: 100%;
    height: 160px;
  }

  .title-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}

/* 确保列表容器有正确的高度和边距 */
.gov-list,
.community-list {
  min-height: 400px;  /* 确保有足够高度显示分页 */
}

/* 分页控件样式 */
:deep(.ant-list-pagination) {
  margin-top: 24px;
  text-align: center;  /* 居中对齐 */
  padding-bottom: 8px;
}

/* 确保每个模块的分页独立 */
.gov-section,
.community-section {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.gov-list,
.community-list {
  flex: 1;
  display: flex;
  flex-direction: column;
}

:deep(.ant-list) {
  display: flex;
  flex-direction: column;
  height: 100%;
}

:deep(.ant-list-items) {
  flex: 1;
}

:deep(.ant-list-pagination) {
  margin-top: auto;  /* 推到底部 */
  padding-top: 20px;
}


</style>
