<!--      员工基本信息管理      -->
<template>
  <div>
    <div style="display: flex; justify-content: space-between">
      <div>
        <el-input prefix-icon="el-icon-search"
                  placeholder="请输入员工关键词"
                  v-model='empName'
                  clearable
                  @clear='loadEmpData'
                  @keydown.enter.native='loadEmpData'
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
        <el-button type="primary" icon="el-icon-plus" @click="showAddEmp">添加员工</el-button>
      </div>
    </div>

    <div style="margin-top: 10px">
      <template>
        <el-table
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
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
          <!--       align = left代表文字左对齐               -->
          <!--       fixed 代表冻结该列，默认左冻结-->
          <el-table-column
              prop="name"
              label="姓名"
              align="left"
              fixed
              width="80">
          </el-table-column>
          <el-table-column
              prop="workID"
              label="工号"
              align="left"
              width="80">
          </el-table-column>
          <el-table-column
              prop="gender"
              label="性别"
              width="50">
          </el-table-column>
          <el-table-column
              prop="birthday"
              label="出生日期"
              width="100">
          </el-table-column>
          <el-table-column
              prop="idCard"
              label="身份证号"
              width="150"
              align="center">
          </el-table-column>
          <el-table-column
              prop="wedlock"
              label="婚姻状况"
              width="70"
              align="center">
          </el-table-column>
          <el-table-column
              prop="nation.name"
              label="民族"
              align="center"
              width="90">
          </el-table-column>
          <el-table-column
              prop="nativePlace"
              label="籍贯"
              width="80">
          </el-table-column>
          <el-table-column
              prop="politicsStatus.name"
              label="政治面貌"
              width="120">
          </el-table-column>
          <el-table-column
              prop="email"
              label="电子邮件"
              align="left"
              width="160">
          </el-table-column>
          <el-table-column
              prop="phone"
              label="电话号码"
              align="left"
              width="110">
          </el-table-column>
          <el-table-column
              prop="address"
              label="家庭住址"
              width="170"
              align="left">
          </el-table-column>
          <el-table-column
              prop="department.name"
              label="所属部门"
              width="100">
          </el-table-column>
          <el-table-column
              prop="joblevel.name"
              label="职称等级"
              width="100">
          </el-table-column>
          <el-table-column
              prop="position.name"
              label="职位名称"
              width="100">
          </el-table-column>
          <el-table-column
              prop="engageForm"
              label="聘用形式"
              width="85">
          </el-table-column>
          <el-table-column
              prop="tiptopDegree"
              label="最高学历"
              width="80">
          </el-table-column>
          <el-table-column
              prop="specialty"
              label="专业"
              width="130">
          </el-table-column>
          <el-table-column
              prop="school"
              label="毕业院校"
              width="120">
          </el-table-column>
          <el-table-column
              prop="beginDate"
              label="入职日期"
              width="110">
          </el-table-column>
          <el-table-column
              prop="workState"
              label="在职状态"
              width="80">
          </el-table-column>
          <el-table-column
              prop="contractTerm"
              label="合同年限"
              width="80">
          </el-table-column>
          <el-table-column
              prop="conversionTime"
              label="转正日期"
              width="110">
          </el-table-column>
          <el-table-column
              prop="beginContract"
              label="合同起始日期"
              width="110">
          </el-table-column>
          <el-table-column
              prop="endContract"
              label="合同终止日期"
              width="110">
          </el-table-column>
          <!--           right代表靠右冻结       -->
          <el-table-column
              label="操作"
              fixed="right"
              width="190">
            <template>
              <el-button style="padding: 4px" size="mini">编辑</el-button>
              <el-button style="padding: 4px" size="mini" type="primary">查看高级资料</el-button>
              <el-button style="padding: 4px" size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="display: flex; justify-content: flex-end; margin-top: 3px">
          <el-pagination
              background
              layout="sizes, prev, pager, next, jumper, ->, total, slot"
              :total="total"
              :page-sizes="[10, 20, 30, 50]"
              @size-change="sizeChange"
              @current-change='changePage'
          >
          </el-pagination>
        </div>
        <!--    添加员工    -->
        <el-dialog
            title="添加员工"
            :visible.sync="dialogVisible"
            width="80%"
        >
          <!--        因为字段较多，如果传统方式可能不美观，因此使用表单+layOut布局
                      而且表单可以进行字段校验              -->
          <el-form ref="empForm" :model="emp">
            <el-row>
              <!--        24分栏，这里使用四列，进行分隔展示    -->
              <el-col :span="6">
                <el-form-item label="姓名:" prop="name">
                  <el-input style="width: 170px"
                            v-model="emp.name"
                            placeholder="请输入员工姓名"
                            prefix-icon="el-icon-edit"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="性别:" prop="gender">
                  <!--               使用单选框组就可以不用为每一个按钮都设置v-model和label绑定值了       -->
                  <el-radio-group v-model="emp.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="出生日期:" prop="birthday">
                  <!--                  日期需要指定格式为yyyy-MM-dd -->
                  <el-date-picker
                      style="width: 170px"
                      v-model="emp.birthday"
                      align="right"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="政治面貌:" prop="politicId">
                  <el-select v-model="emp.politicId" style="width: 200px" clearable placeholder="请选择政治面貌">
                    <el-option
                        v-for="item in politicStatus"
                        :key="item.name"
                        :label="item.name"
                        :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="民族:" prop="nationId">
                  <el-select v-model="emp.nationId"
                             clearable placeholder="请选择民族"
                             style="width: 170px"
                  >
                    <el-option
                        v-for="item in nations"
                        :key="item.name"
                        :label="item.name"
                        :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="籍贯:" prop="nativePlace">
                  <el-input
                      placeholder="请输入籍贯"
                      style="width: 150px"
                      prefix-icon="el-icon-edit"
                      v-model="emp.nativePlace"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电子邮箱:" prop="email">
                  <el-input
                      placeholder="请输入电子邮箱"
                      style="width: 170px"
                      prefix-icon="el-icon-message"
                      v-model="emp.email"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="家庭住址:" prop="address">
                  <el-input
                      style="width: 200px;"
                      placeholder="请输入家庭住址"
                      prefix-icon="el-icon-s-home"
                      v-model="emp.address"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="职位:" prop="posId">
                  <el-select v-model="emp.posId"
                             clearable placeholder="请选择职位"
                             style="width: 170px"
                  >
                    <el-option
                        v-for="item in positions"
                        :key="item.name"
                        :label="item.name"
                        :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="职称:" prop="jobLevelId">
                  <el-select v-model="emp.jobLevelId"
                             clearable placeholder="请选择职称"
                             style="width: 170px"
                  >
                    <el-option
                        v-for="item in jobLevels"
                        :key="item.name"
                        :label="item.name"
                        :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="所属部门:" prop="departmentId">
                  <el-popover
                      placement="right"
                      title="选择所属部门"
                      width="200"
                      trigger="manual"
                      v-model="depVisible">
                    <div>
                      <el-tree :data="departments"
                               :props="defaultProps"
                               @node-click="handleNodeClick">
                      </el-tree>
                    </div>
                    <div
                        slot="reference"
                        style="display: inline-flex;
                            width: 170px;
                            border-radius: 4px;
                            height: 40px;
                            border: 1px solid #DCDFE6;
                            cursor: pointer;
                            padding-left: 12px;
                            box-sizing: border-box;
                            align-items: center"
                        :class="depFontColor"
                        @click="showDepartments"
                    >
                      {{selectedDep}}
                    </div>
