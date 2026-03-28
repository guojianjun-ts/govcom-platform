// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 提交投诉建议 POST /api/life/complaint/submit */
export async function submitComplaintUsingPost(
  body: API.ComplaintApplyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/life/complaint/submit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 提交社区帮助申请 POST /api/life/help/submit */
export async function submitHelpUsingPost(
  body: API.HelpApplyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/life/help/submit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取我的投诉建议列表 GET /api/life/order/complaints */
export async function getMyComplaintsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMyComplaintsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWorkOrderVO_>('/api/life/order/complaints', {
    method: 'GET',
    params: {
      // pageNum has a default value: 1
      pageNum: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取申请详情 GET /api/life/order/detail/${param0} */
export async function getOrderDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseWorkOrderVO_>(`/api/life/order/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取我的社区帮助列表 GET /api/life/order/helps */
export async function getMyHelpsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMyHelpsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWorkOrderVO_>('/api/life/order/helps', {
    method: 'GET',
    params: {
      // pageNum has a default value: 1
      pageNum: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取我的申请列表 POST /api/life/order/list */
export async function getMyOrdersUsingPost(
  body: API.WorkOrderQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWorkOrderVO_>('/api/life/order/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取我的社区证明列表 GET /api/life/order/proofs */
export async function getMyProofsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMyProofsUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWorkOrderVO_>('/api/life/order/proofs', {
    method: 'GET',
    params: {
      // pageNum has a default value: 1
      pageNum: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...params,
    },
    ...(options || {}),
  })
}

/** 提交社区证明申请 POST /api/life/proof/submit */
export async function submitProofUsingPost(
  body: API.ProofApplyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/life/proof/submit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
