<template>
  <!--  高级搜索员工  -->
  <div style="border: 2px solid #409eff;
            box-sizing: border-box; padding: 3px;
            margin-top: 5px; border-radius: 10px; font-size: 14px; font-size: 13px"
  >
    <el-row>
      <el-col :span="5">
        政治面貌:
        <el-select v-model="advSerInfo.politicId" style="width: 140px" clearable placeholder="请选择政治面貌" size="mini">
          <el-option
              v-for="item in politicStatus"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        民族:
        <el-select
            v-model="advSerInfo.nationId"
            clearable placeholder="请选择民族"
            style="width: 140px"
            size="mini"
        >
          <el-option
              v-for="item in nations"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        职位:
        <el-select v-model="advSerInfo.posId"
                   clearable placeholder="请选择职位"
                   style="width: 140px"
                   size="mini"
        >
          <el-option
              v-for="item in positions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        职称:
        <el-select v-model="advSerInfo.jobLevelId"
                   clearable placeholder="请选择职称"
                   style="width: 140px"
                   size="mini"
        >
          <el-option
              v-for="item in jobLevels"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        聘用形式:
        <el-radio-group v-model="advSerInfo.engageForm">
          <el-radio label="劳动合同">劳动合同</el-radio>
          <el-radio label="劳务合同">劳务合同</el-radio>
        </el-radio-group>
      </el-col>

    </el-row>
    <el-row style="margin-top: 8px">

      <el-col :span="5">
        所属部门:
        <el-popover
            placement="right"
            title="选择所属部门"
            width="300"
            trigger="manual"
            v-model="depVisible">
          <div>
            <el-tree :data="departments"
                     :props="defaultProps"
                     default-expand-all
                     @node-click="searchDepNodeClick">
            </el-tree>
          </div>
          <div
              slot="reference"
              style="display: inline-flex;
                            width: 140px;
                            border-radius: 4px;
                            height: 25px;
                            font-size: 12px;
                            border: 1px solid #DCDFE6;
                            cursor: pointer;
                            padding-left: 15px;
                            box-sizing: border-box;
                            align-items: center"
              :class="depFontColor"
              @click="showDepartments"
          >
            {{selectedDep}}
          </div>
          <!--                    <el-button slot="reference" @click="showDepartments">手动激活</el-button>-->
        </el-popover>
      </el-col>
      <el-col :span="6">
        入职日期:
        <el-date-picker
            style="display: inline-flex; width: 240px"
            v-model="advSerInfo.dateScope"
            type="daterange"
            :unlink-panels= true
            range-separator="至"
            start-placeholder="入职日期"
            end-placeholder="截止日期"
            size="mini"
            value-format="yyyy-MM-dd"
        >
        </el-date-picker>
      </el-col>
      <el-col :span="6" :offset="7" style="display: flex; justify-content: flex-end">
        <el-button size="mini" @click="cancelAdvSer">
          取消
        </el-button>
        <el-button size="mini" type="primary" @click="handleAdvSer">
          搜索
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "AdvSearch",
  data() {
    return {
      advSerInfo: {
        nationId: null,
        politicId: null,
        posId: null,
        jobLevelId: null,
        engageForm: '',
        departmentId: null,
        dateScope: '',
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      nations: [],
      politicStatus: [],
      positions: [],
      jobLevels: [],
      departments: [],
      selectedDep: '点击选择部门',
      depVisible: false,
      depFontColor: 'depNoSelected',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
    }
  },

  mounted() {
    this.initData();
    this.getAllPositions()
  },

  methods: {

    initData() {
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/api/employee/basic/nations').then(res => {
          if (res) {
            this.nations = res
            window.sessionStorage.setItem("nations", JSON.stringify(res))
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
      }

      if (!window.sessionStorage.getItem("politicStatus")) {
        this.getRequest('/api/employee/basic/politic-status').then(res => {
          if (res) {
            this.politicStatus = res
            window.sessionStorage.setItem("politicStatus", JSON.stringify(res))
          }
        })
      } else {
        this.politicStatus = JSON.parse(window.sessionStorage.getItem('politicStatus'))
      }

      if (!window.sessionStorage.getItem("jobLevels")) {
        this.getRequest('/api/employee/basic/job-levels').then(res => {
          if (res) {
            this.jobLevels = res
            window.sessionStorage.setItem("jobLevels", JSON.stringify(res))
          }
        })
      } else {
        this.jobLevels = JSON.parse(window.sessionStorage.getItem('jobLevels'))
      }

      if (!window.sessionStorage.getItem("departments")) {
        this.getRequest('/api/employee/basic/departments').then(res => {
          if (res) {
            this.departments = res
            window.sessionStorage.setItem("departments", JSON.stringify(res))
          }
        })
      } else {
        this.departments = JSON.parse(window.sessionStorage.getItem('departments'))
      }
    },

    getAllPositions() {
      this.getRequest('/api/employee/basic/positions').then(res => {
        if (res) {
          this.positions = res
        }
      })
    },

    searchDepNodeClick (data){
      this.selectedDep = data.name
      this.advSerInfo.departmentId = data.id
      this.depVisible = false
      this.depFontColor = 'depSelected'
    },

    showDepartments(){
      this.depVisible = !this.depVisible;
    },

    cancelAdvSer(){
      this.depVisible = false
      this.$emit('cancel')
    },

    handleAdvSer(){
      this.depVisible = false
      console.log(this.advSerInfo)
      this.$emit('search', this.advSerInfo)
    },
  }
}
</script>

<style scoped>
.depNoSelected {
  color: rgb(96, 98, 102, 0.4);
}

.depSelected {
  color: #606266;
}
</style>