<!--                    <el-button slot="reference" @click="showDepartments">手动激活</el-button>-->
                  </el-popover>

                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="电话号码:" prop="phone">
                  <el-input
                      style="width: 200px;"
                      placeholder="请输入电话号码"
                      prefix-icon="el-icon-mobile-phone"
                      v-model="emp.phone"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="工号:" prop="workID">
                  <el-input
                      style="width: 170px;"
                      placeholder="请输入工号"
                      prefix-icon="el-icon-edit"
                      v-model="emp.workID"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="学历:" prop="tiptopDegree">
                  <el-select v-model="emp.tiptopDegree"
                             clearable placeholder="请选择最高学历"
                             style="width: 170px"
                  >
                    <el-option
                        v-for="item in tiptopDegrees"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="毕业院校:" prop="school">
                  <el-input
                      style="width: 170px;"
                      placeholder="请输入毕业院校"
                      prefix-icon="el-icon-edit"
                      v-model="emp.school"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="专业名称:" prop="specialty">
                  <el-input
                      style="width: 200px;"
                      placeholder="请输入专业名称"
                      prefix-icon="el-icon-edit"
                      v-model="emp.specialty"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="入职日期:" prop="beginDate">
                  <el-date-picker
                      style="width: 145px"
                      v-model="emp.beginDate"
                      align="right"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="转正日期:" prop="conversionTime">
                  <el-date-picker
                      style="width: 145px"
                      v-model="emp.conversionTime"
                      align="right"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="合同开始日期:" prop="beginContract">
                  <el-date-picker
                      style="width: 145px"
                      v-model="emp.beginContract"
                      align="right"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="合同截止日期:" prop="endContract">
                  <el-date-picker
                      style="width: 170px"
                      v-model="emp.endContract"
                      align="right"
                      type="date"
                      placeholder="选择日期"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="身份证号码:" prop="idCard">
                  <el-input
                      style="width: 220px;"
                      clearable
                      placeholder="请输入身份证号码"
                      prefix-icon="el-icon-edit"
                      v-model="emp.idCard"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="聘用形式:" prop="engageForm">
                  <el-radio-group v-model="emp.engageForm">
                    <el-radio label="劳动合同">劳动合同</el-radio>
                    <el-radio label="劳务合同">劳务合同</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="婚姻状况:" prop="wedLock">
                  <el-radio-group v-model="emp.wedLock">
                    <el-radio label="已婚">已婚</el-radio>
                    <el-radio label="未婚">未婚</el-radio>
                    <el-radio label="离异">离异</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>


          <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
        </el-dialog>


      </template>
    </div>
  </div>
