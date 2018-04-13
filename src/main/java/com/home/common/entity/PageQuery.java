package com.home.common.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@ApiModel(description = "分页查询")
public class PageQuery implements Serializable {

	@ApiModelProperty(value = "页码", required = true)
	private Integer pageNo;
	@ApiModelProperty(value = "页长", required = true)
	private Integer pageSize;
	@ApiModelProperty(value = "排序列", required = true)
	private String sortBy;
	@ApiModelProperty(value = "排序规则 desc\\asc", required = true)
	private String sortDir;
	@ApiModelProperty(value = "关键字")
	private String searchKey;

	public PageRequest buildPageRequest() {
		return buildPageRequest(1, 10);
	}

	public PageRequest buildPageRequest(int pageNo, int pageSize) {
		return buildPageRequest(pageNo, pageSize, null);
	}

	public PageRequest buildPageRequest(Sort sort) {
		return buildPageRequest(1, 10, sort);
	}

	public PageRequest buildPageRequest(int pageNo, int pageSize, Sort defaultSort) {
		int page = this.pageNo != null ? this.pageNo - 1 : pageNo - 1;
		int size = this.pageSize != null ? this.pageSize : pageSize;

		if (StringUtils.isEmpty(sortBy)) {
			return new PageRequest(page, size, defaultSort);
		} else {
			Sort.Direction direction = StringUtils.isNotEmpty(sortDir) ? Sort.Direction.fromString(sortDir) : Sort.Direction.DESC;
			String[] properties = StringUtils.split(sortBy, ",");
			return new PageRequest(page, size, direction, properties);
		}
	}

	public boolean hasSort() {
		return StringUtils.isNotEmpty(sortBy);
	}

}
