// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取社区工单列表 POST /api/workOrder/community/list */
export async function getCommunityWorkOrdersUsingPost(
  body: API.WorkOrderQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWorkOrderVO_>('/api/workOrder/community/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取工单详情 GET /api/workOrder/detail/${param0} */
export async function getWorkOrderDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWorkOrderDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseWorkOrderVO_>(`/api/workOrder/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取政务工单详情 GET /api/workOrder/gov/detail/${param0} */
export async function getGovWorkOrderDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getGovWorkOrderDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseGovServiceApplicationVO_>(`/api/workOrder/gov/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取政务工单列表 POST /api/workOrder/gov/list */
export async function getGovWorkOrdersUsingPost(
  body: API.WorkOrderQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListGovServiceApplicationVO_>('/api/workOrder/gov/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 处理政务工单 POST /api/workOrder/gov/process */
export async function processGovWorkOrderUsingPost(
  body: API.GovWorkOrderProcessRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/workOrder/gov/process', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 处理工单 POST /api/workOrder/process */
export async function processWorkOrderUsingPost(
  body: API.WorkOrderProcessRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/workOrder/process', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
