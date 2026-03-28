<!-- src/pages/user/UserRegisterPage.vue -->
<template>
  <div class="register-container">
    <a-card class="register-card" title="用户注册">
      <a-form
        :model="registerForm"
        :rules="rules"
        @finish="handleSubmit"
      >
        <a-form-item name="userAccount">
          <a-input
            v-model:value="registerForm.userAccount"
            placeholder="请输入账号"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="userPassword">
          <a-input-password
            v-model:value="registerForm.userPassword"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="checkPassword">
          <a-input-password
            v-model:value="registerForm.checkPassword"
            placeholder="请确认密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            :loading="loading"
            block
            size="large"
          >
            注册
          </a-button>
        </a-form-item>

        <div class="login-link">
          <span>已有账号？</span>
          <a-button type="link" @click="goToLogin">立即登录</a-button>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { userRegisterUsingPost } from '@/api/userController'

const router = useRouter()
const loading = ref(false)

const registerForm = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

// 自定义密码确认验证
const validateCheckPassword = async (_rule: any, value: string) => {
  if (value !== registerForm.userPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, message: '账号长度不能小于4位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能小于8位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateCheckPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  loading.value = true
  try {
    const res = await userRegisterUsingPost(registerForm)
    // 注意这里：根据你的返回格式，应该是 res.data.code
    if (res.data.code === 0) {  // 如果 res.code === 0 就改成 res.code
      message.success('注册成功，请登录')
      // 注册成功后跳转到登录页
      router.push('/user/login')
    } else {
      message.error(res.data.message || '注册失败')
    }
  } catch (error: any) {
    console.error('注册错误:', error)
    message.error(error.message || '注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/user/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.login-link {
  text-align: center;
  margin-top: 16px;
}
</style>
