package com.home.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.home.common.entity.ShiroUser;
import com.home.core.entity.Permission;
import com.home.core.entity.Role;
import com.home.core.entity.User;
import com.home.core.entity.UserGroup;

public class ShiroAuthRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	// 认证.登录
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		String username = (String) authcToken.getPrincipal();
		User user = null;
		if (StringUtils.isBlank(username)) {
			throw new AccountException("请输入用户名");
		}
		user = userService.getByLoginName(username);
		if (user != null) {
			if (!user.isEnabled()) {
				if (user.isAccountInactived()) {
					throw new DisabledAccountException("账号尚未激活");
				}
				if (user.isAccountExpired()) {
					throw new ExpiredCredentialsException("账号已被停用");
				}
				if (user.isAccountLocked()) {
					throw new LockedAccountException("账号已被停用");
				}
				if (user.isAccountTerminated()) {
					throw new DisabledAccountException("账号已被停用");
				}
			}
			ShiroUser shiroUser = new ShiroUser();
			shiroUser.setId(user.getId());
			shiroUser.setLoginName(username);
			shiroUser.setName(user.getName());

			try {
				if (StringUtils.isNotBlank(user.getSalt())) {
					return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
				} else {
					return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), getName());
				}
			} catch (IncorrectCredentialsException e) {
				 throw new IncorrectCredentialsException("用户名或密码错误");
			}
		} else {
			throw new UnknownAccountException("账号不存在");
		}
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		ShiroUser shiroUser = (ShiroUser) principal.getPrimaryPrincipal();
		User user = userService.get(shiroUser.getId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		List<Role> userRoles = user.getRoles();
		List<UserGroup> userGroups = user.getGroups();
		for (Role role : userRoles) {
			info.addRole(role.getCode());
			for (Permission permission : role.getPermissions()) {
				info.addStringPermission(permission.getCode());
			}
		}
		for (UserGroup group : userGroups) {
			List<Role> groupRole = group.getRoles();
			for (Role role : groupRole) {
				info.addRole(role.getCode());
				for (Permission permission : role.getPermissions()) {
					info.addStringPermission(permission.getCode());
				}
			}
		}
		return info;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TokenService getTokenService() {
		return tokenService;
	}

	public void setTokenService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

}