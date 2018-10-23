<template>
    <el-container>
      <div style="width:100%;"> <!-- 确保下面的内容充满 -->
         <el-row>
            <el-col :span="8">
              <label class="el-form-item__label">项目名称:</label>
              <div class="el-form-item__content">{{prjBudget.prjName}}</div>
            </el-col>
            <el-col :span="8">
              <label class="el-form-item__label">项目编号:</label>
              <div class="el-form-item__content">{{prjBudget.prjNo}}</div>
            </el-col>
          </el-row>

      <el-tabs  type="card" v-model="activeTab">
        <el-tab-pane label="人月" name="manmonth">
          <man-month v-bind:prjid="this.curPrjId" ></man-month>
        </el-tab-pane>
        <el-tab-pane label="费用" name="cost">
          <common-item type="cost" name="费用"  v-bind:prjid = "this.curPrjId" ></common-item>
        </el-tab-pane>
        <el-tab-pane label="收款" name="income">
          <common-item type="income" name="收款" v-bind:prjid = "this.curPrjId"></common-item>
        </el-tab-pane>
        <el-tab-pane label="确权" name="rights">
          <common-item type="confirm"  name="确权" v-bind:prjid = "this.curPrjId"></common-item>
        </el-tab-pane>

      </el-tabs>
  </div>      
    </el-container>

</template>



<script>

  import CommonItem from './CommonItem.vue'
  import ManMonth from './PrjManMonth.vue'


  export default{

    beforeMount: function(){
      this.curPrjId = this.$route.params.id;

      console.log("prjId:" + this.curPrjId);

    },

    mounted: function () {
      this.curPrjId = this.$route.params.id;
      this.prjBudget.prjNo = this.$route.params.prjNo;
      this.prjBudget.prjName = this.$route.params.prjName;
      console.log("prjId:" + this.curPrjId +" prjName:" + this.$route.params.prjName);
      // this.initData();
    },

    methods: {
      initData(){
        var _this = this;
        this.loading = true;
       this.getRequest("/system/budget/settings").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.budgetTypes = resp.data.budgetTypes;
          }
        })
      }
    },

    components: {
      'common-item': CommonItem,
      'man-month':ManMonth
    },

    data(){
      return {
        loading: false,
        activeTab:"manmonth",

        curPrjId:-1,

        prjBudget:{
          prjId:-1,
          prjNo:"",
          prjName:'',
          prjManCostAvg:0,
          contractAmount:0,
          curYearAmount:0,
          budgets:[
            {
              year:2017,
              budgets:[
                  {month:8, amount:0, manMonth:2, costAmount:0, realManMonth:0},
                  {month:9, amount:0, manMonth:3, costAmount:0, realManMonth:0},
                  {month:10, amount:0, manMonth:1, costAmount:0, realManMonth:0},
                  {month:11, amount:0, manMonth:4, costAmount:0, realManMonth:0},
                  {month:12, amount:0, manMonth:5, costAmount:0, realManMonth:0}
              ] 
            },
            {
              year:2018,
              budgets:[
                  {month:1, amount:0, manMonth:3, costAmount:0, realManMonth:0},
                  {month:2, amount:0, manMonth:4, costAmount:0, realManMonth:0},
                  {month:3, amount:0, manMonth:1, costAmount:0, realManMonth:0},
                  {month:4, amount:0, manMonth:2, costAmount:0, realManMonth:0},
                  {month:5, amount:0, manMonth:6, costAmount:0, realManMonth:0}

              ]
            }
          ]
        }
      }
    },

 
  }
</script>
