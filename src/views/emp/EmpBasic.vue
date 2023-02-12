<!--      员工基本信息管理      -->
<template>
  <div>
    <div style="display: flex; justify-content: space-between">
      <div>
        <el-input prefix-icon="el-icon-search"
                  placeholder="请输入姓名关键词"
                  v-model='searchEmp.name'
                  clearable
                  @clear='loadEmpData'
                  @keydown.enter.native='loadEmpData'
                  style="width: 300px; margin-right: 6px"
        ></el-input>
        <el-button :disabled="showAdvSer" type="primary" icon="el-icon-search" @click="handleAdvSer" >搜索</el-button>
        <el-button type="primary" @click="displayAdvSer">
          <i :class="advSerIcon"></i>高级搜索
        </el-button>
      </div>

      <div>
        <el-upload
            style="display: inline-flex; margin-right: 10px"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="upload.forbidden"
            :show-file-list="false"
            :headers="upload.headers"
            action="/api/employee/basic/import"
            >
          <el-button type="success" :icon="upload.icon" :disabled="upload.forbidden">
           {{upload.text}}
          </el-button>
        </el-upload>

        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
        <el-button type="primary" icon="el-icon-plus" @click="showAddEmp">添加员工</el-button>
      </div>
    </div>
    <Transition name="slide-fade">
      <adv-search
          v-show="showAdvSer"
          @cancel="cancelAdvSer"
          @search="handleAdvSer"
      >
      </adv-search>
    </Transition>

<!--    展示、添加、编辑员工-->
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
            <template slot-scope="scope">
              <el-button style="padding: 4px" size="mini" @click="showEditEmp(scope.row)">编辑</el-button>
              <el-button style="padding: 4px" size="mini" type="primary">查看高级资料</el-button>
              <el-button style="padding: 4px" size="mini" type="danger" @click="handleDelEmp(scope.row)">删除</el-button>
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
            @close="cancelDialog"
            title="添加员工"
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
              <el-button @click="handleCancelAdd">取 消</el-button>
              <el-button type="primary" @click="handleAddEmp('empForm')">确 定</el-button>
            </span>
        </el-dialog>

        <EditEmp
            :empData="editEmp"
            :department = "department"
            @loadData="loadEmpData()"
            v-if="showEdit"
            @cancel="cancelEdit"
        >
        </EditEmp>

      </template>
    </div>
  </div>
</template>

<script>

