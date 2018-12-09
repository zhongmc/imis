<template>
  <el-container>
    <div style="width:100%;">
      <!-- 确保下面的内容充满 -->
      <el-row>
        <el-col :span="16">
          <label class="el-form-item__label" style="font-weight:bold;">机会名称:</label>
          <span class="el-form-item__label">{{prjChanceBudget.prjChanceName}}</span>
          <label class="el-form-item__label" style="font-weight:bold;">机会编号:</label>
          <span class="el-form-item__label">{{prjChanceBudget.prjChanceNo}}</span>
          <label class="el-form-item__label" style="font-weight:bold;">合同金额:</label>
          <span class="el-form-item__label">{{formatMoney(prjChanceBudget.contractAmount, 1)}}</span>
        </el-col>
      </el-row>

      <el-tabs type="card" v-model="activeTab">
        <el-tab-pane label="人月" name="manmonth">
          <man-month
            v-bind:prjid="this.curPrjChanceId"
            v-bind:contract-amount="this.prjChanceBudget.contractAmount"
            v-bind:tax-rate="this.taxRate"
            type="chance"
            ref="monthBudget"
          ></man-month>
        </el-tab-pane>

        <el-tab-pane label="外采" name="cost">
          <common-item
            type="chanceCost"
            name="外采"
            v-bind:prjid="this.curPrjChanceId"
            v-bind:depid="this.depId"
            v-bind:set-total-amount="this.setTotalCostAmount"
          ></common-item>
        </el-tab-pane>

        <el-tab-pane label="确权" name="rights">
          <common-item
            type="chanceC"
            name="机会确权预测"
            v-bind:prjid="this.curPrjChanceId"
            v-bind:depid="this.depId"
          ></common-item>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-container>
</template>



<script>
import CommonItem from "./CommonItem.vue";
import ManMonth from "./PrjManMonth.vue";

export default {
  beforeMount: function() {
    this.curPrjChanceId = this.$route.params.id;
    this.depId = this.$route.params.depId;

    this.taxRate = this.$route.params.taxRate;

    console.log("prjChanceId:" + this.curPrjChanceId + " depId:" + this.depId);
  },

  mounted: function() {
    this.depId = this.$route.params.depId;
    this.curPrjChanceId = this.$route.params.id;
    this.taxRate = this.$route.params.taxRate;
    this.prjChanceBudget.prjChanceNo = this.$route.params.prjChanceNo;
    this.prjChanceBudget.prjChanceName = this.$route.params.prjChanceName;
    this.prjChanceBudget.contractAmount = this.$route.params.contractAmount;
    console.log(
      "prjChanceId:" +
        this.curPrjChanceId +
        " prjChanceName:" +
        this.$route.params.prjChanceName +
        " depId: " +
        this.depId
    );
    // this.initData();
  },

  methods: {
    initData() {
      var _this = this;
      this.loading = true;
      this.getRequest("/config/budget/settings").then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.budgetTypes = resp.data.budgetTypes;
        }
      });
    },

    setTotalCostAmount(totalAmount) {
      console.log("Total cost amount:" + totalAmount);
      this.$refs.monthBudget.setTotalCostAmount(totalAmount);
    }
  },

  components: {
    "common-item": CommonItem,
    "man-month": ManMonth
  },

  data() {
    return {
      loading: false,
      activeTab: "manmonth",
      curPrjChanceId: null,
      taxRate: 0.0,

      depId: -1,
      prjChanceBudget: {
        prjChanceId: -1,
        prjChanceNo: "",
        prjChanceName: "",
        prjChanceManCostAvg: 0,
        contractAmount: 0,
        curYearAmount: 0
      }
    };
  }
};
</script>
