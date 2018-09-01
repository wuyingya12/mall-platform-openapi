package com.car.mall.openapi.base.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.car.mall.openapi.base.ResponseEntity;
import com.car.mall.openapi.utils.LogExceptionUtil;
import com.car.mall.openapi.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class StatelessAuthcFilter extends AccessControlFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(StatelessAuthcFilter.class);

	@Autowired
	private RedisUtil redisUtil;
	private String[] esc = new String[] {"/logout","/login"};

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response){
		try{
			Subject subject = SecurityUtils.getSubject();
//			if(!subject.isAuthenticated()) {
//				//如果没有登录，直接进行之后的流程
//				return true;
//			}

			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String loginToken = httpRequest.getHeader("login-token");
			String requestURI = httpRequest.getRequestURI();
			logger.info("onAccessDenied访问请求地址："+getIpAddr(httpRequest)+requestURI);
			logger.info("onAccessDenied请求访问："+JSONObject.toJSONString(getHeadersInfo(httpRequest)));

//			boolean needCheck = !include(requestURI);
			boolean needCheck = false;

			if (needCheck) {
				boolean isAjax = isAjax(request);
				//查看是否传递过来登录验证token
				if (StringUtils.isEmpty(loginToken) || StringUtils.isEmpty(loginToken)) {
					if (isAjax) {
						onAjaxAuthFail(request, response);
					} else {
						onLoginFail(request, response);
					}
					logger.info("login-token为空");
					return false;
				}

//				CinemaSessionData cinemaSessionData = (CinemaSessionData) subject.getPrincipal();
//				if(null == cinemaSessionData || StringUtils.isNotEmpty(cinemaSessionData.getAccountName())){
//					cinemaSessionData = (CinemaSessionData) SerializeUtils.deserialize(redisUtil.getCinemaLoginKey(null,loginToken));
//					if(null == cinemaSessionData){
//						onLoginFail(request, response);
//						logger.info("CinemaSessionData获取为空");
//						return false;
//					}
//				}

//				String cinemaId = request.getParameter("cinemaId");
//				if(StringUtils.isNotEmpty(cinemaId) && !cinemaSessionData.getCinemaId().equals(Integer.valueOf(cinemaId))){
//					logger.error("传参的cinemaId："+cinemaId+"和登录用户所属cinemaId："+cinemaSessionData.getCinemaId()+"不符合");
//					onLoginFail(request, response);
//					return false;
//				}

				//登录token已过期
//				Object obj = redisUtil.getCinemaLoginKey(cinemaSessionData.getAccountName(),loginToken);
//				if(null == obj){
//					onLoginFail(request, response);
//					logger.info("loginToken过期："+loginToken);
//					return false;
//				}
			}
		}catch (Exception e){
			logger.error(LogExceptionUtil.getTrace(e));
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断是否需要验证
	 * @param requestURI
	 * @return
	 */
	public boolean include(String requestURI) {
		for (String url : esc) {
			if (requestURI.endsWith(url)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断是否Ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjax(ServletRequest request) {
		boolean isAjax = false;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest rq = (HttpServletRequest) request;
			String requestType = rq.getHeader("X-Requested-With");
			if (requestType != null && "XMLHttpRequest".equals(requestType)) {
				isAjax = true;
			}
		}
		return isAjax;
	}

	protected void onAjaxAuthFail(ServletRequest request, ServletResponse response) throws IOException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
//		httpResponse.getOutputStream().println(JSONObject.toJSONString(ResponseEntity.unauth()));

		httpResponse.getWriter().write(JSONObject.toJSONString(ResponseEntity.unauth()));
	}

	protected void onLoginFail(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		httpResponse.getWriter().write(JSONObject.toJSONString(ResponseEntity.sessionOver()));
//		httpResponse.getOutputStream().println(JSONObject.toJSONString(ResponseEntity.sessionOver()));
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-real-ip");//先从nginx自定义配置获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}


}