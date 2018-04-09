package com.home.core.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "主页页面")
@Controller
public class IndexController {

	@ApiOperation(value = "主页", httpMethod = "GET")
	@GetMapping("/index")
	public String index() {
		return "index";
	} 

	@ApiOperation(value = "登录页面", httpMethod = "GET")
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@ApiOperation(value = "登录系统", notes = "输入用户名和密码", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"), @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String") })
	@PostMapping("/login")
	public String success() { 
		return "index";
	}

	@ApiOperation(value = "登出", httpMethod = "GET")
	@GetMapping("/logout")
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return "login";
	}

}
