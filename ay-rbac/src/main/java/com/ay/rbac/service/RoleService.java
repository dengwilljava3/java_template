package com.ay.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.common.util.DateUtil;
import com.ay.rbac.dao.RoleDao;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Role;
import com.ay.rbac.mapper.RoleMapper;
import com.ay.rbac.vo.QRoleVo;
import com.ay.rbac.vo.RoleVo;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private MenuService menuService;

	@Autowired
	private DepartmentService departmentService;

	public List<Role> selectByUsername(String username) {
		return this.roleDao.selectByUsername(username);
	}

	public List<Long> selectRoleIdByUserId(Long userId) {
		return this.roleDao.selectRoleIdByUserId(userId);
	}

	@Transactional
	public int deleteRoleIdsByUserId(Long userId) {
		return this.roleDao.deleteRoleIdsByUserId(userId);
	}

	@Transactional
	public int insertUserRoles(Long userId, List<Long> roleIds) {
		return this.roleDao.insertUserRoles(userId, roleIds);
	}

	public List<RoleVo> getAllRoleVo() {
		List<QRoleVo> allRoles = this.roleDao.getAllRoleVo();
		Map<Long, RoleVo> allRolesMap = new HashMap<>();
		for (QRoleVo allRole : allRoles) {
			RoleVo vo = allRolesMap.get(allRole.getId());
			if (vo == null) {
				vo = new RoleVo();
				BeanUtils.copyProperties(allRole, vo, "menuId");
			}
			List<Long> menuIds = vo.getMenuIds();
			if (menuIds == null) {
				menuIds = new ArrayList<>();
				vo.setMenuIds(menuIds);
			}
			if (allRole.getMenuId() != null) {
				menuIds.add(allRole.getMenuId());
			}
			List<Long> departmentIds = this.departmentService.selectIdsByRoleId(allRole.getId());
			if (departmentIds != null && departmentIds.size() > 0) {
				vo.setDepartmentId(departmentIds.get(0));
			}
			allRolesMap.put(allRole.getId(), vo);
		}
		Set<Entry<Long, RoleVo>> entrySet = allRolesMap.entrySet();
		List<RoleVo> needRoles = new ArrayList<>();
		for (Entry<Long, RoleVo> entry : entrySet) {
			needRoles.add(entry.getValue());
		}
		return needRoles;
	}

	@Transactional
	public Role saveRoleMenus(RoleVo roleVo) {
		if (roleVo == null) {
			return null;
		}
		Role role = null;
		if (roleVo.getId() != null) {
			role = this.roleMapper.selectByPrimaryKey(roleVo.getId());
			if (role == null) {
				throw new RuntimeException("role id don't exists!");
			}
			role.setMemo(roleVo.getMemo());
			role.setName(roleVo.getName());
			role.setUpdateTime(DateUtil.getCurrentDate());
			this.roleMapper.updateByPrimaryKeySelective(role);
		} else {
			role = new Role();
			role.setCreateTime(DateUtil.getCurrentDate());
			role.setMemo(roleVo.getMemo());
			role.setName(roleVo.getName());
			this.roleMapper.insertSelective(role);
			roleVo.setId(role.getId());
		}
		// 1. 删除以前拥有的菜单
		this.menuService.deleteMenuIdsByRoleId(role.getId());
		// 2. 增加现在的菜单
		if (roleVo.getMenuIds() != null && roleVo.getMenuIds().size() > 0) {
			this.menuService.insertRoleMenus(role.getId(), roleVo.getMenuIds());
		}
		return role;
	}

	public Role selectById(Long id) {
		return this.roleMapper.selectByPrimaryKey(id);
	}

	@Transactional
	public int deleteById(Long roleId) {
		// 1. 删除用户角色关系
		this.deleteUserRoleByRoleId(roleId);
		// 2. 删除角色菜单关系
		this.menuService.deleteRoleMenuByRoleId(roleId);
		// 3. 删除部门角色关系
		this.deleteDepartmentRoleByDepartmentIdAndRoleId(null, roleId);
		// 4. 删除角色
		return this.roleMapper.deleteByPrimaryKey(roleId);
	}

	@Transactional
	private int deleteUserRoleByRoleId(Long roleId) {
		return this.roleDao.deleteUserRoleByRoleId(roleId);
	}

	public List<Role> selectByDepartmentId(Long departmentId) {
		if (departmentId == null) {
			return null;
		}
		return this.roleDao.selectByDepartmentId(departmentId);
	}

	@Transactional
	public Role saveDepartmentRoleMenus(Long departmentId, RoleVo roleVo) {
		// 1.保存角色菜单
		Role role = this.saveRoleMenus(roleVo);
		// 2.保存部门角色
		this.saveDepartmentRole(departmentId, roleVo.getId());
		return role;
	}

	@Transactional
	public int saveDepartmentRole(Long departmentId, Long roleId) {
		this.deleteDepartmentRoleByDepartmentIdAndRoleId(null, roleId);
		return this.roleDao.saveDepartmentRole(departmentId, roleId);
	}

	@Transactional
	public int deleteDepartmentRoleByDepartmentIdAndRoleId(Long departmentId, Long roleId) {
		// 1. 删除部门角色对应的关系
		return this.roleDao.deleteDepartmentRoleByDepartmentIdAndRoleId(departmentId, roleId);
	}

	public List<Role> selectByDepartmentIdAndUsername(Long departmentId, String username) {
		return this.roleDao.selectByDepartmentIdAndUsername(departmentId, username);
	}

	@Transactional
	public void deleteByDepartmentIds(Set<Long> departmentIds) {
		// 1. 删除角色
		List<Long> roleIdList = this.roleDao.selectRoleIdByDepartmentIds(departmentIds);
		if (roleIdList != null && roleIdList.size() > 0) {
			this.roleDao.deleteByIds(roleIdList);
		}
		// 2. 删除部门角色关系
		departmentIds.forEach(departmentId -> {
			this.deleteDepartmentRoleByDepartmentIdAndRoleId(departmentId, null);
		});
	}

	@Transactional
	public int addRoleMenus(Long roleId, Set<Long> menuId) {
		return this.roleDao.insertRoleMenus(roleId, menuId);
	}

}
