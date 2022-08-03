package com.ay.session.mysql.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.common.constants.Dictionary;
import com.ay.common.service.base.CommonService;
import com.ay.common.util.DateUtil;
import com.ay.common.util.StringUtil;
import com.ay.common.util.UUIDUtil;
import com.ay.session.mysql.entity.Session;
import com.ay.session.mysql.entity.SessionExample;
import com.ay.session.mysql.entity.SessionExample.Criteria;
import com.ay.session.mysql.mapper.SessionMapper;

@Service
public class SessionService extends CommonService {

	private Map<String, Session> sessionIdMap = new ConcurrentHashMap<>();

	@Autowired
	private SessionMapper sessionMapper;

	@Transactional
	public Session saveSession(Session session) {
		if (session == null) {
			return null;
		}
		List<Session> sessionList = selectByCondition(session);
		if (sessionList == null || sessionList.size() <= 0) {
			session.setLastRequestTime(DateUtil.getCurrentDate());
			session.setSessionId(System.currentTimeMillis() + UUIDUtil.generateUUID());
			session.setTimeout(Byte.valueOf(Dictionary.STATUS.ENABLE + ""));
			session.setPrivileged(Dictionary.STATUS.DISABLE);
			this.sessionMapper.insert(session);
			sessionIdMap.put(session.getSessionId(), session);
			return session;
		}
		Session oldSession = sessionList.get(0);
		oldSession.setLastRequestTime(DateUtil.getCurrentDate());
		oldSession.setTimeout(Byte.valueOf(Dictionary.STATUS.ENABLE + ""));
		// TODO: 上线的时候打开
//		oldSession.setSessionId(System.currentTimeMillis() + session.getUsername());
		this.sessionMapper.updateByPrimaryKeySelective(oldSession);
		sessionIdMap.put(oldSession.getSessionId(), oldSession);
		return oldSession;
	}

	public List<Session> selectByCondition(Session session) {
		SessionExample example = new SessionExample();
		Criteria createCriteria = example.createCriteria();
		if (!StringUtil.isNull(session.getUsername())) {
			createCriteria.andUsernameEqualTo(session.getUsername());
		}
		if (!StringUtil.isNull(session.getSessionId())) {
			createCriteria.andSessionIdEqualTo(session.getSessionId());
		}
		if (session.getTimeout() != null) {
		}
		return this.sessionMapper.selectByExample(example);
	}

	@Transactional
	public int deleteSession(Session session) {
		if (session == null) {
			return 0;
		}
		List<Session> sessionList = selectByCondition(session);
		if (sessionList == null || sessionList.size() <= 0) {
			return 0;
		}
		Session existSession = sessionList.get(0);
		return this.sessionMapper.deleteByPrimaryKey(existSession.getId());
	}

	@Transactional
	public void cleanUpByTime(Date halfHourAgo) {
		SessionExample example = new SessionExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andLastRequestTimeLessThan(halfHourAgo);
		List<Session> sessionList = this.sessionMapper.selectByExample(example);
		if (sessionList == null || sessionList.size() <= 0) {
			return;
		}
		for (Session session : sessionList) {
			this.deleteSession(session);
		}
	}

	public String getUsernameBySessionId(String sessionId) {
		return this.sessionIdMap.get(sessionId).getUsername();
	}

	public Session getSessionBySessionId(String sessionId) {
		return this.sessionIdMap.get(sessionId);
	}

}
