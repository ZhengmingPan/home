package com.home.mail.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.home.common.entity.IdEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 邮件协议等信息
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
@Table(name = "mail_protocol")
public class MailProtocol extends IdEntity {

	public static final String PROP_NAME = "name";
	public static final String PROP_SMTP = "smtp";
	public static final String PROP_POP3 = "pop3";
	public static final String PROP_IMAP = "imap";

	private String name;
	private String description;
	private String link;
	private String suffix;
	private String smtp;
	private String pop3;
	private String imap;
	private Boolean delFlag;
	
	public MailProtocol(Long id) {
		this.id = id;
	}

}
