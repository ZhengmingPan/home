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
import com.home.core.entity.DataType;
import com.home.core.service.DataTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "数据类型接口")
@RestController
@RequestMapping(value = "/api/dataType")
public class DataTypeEndpoint {

	@Autowired
	private DataTypeService dataTypeService;

	@ApiOperation(value = "数据类型分页列表", httpMethod = "GET", produces = "application/json")
	@GetMapping("page")
	public ResponseResult<Page<DataType>> page(PageQuery pageQuery) {
		Page<DataType> page = dataTypeService.page(pageQuery.getSearchKey(), pageQuery.buildPageRequest());
		return ResponseResult.createSuccess(page);
	}

	@ApiOperation(value = "数据类型保存", httpMethod = "POST", produces = "application/json")
	@PostMapping("save")
	public ResponseResult<DataType> save(@ModelAttribute("preloadDataType") DataType dataType) {
		dataType = dataTypeService.save(dataType);
		return ResponseResult.createSuccess(dataType);
	}

	@ApiOperation(value = "验证编号是否存在", httpMethod = "GET", produces = "application/json")
	@GetMapping("existCode")
	public ResponseResult<?> existCode(Long id, String code) {
		return ResponseResult.createSuccess(dataTypeService.existCode(id, code));
	}

	@ApiOperation(value = "获取数据类型信息", httpMethod = "GET", produces = "application/json")
	@GetMapping("get")
	public ResponseResult<DataType> get(Long id) {
		DataType dataType = dataTypeService.get(id);
		return ResponseResult.createSuccess(dataType);
	}

	@ApiOperation(value = "数据类型删除", httpMethod = "POST", produces = "application/json")
	@PostMapping("delete")
	public ResponseResult<?> delete(Long id) {
		dataTypeService.delete(id);
		return ResponseResult.SUCCEED;
	}

	@ModelAttribute("preloadDataType")
	public DataType getDataType(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return dataTypeService.get(id);
		}
		return new DataType();

	}

}
