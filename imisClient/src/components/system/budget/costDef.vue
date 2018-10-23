<template>
  <div>
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="showAddCostItemDefView">添加</el-button>
    <div style="margin-top: 10px">
      <el-table
        v-loading="loading"
        :data="costItems"
        size="mini"
        stripe
        border
        @selection-change="handleSelectionChange"
        >
        <el-table-column
          type="selection"
          width="55"
          align="left">
        </el-table-column>
        <el-table-column
          prop="id"
          label="编号"
          width="80"
          align="left">
        </el-table-column>

        <el-table-column
          prop="groupId"
          label="费用类别"
          :formatter="formatCostGroup"
          width="180"
          align="left">
        </el-table-column>

        <el-table-column
          prop="name"
          label="名称"
          width="180"
          align="left">
        </el-table-column>
        <el-table-column
          width="120"
          label="是否参与统计"
          prop="beAmount"
          :formatter="formatBeAmount"
          align="left">
          </el-table-column>
        <el-table-column
          width="80"
          label="计算公式"
          prop="amountFormula"
          align="left">
          </el-table-column>

        <el-table-column
          width="180"
          label="备注"
          prop="desc"
          align="left">
          </el-table-column>

        <el-table-column label="操作" align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="text-align: left;margin-top: 10px">
      <el-button type="danger" size="mini" :disabled="multipleSelection.length==0"
                 @click="deleteMany">批量删除
      </el-button>
    </div>

    <el-form :model="costItemDef" :rules="rules" ref="addItemForm" style="margin: 0px;padding: 0px;">

    <div style="text-align: left">
      <el-dialog
        :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
        :visible.sync="dialogVisible"
        width="55%">
        <el-row>
            <el-col :span="12">
                <el-form-item label="费用项名称:" prop="name">
                  <el-input prefix-icon="el-icon-edit" v-model="costItemDef.name" size="mini" style="width: 150px"
                            placeholder="请输入新的费用项名称"></el-input>
                </el-form-item>
            </el-col>

            <el-col :span="12" >
                <el-form-item label="费用分类:" prop="group">
                  <el-select v-model="costItemDef.groupId" size="mini"
                            placeholder="费用分类">
                    <el-option
                      v-for="cg in costGroups"
                      :key="cg.id"
                      :label="cg.name"
                      :value="cg.id">
                    </el-option>
                  </el-select>
                </el-form-item>
           </el-col>
        </el-row>

        <el-row>
          <el-col   :span="6">
            <el-form-item prop="beAmount">
               <el-checkbox v-model="costItemDef.beAmount">参与总数统计</el-checkbox>
            </el-form-item>
          </el-col>

          <el-col   :span="12">
            <el-form-item label="总数计算公式:" prop="amountFormula">
              <el-input v-model="costItemDef.amountFormula" size="mini" placeholder="总数计算公式..." style="width:150px;"></el-input>
            </el-form-item>
          </el-col>

           <el-col :span="6" >
              <el-form-item>
                  <el-select v-model="formulaItemId" size="mini"
                            placeholder="添加公式参数" @change="addFormulaVar">
                    <el-option
                      v-for="cg in costItems"
                      :key="cg.id"
                      :label="cg.name"
                      :value="cg.id">
                    </el-option>
                  </el-select>
              </el-form-item>
           </el-col>

          </el-row>
          <el-row>
          <el-col  :span="24">
            <el-form-item label="备注:" prop="desc">
              <el-input v-model="costItemDef.desc" size="mini" placeholder="备注..." style="width:300px;"></el-input>
            </el-form-item>
          </el-col>
          </el-row>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" size="mini" @click="addCostItemDef('addItemForm')">确 定</el-button>
        </span>
      </el-dialog>
    </div>

    </el-form>

  </div>
