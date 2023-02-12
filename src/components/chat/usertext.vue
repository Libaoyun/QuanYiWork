<template>
  <div>
    <div id="uesrtext">
      <textarea style="background: bisque;"
                placeholder="按 Ctrl + Enter 发送"
                v-model="content" v-on:keyup="addMessage">
      </textarea>
    </div>
    <div class="button">
      <el-button size="mini" style="margin-right: 65px; background-color: white" @click="clearContent">
        清空
      </el-button>
      <div>
          <el-button plain
                     type="primary"
                     size="mini"
                     style="background-color: rgb(18,183,245); color: white"
                     @click="handleSendMessage">
            发送
          </el-button>
      </div>
    </div>
  </div>

</template>

<script>
// import {mapState} from 'vuex'

import {mapState} from "vuex";

export default {
  name: 'uesrtext',
  data() {
    return {
      content: '',
      nullTips: true,
    }
  },

  computed: mapState([
    'admins',
    // 'sessions',
    'currentSession'
  ]),

  methods: {
    addMessage(e) {
      // 限制为ctrl+enter+输入框必须有内容
      if (e.ctrlKey && e.keyCode === 13) {
        this.handleSendMessage()
      }
    },

    handleSendMessage(){
      if (this.content.length){
        let msgObj = new Object();
        msgObj.to = this.currentSession.username
        msgObj.content = this.content;
        // 发布消息
        this.$store.state.stomp.send('/ws/chat', {}, JSON.stringify(msgObj))
        this.$store.commit('addMessage', msgObj);
        this.content = ''
      }else {
        this.$notify({
          title: '提示',
          message: '发送内容不能为空!',
          type: 'warning',
          duration: 2000
        });
      }
    },

    clearContent(){
      this.content = ''
    }
  }
}
</script>

<style lang="scss" scoped>
#uesrtext {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 30%;
  border-top: solid 1px #DDD;

  > textarea {
    padding: 10px;
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
  }
}
  .el-button {
    position: absolute;
    bottom: 7px;
    right: 7px;
  }

</style>
