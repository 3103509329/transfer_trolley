package com.zhcx.netcar.netcarservice.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class UrlParamUtil {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public UrlParamUtil() {
    }

    public static final Map<String, String> getParameters(HttpServletRequest request) throws UnsupportedEncodingException {
        return getParameters(request, "UTF-8");
    }

    public static final Map<String, String> getParameters(HttpServletRequest request, String charset) throws UnsupportedEncodingException {
        Map<String, String> paramMap = new HashMap();

        String paramName;
        String paramValue;
        for(Enumeration paramNames = request.getParameterNames(); paramNames.hasMoreElements(); paramMap.put(CoderUtil.decode(paramName), CoderUtil.decode(paramValue, charset))) {
            paramName = (String)paramNames.nextElement();
            paramValue = request.getParameter(paramName);
            if (paramValue == null) {
                paramValue = "";
            }
        }

        return paramMap;
    }
}