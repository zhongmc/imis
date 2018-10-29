<!-- 部门费用预算汇总表  -->
<template>
<div>

          <el-table
            :data="costBudgets"
            v-loading="tableLoading"
            border
            stripe

:header-row-style="{
    'background-color': '#efefef',
    'color': 'rgb(103, 194, 58)',
    'border-bottom': '1px rgb(103, 194, 58) solid'
}"            
            size="mini"
            style="width: 100%">

            <el-table-column
              prop="groupName"
              align="left"
              fixed
              label="费用类别"
              width="90">
            </el-table-column>
            <el-table-column
              prop= "name"
              fixed
              width="85"
              align="left"
              label="费用名称">
            </el-table-column>

            <el-table-column
              label="总计"
              prop="sum"
              :formatter="formatAmount"
              align = "right"
              width="120">
            </el-table-column>

            <el-table-column
              v-for="(item, index ) in tableTitles" :key="index"
              :label="tableTitles[index].title" 
              align = "right"
              :prop=tableTitles[index].idx width="100" >

                <template slot-scope="scope">
                  <span>
                    {{ formatMoney( scope.row.amounts[index], 1) }}
                  </span>     
                  <!-- span>{{formatMoney( scope.row.amounts[index].amount,1) }}</span -->
                </template>

            </el-table-column>


            <el-table-column
              prop="desc"
              label="备注"
              width="80">
            </el-table-column>

          </el-table>

 
  </div>
</template>




<script>
export default {
  mounted: function() {},

  methods: {
    loadData() {
      var _this = this;
      this.loading = true;

      _this.costBudgetInfos = [];
      this.getRequest("/budget/dep/costColl/" + this.depId).then(resp => {
        if (resp && resp.status == 200) {
          _this.costBudgets = resp.data;
          console.log("cost load ok!");
          console.log(resp.data);
        }
      });
    },

    changeDep(depId) {
      this.depId = depId;
      this.loadData();
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
    }
  },

  data() {
    return {
      loading: false,
      costBudgets: [], //费用预算定义，经前端按 costItem 整理
      tableLoading: false,

      depId: null,

      amount: 0,

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