<template>
  <div>
    <div class="toolBar">
      <el-input placeholder="请输入角色英文名称" v-model="role.name" class="el-input">
<!--      应Security要求，需要在角色前加上ROLE_，这也是与数据库对应   -->
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input placeholder="请输入角色中文名称" v-model="role.nameZh" class="el-input" @keyup.enter.native = addRole>
      </el-input>
      <el-button icon="el-icon-plus" type="primary" @click="addRole">添加</el-button>
    </div>

    <!--    下面以折叠面板（手风琴）样式展示角色对应的菜单里列表-->
    <div class="mainInfo">
<!--      according是手风琴方式展示，@change是打开折叠面板后的操作； v-model代表当前展开哪个，若-1则全部折叠-->
      <el-collapse v-model="activeName" accordion @change="changeSelect">
<!--               :name是值，title是展示的，最终删改时用的还是name，因为他代表数据实际的内容。       -->
        <el-collapse-item :title="role.nameZh" :key="index"
                          :name="role.id" v-for="(role, index) in roles">
<!--          以卡片形式展示内容-->
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>[&nbsp;{{role.nameZh}}&nbsp;]的可访问菜单</span>
              <el-button style="float: right; padding: 3px 0; color: #ff0000"
                         type="text"
                         icon="el-icon-delete"
                         @click="deleteRole(role)"
              >
                删除角色</el-button>
            </div>
            <!--         以树形控件展示具体嵌套内容     -->
            <el-tree :data="menus"
                     :props="defaultProps"
                     show-checkbox
                     :default-checked-keys="selectedMenus"
                     node-key="id"
                     ref="tree"
                     :key="index"
            >
<!--              props就是具体以哪些数据来展示，名称是什么，子节点又是什么标志，showcheck是展示复选框，
 checkedkeys是默认选中哪些，也就是展示已有的权限，nodekey就是这每一项以什么作为key，选择了id，因为每一项菜单都自己有id
    ref是因为要获取所选中的选项，就必须将该结点设为可选中结点，因此用ref可以选中. key是为了添加角色后不会默认显示所有权限，否则会重复利用之前选过的-->
            </el-tree>
            <div style="display: flex; justify-content: flex-end">
              <el-button size="small" @click="activeName = -1">取消修改</el-button>
              <el-button size="small" type="primary" @click="handleUpdateMenus(role.id, index)">确认修改</el-button>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
export default {
  name: "PermissMana",
  data(){
    return{
      role: {
        name: '',
        nameZh: ''
      },
      roles: [],
      //defaultProps默认children（嵌套子节点），label（节点名称），如果键值一致则不用设置，否则需要设置
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      menus: [],
      selectedMenus: [],
      //上面是展开后默认选中的菜单，下面是修改后选中的菜单
      selectKeys:[],
      //使得面板展开某个选项，或全部折叠（-1）
      activeName: -1,
    }
  },

  mounted() {
    this.loadRoleData()
  },

  methods: {
    addRole(){
      if (this.role.name && this.role.nameZh){
        this.postRequest("/api/system/basic/per/role", this.role).then(res => {
          if (res){
            this.loadRoleData()
            this.role.nameZh = ''
            this.role.name = ''
            this.activeName = -1
          }
        })
      }else {
        this.$message.error("请填写所有字段！")
      }
    },

    deleteRole(role){
      this.$confirm('确认删除角色[ ' + role.nameZh + ' ]吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //注意这里后端是/{id}接收参数，因此直接url传原型参数即可,否则需要像下面注释的一行一样?param=data拼接。
        // let id = '?id=' + data.id
        this.deleteRequest('/api/system/basic/per/role/' + role.id).then(res => {
          if (res) {
            this.loadRoleData()
            this.activeName = -1

          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },

    changeSelect(val){
      if (val){
        this.loadMenusData()
        this.loadSelectedMenus(val)
      }

    },

    loadRoleData(){
      this.getRequest("/api/system/basic/per/role").then(res => {
        if (res){
          this.roles = res
        }
      })
    },

    loadMenusData(){
      this.getRequest("/api/system/basic/per/menu").then(res => {
        if (res){
          this.menus = res
        }
      })
    },

    loadSelectedMenus(rid){
      this.getRequest("/api/system/basic/per/menu/" + rid).then(res => {
        if (res){
          this.selectedMenus = res
        }
      })
      },

    handleUpdateMenus(rid, index){
      //因为角色是循环的，因此要确定是哪一个被选中的角色，再选择树形控件
      let tree = this.$refs.tree[index]
      //true代表仅获取叶子结点，否则默认父级结点也会获取，会冗余。
      this.selectKeys = tree.getCheckedKeys(true)
      // eslint-disable-next-line no-unused-vars
      let mids = '?'
      this.selectKeys.forEach(mid => {
        mids += 'mids=' + mid + '&'
      })
      mids = mids.slice(0, mids.length - 1)
      this.putRequest("/api/system/basic/per/role_menu/" + rid + mids).then(res => {
        if (res){
          //无需刷新，只需折叠即可，再展开会自动刷新
          this.activeName = -1
        }
      })
    }
  }
}
</script>

<style>
  .toolBar{
    display: flex;
    justify-content: flex-start;
  }
  .toolBar .el-input{
    width: 280px;
    margin-right: 8px;
  }

  .mainInfo{
    margin-top: 10px;
    width: 650px;
  }
</style>