</template>
<script>
export default {
  mounted: function() {
    this.loadTableData();
  },
  methods: {
    addCostItemDef(formName) {
      var _this = this;
      console.log(this.costItemDef);

      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.costItemDef.id) {
            //更新
            this.tableLoading = true;
            this.putRequest("/system/budget/costItem", this.costItemDef).then(
              resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({ type: data.status, message: data.message });
                  _this.dialogVisible = false;
                  _this.emptyCostDefItemData();
                  _this.loadTableData();
                }
              }
            );
          } else {
            //添加
            this.tableLoading = true;
            this.postRequest("/system/budget/costItem", this.costItemDef).then(
              resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({ type: data.status, message: data.message });
                  _this.dialogVisible = false;
                  _this.emptyCostDefItemData();
                  _this.loadTableData();
                }
              }
            );
          }
        } else {
          return false;
        }
      });
    },

    deleteMany() {
      var _this = this;
      this.$confirm(
        "删除" + this.multipleSelection.length + "条数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          var multipleSelection = _this.multipleSelection;
          var ids = "";
          multipleSelection.forEach(row => {
            ids = ids + row.id + ",";
          });
          _this.doDelete(ids);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },

    formatCostGroup(row, column) {
      return row.group.name;
      // for(var i=0; i<this.costGroups.length; i++)
      // {
      //   if( this.costGroups[i].id == row.groupId )
      //     return this.costGroups[i].name;
      // }
      // return "unknow";
    },

    formatBeAmount(row, column) {
      return row.beAmount == true ? "参与合计" : "不参与合计";
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    showAddCostItemDefView() {
      this.dialogTitle = "添加费用定义信息";
      this.emptyCostDefItemData();
      this.dialogVisible = true;
    },

    handleEdit(index, row) {
      console.log(row);

      this.dialogTitle = "编辑费用定义信息";
      this.costItemDef = row;
      this.dialogVisible = true;
    },
    handleDelete(index, row) {
      var _this = this;
      this.$confirm("删除[" + row.name + "], 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          _this.doDelete(row.id);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    doDelete(ids) {
      var _this = this;
      _this.loading = true;
      var url = "/system/budget/costItem/";
      this.deleteRequest(url + ids).then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.loadTableData();
        }
      });
    },

    loadTableData() {
      var _this = this;
      this.loading = true;
      this.getRequest("/system/budget/settings").then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.costItems = resp.data.costItems;
          _this.costGroups = resp.data.costGroups;
          console.log(resp.data);
          console.log(_this.costItems);
        }
      });
    },

    addFormulaVar() {
      if (this.formulaItemId == null) return;

      if (this.costItemDef.amountFormula != null)
        this.costItemDef.amountFormula += "#" + this.formulaItemId;
      else this.costItemDef.amountFormula = "#" + this.formulaItemId;

      this.formulaItemId = null;
    },

    emptyCostDefItemData() {
      this.costItemDef = {
        id: "",
        name: "",
        groupId: "",
        beAmount: false,
        amountFormula: "",
        desc: ""
      };
      this.formulaItemId = null;
    }
  },

  data() {
    return {
      itemName: "",
      updateItemName: "",
      groupId: -1,
      updateGroupId: -1,
      beAmount: true,
      updateBeAmount: false,
      desc: "",
      updateDesc: "",
      amountFormula: "",
      updateAmountFormula: "",
      loading: false,
      dialogVisible: false,
      dialogTitle: "编辑费用定义",
      multipleSelection: [],
      costItemDef: {
        id: "",
        name: "",
        groupId: "",
        beAmount: false,
        amountFormula: "",
        desc: ""
      },

      costItems: [],
      costGroups: [],

      //初始化为null，select 才显示placeholder
      formulaItemId: null,

      rules: {
        name: [
          { required: true, message: "必填:费用定义名称", trigger: "blur" }
        ],
        groupId: [{ required: true, message: "必填:请选择", trigger: "blur" }]
      }
    };
  },
  props: ["state"]
};
</script>
