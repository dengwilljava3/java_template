package com.ay.pay.dubbo.service;

import java.math.BigDecimal;

import com.ay.pay.dubbo.service.vo.PayVo;

/**
 * 支付dubbo服务
 * 
 * @author jackson
 *
 */
public interface PayDubboService {
	
	String pay(PayVo vo);

	/**
	 * 获取支付url或页面
	 * 
	 * @param siteId
	 *            网站
	 * @param username
	 *            用户
	 * @param orderNum
	 *            订单号
	 * @param type
	 *            支付类型
	 * @param money
	 *            金额
	 * @return url或页面
	 */
	String getPayUrl(String siteId, String username, String orderNum, String type, BigDecimal money);

	/**
	 * 各自的支付类型
	 * 
	 * @return 定义支付类型
	 */
	String getType();

}
