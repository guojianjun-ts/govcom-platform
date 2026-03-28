<!-- src/pages/HomePage.vue -->
<template>
  <div class="home-page">
    <!-- 天气卡片 -->
    <div class="weather-card">
      <div class="weather-info">
        <div class="weather-left">
          <div class="city">重庆 · 渝中区</div>
          <div class="temperature">24°</div>
          <div class="weather-desc">晴 · 空气质量优</div>
        </div>
        <div class="weather-right">
          <div class="weather-icon">☀️</div>
          <div class="weather-range">15° ~ 28°</div>
        </div>
      </div>
      <div class="weather-tips">
        💡 适合户外活动，建议佩戴口罩
      </div>
    </div>

    <!-- 快捷入口 - 只保留核心功能 -->
    <div class="quick-entry">
      <h2 class="section-title">快捷服务</h2>
      <a-row :gutter="[24, 24]">
        <a-col :xs="12" :sm="8" :md="6" v-for="item in quickEntries" :key="item.id">
          <a-card hoverable class="quick-entry-card" @click="goToModule(item.path)">
            <div class="entry-icon">
              <component :is="item.icon" :style="{ fontSize: '32px', color: item.color }" />
            </div>
            <div class="entry-info">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 便民生活服务 -->
    <div class="life-services">
      <h2 class="section-title">便民生活</h2>
      <a-row :gutter="[16, 16]">
        <a-col :span="6" v-for="service in lifeServices" :key="service.id">
          <a-card hoverable class="life-service-card" @click="goToLifeService(service.path)">
            <div class="service-icon">
              <component :is="service.icon" :style="{ fontSize: '28px', color: service.color }" />
            </div>
            <div class="service-name">{{ service.name }}</div>
            <div class="service-desc">{{ service.desc }}</div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 最新资讯 -->
    <div class="latest-news">
      <h2 class="section-title">
        <span>最新资讯</span>
        <a-button type="link" @click="goToInfo">查看更多</a-button>
      </h2>
      <a-row :gutter="24">
        <a-col :span="12" v-for="news in newsList" :key="news.id">
          <a-card hoverable class="news-card" @click="goToNewsDetail(news.id)">
            <div class="news-item">
              <div class="news-content">
                <div class="news-header">
                  <h3>{{ news.title }}</h3>
                  <div class="tag-list" v-if="news.tag">
                    <a-tag
                      v-for="(tag, index) in splitTags(news.tag)"
                      :key="index"
                      :color="getTagColor(tag)"
                    >
                      {{ tag }}
                    </a-tag>
                  </div>
                </div>
                <p class="news-summary">{{ news.summary }}</p>
                <div class="news-meta">
                  <span><calendar-outlined /> {{ news.publishTime }}</span>
                  <span><eye-outlined /> {{ news.viewCount }}阅读</span>
                  <span v-if="news.communityName"><team-outlined /> {{ news.communityName }}</span>
                </div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 社区互动 - 最新帖子 -->
    <div class="community-section">
      <h2 class="section-title">
        <span>社区互动</span>
        <a-button type="link" @click="goToCommunity">查看更多</a-button>
      </h2>
      <a-row :gutter="24">
        <a-col :span="8" v-for="post in communityPosts" :key="post.id">
          <a-card hoverable class="community-card" @click="goToPostDetail(post.id)">
            <div class="post-header">
              <a-avatar :src="post.userAvatar" size="small">
                {{ post.userName?.charAt(0) }}
              </a-avatar>
              <span class="author-name">{{ post.userName }}</span>
              <a-tag :color="getPostTypeColor(post.type)" size="small">
                {{ post.typeName }}
              </a-tag>
            </div>
            <h4 class="post-title">{{ post.title }}</h4>
            <p class="post-content">{{ post.content }}</p>
            <div class="post-meta">
              <span><eye-outlined /> {{ post.viewCount }}</span>
              <span><like-outlined /> {{ post.likeCount }}</span>
              <span><message-outlined /> {{ post.commentCount }}</span>
              <span><clock-circle-outlined /> {{ formatTime(post.createTime) }}</span>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  CalendarOutlined,
  EyeOutlined,
  LikeOutlined,
  MessageOutlined,
  ClockCircleOutlined,
  TeamOutlined,
  BankOutlined,
  ShopOutlined,
  FileTextOutlined,
  InfoCircleOutlined,
  RocketOutlined,
  HeartOutlined,
  FileDoneOutlined,
  ToolOutlined,
  PhoneOutlined
} from '@ant-design/icons-vue'
import { getArticleListUsingGet } from '@/api/infoController'
import { getPostListUsingGet } from '@/api/communityPostController'
import dayjs from 'dayjs'

const router = useRouter()

// 快捷入口 - 移除了个人中心和工单处理
const quickEntries = ref([
  { id: 1, title: '政务服务', description: '在线办事', icon: BankOutlined, color: '#1890ff', path: '/gov' },
  { id: 2, title: '社区互动', description: '邻里交流', icon: TeamOutlined, color: '#52c41a', path: '/community' },
  { id: 3, title: '便民生活', description: '生活服务', icon: ShopOutlined, color: '#faad14', path: '/life' },
  { id: 4, title: '资讯中心', description: '了解动态', icon: InfoCircleOutlined, color: '#eb2f96', path: '/info' }
])

