<!-- src/pages/user/UserInfoPage.vue -->
<template>
  <div class="user-info-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回个人中心
    </a-button>

    <a-card title="个人信息">
      <a-form
        :model="formData"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="账号">
          <span>{{ formData.userAccount }}</span>
        </a-form-item>

        <a-form-item label="昵称">
          <a-input v-model:value="formData.userName" placeholder="请输入昵称" />
        </a-form-item>

        <a-form-item label="性别">
          <a-radio-group v-model:value="formData.gender">
            <a-radio :value="0">男</a-radio>
            <a-radio :value="1">女</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item label="手机号">
          <a-input v-model:value="formData.phone" placeholder="请输入手机号" />
        </a-form-item>

        <a-form-item label="用户类型">
          <span>{{ getUserTypeText(formData.userType) }}</span>
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-button type="primary" @click="handleSubmit" :loading="loading">
            保存修改
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { updateUserUsingPost } from '@/api/userController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

// 保存原始数据，用于对比哪些字段被修改了
const originalData = ref<any>({})

const formData = reactive({
  userAccount: '',
  userName: '',
  gender: 0,
  phone: '',
  userType: 1
})

const getUserTypeText = (type: number) => {
  const typeMap: Record<number, string> = {
    1: '普通用户',
    2: '政务人员',
    3: '社区管理员',
    4: '系统管理员'
  }
  return typeMap[type] || '未知'
}

const handleSubmit = async () => {
  loading.value = true
  try {
    // 只收集有变化的字段
    const submitData: any = {}

    if (formData.userName !== originalData.value.userName) {
      submitData.userName = formData.userName
    }

    if (formData.gender !== originalData.value.gender) {
      submitData.gender = formData.gender
    }

    // 手机号：只有非空且变化时才发送
    if (formData.phone && formData.phone !== originalData.value.phone) {
      submitData.phone = formData.phone
    }
    console.log('提交的数据:', submitData)  // 添加这行看看
    // 如果没有变化，提示用户
    if (Object.keys(submitData).length === 0) {
      message.info('没有检测到任何修改')
      loading.value = false
      return
    }

    const res = await updateUserUsingPost(submitData)
    if (res.data === true) {
      message.success('修改成功')

      // 更新本地存储
      const currentUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const updatedUserInfo = { ...currentUserInfo, ...formData }
      localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo))
      userStore.updateUserInfo(updatedUserInfo)

      // 更新原始数据
      originalData.value = { ...formData }

      setTimeout(() => {
        router.push('/user/profile')
      }, 1000)
    } else {
      message.error('修改失败')
    }
  } catch (error: any) {
    message.error(error.message || '修改失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  const cached = localStorage.getItem('userInfo')
  if (cached) {
    const user = JSON.parse(cached)
    Object.assign(formData, user)
    // 保存原始数据，用于对比
    originalData.value = { ...user }
  }
})
</script>

<style scoped>
.user-info-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}
</style>
