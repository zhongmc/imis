<template>
<div>
  <el-form :model="commonBudgetItem" :rules="rules" ref="addCommBudgetForm" style="margin: 0px;padding: 0px;">

        <el-row>
            <el-col :span="6">
                <el-form-item label="费用项名称:" prop="name">
                  <el-input v-model="commonBudgetItem.name" size="mini" style="width: 150px"
                            placeholder="请输入新的费用项名称"></el-input>
                </el-form-item>
            </el-col>

            <el-col :span="6" >
                <el-form-item label="费用金额:" prop="amount">
                  <el-input v-model="commonBudgetItem.amount" size="mini" style="width: 150px"
                            placeholder="金额"></el-input>
                </el-form-item>
           </el-col>
          <el-col   :span="6">
            <el-form-item prop="expectDate" label="预计发生时间:">
                 <el-date-picker
                    v-model="commonBudgetItem.expectDate"
                    size="mini"
                    style="width: 130px"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="预计发生日期">
                  </el-date-picker>

            </el-form-item>
          </el-col>
        </el-row>
          <el-row>
          <el-col  :span="12">
            <el-form-item label="备注:" prop="desc">
              <el-input v-model="commonBudgetItem.desc" size="mini" placeholder="备注..." style="width:300px;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">          
             <el-button type="primary" size="mini" @click="cancelEditCommonItem()" v-if="editing">取 消</el-button>      
             <el-button type="primary" size="mini" @click="addCommBudgetItem('addCommBudgetForm')">{{buttonLabel}}</el-button>
         </el-col>
        </el-row>
    </el-form>
 


          <el-table
            :data="commonBudges"
            v-loading="tableLoading"
            border
            stripe
            size="mini"
            style="width: 100%">

            <el-table-column
              prop="name"
              align="left"
              label="预算费用名称"
              width="160">
            </el-table-column>
            <el-table-column
              prop="expectDate"
              width="100"
              align="left"
              label="预计发生时间">
              <template slot-scope="scope">{{ scope.row.expectDate | formatDate}}</template>

            </el-table-column>

            <el-table-column
              prop="amount"
              width="90"
              align="right"
              :formatter="formatAmount"
              label="金额">
            </el-table-column>

            <el-table-column
              prop="desc"
              width="200"
              
              label="备注">
            </el-table-column>

            <el-table-column
              label="操作"
              width="150">
              <template slot-scope="scope">
                <el-button @click="editCommonBudget(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="mini">编辑
                </el-button>

               <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                           @click="deleteCommonBudget(scope.row)">删除
                </el-button>

              </template>
            </el-table-column>
          </el-table>


  </div>
</template>




<script>
export default {
  mounted: function() {
    //      this.loadData();
  },

  methods: {
    loadData() {
      var _this = this;
      this.loading = true;

      this.getRequest("/budget/dep/cost/" + this.depId).then(resp => {
        //          _this.loading = false;
        if (resp && resp.status == 200) {
          _this.commonBudges = resp.data;
        }
      });
    },

    changeDep(depId) {
      this.depId = depId;
      this.loadData();
    },

    showAddCommBudgetView() {
      this.addViewVisible = !this.addViewVisible;
      if (!this.addViewVisible) {
        this.emptyBudgetItemData();
      }
    },

    emptyBudgetItemData() {
      this.commonBudgetItem.id = -1;
      this.commonBudgetItem.name = "";
      this.commonBudgetItem.expectDate = "";
      this.commonBudgetItem.amount = 0;
      this.commonBudgetItem.desc = "";
      this.commonBudgetItem.depId = this.depId;
    },

    addCommBudgetItem(formName) {
      var _this = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          //添加
          this.tableLoading = true;
          this.postRequest("/budget/dep/cost", this.commonBudgetItem).then(
            resp => {
              _this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                _this.$message({ type: data.status, message: data.msg });
                _this.emptyBudgetItemData();
                _this.loadData();
              }
            }
          );
        }
      });
    },

    deleteCommonBudget(row) {
      var _this = this;
      this.tableLoading = true;
      this.deleteRequest();
    },

    deleteCommonBudget(row) {
      this.$confirm("此操作将永久删除[" + row.name + "], 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.doDelete(row.id);
        })
        .catch(() => {});
    },

    doDelete(ids) {
      this.tableLoading = true;
      var _this = this;
      var url = "/budget/dep/" + ids;
      this.deleteRequest(url).then(resp => {
        _this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
          _this.loadData();
        }
      });
    },

    editCommonBudget(row) {
      this.editing = true;
      this.buttonLabel = "提交修改";

      this.commonBudgetItem.id = row.id;
      this.commonBudgetItem.name = row.name;
      this.commonBudgetItem.amount = row.amount;
      this.commonBudgetItem.desc = row.desc;
      this.commonBudgetItem.expectDate = row.expectDate;
      this.commonBudgetItem.depId = row.depId;
    },

    cancelEditCommonItem() {
      this.editing = false;
      this.buttonLabel = "确定新增";
      this.emptyBudgetItemData();
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
  }, //methods

  data() {
    return {
      loading: false,
      commonBudges: [],
      tableLoading: false,
      addViewVisible: false,
      faangledoubleup: "fa-angle-double-up",
      faangledoubledown: "fa-angle-double-down",

      depId: null,

      buttonLabel: "确定新增",
      editing: false,

      commonBudgetItem: {
        id: -1,
        name: "",
        amount: 0,
        expectDate: "",
        desc: ""
      },
      rules: {
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        amount: [{ required: true, message: "必填:金额", trigger: "blur" }],
        expectDate: [
          { required: true, message: "必填:发生日期", trigger: "blur" }
        ]
      }
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