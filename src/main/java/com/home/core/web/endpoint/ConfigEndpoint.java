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
import com.home.common.http.ResponseResult;
import com.home.core.entity.Config;
import com.home.core.service.ConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "配置项接口")
@RestController
@RequestMapping(value = "/api/config")
public class ConfigEndpoint {

	@Autowired
	private ConfigService configService;

	@ApiOperation(value = "配置项分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<Config>> page(PageQuery pageQuery) {
		Page<Config> page = configService.page(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "配置项保存", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<Config> save(@ModelAttribute("preloadConfig") Config config) {
		config = configService.save(config);
		return ResponseResult.createSuccess(config);
	}

	@ApiOperation(value = "验证键是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existCode")
	public ResponseResult<?> existCode(Long id, String code) {
		return ResponseResult.createSuccess(configService.existCode(id, code));
	}

	@ApiOperation(value = "获取配置项信息", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<Config> get(Long id) {
		Config config = configService.get(id);
		return ResponseResult.createSuccess(config);
	}

	@ApiOperation(value = "配置项删除", httpMethod = "POST", produces = "application/json")
	@PostMapping("delete")
	public ResponseResult<?> delete(Long id) {
		configService.delete(id);
		return ResponseResult.SUCCEED;
	}

	@ModelAttribute("preloadConfig")
	public Config getConfig(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return configService.get(id);
		}
		return new Config();

	}

}
