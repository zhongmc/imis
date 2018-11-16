// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import store from "./store";
import { getRequest } from "./utils/api";
import { postRequest } from "./utils/api";
import { deleteRequest } from "./utils/api";

import { postJsonRequest } from "./utils/api";

import { putRequest } from "./utils/api";
import { initMenu } from "./utils/utils";

import { findElementName } from "./utils/utils";

import { isNotNullORBlank } from "./utils/utils";
import "./utils/filter_utils";

import { formatMoney } from "./utils/utils";

import "font-awesome/css/font-awesome.min.css";

Vue.config.productionTip = false;
Vue.use(ElementUI);

Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.isNotNullORBlank = isNotNullORBlank;
Vue.prototype.formatMoney = formatMoney;
Vue.prototype.findElementName = findElementName;
Vue.prototype.postJsonRequest = postJsonRequest;
Vue.prototype.initMenu = initMenu;

router.beforeEach((to, from, next) => {
  if (to.name == "login") {
    next();
    return;
  }
  if (to.name == "init") {
    next();
    return;
  }
  var name = store.state.user.username;
  console.log(
    "saved user:" +
      name +
      " to: " +
      to.name +
      ":" +
      to.path +
      " from: " +
      from.name
  );
  if (name == null || name == "未登录") {
    if (to.meta.requireAuth || to.name == null) {
      next({ path: "/", query: { redirect: to.path } });
    } else {
      next();
    }
  } else if (store.state.routes.length == 0) {
    //
    //try to rebuild user info
    var ret = rebuildUserInfo();
    console.log("rebuild user ret:" + ret);

    // if (!ret ) return;
    // console.log("rebuild user info ok!");
    next();
  } else {
    //    initMenu(router, store);
    next();
  }
});

function rebuildUserInfo() {
  var ret = false;
  console.log("rebuild user info...");
  getRequest("/config/userinfo").then(resp => {
    if (resp && resp.status == 200) {
      console.log("get user info:");
      console.log(resp);
      var data = resp.data;
      if (data.retObject.userface == null || data.retObject.userface == "")
        data.retObject.userface = "/static/imgs/userFace.jpg";
      store.commit("login", data.retObject);
      initMenu(router, store);
      // store.dispatch('connect');
      ret = true;
    } else {
      ret = false;
    }
  });
  return ret;
}

function getCookie(sName) {
  console.log("find the cookie:" + sName);

  var aCookie = document.cookie.split(";");
  for (var i = 0; i < aCookie.length; i++) {
    console.log(aCookie);

    var aCrumb = aCookie[i].split("=");
    if (sName == aCrumb[0]) return unescape(aCrumb[1]);
  }
  console.log("not found!");
  return null;
}

new Vue({
  el: "#app",
  router,
  store,
  template: "<App/>",
  components: { App }
});
