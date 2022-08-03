package com.ay.rbac.interceptor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ay.common.controller.base.BaseController;
import com.ay.common.util.JSONUtil;
import com.ay.common.util.StringUtil;
import com.ay.session.mysql.entity.Session;
import com.ay.session.mysql.service.SessionService;

/**
 * 安全拦截,拦截资源问题
 * 
 * @author jackson
 *
 */
public class SecurityInterceptor extends BaseController implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private SessionService sessionService;

	/**
	 * header参数<br>
	 * 1.clientId 商户号<br>
	 * 2.username 用户名<br>
	 * 3.timestamp 当天日期<br>
	 * 4.encrypt r5 + hexsha1加密后的字符串 + r6<br>
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// servlet.inputstrream()只能读取一次,需缓存到setAttribute
		String param = getRequestBody(request);
		request.setAttribute("param", param);
		String ip = this.getIpAddr(request);
		logger.info("uri = {}, ip = {}, param = {}", request.getRequestURI(), ip, param);
		/*
		 * if(true) { return true; }
		 */
		// 过滤api文档
		if (request.getRequestURI().contains("swagger")//
				|| request.getRequestURI().endsWith("api-docs") //
				|| request.getRequestURI().contains("configuration") //
				|| request.getRequestURI().endsWith("error")//
				|| request.getRequestURI().endsWith("NoValid")) { // 过滤api文档, 接受短信
			return true;
		}
		try {
			/*
			 * if(1==1) { return true; }
			 */
//			String sessionId = this.getSessionId(request);
			String sessionId = this.getSessionIdInHeader(request);
			if (StringUtil.isNull(sessionId)) {
				responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null!")));
				// response.sendRedirect(request.getContextPath() + "/login.html");
				return false;
			}
			// 1. session 过滤
			if (this.sessionService == null) {
				sessionService = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()).getBean(SessionService.class);
			}
			// 先从内存中取, 取不到匹配数据库
			Session session = this.sessionService.getSessionBySessionId(sessionId);
			if (session == null) {
				session = new Session();
				session.setSessionId(sessionId);
				List<Session> sessionList = sessionService.selectByCondition(session);
				if (sessionList == null || sessionList.size() <= 0) {
					logger.info("username = {}, session 过期或不存在!", session.getUsername());
					responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null or expire!")));
					return false;
				}
				session = sessionList.get(0);
				if (session == null) {
					// response.sendRedirect(request.getContextPath() + "/login.html");
					responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null or expire!")));
					return false;
				}
				if (StringUtil.isNull(session.getSessionId()) || StringUtil.isNull(session.getUsername())) {
					responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null or expire!")));
					return false;
				}
			}
			// 判断session是否超时 30 分钟
			Long time = System.currentTimeMillis() - session.getLastRequestTime().getTime();
			if (time > 30 * 60 * 1000) {
				responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null or expire!")));
				return false;
			}
			this.sessionService.saveSession(session);
			request.setAttribute("session", session);
			return true;
		} catch (Exception e) {
			logger.error("拦截器异常 : ", e);
			responseMessage(response, JSONUtil.map2Json(result(MAYBE, "interceptor is exception, the reason : " + e.getMessage())));
			return false;
		}
	}

	private String getRequestBody(HttpServletRequest request) throws UnsupportedEncodingException, IOException {
		request.setCharacterEncoding("UTF-8");
		int size = request.getContentLength();
		InputStream is = request.getInputStream();
		byte[] reqBodyBytes = readBytes(is, size);
		return new String(reqBodyBytes);
	}

	private void responseMessage(HttpServletResponse response, String msg) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		writer.write(msg);
		writer.flush();
		writer.close();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private final byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					// Should not happen.
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				// Ignore
				e.printStackTrace();
			}
		}

		return new byte[] {};
	}

}
