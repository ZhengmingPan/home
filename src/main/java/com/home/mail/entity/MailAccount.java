package com.home.mail.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.shiro.crypto.hash.SimpleHash;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 邮件用户
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
@Table(name = "mail_account")
public class MailAccount extends IdEntity {

	public static final String PROP_USERNAME = "username";
	public static final String PROP_PROTOCOL = "protocol";
	public static final String PROP_TYPE = "type";

	public static final Integer TYPE_SYSTEM = 0; // 系统用户
	public static final Integer TYPE_NORMAL = 1; // 普通用户
	 
	public static final String HASH_ALGORITHM_NAME = "SHA-1";
	public static final Integer HASH_ITERATIONS = 1024;
 
	private String username;
	private String password;
	private String name;
	private String mail;
	private String province;
	private String city;
	private String county;
	private String address;
	private String phone;
	@ManyToOne()
	@JoinColumn(name = "protocol_id")
	private MailProtocol protocol;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private Long creator;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	private Long updator;
	private Boolean delFlag;
	private Integer type;

	@Transient
	private String plainPassword;
	
	/**
	 * 加密
	 */
	@Transient
	public void encryptPassword() {
		StringBuilder salt = new StringBuilder();
		salt.append(username).append(protocol.getSuffix()).toString();
		SimpleHash sh = new SimpleHash(HASH_ALGORITHM_NAME, plainPassword, salt.toString(), HASH_ITERATIONS);
		setPassword(sh.toString());
	}
	
	/**
	 * 密码验证
	 * @return
	 */
	@Transient
	public Boolean isSamePassword(String plainPassword) {
		StringBuilder salt = new StringBuilder();
		salt.append(username).append(protocol.getSuffix()).toString();
		SimpleHash sh = new SimpleHash(HASH_ALGORITHM_NAME, plainPassword, salt.toString(), HASH_ITERATIONS);
		if(sh.toString().equals(password)) {
			return true;
		}
		return false;
	}
}
