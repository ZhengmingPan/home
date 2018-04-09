package com.home.mail.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.PageQuery;
import com.home.common.entity.ResponseResult;
import com.home.mail.entity.MailAccount;
import com.home.mail.entity.MailMessage;
import com.home.mail.service.MailAccountService;
import com.home.mail.service.MailMessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "邮件信息接口")
@RestController
@RequestMapping(value = "/api/mailMessage")
public class MailMessageEndpoint {

	@Autowired
	private MailMessageService mailMessageService;
	@Autowired
	private MailAccountService mailAccountService;

	@ApiOperation(value = "使用pop3导入所有邮件", httpMethod = "GET", produces = "application/json")
	@PostMapping("importMessages")
	public ResponseResult<?> importMessages(Long accountId, String plainPassword) {
		if (accountId == null) {
			MailAccount systemAccount = mailAccountService.getOfSystem();
			accountId = systemAccount.getId();
		}
		if (mailAccountService.isSamePassword(accountId, plainPassword)) {
			mailMessageService.receiveOnPop3(accountId, plainPassword);
			return ResponseResult.SUCCEED;
		}
		return ResponseResult.createError(HttpStatus.BAD_REQUEST, "密码验证失败");
	}

	@ApiOperation(value = "邮件信息分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<MailMessage>> page(PageQuery pageQuery, Long accountId) {
		if (accountId == null) {
			MailAccount systemAccount = mailAccountService.getOfSystem();
			accountId = systemAccount.getId();
		}
		Page<MailMessage> page = mailMessageService.page(accountId, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "获取邮件详情", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<MailMessage> get(String id) {
		return ResponseResult.createSuccess(mailMessageService.get(id));
	}

	@ApiOperation(value = "标记邮件", httpMethod = "POST", produces = "application/json")
	@PostMapping("signFlag")
	public ResponseResult<?> signFlag(String id, Integer flag) {
		mailMessageService.signFlag(id, flag);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "移除标记", httpMethod = "POST", produces = "application/json")
	@PostMapping("removeFlag")
	public ResponseResult<?> removeFlag(String id, Integer flag) {
		mailMessageService.removeFlag(id, flag);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "邮件删除", httpMethod = "POST", produces = "application/json")
	@PostMapping("delete")
	public ResponseResult<?> detete(String id) {
		mailMessageService.flagDelete(id);
		return ResponseResult.SUCCEED;
	}
}
