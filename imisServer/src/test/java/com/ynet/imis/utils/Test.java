package com.ynet.imis.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test{

    public static void main(String[] args) {

        try{

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM");
            SimpleDateFormat outFmt = new SimpleDateFormat("yyyy/MM/dd");
            String strVal = "2017/4";
            Date aDate = fmt.parse(strVal);
            System.out.println(String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            strVal = "2017/12";
            aDate = fmt.parse(strVal);
            System.out.println(String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            strVal = "2018/11";
            aDate = fmt.parse(strVal);
            System.out.println(String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            Date today = new Date();
            System.out.println(outFmt.format( today) + " " + today.getTime());

            


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
}