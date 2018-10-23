<template>
<div>

          <el-table
            :data="expenseItems"
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
              width="90">
            </el-table-column>
            <el-table-column
              prop="name"
              fixed
              width="85"
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

            <el-table-column
              label="操作"
              width="195" v-if="editable">
              <template slot-scope="scope">
                <el-button @click="showEditPrjView(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="mini">编辑
                </el-button>
              </template>
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
      console.log(this.editable);
      this.getRequest("/system/budget/settings").then(resp => {
        //          _this.loading = false;
        if (resp && resp.status == 200) {
          _this.tmpExpsData = resp.data.costItems;
          _this.costGroups = resp.data.costGroups;
          console.log("cost settings load ok");
          _this.initData();
        }
      });
    },

    loadData(depId) {
      var _this = this;
      this.loading = true;

      this.getRequest("/budget/dep/" + depId + "/" + this.budgetTypeId).then(
        resp => {
          if (resp && resp.status == 200) {
            _this.budgets = resp.data;
            console.log("cost load ok!");
            console.log(resp.data);
            _this.initData();
          }
        }
      );
    },

    formatAmount1(scope) {
      console.log(scope.index);
      console.log(scope.row);
      console.log(scope.column);
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
      if (this.tmpExpsData.length == 0 || this.budgets.length == 0)
        //同步两个http请求
        return;
    },

    initData() {
      if (this.tmpExpsData.length == 0 || this.budgets.length == 0)
        //同步两个http请求
        return;

      this.loading = false;

      console.log("initData: ");

      for (var i = 0; i < this.budgets.length; i++) {
        var budgetItem = this.budgets[i];
        var costItem = this.getCostItem(budgetItem.costItemId);

        console.log(costItem);

        this.$set(budgetItem, "amountFormula", costItem.amountFormula);

        this.$set(budgetItem, "beAmount", costItem.beAmount);
        this.$set(budgetItem, "name", costItem.name);
        this.$set(budgetItem, "groupId", costItem.groupId);
        this.$set(budgetItem, "desc", costItem.desc);

        this.$set(budgetItem, "sum", 0); //添加新属性，不能直接用. 否则响应式渲染监测不到值的变化
        for (var k = 0; k < 12; k++) {
          budgetItem.sum += budgetItem.amounts[k].amount;
          this.$set(budgetItem.amounts[k], "editFlag", false);
        }
      }

      this.expenseItems = this.budgets; //准备好数据再更新数据模型，vue才能正确渲染页面；
      //     this.tmpExpsData = [];
      console.log("initData OK");
    },

    getCostItem(itemId) {
      for (var i = 0; i < this.tmpExpsData.length; i++) {
        if (this.tmpExpsData[i].id == itemId) return this.tmpExpsData[i];
      }
      return null;
    },

    initData1() {
      if (this.tmpExpsData.length == 0 || this.budgets.length == 0)
        //同步两个http请求
        return;

      this.loading = false;

      console.log("initData: ");

      for (var idx = 0; idx < this.tmpExpsData.length; idx++) {
        var budgetItem = this.getBudgetItem(this.tmpExpsData[idx].id);

        budgetItem.amountFormula = this.tmpExpsData[idx].amountFormula;
        budgetItem.beAmount = this.tmpExpsData[idx].beAmount;

        this.$set(budgetItem, "sum", 0); //添加新属性，不能直接用. 否则响应式渲染监测不到值的变化

        for (var i = 0; i < 12; i++) {
          budgetItem.sum += budgetItem.amounts[i].amount;
          this.$set(budgetItem.amounts[i], "editFlag", false);
          //          budgetItem.amounts[i].editFlag = false;
        }
        this.tmpExpsData[idx].budgetItem = budgetItem;
      }
      this.expenseItems = this.tmpExpsData; //准备好数据再更新数据模型，vue才能正确渲染页面；
      //     this.tmpExpsData = [];
    },

    getBudgetItem(id) {
      for (var i = 0; i < this.budgets.length; i++) {
        if (this.budgets[i].costItemId == id) return this.budgets[i];
      }
      return null;
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
      this.postRequest(
        "/budget/cost/" + this.budgetTypeId,
        this.expenseItems
      ).then(resp => {
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

      for (var idx = 0; idx < this.expenseItems.length; idx++) {
        if (!this.expenseItems[idx].beAmount) continue;

        for (var i = 0; i < 12; i++) {
          sums[2 + i] = sums[2 + i] + this.expenseItems[idx].amounts[i].amount;
        }
        sums[14] = sums[14] + this.expenseItems[idx].sum;
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

      for (var idx = 0; idx < this.expenseItems.length; idx++) {
        if (this.expenseItems[idx].amountFormula == null) continue;
        var tmpStr = this.expenseItems[idx].amountFormula.replace("$", "#");
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
          this.expenseItems[idx].sum -
          this.expenseItems[idx].amounts[index].amount +
          tmpAmount;
        this.expenseItems[idx].amounts[index].amount = tmpAmount;
        this.expenseItems[idx].sum = sumAmount;
        this.changed = true;
      }
    },

    getAmountValue: function(id, index) {
      for (var idx = 0; idx < this.expenseItems.length; idx++) {
        if (this.expenseItems[idx].costItemId == id)
          return this.expenseItems[idx].amounts[index].amount;
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
      expenseItems: [],
      costGroups: [],
      budgets: [],

      tmpExpsData: [],
      tableLoading: false,

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

  props: ["budgetTypeId", "editable", "depId"]
};
</script>


<style>
.cell-icon {
  cursor: pointer;
  color: #fff;
}
</style>