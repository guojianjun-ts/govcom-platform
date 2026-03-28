<!-- src/pages/workOrder/GovWorkOrder.vue -->
<template>
  <div class="workorder-page">
    <div class="page-header">
      <h2 class="page-title">政务工单处理</h2>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <a-select
        v-model:value="filters.type"
        placeholder="工单类型"
        style="width: 150px"
        allow-clear
        @change="handleFilterChange"
      >
        <a-select-option :value="1">社区证明</a-select-option>
        <a-select-option :value="2">社区帮助</a-select-option>
        <a-select-option :value="3">投诉建议</a-select-option>
      </a-select>

      <a-select
        v-model:value="filters.status"
        placeholder="工单状态"
        style="width: 150px; margin-left: 16px"
        allow-clear
        @change="handleFilterChange"
      >
        <a-select-option :value="1">待受理</a-select-option>
        <a-select-option :value="2">处理中</a-select-option>
        <a-select-option :value="3">已完成</a-select-option>
        <a-select-option :value="4">已驳回</a-select-option>
      </a-select>

      <a-button type="primary" style="margin-left: 16px" @click="resetFilters">
        重置
      </a-button>
    </div>

    <!-- 工单列表 -->
    <a-table
      :columns="columns"
      :data-source="orderList"
      :loading="loading"
      :pagination="pagination"
      row-key="id"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'type'">
          {{ record.typeName }}
        </template>

        <template v-else-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ record.statusText }}
          </a-tag>
        </template>

        <template v-else-if="column.key === 'action'">
          <a-button type="link" size="small" @click="viewDetail(record.id)">
            查看详情
          </a-button>
          <a-button
            v-if="record.status === 1"
            type="link"
            size="small"
            @click="acceptOrder(record)"
          >
            受理
          </a-button>
          <a-button
            v-if="record.status === 2"
            type="link"
            size="small"
            @click="processOrder(record)"
          >
            处理
          </a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getGovWorkOrdersUsingPost } from '@/api/workOrderController'

const router = useRouter()
const loading = ref(false)
const orderList = ref<any[]>([])

const filters = reactive({
  type: undefined,
  status: undefined
})

const columns = [
  { title: '工单编号', dataIndex: 'orderNo', key: 'orderNo', width: 180 },
  { title: '申请人', dataIndex: 'applicantName', key: 'applicantName' },
  { title: '工单类型', key: 'type', width: 100 },
  { title: '标题', dataIndex: 'title', key: 'title', ellipsis: true },
  { title: '提交时间', dataIndex: 'createTime', key: 'createTime', width: 150 },
  { title: '状态', key: 'status', width: 100 },
  { title: '操作', key: 'action', width: 150, fixed: 'right' }
]

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

const fetchOrders = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.pageSize,
      type: filters.type,
      status: filters.status
    }
    const res = await getGovWorkOrdersUsingPost(params)
    if (res.code === 0) {
      orderList.value = res.data || []
      pagination.value.total = orderList.value.length * 5 // 临时处理，实际应从后端返回 total
    }
  } catch (error) {
    message.error('获取工单列表失败')
  } finally {
    loading.value = false
  }
}

const handleFilterChange = () => {
  pagination.value.current = 1
  fetchOrders()
}

const resetFilters = () => {
  filters.type = undefined
  filters.status = undefined
  pagination.value.current = 1
  fetchOrders()
}

const handleTableChange = (pag: any) => {
  pagination.value.current = pag.current
  pagination.value.pageSize = pag.pageSize
  fetchOrders()
}

const getStatusColor = (status: number) => {
  const colors: Record<number, string> = {
    1: 'orange',
    2: 'blue',
    3: 'green',
    4: 'red'
  }
  return colors[status] || 'default'
}

const viewDetail = (id: number) => {
  router.push(`/workOrder/detail/${id}`)
}

const acceptOrder = (order: any) => {
  // 受理工单，跳转到处理页
  router.push(`/workOrder/process/${order.id}`)
}

const processOrder = (order: any) => {
  router.push(`/workOrder/process/${order.id}`)
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.workorder-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-bar {
  margin-bottom: 24px;
}
</style>
