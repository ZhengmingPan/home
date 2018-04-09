package com.home.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限
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
@Table(name = "core_permission")
public class Permission extends IdEntity {

	private String code;
	private String name;
	private Integer priority;

}
