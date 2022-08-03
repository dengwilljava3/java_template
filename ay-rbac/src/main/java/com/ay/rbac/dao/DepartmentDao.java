package com.ay.rbac.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.ay.rbac.entity.Department;

public interface DepartmentDao {

	List<Department> selectByUsername(@Param("username") String username);

	int deleteDepartmentUserByDepartmentIdAndUserId(@Param("departmentId") Long departmentId, @Param("userId") Long userId);

	int insertUserDepartment(@Param("userId") Long userId, @Param("departmentIds") List<Long> departmentIds);

	int changeDepartmentIds(@Param("newDepartmentId") Long newDepartmentId, @Param("oldDepartmentIds") Set<Long> oldDepartmentIds);

	List<Department> selectByRoleId(@Param("roleId") Long roleId);

}
