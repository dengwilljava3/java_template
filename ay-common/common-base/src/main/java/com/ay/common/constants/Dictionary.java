package com.ay.common.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 通常字典表
 * 
 * @author jackson
 *
 */
public interface Dictionary {

	// 连表符
	String TABLE_JOIN = "_";

	int MAYBE_MAX_PROCESS = 5;

	/**
	 * 统计状态
	 */
	// 彩票狀態
	List<String> lottoLotteryTotal = Arrays.asList(ResultEnum.WIN.getKey(), ResultEnum.LOSS.getKey(), ResultEnum.CANCEL.getKey(), ResultEnum.TIE.getKey());

	public interface STATUS {
		final byte DISABLE = 0;
		final byte ENABLE = 1;
	}

	/**
	 * 终端类型
	 * 
	 * @author spark
	 *
	 */
	public interface TEMINAL {
		// 1:电脑 2:手机
		final byte COMPUTER = 1;
		final byte MOBILE = 2;
	}

	/**
	 * 真玩还是试玩
	 * 
	 * @author spark
	 *
	 */
	public interface PALY {
		// 0:试玩 1:真玩
		final byte DEMO_PLAY = 0;
		final byte TRUE_PLAY = 1;
	}

	/**
	 * 转账状态
	 * 
	 * @author jackson
	 *
	 */
	interface TransferStatus {
		// 0:开始转行 1:转账中2:转账成功3:转账失败4:转账异常
		byte BEGIN = 0;
		byte ALREADY = 1;
		byte SUCCESS = 2;
		byte FAILED = 3;
		byte EXCEPTION = 4;
	}

	enum MoneyTypeEnum {
		ACTIVITY_PRODUCT("activity_product", "推广产品活动"), //
		ACTIVITY_COUNT("activity_person", "推广人数活动"), //
		RATE_MONEY("rate_money", "一级推荐奖励"), //
		RATE_MONEY2("rate_money2", "二级推荐奖励"), //
		RATE_MONEY3("rate_money3", "三级推荐奖励"), //
		RATE_COUNT("rate_count", "一级推荐返利"), //
		RATE_COUNT2("rate_count2", "二级推荐返利"), //
		RATE_COUNT3("rate_count3", "三级推荐返利"), //
		RELEASE_MONEY("release_money", "返利金额"), //
		THREE_RELEASE("three_release", "三日返利金额"), //
		RELEASE_MONEY_BY_CHILD("RELEASE_MONEY_BY_CHILD", "下级激活奖励"), //
		RELEASE_MONEY2("release_money2", "返利金额"), //
		BACK("back", "后台修改"), //
		BUY("buy", "购买"), //
		BUY_POINT("buy_point", "购买送积分"), //
		REGISTER("register", "注册奖励"), //
		REGISTER_COUNT("register_count", "注册奖励股数"), //
		SIGN("sign", "签到奖励"), //
		POINT("point", "抽奖"), //
		CHARGE("charge", "充值"), //
		WITHDRAW("withdraw", "提现"), //
		WITHDRAW_FAILED("withdraw_failed", "提现失败退款"), //
		PROMISE_MONEY("promise_money", "保证金"), //
		CHANGE("change", "兑换"), //
		AUDIT_FAILED("audit_failed", "审核退款"), //
		RELEASE_PROMISE_MONEY("release_promise_money", "释放保证金"),

		MANUAL_CHARGE("manual_charge", "人工上分"), //
		MANUAL_WITHDRAW("manual_withdraw", "人工下分"), //
		GIFT_MONEY("gift_money", "上分赠送"), //
		REAL_VERIFY_REWARD("real_verify_reward", "实名认证奖励");

		private String key;
		private String value;

