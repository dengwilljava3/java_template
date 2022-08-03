package com.ay.rbac.dao;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import com.ay.rbac.entity.User;
import com.ay.rbac.vo.UserVo;

public interface UserDao {

	List<User> selectByRoleId(@Param("userId") Long roleId);

	List<UserVo> findByUsernameOrName(User user);

	List<UserVo> queryDepartmentUsers(@Param("ids") Set<Long> ids, @Param("username") String username);

	User selectBySessionId(@Param("sessionId") String sessionId);
}
