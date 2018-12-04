<template>
  <div>
    <el-container>
      <el-header
        style="padding: 0px;display:flex;justify-content:space-between;align-items: center"
      >
        <div style="display: inline">
          <el-input
            placeholder="客户名称搜索，记得回车哦..."
            clearable
            @change="keywordsChange"
            style="width: 300px;margin: 0px;padding: 0px;"
            size="mini"
            :disabled="advanceSearchViewVisible"
            @keyup.enter.native="searchPrj"
            prefix-icon="el-icon-search"
            v-model="keywords"
          ></el-input>
          <el-button
            type="primary"
            size="mini"
            style="margin-left: 5px"
            icon="el-icon-search"
            @click="searchPrj"
          >搜索</el-button>
          <el-button
            slot="reference"
            type="primary"
            size="mini"
            style="margin-left: 5px"
            @click="showAdvanceSearchView"
          >
            <i
              class="fa fa-lg"
              v-bind:class="[advanceSearchViewVisible ? faangledoubleup:faangledoubledown]"
              style="margin-right: 5px"
            ></i>高级搜索
          </el-button>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
          <el-upload
            :show-file-list="false"
            accept="application/vnd.ms-excel"
            action="/project/basic/importPrj"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError"
            :disabled="fileUploadBtnText=='正在导入'"
            :before-upload="beforeFileUpload"
            style="display: inline"
          >
            <el-button size="mini" type="success" :loading="fileUploadBtnText=='正在导入'">
              <i class="fa fa-lg fa-level-up" style="margin-right: 5px"></i>
              {{fileUploadBtnText}}
            </el-button>
          </el-upload>
          <el-button type="success" size="mini" @click="exportEmps">
            <i class="fa fa-lg fa-level-down" style="margin-right: 5px"></i>导出数据
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddPrjView">添加客户联系人</el-button>
        </div>
      </el-header>

      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <transition name="slide-fade">
            <div
              style="margin-bottom: 10px;border: 1px;border-radius: 5px;border-style: solid;padding: 15px;box-sizing:border-box;border-color: #20a0ff"
              v-show="advanceSearchViewVisible"
            >
              <el-form :inline="true" :model="advSearchData">
                <el-row>
                  <el-col :span="6">
                    <el-form-item label="所属客户:" prop="customId">
                      <el-select
                        v-model="advSearchData.customId"
                        style="width: 130px"
                        size="mini"
                        placeholder="请选择所属客户"
                      >
                        <el-option
                          v-for="item in customs"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="项目类型:">
                      <el-select
                        v-model="advSearchData.prjTypeId"
                        style="width: 130px"
                        size="mini"
                        placeholder="请选择项目类型"
                      >
                        <el-option
                          v-for="item in prjTypes"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9">
                    <el-form-item label="项目分类:" prop="prjClassId">
                      <el-radio-group v-model="advSearchData.prjClassId">
                        <el-radio label="全部">全部</el-radio>
                        <el-radio label="延续形">延续形</el-radio>
                        <el-radio style="margin-left: 15px" label="新项目">新项目</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row style="margin-top: 10px">
                  <el-col :span="6">
                    <el-form-item label="所属部门:" prop="departmentName">
                      <el-popover
                        v-model="showOrHidePop2"
                        placement="right"
                        title="请选择部门"
                        trigger="manual"
                      >
                        <el-tree
                          :data="deps"
                          :default-expand-all="true"
                          :props="defaultProps"
                          :expand-on-click-node="false"
                          @node-click="handleNodeClick2"
                        ></el-tree>
                        <div
                          slot="reference"
                          style="width: 130px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                          @click="showDepTree2"
                          v-bind:style="{color: depTextColor}"
                        >{{advSearchData.departmentName}}</div>
                      </el-popover>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="立项日期:" prop="beginDateScope">
                      <el-date-picker
                        v-model="advSearchData.beginDateScope"
                        unlink-panels
                        size="mini"
                        type="daterange"
                        value-format="yyyy-MM-dd"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                      ></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="5" :offset="2">
                    <el-button size="mini" @click="cancelSearch">取消</el-button>
                    <el-button
                      icon="el-icon-search"
                      type="primary"
                      size="mini"
                      @click="searchPrj"
                    >搜索</el-button>
                  </el-col>
                </el-row>
              </el-form>
            </div>
          </transition>

          <el-table
            :data="prjs"
            v-loading="tableLoading"
            border
            stripe
            @selection-change="handleSelectionChange"
            size="mini"
            style="width: 100%"
          >
            <el-table-column prop="customName" align="left" fixed label="客户名称" width="90"></el-table-column>
            <el-table-column prop="name" label="名称" width="250"></el-table-column>

            <el-table-column prop="departmentName" align="left" width="100" label="所属部门"></el-table-column>

            <el-table-column prop="costAmount" width="80" align="left" label="职位"></el-table-column>

            <el-table-column fixed="right" label="操作" width="195">
              <template slot-scope="scope">
                <el-button
                  @click="showEditPrjView(scope.row)"
                  style="padding: 3px 4px 3px 4px;margin: 2px"
                  size="mini"
                >编辑</el-button>

                <el-button
                  type="danger"
                  style="padding: 3px 4px 3px 4px;margin: 2px"
                  size="mini"
                  @click="deletePrj(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-pagination
              background
              :page-size="10"
              :current-page="currentPage"
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
    <el-form :model="prj" :rules="rules" ref="addPrjForm" style="margin: 0px;padding: 0px;">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="90%"
        >
          <el-row>
            <el-col :span="10">
              <el-form-item label="名称:" prop="name">
                <el-input v-model="prj.name" size="mini" style="width: 250px" placeholder="请输入项目名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编号:" prop="projectNo">
                <el-input
                  v-model="prj.projectNo"
                  size="mini"
                  style="width: 150px"
                  placeholder="请输入项目编号"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="6">
              <el-form-item label="所属客户:" prop="customId">
                <el-select
                  v-model="prj.customId"
                  style="width: 130px"
                  size="mini"
                  placeholder="请选择所属客户"
                >
                  <el-option
                    v-for="item in customs"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="项目类型:">
                <el-select
                  v-model="prj.prjTypeId"
                  style="width: 130px"
                  size="mini"
                  placeholder="请选择项目类型"
                >
                  <el-option
                    v-for="item in prjTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="项目分类:" prop="prjClassId">
                <el-radio-group v-model="prj.prjClassId">
                  <el-radio label="延续形">延续形</el-radio>
                  <el-radio label="新项目">新项目</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="10">
              <el-form-item label="所属部门:" prop="departmentName">
                <el-popover
                  v-model="showOrHidePop"
                  placement="right"
                  title="请选择部门"
                  trigger="manual"
                >
                  <el-tree
                    :data="deps"
                    :default-expand-all="true"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    @node-click="handleNodeClick"
                  ></el-tree>
                  <div
                    slot="reference"
                    style="width: 230px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                    @click="showDepTree"
                    v-bind:style="{color: depTextColor}"
                  >{{prj.departmentName}}</div>
                </el-popover>
              </el-form-item>
            </el-col>
          </el-row>

          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="cancelEidt">取 消</el-button>
            <el-button size="mini" type="primary" @click="addPrj('addPrjForm')">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      prjs: [],
      keywords: "",
      fileUploadBtnText: "导入数据",
      beginDateScope: "",
      faangledoubleup: "fa-angle-double-up",
      faangledoubledown: "fa-angle-double-down",
      dialogTitle: "",
      multipleSelection: [],
      depTextColor: "#c0c4cc",
      nations: [],
      politics: [],
      positions: [],
      joblevels: [],
      totalCount: -1,
      currentPage: 1,

      deps: [],

      defaultProps: {
        label: "name",
        isLeaf: "leaf",
        children: "children"
      },

      dialogVisible: false,
      tableLoading: false,
      advanceSearchViewVisible: false,
      showOrHidePop: false,
      showOrHidePop2: false,

      advSearchData: {
        name: "",
        customId: -1,
        customName: "请选择客户",
        departmentId: -1,
        departmentName: "请选择部门",
        prjTypeId: -1,
        prjClassId: 0,
        beginDateScope: ""
      },

      prj: {
        id: -1,
        projectNo: "",
        name: "",
        simpleName: "",
        contractNo: "",
        prjTypeId: -1,
        prjClassId: -1,
        departmentId: "",
        departmentName: "所属部门...",
        beginDate: "",
        endDate: "",

        customId: -1,
        customName: "",
        userName: "",

        totalPeopleMonth: 0,
        budgetPeopleMonth: 0,

        contractAmount: 0,
        costAmount: 0,
        labourAmount: 0,
        expenseAmount: 0,
        labourBudget: 0,
        expenseBudget: 0
      },

      rules: {
        name: [{ required: true, message: "必填:项目名称", trigger: "blur" }],
        projectNo: [
          { required: true, message: "必填:项目编号", trigger: "blur" }
        ],
        departmentId: [
          { required: true, message: "必填:所属部门", trigger: "change" }
        ],
        customId: [
          { required: true, message: "必填:所属客户", trigger: "change" }
        ],
        beginDate: [
          { required: true, message: "必填:启动日期", trigger: "blur" }
        ],
        endDate: [{ required: true, message: "必填:结束日期", trigger: "blur" }]
      }
    };
  },
  mounted: function() {
    this.initData();
    this.loadPrjs();
  },
  methods: {
    fileUploadSuccess(response, file, fileList) {
      if (response) {
        this.$message({ type: response.status, message: response.msg });
      }
      this.loadEmps();
      this.fileUploadBtnText = "导入数据";
    },
    fileUploadError(err, file, fileList) {
      this.$message({ type: "error", message: "导入失败!" });
      this.fileUploadBtnText = "导入数据";
    },
    beforeFileUpload(file) {
      this.fileUploadBtnText = "正在导入";
    },
    exportEmps() {
      //        var iframe = document.createElement("iframe");
      //        iframe.style.display = 'none';
      //        iframe.src = "/employee/basic/exportEmp";
      //        iframe.onload=function () {
      //          document.body.removeChild(iframe);
      //        }
      //        document.body.appendChild(iframe);
      window.open("/employee/basic/exportEmp", "_parent");
    },
    cancelSearch() {
      this.advanceSearchViewVisible = false;
      this.emptyEmpData();
      this.beginDateScope = "";
      this.loadEmps();
    },
    showAdvanceSearchView() {
      this.advanceSearchViewVisible = !this.advanceSearchViewVisible;
      this.keywords = "";
      if (!this.advanceSearchViewVisible) {
        this.emptyPrjData();
        this.beginDateScope = "";
        this.loadPrjs();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteManyPrjs() {
      this.$confirm(
        "此操作将删除[" + this.multipleSelection.length + "]条数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          var ids = "";
          for (var i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ",";
          }
          this.doDelete(ids);
        })
        .catch(() => {});
    },
    deletePrj(row) {
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
      this.deleteRequest("/project/basic/prj/" + ids).then(resp => {
        _this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.msg });
          _this.loadPrjs();
        }
      });
    },
    keywordsChange(val) {
      if (val == "") {
        this.loadPrjs();
      }
    },
    searchPrj() {
      this.loadPrjs();
    },
    currentChange(currentChange) {
      this.currentPage = currentChange;
      this.loadPrjs();
    },
    loadPrjs() {
      var _this = this;
      this.tableLoading = true;
      this.getRequest(
        "/project/basic/prj?page=" +
          this.currentPage +
          "&size=10&keywords=" +
          this.keywords +
          "&politicId=" +
          this.prj.politicId +
          "&nationId=" +
          this.prj.nationId +
          "&posId=" +
          this.prj.posId +
          "&jobLevelId=" +
          this.prj.jobLevelId +
          "&engageForm=" +
          this.prj.engageForm +
          "&departmentId=" +
          this.prj.departmentId +
          "&beginDateScope=" +
          this.beginDateScope
      ).then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.prjs = data.prjs;
          _this.totalCount = data.count;
          //            _this.emptyEmpData();
        }
      });
    },

    addPrj(formName) {
      var _this = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.prj.id) {
            //更新
            this.tableLoading = true;
            this.putRequest("/project/basic/prj", this.prj).then(resp => {
              _this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                _this.$message({ type: data.status, message: data.msg });
                _this.dialogVisible = false;
                _this.emptyPrjData();
                _this.loadPrjs();
              }
            });
          } else {
            //添加
            this.tableLoading = true;
            this.postRequest("/project/basic/prj", this.prj).then(resp => {
              _this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                _this.$message({ type: data.status, message: data.msg });
                _this.dialogVisible = false;
                _this.emptyPrjData();
                _this.loadPrjs();
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    cancelEidt() {
      this.dialogVisible = false;
      this.emptyEmpData();
    },
    showDepTree() {
      this.showOrHidePop = !this.showOrHidePop;
    },
    showDepTree2() {
      this.showOrHidePop2 = !this.showOrHidePop2;
    },
    handleNodeClick(data) {
      this.prj.departmentName = data.name;
      this.prj.departmentId = data.id;
      this.showOrHidePop = false;
      this.depTextColor = "#606266";
    },
    handleNodeClick2(data) {
      this.advSearchData.departmentName = data.name;
      this.advSearchData.departmentId = data.id;
      this.showOrHidePop2 = false;
      this.depTextColor = "#606266";
    },
    initData() {
      var _this = this;
      this.getRequest("/project/basic/prj").then(resp => {
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.nations = data.nations;
          _this.politics = data.politics;
          _this.prjs = data.prjs;
          _this.positions = data.positions;
          _this.joblevels = data.joblevels;
          _this.prj.workID = data.workID;
        }
      });
    },
    showEditPrjView(row) {
      console.log(row);
      this.dialogTitle = "编辑项目信息";
      this.prj = row;
      this.prj.birthday = this.formatDate(row.birthday);
      this.prj.conversionTime = this.formatDate(row.conversionTime);
      this.prj.beginContract = this.formatDate(row.beginContract);
      this.prj.endContract = this.formatDate(row.endContract);
      this.prj.beginDate = this.formatDate(row.beginDate);
      this.prj.nationId = row.nation.id;
      this.prj.politicId = row.politicsStatus.id;
      this.prj.departmentId = row.department.id;
      this.prj.departmentName = row.department.name;
      this.prj.jobLevelId = row.jobLevel.id;
      this.prj.posId = row.position.id;
      //        delete this.prj.department;
      //        delete this.prj.jobLevel;
      //        delete this.prj.position;
      //        delete this.prj.nation;
      //        delete this.prj.politicsStatus;
      delete this.prj.workAge;
      delete this.prj.notWorkDate;
      this.dialogVisible = true;
    },
    showAddPrjView() {
      this.dialogTitle = "添加新项目";
      this.emptyPrjData();

      this.dialogVisible = true;
      var _this = this;
      this.getRequest("/employee/basic/maxWorkID").then(resp => {
        if (resp && resp.status == 200) {
          _this.prj.workID = resp.data;
        }
      });
    },
    emptyPrjData() {
      this.prj = {
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: "",
        nativePlace: "",
        politicId: "",
        email: "",
        phone: "",
        address: "",
        departmentId: "",
        departmentName: "所属部门...",
        jobLevelId: "",
        posId: "",
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "",
        workID: "",
        contractTerm: "",
        conversionTime: "",
        notWorkDate: "",
        beginContract: "",
        endContract: "",

        customId: "",
        customName: "",
        prjTypeId: "",

        workAge: ""
      };
    }
  }
};
</script>
<style>
.el-dialog__body {
  padding-top: 0px;
  padding-bottom: 0px;
}

.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}
</style>
