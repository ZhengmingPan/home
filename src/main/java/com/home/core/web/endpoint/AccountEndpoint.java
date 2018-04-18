package com.home.core.web.endpoint;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.ResponseResult;
import com.home.core.entity.BaseUser;
import com.home.core.service.BaseUserService;
import com.home.core.service.TokenService;
import com.home.core.web.CoreThreadContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "登录接口")
@RestController
@RequestMapping("/api/account")
public class AccountEndpoint { 

	@Autowired
	private BaseUserService baseUserService;
	@Autowired
	private TokenService tokenService;

	@ApiOperation(value = "用户登陆", httpMethod = "POST", produces = "application/json")
	@PostMapping("login")
	public ResponseResult<?> login(String username, String password, String terminal, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(new UsernamePasswordToken(username, password));
		} catch (IncorrectCredentialsException e) {
			throw new IncorrectCredentialsException("用户名或密码错误");
		}
		BaseUser user = baseUserService.getByLoginName(username);
		String token = tokenService.apply(user.getId(), terminal, "");
		response.setHeader("token", token);
		return ResponseResult.createSuccess(user);
	}

	@ApiOperation(value = "退出登录", httpMethod = "POST", produces = "application/json")
	@PostMapping("logout")
	public ResponseResult<?> logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "获取当前用户信息", httpMethod = "GET", produces = "application/json")
	@GetMapping("current")
	public ResponseResult<?> current() {
		Long userId = CoreThreadContext.getUserId();
		BaseUser user = null;
		if (userId != null) {
			user = baseUserService.get(userId);
		}
		return ResponseResult.createSuccess(user);
	}
	
	 @MessageMapping("/welcome") //当浏览器向服务端发送请求时,通过@MessageMapping映射/welcome这个地址,类似于@ResponseMapping
     @SendTo("/topic/getResponse")//当服务器有消息时,会对订阅了@SendTo中的路径的浏览器发送消息
     public ResponseResult say() { 
         return ResponseResult.createError(HttpStatus.ACCEPTED, "websocket");
     }

}
