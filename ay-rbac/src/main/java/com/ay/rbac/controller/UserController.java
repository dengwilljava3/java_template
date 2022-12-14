package com.ay.rbac.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ay.common.constants.Dictionary;
import com.ay.common.controller.base.BaseController;
import com.ay.common.util.EncUtil;
import com.ay.common.util.StringUtil;
import com.ay.common.util.httpclient.HttpClientUtil;
import com.ay.rbac.entity.Client;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Menu;
import com.ay.rbac.entity.Role;
import com.ay.rbac.entity.User;
import com.ay.rbac.service.ClientService;
import com.ay.rbac.service.DepartmentService;
import com.ay.rbac.service.MenuService;
import com.ay.rbac.service.RoleService;
import com.ay.rbac.service.UserService;
import com.ay.rbac.vo.ClientVo;
import com.ay.rbac.vo.UserVo;
import com.ay.session.mysql.entity.Session;
import com.ay.session.mysql.service.SessionService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class UserController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private MenuService menuService;

	@Value("${domain.check:false}")
	private String domainCheck;
	@Value("${domain.url:http://oa.nyjt88.com/sys/user-ldap.html?mode=api}")
	private String domainUrl;

	@ApiOperation(value = "????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username:xx, password:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/loginNoValid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> login(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("login param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			String password = parseObject.getString("password");
			if (StringUtil.isNull(username) || StringUtil.isNull(password)) {
				return result(PARAM_IS_NULL, "?????????????????????!");
			}
			User user = this.userService.queryByUsername(username);
			if (user == null) {
				return result(ERROR, "?????????????????????!");
			}
			if ("true".equals(domainCheck) && !"admin".equals(username)) {
				Map<String, Object> params = new HashMap<>();
				params.put("account", username);
				params.put("password", password);
				Map<String, String> headerMap = new HashMap<>();
				headerMap.put("Content-Type", "application/x-www-form-urlencoded");
				String result = HttpClientUtil.sendPost(domainUrl, params, headerMap);
				logger.info("??????????????????{}", result);
				if (StringUtil.isNull(result)) {
					return result(ERROR, "?????????????????????!");
				}
				parseObject = JSONObject.parseObject(result);
				if (!"0".equals(parseObject.getString("code"))) {
					return result(ERROR, "??????????????????!");
				}
			} else {
				user = this.userService.login(username, EncUtil.toMD5(password));
				if (user == null) {
					return result(ERROR, "????????????????????????!");
				}
				if (user.getEnable().intValue() == Dictionary.STATUS.DISABLE) {
					return result(USER_DISABLE, "????????????????????????,??????????????????!");
				}
			}
			Session session = new Session();
			session.setUsername(username);
//			this.departmentService.query
			session = this.sessionService.saveSession(session);
			Client client = this.clientService.selectByUsername(username);
			ClientVo vo = new ClientVo();
			BeanUtils.copyProperties(client, vo);
			vo.setUser(user);
			vo.setSessionId(session.getSessionId());
			return result(SUCCESS, vo);
		} catch (Exception e) {
			logger.error("login ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "??????????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getLoginUserNoValid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getLoginUser(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getLoginUser param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			if (StringUtil.isNull(username)) {
				return result(PARAM_IS_NULL, "username is null!");
			}
			User user = new User();
			user.setUsername(username);
			List<User> userList = this.userService.selectByCondition(user);
			if (userList == null || userList.size() <= 0) {
				return result(USERNAME_IS_NOT_EXIST, "user is not exist!");
			}
			user = userList.get(0);
			List<Department> departmentList = this.departmentService.selectByUsername(username);
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(user, vo);
			Map<Long, Department> topDepartment = new HashMap<>();
			for (Department department : departmentList) {
				Department departmentTop = this.departmentService.getTopDepartmentById(department.getId());
				if (departmentTop == null) {
					continue;
				}
				topDepartment.put(department.getId(), departmentTop);
			}
			Set<Entry<Long, Department>> entrySet = topDepartment.entrySet();
			List<Department> topDepartments = new ArrayList<>();
			for (Entry<Long, Department> entry : entrySet) {
				topDepartments.add(entry.getValue());
			}
			vo.setTopDepartments(topDepartments);
			vo.setDepartments(departmentList);
			List<Role> roleList = this.roleService.selectByUsername(username);
			vo.setRoles(roleList);
			return result(SUCCESS, vo);
		} catch (Exception e) {
			logger.error("getLoginUser ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "??????????????????????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{clientId:xx, user:{id:id(???????????? ????????????),username:'??????',password:'??????',name:'??????',tel:'??????',email:'??????', enable:(0??????,1??????)}, autoGrab:(0:??????, 1:??????)}, roleIds:[??????id???], departmentIds:[??????id???]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> saveUser(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("saveUser param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String clientId = parseObject.getString("clientId");
			if (StringUtil.isNull(clientId)) {
				return result(CLIENT_ID_NOT_EXIST, "clientId is null!");
			}
			User user = JSONObject.parseObject(parseObject.getString("user"), User.class);
			user.setPassword(EncUtil.toMD5(user.getPassword()));
			List<Long> roleIds = parseObject.getJSONArray("roleIds").toJavaList(Long.class);
			List<Long> departmentIds = parseObject.getJSONArray("departmentIds").toJavaList(Long.class);
			this.userService.save(clientId, user, roleIds, departmentIds);
			return result(SUCCESS, OK);
		} catch (DuplicateKeyException unique) {
			return result(USERNAME_EXIST, "username is exists, please update or set other username!");
		} catch (Exception e) {
			logger.error("saveUser ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "??????????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{userIds:[userId??????]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/delUserByUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> delUserByUserId(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("delUserByUserId param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			List<Long> userIds = parseObject.getJSONArray("userIds").toJavaList(Long.class);
			if (userIds == null || userIds.size() <= 0) {
				return result(PARAM_IS_NULL, "userId is null!");
			}
			this.userService.deleteByIds(userIds);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("delUserByUserId ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id:userId, oldPassword:xx, newPassword:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> updatePassword(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("updatePassword param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Long userId = parseObject.getLong("id");
			if (userId == null) {
				return result(PARAM_IS_NULL, "userId is null");
			}
			String newPassword = parseObject.getString("newPassword");
			String oldPassword = parseObject.getString("oldPassword");
			if (StringUtil.isNull(newPassword)) {
				return result(PARAM_IS_NULL, "new password is null!");
			}
			if (StringUtil.isNull(oldPassword)) {
				return result(PARAM_IS_NULL, "old password is null!");
			}
			User user = this.userService.selectById(userId);
			if (user == null) {
				return result(USERNAME_IS_NOT_EXIST, "user don't exist!");
			}
			if (!user.getPassword().equalsIgnoreCase(oldPassword)) {
				return result(ERROR, "old password is error, please reEnter again!");
			}
			this.userService.updatePassword(userId, newPassword);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("updatePassword ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id:userId}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> resetPassword(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("resetPassword param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Long userId = parseObject.getLong("id");
			if (userId == null) {
				return result(PARAM_IS_NULL, "user id is null!");
			}
			User user = this.userService.selectById(userId);
			if (user == null) {
				return result(USERNAME_IS_NOT_EXIST, "user don't exist!");
			}
			this.userService.resetPassword(userId);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("resetPassword ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username:?????????,name:??????,pageNum:??????,pageSize:?????????}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/queryUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> queryUser(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("queryUser param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			String name = parseObject.getString("name");
			Integer pageNo = parseObject.getInteger("pageNum");
			Integer pageSize = parseObject.getInteger("pageSize");
			User user = new User();
			user.setUsername(username);
			user.setName(name);
			PageInfo<UserVo> page = this.userService.queryUser(user, pageNo, pageSize);
			return result(SUCCESS, page);
		} catch (Exception e) {
			logger.error("queryUser ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "????????????????????????????????????????????????????????????????????????,?????????????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{'siteId':'proxy_1','username':'admin',pageNum:1,pageSize:1,'timestamp':'20171206','encrypt':'r5+hexSha1(clientId + timestamp + encryptKey)+r6'}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/queryDepartmentUsers", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> queryDepartmentUser(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			Integer pageNum = parseObject.getInteger("pageNum");
			Integer pageSize = parseObject.getInteger("pageSize");

			// ????????????????????????????????????id????????????ids
			Set<Long> idSet = this.departmentService.queryUserChildDepartmentIds(username);

			// ??????????????????????????????????????????????????????????????????
			PageInfo<UserVo> page = this.userService.queryDepartmentUsers(idSet, username, pageNum, pageSize);

			return result(SUCCESS, page);
		} catch (Exception e) {
			logger.error("getDepartmentByParentId ?????? : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "??????????????????ids??????????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{'clientId':'proxy_1','departmentIds':[1,2],pageNum:1,pageSize:1,'timestamp':'20171206','encrypt':'r5+hexSha1(clientId + timestamp + encryptKey)+r6'}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/queryDepartmentUsersByIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> queryUsersByDepartmentId(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("queryUsersByDepartmentIds param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			Integer pageNum = parseObject.getInteger("pageNum");
			Integer pageSize = parseObject.getInteger("pageSize");

			List<Long> departmentIds = parseObject.getJSONArray("departmentIds").toJavaList(Long.class);
			if (departmentIds.size() == 0) {
				throw new Exception("departmentIds size is zero!");
			}
			Set<Long> idSet = new HashSet<Long>(departmentIds);
			// ????????????id????????????ids
			Set<Long> childSet = this.departmentService.queryDepartmentChildIds(idSet);

			// ??????ids
			idSet.addAll(childSet);

			// ??????????????????????????????????????????????????????????????????
			PageInfo<UserVo> page = this.userService.queryDepartmentUsers(idSet, null, pageNum, pageSize);

			return result(SUCCESS, page);
		} catch (Exception e) {
			logger.error("queryUsersByDepartmentIds ?????? : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/logoffNoValid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> logoff(HttpServletRequest request) {
		try {
			String param = request.getAttribute("param") + "";
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			if (StringUtil.isNull(username)) {
				return result(PARAM_IS_NULL, "username or password is null!");
			}
			Session session = new Session();
			session.setUsername(username);
			this.sessionService.deleteSession(session);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("logoff ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "???????????????")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/allPrivileges", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> allPrivileges(HttpServletRequest request) {
		try {
			Session session = (Session) request.getAttribute("session");
			List<Menu> hasMenuList = this.menuService.selectByUsername(session.getUsername());
			Set<Long> hasMenuId = new HashSet<>();
			hasMenuList.parallelStream().forEach(e -> {
				hasMenuId.add(e.getId());
			});
			Menu menu = new Menu();
			menu.setLevel((byte) 2);
			List<Menu> allMenuList = this.menuService.selectByCondition(menu);
			Set<Long> allMenuId = new HashSet<>();
			allMenuList.parallelStream().forEach(e -> {
				allMenuId.add(e.getId());
			});
			allMenuId.removeAll(hasMenuId);
			if (allMenuId != null && allMenuId.size() > 0) {
				List<Role> roleList = this.roleService.selectByUsername(session.getUsername());
				Role role = roleList.get(0);
				this.roleService.addRoleMenus(role.getId(), allMenuId);
			}
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("allPrivileges ?????? : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

}
