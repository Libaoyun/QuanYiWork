<template>
  <div class="mainContainer">
    <!--    带搜索框的树形控件   -->
    <el-input
        placeholder="输入关键字进行过滤"
        suffix-icon="el-icon-search"
        v-model="filterText">
    </el-input>

<!--    如果不默认展开全部，去掉default-expand-all-->
    <el-tree
        class="filter-tree"
        :data="depData"
        :props="defaultProps"
        :filter-node-method="filterNode"
        :expand-on-click-node="false"草
        ref="tree">
      <span class="custom-tree-node" style="display: flex;justify-content: space-between;width: 100%" slot-scope="{ node, data }">
        <span>{{ data.name }}</span>
        <span>
          <el-button
              class="depButton"
              type="primary"
              size="mini"
              @click="() => showAddDep(data)">
            添加部门
          </el-button>
          <el-button
              class="depButton"
              type="danger"
              size="mini"
              @click="() => handleRemoveDep(node, data)">
            删除部门
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
        title="添加部门"
        :visible.sync="dialogVisible"
        width="30%"
      >
      <div>
        <table>
          <tr>
            <td>
              <el-tag>上级部门</el-tag>
            </td>
            <td>
              <span style="margin-left: 6px">
                {{parentName}}
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>部门名称</el-tag>
            </td>
            <td>
              <el-input style="margin-left: 6px"
                        size="small"
                        v-model="department.name"
                        placeholder="请输入待添加部门名称...">
              </el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleAddDep">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DepMana",
  data(){
    return{
      filterText: '',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      depData: [],
      dialogVisible: false,
      department: {
        parentId: '',
        name: '',
      },
      parentName: '',

    }
  },
  mounted() {
    this.loadDepData();
  },

  watch: {
    filterText(val) {
      //这一步就是去调用树形控件的:filter-node-method方法，也就是对应的filterNode方法
      this.$refs.tree.filter(val);
    }
  },

  methods: {

    filterNode(value, data) {
      //如果输入框没值，那就正常全部展示。value就是搜索框值，data就是全部数据
      if (!value) return true;
      //如果输入框有值，那就判断是否能在所有数据中找到该关键字，并返回
      return data.name.indexOf(value) !== -1;
    },

    loadDepData(){
      this.getRequest("/api/system/basic/department/").then(res => {
        if (res){
          this.depData = res;
        }
      })
    },

    showAddDep(data){
      this.parentName = data.name
      this.department.parentId = data.id;
      this.dialogVisible = true;
    },

    handleAddDep(){
      if (this.department.name){
        this.postRequest("/api/system/basic/department/", this.department).then(res => {
          if (res){
            this.department.name= '';
            this.department.parentId = -1;
            this.parentName = ''
            // this.loadDepData()
            this.updateAddDataShow(this.depData, res.obj)
            this.dialogVisible = false
          }
        })
      }else {
        this.$message.error("部门名称不能为空!");
      }

    },

    //下面这个方法是用来添加部门后更流畅展示，否则如果只是重新加载所有部门数据，他又会自动默认展开全部或者不展开
    //而我们想要的效果就是添加数据以后直观的看到数据添加到树形控件里面而不再次手动展开。因此需要在部门数据中更改添加
    updateAddDataShow(deps, dep){
      for (let i=0; i<deps.length; i++){
        let d = deps[i]
        if (d.id === dep.parentId){
          d.children = d.children.concat(dep);
          console.log(d.children)
          if(d.children.length>0){
            d.isParent = true;
          }
          d.isParent = true;
          return;
        }else {
          this.updateAddDataShow(d.children, dep)
        }
      }
    },

    handleRemoveDep(node, data){
      if (!data.isParent){
        this.$confirm('此操作将永久删除部门[ '+ data.name + ' ], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest("/api/system/basic/department/" + data.id).then(res => {
            if (res){
              //同添加，删除后也要更新当前数据
              this.updateDelDataShow(null, this.depData, data.id)
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }else {
        this.$message.error("该部门下有子部门，无法删除！")
      }

    },

    //parent是父节点，deps是当前结点的所有数据（初始就是最大节点），id不变，就是待删除的
    updateDelDataShow(parent, deps, id){
      for (let i=0; i<deps.length; i++){
        let d=deps[i];
        if (d.id === id){
          deps.splice(i, 1);
          console.log(deps)
          if (deps.length === 0){
            parent.isParent = false
          }
        }else {
          this.updateDelDataShow(d, d.children, id)
        }
      }
    }


  },
}
</script>

<style scoped>
.mainContainer{
  width: 500px;
}
.depButton{
  /*将mini按钮适当缩小，否则相对来说还会比较大*/
  padding: 5px;
}
</style>
