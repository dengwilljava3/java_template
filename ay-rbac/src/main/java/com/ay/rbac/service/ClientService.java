package com.ay.rbac.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.common.util.StringUtil;
import com.ay.rbac.dao.ClientDao;
import com.ay.rbac.entity.Client;
import com.ay.rbac.entity.ClientExample;
import com.ay.rbac.entity.ClientExample.Criteria;
import com.ay.rbac.mapper.ClientMapper;

@Service
public class ClientService {

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private ClientMapper clientMapper;

	private Map<String, Client> clientMap = new HashMap<>();

	@PostConstruct
	@Scheduled(fixedDelay = 1 * 60 * 60 * 1000)
	public Map<String, Client> getClientMap() {
		ClientExample example = new ClientExample();
		List<Client> clientList = this.clientMapper.selectByExample(example);
		if (clientList == null || clientList.size() <= 0) {
			return null;
		}
		for (Client client : clientList) {
			clientMap.put(client.getClientId(), client);
		}
		return clientMap;
	}

	public Client selectByUsername(String username) {
		return this.clientDao.selectByUsername(username);
	}

	public List<Client> selectByCondition(Client client) {
		ClientExample example = new ClientExample();
		Criteria createCriteria = example.createCriteria();
		if (!StringUtil.isNull(client.getClientId())) {
			createCriteria.andClientIdEqualTo(client.getClientId());
		}
		return this.clientMapper.selectByExample(example);
	}

	@Transactional
	public int insertClientUser(String clientId, Long userId) {
		Client client = new Client();
		client.setClientId(clientId);
		List<Client> clientList = this.selectByCondition(client);
		if (clientList == null || clientList.size() <= 0) {
			return 0;
		}
		client = clientList.get(0);
		return this.clientDao.insertClientUser(client.getId(), userId);
	}

}
