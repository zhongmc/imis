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
                :disable-transitions="false">{{dep.name}}
              </el-tag>
              <el-popover
                placement="right"
                title="部门列表"
                width="200"
                @hide="updateSelDepes()"
               
                trigger="click">
                <el-select v-model="selDepIds" multiple placeholder="请选择部门">
                  <el-option
                    v-for="item in myDeps"
                      :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
                <el-button type="text" icon="el-icon-more" style="color: #09c0f6;padding-top: 0px" slot="reference"
                           @click="loadSelDeps()" :disabled="moreBtnState"></el-button>
              </el-popover>

         <el-button type="success" size="mini" :disabled="selDepIds.length==0" @click="loadBudgetTable">加载数据
          </el-button>
        
    </div>


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
              width="185"
              align="left"
              label="">
            </el-table-column>

            <el-table-column
              v-for="(item, index ) in tableDeps" :key="index"
              :label="tableDeps[index].name" 
               width="180" 
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
    this.loadMyDeps();
  },

  methods: {
    loadMyDeps() {
      var _this = this;
      this.loading = true;
      this.getRequest("/system/basic/myDeps").then(resp => {
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
      for (var i = 0; i < this.myDeps.length; i++) {
        for (var j = 0; j < this.selDepIds.length; j++) {
          if (this.myDeps[i].id == this.selDepIds[j]) {
            this.selDeps[k++] = this.myDeps[i];
            break;
          }
        }
      }
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
            console.log(resp.data);
          }
        }
      );
    }
  },

  data() {
    return {
      loading: false,
      tableLoading: false,
      budgetTable: [],

      moreBtnState: false,
      selDepIds: [],
      selDeps: [],
      tableDeps: [],
      myDeps: []
    };
  }
};
</script>
