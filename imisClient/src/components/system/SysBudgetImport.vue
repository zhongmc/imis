<template>
  <div style="margin-top:30px;width:600px;">


       <el-form :rules="rules" :model="adminForm"  label-position="left" ref="adminForm">
    <el-row>
      <el-col :span="8">
    <el-form-item prop="username">
      <el-input type="text" size="small" v-model="adminForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
      </el-col>
      <el-col :span="8">
    <el-form-item prop="password">
      <el-input type="password"  size="small" v-model="adminForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
      </el-col>
      <el-col :span="6">
    <el-form-item >
      <el-button type="danger" size="small"  @click.native.prevent="submitClick"   :loading="clearBtnText=='正在清空数据'">
         <i class="fa fa-lg fa-level-up" style="margin-right: 5px"></i>{{clearBtnText}}</el-button>
    </el-form-item>
      </el-col>
    </el-row>

  </el-form>

  
      <el-row>
      <el-col :span="4" >
      <span>所属部门:</span>
      </el-col>
      <el-col :span="10">
        <el-input placeholder="请选择所属部门" size="small" width="200" v-model="departmentName" suffix-icon ="el-icon-caret-bottom" 
        readonly="readonly" @click.native="isShowSelect = !isShowSelect"> 
        </el-input> 
          <el-tree v-if="isShowSelect" empty-text="暂无数据" 
            :expand-on-click-node="false" 
            :data="deps" :props="defaultProps" 
             :default-expand-all="true"
            @node-click="handleNodeClick" class="objectTree"> 
            </el-tree> 
      </el-col>

          <!-- el-col :span="4">
          <span>{{fileName}} </span>
          </el-col -->

          <el-col :span="8">
          <el-upload
            :show-file-list="false"
            accept="application/vnd.ms-excel"
            action="/system/budget/import"
            :data="uploadParam"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError" :disabled="fileUploadBtnText=='正在导入'"
            :before-upload="beforeFileUpload" style="display: inline;margin-left:20px;">



            <el-button size="mini" type="success" :loading="fileUploadBtnText=='正在导入'">
              <i class="fa fa-lg fa-level-up" style="margin-right: 5px"></i>{{fileUploadBtnText}}
            </el-button>
          </el-upload>
          </el-col>
      </el-row>

      <el-card header="已上传数据" class="box-card">
        <div v-for="item in uploaded" v-bind:key="item.depId" style="margin-bottom:5px;" >
          {{item.depName}} -- {{item.fileName}}
        </div>
      </el-card>

        </div>

</template>

<script>
export default {
  data() {
    return {
      deps: [],
      departmentName: "",
      depId: null,
      fileName: "",
      showOrHideDepPop: false,
      isShowSelect: false,
      uploaded: [],

      uploadParam: {
        depId: null
      },
      clearBtnText: "清空数据",
      adminForm: {
        username: "",
        password: ""
      },

      rules: {
        username: [
          { required: true, message: "请输入管理员账号", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },

      fileUploadBtnText: "导入预算数据",
      depTextColor: "#c0c4cc",

      defaultProps: {
        label: "name",
        children: "childs" //树型数据的子节点列表名称
      }
    };
  },

  mounted: function() {
    this.loadDepTree();
  },

  methods: {
    submitClick: function() {
      var _this = this;

      this.$refs["adminForm"].validate(valid => {
        if (valid) {
          this.clearBtnText = "正在清空数据";
          this.postRequest("/system/budget/clear", {
            userName: this.adminForm.username,
            password: this.adminForm.password
          }).then(resp => {
            _this.clearBtnText = "清空数据";
            if (resp && resp.status == 200) {
              var data = resp.data;
              console.log(data);
              _this.$message({ type: data.status, message: data.message });
            }
          });
        }
      });
    },

    fileUploadSuccess(response, file, fileList) {
      console.log(response);
      if (response) {
        this.$message({ type: response.status, message: response.message });
        if (response.status == "success") {
          var len = this.uploaded.length;
          this.uploaded[len] = new Object();
          this.uploaded[len].depId = this.depId;
          this.uploaded[len].depName = this.departmentName;
          this.uploaded[len].fileName = this.fileName;
          this.departmentName = "";
          this.depId = null;
        }
      }
      this.fileUploadBtnText = "导入预算数据";
    },

    fileUploadError(err, file, fileList) {
      this.$message({ type: "error", message: "导入失败!" });
      this.fileUploadBtnText = "导入预算数据";
    },

    beforeFileUpload(file) {
      if (this.depId == null) {
        this.$message({ type: "error", message: "请选择所属部门！" });
        return false;
      }

      for (var i = 0; i < this.uploaded.length; i++) {
        if (this.depId == this.uploaded[i].depId) {
          this.$message({ type: "error", message: "此部门数据已上传！" });
          return false;
        }
      }

      this.uploadParam.depId = this.depId;
      this.fileName = file.name;
      this.fileUploadBtnText = "正在导入";

      // var len = this.uploaded.length;
      // this.uploaded[len] = new Object();
      // this.uploaded[len].depId = this.depId;
      // this.uploaded[len].depName = this.departmentName;
      // this.uploaded[len].fileName = this.fileName;
    },

    loadDepTree() {
      var _this = this;
      this.getRequest("/system/basic/myDepTree").then(resp => {
        //          _this.treeLoading = false;
        if (resp && resp.status == 200) {
          _this.deps = resp.data;
        }
      });
    },

    showDepTree() {
      this.showOrHideDepPop = !this.showOrHideDepPop;
    },

    handleDepNodeClick(data) {
      this.departmentName = data.name;
      this.depId = data.id;
      this.showOrHideDepPop = false;
      //      this.depTextColor = "#606266";
    },

    handleNodeClick(data) {
      for (var i = 0; i < this.uploaded.length; i++) {
        if (data.id == this.uploaded[i].depId) {
          this.$message({ type: "error", message: "此部门数据已上传！" });
          return;
        }
      }

      this.departmentName = data.name;
      this.depId = data.id;
      this.isShowSelect = false;
    }
  }
};
</script>

<style>
.objectTree {
  position: absolute;
  overflow: auto;
  z-index: 100;
  width: 200px;
  height: 300px;
}

.box-card {
  width: 480px;
  margin-top: 20px;
  font-size: 14px;
}
</style>
