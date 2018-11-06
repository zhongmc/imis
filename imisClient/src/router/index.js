import Vue from "vue";
import Router from "vue-router";

import Login from "@/pages/Login";
import Home from "@/pages/Home";
import Chat from "@/components/chat/Chat";
import Init from "@/pages/Initialize.vue";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      name: "login",
      component: Login,
      hidden: true
    },
    {
      path: "/home",
      name: "主页",
      component: Home,
      hidden: true,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: "/chat",
          name: "消息",
          component: Chat,
          hidden: true,
          meta: {
            keepAlive: false,
            requireAuth: true
          }
        }
      ]
    },
    {
      path: "/init",
      name: "init",
      component: Init,
      hidden: true
    }
  ]
});
