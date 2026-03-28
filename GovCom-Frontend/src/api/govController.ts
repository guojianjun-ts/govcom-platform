// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取申请详情 GET /api/gov/application/detail/${param0} */
export async function getApplicationDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getApplicationDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseServiceApplicationVO_>(`/api/gov/application/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取我的申请列表 GET /api/gov/application/my */
export async function getMyApplicationsUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListServiceApplicationVO_>('/api/gov/application/my', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 提交服务申请 POST /api/gov/application/submit */
export async function submitApplicationUsingPost(
  body: API.ServiceApplicationRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/gov/application/submit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取服务分类列表 GET /api/gov/category/list */
export async function getGovCategoryListUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListServiceCategory_>('/api/gov/category/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取服务详情 GET /api/gov/service/detail/${param0} */
export async function getServiceDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getServiceDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseServiceItemVO_>(`/api/gov/service/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取热门服务 GET /api/gov/service/hot */
export async function getHotServicesUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListServiceItemVO_>('/api/gov/service/hot', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取分类下的服务列表 GET /api/gov/service/list */
export async function getServiceListUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getServiceListUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListServiceItemVO_>('/api/gov/service/list', {
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

/** 搜索服务 GET /api/gov/service/search */
export async function searchServiceUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchServiceUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListServiceItemVO_>('/api/gov/service/search', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
