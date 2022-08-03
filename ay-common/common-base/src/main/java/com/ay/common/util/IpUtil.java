package com.ay.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {

	public static String getHostIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
}
