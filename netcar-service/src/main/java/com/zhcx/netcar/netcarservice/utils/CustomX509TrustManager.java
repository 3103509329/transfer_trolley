package com.zhcx.netcar.netcarservice.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * https安全证书管理器
 * @title 
 * @author 龚进
 * @date 2017年12月21日
 * @version 1.0
 */
public class CustomX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
