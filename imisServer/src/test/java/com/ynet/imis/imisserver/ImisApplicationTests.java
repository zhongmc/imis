package com.ynet.imis.imisserver;


import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ImisApplicationTests {

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        // 转换为格式化的json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


	@Test
	public void contextLoads() {
	}


    @Test
    public void encodePass(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("pass:'123',encoded:" + encoder.encode("123"));

    }

	protected void outputObject(Object obj ) throws Exception
    {
        String str = mapper.writeValueAsString( obj );
		System.out.println( str );

		// byte[] bb = str.getBytes("GBK");
		// System.out.write(bb);
		// System.out.println();

    }


}
