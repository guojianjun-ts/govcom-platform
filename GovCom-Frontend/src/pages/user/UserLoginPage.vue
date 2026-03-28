<!-- src/pages/user/UserLoginPage.vue -->
<template>
  <div class="login-container">
    <a-card class="login-card" title="用户登录">
      <a-form
        :model="loginForm"
        :rules="rules"
        @finish="handleSubmit"
      >
        <a-form-item name="userAccount">
          <a-input
            v-model:value="loginForm.userAccount"
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
            v-model:value="loginForm.userPassword"
            placeholder="请输入密码"
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
            登录
          </a-button>
        </a-form-item>

        <div class="register-link">
          <span>还没有账号？</span>
          <a-button type="link" @click="goToRegister">立即注册</a-button>
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
import { userLoginUsingPost } from '@/api/userController'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const loginForm = reactive({
  userAccount: '',
  userPassword: ''
})

const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, message: '账号长度不能小于4位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能小于8位', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  loading.value = true
  try {
    const res = await userLoginUsingPost(loginForm)
    console.log('登录响应:', res)  // 添加日志查看实际返回

    // ✅ 直接判断 res.code，而不是 res.data.code
    if (res.code === 0) {
      message.success('登录成功')

      // 保存用户信息
      const userData = res.data  // ✅ 直接使用 res.data
      localStorage.setItem('userInfo', JSON.stringify(userData))
      localStorage.setItem('token', 'logged')

      // 更新 store
      userStore.updateUserInfo(userData)

      console.log('登录成功，用户信息:', userData)

      // 跳转到首页
      router.push('/')
    } else {
      message.error(res.message || '登录失败')
    }
  } catch (error) {
    console.error('登录异常:', error)
    message.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/user/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.register-link {
  text-align: center;
  margin-top: 16px;
}
</style>
