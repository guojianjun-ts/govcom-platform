<!-- src/pages/info/InfoDetail.vue -->
<template>
  <div class="info-detail-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <!-- 加载状态 -->
    <a-skeleton active v-if="loading" :paragraph="{ rows: 10 }" />

    <div v-else-if="detail" class="detail-container">
      <!-- 文章头部 -->
      <div class="article-header">
        <h1 class="title">{{ detail.title }}</h1>

        <div class="meta">
          <div class="meta-left">
            <span><user-outlined /> {{ detail.author || '系统' }}</span>
            <span><calendar-outlined /> {{ formatDate(detail.publishTime) }}</span>
            <span><eye-outlined /> {{ detail.viewCount || 0 }} 阅读</span>
          </div>
          <div class="meta-right">
            <span v-if="detail.source">来源：{{ detail.source }}</span>
            <a-tag v-if="detail.scope === 2 && detail.communityName" color="green">
              <team-outlined /> {{ detail.communityName }}
            </a-tag>
            <div class="tag-list" v-if="detail.tag">
              <a-tag
                v-for="(tag, index) in splitTags(detail.tag)"
                :key="index"
                :color="getTagColor(tag)"
              >
                {{ tag }}
              </a-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 封面图（如果有） -->
      <div class="cover-image" v-if="detail.cover">
        <img :src="detail.cover" :alt="detail.title" />
      </div>

      <!-- 文章内容 -->
      <div class="article-content" v-html="detail.content"></div>

      <!-- 附件列表（如果有） -->
      <div class="attachments" v-if="detail.attachments && detail.attachments.length">
        <h3>附件下载</h3>
        <ul>
          <li v-for="file in detail.attachments" :key="file.url">
            <a :href="file.url" target="_blank">
              <file-pdf-outlined v-if="file.type === 'pdf'" />
              <file-word-outlined v-if="file.type === 'doc'" />
              <file-excel-outlined v-if="file.type === 'xls'" />
              {{ file.name }}
            </a>
          </li>
        </ul>
      </div>

      <!-- 分享和点赞 -->
      <div class="article-footer">
        <a-button @click="handleLike" :class="{ liked: isLiked }">
          <like-outlined /> 点赞 {{ detail.likeCount || 0 }}
        </a-button>
        <a-button @click="handleShare">
          <share-alt-outlined /> 分享
        </a-button>
      </div>
    </div>

    <!-- 文章不存在提示 -->
    <a-result v-else status="404" title="文章不存在" sub-title="抱歉，您访问的文章不存在">
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
  UserOutlined,
  CalendarOutlined,
  EyeOutlined,
  TeamOutlined,
  LikeOutlined,
  ShareAltOutlined,
  FilePdfOutlined,
  FileWordOutlined,
  FileExcelOutlined
} from '@ant-design/icons-vue'
import { getArticleDetailUsingGet } from '@/api/infoController'
import { useUserStore } from '@/stores/userStore'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const detail = ref<any>(null)
const isLiked = ref(false)

// 获取文章详情
const fetchArticleDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getArticleDetailUsingGet({ id })
    console.log('文章详情:', res)
    if (res.code === 0) {
      detail.value = res.data
    } else {
      message.error(res.message || '获取文章详情失败')
    }
  } catch (error) {
    message.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 点赞
const handleLike = () => {
  if (!userStore.userInfo) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }
  isLiked.value = !isLiked.value
  if (isLiked.value) {
    detail.value.likeCount = (detail.value.likeCount || 0) + 1
    message.success('点赞成功')
  } else {
    detail.value.likeCount = (detail.value.likeCount || 0) - 1
  }
}

// 分享
const handleShare = () => {
  // 复制链接到剪贴板
  const url = window.location.href
  navigator.clipboard.writeText(url).then(() => {
    message.success('链接已复制到剪贴板')
  })
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
  return dayjs(dateStr).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchArticleDetail(id)
  } else {
    message.error('文章ID不存在')
    router.back()
  }
})
</script>

<style scoped>
.info-detail-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.detail-container {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.article-header {
  margin-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 20px;
}

.article-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  line-height: 1.4;
}

.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.meta-left {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 14px;
}

.meta-left span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.tag-list {
  display: flex;
  gap: 6px;
}

.cover-image {
  margin-bottom: 30px;
  text-align: center;
}

.cover-image img {
  max-width: 100%;
  max-height: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.article-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 40px;
}

.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3) {
  margin: 24px 0 16px;
  font-weight: 600;
}

.article-content :deep(p) {
  margin-bottom: 16px;
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: 4px;
  margin: 16px 0;
}

.article-content :deep(ul),
.article-content :deep(ol) {
  margin: 16px 0;
  padding-left: 24px;
}

.attachments {
  margin: 30px 0;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}

.attachments h3 {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #333;
}

.attachments ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.attachments li {
  margin-bottom: 8px;
}

.attachments a {
  color: #1890ff;
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-footer {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.article-footer .liked {
  color: #1890ff;
  border-color: #1890ff;
}

@media (max-width: 768px) {
  .detail-container {
    padding: 20px;
  }

  .article-header h1 {
    font-size: 22px;
  }

  .meta {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
