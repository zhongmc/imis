<!-- 部门费用预算表  -->
<template>
<div>

          <el-table
            :data="costBudgets"
            v-loading="tableLoading"
            border
            stripe
            show-summary            
            :summary-method="getSummaries"
        
            @cell-mouse-enter="handleMouseEnter"
            @cell-mouse-leave="handleMouseOut"            
            size="mini"
            style="width: 100%">

            <el-table-column
              prop="groupId"
              align="left"
              fixed
              label="费用类别"
              :formatter="formatExpenseGroup"
              width="150">
            </el-table-column>
            <el-table-column
              prop="name"
              fixed
              width="200"
              align="left"
              label="费用名称">
            </el-table-column>

            <el-table-column
              v-for="(item, index ) in tableTitles" :key="index"
              :label="tableTitles[index].title" 
              :prop=tableTitles[index].idx width="100" >

                <template slot-scope="scope">
                  <span v-if="editable === true">
                    <span v-if="!scope.row.amounts[index].editFlag">{{ formatMoney( scope.row.amounts[index].amount,1) }}</span>
                    <span v-if="!scope.row.amounts[index].editFlag & scope.row.refItem == -1" style="margin-left:10px;" class="cell-icon" @click="handleEdit( scope.row, index)"> <i class="el-icon-edit"></i> </span>
                    <span v-if="scope.row.amounts[index].editFlag  & scope.row.refItem == -1" class="cell-edit-input"><el-input v-model="inputColumn1" size="mini" placeholder="请输入内容"  @keyup.enter.native="handleSave(scope.row, index)" @keyup.esc.native="handleCancel(scope.row, index)"></el-input></span>
                    <span v-if="scope.row.amounts[index].editFlag  & scope.row.refItem == -1" style="margin-left:10px; color:black;" class="cell-icon" @click="handleSave(scope.row, index)"> <i class="el-icon-check"></i> </span>
                    <span v-if="scope.row.amounts[index].editFlag  & scope.row.refItem == -1" style="margin-left:10px; color:black;" class="cell-icon" @click="handleCancel( scope.row, index )"> <i class="el-icon-close"></i> </span>
                  </span>
                  <span v-else >
                    {{ formatMoney( scope.row.amounts[index].amount,1) }}
                  </span>     
                  <!-- span>{{formatMoney( scope.row.amounts[index].amount,1) }}</span -->
                </template>

            </el-table-column>

            <el-table-column
              label="总计"
              prop="sum"
              :formatter="formatAmount"
              align = "right"
              width="120">
            </el-table-column>

            <el-table-column
              prop="desc"
              label="备注"
              width="80">
            </el-table-column>

          </el-table>

          <div  class="el-dialog__footer" style="margin: 2px">
            <el-button type="danger" size="mini"  :disabled="!changed"
                       @click="commitChange">提交修改
            </el-button>
          </div>

  </div>
</template>




