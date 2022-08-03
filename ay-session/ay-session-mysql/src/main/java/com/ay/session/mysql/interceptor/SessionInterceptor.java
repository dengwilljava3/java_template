package com.ay.session.mysql.interceptor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ay.common.constants.Dictionary;
import com.ay.common.controller.base.BaseController;
import com.ay.common.util.JSONUtil;
import com.ay.common.util.StringUtil;
import com.ay.session.mysql.entity.Session;
import com.ay.session.mysql.service.SessionService;

/**
 * session拦截器<br>
 * 这个需要在校验拦截器之后执行
 * 
 * @author jackson
 *
 */
public class SessionInterceptor extends BaseController implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionService sessionService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			String param = request.getAttribute("param") + "";
			if (StringUtil.isNull(param)) {
				return true;
			}
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			if (StringUtil.isNull(username)) {
				return true;
			}
			if (this.sessionService == null) {
				this.sessionService = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()).getBean(SessionService.class);
			}
			Session session = new Session();
			session.setUsername(username);
			session.setTimeout(Dictionary.STATUS.ENABLE);
			List<Session> sessionList = this.sessionService.selectByCondition(session);
			if (sessionList == null || sessionList.size() <= 0) {
				responseMessage(response, JSONUtil.map2Json(result(SESSION_IS_NULL_OR_EXPIRE, "session is null or expire, please reLogin!")));
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("session 拦截器出错 : ", e);
			return true;
		} finally {

		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private void responseMessage(HttpServletResponse response, String msg) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
		writer.write(msg);
		writer.flush();
		writer.close();
	}

}
