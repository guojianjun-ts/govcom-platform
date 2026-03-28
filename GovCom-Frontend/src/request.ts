// src/request.ts
import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
  baseURL: '', // 改为空字符串
  timeout: 30000,
  withCredentials: true,
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    console.log('发送请求:', config.url, config.data)
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    console.log('收到响应:', response.data)
    return response.data
  },
  (error) => {
    console.error('请求失败:', error)
    return Promise.reject(error)
  }
)

export default request
