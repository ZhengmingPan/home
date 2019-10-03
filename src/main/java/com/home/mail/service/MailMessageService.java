package com.home.mail.service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.home.common.service.JpaServiceImpl;
import com.home.core.web.CoreThreadContext;
import com.home.mail.entity.MailAccount;
import com.home.mail.entity.MailMessage;
import com.home.mail.entity.MailProtocol;
import com.home.mail.repository.jpa.MailMessageDao;

@Service
@Transactional(readOnly = true)
public class MailMessageService extends JpaServiceImpl<MailMessage, String> {

	private static Logger LOGGER = LoggerFactory.getLogger(MailMessageService.class);

	
	@Autowired
	private MailMessageDao mailMessageDao;
	@Autowired
	private MailAccountService mailAccountService;

	@Override
	@Transactional(readOnly = false)
	public MailMessage save(MailMessage message) {
		if (StringUtils.isEmpty(message.getId())) {
			message.setDelFlag(false);
			message.setCreateTime(new Date());
			message.setCreator(CoreThreadContext.getUserId());
		}
		return super.save(message);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveAll(List<MailMessage> messages) {
		messages.forEach((message) -> {
			if (StringUtils.isEmpty(message.getId())) {
				message.setDelFlag(false);
				message.setCreateTime(new Date());
				message.setCreator(CoreThreadContext.getUserId());
			}
		});
		super.saveAll(messages);
	}

	@Transactional(readOnly = false)
	public void signFlag(String id, Integer flag) {
		MailMessage message = super.get(id);
		message.signFlag(flag);
		save(message);
	}

	@Transactional(readOnly = false)
	public void removeFlag(String id, Integer flag) {
		MailMessage message = super.get(id);
		message.removeFlag(flag);
		save(message);
	}

	@Transactional(readOnly = false)
	public void flagDelete(String id) {
		MailMessage message = super.get(id);
		message.setDelFlag(true);
		save(message);
	}

	public Page<MailMessage> page(final Long accountId, final String searchKey, Pageable pageable) {
		return mailMessageDao.findAll(new Specification<MailMessage>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8687781969754812078L;

			@Override
			public Predicate toPredicate(Root<MailMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(cb.equal(root.get(MailMessage.PROP_DEL_FLAG).as(Boolean.class), false));
				if (accountId != null) {
					predicates.add(cb.equal(root.get(MailMessage.PROP_ACCOUNT).get(MailAccount.PROP_ID).as(Long.class), accountId));
				}

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(MailMessage.PROP_SUBJECT).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(MailMessage.PROP_CONTENT).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
	}

	@Transactional(readOnly = false)
	public void receiveOnPop3(Long accountId, String plainPassword) {
		MailAccount account = mailAccountService.get(accountId);
		MailProtocol protocol = account.getProtocol();
		
		try {
			Properties props = new Properties();
			props.setProperty("mail.store.protocol", MailProtocol.PROP_POP3);
			props.setProperty("mail.pop3.host", protocol.getPop3());

			Session session = Session.getInstance(props);
			Store store = session.getStore(MailProtocol.PROP_POP3);
			store.connect(protocol.getPop3(), account.getUsername(), plainPassword);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);

			Message[] messages = folder.getMessages(); 
			for(int i=0;i<messages.length;i++) { 
				Message message = messages[i];
				MailMessage mailMessage = new MailMessage();
				mailMessage.setAccount(account);
				mailMessage.moveValuesFromMessagge(message);
				save(mailMessage); 
				LOGGER.info("第"+(i+1)+"封：标题为 "+mailMessage.getSubject()+"获取邮件成功!!");
			}  
			folder.close(false);
			store.close();
		} catch (Exception e) {
			LOGGER.error("邮件信息转换异常\n" + e.getMessage());
		}
	}

}
