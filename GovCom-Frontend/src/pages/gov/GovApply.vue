<!-- src/pages/gov/GovApply.vue -->
<template>
  <div class="apply-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <h2 class="page-title">服务申请 - {{ serviceDetail?.serviceName }}</h2>

    <a-card class="apply-card">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        layout="vertical"
      >
        <!-- 服务名称（只读展示） -->
        <a-form-item label="服务名称">
          <div class="info-text">{{ serviceDetail?.serviceName }}</div>
        </a-form-item>

        <!-- 申请人姓名（只读展示） -->
        <a-form-item label="申请人姓名">
          <div class="info-text">{{ form.applicantName }}</div>
        </a-form-item>

        <!-- 联系电话（只读展示） -->
        <a-form-item label="联系电话">
          <div class="info-text">{{ form.applicantPhone }}</div>
        </a-form-item>

        <!-- 选择地区（可选） -->
        <a-form-item label="选择地区" name="region" required>
          <a-select
            v-model:value="form.region"
            placeholder="请选择办理地区"
            :options="regionOptions"
          />
        </a-form-item>

        <!-- 用户协议 -->
        <a-form-item name="agreeProtocol">
          <a-checkbox v-model:checked="form.agreeProtocol">
            我已阅读并同意 <a href="#" @click.prevent="showAgreement">《用户服务协议》</a>
          </a-checkbox>
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item>
          <a-button
            type="primary"
            :loading="submitting"
            @click="handleSubmit"
            block
            size="large"
          >
            提交申请
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 协议弹窗 -->
    <a-modal
      v-model:open="agreementVisible"
      title="用户服务协议"
      @ok="agreementVisible = false"
      width="600px"
    >
      <div style="max-height: 400px; overflow-y: auto;">
        <h3>政务服务在线申请协议</h3>
        <p>1. 申请人保证所填信息真实、准确、完整。</p>
        <p>2. 申请人同意将所提交的信息用于政务服务办理。</p>
        <p>3. 申请人知晓并接受服务办理时限和流程。</p>
        <p>4. 如有虚假信息，申请人承担相应法律责任。</p>
        <p>5. 本平台承诺保护申请人个人信息安全。</p>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { getServiceDetailUsingGet, submitApplicationUsingPost } from '@/api/govController'
import { useUserStore } from '@/stores/userStore'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const submitting = ref(false)
const agreementVisible = ref(false)
const serviceDetail = ref<any>(null)

// 使用 userStore 获取用户信息
const userStore = useUserStore()
const userInfo = userStore.loadUserInfo() || {}
console.log('userInfo 结构:', userInfo) // 添加这行查看实际数据

// 从路由参数获取服务ID
const serviceId = Number(route.params.id)

// 地区选项（添加这一部分！）
const regionOptions = [
  { value: '重庆市万州区', label: '万州区' },
  { value: '重庆市涪陵区', label: '涪陵区' },
  { value: '重庆市渝中区', label: '渝中区' },
  { value: '重庆市大渡口区', label: '大渡口区' },
  { value: '重庆市江北区', label: '江北区' },
  { value: '重庆市沙坪坝区', label: '沙坪坝区' },
  { value: '重庆市九龙坡区', label: '九龙坡区' },
  { value: '重庆市南岸区', label: '南岸区' },
  { value: '重庆市北碚区', label: '北碚区' },
  { value: '重庆市渝北区', label: '渝北区' },
  { value: '重庆市巴南区', label: '巴南区' }
]

// 表单数据
const form = reactive({
  serviceId: serviceId,
  region: '重庆市南岸区',
  // ✅ 关键修复：从 userInfo 中取正确的字段
  applicantName: userInfo.userName,
  applicantPhone: userInfo.phone,
  agreeProtocol: false
})

// 添加这行调试
console.log('表单初始值:', form)
// 表单校验规则（也需要添加！）

const rules = {
  region: [
    { required: true, message: '请选择办理地区', trigger: 'change' }
  ],
  agreeProtocol: [
    {
      validator: async (_rule: any, value: boolean) => {
        if (!value) {
          return Promise.reject('请先同意用户协议')
        }
        return Promise.resolve()
      },
      trigger: 'change'
    }
  ]
}

// 获取服务详情
const fetchServiceDetail = async () => {
  if (!serviceId) {
    message.error('服务ID不能为空')
    router.back()
    return
  }

  try {
    const res = await getServiceDetailUsingGet({ id: serviceId })
    if (res.code === 0) {
      serviceDetail.value = res.data
      form.serviceId = res.data.id
    } else {
      message.error(res.message || '获取服务详情失败')
      router.back()
    }
  } catch (error) {
    message.error('获取服务详情失败')
    router.back()
  }
}

// 提交申请
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    const res = await submitApplicationUsingPost({
      serviceId: form.serviceId,
      region: form.region,
      applicantName: form.applicantName,
      applicantPhone: form.applicantPhone,
      agreeProtocol: form.agreeProtocol
    })

    if (res.code === 0) {
      message.success('申请提交成功')
      router.push('/gov/application/my')
    } else {
      message.error(res.message || '提交失败')
    }
  } catch (error) {
    message.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 修复 Modal 警告（将 visible 改为 open）
const showAgreement = () => {
  agreementVisible.value = true
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchServiceDetail()
})
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

.page-title {
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 30px;
  color: #333;
}

.apply-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

:deep(.ant-form-item-label) {
  font-weight: 500;
}

:deep(.ant-input[disabled]) {
  background-color: #f5f5f5;
  color: #333;
  cursor: not-allowed;
}

/* 信息文本样式 */
.info-text {
  padding: 8px 12px;
  background-color: #f5f5f5;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  color: #333;
  min-height: 40px;
  line-height: 1.5;
  font-size: 14px;
}
</style>
