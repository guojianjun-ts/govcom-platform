// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取轮播图资讯（置顶的前几条） GET /api/info/article/banner */
export async function getBannerArticlesUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListInfoArticleVO_>('/api/info/article/banner', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取资讯详情 GET /api/info/article/detail/${param0} */
export async function getArticleDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getArticleDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseInfoArticleVO_>(`/api/info/article/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取资讯列表（根据登录状态返回不同内容） GET /api/info/article/list */
export async function getArticleListUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getArticleListUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListInfoArticleVO_>('/api/info/article/list', {
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

/** 搜索资讯 GET /api/info/article/search */
export async function searchArticleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchArticleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListInfoArticleVO_>('/api/info/article/search', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取资讯分类列表 GET /api/info/category/list */
export async function getInfoCategoryListUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListInfoCategory_>('/api/info/category/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取指定社区的资讯（需是该社区成员） GET /api/info/community/${param0} */
export async function getCommunityArticlesUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommunityArticlesUsingGETParams,
  options?: { [key: string]: any }
) {
  const { communityId: param0, ...queryParams } = params
  return request<API.BaseResponseListInfoArticleVO_>(`/api/info/community/${param0}`, {
    method: 'GET',
    params: {
      // pageNum has a default value: 1
      pageNum: '1',
      // pageSize has a default value: 10
      pageSize: '10',
      ...queryParams,
    },
    ...(options || {}),
  })
}
