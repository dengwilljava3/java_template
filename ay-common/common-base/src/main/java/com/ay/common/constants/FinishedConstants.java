package com.ay.common.constants;

/**
 * 枚举常量
 * 
 * @author jackson
 *
 */
public enum FinishedConstants {

	NOT_START(1), // 尚未开始
	STARTING(2), // 正在进行中
	FINISHED(3), // 成功
	FAILURE(4); // 失败

	private Integer key;

	private FinishedConstants(Integer key) {
		this.key = key;
	}

	public Integer getKey() {
		return key;
	}

}