import EditEmp from "../../components/sys/emp/EditEmp";
import AdvSearch from "../../components/sys/emp/AdvSearch";
export default {
  name: "EmpBasic",
  components: {EditEmp, AdvSearch},
  data() {
    const validateID = (rule, value, callback) => {
      let pattern =
          /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
      if(!pattern.test(value)){
        callback(new Error("身份证格式不正确!"))
      }else {
        callback()
      }
    };

    return {
      tableData: [],
      loading: false,
      currentPage: 1,
      size: 10,
      total: 100,
      searchEmp: {
        name: '',
        nationId: null,
        politicId: null,
        posId: null,
        jobLevelId: null,
        engageForm: '',
        departmentId: null,
        dateScope: '',
      },
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
      nations: [],
      politicStatus: [],
      positions: [],
      jobLevels: [],
      departments: [],
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其他'],
      selectedDep: '点击选择部门',
      depVisible: false,
      depFontColor: 'depNoSelected',
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
        wedLock: [{required: true, message: '请选择婚姻状况', trigger: 'blur'}],
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
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请选择转正日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请选择合同开始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请选择合同结束日期', trigger: 'blur'}],
      },
      showEdit: false,
      editEmp: {},
      department: '',
      upload: {
        text: '导入数据',
        icon: 'el-icon-upload2',
        forbidden: false,
        headers: {
          Authorization: window.sessionStorage.getItem('tokenStr'),
        },
      },
      showAdvSer: false,
      advSerIcon: 'fa fa-angle-double-down'
    }
  },

  mounted() {
    this.loadEmpData()
    this.initData()
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
      }else {
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
      }else {
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
      }else {
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
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
      }

      this.getRequest('/api/employee/basic/departments').then(res => {
        if (res) {
          this.departments = res
          window.sessionStorage.setItem("departments", JSON.stringify(res))
        }
      })
    },

    getAllPositions(){
      this.getRequest('/api/employee/basic/positions').then(res => {
        if (res) {
          this.positions = res
        }
      })
    },

    getMaxWorkID(){
      this.getRequest('/api/employee/basic/maxWorkId').then(res => {
        if (res) {
          this.emp.workID = res.obj
        }
      })
    },

    showDepartments(){
      this.depVisible = !this.depVisible;
    },

    handleNodeClick(data){
      this.selectedDep = data.name
      this.emp.departmentId = data.id
      this.depVisible = false
      this.depFontColor = 'depSelected'
    },

    cancelDialog(){
      this.depVisible = false
    },

    handleCancelAdd(){
      this.depVisible = false
      this.dialogVisible = false
    },

    handleAddEmp(formName){
      console.log(this.emp)
      this.$refs[formName].validate(valid => {
        if (valid){
          this.emp.workState = '在职'
          this.postRequest('/api/employee/basic/', this.emp).then(res => {
            if (res) {
              for (const key in this.emp) {
                this.emp[key] = null;
              }
              this.loadEmpData()
              this.depVisible = false
              this.dialogVisible = false
            }
          })
        }
      })
    },

    handleDelEmp(emp){
      this.$confirm('确认删除员工[ ' + emp.name + ' ]吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/api/employee/basic/' + emp.id).then(res => {
          if (res){
            this.currentPage = 1
            this.loadEmpData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        });
      });
    },

    showEditEmp(empData){
      this.editEmp = empData
      this.department = empData.department
      this.showEdit = true
    },

    cancelEdit(){
      this.showEdit = false
    },

    handleExport(){
      this.downloadRequest('/api/employee/basic/exports')
      this.$message.success("导出成功!")
    },

    beforeUpload(){
      this.upload.forbidden = true
      this.upload.icon = 'el-icon-loading'
      this.upload.text = '正在上传...'
    },

    onSuccess(){
      this.upload.forbidden = false
      this.upload.icon = 'el-icon-upload2'
      this.upload.text = '导入文件'
      this.$message.success("导入成功!")
      this.loadEmpData()
    },

    onError(){
      this.upload.forbidden = false
      this.upload.icon = 'el-icon-upload2'
      this.upload.text = '导入文件'
      this.$message.error("导入失败!")
    },

    displayAdvSer(){
      this.advSerIcon = this.advSerIcon === 'fa fa-angle-double-down' ? 'fa fa-angle-double-up' : 'fa fa-angle-double-down'
      // this.getAllPositions()
      this.showAdvSer = !this.showAdvSer
    },

    cancelAdvSer(){
      this.advSerIcon = 'fa fa-angle-double-down'
      this.depVisible = false
      this.showAdvSer = false
      for (let searchEmpKey in this.searchEmp) {
        this.searchEmp[searchEmpKey] = '';
      }
    },

    handleAdvSer(advSerInfo){
      // 深拷贝可以保留之前的name，并集
      Object.assign(this.searchEmp, advSerInfo)
      this.loadEmpData()
      this.advSerIcon = 'fa fa-angle-double-down'
      this.depVisible = false
      this.showAdvSer = false
      for (let searchEmpKey in this.searchEmp) {
        this.searchEmp[searchEmpKey] = '';
      }
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
    },

    paramsAdd(key, value){
      return value ? '&' + key + '=' +value : ''
    },

    loadEmpData() {
      this.loading = true
      let urlParam = '';
      for (let searchEmpKey in this.searchEmp) {
        urlParam += this.paramsAdd(searchEmpKey, this.searchEmp[searchEmpKey])
      }
      this.getRequest('/api/employee/basic/?currentPage=' +
          this.currentPage + '&size=' + this.size + urlParam).then(res => {
        if (res) {
          this.tableData = res.data
          this.total = res.total
          this.loading = false
        }
        this.loading = false
      })
    },

    sizeChange(size) {
      this.size = size
      this.loadEmpData()
    },

    changePage(page) {
      this.currentPage = page;
      this.loadEmpData()
    },

    showAddEmp() {
      this.dialogVisible = true
      this.getMaxWorkID()
      this.getAllPositions()
    },

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

.slide-fade-enter-active {
  transition: all .3s ease;
}

.slide-fade-leave-active {
  transition: all .3s cubic-bezier(1, 0.5, 0.8, 1);
}

/*.slide-fade-enter-from{*/
/*  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);*/
/*}*/

.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}
</style>
