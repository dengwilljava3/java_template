package com.ay.rbac.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.ay.rbac.entity.Role;
import com.ay.rbac.vo.QRoleVo;

/**
 * 角色dao
 * 
 * @author jackson
 *
 */
public interface RoleDao {

	/**
	 * 根据用户名查询角色
	 * 
	 * @param username 用户名
	 * @return
	 */
	List<Role> selectByUsername(@Param("username") String username);

	/**
	 * 查询roleId根据userId
	 * 
	 * @param userId 用户id
	 * @return
	 */
	List<Long> selectRoleIdByUserId(@Param("userId") Long userId);

	/**
	 * 保存用户的角色id
	 * 
	 * @param userId  用户id
	 * @param roleIds 角色ids
	 * @return
	 */
	int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

	/**
	 * 删除角色ids根据userId
	 * 
	 * @param userId 用户id
	 * @return
	 */
	int deleteRoleIdsByUserId(@Param("userId") Long userId);

	List<QRoleVo> getAllRoleVo();

	int deleteUserRoleByRoleId(@Param("roleId") Long roleId);

	List<Role> selectByDepartmentId(@Param("departmentId") Long departmentId);

	int saveDepartmentRole(@Param("departmentId") Long departmentId, @Param("roleId") Long roleId);

	int deleteDepartmentRoleByDepartmentIdAndRoleId(@Param("departmentId") Long departmentId, @Param("roleId") Long roleId);

	/**
	 * 查询角色根据部门id和用户名
	 * 
	 * @param departmentId 部门id
	 * @param username     用户名
	 * @return
	 */
	List<Role> selectByDepartmentIdAndUsername(@Param("departmentId") Long departmentId, @Param("username") String username);

	/**
	 * 查询部门下的角色id
	 * 
	 * @param departmentIds
	 * @return
	 */
	List<Long> selectRoleIdByDepartmentIds(@Param("departmentIds") Set<Long> departmentIds);

	int deleteByIds(@Param("roleIds") List<Long> roleIds);

	int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") Set<Long> menuIds);

}
