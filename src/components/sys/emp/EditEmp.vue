<template>
  <div>
    <!--    编辑员工    -->
    <el-dialog
        @close="cancelDialog"
        title="编辑员工"
        :visible.sync="dialogVisible"
        width="80%"
    >
      <!--        因为字段较多，如果传统方式可能不美观，因此使用表单+layOut布局
                  而且表单可以进行字段校验              -->
      <el-form ref="empForm" :model="emp" :rules="rules">
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
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
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
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
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
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
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
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
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
                           default-expand-all
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
                  {{ selectedDep }}
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
                  disabled
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
            <el-form-item label="婚姻状况:" prop="wedlock">
              <el-radio-group v-model="emp.wedlock">
                <el-radio label="已婚">已婚</el-radio>
                <el-radio label="未婚">未婚</el-radio>
                <el-radio label="离异">离异</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>


      <span slot="footer" class="dialog-footer">
              <el-button @click="handleCancelEdit">取 消</el-button>
              <el-button type="primary" @click="handleEditEmp('empForm')">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "addEmp",

  props: {
    empData: Object,
    department: Object
  },

  data() {
    const validateID = (rule, value, callback) => {
      let pattern =
          /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
      if (!pattern.test(value)) {
        callback(new Error("身份证格式不正确!"))
      } else {
        callback()
      }
    };
    return {
      emp: {
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
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
      nations: [],
      politicStatus: [],
      positions: [],
      jobLevels: [],
      departments: [],
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其他'],
      selectedDep: '请选择部门',
      depVisible: false,
      depFontColor: 'depSelected',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      rules: {
        name: [{required: true, message: '请输入员工姓名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入员工性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idCard: [
          {required: true, message: '请输入员工身份证', trigger: 'blur'},
          {validator: validateID, message: '身份证格式不正确!', trigger: 'blur'}
        ],
        wedlock: [{required: true, message: '请选择婚姻状况', trigger: 'blur'}],
        nationId: [{required: true, message: '请选择民族', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请选择政治面貌', trigger: 'blur'}],
        email: [
          {required: true, message: '请输入电子邮件', trigger: 'blur'},
          {type: 'email', message: '电子邮件格式不正确!', trigger: 'blur'}
        ],
        phone: [{required: true, message: '请输入手机号', trigger: 'blur'}],
        address: [{required: true, message: '请输入家庭住址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请选择所属部门', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请选择职称', trigger: 'blur'}],
        posId: [{required: true, message: '请选择职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请选择聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请选择最高学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业名称', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请选择入职日期', trigger: 'blur'}],
        // workState: [{required: true, message: '请选择在职状态', trigger: 'blur'}],
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        // contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请选择转正日期', trigger: 'blur'}],
        // notWorkDate: [{required: true, message: '请输入活动名称', trigger: 'blur'}],
        beginContract: [{required: true, message: '请选择合同开始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请选择合同结束日期', trigger: 'blur'}],
        // workAge: [{required: true, message: '请输入工龄', trigger: 'blur'}],
        // salaryId: [{required: true, message: '请输入活动名称', trigger: 'blur'}],
      },
      showEdit: false,
      dialogVisible: true,
      dep: '',
    }
  },

  created() {
    // 必须深拷贝，否则关联界面的数据，直接DOM实时更新了
    Object.assign(this.emp, this.empData)
    this.initData()
    this.getAllPositions()
  },

  mounted() {
    this.getDepName()
  },

  methods: {

    getDepName() {
      if (this.emp.departmentId) {
        this.selectedDep = this.department.name
      } else {
        this.selectedDep = '请选择部门'
      }
    },

    cancelDialog() {
      this.depVisible = false
      this.cancelEdit()
    },

    showDepartments() {
      this.depVisible = !this.depVisible;
    },

    handleNodeClick(data) {
      this.selectedDep = data.name
      this.emp.departmentId = data.id
      this.depVisible = false
      this.depFontColor = 'depSelected'
    },

    handleCancelEdit() {
      this.$emit('cancel')
    },

    handleEditEmp(formName) {
      this.$refs[formName].validate(valid => {
        if (valid){
          this.emp.workState = '在职'
          console.log(this.emp)
          this.postRequest('/api/employee/basic/update', this.emp).then(res => {
            if (res) {
              this.depVisible = false
              this.$emit('loadData')
              this.handleCancelEdit()
            }
          })
        }
      })
    },

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

      this.getRequest('/api/employee/basic/nations').then(res => {
        if (res) {
          this.nations = res
          window.sessionStorage.setItem("nations", JSON.stringify(res))
        }
      })

      if (!window.sessionStorage.getItem("politicStatus")) {
        this.getRequest('/api/employee/basic/politic-status').then(res => {
          if (res) {
            this.politicStatus = res
            window.sessionStorage.setItem("politicStatus", JSON.stringify(res))
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('politicStatus'))
      }

      this.getRequest('/api/employee/basic/politic-status').then(res => {
        if (res) {
          this.politicStatus = res
          window.sessionStorage.setItem("politicStatus", JSON.stringify(res))
        }
      })

      if (!window.sessionStorage.getItem("jobLevels")) {
        this.getRequest('/api/employee/basic/job-levels').then(res => {
          if (res) {
            this.jobLevels = res
            window.sessionStorage.setItem("jobLevels", JSON.stringify(res))
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('jobLevels'))
      }

      this.getRequest('/api/employee/basic/job-levels').then(res => {
        if (res) {
          this.jobLevels = res
          window.sessionStorage.setItem("jobLevels", JSON.stringify(res))
        }
      })

      if (!window.sessionStorage.getItem("departments")) {
        this.getRequest('/api/employee/basic/departments').then(res => {
          if (res) {
            this.departments = res
            window.sessionStorage.setItem("departments", JSON.stringify(res))
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
      }

      this.getRequest('/api/employee/basic/departments').then(res => {
        if (res) {
          this.departments = res
          window.sessionStorage.setItem("departments", JSON.stringify(res))
        }
      })

    },

    getAllPositions() {
      this.getRequest('/api/employee/basic/positions').then(res => {
        if (res) {
          this.positions = res
        }
      })
    },


  }
}
</script>

<style scoped>

.depSelected {
  color: #606266;
}

</style>
