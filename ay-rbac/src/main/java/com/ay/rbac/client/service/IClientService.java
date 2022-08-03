package com.ay.rbac.client.service;

import java.util.Map;

/**
 * 所有商户系統，都需要實現這個類
 * 
 * @author jackson
 *
 */
public interface IClientService<T> {
	
	/**
	 * 获取所有的商户,{key = 商户id, Value = 商户对象}
	 * @return
	 */
	Map<String, T> getClientMap();
	
	
	

}