		private MoneyTypeEnum(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public static String getValueByKey(String key) {
			MoneyTypeEnum[] values = MoneyTypeEnum.values();
			for (MoneyTypeEnum moneyTypeEnum : values) {
				if (key.equals(moneyTypeEnum.getKey())) {
					return moneyTypeEnum.getValue();
				}
			}
			return null;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * 钱包中心转账类型
	 * 
	 * @author jackson
	 *
	 */
	interface MoneyType {
		// 1: 充值记录, 2: 下注记录, 3:派彩记录, 4: 退款记录, 5: 转账记录, 6: 反水记录, 7:取款记录, 8:代理佣金派发,
		// 9:代理佣金提取, 10:解冻/冻结资金, 11.活动奖金, 12:人工补单, 13:礼物, 14:抢单记录, 15:公司提款, 16:代收预扣
		// 17:代付添加
		int DEPOSIT = 1;
		int BET = 2;
		int PAYOFF = 3;
		int REFUND = 4;
		int TRANSFER = 5;
		int BET_BONUE = 6;
		int WITHDRAW = 7;
		int AGENT_COMMISSION_PAYOFF = 8;
		int AGENT_COMMISSION_WITHDRAWAL = 9;
		int FREEZE_BALANCE = 10;
		int ACTIVITY_BONUS = 11;
		int MANUAL_OPERATOR = 12;
		int GIFT = 13;
		int GRAB = 14;
		int CLIENT_WITHDRAWL = 15;
		int COLLECT = 16;
		int PAYBACK = 17;
	}

	/**
	 * 平台id
	 * 
	 * @author jackson
	 *
	 */
	public enum PlantId {
		AGIN("agin", "ag国际厅"), //
		AGQ("agq", "ag极速厅"), //
		MONEY("money", "钱包"), //
		H8("H8", "H8体育");

		private String key;

		private String value;

		private PlantId(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getValueByKey(String key) {
			PlantId[] values = PlantId.values();
			for (PlantId plantId : values) {
				if (plantId.getKey().equals(key)) {
					return plantId.getValue();
				}
			}
			return null;
		}

	}

	/**
	 * 彩种
	 * 
	 * @author jackson
	 *
	 */
	public enum GameType {

		live("live", "视讯"), //
		jilv("jilv", "老虎机"), //
		sport("sport", "体育"), //
		card("card", "棋牌"), //
		lottery("lottery", "彩票"), //
		electron("electron", "电子");

		private String key;
		private String value;

		private GameType(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 结果狀態
	 */
	public enum ResultEnum {
		// (1:WIN | 2:LOSE | 3:CANCELLED| 4:TIE |5:BET )
		WIN("WIN", "已中奖"), LOSS("LOSS", "未中奖"), CANCEL("CANCEL", "取消"), TIE("TIE", "和"), BET("BET", "投注中");

		private String key;
		private String value;

		private ResultEnum(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	public enum PayType {
		MONEY(0, "余额支付"), //
		WECHAT(1, "微信支付"), //
		ALIPAY(2, "支付宝");

		private Integer key;
		private String value;

		private PayType(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

		public Integer getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getValueByKey(Integer key) {
			if (key == null) {
				return null;
			}
			for (PayType type : PayType.values()) {
				if (key.intValue() == type.getKey().intValue()) {
					return type.getValue();
				}
			}
			return null;
		}
	}

	/**
	 * 支付状态枚举
	 * 
	 * @author jackson
	 *
	 */
	public enum PayStatusEnum {
		PAYING((byte) 1, "支付中"), SUCCESS((byte) 2, "支付成功"), FAILED((byte) 3, "支付失败"), //
		MAYBE((byte) 4, "支付异常"), LOCKED((byte) 5, "抢单锁定"), //
		TO_GRAB((byte) 6, "待抢单"), GRABED((byte) 7, "抢单完成"), //
		REFRESH((byte) 8, "重新生成"), REFUND((byte) 9, "退款中"), //
		TO_AUDIT((byte) 10, "待审核"), AUDITED((byte) 11, "审核通过"), AUDIT_FAIL((byte) 12, "审核不通过");

		private Byte key;
		private String value;

		private PayStatusEnum(Byte key, String value) {
			this.key = key;
			this.value = value;
		}

		public Byte getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getValueByKey(Byte key) {
			if (key == null) {
				return null;
			}
			PayStatusEnum[] values = PayStatusEnum.values();
			for (PayStatusEnum payStatusEnum : values) {
				if (payStatusEnum.key.byteValue() == key.byteValue()) {
					return payStatusEnum.value;
				}
			}
			return null;
		}
	}

	/**
	 * 直播状态
	 * 
	 * @author jackson
	 *
	 */
	public enum LiveStatus {

		NORMAL((byte) 1, "正常"), //
		DISABLE((byte) 0, "禁止登陆"), //
		LIVING((byte) 2, "直播中"), //
		REALNAME((byte) 3, "待认证");

		private Byte key;
		private String value;

		private LiveStatus(Byte key, String value) {
			this.key = key;
			this.value = value;
		}

		public Byte getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getValueByKey(Byte key) {
			if (key == null) {
				return null;
			}
			LiveStatus[] values = LiveStatus.values();
			for (LiveStatus liveStatusEnum : values) {
				if (liveStatusEnum.key.byteValue() == key.byteValue()) {
					return liveStatusEnum.value;
				}
			}
			return null;
		}

	}

	/**
	 * 彩票类型
	 * 
	 * @author jackson
	 *
	 */
	public enum LotteryType {
		HK6("hk6", "六合彩");

		private String key;
		private String value;

		private LotteryType(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public static String getValueByKey(String key) {
			LotteryType[] values = LotteryType.values();
			for (LotteryType lotteryType : values) {
				if (lotteryType.getKey().equals(key)) {
					return lotteryType.getValue();
				}
			}
			return null;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	/**
	 * 下注状态枚举
	 * 
	 * @author jackson
	 *
	 */
	public enum BetStatusEnum {
		BET((byte) 1, "下注未付款"), //
		BETTED((byte) 2, "下注完成"), //
		BET_EXCEPTION((byte) 3, "下注付款异常"), //
		BET_FAILED((byte) 4, "下注付款失败"), //
		WIN((byte) 5, "盈利"), //
		LOSS((byte) 6, "亏损");

		private Byte key;
		private String value;

		private BetStatusEnum(Byte key, String value) {
			this.key = key;
			this.value = value;
		}

		public Byte getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * 下注状态枚举
	 * 
	 * @author jackson
	 *
	 */
	public enum MsgStatusEnum {
		TO_SEND((byte) 1, "待发送"), //
		SENDED((byte) 2, "发送成功"), //
		SEND_FAILED((byte) 3, "发送失败"), //
		SENDING((byte) 4, "发送中");

		private Byte key;
		private String value;

		private MsgStatusEnum(Byte key, String value) {
			this.key = key;
			this.value = value;
		}

		public Byte getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

}
