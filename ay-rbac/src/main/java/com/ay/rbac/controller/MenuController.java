package com.ay.rbac.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ay.common.controller.base.BaseController;
import com.ay.common.util.StringUtil;
import com.ay.rbac.entity.Menu;
import com.ay.rbac.entity.Role;
import com.ay.rbac.service.MenuService;
import com.ay.rbac.service.RoleService;
import com.ay.rbac.vo.MenuVo;
import com.ay.session.mysql.entity.Session;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class MenuController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuService menuService;

	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "根据角色获取功能")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{roleIds:[roleId1,roleId2...]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuFunctionByRoleIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getMenuFunctionByRoleIds(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuFunctionByRoleIds param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			List<Long> roleIdList = parseObject.getJSONArray("roleIds").toJavaList(Long.class);
			if (roleIdList == null || roleIdList.size() <= 0) {
				return result(PARAM_IS_NULL, "roleId is null!");
			}
			List<Menu> menuList = this.menuService.selectByRoleIds(roleIdList.toArray(new Long[roleIdList.size()]));
			return result(SUCCESS, menuList);
		} catch (Exception e) {
			logger.error("getMenuFunctionByRoleIds 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "根据角色获取二级菜单")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{roleIds:[roleId1,roleId2...]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuFileByRoleIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getMenuFileByRoleIds(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuFileByRoleIds param = {}", param);
		try {
			Map<String, Object> resultMap = this.getMenuFunctionByRoleIds(request);
			if (SUCCESS.equals(resultMap.get(STATUS))) {
				List<Menu> funcMenuList = (List<Menu>) resultMap.get(MESSAGE);
				List<Menu> parentMenu = this.menuService.selectParentByChildren(funcMenuList);
				return result(SUCCESS, parentMenu);
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("getMenuFileByRoleIds 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "根据角色获取菜单目录")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{roleIds:[roleId1,roleId2...]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuDirByRoleIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getMenuDirByRoleIds(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuDirByRoleIds param = {}", param);
		try {
			Map<String, Object> resultMap = this.getMenuFileByRoleIds(request);
			if (SUCCESS.equals(resultMap.get(STATUS))) {
				List<Menu> fileMenu = (List<Menu>) resultMap.get(MESSAGE);
				List<Menu> parentMenu = this.menuService.selectParentByChildren(fileMenu);
				return result(SUCCESS, parentMenu);
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("getMenuDirByRoleIds 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "根据菜单目录")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{level:(0,1,2)}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getAllMenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getAllMenu(HttpServletRequest request) {
		try {
			String param = request.getAttribute("param") + "";
			Menu menu = JSONObject.parseObject(param, Menu.class);
			List<Menu> allMenu = this.menuService.selectByCondition(menu);
			return result(SUCCESS, allMenu);
		} catch (Exception e) {
			logger.info("getAllMenu 出错 : ", e);
			return result(MAYBE, "network is error!");
		}
	}

	@ApiOperation(value = "根据角色获取权限菜单")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{roleIds:[roleId1,roleId2...]}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuByRoleIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> getMenuByRoleIds(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuDirByRoleIds param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			JSONArray roleArr = parseObject.getJSONArray("roleIds");
			List<Long> roleIdList = roleArr.toJavaList(Long.class);
			List<Menu> menuFunctionList = this.menuService.selectByRoleIds(roleIdList.toArray(new Long[roleIdList.size()]));
			if (menuFunctionList == null || menuFunctionList.size() <= 0) {
				return result(SUCCESS, menuFunctionList);
			}
			List<Menu> allMenu = this.menuService.selectByCondition(null);
			List<MenuVo> dirMenuList = getLevelMenu(menuFunctionList, allMenu);
			return result(SUCCESS, dirMenuList);
		} catch (Exception e) {
			logger.info("获取菜单目录出错 : ", e);
			return result(MAYBE, e.getMessage());
		}
	}

	@ApiOperation(value = "根据用户加载所有菜单功能")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuByUsername", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getMenuByUsername(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuByUsername param = {}", param);
		try {
			Session session = (Session) request.getAttribute("session");
			List<Role> roleList = this.roleService.selectByUsername(session.getUsername());
			List<Long> roleIdList = new ArrayList<>();
			roleList.forEach(e -> {
				roleIdList.add(e.getId());
			});
			List<Menu> menuFunctionList = this.menuService.selectByRoleIds(roleIdList.toArray(new Long[roleIdList.size()]));
			if (menuFunctionList == null || menuFunctionList.size() <= 0) {
				return result(SUCCESS, menuFunctionList);
			}
			List<Menu> allMenu = this.menuService.selectByCondition(null);
			List<MenuVo> dirMenuList = getLevelMenu(menuFunctionList, allMenu);
			return result(SUCCESS, dirMenuList);
		} catch (Exception e) {
			logger.error("getMenuByUsername 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	private List<MenuVo> getLevelMenu(List<Menu> menuFunctionList, List<Menu> allMenu) {
		// 1. 一级菜单
		Map<Long, Menu> menuIdMap = new HashMap<>();
		for (Menu menu : allMenu) {
			menuIdMap.put(menu.getId(), menu);
		}
		List<MenuVo> fileMenuList = new ArrayList<>();
		Map<Long, MenuVo> fileMenuMap = new HashMap<>();
		// 3级菜单逆推2级菜单
		for (Menu function : menuFunctionList) {
			MenuVo fileVo = fileMenuMap.get(function.getParentId());
			if (fileVo == null) {
				fileVo = new MenuVo();
				Menu file = menuIdMap.get(function.getParentId());
				if (file == null) {
					continue;
				}
				BeanUtils.copyProperties(file, fileVo, "childrenMenu");
				fileMenuMap.put(function.getParentId(), fileVo);
				fileMenuList.add(fileVo);
			}
			List<MenuVo> children = fileVo.getChildrenMenu();
			if (children == null) {
				children = new ArrayList<>();
				fileVo.setChildrenMenu(children);
			}
			MenuVo functionVo = new MenuVo();
			BeanUtils.copyProperties(function, functionVo, "childrenMenu");
			children.add(functionVo);
		}
		// 2级逆推1级
		List<MenuVo> dirMenuList = new ArrayList<>();
		Map<Long, MenuVo> dirMenuMap = new HashMap<>();
		for (MenuVo fileVo : fileMenuList) {
			MenuVo dirVo = dirMenuMap.get(fileVo.getParentId());
			if (dirVo == null) {
				dirVo = new MenuVo();
				Menu dir = menuIdMap.get(fileVo.getParentId());
				BeanUtils.copyProperties(dir, dirVo, "childrenMenu");
				dirMenuMap.put(fileVo.getParentId(), dirVo);
				dirMenuList.add(dirVo);
			}
			List<MenuVo> children = dirVo.getChildrenMenu();
			if (children == null) {
				children = new ArrayList<>();
				dirVo.setChildrenMenu(children);
			}
			children.add(fileVo);
		}
		return dirMenuList;
	}

	@ApiOperation(value = "根据用户加载所有一级菜单")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuFileByTree", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getMenuFileByTree(HttpServletRequest request) {
		try {
			Menu menu = new Menu();
			List<Menu> menuList = this.menuService.selectByCondition(menu);
			if(menuList == null || menuList.size() <= 0) {
				return result(SUCCESS, OK);
			}
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("getMenuFileByTree 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "根据用户加载所有一级菜单")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{username:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/getMenuDirByUsername", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getMenuDirByUsername(HttpServletRequest request) {
		String param = request.getAttribute("param") + "";
		logger.info("getMenuByUsername param = {}", param);
		try {
			JSONObject parseObject = JSONObject.parseObject(param);
			String username = parseObject.getString("username");
			if (StringUtil.isNull(username)) {
				return result(ERROR, "username is null!");
			}
			List<Role> roleList = this.roleService.selectByUsername(username);
			List<Long> roleIdList = new ArrayList<>();
			roleList.forEach(e -> {
				roleIdList.add(e.getId());
			});
			List<Menu> menuFunctionList = this.menuService.selectByRoleIds(roleIdList.toArray(new Long[roleIdList.size()]));
			if (menuFunctionList == null || menuFunctionList.size() <= 0) {
				return result(SUCCESS, menuFunctionList);
			}
			List<Menu> menuFileList = this.menuService.selectParentByChildren(menuFunctionList);
			// {key = parent, value = fileMenus}
			Set<Menu> menuDirs = new HashSet<>();
			for (Menu menu : menuFileList) {
				Menu menuDir = this.menuService.selectParentByChildren(menu.getId()).get(0);
				menuDirs.add(menuDir);
			}
			return result(SUCCESS, menuDirs);
		} catch (Exception e) {
			logger.error("getMenuByUsername 出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "保存菜单")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id:xx, name:xx, memo:xx, url:xx, level:xx, parentId:(为空表示一级菜单, 不为空必须存在的id)}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/saveMenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> saveMenu(HttpServletRequest request) {
		try {
			String param = request.getAttribute("param") + "";
			logger.info("saveMenu param = {}", param);
			Menu menu = JSONObject.parseObject(param, Menu.class);
			this.menuService.save(menu);
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("保存菜单出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

	@ApiOperation(value = "删除菜单根据id")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "param", value = "{id:xx}", dataType = "string", required = true, paramType = "string"), //
	})
	@RequestMapping(value = "/delMenuById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Map<String, Object> delMenuById(HttpServletRequest request) {
		try {
			String param = request.getAttribute("param") + "";
			logger.info("delMenuById param = {}", param);
			JSONObject parseObject = JSONObject.parseObject(param);
			this.menuService.deleteMenuById(parseObject.getLong("id"));
			return result(SUCCESS, OK);
		} catch (Exception e) {
			logger.error("保存菜单出错 : ", e);
			return result(MAYBE, NETWORK_IS_ERROR);
		}
	}

}
