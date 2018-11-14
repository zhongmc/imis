package com.ynet.imis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynet.imis.domain.menu.User;

import com.ynet.imis.service.security.UserDetailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by sang on 2017/12/28.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailService userService;
    @Autowired
    UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    private PersistentTokenRepository persistentLoginsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/index.html", "/config/initSystem", "/static/**", "/login_p");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        }).and().formLogin().loginPage("/login_p").loginProcessingUrl("/login").usernameParameter("username")
                .passwordParameter("password").permitAll().failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, AuthenticationException e)
                            throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        StringBuffer sb = new StringBuffer();
                        sb.append("{\"status\":\"error\",\"message\":\"");
                        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                            sb.append("用户名或密码输入错误，登录失败!");
                        } else if (e instanceof DisabledException) {
                            sb.append("账户被禁用，登录失败，请联系管理员!");
                        } else {
                            sb.append("登录失败!");
                        }
                        sb.append("\"}");
                        out.write(sb.toString());
                        out.flush();
                        out.close();
                    }
                }).successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, Authentication authentication)
                            throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");

                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        // return (Hr)
                        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        PrintWriter out = httpServletResponse.getWriter();
                        ObjectMapper objectMapper = new ObjectMapper();
                        String s = "{\"status\":\"success\",\"msg\":" + objectMapper.writeValueAsString(user) + "}";
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                }).and().logout().permitAll().logoutSuccessHandler(new LogoutSuccessHandler() {

                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        PrintWriter out = response.getWriter();
                        String s = "{\"status\":\"success\",\"message\":\"logout success!\"}";
                        out.write(s);
                        out.flush();
                        out.close();

                    }
                }).and().csrf().disable().rememberMe().rememberMeServices(rememberServices())
                .rememberMeParameter("rememberme").key("imis").and().exceptionHandling()
                .accessDeniedHandler(authenticationAccessDeniedHandler)
                .authenticationEntryPoint(new CustomizedAuthenticationEntryPoint() {

                });
    }

    @Bean
    public RememberMeServices rememberServices() {

        // SavedRequestAwareAuthenticationSuccessHandler;
        // TokenBasedRememberMeServices services = new
        // TokenBasedRememberMeServices("imis", userService);

        // key imis 与config中的一致
        PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices("imis",
                userService, persistentLoginsService);

        // 一定要(不是默认的名称 remember-me时)
        services.setParameter("rememberme");

        return services;
    }

    public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

        RequestCache requestCache = new HttpSessionRequestCache();

        Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException authException) throws IOException, ServletException {

            String targetUrl = "";

            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest != null) {
                targetUrl = savedRequest.getRedirectUrl();
            }
            logger.info("请求 [" + targetUrl + "] 需要身份认证!");

            // Enumeration<String> headerNames = request.getHeaderNames();
            // while (headerNames.hasMoreElements()) {
            // String aName = headerNames.nextElement();
            // logger.info(aName + ":" + request.getHeader(aName));
            // }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // SC_UNAUTHORIZED);
            response.setContentType("application/json:charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"error\",\"msg\":\"尚未登录，请先登录！\"}");
            out.flush();
            out.close();
        }

    }
}