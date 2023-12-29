import Vue from 'vue'
import Router from 'vue-router'
//import permission from "@/components/permission.vue";

Vue.use(Router);

const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'default',
            component: () => import('./components/main.vue')
        }, {
            path: '/login',
            name: 'login',
            component: () => import('./components/login.vue')
        }, {
            path: '/register',
            name: '注册',
            component: () => import('./components/register.vue')
        }, {
            path: '/main',
            name: 'main',
            component: () => import('./components/main.vue'),
        }, {
            path: '/registerProtectedUser',
            name: '注册受限用户',
            component: () => import('./components/registerProtectedUser.vue')
        }, {
            path: '/problem',
            name: '问题',
            component: () => import('./components/problem.vue')
        }, {
            path: '/homework',
            name: '作业',
            component: () => import('./components/homework.vue')
        }, {
            path: '/submitHomework',
            name: '提交作业',
            component: () => import('./components/submitHomework.vue')
        }, {
            path: '/rating',
            name: '互评系统',
            component: () => import('./components/rating.vue')
        }
    ]
})

// router.beforeEach((to, from, next) => {
//     const token = sessionStorage.getItem('token')
//     if(token === null && to.path !== '/login' && to.path !== '/register') {
//         next('/login')
//     } else {
//         next()
//     }
// })

export default router