<script>
export default {
  mounted: function() {
    this.loadSettings();
  },

  methods: {
    loadSettings() {
      var _this = this;
      this.getRequest("/system/budget/settings").then(resp => {
        //          _this.loading = false;
        if (resp && resp.status == 200) {
          _this.costItems = resp.data.costItems;
          _this.costGroups = resp.data.costGroups;
          _this.initData();
        }
      });
    },

    loadData() {
      var _this = this;
      this.loading = true;

      _this.costBudgetInfos = [];
      this.getRequest(
        "/budget/dep/" + this.depId + "/" + this.budgetTypeId
      ).then(resp => {
        if (resp && resp.status == 200) {
          _this.costBudgetInfos = resp.data;
          _this.newInitData();
        }
      });
    },

    changeDep(depId) {
      this.depId = depId;
      this.loadData();
    },

    formatAmount(row, column, cellValue) {
      var props = column.property.split(".");

      var tmp = row;
      for (var i = 0; i < props.length; i++) {
        tmp = tmp[props[i]];
      }

      //     console.log("format amount:" + cellValue + "col prop:" + column.property);
      return this.formatMoney(tmp, 1);
    },

    //        {"id":1, "name":"人数", "groupId":1, "beAmount":false, "amountFormula":null, "desc":"备注"},

    newInitData() {
      if (this.costBudgetInfos.length == 0 || this.costItems.length == 0)
        //同步两个http请求
        return;

      // console.log("init data...");
      var costBudgets = new Array(this.costItems.length);

      // console.log("create array ok");

      for (var i = 0; i < this.costItems.length; i++) {
        var costItem = this.costItems[i];

        var costBudgetItem = {
          costItemId: costItem.id,
          groupId: costItem.groupId,
          name: costItem.name,
          beAmount: costItem.beAmount,
          amountFormula: costItem.amountFormula,
          amounts: [12],
          refItem: -1,
          sum: 0
        };
        if (costItem.amountFormula != null && costItem.amountFormula.length > 0)
          costBudgetItem.refItem = 1;
        costBudgets[i] = costBudgetItem;
      }

      // console.log(costBudgets);

      for (var i = 0; i < this.costBudgetInfos.length; i++) {
        this.setCostBudgetInfoToCostItem(this.costBudgetInfos[i], costBudgets);
      }

      this.costBudgets = costBudgets;
      // console.log(this.costBudgets);
    },

    setCostBudgetInfoToCostItem(info, budgets) {
      for (var i = 0; i < budgets.length; i++) {
        if (budgets[i].costItemId == info.costItemId) {
          // info.editFlag = false;
          budgets[i].amounts[info.month] = info;
          budgets[i].sum = budgets[i].sum + info.amount;
          this.$set(info, "editFlag", false);
        }
      }
    },

    formatExpenseGroup(row, column) {
      for (var i = 0; i < this.costGroups.length; i++) {
        if (this.costGroups[i].id == row.groupId)
          return this.costGroups[i].name;
      }
      return "unknow";
    },

    commitChange() {
      var _this = this;
      this.postJsonRequest("/budget/dep", this.costBudgetInfos).then(resp => {
        _this.treeLoading = false;
        if (resp && resp.status == 200) {
          var respData = resp.data;
          _this.$message({ type: respData.status, message: "修改成功!" });
          _this.changed = false;
        }
      });
    },

    handleEdit: function(row, idx) {
      // var tmp = row.amounts[idx];
      // console.log( tmp );
      // this.$set(tmp, "editFlag", true);

      row.amounts[idx].editFlag = true;
      this.inputColumn1 = row.amounts[idx].amount;
      //  row.amounts[idx].amount = 12; //this.inputColumn1;
      // console.log( row.amounts[idx] );
    },

    getSummaries: function(param) {
      const sums = [];
      sums[0] = "";
      sums[1] = "合计";

      for (var i = 0; i < 13; i++) sums[2 + i] = 0;

      for (var idx = 0; idx < this.costBudgets.length; idx++) {
        if (!this.costBudgets[idx].beAmount) continue;

        for (var i = 0; i < 12; i++) {
          sums[2 + i] = sums[2 + i] + this.costBudgets[idx].amounts[i].amount;
        }
        sums[14] = sums[14] + this.costBudgets[idx].sum;
      }
      for (var i = 0; i < 13; i++)
        sums[2 + i] = this.formatMoney(sums[2 + i], 1);

      return sums;
    },

    handleSave: function(row, index) {
      //保存数据，向后台取数据
      row.amounts[index].editFlag = false;
      row.amounts[index].amount = parseFloat(this.inputColumn1);
      var tmp = 0;
      var tmp1 = row.amounts;
      for (var i = 0; i < 12; i++) {
        tmp = tmp + tmp1[i].amount;
      }
      row.sum = tmp;
      var curId = row.costItemId;
      var values = [];

      var re = /#\d+/g; //正则表达式对象！！

      for (var idx = 0; idx < this.costBudgets.length; idx++) {
        if (this.costBudgets[idx].amountFormula == null) continue;
        var tmpStr = this.costBudgets[idx].amountFormula.replace("$", "#");
        //      console.log(tmpStr);
        var vals = tmpStr.match(re);
        //      console.log( vals );
        if (vals == null) continue;

        for (var i = 0; i < vals.length; i++) {
          tmpStr = tmpStr.replace(vals[i], "values[" + i + "]");
          values[i] = this.getAmountValue(vals[i].substr(1), index);
        }

        var tmpAmount = eval(tmpStr);

        var sumAmount =
          this.costBudgets[idx].sum -
          this.costBudgets[idx].amounts[index].amount +
          tmpAmount;
        this.costBudgets[idx].amounts[index].amount = tmpAmount;
        this.costBudgets[idx].sum = sumAmount;
        this.changed = true;
      }
    },

    getAmountValue: function(id, index) {
      for (var idx = 0; idx < this.costBudgets.length; idx++) {
        if (this.costBudgets[idx].costItemId == id)
          return this.costBudgets[idx].amounts[index].amount;
      }
      return null;
    },

    handleCancel: function(row, idx) {
      row.amounts[idx].editFlag = false;
      var tmp = row.amounts[idx].amount;
      row.amounts[idx].amount = tmp;
    },

    handleMouseEnter: function(row, column, cell, event) {
      var idx = column.property;
      if (this.editable === true && idx >= 1 && idx <= 12) {
        if (!row.amounts[idx - 1].editFlag & (row.refItem == -1))
          cell.children[0].children[0].children[1].style.color = "black";
      }
    },

    handleMouseOut: function(row, column, cell, event) {
      var idx = column.property;
      if (this.editable === true && idx >= 1 && idx <= 12) {
        if (!row.amounts[idx - 1].editFlag & (row.refItem == -1))
          cell.children[0].children[0].children[1].style.color = "#ffffff";
      }
    }
  },

  data() {
    return {
      loading: false,
      costItems: [], //费用定义
      costGroups: [], //费用定义分组

      costBudgetInfos: [], //费用预算（来自服务器）列表

      costBudgets: [], //费用预算定义，经前端按 costItem 整理

      tableLoading: false,

      depId: null,

      changed: false,

      dialogVisible: false,
      currentItemLabel: "",
      amount: 0,
      editingRow: null,
      editingProp: "",
      tableTitles: [
        { title: "一月", idx: "1" },
        { title: "二月", idx: "2" },
        { title: "三月", idx: "3" },
        { title: "四月", idx: "4" },
        { title: "五月", idx: "5" },
        { title: "六月", idx: "6" },
        { title: "七月", idx: "7" },
        { title: "八月", idx: "8" },
        { title: "九月", idx: "9" },
        { title: "十月", idx: "10" },
        { title: "十一月", idx: "11" },
        { title: "十二月", idx: "12" }
      ]
    };
  },

  props: ["budgetTypeId", "editable"]
};
</script>


<style>
.cell-icon {
  cursor: pointer;
  color: #fff;
}
</style>