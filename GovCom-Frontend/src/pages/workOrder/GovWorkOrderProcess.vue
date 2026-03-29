<!-- src/pages/workOrder/GovWorkOrderProcess.vue -->
<template>
  <div class="process-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <a-card title="处理政务工单">
      <a-skeleton active v-if="loading" :paragraph="{ rows: 6 }" />

      <div v-else-if="order">
        <div class="order-info">
          <h3>工单信息</h3>
          <p><strong>申请单号：</strong>{{ order.applicationNo }}</p>
          <p><strong>服务名称：</strong>{{ order.serviceName }}</p>
          <p><strong>申请人：</strong>{{ order.applicantName }}</p>
          <p><strong>联系电话：</strong>{{ order.applicantPhone }}</p>
        </div>

        <a-divider />

        <a-form
          :model="form"
          :rules="rules"
          ref="formRef"
          layout="vertical"
        >
          <a-form-item label="处理意见" name="action" required>
            <a-radio-group v-model:value="form.action">
              <a-radio :value="3">通过并完成</a-radio>
              <a-radio :value="4">驳回</a-radio>
            </a-radio-group>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="handleSubmit" :loading="submitting" block>
              提交处理结果
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { getGovWorkOrderDetailUsingGet, processGovWorkOrderUsingPost } from '@/api/workOrderController'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const submitting = ref(false)
const order = ref<any>(null)

const form = ref({
  action: 3
})

const rules = {
  action: [{ required: true, message: '请选择处理意见', trigger: 'change' }]
}

const fetchOrder = async (id: number) => {
  loading.value = true
  try {
    const res = await getGovWorkOrderDetailUsingGet({ id })
    if (res.code === 0) {
      order.value = res.data
    } else {
      message.error(res.message || '获取工单失败')
    }
  } catch (error) {
    message.error('获取工单失败')
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    const res = await processGovWorkOrderUsingPost({
      id: order.value.id,
      status: form.value.action
    })
    if (res.code === 0) {
      message.success('处理成功')
      setTimeout(() => {
        router.push('/workOrder')
      }, 1500)
    } else {
      message.error(res.message || '处理失败')
    }
  } catch (error) {
    message.error('处理失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    fetchOrder(id)
  } else {
    message.error('工单ID不存在')
    router.back()
  }
})
</script>

<style scoped>
.process-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.order-info {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.order-info h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.order-info p {
  margin-bottom: 8px;
  color: #666;
}
</style>
