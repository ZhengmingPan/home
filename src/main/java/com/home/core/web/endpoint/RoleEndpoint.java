package com.home.core.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.PageQuery;
import com.home.common.entity.ResponseResult;
import com.home.core.entity.BaseUser;
import com.home.core.entity.Role;
import com.home.core.service.BaseUserService;
import com.home.core.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "角色接口")
@RestController
@RequestMapping(value = "/api/role")
public class RoleEndpoint {

	@Autowired
	private RoleService roleService;
	@Autowired
	private BaseUserService baseUserService;

	@ApiOperation(value = "获取角色的分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<Role>> page(PageQuery pageQuery) {
		Page<Role> roles = roleService.page(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(roles);
	}

	@ApiOperation(value = "角色授权用户分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("pageUsers")
	public ResponseResult<Page<BaseUser>> pageUsers(Long id, PageQuery pageQuery) {
		Page<BaseUser> page = baseUserService.pageByRole(id, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "分配用戶角色", httpMethod = "POST", produces = "application/json")
	@PostMapping("distributeUser")
	public ResponseResult<?> distributeUser(Long id, Long userId) {
		BaseUser user = baseUserService.get(userId);
		if (!user.getRoleIds().contains(id)) {
			user.getRoles().add(roleService.get(id));
		}
		baseUserService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "移除用戶角色", httpMethod = "POST", produces = "application/json")
	@PostMapping("removeUser")
	public ResponseResult<?> removeUser(Long id, Long userId) {
		BaseUser user = baseUserService.get(userId);
		for (Role role : user.getRoles()) {
			if (role.getId().equals(id)) {
				user.getRoles().remove(role);
				break;
			}
		}
		baseUserService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "角色信息保存", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<Role> page(@ModelAttribute("preloadRole") Role role) {
		role = roleService.save(role);
		return ResponseResult.createSuccess(role);
	}

	@ModelAttribute("preloadRole")
	public Role getRole(@RequestParam(value = "id", required = false) Long id) {
		if (id == null) {
			return new Role();
		}
		return roleService.get(id);
	}

}
