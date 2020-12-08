package com.zhcx.netcar.netcarservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@WebFilter(filterName="corsConfig",urlPatterns="/*")
public class CorsConfig implements Filter {
	
	Logger log = LoggerFactory.getLogger(getClass());

	private String allowOrigin;
	private String allowMethods;
	private String allowCredentials;
	private String allowHeaders;
	private String exposeHeaders;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		log.info("CorsConfig Init...");
		allowOrigin = "*";
		allowMethods = "GET,POST,PUT,DELETE";
		allowCredentials = "true";

		allowHeaders = "Origin, X-Requested-With, Content-Type, Accept";
		exposeHeaders = "Content-Type";
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;

		HttpServletResponse response = (HttpServletResponse) res;

//		log.info(, request.getRequestURL());
		log.info("url: " + request.getRequestURL() + ", method: " + request.getMethod());

		if (null != allowOrigin && "" != allowOrigin) {
			List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
			if (null != allowOriginList && allowOriginList.size() > 0) {
				String currentOrigin = request.getHeader("Origin");

				response.setHeader("Access-Control-Allow-Origin", currentOrigin);
			}
		}
		if (null != allowMethods && "" != allowMethods) {
			response.setHeader("Access-Control-Allow-Methods", allowMethods);
		}
		if (null != allowCredentials && "" != allowCredentials) {
			response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
		}
		if (null != allowHeaders && "" != allowHeaders) {
			response.setHeader("Access-Control-Allow-Headers", allowHeaders);
		}
		if (null != exposeHeaders && "" != exposeHeaders) {
			response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
		}
		if (!request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString())) {
			chain.doFilter(req, res);
		}
	}

}
