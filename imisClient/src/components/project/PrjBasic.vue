<template>
  <div>
    <el-container>
      <el-header
        style="padding: 0px;display:flex;justify-content:space-between;align-items: center"
      >
        <div style="display: inline">
          <el-input
            placeholder="项目名称搜索，记得回车哦..."
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
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddPrjView">添加项目</el-button>
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
                  <el-col :span="10">
                    <el-form-item label="所属客户:" prop="customId">
                      <el-popover
                        v-model="showOrHideCustomPop2"
                        placement="right"
                        title="请选择所属客户"
                        trigger="manual"
                      >
                        <el-tree
                          :data="customTreeData"
                          :default-expand-all="true"
                          :props="defaultProps"
                          :expand-on-click-node="false"
                          @node-click="handleCustomTreeNodeClick2"
                        ></el-tree>
                        <div
                          slot="reference"
                          style="width: 230px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                          @click="showCustomTree2"
                          v-bind:style="{color: depTextColor}"
                        >{{advSearchData.customName}}</div>
                      </el-popover>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
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
                </el-row>
                <el-row>
                  <el-col :span="7">
                    <el-form-item label="项目类型:">
                      <el-select
                        v-model="advSearchData.prjType"
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
                    <el-form-item label="项目分类:" prop="prjClass">
                      <el-radio-group v-model="advSearchData.prjClass">
                        <el-radio label="ALL">全部</el-radio>
                        <el-radio label="LAST_PRJ">延续形</el-radio>
                        <el-radio style="margin-left: 15px" label="NEW_PRJ">新项目</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
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
                    <el-button size="mini" type="info" @click="emptySearchData">重置</el-button>
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
            <el-table-column type="selection" align="left" width="30"></el-table-column>

            <el-table-column prop="prjNo" label="项目编号" width="150"></el-table-column>

            <el-table-column prop="name" label="项目名称" fixed width="250"></el-table-column>

            <el-table-column
              prop="departmentName"
              :formatter="formatDepartmentName"
              align="left"
              width="100"
              label="所属部门"
            ></el-table-column>

            <el-table-column
              prop="customName"
              :formatter="formatCustomName"
              align="left"
              label="客户名称"
              width="200"
            ></el-table-column>

            <el-table-column prop="contractNo" width="85" align="left" label="合同编号"></el-table-column>

            <el-table-column
              prop="contractAmount"
              width="80"
              align="left"
              :formatter="formatAmount"
              label="合同金额"
            ></el-table-column>

            <el-table-column width="85" align="left" label="签订日期">
              <template slot-scope="scope">{{ scope.row.signDate | formatDate}}</template>
            </el-table-column>

            <el-table-column prop="incomeYear" width="100" label="确认收入年份"></el-table-column>

            <el-table-column width="85" align="left" label="开工日期">
              <template slot-scope="scope">{{ scope.row.beginDate | formatDate}}</template>
            </el-table-column>
            <el-table-column width="85" align="left" label="结束日期">
              <template slot-scope="scope">{{ scope.row.endDate | formatDate}}</template>
            </el-table-column>

            <el-table-column prop="totalPeopleMonth" width="80" align="left" label="人月总数"></el-table-column>

            <el-table-column prop="budgetPeopleMonth" width="80" align="left" label="预算人月总数"></el-table-column>

            <el-table-column prop="costAmount" width="80" align="left" label="成本合计"></el-table-column>

            <el-table-column prop="labourAmount" width="80" align="left" label="人工成本"></el-table-column>

            <el-table-column prop="expenseAmount" width="80" align="left" label="费用成本"></el-table-column>

            <el-table-column prop="labourBudget" width="80" align="left" label="人工预算"></el-table-column>

            <el-table-column prop="expenseBudget" width="80" align="left" label="费用预算"></el-table-column>

            <el-table-column fixed="right" label="操作" width="195">
              <template slot-scope="scope">
                <el-button
                  @click="showEditPrjView(scope.row)"
                  style="padding: 3px 4px 3px 4px;margin: 2px"
                  size="mini"
                >编辑</el-button>
                <el-button
                  @click="showEditPrjBudgetView(scope.row)"
                  style="padding: 3px 4px 3px 4px;margin: 2px"
                  type="primary"
                  size="mini"
                >项目预算</el-button>
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
            <el-button
              type="danger"
              size="mini"
              v-if="prjs.length>0"
              :disabled="multipleSelection.length==0"
              @click="deleteManyPrjs"
            >批量删除</el-button>
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
              <el-form-item label="项目名称:" prop="name">
                <el-input v-model="prj.name" size="mini" style="width: 250px" placeholder="请输入项目名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编号:" prop="prjNo">
                <el-input
                  v-model="prj.prjNo"
                  size="mini"
                  style="width: 150px"
                  placeholder="请输入项目编号"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="10">
              <el-form-item label="所属客户:" prop="customId">
                <el-popover
                  v-model="showOrHideCustomPop"
                  placement="right"
                  title="请选择所属客户"
                  trigger="manual"
                >
                  <el-tree
                    :data="customTreeData"
                    :default-expand-all="true"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    @node-click="handleCustomTreeNodeClick"
                  ></el-tree>
                  <div
                    slot="reference"
                    style="width: 230px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                    @click="showCustomTree"
                    v-bind:style="{color: depTextColor}"
                  >{{prj.customName}}</div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="所属部门:" prop="depId">
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
          <el-row style="margin-top: 10px">
            <el-col :span="7">
              <el-form-item label="项目类型:">
                <el-select
                  v-model="prj.prjType"
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
              <el-form-item label="项目分类:" prop="prjClass">
                <el-radio-group v-model="prj.prjClass">
                  <el-radio label="LAST_PRJ">延续形</el-radio>
                  <el-radio label="NEW_PRJ">新项目</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="6">
              <el-form-item label="启动日期:" prop="beginDate">
                <el-date-picker
                  v-model="prj.beginDate"
                  size="mini"
                  value-format="yyyy-MM-dd"
                  style="width: 150px"
                  type="date"
                  placeholder="启动日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="结束日期:" prop="endDate">
                <el-date-picker
                  v-model="prj.endDate"
                  size="mini"
                  value-format="yyyy-MM-dd"
                  style="width: 150px"
                  type="date"
                  placeholder="结束日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="合同金额:" prop="contractAmount">
                <el-input
                  v-model="prj.contractAmount"
                  size="mini"
                  style="width: 150px"
                  placeholder="合同金额"
                ></el-input>
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
    <!-- end prj edit/add form dialog -->
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
      totalCount: -1,
      currentPage: 1,
      page: 0,

      prjTypes: [
        { id: "TASKS", name: "派单类" },
        { id: "MAN_MONTH", name: "实施类" },
        { id: "PROJECT", name: "人月类" }
      ],

      customTreeData: [],
      deps: [],

      defaultProps: {
        label: "name",
        isLeaf: "leaf",
        children: "childs" //树型数据的子节点列表名称
      },

      budgetDialogVisible: false,

      dialogVisible: false,
      tableLoading: false,
      advanceSearchViewVisible: false,
      showOrHidePop: false,
      showOrHidePop2: false,

      showOrHideCustomPop: false,
      showOrHideCustomPop2: false,

      advSearchData: {
        name: "",
        customId: "",
        customName: "请选择客户",
        departmentId: "",
        departmentName: "请选择部门",
        prjType: "",
        prjClass: "",
        beginDateScope: ""
      },

      prj: {
        id: -1,
        prjNo: "",
        name: "",
        simpleName: "",
        contractNo: "",
        prjType: null,
        prjClass: null,
        departmentId: "",
        departmentName: "所属部门...",
        beginDate: "",
        endDate: "",

        customId: null,
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

      curPrjId: -1,

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
      },
      budgetRules: {
        prjManCostAvg: [
          { required: true, message: "不能为空", trigger: "blur" }
        ]
      }
    };
  },
  mounted: function() {
    this.loadCustomTree();
    this.loadDepTree();
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

    formatAmount(row, column, cellValue) {
      var props = column.property.split(".");

      var tmp = row;
      for (var i = 0; i < props.length; i++) {
        tmp = tmp[props[i]];
      }

      //     console.log("format amount:" + cellValue + "col prop:" + column.property);
      return this.formatMoney(tmp, 1);
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
      this.emptySearchData();
      this.emptyPrjData();
      this.beginDateScope = "";
      this.loadPrjs();
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
          _this.$message({ type: data.status, message: data.message });
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
      console.log("curPage: " + currentChange);
      this.currentPage = currentChange;
      this.page = currentChange - 1;
      this.loadPrjs();
    },

    loadCustomTree() {
      var _this = this;
      this.getRequest("/config/customTree").then(resp => {
        //          _this.treeLoading = false;
        if (resp && resp.status == 200) {
          _this.customTreeData = resp.data;
        }
      });
    },

    loadDepTree() {
      var _this = this;
      this.getRequest("/config/dep/myDepTree").then(resp => {
        //          _this.treeLoading = false;
        if (resp && resp.status == 200) {
          _this.deps = resp.data;
        }
      });
    },

    formatCustomName(row, column) {
      var customId = row.customId;
      var customName = this.findElementName(this.customTreeData, customId);
      row.customName = customName;
      return customName;
    },

    formatDepartmentName(row, column) {
      var departmentId = row.depId;
      var departmentName = this.findElementName(this.deps, departmentId);
      row.departmentName = departmentName;
      return departmentName;
    },

    loadPrjs() {
      var _this = this;
      this.tableLoading = true;

      var url =
        "/project/basic/prjs?page=" +
        this.page +
        "&size=10&keyword=" +
        this.keywords;
      url = url + "&customId=" + this.advSearchData.customId;
      url = url + "&departmentId=" + this.advSearchData.departmentId;
      url = url + "&prjType=" + this.advSearchData.prjType;
      url = url + "&prjClass=" + this.advSearchData.prjClass;
      url = url + "&beginDateScope=" + this.advSearchData.beginDateScope;

      this.getRequest(url).then(resp => {
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
                _this.$message({ type: data.status, message: data.message });
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
                _this.$message({ type: data.status, message: data.message });
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
      //     this.emptyEmpData();
    },

    showCustomTree() {
      this.showOrHideCustomPop = !this.showOrHideCustomPop;
    },

    handleCustomTreeNodeClick(data) {
      this.prj.customName = data.name;
      this.prj.customId = data.id;
      this.showOrHideCustomPop = false;
    },

    showCustomTree2() {
      this.showOrHideCustomPop2 = !this.showOrHideCustomPop2;
    },

    handleCustomTreeNodeClick2(data) {
      this.advSearchData.customName = data.name;
      this.advSearchData.customId = data.id;
      this.showOrHideCustomPop2 = false;
    },

    showDepTree() {
      this.showOrHidePop = !this.showOrHidePop;
    },
    showDepTree2() {
      this.showOrHidePop2 = !this.showOrHidePop2;
    },
    handleNodeClick(data) {
      this.prj.departmentName = data.name;
      this.prj.depId = data.id;
      this.showOrHidePop = false;
      this.depTextColor = "#606266";
    },
    handleNodeClick2(data) {
      this.advSearchData.departmentName = data.name;
      this.advSearchData.departmentId = data.id;
      this.showOrHidePop2 = false;
      this.depTextColor = "#606266";
    },

    showEditPrjBudgetView(row) {
      this.$router.push({
        name: "项目预算",
        params: {
          id: row.id,
          depId: row.depId,
          contractNo: row.contractNo,
          contractAmount: row.contractAmount,
          prjName: row.name
        }
      });
    },

    showEditPrjView(row) {
      console.log(row);
      this.dialogTitle = "编辑项目信息";
      this.prj = row;

      this.prj.endDate = this.formatDate(row.endDate);
      this.prj.beginDate = this.formatDate(row.beginDate);

      this.prj.departmentId = row.depId;
      this.dialogVisible = true;
    },

    showAddPrjView() {
      this.dialogTitle = "添加新项目";
      this.emptyPrjData();

      this.dialogVisible = true;
      var _this = this;
    },

    emptySearchData() {
      this.advSearchData = {
        name: "",
        customId: "",
        customName: "请选择客户",
        departmentId: "",
        departmentName: "请选择部门",
        prjType: -1,
        prjClass: -1,
        beginDateScope: ""
      };
    },

    emptyPrjData() {
      this.prj = {
        name: "",
        departmentId: "",
        departmentName: "所属部门...",
        beginDate: "",
        endDate: "",
        customId: null,
        customName: "",
        contractAmount: 0,
        prjType: null
      };
    }
  }
};
</script>
<style>
</style>
