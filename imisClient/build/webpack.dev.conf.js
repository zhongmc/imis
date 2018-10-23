"use strict";
const utils = require("./utils");
const webpack = require("webpack");
const config = require("../config");
const merge = require("webpack-merge");
const path = require("path");
const baseWebpackConfig = require("./webpack.base.conf");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const FriendlyErrorsPlugin = require("friendly-errors-webpack-plugin");
const portfinder = require("portfinder");

const express = require("express");
const app = express(); //请求server
var appData = require("../apidata.json"); //加载本地数据文件
var apiRoutes = express.Router();

const HOST = process.env.HOST;
const PORT = process.env.PORT && Number(process.env.PORT);

const devWebpackConfig = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({
      sourceMap: config.dev.cssSourceMap,
      usePostCSS: true
    })
  },
  // cheap-module-eval-source-map is faster for development
  devtool: config.dev.devtool,

  // these devServer options should be customized in /config/index.js
  devServer: {
    clientLogLevel: "warning",
    historyApiFallback: {
      rewrites: [
        {
          from: /.*/,
          to: path.posix.join(config.dev.assetsPublicPath, "index.html")
        }
      ]
    },
    hot: true,
    contentBase: false, // since we use CopyWebpackPlugin.
    compress: true,
    host: HOST || config.dev.host,
    port: PORT || config.dev.port,
    open: config.dev.autoOpenBrowser,
    overlay: config.dev.errorOverlay
      ? { warnings: false, errors: true }
      : false,
    publicPath: config.dev.assetsPublicPath,
    proxy: config.dev.proxyTable,
    quiet: true, // necessary for FriendlyErrorsPlugin
    watchOptions: {
      poll: config.dev.poll
    }

    // before(app) {
    //   app.post("/login", (req, res) => {
    //     res.json(appData.login); //接口返回json数据，上面配置的数据seller就赋值给data请求后调用
    //   }),
    //     //  sysmenu:/config GET
    //     app.get("/config/sysmenu", (req, res) => {
    //       res.json(appData.sysmenu);
    //     }),
    //     //  sysmsg:/chat    GET
    //     app.get("/chat/sysmsg", (req, res) => {
    //       res.json(appData.sysmsg);
    //     }),
    //     //info:/ws/endpointChat GET
    //     app.get("/ws/endpointChat/info", (req, res) => {
    //       res.json(appData.info);
    //     }),
    //     //basicdata:/employee/basic GET
    //     app.get("/employee/basic/basicdata", (req, res) => {
    //       res.json(appData.basicdata);
    //     }),
    //     //emp: /employee/basic GET
    //     app.get("/employee/basic/emp", (req, res) => {
    //       res.json(appData.emp);
    //     }),
    //     app.get("/project/basic/prj", (req, res) => {
    //       res.json(appData.prj);
    //     }),
    //     app.get("/budget/project/cost/*", (req, res) => {
    //       console.log("required for:" + req.url);
    //       res.json(appData.prjCost);
    //     }),
    //     app.get("/budget/project/income/*", (req, res) => {
    //       console.log("required for:" + req.url);
    //       res.json(appData.prjIncome);
    //     }),
    //     app.get("/budget/project/confirm/*", (req, res) => {
    //       console.log("required for:" + req.url);
    //       res.json(appData.prjConfirm);
    //     }),
    //     app.get("/budget/project/*", (req, res) => {
    //       console.log("required for:" + req.url);
    //       res.json(appData.prjBudget);
    //     }),
    //     app.get("/system/budget/settings", (req, res) => {
    //       res.json(appData.budgetSettings);
    //     }),
    //     app.get("/budget/costBudgets", (req, res) => {
    //       res.json(appData.costBudgets);
    //     }),
    //     //positions: /system/basic
    //     app.get("/system/basic/positions", (req, res) => {
    //       res.json(appData.positions);
    //     }),
    //     //joblevels: /system/basic
    //     app.get("/system/basic/joblevels", (req, res) => {
    //       res.json(appData.joblevels);
    //     }),
    //     //roles: /system/basic
    //     app.get("/system/basic/roles", (req, res) => {
    //       res.json(appData.roles);
    //     }),
    //     // all: /system/hr
    //     app.get("/system/hr/all", (req, res) => {
    //       res.json(appData.all);
    //     }),
    //     //dep: /system/basic/dep/-1
    //     app.get("/system/basic/dep/-1", (req, res) => {
    //       res.json(appData.dep);
    //     }),
    //     app.get("/system/basic/deps", (req, res) => {
    //       res.json(appData.deps);
    //     }),
    //     app.get("/system/basic/menuTree/*", (req, res) => {
    //       res.json(appData.menuTree);
    //     }),
    //     app.get("/salary/sob/salary", (req, res) => {
    //       res.json(appData.sob);
    //     }),
    //     app.get("/salary/sobcfg/emp/*", (req, res) => {
    //       res.json(appData.empSalary);
    //     }),
    //     app.get("/custom/customs/*", (req, res) => {
    //       res.json(appData.customs);
    //     }),
    //     //* depId 部门公共预算
    //     app.get("/budget/commBudgets/*", (req, res) => {
    //       res.json(appData.commBudgets);
    //     }),
    //     //* depId 部门项目预算表
    //     app.get("/budget/prjBudgets/*", (req, res) => {
    //       res.json(appData.prjBudgets);
    //     }),
    //     //我的部门列表
    //     app.get("/system/basic/mydeps", (req, res) => {
    //       res.json(appData.myDeps);
    //     }),
    //     //部门预算表 * depId
    //     app.get("/budget/budgetTable/*", (req, res) => {
    //       res.json(appData.depBudgetTable);
    //     }),
    //     // * depId 部门费用汇总
    //     app.get("/budget/costCollect/*", (req, res) => {
    //       res.json(appData.depCostCollect);
    //     }),
    //     // 部门单项费用表 budget/cost/depId/costId
    //     app.get("/budget/cost/*", (req, res) => {
    //       res.json(appData.costBudgets11);
    //     });
    // }
  },

  plugins: [
    new webpack.DefinePlugin({
      "process.env": require("../config/dev.env")
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NamedModulesPlugin(), // HMR shows correct file names in console on update.
    new webpack.NoEmitOnErrorsPlugin(),
    // https://github.com/ampedandwired/html-webpack-plugin
    new HtmlWebpackPlugin({
      filename: "index.html",
      template: "index.html",
      inject: true
    }),
    // copy custom static assets
    new CopyWebpackPlugin([
      {
        from: path.resolve(__dirname, "../static"),
        to: config.dev.assetsSubDirectory,
        ignore: [".*"]
      }
    ])
  ]
});

module.exports = new Promise((resolve, reject) => {
  portfinder.basePort = process.env.PORT || config.dev.port;
  portfinder.getPort((err, port) => {
    if (err) {
      reject(err);
    } else {
      // publish the new Port, necessary for e2e tests
      process.env.PORT = port;
      // add port to devServer config
      devWebpackConfig.devServer.port = port;

      // Add FriendlyErrorsPlugin
      devWebpackConfig.plugins.push(
        new FriendlyErrorsPlugin({
          compilationSuccessInfo: {
            messages: [
              `Your application is running here: http://${
                devWebpackConfig.devServer.host
              }:${port}`
            ]
          },
          onErrors: config.dev.notifyOnErrors
            ? utils.createNotifierCallback()
            : undefined
        })
      );

      resolve(devWebpackConfig);
    }
  });
});
