declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseCommunity_ = {
    code?: number
    data?: Community
    message?: string
  }

  type BaseResponseCommunityPostVO_ = {
    code?: number
    data?: CommunityPostVO
    message?: string
  }

  type BaseResponseInfoArticleVO_ = {
    code?: number
    data?: InfoArticleVO
    message?: string
  }

  type BaseResponseInt_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseListCommunityCommentVO_ = {
    code?: number
    data?: CommunityCommentVO[]
    message?: string
  }

  type BaseResponseListCommunityPostVO_ = {
    code?: number
    data?: CommunityPostVO[]
    message?: string
  }

  type BaseResponseListCommunityVO_ = {
    code?: number
    data?: CommunityVO[]
    message?: string
  }

  type BaseResponseListInfoArticleVO_ = {
    code?: number
    data?: InfoArticleVO[]
    message?: string
  }

  type BaseResponseListInfoCategory_ = {
    code?: number
    data?: InfoCategory[]
    message?: string
  }

  type BaseResponseListServiceApplicationVO_ = {
    code?: number
    data?: ServiceApplicationVO[]
    message?: string
  }

  type BaseResponseListServiceCategory_ = {
    code?: number
    data?: ServiceCategory[]
    message?: string
  }

  type BaseResponseListServiceItemVO_ = {
    code?: number
    data?: ServiceItemVO[]
    message?: string
  }

  type BaseResponseListWorkOrderVO_ = {
    code?: number
    data?: WorkOrderVO[]
    message?: string
  }

  type BaseResponseLoginUserVO_ = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseServiceApplicationVO_ = {
    code?: number
    data?: ServiceApplicationVO
    message?: string
  }

  type BaseResponseServiceItemVO_ = {
    code?: number
    data?: ServiceItemVO
    message?: string
  }

  type BaseResponseWorkOrderVO_ = {
    code?: number
    data?: WorkOrderVO
    message?: string
  }

  type Community = {
    address?: string
    communityCode?: string
    communityName?: string
    contactPhone?: string
    createTime?: string
    id?: number
    intro?: string
    isDeleted?: number
    propertyCompany?: string
    status?: number
    updateTime?: string
  }

  type CommunityCommentVO = {
    /** 评论内容 */
    content?: string
    /** 创建时间 */
    createTime?: string
    /** 评论ID */
    id?: number
    /** 当前用户是否点赞 */
    isLiked?: boolean
    /** 点赞次数 */
    likeCount?: number
    /** 父评论ID */
    parentId?: number
    /** 帖子ID */
    postId?: number
    /** 子评论列表 */
    replies?: CommunityCommentVO[]
    /** 评论者头像 */
    userAvatar?: string
    /** 评论者ID */
    userId?: number
    /** 评论者姓名 */
    userName?: string
  }

  type CommunityPostVO = {
    /** 评论次数 */
    commentCount?: number
    /** 所属社区ID */
    communityId?: number
    /** 所属社区名称 */
    communityName?: string
    /** 内容 */
    content?: string
    /** 创建时间 */
    createTime?: string
    /** 帖子ID */
    id?: number
    /** 图片列表 */
    images?: string[]
    /** 当前用户是否点赞 */
    isLiked?: boolean
    /** 点赞次数 */
    likeCount?: number
    /** 标题 */
    title?: string
    /** 类型：1-互助，2-二手，3-活动，4-闲聊 */
    type?: number
    /** 类型名称 */
    typeName?: string
    /** 发布者头像 */
    userAvatar?: string
    /** 发布者ID */
    userId?: number
    /** 发布者姓名 */
    userName?: string
    /** 浏览次数 */
    viewCount?: number
  }

  type CommunityVO = {
    /** 社区地址 */
    address?: string
    /** 社区编码 */
    communityCode?: string
    /** 社区名称 */
    communityName?: string
    /** 联系电话 */
    contactPhone?: string
    /** 社区ID */
    id?: number
    /** 社区简介 */
    intro?: string
    /** 是否已加入 */
    isJoined?: boolean
    /** 物业公司 */
    propertyCompany?: string
  }

  type ComplaintApplyRequest = {
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 附件列表 */
    attachments?: string[]
    /** 详细描述 */
    description?: string
    /** 发生地点 */
    location?: string
    /** 投诉类型：environmental-环境卫生，management-社区管理，facility-设施维护，other-其他 */
    subType?: string
    /** 投诉标题 */
    title?: string
  }

  type createCommentUsingPOSTParams = {
    /** content */
    content: string
    /** parentId */
    parentId?: number
    /** postId */
    postId: number
  }

  type getApplicationDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getArticleDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getArticleListUsingGETParams = {
    /** categoryId */
    categoryId?: number
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
    /** scope */
    scope?: number
  }

  type getCommentsUsingGETParams = {
    /** postId */
    postId: number
  }

  type getCommunityArticlesUsingGETParams = {
    /** categoryId */
    categoryId?: number
    /** communityId */
    communityId: number
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
  }

  type getCommunityDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getMyComplaintsUsingGETParams = {
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
  }

  type getMyHelpsUsingGETParams = {
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
  }

  type getMyProofsUsingGETParams = {
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
  }

  type getOrderDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getPostDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getPostListUsingGETParams = {
    /** communityId */
    communityId?: number
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
    /** type */
    type?: number
  }

  type getServiceDetailUsingGETParams = {
    /** id */
    id: number
  }

  type getServiceListUsingGETParams = {
    /** categoryId */
    categoryId: number
    /** pageNum */
    pageNum?: number
    /** pageSize */
    pageSize?: number
  }

  type getWorkOrderDetailUsingGETParams = {
    /** id */
    id: number
  }

  type HelpApplyRequest = {
    /** 具体地址 */
    address?: string
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 预约时间 */
    appointmentTime?: string
    /** 附件列表 */
    attachments?: string[]
    /** 详细描述 */
    description?: string
    /** 帮助类型：repair-物业报修，consult-政策咨询，elderly-老人关怀，other-其他 */
    subType?: string
  }

  type InfoArticleVO = {
    /** 作者 */
    author?: string
    /** 分类ID */
    categoryId?: number
    /** 分类名称 */
    categoryName?: string
    /** 社区ID（当scope=2时） */
    communityId?: number
    /** 社区名称 */
    communityName?: string
    /** 内容（支持HTML） */
    content?: string
    /** 封面图 */
    cover?: string
    /** 文章ID */
    id?: number
    /** 是否置顶 */
    isTop?: boolean
    /** 点赞次数 */
    likeCount?: number
    /** 发布时间 */
    publishTime?: string
    /** 发布范围：1-全市，2-社区 */
    scope?: number
    /** 来源 */
    source?: string
    /** 摘要 */
    summary?: string
    /** 标签 */
    tag?: string
    /** 标题 */
    title?: string
    /** 阅读次数 */
    viewCount?: number
  }

  type InfoCategory = {
    categoryCode?: string
    categoryName?: string
    createTime?: string
    icon?: string
    id?: number
    isDeleted?: number
    scope?: number
    sortOrder?: number
    status?: number
    updateTime?: string
  }

  type joinCommunityUsingPOSTParams = {
    /** communityId */
    communityId: number
  }

  type leaveCommunityUsingPOSTParams = {
    /** communityId */
    communityId: number
  }

  type LoginUserVO = {
    /** 性别:0-男;1-女 */
    gender?: number
    /** 用户id */
    id?: number
    /** 手机号 */
    phone?: string
    /** 用户状态：0-正常，1-禁用 */
    status?: number
    /** 账号 */
    userAccount?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户昵称 */
    userName?: string
    /** 用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员 */
    userType?: number
  }

  type PostCreateRequest = {
    /** 所属社区ID（可选） */
    communityId?: number
    /** 内容 */
    content?: string
    /** 图片列表 */
    images?: string[]
    /** 标题 */
    title?: string
    /** 类型：1-互助，2-二手，3-活动，4-闲聊 */
    type?: number
  }

  type ProofApplyRequest = {
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 附件列表 */
    attachments?: string[]
    /** 用途说明 */
    purpose?: string
    /** 就读学校 */
    school?: string
    /** 学生姓名 */
    studentName?: string
    /** 证明类型：residence-居住证明，economic-经济状况证明，activity-活动证明，background-政审证明 */
    subType?: string
  }

  type searchArticleUsingGETParams = {
    /** keyword */
    keyword: string
  }

  type searchServiceUsingGETParams = {
    /** keyword */
    keyword: string
  }

  type ServiceApplicationRequest = {
    /** 是否同意用户协议 */
    agreeProtocol?: boolean
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 地区 */
    region?: string
    /** 服务ID */
    serviceId?: number
  }

  type ServiceApplicationVO = {
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 申请单号 */
    applicationNo?: string
    /** 申请时间 */
    createTime?: string
    /** 申请ID */
    id?: number
    /** 地区 */
    region?: string
    /** 服务图标 */
    serviceIcon?: string
    /** 服务ID */
    serviceId?: number
    /** 服务名称 */
    serviceName?: string
    /** 状态：1-已提交 2-审核中 3-已完成 4-已驳回 */
    status?: number
    /** 状态中文描述 */
    statusText?: string
    /** 提交时间 */
    submitTime?: string
  }

  type ServiceCategory = {
    categoryCode?: string
    categoryName?: string
    createTime?: string
    icon?: string
    id?: number
    isDeleted?: number
    sortOrder?: number
    status?: number
    updateTime?: string
  }

  type ServiceItemVO = {
    applyCount?: number
    baseInfo?: Record<string, any>
    briefDesc?: string
    categoryId?: number
    icon?: string
    id?: number
    materialList?: string[]
    processDesc?: string
    serviceCode?: string
    serviceName?: string
    viewCount?: number
  }

  type toggleLikeUsingPOSTParams = {
    /** commentId */
    commentId?: number
    /** postId */
    postId?: number
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserRegisterRequest = {
    checkPassword?: string
    userAccount?: string
    userPassword?: string
  }

  type UserUpdatePasswordRequest = {
    /** 确认新密码 */
    checkPassword: string
    /** 新密码 */
    newPassword: string
    /** 原密码 */
    oldPassword: string
  }

  type UserUpdateRequest = {
    /** 性别:0-男;1-女 */
    gender?: number
    /** 手机号 */
    phone?: string
    /** 用户头像URL */
    userAvatar?: string
    /** 用户昵称 */
    userName?: string
  }

  type WorkOrderProcessRequest = {
    /** 工单ID */
    id?: number
    /** 处理结果 */
    result?: string
    /** 处理状态：3-已完成，4-已驳回 */
    status?: number
  }

  type WorkOrderQueryRequest = {
    /** 页码 */
    pageNum?: number
    /** 每页大小 */
    pageSize?: number
    /** 状态：1-待受理，2-处理中，3-已完成，4-已驳回 */
    status?: number
    /** 工单类型：1-社区证明，2-社区帮助，3-投诉建议 */
    type?: number
  }

  type WorkOrderVO = {
    /** 申请人姓名 */
    applicantName?: string
    /** 联系电话 */
    applicantPhone?: string
    /** 附件列表 */
    attachments?: string[]
    /** 详细描述 */
    content?: string
    /** 提交时间 */
    createTime?: string
    /** 表单数据 */
    formData?: Record<string, any>
    /** 处理时间 */
    handleTime?: string
    /** 工单ID */
    id?: number
    /** 工单编号 */
    orderNo?: string
    /** 处理结果 */
    result?: string
    /** 结果文件列表 */
    resultFiles?: string[]
    /** 状态：1-待受理，2-处理中，3-已完成，4-已驳回 */
    status?: number
    /** 状态名称 */
    statusText?: string
    /** 子类型编码 */
    subType?: string
    /** 子类型名称 */
    subTypeName?: string
    /** 申请标题 */
    title?: string
    /** 工单类型：1-社区证明，2-社区帮助，3-投诉建议 */
    type?: number
    /** 类型名称 */
    typeName?: string
  }
}
