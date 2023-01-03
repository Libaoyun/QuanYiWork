// 用于menu菜单初始化，加载菜单数据
import {getRequest} from "./api";

export const initMenu = (router, store) =>{
    // console.log("0101")
    if (store.state.routes.length > 0){
        return ;
    }

    //注意只要项目加了api代理前缀，就必须要加/api注意/也要！！！！
    getRequest("/api/system/cfg/menu").then(data =>{
        if (data){
            //格式化router
            let fmtRoutes = formatRoutes(data)
        //    添加到Router
            router.addRoutes(fmtRoutes)
        //    将路由数据保存到vuex中
            store.commit('initRoutes', fmtRoutes)
        }
    })
}

export const formatRoutes = (routes) => {
    let fmtRoutes = [];
    routes.forEach(router => {
        let {
            path ,
            name ,
            component ,
            iconCls ,
            children
        } = router;
        if (children && children instanceof Array){
        //    递归将子路由也初始化
            children = formatRoutes(children);
        }
        let fmtRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve){
                //ES5的写法，获取实际组件
                if (component.startsWith("Home")){
                    //Home必须要加，因为他是所有一级菜单，不加则跳转没有反应
                    require(['../views/' + component + '.vue'], resolve);
                }else if (component.startsWith("Emp")) {
                    require(['../views/emp/' + component + '.vue'], resolve);
                } else if (component.startsWith("Per")) {
                    require(['../views/per/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sal")) {
                    require(['../views/sal/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sta")) {
                    require(['../views/sta/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sys")) {
                    require(['../views/sys/' + component + '.vue'], resolve);
                } else {
                    require(['../views/404'], resolve);

                }
            }
        }
        fmtRoutes.push(fmtRouter)
    })
    return fmtRoutes;
}
