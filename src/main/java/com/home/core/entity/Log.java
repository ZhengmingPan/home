package com.home.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.home.common.entity.UuIdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志
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
@Table(name = "core_log")
public class Log extends UuIdEntity {

	public final static String PROP_DESCRIPTION = "description";
	public final static String PROP_URL = "url";
	public final static String PROP_LOG_TIME = "logTime";

	private String description;
	private String username;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date logTime;
	private Integer spendTime;
	private String ip;
	private String url;
	private String method;
	private String userAgent;
	private String parameter;
	private String result;

}
