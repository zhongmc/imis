/**
* CommonController.java
* @author ZHONGMC
* @description 
* @created Wed Sep 26 2018 16:53:24 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Oct 17 2018 09:54:25 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.service.menu.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/")
    public void defaultHomePage(HttpServletResponse response) throws IOException {
        System.out.println("Request for root!");
        response.sendRedirect("/index.html");
        // return "forward:/index.html";

        // response.setStatus(HttpServletResponse.SC_OK);
        // response.setContentType("application/json:charset=UTF-8");

        // PrintWriter out = response.getWriter();
        // out.write("forward:/index.html");
        // out.flush();
        // out.close();

    }

    @RequestMapping("/login_p")
    public void loginRequierd(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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

}