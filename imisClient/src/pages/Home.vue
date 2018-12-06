<template>
  <div>
    <el-container class="home-container">
      <el-header>
        <div class="home-header">
          <span hidefocus="true" class="menuButton" style="float:left;">
            <a class="menuList" v-on:click="toggleMenu"></a>
          </span>
          
          <span class="home_title" style="float:left;width:200px;">IMIS</span>
          <div style="display: flex;align-items: center;margin-right: 7px">
            <el-badge style="margin-right: 30px" :is-dot="this.$store.state.nfDot">
              <i class="fa fa-bell-o" @click="goChat" style="cursor: pointer"></i>
            </el-badge>
            <el-dropdown @command="handleCommand">
              <span
                class="el-dropdown-link home_userinfo"
                style="display: flex;align-items: center"
              >
                <i>
                  <img
                    v-if="user.userface!=''"
                    :src="user.userface"
                    style="width: 40px;height: 40px;margin-right: 5px;margin-left: 5px;border-radius: 40px"
                  >
                </i>
                {{user.nickName}}
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>设置</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout" divided>签退</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>

        <div
          v-bind:class="[this.showMenuClass]"
          v-show="this.isMenuShow"
          v-on:mouseleave="toggleMenu"
        >
          <el-menu unique-opened router>
            <template v-for="(item,index) in routes" v-if="!item.hidden">
              <el-submenu :key="index" :index="index+''">
                <template slot="title">
                  <i :class="item.iconCls" style="color: #20a0ff;width: 14px;"></i>
                  <span slot="title">{{item.name}}</span>
                </template>
                <el-menu-item
                  width="180px"
                  style="padding-left: 30px;padding-right:0px;margin-left: 0px;width: 170px;text-align: left"
                  v-for="child in item.children"
                  v-if="!child.hidden"
                  :index="child.path"
                  :key="child.path"
                >{{child.name}}</el-menu-item>
              </el-submenu>
            </template>
          </el-menu>
        </div>
      </el-header>

      <el-container>
        <el-main>
          <el-breadcrumb separator="/" class="breadcrumb-inner">
            <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">{{ item.name }}</el-breadcrumb-item>
          </el-breadcrumb>

          <div style="clear:both"></div>
          <div>
            <router-view></router-view>

            <el-form
              :model="passwordForm"
              :rules="rules"
              ref="passwordForm"
              style="margin: 0px;padding: 0px;"
            >
              <div style="text-align: left">
                <el-dialog
                  :title="dialogTitle"
                  style="padding: 0px;"
                  :close-on-click-modal="false"
                  :visible.sync="dialogVisible"
                  width="40%"
                >
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="密码:" prop="password">
                        <el-input
                          v-model="passwordForm.password"
                          size="mini"
                          style="width: 150px"
                          :type="passwordType"
                          placeholder="请输入初始密码"
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="新密码:" prop="newPassword">
                        <el-input
                          v-model="passwordForm.newPassword"
                          size="mini"
                          style="width: 150px"
                          :type="passwordType"
                          placeholder="请输入新密码"
                        ></el-input>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="重试密码:" prop="rePassword">
                        <el-input
                          v-model="passwordForm.rePassword"
                          size="mini"
                          style="width: 150px"
                          :type="passwordType"
                          placeholder="请重试密码"
                        >
                          <i
                            class="el-icon-view el-input__icon"
                            :style="fontstyle"
                            slot="suffix"
                            @click="showPassword"
                          ></i>
                          <i slot="prefix" class="icon-mima"></i>
                        </el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <span slot="footer" class="dialog-footer">
                    <el-button size="mini" @click="cancelEdit">取 消</el-button>
                    <el-button
                      size="mini"
                      type="primary"
                      @click="changePassword('passwordForm')"
                    >确 定</el-button>
                  </span>
                </el-dialog>
              </div>
            </el-form>
            <!-- end user edit/add form dialog -->
          </div>
        </el-main>
      </el-container>
      <!-- el-footer>
    <home-foot> </home-foot>
      </el-footer-->
    </el-container>
  </div>
</template>

<script>
// 导入要用到的子组件
import HomeFoot from "../components/HomeFoot";

