<template>
  <div>
    <el-form
      :model="prjBudget"
      :rules="budgetRules"
      ref="prjBudgetForm"
      style="margin: 0px;padding: 0px;"
    >
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
        </el-row-->
        <el-row>
          <!-- el-col :span="4">
            <el-form-item label="增值税率:" style="font-weight:bold;" prop="taxRate">
              <el-input size="mini" style="width: 60px" v-model.number="prjBudget.taxRate"/>
            </el-form-item>
          </el-col-->
          <el-col :span="20">
            <span class="el-form-item__label" style="margin-left:20px;font-weight:bold;">合同金额:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{formatMoney(contractAmount, 1) }}&nbsp;&nbsp;元</span>
            
            <span class="el-form-item__label" style="margin-left:20px;font-weight:bold;">外采:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{formatMoney(totalCostAmount, 1) }}&nbsp;&nbsp;元</span>
            
            <span class="el-form-item__label" style="margin-left:20px;font-weight:bold;">收入:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{formatMoney(income, 1) }}&nbsp;&nbsp;元</span>
            
            <span class="el-form-item__label" style="margin-left:40px;font-weight:bold;">人月合计:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{totalManmonth}}&nbsp;&nbsp;人月</span>
            <span class="el-form-item__label" style="margin-left:20px;font-weight:bold;">成本合计:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{formatMoney(totalAmount, 1) }}&nbsp;&nbsp;元</span>
            
            <span class="el-form-item__label" style="margin-left:20px;font-weight:bold;">毛利:</span>
            <span
              class="el-form-item__label"
              style="margin-left:10px;"
            >{{formatMoney(grossProfit, 1) }}&nbsp;&nbsp;元</span>
            
            <span class="el-form-item__label" style="margin-left:40px;font-weight:bold;">毛利率:</span>
            <span class="el-form-item__label" style="margin-left:10px;">{{grossProfitRate}}%</span>
          </el-col>

          <el-col :span="6">
            <el-form-item label="项目平均人月费用:" style="font-weight:bold;" prop="avgManMonthCost">
              <el-input
                size="mini"
                style="width: 130px"
                v-model.number="prjBudget.avgManMonthCost"
                @blur="handleAvgManMonthChange()"
                @keyup.enter.native="handleAvgManMonthChange()"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <div v-for="budget in budgets" :key="budget.year">
          <template>
            <el-row>
              <el-col :span="20">
                <h3>{{budget.year}}年预算(单位：人月)</h3>
              </el-col>
            </el-row>

            <el-row :gutter="10">
              <el-col v-for="item in budget.budgets" :key="item.month" :span="8">
                <div
                  style="line-height:40px;font-size:14px;"
                  v-if="item.realAmount == null ||  item.realAmount <= 10"
                >
                  <span style="font-weight:bold;">{{item.month + 1}}月:&nbsp;&nbsp;</span>
                  <el-input
                    class="col-input"
                    v-model.number="item.manMonth"
                    size="mini"
                    style="width:60px;"
                    @blur="handleManMonthChange(item)"
                    @keyup.enter.native="handleManMonthChange(item)"
                    prop="manMonth"
                  ></el-input>
                  <span>人;&nbsp;</span>
                  <span>共{{formatMoney(item.amount, 1)}}元.</span>
                </div>

                <div style="line-height:40px;font-size:14px;" v-else>
                  <span style="font-weight:bold;">{{item.month + 1}}月:&nbsp;&nbsp;</span>
                  <span>{{item.realManMonth }}人;&nbsp;</span>
                  
                  <span>&nbsp;&nbsp;共{{formatMoney(item.realAmount, 1)}}元.</span>
                </div>
              </el-col>
            </el-row>

            <!-- el-row :gutter="10">
              <el-col v-for="item in budget.budgets" :key="item.month" :span="4">
                <div
                  style="font-size:14px;padding:5px;"
                  v-if="item.realAmount == null ||  item.realAmount <= 10"
                >
                  <div style="line-height:35px;float:left;">
                    <span style="margin-right:15px;">{{item.month + 1}} 月:</span>
                    <el-input
                      class="col-input"
                      v-model.number="item.manMonth"
                      size="mini"
                      style="width:80px;margin-right:15px;"
                      @blur="handleManMonthChange(item)"
                      @keyup.enter.native="handleManMonthChange(item)"
                      prop="manMonth"
                    ></el-input>
                    <span>人</span>
                  </div>
                  <div style="clear:both;"></div>
                  <div
                    style="margin-top:5px;margin-left:50px;float:clear;"
                  >共 {{formatMoney(item.amount, 1)}} 元</div>
                </div>

                <div v-else>
                  <span style="margin-right:15px;">{{item.month + 1}} 月:</span>
                  <span>{{item.realManMonth }}人</span>

                  <div
                    style="margin-top:5px;margin-left:50px;float:clear;"
                  >共 {{formatMoney(item.realAmount, 1)}} 元</div>
                </div>
              </el-col>
            </el-row-->
          </template>
        </div>

        <div class="el-dialog__footer" style="margin: 2px">
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

      var url;
      if (this.type && this.type == "chance") url = "budget/prjChance/";
      else url = "budget/project/";

      _this.getRequest(url + prjId).then(resp => {
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
      var url;

      if (this.type && this.type == "chance") url = "budget/prjChance";
      else url = "budget/project";

      _this.postJsonRequest(url, this.prjBudget).then(resp => {
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

      if (curYear == theYear) {
        budgets[idx].budgets = new Array(12);
        for (var kk = 0; kk < 12; kk++) budgets[idx].budgets[kk] = null;
      } else budgets[idx].budgets = new Array();

      budgets[idx].year = curYear;

      for (var i = 0; i < data.monthBudgets.length; i++) {
        if (data.monthBudgets[i].year != curYear) {
          k = 0;
          idx++;
          curYear = data.monthBudgets[i].year;

          budgets[idx] = new Object();

          if (curYear == theYear) {
            budgets[idx].budgets = new Array(12);
            for (var kk = 0; kk < 12; kk++) budgets[idx].budgets[kk] = null;
          } else budgets[idx].budgets = new Array();
          budgets[idx].budgets = new Array();
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
        for (var k = 0; k < 12; k++) {
          if (bgs[k] == null) {
            bgs[k] = this.getEmptyMonthBudget(data, theYear, k);
            data.monthBudgets[idx++] = bgs[k];
          }
        }
      }
      // console.log(budgets);
      // console.log(data);

      this.prjBudget = data;
      this.budgets = budgets;
    },

    getEmptyMonthBudget: function(prjBudget, iyear, imonth) {
      if (this.type && this.type == "chance") {
        return {
          id: null,
          prjChanceId: prjBudget.prjChanceId,
          prjChanceBudgetId: prjBudget.id,
          depId: prjBudget.depId,
          month: imonth,
          year: iyear,
          amount: 0,
          manMonth: 0,
          changed: false
        };
      } else {
        return {
          id: null,
          prjId: prjBudget.prjId,
          prjBudgetId: prjBudget.id,
          depId: prjBudget.depId,
          month: imonth,
          year: iyear,
          realAmount: 0,
          realManMonth: 0,
          amount: 0,
          manMonth: 0,
          changed: false
        };
      }
    },

    getEmptyMonthBudgets: function(prjBudget, iyear) {
      var budgets = new Array(12);

      if (this.type && this.type == "chance") {
        for (var idx = 0; idx < 12; idx++) {
          budgets[idx] = {
            id: null,
            prjChanceId: prjBudget.prjChanceId,
            prjChanceBudgetId: prjBudget.id,
            depId: prjBudget.depId,
            month: idx,
            year: iyear,
            amount: 0,
            manMonth: 0,
            changed: false
          };
        }
      } else {
        for (var idx = 0; idx < 12; idx++) {
          budgets[idx] = {
            id: null,
            prjId: prjBudget.prjId,
            prjBudgetId: prjBudget.id,
            depId: prjBudget.depId,
            month: idx,
            year: iyear,
            realAmount: 0,
            realManMonth: 0,
            amount: 0,
            manMonth: 0,
            changed: false
          };
        }
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
      var totalAmount = 0.0,
        totalMan = 0.0;
      for (var idx = 0; idx < this.prjBudget.monthBudgets.length; idx++) {
        totalMan += parseFloat(this.prjBudget.monthBudgets[idx].manMonth);
        totalAmount = totalAmount + this.prjBudget.monthBudgets[idx].amount;
      }
      this.totalAmount = totalAmount;
      this.totalManmonth = totalMan;

      this.calculatTheProfit();
      // console.log(totalMan);
      // console.log(this.totalManmonth + ":" + this.totalAmount);
    },

    handleAvgManMonthChange: function() {
      var totalAmount = 0.0,
        totalMan = 0.0;
      var amount = 0,
        manmonth = 0;
      for (var idx = 0; idx < this.prjBudget.monthBudgets.length; idx++) {
        manmonth = parseFloat(this.prjBudget.monthBudgets[idx].manMonth);
        amount = manmonth * this.prjBudget.avgManMonthCost;

        this.prjBudget.monthBudgets[idx].amount = amount;
        totalAmount = totalAmount + amount;
        totalMan = totalMan + manmonth;
      }
      this.totalAmount = totalAmount;
      this.totalManmonth = totalMan;
      this.calculatTheProfit();
      // console.log(this.totalManmonth + ":" + this.totalAmount);
    },

    setTotalCostAmount(totalAmount) {
      this.totalCostAmount = totalAmount;
      this.calculatTheProfit();
    },

    calculatTheProfit() {
      var taxRatePlus = 1.0 + this.taxRate / 100.0;

      this.income =
        this.contractAmount / taxRatePlus - this.totalCostAmount / taxRatePlus;
      //    console.log(this.income);

      // console.log(this.contractAmount + ", " + this.totalCostAmount);
      this.tax =
        this.contractAmount -
        this.contractAmount / taxRatePlus -
        (this.totalCostAmount - this.totalCostAmount / taxRatePlus);

      // console.log("tax: " + this.tax);

      this.grossProfit = this.income - this.totalAmount;

      this.grossProfitRate = (this.grossProfit / this.income) * 100.0;
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

      //收入
      income: 0.0,
      tax: 0.0,
      //毛利
      grossProfit: 0.0,

      //毛利率
      grossProfitRate: 0,

      //外采
      totalCostAmount: 0.0,
      // taxRate: 0.0,

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
        ],
        taxRate: [
          { required: true, message: "不能为空", trigger: "blur" },
          { type: "number", message: "必须为数字值" }
        ]
      }
    };
  }, //data

  props: ["prjid", "type", "contractAmount", "taxRate"]
};
</script>
