import { getRequest } from "./api";
import { Message } from "element-ui";

export const isNotNullORBlank = (...args) => {
  for (var i = 0; i < args.length; i++) {
    var argument = args[i];
    if (argument == null || argument == "" || argument == undefined) {
      Message.warning({ message: "数据不能为空!" });
      return false;
    }
  }
  return true;
};
export const initMenu = (router, store) => {
  if (store.state.routes.length > 0) {
    return;
  }
  getRequest("/config/sysmenu").then(resp => {
    if (resp && resp.status == 200) {
      var fmtRoutes = formatRoutes(resp.data);
      router.addRoutes(fmtRoutes);
      store.commit("initMenu", fmtRoutes);
      console.log(fmtRoutes);
      // store.dispatch('connect');
    }
  });
};

export const formatRoutes = routes => {
  let fmRoutes = [];
  routes.forEach(router => {
    let {
      path,
      component,
      name,
      label,
      meta,
      iconCls,
      hidden,
      childs
    } = router;
    if (childs && childs instanceof Array) {
      childs = formatRoutes(childs);
    }
    let fmRouter = {
      path: path,
      component(resolve) {
        require(["../" + component], resolve);
      },

      name: name,
      label: label,
      iconCls: iconCls,
      meta: meta,
      hidden: hidden,
      children: childs
    };
    fmRoutes.push(fmRouter);
  });
  return fmRoutes;
};

export const findElementName = (nodes, fid) => {
  for (var i = 0; i < nodes.length; i++) {
    let { id, name, childs } = nodes[i];

    //   console.log(id + ":" + name);
    if (id === fid) {
      var fname = name;
      //   console.log("find it: " + fname);
      return fname;
    }
    if (childs && childs instanceof Array && childs.length > 0) {
      var fname = findElementName(childs, fid);
      // console.log("c ret:" + fname);
      if (fname && fname != "") {
        //       console.log("Ret it:" + fname);
        return fname;
      }
    }
  }
  return "";
};

// 返回数字
export const removeFormatMoney = s => {
  return parseFloat(s.replace(/[^\d\.-]/g, ""));
};

/* 
* formatMoney(s,type) 
* 功能：金额按千位逗号分隔
* 参数：s，需要格式化的金额数值. 
* 参数：type,判断格式化后的金额是否需要小数位. 
* 返回：返回格式化后的数值字符串. 
*/

export const formatMoney1 = (s, type) => {
  if (/[^0-9\.]/.test(s)) return "0.00";
  if (s == null || s == "null" || s == "") return "0.00";
  s = s.toString().replace(/^(\d*)$/, "$1.");
  s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
  s = s.replace(".", ",");
  var re = /(\d)(\d{3},)/;
  while (re.test(s)) s = s.replace(re, "$1,$2");
  s = s.replace(/,(\d\d)$/, ".$1");
  if (type == 0) {
    var a = s.split(".");
    if (a[1] == "00") {
      s = a[0];
    }
  }
  return s;
};

//格式化金额的小数点

export const formatMoney = (value, type) => {
  var b = false;

  if (value == null || value == "") return "0";

  value = value.toString();

  if (value.indexOf("-") != -1) {
    b = true;

    value = value.substring(1, value.length);
  }

  if (/^\-?[0-9]+(.[0-9]+)?$/.test(value)) {
    value = value.toString().replace(/^(\d*)$/, "$1.");

    value = (value + "00").replace(/(\d*\.\d\d)\d*/, "$1");

    value = value.replace(".", ",");

    var re = /(\d)(\d{3},)/;

    while (re.test(value)) value = value.replace(re, "$1,$2");

    value = value.replace(/,(\d\d)$/, ".$1");
  }

  if (b) {
    value = "-" + value;
  }

  return value;
};
