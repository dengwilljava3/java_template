package com.ay.rbac.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ay.common.controller.base.BaseController;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Role;
import com.ay.rbac.service.DepartmentService;
import com.ay.rbac.service.RoleService;
import com.ay.rbac.vo.RoleVo;
import com.ay.session.mysql.entity.Session;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class RoleController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	@Autowired
	private DepartmentService departmentService;

	@ApiOperation(value = "根据用户加载角色")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getRolesByUsername", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getRolesByUsername(HttpServletRequest request) {
		try {
			Session session = (Session) request.getAttribute("session");
			List<Role> roleList = this.roleService.selectByUsername(session.getUsername());
			return result(SUCCESS, roleList);
		} catch (Exception e) {
			logger.error("getRolesByUsername 出错 : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "根据部门id加载角色")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{departmentId:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getRolesByDepartmentId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getRolesByDepartmentId(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getRolesByDepartmentId param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Long departmentId = parseObject.getLong("departmentId");
			if (departmentId == null) {
				return result(PARAM_IS_NULL, "department id is null!");
			}
			List<Role> roleList = this.roleService.selectByDepartmentId(departmentId);
			return result(SUCCESS, roleList);
		} catch (Exception e) {
			logger.error("login 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "加载所有角色")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getAllRoles(HttpServletRequest request) {
		try {
			List<RoleVo> allRoleVo = this.roleService.getAllRoleVo();
			return result(SUCCESS, allRoleVo);
		} catch (Exception e) {
			logger.info("getRoles 出错 : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "保存或修改角色")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id:xx, name:xx, memo:xx, menuIds:[原有的菜单id也要传], departmentId:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/saveRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> saveRole(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject jsonObj = JSONObject.parseObject(param);
			Long departmentId = jsonObj.getLong("departmentId");
			if (departmentId == null) {
				return result(PARAM_IS_NULL, "departmentId is null!");
			}
			RoleVo roleVo = jsonObj.toJavaObject(RoleVo.class);
			Department department = this.departmentService.selectById(departmentId);
			if (department.getParentId() == null) {
				return result(ERROR, "company don't exist role, please child department add the role!");
			}
			Role role = this.roleService.saveDepartmentRoleMenus(departmentId, roleVo);
			return result(SUCCESS, role);
		} catch (Exception e) {
			logger.info("saveRole 出错 : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "删除角色信息")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{roleId:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> deleteRole(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("deleteRole param = {}", param);
		try {
			JSONObject jsonObj = JSONObject.parseObject(param);
			Long roleId = jsonObj.getLong("roleId");
			if (roleId == null) {
				return result(PARAM_IS_NULL, "roleId is null");
			}
			// 查询岗位是否关联用户
			this.roleService.deleteById(roleId);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.info("deleteRole 出错 : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

}
