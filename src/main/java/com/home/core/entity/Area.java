package com.home.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 地区信息
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
@Table(name = "core_area")
public class Area implements Serializable {

	@Id
	private String id;
	private String code;
	private String name;
	private String fullName;
	private Integer grade;
	private Integer type; // 类型(1:省,2:直辖市,3:地级市,4:县级市,5:县,6:区)
	private String rootId;
	private String parentId;

}
