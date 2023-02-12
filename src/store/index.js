import Vue from 'vue'
import Vuex from 'vuex'
import {getRequest} from "../utils/api";

import SockJs from 'sockjs-client'
import Stomp from 'stompjs'
import { Notification } from 'element-ui';

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({

    state: {
        routes: [],
        admins: [],
        stomp: null,
        // sessions格式为: {'zhangsan#lisi': {}},
        sessions: {},
        currentAdmin: JSON.parse(window.sessionStorage.getItem('user')),
        // 是否未读，红点提示
        notRead: {},

        // 改为-1，这样就不能选取自己了
        currentSession: null,
        filterKey: ''
    },

    getters: {},

    mutations: {
        getAllAdmins(state){
            getRequest('/api/system/admin/').then(res=>{
                if (res){
                    state.admins = res
                }
            })
        },

        initRoutes(state, data) {
            state.routes = data;
        },
        changeCurrentSession(state, currentSession) {
            state.currentSession = currentSession;
            Vue.set(state.notRead, state.currentAdmin.username+'#'+currentSession.username, false)
        },
        // 发送消息
        addMessage(state, msg) {
            // 以之前自定义格式记录内容，将a#b作为键，内容作为值
            let mss = state.sessions[state.currentAdmin.username + '#' + msg.to]
            // 如果之前没有聊过天，那就先定义为空
            if (!mss){
                // state.sessions[state.currentAdmin.username + '#' + msg.to] = []
                Vue.set(state.sessions, state.currentAdmin.username + '#' + msg.to, [])
            }
            state.sessions[state.currentAdmin.username + '#' + msg.to].push({
                content: msg.content,
                date: new Date(),
                // self用于判断是否是我发的，发送消息时没有该属性那!msg.notSelf就位true
                self: !msg.notSelf
            })
        },
        INIT_DATA(state) {
            let data = localStorage.getItem('vue-chat-session');
            //console.log(data)
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },

    },

    actions: {
        initData(context) {
            context.commit('INIT_DATA')
        },
        initAdmins(context){
            context.commit('getAllAdmins')
        },
        connect(context){
            // 连接后端端点,客户端就用client，如果是node环境那就用overWS方法
            context.state.stomp = Stomp.client('ws://localhost:8081/ws/ep');
            // 由于使用到了JWT，因此要携带
            let token = window.sessionStorage.getItem('tokenStr')
            context.state.stomp.connect({'Auth-Token': token }, success=>{
                // 若接成功，那就订阅频道,/user前缀必须要加
                // 订阅的频道就是后端中转的路径destinaiton
                context.state.stomp.subscribe('/user/queue/chat', msg=>{
                    // 接收消息
                    let receiveMsg = JSON.parse(msg.body)
                    // 提示收到信息，使用EL组件，展示最多前十个字符
                    if (!context.state.currentSession ||
                        receiveMsg.from !== context.state.currentSession.username) {
                        Notification.info({
                            title: '【' + receiveMsg.from + '】发来一条消息！',
                            message: receiveMsg.content.length > 10 ?
                                receiveMsg.content.substr(0, 10) + '...' : receiveMsg.content,
                            position: 'bottom-right'
                        })
                        Vue.set(context.state.notRead, context.state.currentAdmin.username+'#'+receiveMsg.from, true)
                    }
                    // 因为是接收消息，所以不是自己发的
                    receiveMsg.notSelf = true;
                    receiveMsg.to = receiveMsg.from;
                    context.commit('addMessage', receiveMsg)

                })
            }, error=>{
            })
        }
    }
})

store.watch(function (state) {
    return state.sessions
}, function (val) {
    console.log('CHANGE: ', val);
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})


export default store;
/*
数据双向存储，比如a发给b，那么a会记录a->b，isSelf:true，因为是自己发的。然后b肯定接收记录消息，那么b记录b->a，isSelf=false，他要转成他和a的消息，只不过不是自己发的。
这样在记录消息时才会完整，a和b会有一样的数据，只不过isSelf会相反，便于展示消息时的“我”和对方分开展示。
可能是因为自己发送的不会被自己订阅，因此不会保留2*记录。
*/

