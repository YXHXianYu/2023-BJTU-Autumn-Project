import Vue from 'vue'
import Router from 'vue-router'
import draft from "@/components/draft";
import allocContract from "@/components/allocContract";
import counterSign from "@/components/counterSign";
import watchContract from "@/components/watchContract";
import signContract from "@/components/signContract";
import iscounterSign from "./components/iscounterSign";
import isWatchContract from "@/components/isWatchContract";
import isSign from "@/components/isSign";
import login from "@/components/login.vue";
//import permission from "@/components/permission.vue";

Vue.use(Router);

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'login',
            component: () => import('./components/login.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('./components/login.vue')
        },

        {
            path: '/register',
            name: '注册',
            component: () => import('./components/register.vue')
        },
        {
            path: '/test1',
            name: 'CommittedContract',
            component: () => import('./components/CommittedContract.vue')
        },
        {
            path: '/test2',
            name: 'CommittedContract2',
            component: () => import('./components/UnCommittedContract.vue')
        },
        {
            path: '/test3',
            name: 'CommittedContract',
            component: () => import('./components/EditContract.vue')
        },
        {
            path: '/manager',
            name: '管理',
            redirect: "/manager/userManager",
            component: () => import('./components/manager.vue'),
            children: [
                {
                    path: 'userManager',
                    name: '用户管理',
                    component: () => import('./components/userManager.vue')
                },
                {
                    path: 'permission',
                    name: '起草',
                    component: allocContract,
                },
                {
                    path: 'login',
                    name: '退出登录',
                    redirect: "/login",
                    component: () => import('./components/login.vue'),
                },
                {
                    path: 'allocpermission',
                    name: '分配权限',
                    component: () => import('./components/allocpermission.vue'),
                },
                {
                    path: 'process',
                    name: '查看合同进度',
                    component: () => import('./components/process.vue'),
                },
                {
                    path: 'checklog',
                    name: '查看操作日志',
                    component: () => import('./components/checklog.vue'),
                },
            ]
        },
        {
            path: '/mainFrame',
            name: '主页',
            redirect: '/mainFrame/draft',
            component: () => import('./components/mainFrame.vue'),
            children: [
                {
                    path: 'login',
                    name: '退出登录',
                    redirect: "/login",
                    component: login,
                },
                {
                    path: 'draft',
                    name: '起草合同',
                    component: draft
                },
                {
                    path: 'addCustomer',
                    name: '添加用户',
                    component: () => import('./components/addCustomer.vue'),
                },
                {
                    path: 'uncommitted',
                    name: '待定稿合同',
                    component: () => import('./components/UnCommittedContract.vue')
                },
                {
                    path: 'counterSign',
                    name: '会签',
                    component: counterSign
                },
                {
                    path: 'watchContract',
                    name: '审批',
                    component: watchContract
                },
                {
                    path: 'sign',
                    name: '签订',
                    component: signContract
                },

                {
                    path: 'iscounterSign',
                    name: '已会签',
                    component: iscounterSign
                },
                {
                    path: 'isWatch',
                    name: '已审批',
                    component: isWatchContract
                },
                {
                    path: 'isSign',
                    name: '已会签',
                    component: isSign
                },
            ]
        },
    ]
})
