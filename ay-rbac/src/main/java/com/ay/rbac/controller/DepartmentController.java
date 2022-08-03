package com.ay.rbac.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.ay.common.controller.base.BaseController;
import com.ay.common.util.StringUtil;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Role;
import com.ay.rbac.service.DepartmentService;
import com.ay.rbac.service.RoleService;
import com.ay.rbac.vo.DepartmentVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class DepartmentController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "根据用户加载部门")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getDepartmentByUsername", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getDepartmentByUsername(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			List<Department> departmentList = this.departmentService.selectByUsername(parseObject.getString("username"));
			return result(SUCCESS, departmentList);
		} catch (Exception e) {
			logger.error("getDepartmentByUsername 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "加载部门根据上级部门id(不传默认加载顶级部门)")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{parentId : xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getDepartmentByParentId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getDepartmentByParentId(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Long parentId = parseObject.getLong("parentId");
			List<Department> departmentList = this.departmentService.getDepartmentByParentId(parentId);
			// 上级部门拥有的角色列表
			List<Role> roleList = null;
			if (parentId != null) {
				roleList = this.roleService.selectByDepartmentId(parentId);
			}
			// 加载子集部门和上级部门角色列表
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("departmentList", departmentList);
			resultMap.put("roleList", roleList);
			return result(SUCCESS, resultMap);
		} catch (Exception e) {
			logger.error("getDepartmentByParentId 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "保存部门")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id: xx, name: xx, memo: 备注, parentId: 父节点id}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/saveDepartment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> saveDepartment(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			if (StringUtil.isNull(parseObject.getString("parentId"))) {
				return result(PARAM_IS_NULL, "添加网站顶级部门联系申请开站人员!");
			}
			Department department = parseObject.toJavaObject(Department.class);
			department = this.departmentService.saveDepartment(department);
			DepartmentVo vo = new DepartmentVo();
			BeanUtils.copyProperties(department, vo);
			vo.setChildDepartment(new ArrayList<>());
			vo.setRoleList(new ArrayList<>());
			return result(SUCCESS, vo);
		} catch (Exception e) {
			logger.error("saveDepartment 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "删除部门")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id: xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/deleteDepartmentById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> deleteDepartmentById(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Long id = parseObject.getLong("id");
			if (id == null) {
				return result(PARAM_IS_NULL, "id is null!");
			}
			this.departmentService.deleteById(id);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("deleteDepartmentById 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "加载顶级部门根据部门id")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id : xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getTopDepartmentById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getTopDepartmentById(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Department department = this.departmentService.getTopDepartmentById(parseObject.getLong("id"));
			return result(SUCCESS, department);
		} catch (Exception e) {
			logger.error("getDepartmentByParentId 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "根据用户名加载部门角色树")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username : xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getDepartmentRoleByUsername", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getDepartmentRoleByUsername(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			List<Department> departmentList = this.departmentService.selectByUsername(username);
			Set<Long> departmentIds = new HashSet<>();
			for (Department department : departmentList) {
				departmentIds.add(department.getId());
			}
			// 查询子部门
			Set<Long> childIds = this.departmentService.queryDepartmentChildIds(departmentIds);
			Map<Long, List<DepartmentVo>> departmentMapByParentId = new HashMap<>();
			if (childIds != null && childIds.size() > 0) {
				List<Department> childDepartment = this.departmentService.selectByIds(new ArrayList<>(childIds));
				for (Department department : childDepartment) {
					DepartmentVo vo = new DepartmentVo();
					BeanUtils.copyProperties(department, vo); // 同级的角色不加载
					List<DepartmentVo> list = departmentMapByParentId.get(department.getParentId());
					if (list == null) {
						list = new ArrayList<>();
						departmentMapByParentId.put(department.getParentId(), list);
					}
					// 组装 角色
					List<Role> roleList = this.roleService.selectByDepartmentId(department.getId());
					vo.setRoleList(roleList);
					list.add(vo);
				}
			}
			// 从顶级子部门开始遍历各个子部门
			List<DepartmentVo> dataList = new ArrayList<>();
			for (Department department : departmentList) {
				DepartmentVo vo = new DepartmentVo();
				BeanUtils.copyProperties(department, vo);
				if (departmentMapByParentId != null && departmentMapByParentId.size() > 0) {
					vo = findChildDepartment(vo, department.getId(), departmentMapByParentId);
				}
				// 过滤同级,只保留自己拥有的
				List<Role> roleList = this.roleService.selectByDepartmentIdAndUsername(department.getId(), username);
				vo.setRoleList(roleList);
				dataList.add(vo);
			}
			return result(SUCCESS, dataList);
		} catch (Exception e) {
			logger.error("getDepartmentByParentId 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	private DepartmentVo findChildDepartment(DepartmentVo parent, Long parentId, Map<Long, List<DepartmentVo>> departmentMapByParentId) {
		// 1.获取到顶级子部门
		List<DepartmentVo> childTopDepartment = this.findTopChildDepartment(parentId, departmentMapByParentId);
		// 设置子部门
		parent.setChildDepartment(childTopDepartment);
		if (childTopDepartment != null) {
			for (DepartmentVo departmentVo : childTopDepartment) {
				this.findChildDepartment(departmentVo, departmentVo.getId(), departmentMapByParentId);
			}
		}
		return parent;
	}

	/**
	 * 根据上级加载下级子部门
	 */
	private List<DepartmentVo> findTopChildDepartment(Long parentId, Map<Long, List<DepartmentVo>> departmentMapByParentId) {
		List<DepartmentVo> list = departmentMapByParentId.get(parentId);
		departmentMapByParentId.remove(parentId);
		return list;
	}

}
