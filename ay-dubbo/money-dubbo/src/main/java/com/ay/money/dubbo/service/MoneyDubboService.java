package com.ay.money.dubbo.service;

import java.util.List;

import com.ay.money.entity.ActiveUser;
import com.ay.money.entity.MoneyLog;

/**
 * 钱包dubbo服务
 * 
 * @author jackson
 *
 */
public interface MoneyDubboService {

	/**
	 * 创建会员
	 * 
	 * @param clientId
	 *            商户号
	 * @param username
	 *            用户名
	 * @param trueName
	 *            真实姓名
	 * @param source
	 *            来源
	 * @return
	 */
	String createMember(String clientId, String username, String trueName, String source);

	/**
	 * 查询余额
	 * 
	 * @param clientId
	 *            商户号
	 * @param username
	 *            用户名
	 * @return 余额
	 */
	String queryBalance(String clientId, String username);

	/**
	 * 批量查询会员
	 * 
	 * @param clientId
	 *            商户号
	 * @param usernameList
	 *            用户名列表
	 * @return 会员列表
	 */
	String queryMembers(String clientId, List<String> usernameList);

	/**
	 * 批量查询会员
	 * 
	 * @param clientId
	 *            商户号
	 * @param usernameList
	 * 
	 * @return 会员map
	 */
	String queryMembersMap(String clientId, List<String> usernameList);

	/**
	 * 查询会员信息
	 * 
	 * @param clientId
	 *            商户号
	 * @param username
	 *            用户名
	 * @return
	 */
	String queryMember(String clientId, String username);

	/**
	 * 转账
	 * 
	 * @param clientId
	 *            商户号
	 * @param moneyLog
	 *            钱包日志对象
	 * @return
	 */
	String transfer(String clientId, MoneyLog moneyLog);

	/**
	 * 批量转账
	 * 
	 * @param clientId
	 *            商户号
	 * @param moneyLogList
	 *            钱包日志对象
	 * @return 成功的订单号
	 */
	String transferBatch(String clientId, List<MoneyLog> moneyLogList);

	/**
	 * 查询现金流
	 * 
	 * @param clientId
	 *            商户号
	 * @param moneyLog
	 *            钱包日志对象
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            每页显示的记录数
	 * @return
	 */
	String queryMoneyLog(String clientId, MoneyLog moneyLog, Integer pageNum, Integer pageSize);

	/**
	 * 批量查询订单日志
	 * 
	 * @param clientId
	 *            商户号
	 * @param orderNums
	 *            订单
	 * @return
	 */
	String queryLogByOrderNums(String clientId, List<String> orderNums);

	/**
	 * 查询活跃会员
	 * 
	 * @param activeUser
	 *            查询条件
	 * @return 活跃报表对象
	 */
	String queryActiveUserMap(ActiveUser activeUser);

}
