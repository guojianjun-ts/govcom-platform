import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/pages/HomePage.vue'),
      meta: { layout: 'main', requiresAuth: false }
    },
    {
      path: '/user/login',
      name: 'UserLogin',
      component: () => import('@/pages/user/UserLoginPage.vue'),
      meta: { layout: 'blank', requiresAuth: false }
    },
    {
      path: '/user/register',
      name: 'UserRegister',
      component: () => import('@/pages/user/UserRegisterPage.vue'),
      meta: { layout: 'blank', requiresAuth: false }
    },
    {
      path: '/user/profile',
      name: 'UserProfile',
      component: () => import('@/pages/user/UserProfilePage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/user/profile/info',
      name: 'UserInfo',
      component: () => import('@/pages/user/UserInfoPage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/user/profile/password',
      name: 'UserPassword',
      component: () => import('@/pages/user/UserPasswordPage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },

    // 政务服务模块
    {
      path: '/gov',
      name: 'Gov',
      component: () => import('@/pages/gov/GovPage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/gov/service/list',
      name: 'GovServiceList',
      component: () => import('@/pages/gov/GovServiceList.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/gov/service/detail/:id',
      name: 'GovServiceDetail',
      component: () => import('@/pages/gov/GovServiceDetail.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/gov/apply/:id',
      name: 'GovApply',
      component: () => import('@/pages/gov/GovApply.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/gov/application/my',
      name: 'GovMyApplications',
      component: () => import('@/pages/gov/GovMyApplications.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/gov/application/detail/:id',
      name: 'GovApplicationDetail',
      component: () => import('@/pages/gov/GovApplicationDetail.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },

    // 资讯中心模块
    {
      path: '/info',
      name: 'Info',
      component: () => import('@/pages/info/InfoCommunityPage.vue'),
      meta: { layout: 'main', requiresAuth: false }
    },
    {
      path: '/info/detail/:id',
      name: 'InfoDetail',
      component: () => import('@/pages/info/InfoDetail.vue'),
      meta: { layout: 'main', requiresAuth: false }
    },

    // 社区管理模块
    {
      path: '/community',
      name: 'Community',
      component: () => import('@/pages/community/CommunityPage.vue'),
      meta: { layout: 'main', requiresAuth: false }
    },
    {
      path: '/community/post/:id',
      name: 'CommunityPostDetail',
      component: () => import('@/pages/community/CommunityPostDetail.vue'),
      meta: { layout: 'main', requiresAuth: false }
    },
    {
      path: '/community/post/create',
      name: 'CommunityPostCreate',
      component: () => import('@/pages/community/CommunityPostCreate.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/community/manage',
      name: 'CommunityManage',
      component: () => import('@/pages/community/CommunityManage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },

    // 便民生活模块
    {
      path: '/life',
      name: 'Life',
      component: () => import('@/pages/life/LifePage.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/life/proof/apply',
      name: 'LifeProofApply',
      component: () => import('@/pages/life/LifeProofApply.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/life/help/apply',
      name: 'LifeHelpApply',
      component: () => import('@/pages/life/LifeHelpApply.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/life/complaint/apply',
      name: 'LifeComplaintApply',
      component: () => import('@/pages/life/LifeComplaintApply.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/life/orders',
      name: 'LifeMyOrders',
      component: () => import('@/pages/life/LifeMyOrders.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },
    {
      path: '/life/order/detail/:id',
      name: 'LifeOrderDetail',
      component: () => import('@/pages/life/LifeOrderDetail.vue'),
      meta: { layout: 'main', requiresAuth: true }
    },

    // ============== 工单处理模块 ==============
    // 工单首页 - 根据用户类型自动跳转
    {
      path: '/workOrder',
      name: 'WorkOrder',
      component: () => import('@/pages/workOrder/WorkOrderDashboard.vue'),
      meta: {
        layout: 'main',
        requiresAuth: true,
        roles: [2, 3, 4]  // 只有政务人员、社区人员、管理员可访问
      }
    },
    // 政务工单 - 政务人员和管理员
    {
      path: '/workOrder/gov',
      name: 'GovWorkOrder',
      component: () => import('@/pages/workOrder/GovWorkOrder.vue'),
      meta: {
        layout: 'main',
        requiresAuth: true,
        roles: [2, 4]  // 政务人员和管理员
      }
    },
    // 社区工单 - 社区人员和管理员
    {
      path: '/workOrder/community',
      name: 'CommunityWorkOrder',
      component: () => import('@/pages/workOrder/CommunityWorkOrder.vue'),
      meta: {
        layout: 'main',
        requiresAuth: true,
        roles: [3, 4]  // 社区人员和管理员
      }
    },
    // 工单详情
    {
      path: '/workOrder/detail/:id',
      name: 'WorkOrderDetail',
      component: () => import('@/pages/workOrder/WorkOrderDetail.vue'),
      meta: {
        layout: 'main',
        requiresAuth: true,
        roles: [2, 3, 4]
      }
    },
    // 工单处理页
    {
      path: '/workOrder/process/:id',
      name: 'WorkOrderProcess',
      component: () => import('@/pages/workOrder/WorkOrderProcess.vue'),
      meta: {
        layout: 'main',
        requiresAuth: true,
        roles: [2, 3, 4]
      }
    },

    // 404页面
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/pages/NotFoundPage.vue')
    }
  ]
})

// 路由守卫 - 权限控制
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const userType = userInfo.userType || 1

  // 需要登录的页面
  if (to.meta.requiresAuth && !token) {
    next('/user/login')
    return
  }

  // 检查角色权限
  if (to.meta.roles) {
    const allowedRoles = to.meta.roles as number[]
    if (!allowedRoles.includes(userType)) {
      // 没有权限，跳转到首页
      next('/')
      return
    }
  }

  next()
})

export default router
