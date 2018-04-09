package com.home.core.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
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
@Table(name = "core_role")
public class Role extends IdEntity {

	public static final String PROP_CODE = "code";
	public static final String PROP_NAME = "name";

	private String code;
	private String name;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Permission.class)
	@JoinTable(name = "core_role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	private List<Permission> permissions;

	public Role(Long id) {
		this.id = id;
	}

}
