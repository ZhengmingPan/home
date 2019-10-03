package com.home.core.web.endpoint;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.common.entity.PageQuery;
import com.home.common.http.ResponseResult;
import com.home.core.entity.Log;
import com.home.core.service.LogService;

import io.swagger.annotations.Api;

@Api(description = "日志接口")
@RestController
@RequestMapping(value = "/api/log")
public class LogEndpoint {

	@Autowired
	private LogService logService;

	// @ApiOperation(value = "日志分页列表", httpMethod = "GET", produces =
	// "application/json")
	@GetMapping("page")
	public ResponseResult<Page<Log>> page(PageQuery pageQuery, Date startTime, Date endTime) {
		Page<Log> logs = logService.page(startTime, endTime, pageQuery.getSearchKey(), pageQuery.buildPageRequest());
 		return ResponseResult.createSuccess(logs);
	}

	// @ApiOperation(value = "日志删除", httpMethod = "POST", produces =
	// "application/json")
	@PostMapping("delete")
	public ResponseResult<?> delete(String id) {
		logService.delete(id);
		return ResponseResult.SUCCEED;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
