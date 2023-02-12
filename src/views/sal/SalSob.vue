<!--    工资账套管理    -->
<template>
  <div>
    <div style="display: flex; justify-content: space-between">
      <el-button icon="el-icon-plus" type="primary" @click="showAddSalary">添加工资账套
      </el-button>
      <el-button icon="el-icon-refresh" type="success" @click="loadSalaryData">刷新
      </el-button>
    </div>
    <div style="margin-top: 8px">
      <el-table
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          :data="salaries"
          border
          stripe
          width="100%"
          :row-class-name="tableRowClassName"
      >
        <el-table-column
            type="selection"
            width="40">
        </el-table-column>
        <el-table-column
            prop="name"
            label="账套名称"
            width="130">
        </el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资"
            width="85">
        </el-table-column>
        <el-table-column
            prop="bonus"
            label="奖金"
            width="80">
        </el-table-column>
        <el-table-column
            prop="lunchSalary"
            label="午餐补助"
            width="85">
        </el-table-column>
        <el-table-column
            prop="trafficSalary"
            label="交通补助"
            width="85">
        </el-table-column>
        <el-table-column
            prop="allSalary"
            label="应发工资"
            width="90">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="启用时间"
            width="120">
        </el-table-column>
        <el-table-column
            label="养老金"
            align="center"
        >
          <el-table-column
              prop="pensionBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="pensionPer"
              label="比率"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="医疗保险"
            align="center"
        >
          <el-table-column
              prop="medicalBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="medicalPer"
              label="比率"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="公积金"
            align="center"
        >
          <el-table-column
              prop="accumulationFundBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="accumulationFundPer"
              label="比率"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
        >
          <template slot-scope="scope">
            <el-button type="primary" style="padding: 7px" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" style="padding: 7px" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <AddSalary
        v-if="showAdd"
        @cancel="cancelDialog"
        @loadData="loadSalaryData"
    >
    </AddSalary>

    <EditSalary
        v-if="showEdit"
        @cancel="cancelDialog"
        @loadData="loadSalaryData"
        :salaryData="editSa"
    >
    </EditSalary>
  </div>
</template>

<script>

import AddSalary from "../../components/salary/AddSalary";
import EditSalary from "../../components/salary/EditSalary";

export default {
  components: {AddSalary, EditSalary},
  name: "SalSob",
  data() {
    return {
      salary: {
        name: '',
        basicSalary: '',
        bonus: '',
        lunchSalary: '',
        trafficSalary: '',
        allSalary: '',
        pensionBase: '',
        pensionPer: '',
        createDate: '',
        medicalBase: '',
        medicalPer: '',
        accumulationFundBase: '',
        accumulationFundPer: '',
      },
      salaries: [],
      showAdd: false,
      loading: true,
      showEdit: false,
      editSa: {},
    }
  },

  mounted() {
    this.loadSalaryData()
  },

  methods: {
    loadSalaryData() {
      this.loading = true
      this.getRequest('/api/sal/sob/').then(res => {
        if (res) {
          this.salaries = res
          this.loading = false
        }
      })
      this.loading = false
    },

    // eslint-disable-next-line no-unused-vars
    tableRowClassName({row, rowIndex}) {
      // 这里暂定表格一共四种颜色，颜色每行错开
      if (rowIndex % 4 === 1) {
        return 'warning-row';
      } else if (rowIndex % 4 === 3) {
        return 'success-row';
      }
      return '';
    },

    showAddSalary() {
      this.showAdd = true
    },

    cancelDialog() {
      this.showAdd = false
      this.showEdit = false
    },

    handleEdit(data) {
      this.editSa = data
      this.showEdit = true
    },

    handleDelete(data) {
      this.$confirm('确认删除工资账套[ ' + data.name + ' ]吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/api/sal/sob/' + data.id).then(res => {
          if (res) {
            this.currentPage = 1
            this.loadSalaryData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },
  }
}
</script>

<style scoped>

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

</style>
