import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'login',
            component: () => import('@/components/login.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('@/components/login.vue')
        },

        // {
        //     path: '/register',
        //     name: '注册',
        //     component: () => import('./views/register.vue')
        // },
        // {
        //     path: '/test1',
        //     name: 'CommittedContract',
        //     component: () => import('./components/CommittedContract.vue')
        // },
        // {
        //     path: '/test2',
        //     name: 'CommittedContract2',
        //     component: () => import('./components/UnCommittedContract.vue')
        // },
        // {
        //     path: '/test3',
        //     name: 'CommittedContract',
        //     component: () => import('./components/EditContract.vue')
        // },
        // {
        //     path: '/manager',
        //     name: '管理',
        //     redirect: "/manager/userManager",
        //     component: () => import('./components/manager.vue'),
        //     children: [
        //         {
        //             path: 'userManager',
        //             name: '用户管理',
        //             component: () => import('./components/userManager.vue')
        //         },
        //         {
        //             path: 'permission',
        //             name: '起草',
        //             component: allocContract,
        //         },
        //         {
        //             path: 'login',
        //             name: '退出登录',
        //             redirect: "/login",
        //             component: () => import('./components/login.vue'),
        //         },
        //         {
        //             path: 'allocpermission',
        //             name: '分配权限',
        //             component: () => import('./components/allocpermission.vue'),
        //         },
        //         {
        //             path: 'process',
        //             name: '查看合同进度',
        //             component: () => import('./components/process.vue'),
        //         },
        //         {
        //             path: 'checklog',
        //             name: '查看操作日志',
        //             component: () => import('./components/checklog.vue'),
        //         },
        //     ]
        // },
        // {
        //     path: '/mainFrame',
        //     name: '主页',
        //     redirect: '/mainFrame/draft',
        //     component: () => import('./components/mainFrame.vue'),
        //     children: [
        //         {
        //             path: 'login',
        //             name: '退出登录',
        //             redirect: "/login",
        //             component: login,
        //         },
        //         {
        //             path: 'draft',
        //             name: '起草合同',
        //             component: draft
        //         },
        //         {
        //             path: 'addCustomer',
        //             name: '添加用户',
        //             component: () => import('./components/addCustomer.vue'),
        //         },
        //         {
        //             path: 'uncommitted',
        //             name: '待定稿合同',
        //             component: () => import('./components/UnCommittedContract.vue')
        //         },
        //         {
        //             path: 'counterSign',
        //             name: '会签',
        //             component: counterSign
        //         },
        //         {
        //             path: 'watchContract',
        //             name: '审批',
        //             component: watchContract
        //         },
        //         {
        //             path: 'sign',
        //             name: '签订',
        //             component: signContract
        //         },

        //         {
        //             path: 'iscounterSign',
        //             name: '已会签',
        //             component: iscounterSign
        //         },
        //         {
        //             path: 'isWatch',
        //             name: '已审批',
        //             component: isWatchContract
        //         },
        //         {
        //             path: 'isSign',
        //             name: '已会签',
        //             component: isSign
        //         },
        //     ]
        // },
    ]
})


/* Layout */
// import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
// export const constantRoutes = [
//   {
//     path: '/login',
//     component: () => import('@/views/login/index'),
//     hidden: true
//   },

//   {
//     path: '/404',
//     component: () => import('@/views/404'),
//     hidden: true
//   },

//   {
//     path: '/',
//     component: Layout,
//     redirect: '/dashboard',
//     children: [{
//       path: 'dashboard',
//       name: 'Dashboard',
//       component: () => import('@/views/dashboard/index'),
//       meta: { title: 'Dashboard', icon: 'dashboard' }
//     }]
//   },

//   {
//     path: '/example',
//     component: Layout,
//     redirect: '/example/table',
//     name: 'Example',
//     meta: { title: 'Example', icon: 'el-icon-s-help' },
//     children: [
//       {
//         path: 'table',
//         name: 'Table',
//         component: () => import('@/views/table/index'),
//         meta: { title: 'Table', icon: 'table' }
//       },
//       {
//         path: 'tree',
//         name: 'Tree',
//         component: () => import('@/views/tree/index'),
//         meta: { title: 'Tree', icon: 'tree' }
//       }
//     ]
//   },

//   {
//     path: '/form',
//     component: Layout,
//     children: [
//       {
//         path: 'index',
//         name: 'Form',
//         component: () => import('@/views/form/index'),
//         meta: { title: 'Form', icon: 'form' }
//       }
//     ]
//   },

//   {
//     path: '/nested',
//     component: Layout,
//     redirect: '/nested/menu1',
//     name: 'Nested',
//     meta: {
//       title: 'Nested',
//       icon: 'nested'
//     },
//     children: [
//       {
//         path: 'menu1',
//         component: () => import('@/views/nested/menu1/index'), // Parent router-view
//         name: 'Menu1',
//         meta: { title: 'Menu1' },
//         children: [
//           {
//             path: 'menu1-1',
//             component: () => import('@/views/nested/menu1/menu1-1'),
//             name: 'Menu1-1',
//             meta: { title: 'Menu1-1' }
//           },
//           {
//             path: 'menu1-2',
//             component: () => import('@/views/nested/menu1/menu1-2'),
//             name: 'Menu1-2',
//             meta: { title: 'Menu1-2' },
//             children: [
//               {
//                 path: 'menu1-2-1',
//                 component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
//                 name: 'Menu1-2-1',
//                 meta: { title: 'Menu1-2-1' }
//               },
//               {
//                 path: 'menu1-2-2',
//                 component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
//                 name: 'Menu1-2-2',
//                 meta: { title: 'Menu1-2-2' }
//               }
//             ]
//           },
//           {
//             path: 'menu1-3',
//             component: () => import('@/views/nested/menu1/menu1-3'),
//             name: 'Menu1-3',
//             meta: { title: 'Menu1-3' }
//           }
//         ]
//       },
//       {
//         path: 'menu2',
//         component: () => import('@/views/nested/menu2/index'),
//         name: 'Menu2',
//         meta: { title: 'menu2' }
//       }
//     ]
//   },

//   {
//     path: 'external-link',
//     component: Layout,
//     children: [
//       {
//         path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
//         meta: { title: 'External Link', icon: 'link' }
//       }
//     ]
//   },

//   // 404 page must be placed at the end !!!
//   { path: '*', redirect: '/404', hidden: true }
// ]

// const createRouter = () => new Router({
//   // mode: 'history', // require service support
//   scrollBehavior: () => ({ y: 0 }),
//   routes: constantRoutes
// })

// const router = createRouter()

// // Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
// export function resetRouter() {
//   const newRouter = createRouter()
//   router.matcher = newRouter.matcher // reset router
// }

// export default router
