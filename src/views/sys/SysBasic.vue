<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="部门管理" name="depMana">
              <dep-mana>

              </dep-mana>
            </el-tab-pane>
            <el-tab-pane label="职位管理" name="posMana">
              <pos-mana>

              </pos-mana>
            </el-tab-pane>
            <el-tab-pane label="职称管理" name="jobLevelMana">
              <job-level-mana>

              </job-level-mana>
            </el-tab-pane>
            <el-tab-pane label="奖惩规则" name="ecMana">
              <ec-mana>

              </ec-mana>
            </el-tab-pane>
            <el-tab-pane label="权限组" name="permMana">
              <permiss-mana>

              </permiss-mana>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import DepMana from "../../components/sys/basic/DepMana";
    import EcMana from "../../components/sys/basic/EcMana";
    import PermissMana from "../../components/sys/basic/PermissMana";
    import PosMana from "../../components/sys/basic/PosMana";
    import JobLevelMana from "../../components/sys/basic/JobLevelMana";

    export default {
        name: "SysBasic",
      // eslint-disable-next-line vue/no-unused-components
        components: {JobLevelMana, PermissMana, EcMana, PosMana, DepMana},
        data(){
            return{
                activeName:'depMana'
            }
        }
    }
</script>

<style scoped>

</style>
