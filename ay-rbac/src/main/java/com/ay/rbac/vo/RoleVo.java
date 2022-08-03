package com.ay.rbac.vo;

import java.util.List;

import com.ay.rbac.entity.Role;

/**
 * 扩展角色
 * 
 * @author jackson
 *
 */
public class RoleVo extends Role {

	private static final long serialVersionUID = -4550918481471570602L;

	private List<Long> menuIds;

	private Long departmentId;

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

}
