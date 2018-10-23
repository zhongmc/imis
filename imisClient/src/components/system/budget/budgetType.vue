<template>
  <div>
    <div style="text-align: left">

      <el-button type="primary" icon="el-icon-plus" size="mini" @click="showAddBudgetTypeDialog">添加</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
        v-loading="loading"
        :data="bgTypeData"
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
          prop="name"
          label="名称"
          width="180"
          align="left">
        </el-table-column>
        <el-table-column
          prop="desc"
          label="备注"
          width="250"
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
      <el-button type="danger" size="mini" v-if="bgTypeData.length>0" :disabled="multipleSelection.length==0"
                 @click="deleteMany">批量删除
      </el-button>
    </div>

    
  <el-form :model="budgetType" label-width="150px" 
      label-position="right" :rules="rules" size="mini"
      ref="addBudgetTypeForm" style="margin: 0px;padding: 0px; padding-right:30px;">

    <div style="text-align: left">
      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="500px">


      <el-form-item label="费用预算类别名称:" prop="name">
        <el-input v-model="budgetType.name" size="20" placeholder="请输入新的预算类别名称..."></el-input>
      </el-form-item>

      <el-form-item label="备注:" prop="desc">
        <el-input v-model="budgetType.desc"  placeholder="请输入新的备注..." style="width:100%;"></el-input>
      </el-form-item>

        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" size="mini" @click="addBudgetType('addBudgetTypeForm')">确 定</el-button>
        </span>
    </el-dialog>
    </div>
    </el-form>

  </div>
</template>

<script>
  export default{
    mounted: function () {
      this.loadTableData();
    },
    methods: {
      addBudgetType(formName){

       var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.budgetType.id && this.budgetType.id != -1) {
              //更新
              this.tableLoading = true;
              this.putRequest("/system/budget/budgetType", this.budgetType).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyBudgetTypeData();
                  _this.loadTableData();
                }
              })
            } else {
              //添加
              this.tableLoading = true;
              this.postRequest("/system/budget/budgetType", this.budgetType).then(resp=> {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.$message({type: data.status, message: data.msg});
                  _this.dialogVisible = false;
                  _this.emptyBudgetTypeData();
                  _this.loadTableData();
                }
              })
            }
          } else {
            return false;
          }
        });
    },

      deleteMany(){
        var _this = this;
        this.$confirm('删除' + this.multipleSelection.length + '条数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var multipleSelection = _this.multipleSelection;
          var ids = '';
          multipleSelection.forEach(row=> {
            ids = ids + row.id + ',';
          })
          _this.doDelete(ids);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },

      emptyBudgetTypeData(){
        
        this.budgetType = {
          id:-1,
          name:'',
          desc:''
        }
      },


      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      showAddBudgetTypeDialog()
      {
        this.dialogTitle = "新加费用预算类别";
        this.emptyBudgetTypeData();
        this.dialogVisible = true;
      },

      handleEdit(index, row){
        this.dialogTitle = "编辑费用预算类别";
        this.budgetType = row;
        this.dialogVisible = true;
      },

      handleDelete(index, row){
        var _this = this;
        this.$confirm('删除[' + row.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.doDelete(row.id);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      doDelete(ids){
        var _this = this;
        _this.loading = true;
        var url = '/system/budget/budgetType/';
        this.deleteRequest(url + ids).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.loadTableData();
          }
        })
      },
      loadTableData(){
        var _this = this;
        this.loading = true;
       this.getRequest("/system/budget/settings").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.bgTypeData = resp.data.budgetTypes;
          }
        })
      }
    },

    data(){
      return {
        loading: false,
        dialogVisible: false,
        multipleSelection: [],
        bgTypeData: [],

        dialogTitle:'',
        budgetType:{
          id:-1,
          name:'',
          desc:''
        },

        rules:{
          name: [{required: true, message: '必填:费用预算名称！', trigger: 'blur'}],

        }

      }
    }
  }
</script>
