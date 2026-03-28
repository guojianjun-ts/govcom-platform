<!-- src/pages/community/CommunityPostDetail.vue -->
<template>
  <div class="post-detail-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回社区
    </a-button>

    <!-- 加载状态 -->
    <a-skeleton active v-if="loading" :paragraph="{ rows: 8 }" />

    <div v-else-if="post">
      <!-- 帖子内容卡片 -->
      <a-card class="post-card">
        <!-- 帖子头部 -->
        <div class="post-header">
          <div class="user-info">
            <a-avatar :size="48" :src="post.userAvatar">
              {{ post.userName?.charAt(0) }}
            </a-avatar>
            <div class="user-meta">
              <div class="user-name">{{ post.userName }}</div>
              <div class="post-meta">
                <span><clock-circle-outlined /> {{ formatTime(post.createTime) }}</span>
                <span><eye-outlined /> {{ post.viewCount }}阅读</span>
                <a-tag :color="getTypeColor(post.type)">{{ post.typeName }}</a-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 帖子标题和内容 -->
        <h1 class="post-title">{{ post.title }}</h1>
        <div class="post-content">{{ post.content }}</div>

        <!-- 图片展示 -->
        <div class="post-images" v-if="post.images && post.images.length > 0">
          <div
            v-for="(img, index) in post.images"
            :key="index"
            class="image-wrapper"
            @click="previewImage(img)"
          >
            <img :src="img" :alt="`图片${index + 1}`" />
          </div>
        </div>

        <!-- 点赞按钮 -->
        <div class="post-actions">
          <a-button
            :type="post.isLiked ? 'primary' : 'default'"
            @click="handleLike"
          >
            <like-outlined /> {{ post.isLiked ? '已点赞' : '点赞' }} ({{ post.likeCount }})
          </a-button>
        </div>
      </a-card>

      <!-- 评论区域 -->
      <div class="comment-section">
        <h3 class="comment-title">
          <message-outlined /> 评论 ({{ post.commentCount || 0 }})
        </h3>

        <!-- 评论输入框 -->
        <div class="comment-input">
          <a-textarea
            v-model:value="newComment"
            placeholder="写下你的评论..."
            :rows="3"
          />
          <a-button
            type="primary"
            @click="submitComment"
            :loading="submitting"
            class="submit-btn"
          >
            发表评论
          </a-button>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div v-for="comment in commentList" :key="comment.id" class="comment-item">
            <a-avatar :size="32" :src="comment.userAvatar" class="comment-avatar">
              {{ comment.userName?.charAt(0) }}
            </a-avatar>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-user">{{ comment.userName }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions">
                <a-button
                  type="link"
                  size="small"
                  @click="handleCommentLike(comment)"
                >
                  <like-outlined /> {{ comment.likeCount }}
                </a-button>
                <a-button type="link" size="small" @click="replyTo(comment)">
                  回复
                </a-button>
              </div>

              <!-- 回复列表 -->
              <div class="reply-list" v-if="comment.replies && comment.replies.length > 0">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <a-avatar :size="24" :src="reply.userAvatar" class="reply-avatar">
                    {{ reply.userName?.charAt(0) }}
                  </a-avatar>
                  <div class="reply-content">
                    <div class="reply-header">
                      <span class="reply-user">{{ reply.userName }}</span>
                      <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                    </div>
                    <div class="reply-text">{{ reply.content }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 帖子不存在提示 -->
    <a-result v-else status="404" title="帖子不存在" sub-title="抱歉，您访问的帖子不存在">
      <template #extra>
        <a-button type="primary" @click="goBack">返回社区</a-button>
      </template>
    </a-result>

    <!-- 图片预览模态框 -->
    <a-modal
      v-model:open="previewVisible"
      :footer="null"
      @cancel="previewVisible = false"
      width="80%"
    >
      <img :src="previewUrl" style="width: 100%;" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  LeftOutlined,
  ClockCircleOutlined,
  EyeOutlined,
  LikeOutlined,
  MessageOutlined
} from '@ant-design/icons-vue'
import {
  getPostDetailUsingGet,
  getCommentsUsingGet,
  createCommentUsingPost,
  toggleLikeUsingPost
} from '@/api/communityPostController'
import { useUserStore } from '@/stores/userStore'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const submitting = ref(false)
const post = ref<any>(null)
const commentList = ref<any[]>([])
const newComment = ref('')
const replyToId = ref<number | null>(null)
const previewVisible = ref(false)
const previewUrl = ref('')

