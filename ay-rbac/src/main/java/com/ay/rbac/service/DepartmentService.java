package com.ay.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.common.util.DateUtil;
import com.ay.rbac.dao.DepartmentDao;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.DepartmentExample;
import com.ay.rbac.entity.DepartmentExample.Criteria;
import com.ay.rbac.entity.Role;
import com.ay.rbac.mapper.DepartmentMapper;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	public Department selectById(Long id) {
		return this.departmentMapper.selectByPrimaryKey(id);
	}

	public List<Department> selectByUsername(String username) {
		return this.departmentDao.selectByUsername(username);
	}

	public List<Department> selectByIds(List<Long> ids) {
		if (ids == null || ids.size() <= 0) {
			return null;
		}
		DepartmentExample example = new DepartmentExample();
		example.createCriteria().andIdIn(ids);
		return this.departmentMapper.selectByExample(example);
	}

	public List<Department> getDepartmentByParentId(Long parentId) {
		DepartmentExample example = new DepartmentExample();
		Criteria createCriteria = example.createCriteria();
		if (parentId == null) {
			createCriteria.andParentIdIsNull();
		} else {
			createCriteria.andParentIdEqualTo(parentId);
		}
		return this.departmentMapper.selectByExample(example);
	}

	@Transactional
	public Department saveDepartment(Department department) {
		if (department == null) {
			return null;
		}
		if (department.getId() != null) {
			Department originalDepartment = this.selectById(department.getId());
			originalDepartment.setMemo(department.getMemo());
			originalDepartment.setName(department.getName());
			originalDepartment.setParentId(department.getParentId());
			originalDepartment.setUpdateTime(DateUtil.getCurrentDate());
			this.departmentMapper.updateByPrimaryKeySelective(originalDepartment);
			return originalDepartment;
		}
		department.setCreateTime(DateUtil.getCurrentDate());
		this.departmentMapper.insertSelective(department);
		return department;
	}

	@Transactional
	public void deleteById(Long id) {
		// 1.查询当前及子部门ids
		Set<Long> departmentIdList = new HashSet<>();
		departmentIdList.add(id);
		Set<Long> childDepartmentIds = this.queryDepartmentChildIds(departmentIdList);
		childDepartmentIds.add(id);
		// 2. 删除部门下的角色&删除部门角色关系
		// 2.1 删除部门下的角色
		this.roleService.deleteByDepartmentIds(childDepartmentIds);
		// 2.2 删除部门角色关系&删除角色
		childDepartmentIds.forEach(childId -> {
			List<Role> roleList = this.roleService.selectByDepartmentId(childId);
			roleList.forEach(role -> {
				this.roleService.deleteById(role.getId());
			});
			this.roleService.deleteDepartmentRoleByDepartmentIdAndRoleId(childId, null);
		});
		// 3. 查询该部门的顶级部门, 绑定到二级部门
		Department topDepartment = this.getTopDepartmentById(id);
		List<Department> childDepartmentId = this.getDepartmentByParentId(topDepartment.getId());
		// 4. 更新部门用户<==>更新到超管部门下
		this.departmentDao.changeDepartmentIds(childDepartmentId.get(0).getId(), childDepartmentIds);
		// this.departmentDao.deleteDepartmentUserByDepartmentIdAndUserId(id, null);
		// 5. 删除部门
		childDepartmentIds.forEach(departmentId -> {
			this.departmentMapper.deleteByPrimaryKey(departmentId);
		});
	}

	@Transactional
	public int deleteDepartmentIdByUserId(Long userId) {
		if (userId == null) {
			return 0;
		}
		return this.departmentDao.deleteDepartmentUserByDepartmentIdAndUserId(null, userId);
	}

	@Transactional
	public int insertUserDepartment(Long userId, List<Long> departmentIds) {
		return this.departmentDao.insertUserDepartment(userId, departmentIds);
	}

	public Department getTopDepartmentById(Long id) {
		Department department = selectById(id);
		while (department.getParentId() != null) {
			department = selectById(department.getParentId());
		}
		return department;
	}

	public Map<Long, Department> getTopDepartmentsById(Long... ids) {
		Map<Long, Department> resultMap = new HashMap<>();
		for (Long id : ids) {
			Department department = selectById(id);
			while (department.getParentId() != null) {
				department = selectById(department.getParentId());
			}
			resultMap.put(id, department);
		}
		return resultMap;
	}

	/**
	 * 根据当前部门id， 查询该部门下的子部门ids
	 * 
	 * @param departmentIds
	 * @return
	 */
	public Set<Long> queryDepartmentChildIds(Set<Long> departmentIds) {
		Set<Long> childSet = new HashSet<>();
		this.getChildDepartment(childSet, departmentIds);
		return childSet;
	}

	private void getChildDepartment(Set<Long> childSet, Set<Long> departmentIds) {
		for (Long id : departmentIds) {
			List<Department> childDepartmentList = this.getDepartmentByParentId(id);
			Set<Long> childDepartments = new HashSet<>();
			childDepartmentList.forEach(e -> {
				childSet.add(e.getId());
				childDepartments.add(e.getId());
			});
			this.getChildDepartment(childSet, childDepartments);
		}
	}

	/**
	 * 获取当前用户所拥有的departmentIds
	 * 
	 * @param departmentIds
	 * @return
	 */
	public Set<Long> queryUserDepartmentIds(String username) {
		Set<Long> idSet = new HashSet<>();
		// 根据用户名获取该用户所在部门Ids
		List<Department> departmentList = this.selectByUsername(username);
		for (Department department : departmentList) {
			idSet.add(department.getId());
		}

		// 获取部门所拥有的子部门ids
		Set<Long> childSet = this.queryDepartmentChildIds(idSet);

		// 合并ids
		idSet.addAll(childSet);
		return idSet;
	}

	/**
	 * 获取当前用户所拥有的子部门departmentIds
	 * 
	 * @param username
	 * @return
	 */
	public Set<Long> queryUserChildDepartmentIds(String username) {
		Set<Long> idSet = new HashSet<>();
		// 根据用户名获取该用户所在部门Ids
		List<Department> departmentList = this.selectByUsername(username);
		for (Department department : departmentList) {
			idSet.add(department.getId());
		}
		// 获取部门所拥有的子部门ids
		Set<Long> childSet = this.queryDepartmentChildIds(idSet);
		if (childSet != null && childSet.size() > 0) {
			idSet.addAll(childSet);
		}
		return idSet;
	}

	public List<Department> selectByRoleId(Long roleId) {
		return this.departmentDao.selectByRoleId(roleId);
	}

	public List<Long> selectIdsByRoleId(Long roleId) {
		List<Department> departmentList = this.selectByRoleId(roleId);
		List<Long> departmentIds = new ArrayList<>();
		departmentList.forEach(d -> {
			departmentIds.add(d.getId());
		});
		return departmentIds;
	}
}
