<template>
  <div style="margin-top: 10px" v-loading="fullloading">
    <div style="margin-bottom: 10px;display: flex;justify-content: center;align-items: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索更多用户..."
        prefix-icon="el-icon-search"
        size="small"
        style="width: 400px;margin-right: 10px"
        v-model="keywords">
      </el-input>
      <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
    </div>
    <div style="display: flex;flex-wrap: wrap;text-align: left;">
      <el-card style="width: 300px;margin-bottom: 20px;margin-right:20px;" v-for="(item,index) in users" :key="item.id"
               v-loading="cardLoading[index]">
        <div slot="header" class="clearfix">
          <span>{{item.name}}</span>
          <el-button type="text"
                     style="color: #f6061b;margin: 0px;float: right; padding: 3px 0;width: 15px;height:15px"
                     icon="el-icon-delete" @click="deleteUser(item.id)"></el-button>
        </div>
        <div>
          <div style="width: 100%;display: flex;justify-content: center">
            <img :src="item.avadaImage" :alt="item.nickName" style="width: 100px;height: 70px;border-radius: 70px">
          </div>
          <div style="margin-top: 20px">
            <div  class="user-info"><span>用户ID: {{item.userName}}</span></div>
            <div  class="user-info"><span>用户名: {{item.nickName}}</span></div>
            <div class="user-info"><span>所属部门: {{formatDepartmentName(item.depId)}}</span></div>
            <div class="user-info" style="display: flex;align-items: center;">
              用户状态:
              <el-switch
                style="display: inline;margin-left: 15px;font-size:10px;"
                v-model="item.status"
                active-color="#13ce66"
                inactive-color="#aaaaaa"
                active-text="启用"
                active-value="ACTIVE"
                inactive-value="FREEZE"
                
                @change="switchChange(item.status,item.id,index)"
                inactive-text="禁用">
              </el-switch>
            </div>
            <div class="user-info">
              用户角色:
              <el-tag
                v-for="role in item.roles"
                :key="role.id"
                type="success"
                size="mini"
                style="margin-right: 15px"
                :disable-transitions="false">{{role.nameZh}}
              </el-tag>
              <el-popover
                v-loading="eploading[index]"
                placement="right"
                title="角色列表"
                width="200"
                @hide="updateUserRoles(item.id,index)"
               
                trigger="click">
                <el-select v-model="selRoles" multiple placeholder="请选择角色">
                  <el-option
                    v-for="ar in allRoles"
                    :key="ar.id"
                    :label="ar.nameZh"
                    :value="ar.id">
                  </el-option>
                </el-select>
                <el-button type="text" icon="el-icon-more" style="color: #09c0f6;padding-top: 0px" slot="reference"
                           @click="loadSelRoles(item.roles,index)" :disabled="moreBtnState"></el-button>
<!--                <i class="el-icon-more" style="color: #09c0f6;cursor: pointer" slot="reference"
                   @click="loadSelRoles(item.roles,index)" disabled="true"></i>-->
              </el-popover>
            </div>
            <div><span class="user-info">备注:{{item.remark}}</span></div>
          </div>
        </div>
      </el-card>

      <div style="display:flex;align-items:center;">           
        <el-button type="primary" circle icon="el-icon-plus" @click="showAddUserView">
           
          </el-button>
       </div>
    </div>

    <el-form :model="user" :rules="rules" ref="addUserForm" style="margin: 0px;padding: 0px;">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="50%">
          <el-row>
            <el-col :span="12">
                <el-form-item label="用户ID:" prop="userName">
                  <el-input  v-model="user.userName" size="mini" style="width: 250px"
                            placeholder="请输入用户ID"></el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="用户名称:" prop="nickName">
                  <el-input  v-model="user.nickName" size="mini" style="width: 150px"
                            placeholder="请输入用户名称"></el-input>
                </el-form-item>
            </el-col>
          </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="所属部门:" prop = "depId">                  
                  <el-popover
                    v-model="showOrHidePop"
                    placement="right"
                    title="请选择部门"
                    trigger="manual">
                    <el-tree :data="deps" :default-expand-all="true" :props="defaultProps" :expand-on-click-node="false"
                             @node-click="handleNodeClick"></el-tree>
                    <div slot="reference"
                         style="width: 230px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                         @click="showDepTree" v-bind:style="{color: depTextColor}">{{departmentName}}
                    </div>
                  </el-popover>
                  </el-form-item>
                </el-col>
              </el-row>
           <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="cancelEdit">取 消</el-button>
    <el-button size="mini" type="primary" @click="addUser('addUserForm')">确 定</el-button>
  </span>
        </el-dialog>
      </div>
    </el-form> <!-- end user edit/add form dialog -->


  </div>
