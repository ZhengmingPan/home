package com.home.mail.web.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.PageQuery;
import com.home.common.http.ResponseResult;
import com.home.mail.entity.MailProtocol;
import com.home.mail.service.MailProtocolService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "邮箱协议接口")
@RestController
@RequestMapping(value="/api/mailProtocol")
public class MailProtocolEndpoint {
	
	@Autowired
	private MailProtocolService mailProtocolService;
	
	@ApiOperation(value = "协议分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<MailProtocol>> page(PageQuery pageQuery) {
		Page<MailProtocol> page = mailProtocolService.page(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}
	
	@ApiOperation(value="获取协议信息详情", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<MailProtocol> get(Long id) {
		return ResponseResult.createSuccess(mailProtocolService.get(id));
	}
	 
	@ApiOperation(value = "获取协议列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("list")
	public ResponseResult<List<MailProtocol>> list() {
		List<MailProtocol> protocols = mailProtocolService.listOfTrue();
		return ResponseResult.createSuccess(protocols);
	}
	
	@ApiOperation(value = "判断名称是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existName")
	public ResponseResult<Boolean> existName(Long id, String name) {
		return ResponseResult.createSuccess(mailProtocolService.existName(id, name));
	}
	
	@ApiOperation(value = "保存协议编辑", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<MailProtocol> save(@ModelAttribute("preloadMailProtocol")MailProtocol mailProtocol) {
		mailProtocolService.save(mailProtocol);
		return ResponseResult.createSuccess(mailProtocol);
	}
	
	@ApiOperation(value = "停用协议", httpMethod = "POST", produces = "application/json")
	@PostMapping("stop")
	public ResponseResult<?> stop(Long id) {
		mailProtocolService.stop(id);
		return ResponseResult.SUCCEED;
	}
	
	@ApiOperation(value = "启用协议", httpMethod = "POST", produces = "application/json")
	@PostMapping("start")
	public ResponseResult<?> start(Long id) {
		mailProtocolService.start(id);
		return ResponseResult.SUCCEED;
	}
	
	@ModelAttribute("preloadMailProtocol")
	public MailProtocol getMailProtocol(@RequestParam(value="id", required = false)Long id) {
		if(id != null) {
			return mailProtocolService.get(id);
		}
		return new MailProtocol();
	}
	
}
