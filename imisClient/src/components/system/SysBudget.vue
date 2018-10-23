<template>
  <div style="margin-top: 10px">
    <el-tabs v-model="defaultTab" type="card">
      <el-tab-pane label="费用预算类别" name="budgetType">
        <budget-type></budget-type>
      </el-tab-pane>
      <el-tab-pane label="费用分组" name="costGroup">
        <cost-group></cost-group>
      </el-tab-pane>
      <el-tab-pane label="费用预算定义" name="costDef">
        <cost-def></cost-def>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>

  import BudgetType from './budget/budgetType.vue'
  import CostDef from './budget/costDef.vue'
  import CostGroup from './budget/costGroup.vue'
  export default {
    data() {
      return {
        defaultTab: 'budgetType'
      };
    },
    methods: {},
    components: {
      'budget-type': BudgetType,
      'cost-def': CostDef,
      'cost-group':CostGroup
    }
  };
</script>
