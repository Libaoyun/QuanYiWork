<template>
  <div>
    <div style="display: flex; justify-content: space-between">
      <div>
        <el-input prefix-icon="el-icon-search" placeholder="请输入员工关键词"
                  v-model='aa'
                  style="width: 300px; margin-right: 6px"
        ></el-input>
        <el-button type="primary" icon="el-icon-search">搜索</el-button>
        <el-button type="primary"><i class="fa fa-angle-double-down"></i>高级搜索</el-button>
      </div>
      <div>
        <el-button type="success">
          <i class="fa fa-level-up" aria-hidden="true"></i>导入数据
        </el-button>
        <el-button type="success">
          <i class="fa fa-level-down"></i>导出数据
        </el-button>
        <el-button type="primary" icon="el-icon-plus">添加员工</el-button>
      </div>
    </div>

    <div style="margin-top: 10px">
      <template>
        <el-table
            :data="tableData"
            size="small"
            border
            style="width: 100%"
            :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="date"
              label="日期"
              width="180">
          </el-table-column>
          <el-table-column
              prop="name"
              label="姓名"
              width="180">
          </el-table-column>
          <el-table-column
              prop="address"
              label="地址">
          </el-table-column>
        </el-table>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄',
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄',
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }]
    }
  },
  mounted() {

  },
  methods: {
    //表格样式
    // eslint-disable-next-line no-unused-vars
    tableRowClassName({row, rowIndex}) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    }

  }
}
</script>

<!--这里使用el表格样式时，不能加scoped，否则读取不到颜色-->
<style >
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

</style>
