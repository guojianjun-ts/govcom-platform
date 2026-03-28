// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 发表评论 POST /api/community/post/comment/${param0} */
export async function createCommentUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.createCommentUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseInt_>(`/api/community/post/comment/${param0}`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 获取评论列表 GET /api/community/post/comments/${param0} */
export async function getCommentsUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommentsUsingGETParams,
  options?: { [key: string]: any }
) {
  const { postId: param0, ...queryParams } = params
  return request<API.BaseResponseListCommunityCommentVO_>(
    `/api/community/post/comments/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 发布帖子 POST /api/community/post/create */
export async function createPostUsingPost(
  body: API.PostCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/community/post/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取帖子详情 GET /api/community/post/detail/${param0} */
export async function getPostDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPostDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseCommunityPostVO_>(`/api/community/post/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 点赞/取消点赞 POST /api/community/post/like */
export async function toggleLikeUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.toggleLikeUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/community/post/like', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取帖子列表 GET /api/community/post/list */
export async function getPostListUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPostListUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListCommunityPostVO_>('/api/community/post/list', {
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
