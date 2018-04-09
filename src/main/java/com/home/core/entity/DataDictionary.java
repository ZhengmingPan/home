package com.home.core.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据字典
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
@Table(name = "core_data_dictionary")
public class DataDictionary extends IdEntity {

	@ManyToOne
	@JoinColumn(name = "type_id")
	private DataType dataType;
	private String code;
	private String name;
	private Integer priority;
	private Integer grade;
	private Long parentId;
	private String remark;

}
