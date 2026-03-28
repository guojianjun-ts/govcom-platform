<!-- src/pages/life/LifeHelpApply.vue -->
<template>
  <div class="apply-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回便民生活
    </a-button>

    <a-card title="社区帮助申请">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        layout="vertical"
      >
        <!-- 帮助类型 -->
        <a-form-item label="帮助类型" name="subType" required>
          <a-select v-model:value="form.subType" placeholder="请选择帮助类型">
            <a-select-option value="repair">物业报修（水电/门窗/家电）</a-select-option>
            <a-select-option value="consult">政策咨询（社保/医保/入学）</a-select-option>
            <a-select-option value="elderly">老人关怀（上门探访/代购）</a-select-option>
            <a-select-option value="other">其他</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 详细描述 -->
        <a-form-item label="详细描述" name="description" required>
          <a-textarea
            v-model:value="form.description"
            placeholder="请详细描述您需要帮助的事项"
            :rows="4"
          />
        </a-form-item>

        <!-- 预约时间 -->
        <a-form-item label="预约时间" name="appointmentTime">
          <a-date-picker
            v-model:value="form.appointmentTime"
            placeholder="请选择预约时间"
            style="width: 100%"
            format="YYYY-MM-DD"
          />
        </a-form-item>

        <!-- 具体地址 -->
        <a-form-item label="具体地址" name="address">
          <a-input v-model:value="form.address" placeholder="请输入具体地址" />
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
import { submitHelpUsingPost } from '@/api/lifeController'
import { useUserStore } from '@/stores/userStore'
import dayjs from 'dayjs'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const submitting = ref(false)
const fileList = ref<any[]>([])

const form = reactive({
  subType: undefined,
  description: '',
  appointmentTime: undefined,
  address: '',
  applicantName: userStore.userInfo?.userName || '',
  applicantPhone: userStore.userInfo?.phone || '',
  attachments: [] as string[]
})

const rules = {
  subType: [{ required: true, message: '请选择帮助类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入详细描述', trigger: 'blur' }],
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

  const submitData = {
    ...form,
    appointmentTime: form.appointmentTime ? dayjs(form.appointmentTime).format('YYYY-MM-DD') : undefined
  }

  submitting.value = true
  try {
    const res = await submitHelpUsingPost(submitData)
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
