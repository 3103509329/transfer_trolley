package com.zhcx.authorization.utils;

import java.io.UnsupportedEncodingException;

public class UtilTools {

    public static String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    /**
     * 填充字符串
     *
     * @param data   数据
     * @param length 目标长度
     * @param right  是否从右边开始
     * @param type   填充的字段类型
     * @return
     */
    public static String fillStrByLength(String data, int length, boolean right, String type) {
        StringBuilder result = new StringBuilder(length);
        int dataLenth = 0;
        try {
            dataLenth = data.getBytes("GBK").length;
            if (dataLenth > length || length - dataLenth <= 0) {
                return data;
            }


            int fillLength = length - dataLenth;
            String fillStr = type;
            //组装补足字符串
            StringBuilder addStr = new StringBuilder(fillLength);
            for (int i = 0; i < fillLength; i++) {
                addStr.append(fillStr);
            }
            if (right) {
                result.append(data).append(addStr);
            } else {
                result.append(addStr).append(data);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


}
