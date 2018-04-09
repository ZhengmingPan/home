package com.home.mail.entity;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Sets;
import com.home.common.entity.UuIdEntity;
import com.home.mail.utils.MimeMessageUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 收件箱
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
@Table(name = "mail_message")
public class MailMessage extends UuIdEntity {

	public static final String PROP_ACCOUNT = "account";
	public static final String PROP_SUBJECT = "subject";
	public static final String PROP_CONTENT = "content";
	public static final String PROP_SEND_TIME = "sendTime";
	public static final String PROP_RECEIVED_TIME = "receivedTime";
	public static final String PROP_FLAGS = "flags";
	public static final String PROP_DEL_FLAG = "delFlag";

	public static final Integer FLAG_UNREAD = 0; // 未读
	public static final Integer FLAG_SEEN = 1; // 已读
	public static final Integer FLAG_DELETED = 2; // 已删除
	public static final Integer FLAG_ANSWERED = 3; // 已回复
	public static final Integer FLAG_DRAFT = 4; // 草稿
	public static final Integer FLAG_FLAGGED = 5; // 回收站
	public static final Integer FLAG_RECENT = 6; // 新邮件

	public static final String FLAGS_SEPARATOR = ",";

	@ManyToOne()
	@JoinColumn(name = "account_id")
	private MailAccount account;
	@Column(name = "from_address")
	private String from;
	@Column(name = "to_address")
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "send_date")
	private Date sendTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "received_date")
	private Date receivedTime;
	private String flags;
	private Integer size;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private Long creator;
	private Boolean delFlag;

	@Transient
	public Set<String> getFlagList() {
		Set<String> flagElements = Sets.newHashSet();
		if (StringUtils.isNotEmpty(flags)) {
			flagElements = Sets.newHashSet(flags.split(FLAGS_SEPARATOR));
		}
		return flagElements;
	}

	@Transient
	public void signFlag(Integer flag) {
		Set<String> flags = getFlagList();
		flags.add(flag.toString());
		setFlags(flags.stream().collect(Collectors.joining(FLAGS_SEPARATOR)));
	}

	@Transient
	public void removeFlag(Integer flag) { 
		setFlags(getFlagList().stream().filter(element -> !flag.equals(Integer.valueOf(element)))
				                       .collect(Collectors.joining(FLAGS_SEPARATOR)));
	}

	@Transient
	public void moveValuesFromMessagge(Message message) throws MessagingException {
		MimeMessage mimeMessage = (MimeMessage) message;
		MimeMessageUtils.setMimeMessage(mimeMessage);
		setFrom(MimeMessageUtils.getFroms());
		setTo(MimeMessageUtils.getRecipientsByType(RecipientType.TO));
		setCc(MimeMessageUtils.getRecipientsByType(RecipientType.CC));
		setBcc(MimeMessageUtils.getRecipientsByType(RecipientType.BCC));
		setSubject(MimeMessageUtils.getSubject());
		setContent(MimeMessageUtils.getContent());
		setSendTime(message.getSentDate());
		setReceivedTime(message.getReceivedDate());
		setSize(message.getSize());
	}
}
