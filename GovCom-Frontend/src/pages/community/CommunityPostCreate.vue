<!-- src/pages/community/CommunityPostCreate.vue -->
<template>
  <div class="create-post-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回社区
    </a-button>

    <a-card title="发布新帖子">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        layout="vertical"
      >
        <!-- 选择社区 -->
        <a-form-item label="选择社区" name="communityId" required>
          <a-select
            v-model:value="form.communityId"
            placeholder="请选择社区（选填，不选则发布到全平台）"
            allow-clear
          >
            <a-select-option
              v-for="community in myCommunities"
              :key="community.id"
              :value="community.id"
            >
              {{ community.communityName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <!-- 帖子类型 -->
        <a-form-item label="帖子类型" name="type" required>
          <a-radio-group v-model:value="form.type">
            <a-radio :value="1">邻里互助</a-radio>
            <a-radio :value="2">二手交易</a-radio>
            <a-radio :value="3">社区活动</a-radio>
            <a-radio :value="4">闲聊交流</a-radio>
          </a-radio-group>
        </a-form-item>

        <!-- 标题 -->
        <a-form-item label="标题" name="title" required>
          <a-input
            v-model:value="form.title"
            placeholder="请输入标题"
            :maxlength="100"
            show-count
          />
        </a-form-item>

        <!-- 内容 -->
        <a-form-item label="内容" name="content" required>
          <a-textarea
            v-model:value="form.content"
            placeholder="请输入内容..."
            :rows="6"
            :maxlength="2000"
            show-count
          />
        </a-form-item>

        <!-- 图片上传 -->
        <a-form-item label="图片（可选）">
          <a-upload
            v-model:file-list="fileList"
            list-type="picture-card"
            :multiple="true"
            :max-count="9"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
            @preview="handlePreview"
          >
            <div v-if="fileList.length < 9">
              <plus-outlined />
              <div style="margin-top: 8px">上传图片</div>
            </div>
          </a-upload>
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item>
          <a-button
            type="primary"
            @click="handleSubmit"
            :loading="submitting"
            block
            size="large"
          >
            发布帖子
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 图片预览 -->
    <a-modal
      v-model:open="previewVisible"
      :footer="null"
      @cancel="previewVisible = false"
    >
      <img alt="预览" style="width: 100%" :src="previewImageUrl" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { createPostUsingPost } from '@/api/communityPostController'
import { getMyCommunitiesUsingGet } from '@/api/communityController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const submitting = ref(false)
const myCommunities = ref<any[]>([])
const fileList = ref<any[]>([])
const previewVisible = ref(false)
const previewImageUrl = ref('')

// 表单数据
const form = reactive({
  communityId: undefined,
  type: 1,
  title: '',
  content: '',
  images: [] as string[]
})

// 表单校验规则
const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, message: '标题至少2个字符', trigger: 'blur' },
    { max: 100, message: '标题最多100个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { min: 5, message: '内容至少5个字符', trigger: 'blur' },
    { max: 2000, message: '内容最多2000个字符', trigger: 'blur' }
  ]
}

// 获取我的社区列表
const fetchMyCommunities = async () => {
  try {
    const res = await getMyCommunitiesUsingGet()
    if (res.code === 0) {
      myCommunities.value = res.data || []
    }
  } catch (error) {
    console.error('获取社区列表失败', error)
  }
}

// 上传前检查
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件')
    return false
  }
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    message.error('图片大小不能超过5MB')
    return false
  }
  return false // 返回 false 阻止自动上传
}

// 上传变化处理
const handleUploadChange = ({ fileList: newFileList }: any) => {
  fileList.value = newFileList
  // 这里应该调用上传接口获取图片URL
  // 为了简化，我们直接用本地预览的URL
  form.images = newFileList.map((file: any) => file.url || file.thumbUrl)
}

// 预览图片
const handlePreview = (file: any) => {
  previewImageUrl.value = file.url || file.thumbUrl
  previewVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  if (!userStore.userInfo) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }

  submitting.value = true
  try {
    const res = await createPostUsingPost(form)
    if (res.code === 0) {
      message.success('发布成功')
      router.push(`/community/post/${res.data}`)
    } else {
      message.error(res.message || '发布失败')
    }
  } catch (error) {
    message.error('发布失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchMyCommunities()
})
</script>

<style scoped>
.create-post-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

:deep(.ant-upload-select-picture-card) {
  width: 100px;
  height: 100px;
}
</style>
