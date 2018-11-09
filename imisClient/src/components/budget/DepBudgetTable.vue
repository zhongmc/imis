<!-- 部门预算汇总表（损益表） -->
<template>
<div>

          <el-table
            :data="budgetTable"
            v-loading="tableLoading"
            border
            stripe
            size="mini"
            style="width: 100%">

            <el-table-column
              prop="name"
              fixed
              width="85"
              align="left"
              label="">
            </el-table-column>

            <el-table-column
              label="总计"
              prop="sum"
               :formatter="formatAmount"
              align = "right"
              width="120">

                <template slot-scope="scope">
                  <span>{{ formatMoney( scope.row.sum,1) }}</span>
                </template>

            </el-table-column>

            <el-table-column
              v-for="(item, index ) in tableTitles" :key="index"
              :label="tableTitles[index].title" 
              :prop=tableTitles[index].idx width="100" 
              align = "right"
              >

                <template slot-scope="scope">
                  <span>{{ formatMoney( scope.row.amounts[index],1) }}</span>
                </template>

            </el-table-column>


          </el-table>
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

      this.getRequest("/budget/dep/budgetTable/" + this.depId).then(resp => {
        if (resp && resp.status == 200) {
          _this.budgetTable = resp.data;
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
    }
  },

  data() {
    return {
      loading: false,

      budgetTable: [],
      tableLoading: false,
      currentItemLabel: "",
      amount: 0,
      editingProp: "",
      depId: null,

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