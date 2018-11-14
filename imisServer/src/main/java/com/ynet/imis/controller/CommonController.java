/**
* CommonController.java
* @author ZHONGMC
* @description 
* @created Wed Sep 26 2018 16:53:24 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Nov 14 2018 17:22:46 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.service.menu.MenuService;
import com.ynet.imis.service.utils.InitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MenuService menuService;

    @Autowired
    InitService initService;

    /**
     * 请求缓存，保存跳转到当前服务前的请求的url
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    @RequestMapping("/")
    public void defaultHomePage(HttpServletResponse response) throws IOException {
        System.out.println("Request for root!");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null)
            response.sendRedirect("/index.html");
        else
            response.sendRedirect("/home");
        // return "forward:/index.html";

        // response.setStatus(HttpServletResponse.SC_OK);
        // response.setContentType("application/json:charset=UTF-8");

        // PrintWriter out = response.getWriter();
        // out.write("forward:/index.html");
        // out.flush();
        // out.close();

    }

    @RequestMapping("/login_p")
    public void loginRequierd(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String targetUrl = "";

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        }
        logger.info("请求 [" + targetUrl + "] 需要身份认证!");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String aName = headerNames.nextElement();
            logger.info(aName + ":" + request.getHeader(aName));
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // SC_UNAUTHORIZED);
        response.setContentType("application/json:charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"status\":\"error\",\"msg\":\"尚未登录，请先登录！\"}");
        out.flush();
        out.close();

    }

    /**
     * 当前用户的菜单树
     * 
     * @return
     */
    @RequestMapping("/config/sysmenu")
    public List<Menu> sysmenu() {
        return menuService.myMenuTree();
    }

    @RequestMapping("/config/hr")
    public User userInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    @RequestMapping(value = "/config/initSystem", method = RequestMethod.POST)
    public ResponseBean initializeSystem(String userName, String password, MultipartFile file) {
        int ret = initService.doInitialize(userName, password, file);
        if (ret == 0) {
            ResponseBean res = new ResponseBean("success", "初始化成功！");
            return res;
        }

        String message = "";
        if (ret == -1)
            message = "系统已初始！";
        else if (ret == -2)
            message = "请提供正确的用户名密码！";
        else
            message = "系统内部错误，初始失败！";

        return new ResponseBean("error", message);
    }

}