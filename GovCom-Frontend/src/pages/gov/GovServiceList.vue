<!-- src/pages/gov/GovServiceList.vue -->
<template>
  <div class="service-list-page">
    <!-- 返回按钮 -->
    <a-button type="link" @click="goBack" class="back-btn">
      <left-outlined /> 返回
    </a-button>

    <!-- 分类标题 -->
    <h2 class="page-title">{{ categoryName }}</h2>

    <!-- 服务列表 -->
    <a-list
      :data-source="serviceList"
      :loading="loading"
      :pagination="pagination"
      item-layout="horizontal"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card hoverable class="service-card" @click="goToDetail(item.id)">
            <div class="service-content">
              <div class="service-icon">
                <i :class="item.icon || 'fas fa-file-alt'" style="color: #1890ff"></i>
              </div>
              <div class="service-info">
                <h3>{{ item.serviceName }}</h3>
                <p class="brief">{{ item.briefDesc }}</p>
                <div class="stats">
                  <span><eye-outlined /> {{ item.viewCount || 0 }}</span>
                  <span><file-done-outlined /> {{ item.applyCount || 0 }}人申请</span>
                </div>
              </div>
              <div class="service-action">
                <a-button type="primary" ghost>立即办理</a-button>
              </div>
            </div>
          </a-card>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { LeftOutlined, EyeOutlined, FileDoneOutlined } from '@ant-design/icons-vue'
import { getServiceListUsingGet, getGovCategoryListUsingGet } from '@/api/govController'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const categoryName = ref('')
const serviceList = ref<any[]>([])

// 分页配置
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.value.current = page
    fetchServiceList()
  }
})

// 获取分类名称
const fetchCategoryName = async () => {
  try {
    const res = await getGovCategoryListUsingGet()
    if (res.code === 0) {
      const category = res.data?.find((c: any) => c.id === Number(route.query.categoryId))
      categoryName.value = category?.categoryName || '服务列表'
    }
  } catch (error) {
    categoryName.value = '服务列表'
  }
}

// 获取服务列表
const fetchServiceList = async () => {
  const categoryId = route.query.categoryId
  if (!categoryId) {
    message.error('分类ID不能为空')
    return
  }

  loading.value = true
  try {
    const res = await getServiceListUsingGet({
      categoryId: Number(categoryId),
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize
    })
    console.log('服务列表响应:', res)  // 添加调试日志
    if (res.code === 0) {
      serviceList.value = res.data || []
      // 如果后端返回了总条数，可以使用 res.total；这里暂时用数组长度模拟
      pagination.value.total = serviceList.value.length * 5
    } else {
      message.error(res.message || '获取服务列表失败')
    }
  } catch (error) {
    console.error('获取服务列表异常:', error)
    message.error('获取服务列表失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const goToDetail = (id: number) => {
  router.push(`/gov/service/detail/${id}`)
}

// 监听路由参数变化
watch(() => route.query.categoryId, () => {
  pagination.value.current = 1
  fetchCategoryName()
  fetchServiceList()
})

onMounted(() => {
  fetchCategoryName()
  fetchServiceList()
})
</script>

<style scoped>
.service-list-page {
  max-width: 1200px;
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
.service-card {
  width: 100%;
  cursor: pointer;
}
.service-content {
  display: flex;
  align-items: center;
  gap: 20px;
}
.service-icon {
  width: 60px;
  height: 60px;
  background: #f0f5ff;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}
.service-info {
  flex: 1;
}
.service-info h3 {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}
.service-info .brief {
  color: #666;
  margin-bottom: 8px;
}
.stats {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
}
.stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}
.service-action {
  margin-left: auto;
}
:deep(.ant-list-item) {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}
</style>
