package com.home.core.web.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.ResponseResult;
import com.home.core.entity.Token;
import com.home.core.service.TokenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Token令牌接口")
@RestController
@RequestMapping(value = "/api/token")
public class TokenEndpoint {

	@Autowired
	private TokenService tokenService;

	@ApiOperation(value = "检查token是否有效", httpMethod = "GET", produces = "application/json")
	@GetMapping(value = "/validate/{token}")
	public ResponseResult<?> validateToken(@PathVariable("token") String id, HttpServletRequest request) {
		Token token = tokenService.get(id);
		if (token != null && token.isEnabled() && token.getUser().isEnabled()) {
			return ResponseResult.SUCCEED;
		}
		return ResponseResult.createError(HttpStatus.UNAUTHORIZED, "由于您长时间未登录 请重新登录。");
	}

}
