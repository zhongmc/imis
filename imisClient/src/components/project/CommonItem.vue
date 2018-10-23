<template>

<div> 

<el-form :model="commonItem" :rules="rules" ref="addCommItemForm" style="margin: 0px;padding: 0px;">

        <el-row>
            <el-col :span="8">
                <el-form-item :label=label prop="name">
                  <el-input v-model="commonItem.name" size="mini" style="width: 150px"
                            :placeholder=placeHolder ></el-input>
                </el-form-item>
            </el-col>

            <el-col :span="6"  v-if="(type=='cost' | type=='income')">
                <el-form-item label="金额:" prop="amount">
                  <el-input v-model="commonItem.amount" size="mini" style="width: 150px"
                            placeholder="金额"></el-input>
                </el-form-item>
           </el-col>
          <el-col   :span="6">
            <el-form-item prop="bgDate" label="预计发生时间:">
                 <el-date-picker
                    v-model="commonItem.bgDate"
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
              <el-input v-model="commonItem.desc" size="mini" placeholder="备注..." style="width:300px;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">    
              <el-button type="primary" size="mini" @click="cancelEditCommonItem()" v-if="editing">取 消</el-button>      
             <el-button type="primary" size="mini" @click="addCommItem('addCommItemForm')">{{buttonLabel}}</el-button>
         </el-col>
        </el-row>
    </el-form>

          <el-table
            :data="commonItemes"
            v-loading="tableLoading"
             style="width: 100%;"
            border
            stripe
            size="mini"
            >

            <el-table-column
              prop="name"
              align="left"
              label="名称"
              width="250">
            </el-table-column>
            <el-table-column
              prop="bgDate"
              width="100"
              align="left"
              label="预计发生时间">
              <template slot-scope="scope">{{ scope.row.bgDate | formatDate}}</template>

            </el-table-column>

            <el-table-column  v-if="(type =='cost' || type == 'income')"
              prop="amount"
              width="100"
              align="right"
              :formatter="formatAmount"
              label="金额">
            </el-table-column>

            <el-table-column
              prop="desc"
              width="250"
              
              label="备注">
            </el-table-column>

            <el-table-column
              label="操作"
              >
              <template slot-scope="scope">
                <el-button @click="editCommonItem(scope.row)"
                      style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="mini">编辑
                </el-button>

               <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                           @click="deleteCommonItem(scope.row)">删除
                </el-button>

              </template>
            </el-table-column>
          </el-table>


  </div>
</template>




<script>
export default {
  beforeMount: function() {},

  mounted: function() {
    console.log(this.name + " prjId:" + this.prjid);
    this.placeHolder = "请输入新的" + this.name + "名称...";
    (this.label = this.name + "项名称："), this.loadData();
    this.commonItem.prjId = this.prjid;
  },

  methods: {
    loadData() {
      var _this = this;
      this.loading = true;
      var url = "/budget/project/" + this.type + "/" + this.prjid;
      console.log(url);

      this.getRequest(url).then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          _this.commonItemes = resp.data;
        }
      });
    },

    emptyCommonItemData() {
      this.commonItem.id = -1;
      this.commonItem.prjId = this.prjid;
      this.commonItem.name = "";
      this.commonItem.bgDate = "";
      this.commonItem.amount = 0;
      this.commonItem.desc = "";
    },

    addCommItem(formName) {
      var _this = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          //添加/修改
          this.tableLoading = true;
          var url = "/budget/project/" + this.type;
          this.postRequest(url, this.commonItem).then(resp => {
            _this.tableLoading = false;
            if (resp && resp.status == 200) {
              var data = resp.data;
              _this.$message({ type: data.status, message: data.message });
              _this.emptyCommonItemData();
              _this.loadData();
            }
          });
        }
      });
    },

    deleteCommonItem(row) {
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
      var url = "/budget/project/" + this.type + "/" + ids;
      this.deleteRequest(url).then(resp => {
        _this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
          _this.loadData();
        }
      });
    },

    editCommonItem(row) {
      this.editing = true;

      this.commonItem.id = row.id;
      this.commonItem.prjId = row.prjId;
      this.commonItem.name = row.name;
      this.commonItem.amount = row.amount;
      this.commonItem.desc = row.desc;
      this.commonItem.bgDate = row.bgDate;
      this.buttonLabel = "提交修改";
    },

    cancelEditCommonItem() {
      this.editing = false;
      this.buttonLabel = "确定新增";
      this.emptyCommonItemData();
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
      commonItemes: [],
      tableLoading: false,
      addViewVisible: false,
      placeHolder: "",
      label: "",
      amountLabel: "",
      buttonLabel: "确定新增",
      editing: false,

      faangledoubleup: "fa-angle-double-up",
      faangledoubledown: "fa-angle-double-down",

      commonItem: {
        id: -1,
        prjId: null,
        name: "",
        amount: 0,
        bgDate: "",
        desc: ""
      },

      rules: {
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        amount: [{ required: true, message: "必填:金额", trigger: "blur" }],
        bgDate: [{ required: true, message: "必填:发生日期", trigger: "blur" }]
      }
    };
  },

  props: ["prjid", "type", "name"]
};
</script>


<style>
.cell-icon {
  cursor: pointer;
  color: #fff;
}
</style>