// src/main.ts
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import { useUserStore } from './stores/userStore'

console.log('main.ts 开始执行')

// 创建 store 实例并验证数据
const userStore = useUserStore()
console.log('main.ts 中 userStore.userInfo:', userStore.userInfo)
console.log('localStorage 中的 userInfo:', localStorage.getItem('userInfo'))

const app = createApp(App)

app.use(router)
app.use(Antd)

app.mount('#app')
console.log('应用已挂载')
