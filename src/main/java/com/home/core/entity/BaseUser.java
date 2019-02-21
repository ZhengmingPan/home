package com.home.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户基础信息
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
@Table(name = "core_user")
public class BaseUser extends IdEntity {

	public final static String PROP_LOGIN_NAME = "loginName";
	public final static String PROP_NAME = "name";
	public final static String PROP_ROLES = "roles";
	public final static String PROP_GROUPS = "groups";
	public final static String PROP_STATUS = "status";

	private String loginName;
	private String name;
	private String mobile;
	private String email;
	private String avatar;
	private Integer sex;
	private Date birthday;
	private String status;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Role.class)
	@JoinTable(name = "core_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<Role> roles = Lists.newArrayList();

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = UserGroup.class)
	@JoinTable(name = "core_membership", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private List<UserGroup> groups = Lists.newArrayList();

	@Transient
	public List<Long> getRoleIds() {
		List<Long> roleIds = new ArrayList<>();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
		}
		return roleIds;
	}

	@Transient
	public List<Long> getGroupIds() {
		List<Long> groupIds = new ArrayList<>();
		if (groups != null && !groups.isEmpty()) {
			for (UserGroup group : groups) {
				groupIds.add(group.getId());
			}
		}
		return groupIds;
	}

}
