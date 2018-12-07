/**
* ImisApplication.java
* @author ZHONGMC
* @description  继承于 SpringBootServletInitializer 为了适应打包为war 部署到JavaEE容器
* @created Tue Sep 11 2018 10:10:08 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Dec 06 2018 15:53:16 GMT+0800 (中国标准时间)
*/

package com.ynet.imis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ImisApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ImisApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ImisApplication.class);
	}
}
