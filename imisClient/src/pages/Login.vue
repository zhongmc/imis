<template>
  <el-form :rules="rules" class="login-container" :v-model="loginForm"  label-position="left"
           label-width="0px" v-loading="loading">
    <div>
    <h3 class="login_title">系统登录</h3>
    </div>
    <el-form-item prop="username">
      <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox class="login_remember" v-model="loginForm.rememberMe" label-position="left">下次自动登录</el-checkbox>
    <el-form-item style="width: 100%">
      <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">登录</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  data() {
    return {
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },
      checked: true,
      loginForm: {
        username: "admin",
        password: "123456",
        remenberMe: false
      },
      loading: false
    };
  },
  methods: {
    submitClick: function() {
      var _this = this;
      this.loading = true;
      this.postRequest("/login", {
        username: this.loginForm.username,
        password: this.loginForm.password,
        rememberme: this.loginForm.rememberMe
      }).then(resp => {
        _this.loading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          if (data.msg.userface == null || data.msg.userface == "")
            data.msg.userface = "/static/imgs/userFace.jpg";

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
