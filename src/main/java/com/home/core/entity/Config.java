package com.home.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 配置项
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
@Table(name = "core_config")
public class Config extends IdEntity {

	public final static String PROP_CODE = "code";
	public final static String PROP_NAME = "remark";

	public final static Integer TYPE_INPUT = 0;
	public final static Integer TYPE_TEXT = 1;
	public final static Integer TYPE_RADIO = 2;
	public final static Integer TYPE_CHECKBOX = 3;
	public final static Integer TYPE_SELECT = 4;
	public final static Integer TYPE_MULTISELECT = 5;

	private String code;
	private String value;
	private String name;
	private Integer type; // 类型(0:Input输入框,1:Textarea文本框,2:Radio单选框,3:Checkbox多选框,4:Select单选框,5:Select多选框)
	private String params;
	private Boolean editable;
	private String remark;

	@Transient
	public String getTypeName() {
		if(type == null) {
			return "其他";
		}
		switch (type) {
		case 0:
			return "Input输入框";
		case 1:
			return "Textarea文本框";
		case 2:
			return "Radio单选框";
		case 3:
			return "Checkbox多选框";
		case 4:
			return "Select单选框";
		case 5:
			return "Select多选框";
		default:
			return "其他";
		}
	}

}
