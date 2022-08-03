package com.ay.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ay.rbac.entity.Menu;

public interface MenuDao {

	/**
	 * 根据角色id查询菜单
	 * 
	 * @param ids
	 * @return
	 */
	List<Menu> selectByRoleIds(@Param("roleIds") Long... roleIds);

	/**
	 * 根据子菜单id加载父节点菜单
	 * 
	 * @param childMenuIds 子节点ids
	 * @return
	 */
	List<Menu> selectParentByChildIds(@Param("childMenuIds") Long... childMenuIds);

	int deleteMenuIdsByRoleId(@Param("roleId") Long roleId);

	int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

	int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

	int deleteRoleMenuByMenuId(@Param("menuId") Long menuId);

}
