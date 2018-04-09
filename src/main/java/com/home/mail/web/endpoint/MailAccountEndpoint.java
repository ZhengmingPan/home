package com.home.mail.web.endpoint;

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
import com.home.mail.entity.MailAccount;
import com.home.mail.entity.MailProtocol;
import com.home.mail.service.MailAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "邮箱用户接口")
@RestController
@RequestMapping(value="/api/mailAccount")
public class MailAccountEndpoint {

	@Autowired
	private MailAccountService mailAccountService;
	
	@ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<MailAccount>> page(PageQuery pageQuery, Long protocolId) {
		Page<MailAccount> page = mailAccountService.page(protocolId, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}
	
	@ApiOperation(value="获取主账户详情", httpMethod = "GET", produces = "application/json")
	@GetMapping("getOfSysten")
	public ResponseResult<MailAccount> getOfSysten() {
		return ResponseResult.createSuccess(mailAccountService.getOfSystem());
	}
	
	@ApiOperation(value="获取邮箱账户详情", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<MailAccount> get(Long id) {
		return ResponseResult.createSuccess(mailAccountService.get(id));
	}
	  
	@ApiOperation(value = "判断账号是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existName")
	public ResponseResult<Boolean> existUsername(Long id, String username, Long protocolId) {
		return ResponseResult.createSuccess(mailAccountService.existUsername(id, username, protocolId));
	}
	
	@ApiOperation(value = "保存用户编辑", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<MailAccount> save(@ModelAttribute("preloadMailAccount")MailAccount mailAccount, Long protocolId) {
		mailAccount.setProtocol(new MailProtocol(protocolId));
		mailAccountService.save(mailAccount);
		return ResponseResult.createSuccess(mailAccount);
	}
	
	@ApiOperation(value = "重置邮箱密码", httpMethod = "POST", produces = "application/json")
	@PostMapping("entryPassword")
	public ResponseResult<?> entryPassword(Long id, String plainPassword) {
		mailAccountService.entryPassword(id, plainPassword);
		return ResponseResult.SUCCEED;
	}
	
	@ApiOperation(value = "停用用户", httpMethod = "POST", produces = "application/json")
	@PostMapping("stop")
	public ResponseResult<?> stop(Long id) {
		mailAccountService.stop(id);
		return ResponseResult.SUCCEED;
	}
	
	@ApiOperation(value = "启用用户", httpMethod = "POST", produces = "application/json")
	@PostMapping("start")
	public ResponseResult<?> start(Long id) {
		mailAccountService.start(id);
		return ResponseResult.SUCCEED;
	}
	
	@ModelAttribute("preloadMailAccount")
	public MailAccount getMailAccount(@RequestParam(value="id", required = false)Long id) {
		if(id != null) {
			return mailAccountService.get(id);
		}
		return new MailAccount();
	}
 
}
