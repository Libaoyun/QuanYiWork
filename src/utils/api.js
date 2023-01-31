import axios from 'axios'
import router from '../router'
import {Message} from "element-ui";
//上面导入message是因为正常情况下this.$message这种方式只能在组件中使用，而js是没有的，因此单独导入即可

/*下面为axios添加拦截器，用于某些错误的统一处理，否则每个地方在axios请求后
* 都要判断响应码然后提示相应信息，太麻烦
*
* 下面的success仅代表请求到了后端接口，而后端返回状态码code就不一定时200了
* 后台可能会判断得出该用户当前操作无权限或其他某些错误，后台也会返回指定的相应码(与前端响应码区分开)
* 因此在处理之前需要统一拦截，做出某些处理后再说*/

axios.interceptors.response.use(success => {
//    业务逻辑错误,这里status是http的，只要请求到了后台接口就是200（也就是success相当于200）
    if (success.status && success.status === 200) {
        //这里不一一列举，简单示意部分后台返回的状态码
        // FIXME 可以反着来，if...code===200则成功，后面统一处理其余后端情况
        if (success.data.code == 500 || success.data.code == 401 || success.data.code == 403) {
            Message.error({message: success.data.message})
            //业务逻辑错误，只需返回空即可，既然错了后面也不会对res进行二次处理
            return ;
        }
        //下面默认后台处理正常，暂定所有的异常在上面的已经处理
        if (success.data) {
            if (success.data.message!="" && success.data.message!=null){
                Message.success({message: success.data.message});
                return success.data;
            }
        }

        //如果上面没出异常，就将结果返回用于二次处理
        return success.data
    }


//    下面error代表连接口都未访问成功，如断网或者服务器挂机,会直接跳转到这里拦截
}, error => {
    if (error.response.code == 504 || error.response.code == 404) {
        //因为这里是接口都未访问，因此后端的提示也无法传递过来，只能自行设定
        Message.error({message: '服务器异常，请稍后重试！'})
    } else if (error.response.code == 403) {
        Message.error({message: '权限不足，请联系管理员！'})
    } else if (error.response.code == 401) {
        Message.error({message: '尚未登录，请登录！'})
        // 跟 router.push 很像，唯一的不同就是，它不会向 history 添加新记录，而是跟它的方法名一样 —— 替换掉当前的 history 记录
        router.replace('/login')
    } else {
        //如果以上响应码都未出现，那就直接将http的相应信息返回
        if (error.response.data.message){
            Message.error({message: error.response.data.message})
        }
        else Message.error({message: '未知错误!'})
    }
    return;
})
//以上内容是响应拦截器

// 下面是请求拦截器
axios.interceptors.request.use(config => {
        //如果已经获取了token，那么就将token放入请求头中
        if (window.sessionStorage.getItem('tokenStr')){
            console.log("token:" + window.sessionStorage.getItem('tokenStr'))
            config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr')
        }
    return config;
}, error => {
    console.log(error)
})






let baseUrl = ''
//下面是get请求的封装，传送json格式的post请求
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${baseUrl}${url}`,
        data: params
    })
}

//下面是post请求的封装，传送json格式的post请求
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${baseUrl}${url}`,
        data: params
    })
}

//下面是put请求的封装，传送json格式的post请求
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${baseUrl}${url}`,
        data: params
    })
}

//下面是delete请求的封装，传送json格式的post请求
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${baseUrl}${url}`,
        data: params
    })
}

