package com.ay.rbac.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.common.util.DateUtil;
import com.ay.common.util.EncUtil;
import com.ay.common.util.StringUtil;
import com.ay.rbac.dao.UserDao;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Menu;
import com.ay.rbac.entity.Role;
import com.ay.rbac.entity.User;
import com.ay.rbac.entity.UserExample;
import com.ay.rbac.entity.UserExample.Criteria;
import com.ay.rbac.mapper.UserMapper;
import com.ay.rbac.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserService {

	@Autowired
	private ClientService clientService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MenuService menuService;

	/** 登录 */
	@Transactional
	public User login(String username, String password) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andPasswordEqualTo(password);
		List<User> users = this.userMapper.selectByExample(example);
		if (users == null || users.size() <= 0) {
			return null;
		}
		User user = users.get(0);
		user.setUpdateTime(DateUtil.getCurrentDate());
		this.userMapper.updateByPrimaryKey(user);
		return user;
	}

	/** 保存用户以及角色 */
	@Transactional
	public void save(String clientId, User newUser, List<Long> newRoleIds, List<Long> departmentIds) {
		User user = null;
		if (newUser.getId() != null) {
			user = this.userMapper.selectByPrimaryKey(newUser.getId());
			user.setEmail(newUser.getEmail());
			user.setName(newUser.getName());
			user.setTel(newUser.getTel());
			user.setEnable(newUser.getEnable());
			user.setUpdateTime(DateUtil.getCurrentDate());
			this.userMapper.updateByPrimaryKey(user);
		} else {
			newUser.setUpdateTime(DateUtil.getCurrentDate());
			this.userMapper.insertSelective(newUser);
			user = newUser;
			this.clientService.insertClientUser(clientId, user.getId());
		}
		// 1. 删除以前的roleIds
		this.roleService.deleteRoleIdsByUserId(user.getId());
		// 2. 删除以前的departmentId
		this.departmentService.deleteDepartmentIdByUserId(user.getId());
		// 3. 直接插入
		this.departmentService.insertUserDepartment(user.getId(), departmentIds);
		this.roleService.insertUserRoles(user.getId(), newRoleIds);
	}

	@Transactional
	public int deleteByIds(List<Long> ids) {
		int i = 0;
		for (Long id : ids) {
			i += this.deleteById(id);
		}
		return i;
	}

	@Transactional
	public int deleteById(Long id) {
		// 1. 删除所拥有的角色
		this.roleService.deleteRoleIdsByUserId(id);
		// 2. 删除用户
		return this.userMapper.deleteByPrimaryKey(id);
	}

	public User selectById(Long id) {
		return this.userMapper.selectByPrimaryKey(id);
	}

	public List<User> selectByCondition(User user) {
		UserExample example = new UserExample();
		Criteria createCriteria = example.createCriteria();
		if (user != null) {
			if (!StringUtil.isNull(user.getUsername())) {
				createCriteria.andUsernameEqualTo(user.getUsername());
			}
			if (!StringUtil.isNull(user.getName())) {
				createCriteria.andNameLike(user.getName());
			}
			if (user.getAutoGrab() != null) {
				createCriteria.andAutoGrabEqualTo(user.getAutoGrab());
			}
			if (user.getEnable() != null) {
				createCriteria.andEnableEqualTo(user.getEnable());
			}
		}
		return this.userMapper.selectByExample(example);
	}

	public List<User> selectByRoleId(Long roleId) {
		return this.userDao.selectByRoleId(roleId);
	}

	public User selectBySessionId(String sessionId) {
		return this.userDao.selectBySessionId(sessionId);
	}

	@Transactional
	public int updatePassword(Long id, String password) {
		User user = this.userMapper.selectByPrimaryKey(id);
		if (user == null) {
			return 0;
		}
		user.setPassword(password);
		user.setUpdateTime(DateUtil.getCurrentDate());
		return this.userMapper.updateByPrimaryKey(user);
	}

	@Transactional
	public int updatePayPwd(Long id, String payPwd) {
		User user = this.userMapper.selectByPrimaryKey(id);
		if (user == null) {
			return 0;
		}
		user.setPayPwd(payPwd);
		user.setUpdateTime(DateUtil.getCurrentDate());
		return this.userMapper.updateByPrimaryKey(user);
	}

	@Transactional
	public int resetPassword(Long id) {
		User user = this.userMapper.selectByPrimaryKey(id);
		if (user == null) {
			return 0;
		}
		user.setPassword(EncUtil.toMD5("000000"));
		return this.userMapper.updateByPrimaryKey(user);
	}

	public PageInfo<UserVo> queryUser(User user, Integer pageNum, Integer pageSize) {
		if (pageNum != null && pageSize != null) {
			PageHelper.startPage(pageNum, pageSize);
		}
		List<UserVo> dataList = this.userDao.findByUsernameOrName(user);
		for (UserVo userVo2 : dataList) {
			// 查询关联的部门
			List<Department> departments = this.departmentService.selectByUsername(userVo2.getUsername());
			userVo2.setDepartments(departments);
			// 查询关联的角色
			List<Role> roles = this.roleService.selectByUsername(userVo2.getUsername());
			userVo2.setRoles(roles);
		}
		return new PageInfo<>(dataList);
	}

	public PageInfo<UserVo> queryDepartmentUsers(Set<Long> ids, String username, Integer pageNum, Integer pageSize) {
		if (pageNum != null && pageSize != null) {
			PageHelper.startPage(pageNum, pageSize);
		}
		List<UserVo> userList = queryDepartmentUsers(ids, username);
		for (UserVo userVo2 : userList) {
			// 查询关联的部门
			List<Department> departments = this.departmentService.selectByUsername(userVo2.getUsername());
			userVo2.setDepartments(departments);
			// 查询关联的角色
			List<Role> roles = this.roleService.selectByUsername(userVo2.getUsername());
			userVo2.setRoles(roles);
			List<Long> departmentIdList = new ArrayList<>();
			departments.forEach(e -> {
				departmentIdList.add(e.getId());
			});
			Map<Long, Department> topDepartments = this.departmentService.getTopDepartmentsById(departmentIdList.toArray(new Long[departmentIdList.size()]));
			Set<Entry<Long, Department>> entrySet = topDepartments.entrySet();
			Set<String> companies = new HashSet<>();
			for (Entry<Long, Department> entry : entrySet) {
				companies.add(entry.getValue().getName());
			}
			StringBuffer companyBuffer = new StringBuffer();
			companies.forEach(e -> {
				companyBuffer.append(e).append(",");
			});
			userVo2.setCompany(companyBuffer.substring(0, companyBuffer.length() - 1).toString());
		}
		return new PageInfo<>(userList);
	}

	/**
	 * 查询部门用户,是否包括自己
	 * 
	 * @param ids      部门ids
	 * @param username 自己
	 * @return
	 */
	public List<UserVo> queryDepartmentUsers(Set<Long> ids, String username) {
		List<UserVo> userList = this.userDao.queryDepartmentUsers(ids, username);
		return userList;
	}

	public User queryByUsername(String username) {
		UserExample example = new UserExample();
		Criteria createCriteria = example.createCriteria();
		if (!StringUtil.isNull(username)) {
			createCriteria.andUsernameEqualTo(username);
		}
		List<User> dataList = this.userMapper.selectByExample(example);
		if (dataList != null && dataList.size() > 0) {
			return dataList.get(0);
		}
		return null;
	}

	public boolean isAuthByName(String username, String functionName) {
		boolean isAuth = false;
		List<Menu> menuList = menuService.selectByUsername(username);
		for (Menu menu : menuList) {
			if (functionName.equals(menu.getName())) {
				isAuth = true;
				break;
			}
		}
		return isAuth;
	}

	public boolean isRoleByName(String username, String roleName) {
		boolean isRoleName = false;
		List<Role> roleList = roleService.selectByUsername(username);
		for (Role role : roleList) {
			if (!StringUtil.isNull(role.getName())) {
				if (role.getName().contains(roleName)) {
					isRoleName = true;
					break;
				}
			}
		}
		return isRoleName;
	}
}