</template>
<script>
export default {
  data() {
    return {
      keywords: "",
      fullloading: false,
      users: [],
      deps: [],
      cardLoading: [],
      eploading: [],
      allRoles: [],
      moreBtnState: false,
      selRoles: [],
      selRolesBak: [],
      dialogTitle: "",
      dialogVisible: false,
      showOrHidePop: false,
      depTextColor: "#c0c4cc",
      user: {
        id: -1,
        userName: "",
        depId: null,
        nickName: "",
        password: "123",
        status: "ACTIVE"
      },
      departmentName: "",

      rules: {
        userName: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        nickName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" }
        ],
        depId: [{ required: true, message: "必填:所属部门", trigger: "change" }]
      },

      defaultProps: {
        label: "name",
        isLeaf: "leaf",
        children: "childs" //树型数据的子节点列表名称
      }
    };
  },
  mounted: function() {
    this.loadDepTree();
    this.loadAllRoles();
    this.initCards();
  },
  methods: {
    emptyUserData() {
      this.user = {
        id: -1,
        userName: "",
        nickName: "",
        depId: null,
        password: "123",
        status: "ACTIVE"
      };
      this.departmentName = "";
    },

    showAddUserView() {
      this.dialogTitle = "添加新用户";
      this.emptyUserData();
      this.dialogVisible = true;
    },

    cancelEdit() {
      this.dialogVisible = false;
      //     this.emptyEmpData();
    },

    addUser(formName) {
      var _this = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          //添加
          this.tableLoading = true;
          this.postRequest("/system/user/reg", this.user).then(resp => {
            _this.tableLoading = false;
            if (resp && resp.status == 200) {
              var data = resp.data;
              _this.$message({ type: data.status, message: data.message });
              _this.dialogVisible = false;
              _this.emptyUserData();
              _this.initCards();
            }
          });
        } else {
          return false;
        }
      });
    },

    handleNodeClick(data) {
      this.departmentName = data.name;
      this.user.depId = data.id;
      this.showOrHidePop = false;
      this.depTextColor = "#606266";
    },

    showDepTree() {
      this.showOrHidePop = !this.showOrHidePop;
    },

    searchClick() {
      this.initCards();
      this.loadAllRoles();
    },

    updateUserRoles(userId, index) {
      this.moreBtnState = false;
      var _this = this;
      if (this.selRolesBak.length == this.selRoles.length) {
        for (var i = 0; i < this.selRoles.length; i++) {
          for (var j = 0; j < this.selRolesBak.length; j++) {
            if (this.selRoles[i] == this.selRolesBak[j]) {
              this.selRolesBak.splice(j, 1);
              break;
            }
          }
        }
        if (this.selRolesBak.length == 0) {
          return;
        }
      }
      this.eploading.splice(index, 1, true);
      this.putRequest("/system/user/roles", {
        userId: userId,
        rids: this.selRoles
      }).then(resp => {
        _this.eploading.splice(index, 1, false);
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
          if (data.status == "success") {
            _this.refreshUser(userId, index);
          }
        }
      });
    },

    refreshUser(userId, index) {
      var _this = this;
      console.log("Refresh user: " + userId + " " + index);
      _this.cardLoading.splice(index, 1, true);
      this.getRequest("/system/user/id/" + userId).then(resp => {
        _this.cardLoading.splice(index, 1, false);
        _this.users.splice(index, 1, resp.data);
      });
    },

    loadSelRoles(hrRoles, index) {
      this.moreBtnState = true;
      this.selRoles = [];
      this.selRolesBak = [];
      hrRoles.forEach(role => {
        this.selRoles.push(role.id);
        this.selRolesBak.push(role.id);
      });
    },
    loadAllRoles() {
      var _this = this;
      this.getRequest("/system/basic/roles").then(resp => {
        _this.fullloading = false;
        if (resp && resp.status == 200) {
          _this.allRoles = resp.data;
        }
      });
    },
    switchChange(newValue, userId, index) {
      var _this = this;
      _this.cardLoading.splice(index, 1, true);
      this.putRequest("/system/user/active", {
        status: newValue,
        id: userId
      }).then(resp => {
        _this.cardLoading.splice(index, 1, false);
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
          if (data.status == "error") {
            _this.refreshUser(userId, index);
          }
        } else {
          _this.refreshUser(userId, index);
        }
      });
    },

    loadDepTree() {
      var _this = this;
      this.getRequest("/system/basic/depTree").then(resp => {
        //          _this.treeLoading = false;
        if (resp && resp.status == 200) {
          _this.deps = resp.data;
        }
      });
    },

    initCards() {
      this.fullloading = true;
      var _this = this;
      var searchWords;
      if (this.keywords === "") {
        searchWords = "all";
      } else {
        searchWords = this.keywords;
      }
      this.getRequest("/system/user/" + searchWords).then(resp => {
        if (resp && resp.status == 200) {
          _this.users = resp.data;
          var length = resp.data.length;

          _this.cardLoading = Array.apply(null, Array(length)).map(function(
            item,
            i
          ) {
            return false;
          });

          _this.eploading = Array.apply(null, Array(length)).map(function(
            item,
            i
          ) {
            return false;
          });
        }
        _this.fullloading = false;
      });
    },

    deleteUser(userId) {
      var _this = this;
      this.fullloading = true;
      this.deleteRequest("/system/user/id/" + userId).then(resp => {
        _this.fullloading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$message({ type: data.status, message: data.message });
          if (data.status == "success") {
            _this.initCards();
            _this.loadAllRoles();
          }
        }
      });
    },

    formatDepartmentName(depId) {
      var departmentName = this.findElementName(this.deps, depId);
      return departmentName;
    }
  }
};
</script>
<style>
.user-info {
  font-size: 14px;
  color: #09c0f6;
  margin-bottom: 5px;
}
</style>
