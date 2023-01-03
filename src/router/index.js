import VueRouter from 'vue-router'
import Vue from 'vue'
Vue.use(VueRouter)


// // 解决报错
// const originalPush = VueRouter.prototype.push
// const originalReplace = VueRouter.prototype.replace
// // 针对 push 方法
// VueRouter.prototype.push = function push (location, onResolve, onReject) {
//     if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
//     return originalPush.call(this, location).catch(err => err)
// }
// // 针对 replace 方法
// VueRouter.prototype.replace = function push (location, onResolve, onReject) {
//     if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
//     return originalReplace.call(this, location).catch(err => err)
// }


const routes = [
    {
        path: '/login',
        name: 'Login',
        //下面这种组件导入类似于懒加载，动态导入，在真正用到时才回去调用组件
        component: () => import('../views/Login'),
        hidden: true
    },
    {
        path: '/',
        redirect: '/login',
        hidden: true
    },

    // {
    //     path: '/home',
    //     name: '导航一',
    //     component: () => import('../views/Home'),
    //     children: [
    //         {
    //             path: '/test1',
    //             name: '选项一',
    //             component: () => import('../views/Test1'),
    //         },
    //         {
    //             path: '/test2',
    //             name: '选项二',
    //             component: () => import('../views/Test2'),
    //         }
    //     ]
    // },

    // {
    //     path: '/*',
    //     name: '404',
    //     component: () => import('../views/404'),
    //     hidden: true
    // },
]

export default new VueRouter({
    mode: 'history',
    routes
})