</template>

<script>

export default {
  name: "EmpBasic",
  data() {
    return {
      tableData: [],
      loading: false,
      currentPage: 1,
      size: 10,
      total: 100,
      empName: '',
      dialogVisible: false,
      emp: {
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedLock: '',
        nationId: null,
        nativePlace: '',
        politicId: '',
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '',
        workID: null,
        contractTerm: '',
        conversionTime: '',
        notWorkDate: '',
        beginContract: '',
        endContract: '',
        workAge: '',
        salaryId: null,
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
      options: [
        {
          value: '选项1',
          label: '黄金糕'
        },
        {
          value: '选项2',
          label: '双皮奶'
        },
        {
          value: '选项3',
          label: '蚵仔煎'
        },
        {
          value: '选项4',
          label: '龙须面'
        },
        {
          value: '选项5',
          label: '北京烤鸭'
        }
      ],
      nations: [],
      politicStatus: [],
      positions: [],
      jobLevels: [],
      departments: [{a: 'aaa', name: 'name'}],
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其他'],
      selectedDep: '点击选择部门',
      depVisible: false,
      depFontColor: 'depNoSelected',
      defaultProps: {
        children: 'children',
        label: 'name'
      }


    }
  },
  mounted() {
    this.loadEmpData();
    this.initData()
  },
  methods: {

    initData() {
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/api/employee/basic/').then(res => {
          if (res) {
            this.nations = res
            window.sessionStorage.setItem("nations", JSON.stringify(res))
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
      }
      if (!window.sessionStorage.getItem("politicStatus")) {
        this.getRequest('/api/employee/basic/').then(res => {
          if (res) {
            this.politicStatus = res
            window.sessionStorage.setItem("politicStatus", JSON.stringify(res))
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem('politicStatus'))
      }
      if (!window.sessionStorage.getItem("jobLevels")) {
        this.getRequest('/api/employee/basic/').then(res => {
          if (res) {
            this.jobLevels = res
            window.sessionStorage.setItem("jobLevels", JSON.stringify(res))
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem('jobLevels'))
      }
      if (!window.sessionStorage.getItem("departments")) {
        this.getRequest('/api/employee/basic/').then(res => {
          if (res) {
            this.departments = res
            window.sessionStorage.setItem("departments", JSON.stringify(res))
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem('jobLevels'))
      }
    },

    getAllPositions(){
      this.getRequest('/api/employee/basic/').then(res => {
        if (res) {
          this.positions = res
        }
      })
    },

    getMaxWorkID(){
      this.getRequest('/api/employee/basic/').then(res => {
        if (res) {
          this.emp.workID = res
        }
      })
    },

    showDepartments(){
      this.depVisible = !this.depVisible;
    },

    handleNodeClick(data){
      this.selectedDep = data.name
      this.depVisible = false
      this.depFontColor = 'depSelected'
    },

    //表格样式
    // eslint-disable-next-line no-unused-vars
    tableRowClassName({row, rowIndex}) {
      // 这里暂定表格一共四种颜色，颜色每行错开
      if (rowIndex % 4 === 1) {
        return 'warning-row';
      } else if (rowIndex % 4 === 3) {
        return 'success-row';
      }
      return '';
    }
    ,

    loadEmpData() {
      this.loading = true
      this.getRequest('/api/employee/basic/?currentPage=' +
          this.currentPage + '&size=' + this.size + '&name=' + this.empName).then(res => {
        if (res) {
          this.tableData = res.data
          this.total = res.total
          this.loading = false
        }
        this.loading = false
      })
    }
    ,

    sizeChange(size) {
      this.size = size
      this.loadEmpData()
    }
    ,

    changePage(page) {
      this.currentPage = page;
      this.loadEmpData()
    }
    ,

    showAddEmp() {
      this.dialogVisible = true
      this.getMaxWorkID()
      this.getAllPositions()
    }
    ,


    handleSelectionChange() {

    }


  }
}
</script>

<!--这里使用el表格样式时，不能加scoped，否则读取不到颜色-->
<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.depNoSelected {
  color: rgb(96, 98, 102, 0.4);
}

.depSelected {
  color: #606266;
}

</style>
