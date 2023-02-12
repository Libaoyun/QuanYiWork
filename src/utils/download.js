import axios from "axios";

// 设置调用该接口的默认返回类型为流形式
const service = axios.create({
    responseType: 'arraybuffer'
})
// 请求拦截器，虽然是下载但还是要登录后才能执行
service.interceptors.request.use(config => {
    config.headers['Authorization'] =
        window.sessionStorage.getItem('tokenStr');
    return config;
}, error => {
    console.log(error);
})

// 响应拦截器，如果使用了该请求但是可能有两种情况
service.interceptors.response.use(resp => {
    const headers = resp.headers;
    // 如果是普通json返回，那就转成正常的json，因为之前改成了流形式。
    let reg = RegExp(/application\/json/);
    if (headers['content-type'].match(reg)){
        resp.data = uintToString(resp.data)
    }else {
        // 否则就根据请求头获取文件信息并下载
        let fileDownload = require('js-file-download')
        let fileName = headers['content-disposition']
            .split(';')[1].split('filename=')[1];
        let contentType = headers['content-type'];
        fileName = decodeURIComponent(fileName)
        fileDownload(resp.data, fileName, contentType);
    }
}, error => {
    console.log(error)
})

// 将之前的流形式先转String然后转回Json形式
function uintToString(uintArray){
    let encodedString = String.fromCharCode.apply(
        null, new Uint8Array(uintArray));
    let decodedString = decodeURIComponent(
        escape(encodedString));
    return JSON.parse(decodedString);
}

// 以便请求路径要加某些前缀
let base = ''
export const downloadRequest = (url, params) => {
    return service({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

export default service;
