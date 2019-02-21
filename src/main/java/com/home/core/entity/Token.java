package com.home.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.home.common.entity.UuIdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Token令牌
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
@Table(name = "core_token")
public class Token extends UuIdEntity {

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expireTime;
	private String terminal;
	private String remark;
	private Integer status; // 0、有效 1、失效
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static final int STATUS_ENABLE = 0;
	public static final int STATUS_DISABLE = 1;

	public static final int DEFAULT_EXPIRE = 30 * 24;// 30天
	public static final int DEFAULT_RENEW = 5 * 24;// 5天

	@Transient
	public boolean isEnabled() {
		return STATUS_ENABLE == status && expireTime.compareTo(new Date()) > 0;
	}

	@Transient
	public boolean renew(Date date) {
		return STATUS_ENABLE == status && expireTime.compareTo(date) < 0;
	}

}
