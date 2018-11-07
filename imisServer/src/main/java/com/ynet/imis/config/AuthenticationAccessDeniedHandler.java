/**
* AuthenticationAccessDeniedHandler.java
* @author ZHONGMC
* @description 
* @created Thu Sep 13 2018 10:46:56 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Nov 07 2018 15:16:09 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp, AccessDeniedException e)
            throws IOException, ServletException {

        String msg = e.getMessage();
        if (msg.equals("未登录")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员!\"}");
        out.flush();
        out.close();

    }

}
