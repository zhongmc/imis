<!-- 部门预算汇总 -->
<template>
  <div style="margin-top: 10px">
    <div style="padding:10px;">
      <label>请选择部门：</label>

      <el-select
        v-model="depId"
        style="width: 130px"
        size="mini"
        placeholder="请选择部门"
        @change="changeDep"
      >
        <el-option v-for="item in myDeps" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>

      <el-button type="success" size="mini" @click="exportBudgetTable">
        <i class="fa fa-lg fa-level-down" style="margin-right: 5px"></i>导出数据
      </el-button>
    </div>

    <el-tabs type="card" v-model="activeTab">
      <el-tab-pane label="部门预算表" name="depBudget">
        <dep-budget ref="depBudget"></dep-budget>
      </el-tab-pane>
      <el-tab-pane label="经营费用汇总" name="depCost">
        <dep-cost budgetTypeId="-1" v-bind:editable="false" ref="depCost"></dep-cost>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>



<script>
import DepBudgetTable from "./DepBudgetTable.vue";
import DepCostCollect from "./DepCostCollect.vue";

export default {
  mounted: function() {
    this.loadMyDeps();
  },

  methods: {
    loadMyDeps() {
      var _this = this;
      this.loading = true;
      this.getRequest("/config/dep/myDeps").then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.myDeps = resp.data;
        }
      });
    },

    changeDep: function(depId) {
      // console.log(this.depId);
      // console.log(this.$refs.cost);
      this.depId = depId;
      this.$refs.depBudget.changeDep(depId);
      this.$refs.depCost.changeDep(depId);
    },

    exportBudgetTable() {
      //        var iframe = document.createElement("iframe");
      //        iframe.style.display = 'none';
      //        iframe.src = "/employee/basic/exportEmp";
      //        iframe.onload=function () {
      //          document.body.removeChild(iframe);
      //        }
      //        document.body.appendChild(iframe);
      if (this.depId == null || this.depId == "") {
        this.$message({
          type: "error",
          message: "Please select a department!"
        });
        return;
      }

      var url = "/budget/budgetTable/export/" + this.depId;
      window.open(url, "_parent");
    }
  },

  components: {
    "dep-budget": DepBudgetTable,
    "dep-cost": DepCostCollect
  },

  data() {
    return {
      loading: false,

      myDeps: [],
      depId: "",
      activeTab: "depBudget"
    };
  }
};
</script>
