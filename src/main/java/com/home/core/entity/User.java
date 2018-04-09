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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户
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
public class User extends IdEntity {

	public static final String PROP_LOGIN_NAME = "loginName";
	public static final String PROP_NAME = "name";
	public static final String PROP_MOBILE = "mobile";
	public static final String PROP_STATUS = "status";
	public static final String PROP_REGISTER_TIME = "registerTime";

	public static final String STATUS_INACTIVE = "I";
	public static final String STATUS_ACTIVE = "A";
	public static final String STATUS_EXPIRED = "E";
	public static final String STATUS_LOCKED = "L";
	public static final String STATUS_TERMINATED = "T";

	public static final String PICTURE_PATH = "/user";
	public static final String PICTURE_TYPE = "user";
	private String loginName;
	private String password;
	private String salt;
	private String name;
	private String mobile;
	private String status;
	private String email;
	private String avatar;
	private Integer sex;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private String registerIp;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registerTime;
	private String lastLoginIp;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastLoginTime;
	private Long loginCount;

	@Transient
	private String plainPassword;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Role.class)
	@JoinTable(name = "core_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<Role> roles;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = UserGroup.class)
	@JoinTable(name = "core_membership", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private List<UserGroup> groups;

	public User(Long id) {
		this.id = id;
	}

	@Transient
	public List<String> getRoleCodes() {
		List<String> roleCodes = new ArrayList<>();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				roleCodes.add(role.getCode());
			}
		}
		return roleCodes;
	}

	@Transient
	public boolean isEnabled() {
		return STATUS_ACTIVE.equals(status);
	}

	@Transient
	public boolean isAccountInactived() {
		return STATUS_INACTIVE.equals(status);
	}

	@Transient
	public boolean isAccountExpired() {
		return STATUS_EXPIRED.equals(status);
	}

	@Transient
	public boolean isAccountLocked() {
		return STATUS_LOCKED.equals(status);
	}

	@Transient
	public boolean isAccountTerminated() {
		return STATUS_TERMINATED.equals(status);
	}

}
