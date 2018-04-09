package com.home.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据字典类型
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "core_data_type")
public class DataType extends IdEntity {

	public static final String PROP_NAME = "name";
	public static final String PROP_CODE = "code";

	private String code;
	private String name;
	private Integer grade;
	private Boolean editable;
	private String remark;

}
