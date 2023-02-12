//实现代理，通过node.js进行转发从而实现跨域访问

let proxyObj = {}

//  ’/‘代表所有/api开头的路径都代理，或者使用/代表所有路径都代理（但不建议）
proxyObj['/api'] = {
//    websocket代理？这里只是用到了普通代理，ws暂不使用，后期可能会用
    ws: false,

//    目标地址，即要代理到那个地址，一般就是服务器地址
    target: 'http://localhost:8081',

    // 代表发送请求头时候host会被代理设置成target地址，否则就是前端的地址，那就不能跨域
    changeOrigin: true,

    //不重写请求地址，如果后端地址有前缀（即从端口往后的地址）则需要更改，但一般不用
    pathRewrite: {
        '^/api': '/'
    }
}

//上面暂时忽略

module.exports = {
    devServer: {
        host: 'localhost',
        port: 8080,
        // proxy: proxyObj

        //注意，下面这个需要被代理的路径要设置好！！！
        //这样只会在你的请求路径含有/api字样时才会代理成target路径，否则会错乱，刷新也会异常，因为刷新也去请求后端了！！
        proxy: {
            '/api': {
                //    websocket代理？这里只是用到了普通代理，ws暂不使用，后期可能会用
                ws: false,

//    目标地址，即要代理到那个地址，一般就是服务器地址
                target: 'http://localhost:8081',

                // 代表发送请求头时候host会被代理设置成target地址，否则就是前端的地址，那就不能跨域
                changeOrigin: true,

                //不重写请求地址，如果后端地址有前缀（即从端口往后的地址）则需要更改右边值，但一般不用
                pathRewrite: {
                    '^/api': '/'
                }
            },
            '/ws': {
                // 是否为ws代理
                ws: true,
                target: 'ws://localhost:8081'
            }
        }
    }
}
