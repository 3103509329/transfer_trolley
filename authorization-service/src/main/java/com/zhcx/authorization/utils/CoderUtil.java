package com.zhcx.authorization.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CoderUtil {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public CoderUtil() {
    }

    public static final String decode(String str, String charset) throws UnsupportedEncodingException {
        try {
            return URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException var3) {
            throw var3;
        } catch (Exception var4) {
            return str;
        }
    }

    public static final String encode(String str, String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, charset);
    }

    public static final String decode(String str) throws UnsupportedEncodingException {
        return decode(str, "UTF-8");
    }

    public static final String encode(String str) throws UnsupportedEncodingException {
        return encode(str, "UTF-8");
    }

    public static final String decode(String str, String charset, int count) throws UnsupportedEncodingException {
        for(int i = 0; i < count; ++i) {
            str = URLDecoder.decode(str, charset);
        }

        return str;
    }

    public static final String encode(String str, String charset, int count) throws UnsupportedEncodingException {
        for(int i = 0; i < count; ++i) {
            str = URLEncoder.encode(str, charset);
        }

        return str;
    }

    public static final String decode(String str, int count) throws UnsupportedEncodingException {
        for(int i = 0; i < count; ++i) {
            str = decode(str, "UTF-8");
        }

        return str;
    }

    public static final String encode(String str, int count) throws UnsupportedEncodingException {
        for(int i = 0; i < count; ++i) {
            str = encode(str, "UTF-8");
        }

        return str;
    }
}
