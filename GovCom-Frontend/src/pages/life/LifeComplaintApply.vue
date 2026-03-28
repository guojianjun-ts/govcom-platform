<!-- src/pages/life/LifeComplaintApply.vue -->
<template>
  <div class="apply-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回便民生活
    </a-button>

    <a-card title="投诉建议">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        layout="vertical"
      >
        <!-- 投诉类型 -->
        <a-form-item label="投诉类型" name="subType" required>
          <a-select v-model:value="form.subType" placeholder="请选择投诉类型">
            <a-select-option value="environmental">环境卫生（垃圾/噪音/污染）</a-select-option>
            <a-select-option value="management">社区管理（停车/安保/物业）</a-select-option>
            <a-select-option value="facility">设施维护（路灯/健身器材）</a-select-option>
            <a-select-option value="other">其他</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 投诉标题 -->
        <a-form-item label="投诉标题" name="title" required>
          <a-input v-model:value="form.title" placeholder="请输入投诉标题" />
        </a-form-item>

        <!-- 详细描述 -->
        <a-form-item label="详细描述" name="description" required>
          <a-textarea
            v-model:value="form.description"
            placeholder="请详细描述您的问题"
            :rows="4"
          />
        </a-form-item>

        <!-- 发生地点 -->
        <a-form-item label="发生地点" name="location">
          <a-input v-model:value="form.location" placeholder="请输入问题发生的地点" />
        </a-form-item>

        <!-- 申请人信息 -->
        <a-divider>您的信息</a-divider>

        <a-form-item label="您的姓名" name="applicantName" required>
          <a-input v-model:value="form.applicantName" placeholder="请输入您的姓名" />
        </a-form-item>

        <a-form-item label="联系电话" name="applicantPhone" required>
          <a-input v-model:value="form.applicantPhone" placeholder="请输入联系电话" />
        </a-form-item>

        <!-- 附件上传 -->
        <a-form-item label="相关图片（可选）">
          <a-upload
            v-model:file-list="fileList"
            :multiple="true"
            :max-count="5"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
          >
            <a-button>
              <upload-outlined /> 上传图片
            </a-button>
          </a-upload>
          <div class="upload-tip">可上传现场照片等（最多5张）</div>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            @click="handleSubmit"
            :loading="submitting"
            block
            size="large"
          >
            提交
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined, UploadOutlined } from '@ant-design/icons-vue'
import { submitComplaintUsingPost } from '@/api/lifeController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const submitting = ref(false)
const fileList = ref<any[]>([])

const form = reactive({
  subType: undefined,
  title: '',
  description: '',
  location: '',
  applicantName: userStore.userInfo?.userName || '',
  applicantPhone: userStore.userInfo?.phone || '',
  attachments: [] as string[]
})

const rules = {
  subType: [{ required: true, message: '请选择投诉类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入投诉标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入详细描述', trigger: 'blur' }],
  applicantName: [{ required: true, message: '请输入您的姓名', trigger: 'blur' }],
  applicantPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

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
  return false
}

const handleUploadChange = ({ fileList: newFileList }: any) => {
  fileList.value = newFileList
  form.attachments = newFileList.map((f: any) => f.url || f.thumbUrl || URL.createObjectURL(f.originFileObj))
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    const res = await submitComplaintUsingPost(form)
    if (res.code === 0) {
      message.success('提交成功')
      setTimeout(() => {
        router.push('/life/orders')
      }, 1500)
    } else {
      message.error(res.message || '提交失败')
    }
  } catch (error) {
    message.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.apply-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
