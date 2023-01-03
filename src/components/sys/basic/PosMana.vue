<template>
  <div>
    <!--    放俩div，一个用于上面的添加框功能，下面用于表格展示数据-->
    <div>
      <el-input v-model="pos.name"
                class="addPosInput"
                placeholder="请输入待添加职位"
                suffix-icon="el-icon-plus"
                @keyup.enter.native="addPosition"
      ></el-input>
      <el-button type="primary" icon="el-icon-plus" @click="addPosition">添加</el-button>
    </div>
    <div class="mainTable">
      <!--      如果需要添加前面勾选框的话，只需表格中加上@selection-change，然后在第一列加一列type="selection"即可-->
      <el-table
          @selection-change="handleSelectionChange"
          :data="tableData"
          stripe
          border
          style="width: 70%">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="id"
            label="编号"
            width="80">
        </el-table-column>
        <el-table-column
            prop="name"
            label="职位名称"
            width="150">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建时间"
            width="250">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="showEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button
          type="danger"
          size="small"
          style="margin-top: 5px"
          :disabled="multipleSelect.length === 0"
          @click="handleMultipleDelete"
      >
        批量删除
      </el-button>
    </div>

    <el-dialog
        title="编辑职位"
        :visible.sync="dialogVisible"
        width="30%"
    >
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="setPosition.name" style="width: 250px; margin-left: 10px">
        </el-input>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleUpdate()">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "PosMana",
  data() {
    return {
      pos: {
        name: '',
      },
      tableData: [],
      dialogVisible: false,
      setPosition: {
        name: '',
      },
      multipleSelect: [],
    }
  },
  mounted() {
    this.loadPositionsData();
  },
  methods: {

    handleSelectionChange(val) {
      this.multipleSelect = val;
    },

    showEdit(index, data) {
      this.dialogVisible = true;
      // this.setPosition = data; 这样会有小问题，导致在输入更新值后原来的值也同步更新，因为这是深拷贝，因此要用下面的浅拷贝才可以
      Object.assign(this.setPosition, data)
      //这里创建日期必须空着，因为后端可能格式和这个有问题，会报错，后端不接收该修改值
      this.setPosition.createDate = ''
    },

    handleUpdate() {
      this.putRequest('/api/system/basic/pos/', this.setPosition).then(res => {
        if (res) {
          this.dialogVisible = false
          this.loadPositionsData()
        }
      })
    },

    handleDelete(index, data) {
      this.$confirm('确认删除职位[ ' + data.name + ' ]吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //注意这里后端是/{id}接收参数，因此直接url传原型参数即可,否则需要像下面注释的一行一样?param=data拼接。
        // let id = '?id=' + data.id
        this.deleteRequest('/api/system/basic/pos/' + data.id).then(res => {
          if (res) {
            this.loadPositionsData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },

    handleMultipleDelete() {
      this.$confirm('确认删除已选的[ ' + this.multipleSelect.length + ' ]条职位吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?'
        this.multipleSelect.forEach(item => {
          ids += 'ids=' + item.id + '&';
        })
        //去除最后一个 & 号，不过不去除也不影响
        ids = ids.slice(0, ids.length-1)
        //注意这里后端是直接接收参数，也就是默认的@RequestParam字符串形式，因此需要像上面ids字符串拼接起来。
        this.deleteRequest('/api/system/basic/pos/' + ids).then(res => {
          if (res) {
            this.loadPositionsData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },

    loadPositionsData() {
      this.getRequest('/api/system/basic/pos/').then(res => {
        if (res) {
          this.tableData = res;
        }
      })
    },
    addPosition() {
      if (this.pos.name) {
        //因为后端接收是以position实体类接受的，也是@RequestBody的json对象因此必须要传json给后端
        this.postRequest('/api/system/basic/pos/', this.pos).then(res => {
          if (res) {
            //添加过后将输入框清空
            this.pos.name = ''
            this.loadPositionsData()
          }
        })
      } else {
        this.$message.error("请填写职位名称后再添加！")
      }

    }
  }
}
</script>

<style scoped>

.addPosInput {
  width: 300px;
  margin-right: 8px;
}

.mainTable {
  margin-top: 10px;
}


</style>
