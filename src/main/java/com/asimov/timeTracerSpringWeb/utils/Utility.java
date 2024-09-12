package com.asimov.timeTracerSpringWeb.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utility {
    public static SimpleDateFormat sdfWsecs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static Timestamp str2Timestamp(String strDate) {
        return str2Timestamp(strDate, false);
    }

    public static Timestamp str2Timestamp(String strDate, boolean seconds) {
        try {
            String strDateWOt = strDate.replace("T", " ");
            if(seconds) {
                String strDateWsecs = strDateWOt.length() == 16 ? strDateWOt + ":00" : strDateWOt;
                return new java.sql.Timestamp((sdfWsecs.parse(strDateWsecs).getTime()));
            } else {
                return new java.sql.Timestamp((sdf.parse(strDateWOt.substring(0,16)).getTime()));
            }
        } catch (ParseException e) {
            return new java.sql.Timestamp(0);
        }
    }
}
