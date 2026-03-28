<!-- src/pages/user/UserPasswordPage.vue -->
<template>
  <div class="user-password-page">
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回个人中心
    </a-button>

    <a-card title="修改密码">
      <a-form
        :model="formData"
        :rules="rules"
        ref="formRef"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="原密码" name="oldPassword">
          <a-input-password v-model:value="formData.oldPassword" placeholder="请输入原密码" />
        </a-form-item>

        <a-form-item label="新密码" name="newPassword">
          <a-input-password v-model:value="formData.newPassword" placeholder="请输入新密码" />
        </a-form-item>

        <a-form-item label="确认密码" name="checkPassword">
          <a-input-password v-model:value="formData.checkPassword" placeholder="请再次输入新密码" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-button type="primary" @click="handleSubmit" :loading="loading">
            确认修改
          </a-button>
        </a-form-item>
      </a-form>

      <div class="tips">
        <h4>密码要求：</h4>
        <ul>
          <li>长度至少8位</li>
          <li>建议包含字母和数字</li>
          <li>请勿使用过于简单的密码</li>
        </ul>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined } from '@ant-design/icons-vue'
import { updatePasswordUsingPost } from '@/api/userController'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const formData = reactive({
  oldPassword: '',
  newPassword: '',
  checkPassword: ''
})

const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能小于8位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: async (_rule: any, value: string) => {
        if (value !== formData.newPassword) {
          return Promise.reject('两次输入的密码不一致')
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    const res = await updatePasswordUsingPost(formData)
    if (res.data === true) {
      message.success('密码修改成功，请重新登录')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
      setTimeout(() => {
        router.push('/user/login')
      }, 1500)
    } else {
      message.error(res.data?.message || '修改失败')
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
</script>

<style scoped>
.user-password-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  margin-bottom: 20px;
  padding: 0;
}

.tips {
  margin-top: 30px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.tips h4 {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
}

.tips ul {
  margin: 0;
  padding-left: 20px;
  color: #666;
}

.tips li {
  margin: 4px 0;
}
</style>
