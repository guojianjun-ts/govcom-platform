// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取社区详情 GET /api/community/detail/${param0} */
export async function getCommunityDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommunityDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseCommunity_>(`/api/community/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 加入社区 POST /api/community/join/${param0} */
export async function joinCommunityUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.joinCommunityUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { communityId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/community/join/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 退出社区 POST /api/community/leave/${param0} */
export async function leaveCommunityUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.leaveCommunityUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { communityId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/community/leave/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取所有社区列表 GET /api/community/list */
export async function getCommunityListUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListCommunityVO_>('/api/community/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取我加入的社区 GET /api/community/my */
export async function getMyCommunitiesUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListCommunityVO_>('/api/community/my', {
    method: 'GET',
    ...(options || {}),
  })
}
