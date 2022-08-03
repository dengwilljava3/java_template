package com.ay.rbac.vo;

import java.util.List;

import com.ay.rbac.entity.Menu;

/**
 * 导航栏菜单显示列表
 * 
 * @author jackson
 *
 */
public class MenuVo extends Menu {

	private static final long serialVersionUID = -636946872425160966L;

	private List<MenuVo> childrenMenu;

	public List<MenuVo> getChildrenMenu() {
		return childrenMenu;
	}

	public void setChildrenMenu(List<MenuVo> childrenMenu) {
		this.childrenMenu = childrenMenu;
	}

}
