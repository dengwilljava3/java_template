package com.ay.rbac.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.rbac.dao.MenuDao;
import com.ay.rbac.entity.Menu;
import com.ay.rbac.entity.MenuExample;
import com.ay.rbac.entity.MenuExample.Criteria;
import com.ay.rbac.entity.Role;
import com.ay.rbac.mapper.MenuMapper;

@Service
public class MenuService {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private RoleService roleService;

	/** 加载菜单根据角色id */
	public List<Menu> selectByRoleIds(Long... roleIds) {
		if (roleIds == null || roleIds.length <= 0) {
			return null;
		}
		List<Menu> menuList = this.menuDao.selectByRoleIds(roleIds);
		menuList = menuList.stream().collect(Collectors.collectingAndThen(//
				Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getId()))), //
				ArrayList::new));
		return menuList;
	}

	/** 加载菜单根据用户名 */
	public List<Menu> selectByUsername(String username) {
		// 加载角色
		List<Role> roleList = this.roleService.selectByUsername(username);
		if (roleList == null) {
			return null;
		}
		List<Long> roleIdList = new ArrayList<>();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		// 加载菜单
		return this.selectByRoleIds(roleIdList.toArray(new Long[roleIdList.size()]));
	}

	public List<Menu> selectParentByChildren(List<Menu> funcMenuList) {
		if (funcMenuList == null || funcMenuList.size() <= 0) {
			return null;
		}
		List<Long> childList = new ArrayList<>();
		for (Menu menu : funcMenuList) {
			childList.add(menu.getId());
		}
		return this.selectParentByChildren(childList.toArray(new Long[childList.size()]));
	}

	public List<Menu> selectParentByChildren(Long... childMenuIds) {
		if (childMenuIds == null || childMenuIds.length == 0) {
			return null;
		}
		return this.menuDao.selectParentByChildIds(childMenuIds);
	}

	public List<Menu> selectByCondition(Menu menu) {
		MenuExample example = new MenuExample();
		Criteria createCriteria = example.createCriteria();
		if (menu != null) {
			if (menu.getLevel() != null) {
				createCriteria.andLevelEqualTo(menu.getLevel());
			}
		}
		return this.menuMapper.selectByExample(example);
	}

	@Transactional
	public int deleteMenuIdsByRoleId(Long roleId) {
		return this.menuDao.deleteMenuIdsByRoleId(roleId);
	}

	@Transactional
	public int insertRoleMenus(Long roleId, List<Long> menuIds) {
		// 过滤不是功能的权限
		MenuExample example = new MenuExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andIdIn(menuIds);
		List<Menu> menuList = this.menuMapper.selectByExample(example);
		if (menuList == null || menuList.size() <= 0) {
			return 0;
		}
		for (Menu menu : menuList) {
			if (menu.getLevel().byteValue() != (byte) 2) {
				menuIds.remove(menu.getId());
			}
		}
		if (menuIds == null || menuIds.size() <= 0) {
			return 0;
		}
		return this.menuDao.insertRoleMenus(roleId, menuIds);
	}

	@Transactional
	public int deleteRoleMenuByRoleId(Long roleId) {
		return this.menuDao.deleteRoleMenuByRoleId(roleId);
	}

	@Transactional
	public int save(Menu menu) {
		if (menu.getId() == null) {
			menu.setCreateTime(new Date());
			return this.menuMapper.insert(menu);
		}
		Menu old = this.menuMapper.selectByPrimaryKey(menu.getId());
		BeanUtils.copyProperties(menu, old, "id", "createTime");
		old.setUpdateTime(new Date());
		return this.menuMapper.updateByPrimaryKey(old);
	}

	@Transactional
	public int deleteMenuById(Long id) {
		this.menuDao.deleteRoleMenuByMenuId(id);
		return this.menuMapper.deleteByPrimaryKey(id);
	}

	public List<Menu> selectChildrenByParentId(Long parentId) {
		Menu menu = new Menu();
		menu.setParentId(parentId);
		List<Menu> menuList = this.selectByCondition(menu);
		return menuList;
	}

}
