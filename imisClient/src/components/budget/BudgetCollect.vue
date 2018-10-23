<template>
  <div style="margin-top: 10px">
    <div style="padding:10px;">
     <label>请选择部门：</label>
    
      <el-select v-model="depId" style="width: 130px" size="mini" placeholder="请选择部门" @change="changeDep">
         <el-option
           v-for="item in myDeps"
            :key="item.id"
          :label="item.name"
          :value="item.id">
         </el-option>
       </el-select>
    </div>

      <el-tabs  type="card" v-model="activeTab">
        <el-tab-pane label="部门预算表" name="prjBudget" >
          <dep-budget ref="budgetTable"></dep-budget>
        </el-tab-pane>
        <el-tab-pane label="经营费用汇总"  name="costCollect">
          <expense-budget budgetTypeId=-1 v-bind:editable=false ref="costCollect" ></expense-budget>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>



<script>

  import DepBudgetTable from './DepBudgetTable.vue'
  import expenseBudget from './ExpenseBudget.vue'

  export default{
    mounted: function () {
      this.loadMyDeps();
    },

    methods: {

      loadMyDeps(){
        var _this = this;
        this.loading = true;
       this.getRequest("/system/basic/myDeps").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.myDeps = resp.data;
          }
        })
      },


    changeDep: function(depId){

      console.log(this.depId);
      console.log(this.$refs.cost);

      this.$refs.budgetTable.loadData(depId); 
      this.$refs.costCollect.loadData(depId); 

    }
  
  },

    components: {
      'dep-budget': DepBudgetTable,
      'expense-budget': expenseBudget,
    },

    data(){
      return {
        loading: false,
        
        myDeps:[],
        depId:"",
        activeTab:"prjBudget",
      }
    }
  }
</script>
