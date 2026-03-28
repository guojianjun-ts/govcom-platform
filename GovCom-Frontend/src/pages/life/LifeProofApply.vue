<!-- src/pages/life/LifeProofApply.vue -->
<template>
  <div class="apply-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回便民生活
    </a-button>

    <a-card title="社区证明申请">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        layout="vertical"
      >
        <!-- 证明类型 -->
        <a-form-item label="证明类型" name="subType" required>
          <a-select v-model:value="form.subType" placeholder="请选择证明类型">
            <a-select-option value="residence">入学居住证明</a-select-option>
            <a-select-option value="economic">家庭经济状况证明（助学金）</a-select-option>
            <a-select-option value="activity">学生参加社区活动证明</a-select-option>
            <a-select-option value="background">政审/现实表现证明</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 学生姓名 -->
        <a-form-item label="学生姓名" name="studentName" required>
          <a-input v-model:value="form.studentName" placeholder="请输入学生姓名" />
        </a-form-item>

        <!-- 就读学校 -->
        <a-form-item label="就读学校" name="school">
          <a-input v-model:value="form.school" placeholder="请输入就读学校" />
        </a-form-item>

        <!-- 用途说明 -->
        <a-form-item label="用途说明" name="purpose" required>
          <a-textarea
            v-model:value="form.purpose"
            placeholder="例如：幼升小入学报名、申请助学金等"
            :rows="3"
          />
        </a-form-item>

        <!-- 申请人信息 -->
        <a-divider>申请人信息</a-divider>

        <a-form-item label="申请人姓名" name="applicantName" required>
          <a-input v-model:value="form.applicantName" placeholder="请输入申请人姓名" />
        </a-form-item>

        <a-form-item label="联系电话" name="applicantPhone" required>
          <a-input v-model:value="form.applicantPhone" placeholder="请输入联系电话" />
        </a-form-item>

        <!-- 附件上传 -->
        <a-form-item label="佐证材料（可选）">
          <a-upload
            v-model:file-list="fileList"
            :multiple="true"
            :max-count="5"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
          >
            <a-button>
              <upload-outlined /> 上传材料
            </a-button>
          </a-upload>
          <div class="upload-tip">支持上传户口本、身份证等图片（最多5张）</div>
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
            提交申请
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
import { submitProofUsingPost } from '@/api/lifeController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const submitting = ref(false)
const fileList = ref<any[]>([])

const form = reactive({
  subType: undefined,
  studentName: '',
  school: '',
  purpose: '',
  applicantName: userStore.userInfo?.userName || '',
  applicantPhone: userStore.userInfo?.phone || '',
  attachments: [] as string[]
})

const rules = {
  subType: [{ required: true, message: '请选择证明类型', trigger: 'change' }],
  studentName: [{ required: true, message: '请输入学生姓名', trigger: 'blur' }],
  purpose: [{ required: true, message: '请输入用途说明', trigger: 'blur' }],
  applicantName: [{ required: true, message: '请输入申请人姓名', trigger: 'blur' }],
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
  return false // 阻止自动上传
}

const handleUploadChange = ({ fileList: newFileList }: any) => {
  fileList.value = newFileList
  // 模拟上传，实际应该调用上传接口
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
    const res = await submitProofUsingPost(form)
    if (res.code === 0) {
      message.success('申请提交成功')
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
