<!--    添加工资账套    -->
<template>
  <div>
    <el-dialog
        title="添加账套"
        :visible.sync='visible'
        width="50%"
        @close="handleCancel"
    >
      <div style="display: flex;
      justify-content: space-around; align-items: center">
        <el-steps direction="vertical" :active="activeItem" >
          <el-step :title="itemName" v-for="(itemName, index) in salaryItem" :key="index">
          </el-step>
        </el-steps>
        <el-input v-for="(value, title, index) in salary"
                  :key="index"
                  :placeholder="'请输入' + salaryItem[index] + '...'"
                  v-show="index===activeItem"
                  style="width: 200px"
                  v-model="salary[title]"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="preStep">
          {{activeItem === 0 ? '取消' : '上一步'}}
        </el-button>
        <el-button type="primary" @click="nextStep">
          {{activeItem === salaryItem.length-1 ? '完成' : '下一步'}}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AddSalary",
  data(){
    return{
      visible: true,
      activeItem: 0,
      salaryItem: ['账套名称', '基本工资', '奖金', '午餐补助', '交通补助',
        '养老金基数', '养老金比率', '医疗保险基数', '医疗保险比率', '公积金基数', '公积金比率'],
      salary: {
        name: '',
        basicSalary: 0,
        bonus: 0,
        lunchSalary: 0,
        trafficSalary: 0,
        pensionBase: 0,
        pensionPer: 0,
        medicalBase: 0,
        medicalPer: 0,
        accumulationFundBase: 0,
        accumulationFundPer: 0,
      },
    }
  },

  mounted() {
  },

  methods:{

    handleCancel(){
      this.visible=false
      this.salary= {
        name: '',
        basicSalary: 0,
        bonus: 0,
        lunchSalary: 0,
        trafficSalary: 0,
        allSalary: 0,
        pensionBase: 0,
        pensionPer: 0,
        createDate: 0,
        medicalBase: 0,
        medicalPer: 0,
        accumulationFundBase: 0,
        accumulationFundPer: 0,
      },
      this.$emit('cancel')
    },

    handleAdd(){
      console.log(this.salary)
      this.postRequest('/api/sal/sob/', this.salary).then(res=>{
          if (res){
            this.$emit('cancel')
            this.$emit('loadData')
          }
      })
    },

    preStep(){
      if (this.activeItem === 0){
        this.handleCancel()
        return
      }
      this.activeItem--
    },

    nextStep(){
      if (this.activeItem >= this.salaryItem.length-1){
        this.handleAdd()
        return
      }
      this.activeItem++
    },
  }
}
</script>

<style scoped>

</style>
