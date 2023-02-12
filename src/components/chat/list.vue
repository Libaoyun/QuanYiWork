<template>
  <div id="list">
    <ul style="padding-left: 0">
      <li :key="item.id" v-for="item in admins"
          :class="{ active: currentSession ?
          item.name === currentSession.name: false }"
          v-on:click="changeCurrentSession(item)"><!--   :class="[item.id === currentSession ? 'active':'']" -->
        <img class="avatar" :src="item.userFace" :alt="item.name">
        <el-badge :is-dot="notRead[user.username+'#'+item.username]">
          <p class="name">{{ item.name }}</p>
        </el-badge>
        <!--        <p class="name">{{ item.name }}</p>-->
      </li>
    </ul>
  </div>
</template>

<script>
import {mapState} from 'vuex'
// 下载两个插件，用于实现WS：npm install sockjs-client      npm install stompjs

export default {
  name: 'list',
  data() {
    return {
      user: JSON.parse(window.sessionStorage.getItem('user')),
    }
  },
  computed: mapState([
    'admins',
    'notRead',
    // 'sessions',
    'currentSession'
  ]),
  methods: {
    changeCurrentSession: function (currentSession) {
      this.$store.commit('changeCurrentSession', currentSession)
    }
  }
}
</script>

<style lang="scss" scoped>
#list {
  li {
    padding: 12px 15px;
    border-bottom: 1px solid #292C33;
    cursor: pointer;

    &:hover {
      background-color: rgba(255, 255, 255, 0.03);
    }
  }

  li.active {
    background-color: rgba(255, 255, 255, 0.1);
  }

  .avatar {
    border-radius: 2px;
    width: 30px;
    height: 30px;
    vertical-align: middle;
  }

  .name {
    display: inline-block;
    margin-left: 15px;
    margin-top: 0px;
    margin-bottom: 0px;
  }
}
</style>
