<!-- 部门预算汇总 -->
<template>
  <div style="margin-top: 10px">
    <div style="padding:10px;">
      <label>请选择部门：</label>

      <el-tag
        v-for="dep in selDeps"
        :key="dep.id"
        type="success"
        size="mini"
        style="margin-right: 15px"
        :disable-transitions="false"
      >{{dep.name}}</el-tag>
      <el-popover
        placement="right"
        title="部门列表"
        width="200"
        @hide="updateSelDepes()"
        trigger="click"
      >
        <el-select v-model="selDepIds" multiple placeholder="请选择部门">
          <el-option v-for="item in myDeps" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-button
          type="text"
          icon="el-icon-more"
          style="color: #09c0f6;padding-top: 0px"
          slot="reference"
          @click="loadSelDeps()"
          :disabled="moreBtnState"
        ></el-button>
      </el-popover>

      <el-button
        type="success"
        size="mini"
        :disabled="selDepIds.length==0"
        @click="loadBudgetTable"
      >加载数据</el-button>

      <el-button
        type="success"
        size="mini"
        :disabled="selDepIds.length==0"
        @click="exportBudgetTable"
      >
        <i class="fa fa-lg fa-level-down" style="margin-right: 5px"></i>导出数据
      </el-button>
    </div>

    <el-table
      :data="budgetTable"
      v-loading="tableLoading"
      border
      stripe
      size="mini"
      @selection-change="handleSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" align="left" width="30"></el-table-column>

      <el-table-column prop="name" fixed width="185" align="left" label></el-table-column>

      <el-table-column
        v-for="(item, index ) in tableDeps"
        :key="index"
        :label="tableDeps[index].name"
        width="180"
        align="right"
      >
        <template slot-scope="scope">
          <span>{{ formatMoney( scope.row.amounts[index],1) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex;justify-content: space-between;margin: 2px;">
      <el-button
        type="danger"
        size="mini"
        :disabled="multipleSelection.length==0"
        @click="showEchart"
      >图表显示</el-button>
    </div>

    <el-dialog
      title="比较图表"
      :visible.sync="chartDialogVisible"
      @close="closeEchart"
      @open="initChart"
      width="90%"
    >
      <div ref="myEchart" style="width:800px;height:500px;"></div>
    </el-dialog>
  </div>
</template>



<script>
let echarts = require("echarts/lib/echarts");

require("echarts/lib/chart/bar");
require("echarts/lib/component/tooltip");
require("echarts/lib/component/toolbox");
require("echarts/lib/component/legend");
require("echarts/lib/component/markLine");

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

    updateSelDepes() {
      this.moreBtnState = false;
      this.selDeps = [];
      var k = 0;
      for (var j = 0; j < this.selDepIds.length; j++) {
        for (var i = 0; i < this.myDeps.length; i++) {
          if (this.myDeps[i].id == this.selDepIds[j]) {
            this.selDeps[k++] = this.myDeps[i];
            break;
          }
        }
      }

      // for (var i = 0; i < this.myDeps.length; i++) {
      //   for (var j = 0; j < this.selDepIds.length; j++) {
      //     if (this.myDeps[i].id == this.selDepIds[j]) {
      //       this.selDeps[k++] = this.myDeps[i];
      //       break;
      //     }
      //   }
      // }
    },

    loadSelDeps() {
      this.moreBtnState = true;
    },

    loadBudgetTable() {
      var _this = this;
      this.budgetTable = [];
      this.tableDeps = [];
      this.tableLoading = true;
      this.postRequest("/budget/dep/chart", { depIds: this.selDepIds }).then(
        resp => {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            for (var i = 0; i < _this.selDeps.length; i++) {
              _this.tableDeps[i] = _this.selDeps[i];
            }
            _this.budgetTable = resp.data;
            //         console.log(resp.data);
          }
        }
      );
    },

    exportBudgetTable() {
      //        var iframe = document.createElement("iframe");
      //        iframe.style.display = 'none';
      //        iframe.src = "/employee/basic/exportEmp";
      //        iframe.onload=function () {
      //          document.body.removeChild(iframe);
      //        }
      //        document.body.appendChild(iframe);

      var url = "/budget/dep/chartExport?depIds=" + this.selDepIds[0];
      for (var i = 1; i < this.selDepIds.length; i++)
        url = url + "&depIds=" + this.selDepIds[i];
      window.open(url, "_parent");
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
      //    console.log(val);
    },

    showEchart() {
      this.chartDialogVisible = true;
    },

    initChart() {
      this.$nextTick(() => {
        //   console.log("init chart...");
        //        console.log(this.$refs.myEchart);
        this.chart = echarts.init(this.$refs.myEchart);
        var legends = [];
        var catalogs = [];
        var series = [];

        for (
          var i = 0;
          i < this.selDeps.length;
          i++ //xAxis
        ) {
          catalogs[i] = this.selDeps[i].name;
        }
        //        console.log(catalogs);
        for (var i = 0; i < this.multipleSelection.length; i++) {
          legends[i] = this.multipleSelection[i].name;
          series[i] = {
            name: this.multipleSelection[i].name,
            type: "bar",
            data: this.multipleSelection[i].amounts
          };
        }
        //      console.log(series);
        this.chart.setOption({
          title: { text: "对比" },
          tooltip: { trigger: "axis" },
          legend: {
            data: legends
          },
          xAxis: { data: catalogs },
          yAxis: {},
          series: series
        });
      });
    },

    closeEchart() {
      if (this.chart) {
        this.chart.dispose();
        this.chart = null;
      }
      this.chartDialogVisible = false;
    }
  },

  data() {
    return {
      loading: false,
      tableLoading: false,

      chartDialogVisible: false,

      budgetTable: [],

      multipleSelection: [],

      moreBtnState: false,
      selDepIds: [],
      selDeps: [],
      tableDeps: [],
      myDeps: [],

      chart: null
    };
  }
};
</script>
