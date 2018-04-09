package com.home.core.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户组
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
@Table(name = "core_user_group")
public class UserGroup extends IdEntity {

	public static final Integer TYPE_COMPANY = 0;
	public static final Integer TYPE_DEPARTMENT = 1;
	public static final Integer TYPE_GROUP = 2;
	public static final Integer TYPE_OTHER = 3;

	private String code;
	private String name;
	private Integer type; // 类型(0:公司,1:部门,2:小组,3:其他)
	private Integer grade;
	private Long director;
	private String path;
	private Long parentId;
	private String remark;
	private Boolean delFlag;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Role.class)
	@JoinTable(name = "core_group_role", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<Role> roles;

	@Transient
	private Boolean isParent;

}
