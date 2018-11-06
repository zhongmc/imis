<template>
  <el-form :rules="rules" :model="initParam" class="login-container" ref="userForm" label-position="left"
           label-width="0px" v-loading="loading">
    <div>
    <h3 class="login_title">系统初始化</h3>
    </div>
    <el-form-item prop="userName">
      <el-input type="text" v-model="initParam.userName" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="initParam.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>

         <el-upload
            :show-file-list="false"
            accept=".json"
            action="/config/initSystem"
            :data="initParam"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError" :disabled="fileUploadBtnText=='正在导入'"
            :before-upload="beforeFileUpload" style="display: inline;margin-left:20px;">

            <div slot="tip" class="el-upload__tip">请选择json格式的初始化文件上传！</div>


            <el-button size="mini" type="success" :loading="fileUploadBtnText=='正在导入'"><i class="fa fa-lg fa-level-up"
                                                                                          style="margin-right: 5px"></i>{{fileUploadBtnText}}
            </el-button>
          </el-upload>


  </el-form>
</template>
<script>
export default {
  data() {
    return {
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },

      initParam: {
        userName: "",
        password: ""
      },

      fileUploadBtnText: "导入初始化文件",
      loading: false
    };
  },
  methods: {
    fileUploadSuccess(response, file, fileList) {
      console.log(response);
      if (response) {
        this.$message({ type: response.status, message: response.message });
        if (response.status == "success") {
          this.$router.replace({ path: "/login" });
        } else this.fileUploadBtnText = "导入初始化文件";
      }
    },

    fileUploadError(err, file, fileList) {
      this.$message({ type: "error", message: "导入失败!" });
      this.fileUploadBtnText = "导入初始化文件";
    },

    beforeFileUpload(file) {
      var _this = this;
      var valRet = false;
      this.$refs["userForm"].validate(valid => {
        if (valid) {
          _this.fileUploadBtnText = "正在导入";
          valRet = valid;
        }
      });

      return valRet;
    },

    submitClick: function() {
      var _this = this;
      this.loading = true;
      this.postRequest("/login", {
        username: this.loginForm.username,
        password: this.loginForm.password
      }).then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$store.commit("login", data.msg);
          var path = _this.$route.query.redirect;
          _this.$router.replace({
            path: path == "/" || path == undefined ? "/home" : path
          });
        }
      });
    }
  }
};
</script>
<style>
.login-container {
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 40px auto;
  color: #505458;
}

.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
