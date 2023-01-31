<template>
  <div>
    <el-container style="height: 500px; border: 1px solid #eee">
      <el-header class="header">
        <div class="title">全易在线办公系统</div>
        <el-dropdown class="dropdown" @command = "commandHandler">
          <span class="el-dropdown-link">
            {{ user.name }}<i><img :src="user.userFace" alt="用户头像"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command = "userInfo">用户中心</el-dropdown-item>
            <el-dropdown-item command = "settings">设置</el-dropdown-item>
            <el-dropdown-item command = "logout"> 注销登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <!--          下面的方式是基础，但是比较麻烦，当路由较多时一个个展示冗余，因此可以使用v--for来展示-->
          <!--          <el-menu @select="menuTransform">-->
          <!--            <el-submenu index="1" >-->
          <!--              <template slot="title"><i class="el-icon-location"></i>导航一</template>-->
          <!--              <el-menu-item-group >-->
          <!--                <el-menu-item index="/test1">选项1</el-menu-item>-->
          <!--                <el-menu-item index="/test2">选项2</el-menu-item>-->
          <!--              </el-menu-item-group>-->
          <!--            </el-submenu>-->
          <!--          </el-menu>-->

          <!--        加上router代表启用router导航，那么表示下面的菜单子菜单的index就是路由的路径path，因此可以省略手动点击跳转事件  -->
          <el-menu router
          >   <!-- unique-opnend代表只打开一层路由而不是每次都打开 -->
            <!--            注意v-if和v-for不能在同一级使用，如果一定要一起用那就将v-for提到外层，因为v-if优先级比for高，可能导致获取不到item-->
            <template v-for="(item, index) in routes">
              <!--             index要求字符串形式，而上面的index是number形式，所以加个''改成串即可 -->
              <el-submenu :index="index + ''"
                          :key="index"
                          v-if="!item.hidden"
              >
                <template slot="title">
                  <i :class="item.iconCls" style="color: #1accff; margin-right: 10px"></i>
                  <span>{{ item.name }}</span>
                </template>
                <el-menu-item :index="children.path"
                              v-for="(children, childrenIndex) in item.children"
                              :key="childrenIndex"
                >
                  {{ children.name }}
                </el-menu-item>
              </el-submenu>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
<!--          首页无需展示面包屑，否则有些多余，只需展示欢迎即可-->
          <el-breadcrumb separator-class="el-icon-arrow-right" v-if="$router.currentRoute.path!=='/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{$router.currentRoute.name}}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="welcomeHome" v-if="$router.currentRoute.path==='/home'">
            欢迎来到云E办系统!
          </div>

          <!--          注意这里如果用router-view的话，跳转时必须跳转的是子路由，否则默认会跳转到APP.vue下的router-view，也就是全屏显示了-->
          <router-view class="mainContainer"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

export default {
  name: "home",
  data() {
    return {
      user: JSON.parse(window.sessionStorage.getItem('user'))
    }
  },
  computed: {
    routes() {
      return this.$store.state.routes;
    }
  },

  created() {
    console.log("进入首页！！！！！！！")
  },

  methods: {

    commandHandler(command){
      if (command === "logout"){
        this.$confirm('确认退出登录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.postRequest('api/logout')
          //将用户session数据移除，并跳转至登录页
          window.sessionStorage.removeItem("tokenStr")
          window.sessionStorage.removeItem("user")
          //注意这里还要清空菜单，因为存在vuex中，如果不清空会导致换账户登录时菜单一样！！！而只能刷新页面才能获取，因此需清空
          this.$store.commit('initRoutes', [])
          this.$router.replace('/login')
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消操作'
          });
        });

      }
    }
    // menuTransform(index) {
    //   this.$router.push(index)
    // }
  }
}
</script>

<style scoped>
.header {
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
}

.header .title {
  font-size: 30px;
  font-family: 华文行楷;
  color: white;
}

.dropdown{
  cursor: pointer;
}

.el-dropdown-link img {
  width: 45px;
  height: 45px;
  border-radius: 48px;
  margin-left: 20px;
}

.welcomeHome{
  text-align: center;
  font-size: 30px;
  color: #1accff;
  font-family: '华文行楷';
  margin-top: 80px;
}

.mainContainer{
  margin-top: 15px;
}

</style>
