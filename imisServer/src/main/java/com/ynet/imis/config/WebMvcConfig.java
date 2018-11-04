package com.ynet.imis.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import com.ynet.imis.utils.DateConverter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        String tmpUtl = "";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            tmpUtl = "C:\\tmp\\imis\\";
        } else if (osName.contains("Mac OS X")) {
            tmpUtl = "/Users/jiong/Desktop/";
        } else if (osName.contains("Linux")) {
            tmpUtl = "/tmp/imis/";
        }
        File tmp = new File(tmpUtl);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tmpUtl);
        return factory.createMultipartConfig();
    }    

}