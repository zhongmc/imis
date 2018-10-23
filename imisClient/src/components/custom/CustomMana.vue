<template>
  <div style="margin-top:30px;">
    <!-- div style="text-align: left;">
      <el-input
        placeholder="输入客户名称搜索客户..."
        style="width: 500px;margin: 0px;padding: 0px;"
        size="mini"
        prefix-icon="el-icon-search"
        v-model="keywords">
      </el-input>

      <div style="display: inline; margin-bottom:5px;float:right;">

          <el-button slot="reference" type="primary" size="mini" style="margin-left: 5px"
                     @click="showAddCustomView"><i
            class="fa fa-lg" v-bind:class="[addCustomViewVisible ? faangledoubleup:faangledoubledown]"
            style="margin-right: 5px"></i>新客户
          </el-button>
      </div>
      <div style="clear:all;"></div>
      <div v-show="addCustomViewVisible" 
        style="margin: 10px;border: 1px;border-radius: 5px;border-style: solid;padding:10px;box-sizing:border-box;">
        <span>新建客户：</span>
          <el-input
              placeholder="输入新客户名称..."
              style="width: 500px;margin: 0px;padding: 0px;"
              size="mini"
              v-model="customName">
            </el-input>
            <el-button  type="primary" size="mini"  @click="addNewCustom">提交</el-button>
           
      </div>

    </div -->

    <div style="text-align: left">
      <el-input
        placeholder="添加客户..."
        size="mini"
        @keyup.enter.native="addNewCustom"
        style="width: 300px;"
        prefix-icon="el-icon-plus"
        v-model="customName">
      </el-input>
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="addNewCustom">添加</el-button>
    </div>


    <div style="max-width:800px;">
      <el-tree
        :props="defaultProps"
        :data="treeData"
        ref="tree"
        :filter-node-method="filterNode"
        v-loading="treeLoading"
        style="margin-top: 10px"
        :render-content="renderContent">
      </el-tree>
      <div style="text-align: left">
        <el-dialog
          title="添加客户部门"
          :visible.sync="dialogVisible"
          width="35%">
          <div>
            <span>上级部门：</span>

                  <el-popover
                    v-model="showOrHidePop"
                    placement="right"
                    title="请选择部门"
                    trigger="manual">
                    <el-tree :data="treeData" :default-expand-all="true" 
                      :props="defaultProps" :expand-on-click-node="false"
                             @node-click="handleNodeClick">
                    </el-tree>
                    <div slot="reference"
                         style="width: 150px;height: 26px;
                         display: inline-flex;font-size:13px;border: 1px;
                         border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                         @click.left="showCustomTree" v-bind:style="{color: depTextColor}">{{pCustomName}}
                    </div>
                  </el-popover>

          </div>
          <div style="margin-top: 10px">
            <span>部门名称：</span>
            <el-input size="mini" style="width: 200px;" v-model="customName" placeholder="请输入部门名称..." @keyup.enter.native="addCustom"></el-input>
          </div>
          <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="dialogVisible = false">取消</el-button>
    <el-button size="small" type="primary" @click="addCustom">添加</el-button>
  </span>
  </el-dialog>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      keywords: "",
      customName: "",
      treeLoading: false,
      dialogVisible: false,

      faangledoubleup: "fa-angle-double-up",
      faangledoubledown: "fa-angle-double-down",

      allCustoms: [],
      pCustom: "",
      treeData: [],
      showOrHidePop: false,

      addCustomViewVisible: false,

      customName: "",

      pCustomName: "",
      pCustomId: -1,
      depTextColor: "#c0c4cc",

      defaultProps: {
        label: "name",
        isLeaf: "leaf",
        children: "childs"
      }
    };
  },

  mounted: function() {
    this.treeLoading = true;
    this.loadTreeData();
  },
  watch: {
    keywords(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    showCustomTree() {
      this.showOrHidePop = !this.showOrHidePop;
    },

    handleNodeClick(data) {
      this.pCustomName = data.name;
      this.pCustomId = data.id;
      this.showOrHidePop = false;
      this.depTextColor = "#606266";
    },

    showAddCustomView() {
      this.pCustomId = -1;
      this.customName = "";
      this.addCustomViewVisible = !this.addCustomViewVisible;
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    loadTreeData() {
      var _this = this;
      this.getRequest("/system/custom/customTree").then(resp => {
        _this.treeLoading = false;
        if (resp && resp.status == 200) {
          _this.treeData = resp.data;
        }
      });
    },
    setDataToTree(treeData, pId, respData) {
      if (pId == null || pId == -1) {
        console.log("add root element: ");
        this.treeData = treeData.concat(respData); // concat(respData);
        console.log(treeData);
        return;
      }

      for (var i = 0; i < treeData.length; i++) {
        var td = treeData[i];
        if (td.id == pId) {
          treeData[i].childs = treeData[i].childs.concat(respData);
          return;
        } else {
          this.setDataToTree(td.childs, pId, respData);
        }
      }
    },

    addNewCustom() {
      var _this = this;
      if (this.customName == "") {
        _this.$message({ message: "请输入客户名称！" });
        return;
      }

      this.treeLoading = true;
      this.postRequest("/system/custom", {
        name: this.customName
      }).then(resp => {
        _this.treeLoading = false;
        if (resp && resp.status == 200) {
          var respData = resp.data;
          _this.$message({ type: respData.status, message: respData.message });
          if (respData.status == "success") {
            console.log("add new custom: " + respData.retObject);
            _this.setDataToTree(_this.treeData, -1, respData.retObject);
          }
          _this.customName = "";
          _this.pCustomId = -1;
        }
      });
    },

    addCustom() {
      var _this = this;
      this.dialogVisible = false;
      this.treeLoading = true;
      this.postRequest("/system/custom", {
        name: this.customName,
        parentId: this.pCustomId
      }).then(resp => {
        _this.treeLoading = false;
        if (resp && resp.status == 200) {
          var respData = resp.data;
          _this.customName = "";
          _this.$message({ type: respData.status, message: respData.message });
          if (respData.status == "success")
            _this.setDataToTree(
              _this.treeData,
              _this.pCustomId,
              respData.retObject
            );
        }
      });
    },

    showAddCustomView(data, event) {
      this.dialogVisible = true;
      this.pCustomId = data.id;
      this.pCustomName = data.name;

      event.stopPropagation();
    },
    deleteCustom(data, event) {
      var _this = this;
      if (data.childs.length > 0) {
        this.$message({
          message: "该记录下尚有子记录，不能被删除!",
          type: "warning"
        });
      } else {
        this.$confirm("删除[" + data.name + "], 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            _this.treeLoading = true;
            _this.deleteRequest("/system/custom/" + data.id).then(resp => {
              _this.treeLoading = false;
              if (resp && resp.status == 200) {
                var respData = resp.data;
                _this.$message({
                  message: respData.msg,
                  type: respData.status
                });
                if (respData.status == "success")
                  _this.deleteLocalCustom(_this.treeData, data);
              }
            });
          })
          .catch(() => {
            _this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
      event.stopPropagation();
    },
    deleteLocalCustom(treeData, data) {
      for (var i = 0; i < treeData.length; i++) {
        var td = treeData[i];
        if (td.id == data.id) {
          treeData.splice(i, 1);
          return;
        } else {
          this.deleteLocalCustom(td.childs, data);
        }
      }
    },
    renderContent(h, { node, data, store }) {
      return (
        <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;">
          <span>
            <span>{node.label}</span>
          </span>

          <span>
            <span style="margin-right:20px;">
              维护部门：
              {data.departmentId}{" "}
            </span>
            <el-button
              style="font-size: 12px;"
              type="primary"
              size="mini"
              style="padding:3px"
              on-click={() => this.showAddCustomView(data, event)}
            >
              添加部门
            </el-button>
            <el-button
              style="font-size: 12px;"
              type="danger"
              size="mini"
              style="padding:3px"
              on-click={() => this.deleteCustom(data, event)}
            >
              删除部门
            </el-button>
          </span>
        </span>
      );
    }
  }
};
</script>
