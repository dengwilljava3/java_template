package com.ay.rbac.vo;

import com.ay.rbac.entity.Role;

/**
 * 扩展角色
 * 
 * @author jackson
 *
 */
public class QRoleVo extends Role {

	private static final long serialVersionUID = -4550918481471570602L;

	private Long menuId;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

}
