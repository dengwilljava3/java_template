package com.ay.session.mysql.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface SessionDao {

	int deleteByTimestamp(@Param("timeoutTimestamp") Date timeoutTimestamp);

}
