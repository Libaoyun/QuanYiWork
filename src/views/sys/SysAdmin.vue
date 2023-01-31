<template>
  <div>
    <div style="display: flex; justify-content: center">
      <el-input placeholder="请输入操作员关键词"
                prefix-icon="el-icon-search"
                style="width: 300px; margin-right: 6px"
                v-model="keywords"
                @keyup.enter.native="loadAdminData"
      >
      </el-input>
      <el-button type="primary" @click="loadAdminData">
        搜索
      </el-button>
    </div>

    <div class="main-container">
      <el-card class="box-card" v-for="(admin, index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
          <el-button style="float: right; padding: 3px 0; color: red"
                     type="text"
                     icon="el-icon-delete"
                     @click="deleteAdmin(admin)"
          >删除
          </el-button>
        </div>
        <div>
          <div style="display: flex;justify-content: center">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="imgStyle">
          </div>
          <div class="userInfo">
            <div>用户名: {{ admin.name }}</div>
            <div>手机号码: {{ admin.phone }}</div>
            <div>电话号码: {{ admin.telephone }}</div>
            <div>地址: {{ admin.address }}</div>
            <div>
              用户状态:
              <el-switch
                  v-model="admin.enabled"
                  active-text="启用"
                  inactive-text="禁用"
                  @change="changeStatus(admin)"
              >
              </el-switch>
            </div>
            <div>
              用户角色:
              <el-tag v-for="(role, indexj) in admin.roles" :key="indexj" size="small">{{ role.nameZh }}</el-tag>
              <el-popover
                  placement="right"
                  title="角色列表"
                  width="200"
                  trigger="click"
                  @show="showGetAllRoles(admin)"
                  @hide="updateAdminRoles(admin)"
              >
                <el-button slot="reference" type="text" icon="el-icon-more"></el-button>
                <!--              这里将选择的角色存入到selectedRoles中，并设置多选     -->
                <el-select v-model="selectedRoles" multiple placeholder="请选择用户角色">
                  <el-option
                      v-for="(r, index) in allRoles"
                      :key="index"
                      :label="r.nameZh"
                      :value="r.id">
                  </el-option>
                </el-select>

              </el-popover>
            </div>
            <div>
              备注: {{ admin.remark }}
            </div>
          </div>
        </div>
      </el-card>
    </div>


  </div>

</template>

<script>
export default {
  name: "SysAdmin",
  data() {
    return {
      keywords: '',
      admins: [],
      allRoles: [],
      selectedRoles: [],

    }
  },

  mounted() {
    this.loadAdminData()
  },

  methods: {
    loadAdminData() {
      this.getRequest("/api/system/admin/?keywords=" + this.keywords).then(res => {
        if (res) {
          this.admins = res
        }
      })
    },
    deleteAdmin(admin) {
      if (!admin.isParent) {
        this.$confirm('此操作将永久删除[ ' + admin.name + ' ]操作员, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest("/api/system/admin/" + admin.id).then(res => {
            if (res) {
              //同添加，删除后也要更新当前数据
              this.loadAdminData()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      } else {
        this.$message.error("删除失败!！")
      }
    },

    changeStatus(admin) {
      console.log(admin)
      this.putRequest("/api/system/admin/", admin).then(res => {
        if (res) {
          this.loadAdminData()
        }
      })
    },

    showGetAllRoles(admin) {
      this.selectedRoles = []
      this.getAllRoles()
      let roles = admin.roles
      roles.forEach(role => {
        this.selectedRoles.push(role.id)
      })
    },

    getAllRoles() {
      this.getRequest("/api/system/admin/roles").then(res => {
        if (res) {
          this.allRoles = res;
        }
      })
    },

    updateAdminRoles(admin) {
      // 有必要在更新前判断是否更新，避免无操作收回后还要请求。
      let roles = []
      let i = 0;
      admin.roles.forEach(role => {
        roles.push(role.id)
      })
      //如果admin原本角色中与选择后的角色有差异，那就请求后台，否则不请求
      if (roles.length === this.selectedRoles.length) {
        for (i = 0; i < roles.length; i++) {
          if (this.selectedRoles.indexOf(roles[i]) === -1) {
            break;
          }
        }
        if (i === roles.length) {
          for (i = 0; i < this.selectedRoles.length; i++) {
            if (roles.indexOf(this.selectedRoles[i]) === -1) {
              break;
            }
          }
        }
      }
      // 如果不等，说明上面有不同的内容。
      if (i !== this.selectedRoles.length) {
        this.putRequest("/api/system/admin/role/" + admin.id, this.selectedRoles).then(res => {
          if (res) {
            this.loadAdminData()
          }
        })
      }

    },
  }

}
</script>

<style scoped>

.main-container {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
  flex-wrap: wrap;

}

.box-card {
  width: 325px;
  height: 310px;
  margin-right: 20px;
  margin-top: 8px;
}

.imgStyle {
  width: 50px;
  height: 50px;
  border-radius: 50px;
}

.userInfo {
  font-size: 12px;
  color: #E6A23C;
}

</style>
