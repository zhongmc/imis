/**
* DateConverter.java
* @author ZHONGMC
* @description 
* @created Thu Sep 13 2018 11:04:41 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Oct 16 2018 15:14:14 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.utils;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String, Date> {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String s) {
        if ("".equals(s) || s == null) {
            return null;
        }
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}