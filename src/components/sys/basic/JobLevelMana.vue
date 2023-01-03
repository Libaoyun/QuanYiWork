<template>
  <div>
    <div>
      <el-input suffix-icon="el-icon-plus"
                style="width: 250px"
                placeholder="请输入待添加职称名称"
                v-model="jobLevel.name"
      >
      </el-input>
      <el-select v-model="jobLevel.titleLevel"
                 placeholder="请选择职称等级"
                 style="margin-left: 8px;margin-right: 8px"
      >
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-button icon="el-icon-plus"
                 type="primary"
                 @click="addJobLevel"
      >
        添加
      </el-button>
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
            label="职称名称"
            width="140">
        </el-table-column>
        <el-table-column
            prop="titleLevel"
            label="职称等级"
            width="140">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建时间"
            width="150">
        </el-table-column>
        <el-table-column
            prop="enabled"
            label="是否启用"
            width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope" >
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
          style="margin-top: 5px"
          @click="handleMultipleDelete"
          :disabled="multipleSelect.length === 0"
          type="danger"
          size="small"
      >
        批量删除
      </el-button>
    </div>
    <el-dialog
        title="编辑职称"
        :visible.sync="dialogVisible"
        width="30%"
    >
      <div>
        <table>
          <tr>
            <td>
              <el-tag>职称名称</el-tag>
              <el-input v-model="setJobLevel.name" style="width: 250px;margin-left: 6px" size="small">
              </el-input>
            </td>
          </tr><br>
          <tr>
            <td>
              <el-tag>职称等级</el-tag>
              <el-select v-model="setJobLevel.titleLevel"
                         placeholder="请选择职称等级"
                         style="margin-left: 6px;"
                         size="small"
              >
                <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.label">
                </el-option>
              </el-select>
            </td>
          </tr><br>
          <tr >
            <td>
              <el-tag>是否启用</el-tag>
              <el-switch
                  style="margin-left: 6px"
                  v-model="setJobLevel.enabled"
                  active-text="启用"
                  inactive-text="未启用">
              </el-switch>
            </td>
          </tr>
        </table>
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
  name: "JobLevelMana",
  data() {
    return {
      jobLevel: {
        name: '',
        titleLevel: '',
      },
      options: [{
        value: '选项1',
        label: '正高级'
      }, {
        value: '选项2',
        label: '副高级'
      }, {
        value: '选项3',
        label: '初级'
      }, {
        value: '选项4',
        label: '中级'
      }, {
        value: '选项5',
        label: '员级'
      }],
      tableData: [],
      multipleSelect: [],
      dialogVisible: false,
      setJobLevel: {
        name: '',
        titleLevel: '',
        enabled: '',
      }

    }
  },
  mounted() {
    this.loadJobLevelData()
  },
  methods: {
    loadJobLevelData() {
      this.getRequest('/api/system/basic/joblevel/').then(res => {
        if (res) {
          this.tableData = res
        }
      })
    },

    handleSelectionChange(val) {
      this.multipleSelect = val
    },

    showEdit(index, data) {
      Object.assign(this.setJobLevel, data)
      this.setJobLevel.createDate = ''
      this.dialogVisible = true
    },

    addJobLevel() {
      // console.log(this.jobLevel)
      if (this.jobLevel.name !== '' && this.jobLevel.name !== '') {
        this.postRequest('/api/system/basic/joblevel/', this.jobLevel).then(res => {
          if (res) {
            this.jobLevel.titleLevel = ''
            this.jobLevel.name = ''
            this.loadJobLevelData()
          }
        })
      } else {
        this.$message.error("请填写所有字段后重试!")
      }
    },

    handleDelete(index, data) {
      this.$confirm('确认删除职称[ ' + data.name + ' ]吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //注意这里后端是/{id}接收参数，因此直接url传原型参数即可,否则需要像下面注释的一行一样?param=data拼接。
        // let id = '?id=' + data.id
        this.deleteRequest('/api/system/basic/joblevel/' + data.id).then(res => {
          if (res) {
            this.loadJobLevelData()
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
        ids = ids.slice(0, ids.length - 1)
        //注意这里后端是直接接收参数，也就是默认的@RequestParam字符串形式，因此需要像上面ids字符串拼接起来。
        this.deleteRequest('/api/system/basic/joblevel/' + ids).then(res => {
          if (res) {
            this.loadJobLevelData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },

    handleUpdate() {
      this.putRequest('/api/system/basic/joblevel/', this.setJobLevel).then(res => {
        if (res) {
          this.dialogVisible = false
          this.loadJobLevelData()
        }
      })
    }


  }
}
</script>

<style scoped>
.moveDown{
  margin-top: 50px;
}


</style>
