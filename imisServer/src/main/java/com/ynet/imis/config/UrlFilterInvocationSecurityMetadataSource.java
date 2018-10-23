/**
* UrlFilterInvocationSecurityMetadataSource.java
* @author ZHONGMC
* @description 
* @created Thu Sep 13 2018 10:46:56 GMT+0800 (中国标准时间)
* @copyright 
* @last-modified Thu Sep 13 2018 10:55:24 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.config;

import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;

import com.ynet.imis.service.menu.MenuService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
  
	private Logger logger = LoggerFactory.getLogger(getClass());
  
    @Autowired
    MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Collection<UrlAttributes> urlAttributes = null;

    private class UrlAttributes{
        protected String url;
        protected Collection<ConfigAttribute> attributes;

        public UrlAttributes(String url, Collection<ConfigAttribute> attributes){
            this.url = url;
            this.attributes = attributes;
        }
    }


    //do initialize the url pattern settings
    private synchronized void doInistilize() {
        if( urlAttributes != null )
            return;
            
        logger.info("Do initialize the url attributes........" );    
        urlAttributes = new ArrayList<UrlAttributes>();

        Iterable<Menu> allMenu = menuService.listAllMenu();
        for (Menu menu : allMenu) {
            logger.info("menu url:" + menu.getUrl() + ", roles:" + menu.getRoles() );
            if( menu.getUrl() == null || menu.getRoles().size() == 0)
                continue;
            List<Role> roles = menu.getRoles();
            int size = roles.size();
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = roles.get(i).getName();
            }
            UrlAttributes attrs = new UrlAttributes(menu.getUrl(), SecurityConfig.createList(values));
            urlAttributes.add( attrs );
        }

    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        logger.info("get attrs: " + requestUrl);

        if ("/login_p".equals(requestUrl)) {
            return null;
        }

        doInistilize();

        for( UrlAttributes attrs: urlAttributes)
        {
            if( antPathMatcher.match(attrs.url, requestUrl) )
                return attrs.attributes;
        }

        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