// 获取帖子详情
const fetchPostDetail = async (id: number) => {
  loading.value = true
  try {
    const res = await getPostDetailUsingGet({ id })
    if (res.code === 0) {
      post.value = res.data
    } else {
      message.error(res.message || '获取帖子详情失败')
    }
  } catch (error) {
    message.error('获取帖子详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const fetchComments = async (postId: number) => {
  try {
    const res = await getCommentsUsingGet({ postId })
    if (res.code === 0) {
      commentList.value = res.data || []
    }
  } catch (error) {
    console.error('获取评论失败', error)
  }
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    message.warning('请输入评论内容')
    return
  }
  if (!userStore.userInfo) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }

  submitting.value = true
  try {
    const res = await createCommentUsingPost({
      postId: post.value.id,
      content: newComment.value,
      parentId: replyToId.value || undefined
    })
    if (res.code === 0) {
      message.success('评论成功')
      newComment.value = ''
      replyToId.value = null
      // 刷新评论列表
      await fetchComments(post.value.id)
      // 更新帖子评论数
      if (post.value) {
        post.value.commentCount = (post.value.commentCount || 0) + 1
      }
    } else {
      message.error(res.message || '评论失败')
    }
  } catch (error) {
    message.error('评论失败')
  } finally {
    submitting.value = false
  }
}

// 点赞/取消点赞
const handleLike = async () => {
  if (!userStore.userInfo) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }

  try {
    const res = await toggleLikeUsingPost({ postId: post.value.id })
    if (res.code === 0) {
      post.value.isLiked = res.data
      post.value.likeCount += res.data ? 1 : -1
    }
  } catch (error) {
    message.error('操作失败')
  }
}

// 评论点赞
const handleCommentLike = async (comment: any) => {
  if (!userStore.userInfo) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }

  try {
    const res = await toggleLikeUsingPost({ commentId: comment.id })
    if (res.code === 0) {
      comment.isLiked = res.data
      comment.likeCount += res.data ? 1 : -1
    }
  } catch (error) {
    message.error('操作失败')
  }
}

// 回复评论
const replyTo = (comment: any) => {
  replyToId.value = comment.id
  newComment.value = `@${comment.userName} `
  // 聚焦输入框
  // 可以添加 focus 逻辑
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

// 预览图片
const previewImage = (url: string) => {
  previewUrl.value = url
  previewVisible.value = true
}

// 格式化时间
const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchPostDetail(id)
    fetchComments(id)
  } else {
    message.error('帖子ID不存在')
    router.back()
  }
})
</script>

<style scoped>
.post-detail-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.post-card {
  margin-bottom: 24px;
  border-radius: 12px;
}

.post-header {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  gap: 16px;
}

.user-meta {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.post-meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
  align-items: center;
}

.post-title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 16px;
  line-height: 1.4;
}

.post-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 24px;
  white-space: pre-wrap;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.image-wrapper {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 1;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.image-wrapper:hover img {
  transform: scale(1.05);
}

.post-actions {
  display: flex;
  justify-content: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

/* 评论区域 */
.comment-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.comment-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.comment-input {
  margin-bottom: 30px;
}

.submit-btn {
  margin-top: 12px;
  float: right;
}

.comment-list {
  clear: both;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.comment-user {
  font-weight: 600;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  color: #333;
  margin-bottom: 8px;
  line-height: 1.6;
}

.comment-actions {
  display: flex;
  gap: 8px;
}

/* 回复列表 */
.reply-list {
  margin-top: 12px;
  margin-left: 24px;
  padding-left: 12px;
  border-left: 2px solid #f0f0f0;
}

.reply-item {
  display: flex;
  gap: 8px;
  padding: 8px 0;
}

.reply-avatar {
  flex-shrink: 0;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.reply-user {
  font-weight: 500;
  color: #333;
  font-size: 13px;
}

.reply-time {
  font-size: 11px;
  color: #999;
}

.reply-text {
  color: #666;
  font-size: 13px;
  line-height: 1.5;
}
</style>
