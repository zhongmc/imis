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
    </div>

    <el-tabs type="card" v-model="activeTab">
      <el-tab-pane label="项目预算" name="prjBudget">
        <project-budget ref="prjBudget"></project-budget>
      </el-tab-pane>

      <el-tab-pane label="机会预算" name="chanceBudget">
        <project-budget ref="chanceBudget" type="chance"></project-budget>
      </el-tab-pane>

      <el-tab-pane
        v-for="(item ) in budgetTypes"
        :key="item.id"
        :label="item.name"
        :name="item.id.toString()"
      >
        <expense-budget
          :budgetTypeId="item.id"
          :bgsettings="bgSettings"
          :editable="true"
          ref="cost"
        ></expense-budget>
        <!-- component :is=item.content></component -->
      </el-tab-pane>

      <el-tab-pane label="公共预算" name="comBudget" v-if="beCommonBudget">
        <common-budget ref="comm"></common-budget>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>



<script>
import ProjectBudget from "./ProjectBudget.vue";
import expenseBudget from "./ExpenseBudget.vue";
import commonBudget from "./CommonBudget.vue";

export default {
  mounted: function() {
    this.loadMyDeps();
    this.initData();
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

    initData() {
      var _this = this;
      this.loading = true;
      this.getRequest("/config/budget/settings").then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.bePrjBudget = resp.data.bePrjBudget;
          _this.beCommonBudget = resp.data.beCommonBudget;

          if (_this.bePrjBudget) _this.activeTab = "prjBudget";
          else _this.activeTab = "" + resp.data.budgetTypes[0].id;

          _this.budgetTypes = resp.data.budgetTypes;
          _this.bgSettings = resp.data;
        }
      });
    },

    changeDep: function(depId) {
      // console.log(this.depId);
      // console.log(this.$refs.cost);

      this.$refs.prjBudget.changeDep(depId); //depId
      this.$refs.chanceBudget.changeDep(depId);
      this.$refs.comm.changeDep(depId); //depId

      for (var i = 0; i < this.$refs.cost.length; i++) {
        this.$refs.cost[i].changeDep(depId);
      }
    }
  },

  components: {
    "project-budget": ProjectBudget,
    "expense-budget": expenseBudget,
    "common-budget": commonBudget
  },

  data() {
    return {
      myDeps: [],
      depId: "",

      loading: false,
      budgetTypes: [],
      budgetType: {
        id: -1,
        name: "",
        desc: ""
      },
      activeTab: "prjBudget",
      bePrjBudget: false,
      beCommonBudget: false,
      bgSettings: null
    };
  }
};
</script>
