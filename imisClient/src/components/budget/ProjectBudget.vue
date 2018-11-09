<!-- 部门项目预算表（本年度） -->
<template>
<div>

          <el-table
            :data="sortedPrjBudgets"
            v-loading="tableLoading"
            border
            stripe
            @cell-mouse-enter="handleMouseEnter"
            @cell-mouse-leave="handleMouseOut"            
            size="mini"
            style="width: 100%">

            <el-table-column
              prop="prjNo"
              align="left"
              fixed
              label="项目编号"
              width="150">
            </el-table-column>
            <el-table-column
              prop="prjName"
              fixed
              width="220"
              align="left"
              label="项目名称">
            </el-table-column>

            <el-table-column
              prop="avgManMonthCost"
              width="100"
              align="right"
              label="平均人月费用">
                <template slot-scope="scope">
                      <span v-if="!scope.row.editFlag" style="align:right;">
                        <span>{{ formatMoney( scope.row.avgManMonthCost,1) }}</span>
                        <span style="margin-left:10px; color:#ffffff;" class="cell-icon" @click="handleEdit( scope.row, -1)"> <i class="el-icon-edit"></i> </span>
                      </span>
                      <span v-else>
                        <span class="cell-edit-input"><el-input v-model="inputColumn1" size="mini" placeholder="请输入内容"  @keyup.enter.native="handleSave(scope.row, -1, scope.$index)" @keyup.esc.native="handleCancel(scope.row, -1)"></el-input></span>
                        <span style="margin-left:10px; color:black;" class="cell-icon" @click="handleSave(scope.row, -1, scope.$index)"> <i class="el-icon-check"></i> </span>
                        <span style="margin-left:10px; color:black;" class="cell-icon" @click="handleCancel( scope.row, -1 )"> <i class="el-icon-close"></i> </span>
                      </span>

                </template>

            </el-table-column>

            <el-table-column
              v-for="(item, index ) in tableTitles" :key="index"
              :label="tableTitles[index].title" 
              :prop=tableTitles[index].idx width="120" align="center" >

                <template slot-scope="scope">
                <span v-if = "scope.row.amounts[index] != null">
                  <span v-if="!scope.row.amounts[index].editFlag">{{scope.row.amounts[index].manMonth}}人月<br/>{{ formatMoney( scope.row.amounts[index].amount,1) }}元 </span>
                  <span v-if="!scope.row.amounts[index].editFlag" style="margin-left:10px;color:#ffffff;" class="cell-icon" @click="handleEdit( scope.row, index)"> <i class="el-icon-edit"></i> </span>
                  
                  <span v-if="scope.row.amounts[index].editFlag" class="cell-edit-input"><el-input v-model="inputColumn1" size="mini" placeholder="请输入内容"  @keyup.enter.native="handleSave(scope.row, index, scope.$index)" @keyup.esc.native="handleCancel(scope.row, index)"></el-input></span>
                  <span v-if="scope.row.amounts[index].editFlag" style="margin-left:10px; color:black;" class="cell-icon" @click="handleSave(scope.row, index, scope.$index)"> <i class="el-icon-check"></i> </span>
                  <span v-if="scope.row.amounts[index].editFlag" style="margin-left:10px; color:black;" class="cell-icon" @click="handleCancel( scope.row, index)"> <i class="el-icon-close"></i> </span>
                </span>
                <span v-else>
                  ---
                </span>
                  <!-- span>{{formatMoney( scope.row.budgetItem.amounts[index].amount,1) }}</span -->
                </template>

            </el-table-column>

            <el-table-column
              label="附加费用"
              prop="sum"
              :formatter="formatAmount"
              align = "center"
              width="120">
              <template slot-scope="scope">
                {{ formatMoney( scope.row.amountSum,1) }}元
              </template>
            </el-table-column>


            <el-table-column
              label="总计"
              prop="sum"
              :formatter="formatAmount"
              align = "center"
              width="150">
              <template slot-scope="scope">
                {{scope.row.manMonthSum}}人月<br/>
                {{ formatMoney( scope.row.amountSum,1) }}元
              </template>
            </el-table-column>

            <el-table-column
              label="操作"
              width="195">
              <template slot-scope="scope">
                <el-button v-show="scope.row.changed" 
                   @click="updatePrjBudget(scope.row, scope.$index)" 
                  type="danger" 
                   size="mini">提交修改
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-pagination
              background
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount">
            </el-pagination>
          </div>
      
 

  </div>