// 便民生活服务
const lifeServices = ref([
  {
    id: 1,
    name: '社区证明',
    desc: '入学居住、助学金等',
    icon: FileDoneOutlined,
    color: '#1890ff',
    path: '/life/proof/apply'
  },
  {
    id: 2,
    name: '社区帮助',
    desc: '物业报修、政策咨询',
    icon: ToolOutlined,
    color: '#52c41a',
    path: '/life/help/apply'
  },
  {
    id: 3,
    name: '投诉建议',
    desc: '环境卫生、社区管理',
    icon: PhoneOutlined,
    color: '#faad14',
    path: '/life/complaint/apply'
  },
  {
    id: 4,
    name: '我的申请',
    desc: '查看办理进度',
    icon: FileTextOutlined,
    color: '#722ed1',
    path: '/life/orders'
  }
])

// 最新资讯
const newsList = ref<any[]>([])

// 社区帖子
const communityPosts = ref<any[]>([])

// 获取最新资讯
const fetchLatestNews = async () => {
  try {
    const res = await getArticleListUsingGet({
      pageNum: 1,
      pageSize: 4
    })
    if (res.code === 0) {
      newsList.value = (res.data || []).map((item: any) => ({
        ...item,
        publishTime: dayjs(item.publishTime).format('YYYY-MM-DD')
      }))
    }
  } catch (error) {
    console.error('获取最新资讯失败', error)
  }
}

// 获取社区最新帖子
const fetchLatestPosts = async () => {
  try {
    const res = await getPostListUsingGet({
      pageNum: 1,
      pageSize: 3
    })
    if (res.code === 0) {
      communityPosts.value = (res.data || []).map((item: any) => ({
        ...item,
        createTime: dayjs(item.createTime).format('MM-DD HH:mm')
      }))
    }
  } catch (error) {
    console.error('获取社区帖子失败', error)
  }
}

// 拆分标签
const splitTags = (tagStr: string) => {
  if (!tagStr) return []
  return tagStr.split(/[,，]/).filter(t => t.trim() !== '')
}

// 获取资讯标签颜色
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

// 获取帖子类型颜色
const getPostTypeColor = (type: number) => {
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

// 跳转方法
const goToModule = (path: string) => {
  router.push(path)
}

const goToInfo = () => {
  router.push('/info')
}

const goToCommunity = () => {
  router.push('/community')
}

const goToNewsDetail = (id: number) => {
  router.push(`/info/detail/${id}`)
}

const goToPostDetail = (id: number) => {
  router.push(`/community/post/${id}`)
}

const goToLifeService = (path: string) => {
  router.push(path)
}

onMounted(() => {
  fetchLatestNews()
  fetchLatestPosts()
})
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 天气卡片 */
.weather-card {
  background: linear-gradient(135deg, #43a9f1, #228be6);
  border-radius: 16px;
  padding: 20px;
  color: white;
  margin-bottom: 30px;
  box-shadow: 0 8px 20px rgba(0, 100, 200, 0.2);
}

.weather-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.city {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.temperature {
  font-size: 48px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 4px;
}

.weather-desc {
  font-size: 14px;
  opacity: 0.9;
}

.weather-right {
  text-align: center;
}

.weather-icon {
  font-size: 48px;
  margin-bottom: 8px;
}

.weather-range {
  font-size: 14px;
  opacity: 0.9;
}

.weather-tips {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  font-size: 14px;
  opacity: 0.9;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 24px;
  font-weight: 500;
  color: #333;
}

/* 快捷入口 */
.quick-entry {
  margin-bottom: 40px;
}

.quick-entry-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.quick-entry-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.entry-icon {
  margin-bottom: 12px;
}

.entry-info h3 {
  font-size: 16px;
  margin-bottom: 4px;
  color: #333;
}

.entry-info p {
  color: #999;
  font-size: 12px;
}

/* 便民生活服务 */
.life-services {
  margin-bottom: 40px;
}

.life-service-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  height: 140px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.life-service-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.service-icon {
  margin-bottom: 12px;
}

.service-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.service-desc {
  font-size: 12px;
  color: #999;
}

/* 最新资讯 */
.latest-news {
  margin-bottom: 40px;
}

.news-card {
  cursor: pointer;
  height: 100%;
}

.news-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.news-header h3 {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tag-list {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}

.news-summary {
  color: #666;
  font-size: 14px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.news-meta {
  display: flex;
  gap: 16px;
  color: #999;
  font-size: 12px;
}

.news-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 社区互动 */
.community-section {
  margin-bottom: 40px;
}

.community-card {
  cursor: pointer;
  height: 100%;
  transition: all 0.3s;
}

.community-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.author-name {
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.post-content {
  color: #666;
  font-size: 13px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.post-meta {
  display: flex;
  gap: 12px;
  color: #999;
  font-size: 12px;
  flex-wrap: wrap;
}

.post-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 响应式 */
@media (max-width: 768px) {
  .news-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .news-meta {
    flex-wrap: wrap;
  }
}
</style>
