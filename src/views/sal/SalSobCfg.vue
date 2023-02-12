<template>
  <div>
    <el-table
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :data="empSalaries"
        border
        stripe
        width="100%"
    >
      <el-table-column
          type="selection"
          width="40">
      </el-table-column>
      <el-table-column
          prop="name"
          label="员工姓名"
          width="90">
      </el-table-column>
      <el-table-column
          prop="workID"
          label="工号"
          width="110">
      </el-table-column>
      <el-table-column
          prop="email"
          label="电子邮件"
          width="210">
      </el-table-column>
      <el-table-column
          prop="phone"
          label="电话号码"
          width="160">
      </el-table-column>
      <el-table-column
          prop="department.name"
          label="所属部门"
          width="140">
      </el-table-column>
      <el-table-column
          label="工资账套"
          align="center"
          width="220">
        <template slot-scope="scope">
          <el-tooltip placement="right" v-if="scope.row.salary">
            <div slot="content">
              <tr>
                <td>
                  账套名称：
                </td>
                <td>
                  {{scope.row.salary.name}}
                </td>
              </tr>
              <tr>
                <td>
                  基本工资：
                </td>
                <td>
                  {{scope.row.salary.basicSalary}}
                </td>
              </tr>
              <tr>
                <td>
                  奖金：
                </td>
                <td>
                  {{scope.row.salary.bonus}}
                </td>
              </tr>
              <tr>
                <td>
                  午餐补助：
                </td>
                <td>
                  {{scope.row.salary.lunchSalary}}
                </td>
              </tr>
              <tr>
                <td>
                  交通补助：
                </td>
                <td>
                  {{scope.row.salary.trafficSalary}}
                </td>
              </tr>
              <tr>
                <td>
                  养老金基数：
                </td>
                <td>
                  {{scope.row.salary.pensionBase}}
                </td>
              </tr>
              <tr>
                <td>
                  养老金比率：
                </td>
                <td>
                  {{scope.row.salary.pensionPer}}
                </td>
              </tr>
              <tr>
                <td>
                  医疗基数：
                </td>
                <td>
                  {{scope.row.salary.medicalBase}}
                </td>
              </tr>
              <tr>
                <td>
                  医疗保险比率：
                </td>
                <td>
                  {{scope.row.salary.medicalPer}}
                </td>
              </tr>
              <tr>
                <td>
                  公积金基数：
                </td>
                <td>
                  {{scope.row.salary.accumulationFundBase}}
                </td>
              </tr>
              <tr>
                <td>
                  公积金比率：
                </td>
                <td>
                  {{scope.row.salary.accumulationFundPer}}
                </td>
              </tr>
              <tr>
                <td>
                  交通补助：
                </td>
                <td>
                  {{scope.row.salary.trafficSalary}}
                </td>
              </tr>
            </div>
            <el-tag >{{ scope.row.salary.name }}</el-tag>
          </el-tooltip>
          <el-tag v-else type="danger" >暂未设置</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          label="操作"
          align="center"
      >
        <template slot-scope="scope">
          <div>
            <el-popover
                placement="left"
                title="修改工资账套"
                width="200"
                trigger="click"
                @show="showEdit(scope.row.salary)"
                @hide="editEmpSal(scope.row.salary, scope.row.id)"
            >
              <el-select v-model="selectedSal" clearable placeholder="请选择工资账套">
                <el-option
                    v-for="item in salaries"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
              <el-button slot="reference" type="danger">修改工资账套</el-button>
            </el-popover>
          </div>

        </template>
      </el-table-column>
    </el-table>

    <div style="display: flex; justify-content: flex-end; margin-top: 3px">
      <el-pagination
          background
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          :total="page.total"
          :page-sizes="[10, 20, 30, 50]"
          @size-change="sizeChange"
          @current-change='changePage'
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "SalSobCfg",
  data() {
    return {
      loading: false,
      empSal: {
        name: '',
        workID: null,
        email: '',
        phone: '',
        department: {},
        salary: {},
      },
      page: {
        total: null,
        size: 10,
        currentPage: 1
      },
      empSalaries: [],
      salaries: [],
      selectedSal: null,
    }
  },

  mounted() {
    this.loadData()
  },

  methods: {
    loadData() {
      this.loading = true
      this.getRequest('/api/sal/sobcfg/salaries?currentPage='+
          this.page.currentPage+'&size=' + this.page.size).then(res => {
        if (res) {
          this.empSalaries = res.data
          this.page.total = res.total
        }
      })
      this.getRequest('/api/sal/sobcfg/').then(res=>{
        if (res){
          this.salaries = res
        }
      })
      this.loading = false
    },

    showEdit(salary){
      if (salary){
        this.selectedSal = salary.id
      }
      else {
        this.selectedSal = ''
      }
    },

    editEmpSal(salary, eid){
      if (salary && salary.id === this.selectedSal || !this.selectedSal ){
        this.$message("取消编辑")
        return
      }
      this.postRequest('/api/sal/sobcfg/salaries?eid='+eid+'&sid='+this.selectedSal ).then(res => {
        if (res){
          this.loadData()
        }
      })
      this.selectedSal = null
    },

    sizeChange(size) {
      this.page.size = size
      this.loadData()
    },

    changePage(page) {
      this.page.currentPage = page;
      this.loadData()
    },

  }

}
</script>

<style scoped>

</style>