export default {
  mounted: function() {
    console.log(this.$route.matched);

    //   this.$router.push({path:"/prj/basic"});
    //      this.devMsg();
    //      this.loadNF();
  },

  // 在components字段中，包含导入的子组件
  components: {
    HomeFoot
  },

  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入重试密码"));
      } else if (value != this.passwordForm.newPassword) {
        callback(new Error("密码不一致！"));
      } else {
        callback();
      }
    };

    return {
      isDot: false,
      isMenuShow: false,
      showMenuClass: "nav-content hideNav",

      dialogTitle: "修改密码",
      dialogVisible: false,
      passwordType: "password",
      fontstyle: {},

      passwordForm: {
        password: "",
        newPassword: "",
        rePassword: ""
      },

      rules: {
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" }
        ],
        rePassword: [
          { required: true, trigger: "blur", validator: validatePass }
        ]
      }
    };
  },

  methods: {
    toggleMenu() {
      // isMenuShow = this.$store.state.isMenuShow;
      var menuShow = this.isMenuShow;
      menuShow = !menuShow;
      if (menuShow) this.showMenuClass = "nav-content showNav";
      else this.showMenuClass = "nav-content hideNav";

      this.isMenuShow = menuShow;

      // this.$store.commit("toggleMenuShow", isMenuShow)
      //  alert(this.isMenuShow);
    },

    loadNF() {
      var _this = this;
      this.getRequest("/chat/sysmsgs").then(resp => {
        var isDot = false;
        resp.data.forEach(msg => {
          if (msg.state == 0) {
            isDot = true;
          }
        });
        _this.$store.commit("toggleNFDot", isDot);
      });
    },
    goChat() {
      this.$router.push({ path: "/chat" });
    },

    handleCommand(cmd) {
      if (cmd == "logout") {
        this.doLogout();
      } else if (cmd == "password") {
        this.showChangePasswordDialog();
      }
    },

    showPassword() {
      this.fontstyle === ""
        ? (this.fontstyle = "color: red")
        : (this.fontstyle = ""); // 改变密码可见按钮颜色
      this.passwordType === ""
        ? (this.passwordType = "password")
        : (this.passwordType = "");
    },

    showChangePasswordDialog() {
      this.dialogVisible = true;
    },

    cancelEdit() {
      this.dialogVisible = false;
      //     this.emptyEmpData();
    },

    changePassword(formName) {
      var _this = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          //添加
          this.tableLoading = true;
          this.postRequest(
            "/config/user/changepassword",
            this.passwordForm
          ).then(resp => {
            _this.tableLoading = false;
            if (resp && resp.status == 200) {
              var data = resp.data;
              _this.$message({ type: data.status, message: data.message });
              _this.dialogVisible = false;
            }
          });
        } else {
          return false;
        }
      });
    },

    doLogout() {
      var _this = this;
      this.$confirm("注销登录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          _this.getRequest("/logout"); //logout
          _this.$store.commit("logout");
          _this.$router.replace({ path: "/" });
        })
        .catch(() => {
          _this.$message({
            type: "info",
            message: "取消"
          });
        });
    }
  },

  computed: {
    user() {
      return this.$store.state.user;
    },
    routes() {
      return this.$store.state.routes;
    }
  }
};
</script>
<style>
.home-container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}

.home-header {
  background-color: #20a0ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: content-box;
  padding: 0px;
  height: 55px;
}

.home-aside {
  background-color: #ececec;
}

.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
  margin-left: 8px;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}

.el-submenu .el-menu-item {
  width: 180px;
  min-width: 175px;
}

.menuButton {
  background: transparent;
  cursor: pointer;
  display: inline;
}

.menuButton:hover {
  text-decoration: none;
}

.menuButton a {
  display: block;
  width: 40px;
  height: 40px;
  padding: 0;
  margin-top: 1px;
  line-height: 12px;
  text-align: center;
  color: #787878;
  text-decoration: none;
  background: url(../assets/scrolltop.png) no-repeat 0 3px;
}

.menuButton a:hover {
  background: #759fd9 url(../assets/scrolltop.png) no-repeat 0 3px;
  background-position: -40px 3px !important;
}

.menuButton a.menuList {
  background-position: 0 -72px !important;
}
.menuButton a.menuList:hover {
  background-position: -40px -72px !important;
}

.nav-content {
  position: fixed;
  background: #ececec;
  top: 50px;
  left: -100%;
  width: 200px;
  padding: 5px, 20px;
  display: block;
  height: 100%;
  overflow-y: auto;
  z-index: 9;
}

@keyframes showNav {
  from {
    left: -100%;
  }
  to {
    left: 20px;
  }
}
@-webkit-keyframes showNav {
  from {
    left: -100%;
  }
  to {
    left: 20px;
  }
}
@-moz-keyframes showNav {
  from {
    left: -100%;
  }
  to {
    left: 20px;
  }
}
@-o-keyframes showNav {
  from {
    left: -100%;
  }
  to {
    left: 20px;
  }
}

.showNav {
  -webkit-animation: showNav 0.5s ease forwards;
  -moz-animation: showNav 0.5s ease forwards;
  -o-animation: showNav 0.5s ease forwards;
  animation: showNav 0.5s ease forwards;
}

@keyframes hideNav {
  from {
    left: 20px;
  }
  to {
    left: -100%;
  }
}
@-webkit-keyframes hideNav {
  from {
    left: 20px;
  }
  to {
    left: -100%;
  }
}
@-moz-keyframes hideNav {
  from {
    left: 20px;
  }
  to {
    left: -100%;
  }
}
@-o-keyframes hideNav {
  from {
    left: 20px;
  }
  to {
    left: -100%;
  }
}
.hideNav {
  -webkit-animation: hideNav 0.5s ease forwards;
  -moz-animation: hideNav 0.5s ease forwards;
  -o-animation: hideNav 0.5s ease forwards;
  animation: hideNav 0.5s ease forwards;
}
.hidden {
  display: none;
}
</style>
