package com.home.core.web.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.home.common.entity.PageQuery;
import com.home.common.http.ResponseResult;
import com.home.core.entity.BaseUser;
import com.home.core.entity.UserGroup;
import com.home.core.service.BaseUserService;
import com.home.core.service.UserGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户组接口")
@RestController
@RequestMapping(value = "/api/userGroup")
public class UserGroupEndpoint {

	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private BaseUserService baseUserService;

	@ApiOperation(value = "根据上一级部门id获取组织列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("getByParentId")
	public ResponseResult<List<UserGroup>> getByParentId(Long parentId) {
		List<UserGroup> groups = Lists.newArrayList();
		if (parentId == null) {
			groups = userGroupService.listRootGroups();
		} else {
			groups = userGroupService.listByParentId(parentId);
		}
		for (UserGroup group : groups) {
			List<UserGroup> children = userGroupService.listByParentId(group.getId());
			group.setIsParent(children != null && children.size() > 0);
		}
		return ResponseResult.createSuccess(groups);
	}

	@ApiOperation(value = "角色授权用户分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("pageUsers")
	public ResponseResult<Page<BaseUser>> pageUsers(Long id, PageQuery pageQuery) {
		Page<BaseUser> page = null;
		if (id != null) {
			page = baseUserService.pageByGroup(id, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		}
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "查询组织编号是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existCode")
	public ResponseResult<Boolean> existCode(Long id, String code) {
		return ResponseResult.createSuccess(userGroupService.existCode(id, code));
	}

	@ApiOperation(value = "保存用户组信息", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<UserGroup> save(@ModelAttribute("preloadUserGroup") UserGroup userGroup) {
		UserGroup group = userGroupService.save(userGroup);
		return ResponseResult.createSuccess(group);
	}

	@ApiOperation(value = "分配用戶组", httpMethod = "POST", produces = "application/json")
	@PostMapping("distributeUser")
	public ResponseResult<?> distributeUser(Long id, Long userId) {
		BaseUser user = baseUserService.get(userId);
		if (!user.getGroupIds().contains(id)) {
			user.getGroups().add(userGroupService.get(id));
		}
		baseUserService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "移除用戶", httpMethod = "POST", produces = "application/json")
	@PostMapping("removeUser")
	public ResponseResult<?> removeUser(Long id, Long userId) {
		BaseUser user = baseUserService.get(userId);
		for (UserGroup group : user.getGroups()) {
			if (group.getId().equals(id)) {
				user.getGroups().remove(group);
				break;
			}
		}
		baseUserService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ModelAttribute("preloadUserGroup")
	public UserGroup getUserGroup(@RequestParam(value = "id", required = false) Long id) {
		if (id == null) {
			return new UserGroup();
		}
		return userGroupService.get(id);
	}

}
