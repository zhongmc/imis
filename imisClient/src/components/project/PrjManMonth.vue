<template>
  <div>
 
    <el-form :model="prjBudget" :rules="budgetRules" ref="prjBudgetForm"
         style="margin: 0px;padding: 0px;">
      <div style="text-align: left">
           <!-- el-row>
            <el-col :span="10">
              <el-form-item label="项目名称:">
              {{prjBudget.prjName}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编号:">
                {{prjBudget.prjNo}}
              </el-form-item>
            </el-col>
          </el-row -->

          <el-row>
            <el-col :span="6">
              <el-form-item label="项目平均人月费用:" prop="avgManMonthCost">
                <el-input size="mini" style="width: 130px"  v-model = "prjBudget.avgManMonthCost"  @blur="handleAvgManMonthChange()"  @keyup.enter.native="handleAvgManMonthChange()" />
              </el-form-item>
            </el-col>
          <el-col :span="16">
              <span style="margin-left:40px;">人月合计:</span><span style="margin-left:10px;">{{totalManmonth}}人月</span>
              <span style="margin-left:20px;">项目费用合计:</span><span style="margin-left:10px;">{{formatMoney(totalAmount, 1) }}元</span>
            </el-col>

          </el-row>

          <div  v-for="budget in budgets" :key=budget.year>
            <template>
              <el-row>
                <el-col :span="20"> 
                  <h3>{{budget.year}}年预算(单位：人月)</h3>
                </el-col>
              </el-row>
              
          <el-row :gutter="10">
            <el-col v-for = "item in budget.budgets" :key=item.month :span="4" >
                  <div style="font-size:14px;padding:5px;">
                    <div style="line-height:35px;float:left;">
                      <span style="margin-right:15px;">{{item.month + 1}} 月:</span> 
                      <el-input class="col-input" v-model="item.manMonth" size="mini" style="width:80px;margin-right:15px;"

                          @blur="handleManMonthChange(item)"  
                          @keyup.enter.native="handleManMonthChange(item)" prop="manMonth"></el-input><span>人</span>

                    </div>
                    <div style="clear:both;"></div>
                    <div style="margin-top:5px;margin-left:50px;float:clear;">共 {{formatMoney(item.amount, 1)}} 元 </div>
                  </div>
              </el-col>
          </el-row>

            </template>
          </div>

           <div  class="el-dialog__footer" style="margin: 2px">
              <el-button size="mini" type="primary" @click="savePrjBudget('prjBudgetForm')">提 交</el-button>
          </div>
      </div>
    </el-form>

  </div>
</template>
<script>
export default {
  mounted: function() {
    //    console.log("ManMonth prjId:" + this.prjid);
    this.loadPrjBudget(this.prjid);
  },
  methods: {
    loadPrjBudget(prjId) {
      var _this = this;
      _this.curPrjId = prjId;
      _this.tableLoading = true;
      _this.getRequest("/budget/project/" + prjId).then(resp => {
        _this.tableLoading = false;
        //        console.log(resp.status);

        if (resp && resp.status == 200) {
          _this.sortPrjBudget(resp.data);
          _this.prjBudget = resp.data;
          _this.updateTotal();
        } else {
          _this.curPrjId = -1;
        }
      });
    },

    savePrjBudget() {
      var _this = this;
      _this.tableLoading = true;
      // console.log(this.prjBudget);
      // console.log(this.budgets);

      _this.postJsonRequest("/budget/project", this.prjBudget).then(resp => {
        _this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
        }
      });
    },

    sortPrjBudget(data) {
      if (data.monthBudgets == null || data.monthBudgets.length == 0)
        return null;

      var date = new Date();
      var theYear = date.getFullYear();

      var budgets = new Array();
      var curYear = data.monthBudgets[0].year;

      var idx = 0,
        k = 0;

      // console.log(curYear);
      budgets[idx] = new Object();

      if (curYear == theYear)
        budgets[idx].budgets = this.getEmptyMonthBudgets(data, theYear);
      else budgets[idx].budgets = new Array();

      budgets[idx].year = curYear;

      for (var i = 0; i < data.monthBudgets.length; i++) {
        if (data.monthBudgets[i].year != curYear) {
          k = 0;
          idx++;
          curYear = data.monthBudgets[i].year;

          budgets[idx] = new Object();

          if (curYear == theYear)
            budgets[idx].budgets = this.getEmptyMonthBudgets(data, theYear);
          else budgets[idx].budgets = new Array();
          budgets[idx].year = curYear;
        }

        if (curYear == theYear)
          budgets[idx].budgets[data.monthBudgets[i].month] =
            data.monthBudgets[i];
        else budgets[idx].budgets[k++] = data.monthBudgets[i];
      }

      var idx = data.monthBudgets.length;
      for (var i = 0; i < budgets.length; i++) {
        if (budgets[i].year != theYear) continue;
        var bgs = budgets[i].budgets;
        for (var k = 0; k < bgs.length; k++) {
          if (bgs[k].id == null) data.monthBudgets[idx++] = bgs[k];
        }
      }

      this.prjBudget = data;
      this.budgets = budgets;
    },

    getEmptyMonthBudgets: function(prjBudget, iyear) {
      var budgets = new Array(12);

      for (var idx = 0; idx < 12; idx++) {
        budgets[idx] = {
          id: null,
          prjId: prjBudget.prjId,
          prjBudgetId: prjBudget.id,
          depId: prjBudget.depId,
          month: idx,
          year: iyear,
          amount: 0,
          manMonth: 0,
          changed: false
        };
      }

      return budgets;
    },

    handleManMonthChange: function(monthBudget) {
      monthBudget.amount =
        this.prjBudget.avgManMonthCost * monthBudget.manMonth;
      if (monthBudget.id == null) monthBudget.changed = true;

      this.updateTotal();
    },

    updateTotal: function() {
      var totalAmount = 0,
        totalMan = 0;
      for (var idx = 0; idx < this.prjBudget.monthBudgets.length; idx++) {
        totalMan = totalMan + this.prjBudget.monthBudgets[idx].manMonth;
        totalAmount = totalAmount + this.prjBudget.monthBudgets[idx].amount;
      }
      this.totalAmount = totalAmount;
      this.totalManmonth = totalMan;
      console.log(this.totalManmonth + ":" + this.totalAmount);
    },

    handleAvgManMonthChange: function() {
      var totalAmount = 0,
        totalMan = 0;
      var amount, manmonth;
      for (var idx = 0; idx < this.prjBudget.monthBudgets.length; idx++) {
        manmonth = this.prjBudget.monthBudgets[idx].manMonth;
        amount = manmonth * this.prjBudget.avgManMonthCost;

        this.prjBudget.monthBudgets[idx].amount = amount;
        totalAmount = totalAmount + amount;
        totalMan = totalMan + manmonth;
      }
      this.totalAmount = totalAmount;
      this.totalManmonth = totalMan;
      console.log(this.totalManmonth + ":" + this.totalAmount);
    }
  }, //methods

  //   PrjBudget{
  //     avgManMonthCost,prjId,prjNo，prjName,
  //     monthBudgets[
  //       year,month,amount,realAmount,manMonth,realManMonth

  //     ],
  //     commBudgets[

  //     ],
  //  }

  data() {
    return {
      loading: false,
      multipleSelection: [],
      budgets: [],

      totalManmonth: 0.0,
      totalAmount: 0.0,
      prjBudget: {},

      budgetRules: {
        avgManMonthCost: [
          { required: true, message: "不能为空", trigger: "blur" },
          { type: "number", message: "必须为数字值" }
        ],
        manMonth: [
          { required: true, message: "不能为空", trigger: "blur" },
          { type: "number", message: "人月必须为数字值" }
        ]
      }
    };
  }, //data

  props: ["prjid"]
};
</script>