</template>




<script>
export default {
  mounted: function() {
    //     this.loadData();
  },

  methods: {
    loadData() {
      var _this = this;
      this.loading = true;

      var url =
        "/budget/dep/prj/paged?depId=" +
        this.depId +
        "&size=" +
        this.pageSize +
        "&page=" +
        this.page;
      // var url = "/budget/dep/prj/" + this.depId;

      this.getRequest(url).then(resp => {
        if (resp && resp.status == 200) {
          //     console.log(resp.data);

          this.totalCount = resp.data.totalElements;
          _this.initData(resp.data.content);
          _this.prjBudgets = resp.data.content;
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

    currentChange(currentChange) {
      this.currentPage = currentChange;
      this.page = currentChange - 1;
      this.loadData();
    },

    initData(budgets) {
      this.loading = false;
      var amountSum = 0.0,
        manMonthSum = 0.0;

      //     console.log("init project budgets.....");
      //     console.log(budgets);

      var sortedPrjBudgets = new Array(budgets.length);

      for (var idx = 0; idx < budgets.length; idx++) {
        var prjBudget = budgets[idx];

        sortedPrjBudgets[idx] = {
          id: prjBudget.id,
          depId: prjBudget.depId,
          prjId: prjBudget.prjId,
          prjNo: prjBudget.prjNo,
          prjName: prjBudget.prjName,
          avgManMonthCost: prjBudget.avgManMonthCost,
          amountSum: 0,
          manMonthSum: 0.0,
          editFlag: false,
          changed: false,
          amounts: new Array(12)
        };

        var amounts = sortedPrjBudgets[idx].amounts;

        //       console.log(amounts);

        for (var k = 0; k < 12; k++) amounts[k] = null; //this.emptyMonthBudget;

        for (var k = 0; k < prjBudget.monthBudgets.length; k++) {
          var ii = prjBudget.monthBudgets[k].month;
          amounts[ii] = prjBudget.monthBudgets[k];
        }
        //       console.log(amounts);

        var amountSum = 0.0;
        var manMonthSum = 0.0;
        var tmp = 0;
        var avg = prjBudget.avgManMonthCost;

        for (var i = 0; i < 12; i++) {
          if (amounts[i] == null) continue;

          this.$set(amounts[i], "editFlag", false);
          tmp = amounts[i].manMonth * avg;
          amounts[i].amount = tmp;
          amountSum += tmp;
          manMonthSum += amounts[i].manMonth;
          //          budgetItem.amounts[i].editFlag = false;
        }
        sortedPrjBudgets[idx].amountSum = amountSum;
        sortedPrjBudgets[idx].manMonthSum = manMonthSum;
      }

      this.sortedPrjBudgets = sortedPrjBudgets;

      //      console.log(sortedPrjBudgets);
    },

    cellClicked(row, column, cellValue) {
      this.currentItemLabel = row.name;
      this.editingRow = row;
      this.editingProp = column.property.split(".");

      var tmp = row;
      for (var i = 0; i < this.editingProp.length; i++) {
        tmp = tmp[this.editingProp[i]];
      }
      this.amount = tmp; //row[this.editingProp];
      //console.log( this.editingProp );
      // console.log(row);
      // console.log(column);
      //console.log(cellValue);
      this.dialogVisible = true;
    },

    handleEdit: function(row, idx) {
      // var tmp = row.budgetItem.amounts[idx];
      // console.log( tmp );
      // this.$set(tmp, "editFlag", true);

      if (idx == -1) {
        row.editFlag = true;
        this.inputColumn1 = row.avgManMonthCost;
      } else {
        if (row.amounts[idx] != null) {
          row.amounts[idx].editFlag = true;
          this.inputColumn1 = row.amounts[idx].manMonth;
        }
      }
      //  row.budgetItem.amounts[idx].amount = 12; //this.inputColumn1;
      // console.log( row.budgetItem.amounts[idx] );
    },

    handleSave: function(row, index, idx) {
      if (index == -1) {
        row.editFlag = false;
        row.avgManMonthCost = parseFloat(this.inputColumn1);

        this.prjBudgets[idx].avgManMonthCost = row.avgManMonthCost;

        for (var i = 0; i < 12; i++) {
          if (row.amounts[i] != null) {
            row.amounts[i].amount = parseFloat(
              row.amounts[i].manMonth * row.avgManMonthCost
            );
          }
        }
        row.amountSum = parseFloat(row.manMonthSum * row.avgManMonthCost);
      } else {
        row.amounts[index].editFlag = false;
        var f1 = 0.0,
          f2 = 0.0,
          f3 = 0.0,
          f4 = 0.0;
        f1 = parseFloat(row.manMonthSum);
        f2 = parseFloat(this.inputColumn1);
        f3 = parseFloat(row.amounts[index].manMonth);
        f4 = parseFloat(row.avgManMonthCost);
        var ff = f1 - f3 + f2;

        // console.log(ff);

        row.manMonthSum = ff; //parseFloat(row.manMonthSum) - parseFloat(row.amounts[index].manMonth) +  parseFloat( this.inputColumn1);
        row.amountSum = ff * f4; //parseFloat( row.manMonthSum * row.avgManMonthCost);
        // console.log(row.manMonthSum);
        // console.log(row.amountSum);

        row.amounts[index].manMonth = f2; //parseFloat( this.inputColumn1);
        row.amounts[index].amount = f2 * f4; //parseFloat( row.amounts[index].manMonth * row.avgManMonthCost);
      }
      row.changed = true;
    },

    handleCancel: function(row, idx) {
      if (idx == -1) row.editFlag = false;
      else row.amounts[idx].editFlag = false;
    },

    handleMouseEnter: function(row, column, cell, event) {
      var childs = cell.getElementsByTagName("i");
      if (childs.length == 0) return;
      childs[0].style.color = "black";

      // var idx = column.property;
      // if (idx == "avgManMonthCost") {
      //   if (!row.editFlag)
      //     cell.children[0].children[0].children[1].style.color = "black";
      // } else if (idx >= 1 && idx <= 12) {
      //   if (row.amounts[idx - 1] != null && !row.amounts[idx - 1].editFlag)
      //     //& row.budgetItem.refItem == -1 )
      //     cell.children[0].children[0].children[1].style.color = "black";
      // }
    },

    handleMouseOut: function(row, column, cell, event) {
      var childs = cell.getElementsByTagName("i");
      if (childs.length == 0) return;
      childs[0].style.color = "#ffffff";

      // var idx = column.property;
      // if (idx == "avgManMonthCost") {
      //   if (!row.editFlag)
      //     cell.children[0].children[0].children[1].style.color = "#ffffff";
      // } else if (idx >= 1 && idx <= 12) {
      //   if (row.amounts[idx - 1] != null && !row.amounts[idx - 1].editFlag)
      //     // & row.budgetItem.refItem == -1 )
      //     cell.children[0].children[0].children[1].style.color = "#ffffff";
      // }
    },

    updatePrjBudget: function(row, idx) {
      var _this = this;
      this.tableLoading = true;
      this.postJsonRequest("budget/dep/prj", this.prjBudgets[idx]).then(
        resp => {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({ type: data.status, message: data.msg });
            row.changed = false;
          }
        }
      );
    }
  },

  data() {
    return {
      loading: false,
      prjBudgets: [], //来自服务器

      sortedPrjBudgets: [], //前端整理后的

      depId: null,

      tableLoading: false,
      dialogVisible: false,
      currentItemLabel: "",
      amount: 0,
      editingRow: null,
      editingProp: "",

      totalCount: -1,
      currentPage: 1,
      page: 0,
      pageSize: 10,

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
  }
};
</script>


<style>
.cell-icon {
  cursor: pointer;
  color: #fff;
}
</style>