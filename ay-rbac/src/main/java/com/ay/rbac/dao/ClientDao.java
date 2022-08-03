package com.ay.rbac.dao;

import org.apache.ibatis.annotations.Param;

import com.ay.rbac.entity.Client;

public interface ClientDao {

	Client selectByUsername(@Param("username") String username);

	int insertClientUser(@Param("clientId") Long clientId, @Param("userId") Long userId);

}
