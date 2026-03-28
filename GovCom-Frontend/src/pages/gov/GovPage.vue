<!-- src/pages/gov/GovPage.vue -->
<template>
  <div class="gov-page">
    <!-- 搜索栏 -->
    <div class="search-section">
      <a-input-search
        v-model:value="searchKeyword"
        placeholder="搜索政务服务，如：社保、公积金..."
        size="large"
        @search="onSearch"
      >
        <template #enterButton>
          <a-button type="primary">搜索</a-button>
        </template>
      </a-input-search>
    </div>

    <!-- 分类列表 -->
    <div class="category-section" v-if="categoryList.length > 0">
      <h2 class="section-title">主题分类</h2>
      <a-row :gutter="[16, 16]">
        <a-col :xs="12" :sm="8" :md="6" :lg="6" v-for="item in categoryList" :key="item.id">
          <a-card hoverable class="category-card" @click="goToServiceList(item.id)">
            <div class="category-icon" :style="{ backgroundColor: getCategoryColor(item.id) }">
              <i :class="item.icon || 'fas fa-folder'" class="text-white text-xl"></i>
            </div>
            <div class="category-info">
              <h3>{{ item.categoryName }}</h3>
              <p>{{ item.serviceCount }}项服务</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 热门服务 -->
    <div class="hot-section" v-if="hotServices.length > 0">
      <h2 class="section-title">
        <span>热门服务</span>
        <a-button type="link" @click="goToAllServices">查看更多</a-button>
      </h2>
      <a-row :gutter="[16, 16]">
        <a-col :xs="12" :sm="8" :md="6" v-for="item in hotServices" :key="item.id">
          <a-card hoverable class="hot-card" @click="goToServiceDetail(item.id)">
            <div class="hot-icon">
              <i :class="item.icon || 'fas fa-star'" :style="{ color: getRandomColor() }"></i>
            </div>
            <div class="hot-info">
              <h4>{{ item.serviceName }}</h4>
              <p>{{ item.briefDesc }}</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getGovCategoryListUsingGet, getHotServicesUsingGet, getServiceListUsingGet } from '@/api/govController'

const router = useRouter()
const searchKeyword = ref('')
const categoryList = ref<any[]>([])
const hotServices = ref<any[]>([])

// 获取分类列表
const fetchCategoryList = async () => {
  try {
    const res = await getGovCategoryListUsingGet()
    if (res.code === 0) {
      const categories = res.data || []
      // 为每个分类获取服务数量
      const promises = categories.map(async (cat: any) => {
        try {
          const serviceRes = await getServiceListUsingGet({
            categoryId: cat.id,
            pageNum: 1,
            pageSize: 100 // ✅ 改为较大的值，获取尽可能多的服务
          })
          // 使用返回的 data 长度作为服务数量
          cat.serviceCount = serviceRes.data?.length || 0
        } catch (e) {
          cat.serviceCount = 0
        }
      })
      await Promise.all(promises)
      categoryList.value = categories
    } else {
      message.error(res.message || '获取分类失败')
    }
  } catch (error) {
    message.error('获取分类失败')
  }
}

// 获取热门服务
const fetchHotServices = async () => {
  try {
    const res = await getHotServicesUsingGet()
    console.log('热门服务返回:', res)

    // ✅ 正确判断
    if (res.code === 0) {
      hotServices.value = res.data || []
      console.log('热门服务数据:', hotServices.value)
    } else {
      console.error('获取热门服务失败:', res.message)
      message.error(res.message || '获取热门服务失败')
    }
  } catch (error) {
    console.error('获取热门服务异常:', error)
    message.error('获取热门服务失败')
  }
}

// 搜索方法
const onSearch = () => {
  if (!searchKeyword.value.trim()) {
    message.warning('请输入搜索关键词')
    return
  }
  router.push(`/gov/service/search?keyword=${searchKeyword.value}`)
}

// 跳转到服务列表
const goToServiceList = (categoryId: number) => {
  router.push(`/gov/service/list?categoryId=${categoryId}`)
}

// 跳转到服务详情
const goToServiceDetail = (id: number) => {
  router.push(`/gov/service/detail/${id}`)
}

// 跳转到所有服务
const goToAllServices = () => {
  router.push('/gov/service/list')
}

// 获取分类颜色
const getCategoryColor = (id: number) => {
  const colors = ['#1890ff', '#52c41a', '#faad14', '#722ed1', '#eb2f96', '#13c2c2', '#f5222d', '#fa8c16']
  return colors[(id - 1) % colors.length]
}

// 获取随机颜色
const getRandomColor = () => {
  const colors = ['#1890ff', '#52c41a', '#faad14', '#722ed1', '#eb2f96']
  return colors[Math.floor(Math.random() * colors.length)]
}

onMounted(() => {
  fetchCategoryList()
  fetchHotServices()
})
</script>

<style scoped>
.gov-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin-bottom: 30px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
  font-weight: 500;
  margin-bottom: 20px;
  color: #333;
}

.category-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.category-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px auto 15px;
}

.category-info h3 {
  font-size: 16px;
  margin-bottom: 5px;
  color: #333;
}

.category-info p {
  font-size: 12px;
  color: #999;
}

.hot-card {
  cursor: pointer;
}

.hot-icon {
  font-size: 24px;
  text-align: center;
  margin-bottom: 10px;
}

.hot-info h4 {
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  margin-bottom: 5px;
  color: #333;
}

.hot-info p {
  font-size: 12px;
  color: #999;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-section,
.hot-section {
  margin-bottom: 40px;
}
</style>
