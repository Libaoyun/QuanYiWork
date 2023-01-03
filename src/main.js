import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import {getRequest} from "./utils/api";
import {postRequest} from "./utils/api";
import {putRequest} from "./utils/api";
import {deleteRequest} from "./utils/api";

import store from './store'
import {initMenu} from "./utils/menus";
//导入font-awesome可以使用导航菜单的图标
import 'font-awesome/css/font-awesome.css'

//import会自动通过babel转为es5的语法require导入

//开发环境时不提示：当前非生产模式然后xxxx，关闭了浏览器控制台环境相关提示
Vue.config.productionTip = false

Vue.use(ElementUI)
// Vue.use(ElementUI, {size:'small'}) 可以像这样直接全局设置size为small，只要有这个选项默认就会small，但优先级低于内部设置

//下面可以将其设置为全局变量，而不用每次使用都要导入。就是使用时记得用this.xxx指向
Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;

router.beforeEach((to, from, next) => {
  if (window.sessionStorage.getItem('tokenStr')){
    console.log("创建菜单(已登录)")
    initMenu(router,store);
    if (!window.sessionStorage.getItem('user')){
      return getRequest('/api/admin/info').then(res => {
        //存入用户信息
        if (res){
          console.log("获取用户信息(已登录)")
          window.sessionStorage.setItem('user', JSON.stringify(res))
          next();
        }
      })
    }
    next();
  //  如果未登录状态，那就先不用加载路由，也不存信息
  }else {
    //如果未登录过，且要去登录就放行
    if (to.path === '/login'){
      console.log("to == /login(未登录)")
      next();
    }
    //如果未登录过而且要去其他有效地址，那就先跳转至登录页，登录成功后直接跳转到刚才想去的地址而不是直接首页！
    else {
      console.log("to == /重定向参数，(未登录)")
      //注意这里?redirect前面不要加/（如果前面是/login），路由传参无需/，而是直接?后面参数就可以了！！
      next('/login?redirect=' + to.path)
    }
  }
})



new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
