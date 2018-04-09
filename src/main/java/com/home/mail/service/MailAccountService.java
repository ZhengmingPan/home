package com.home.mail.service;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import com.home.mail.entity.MailProtocol;
import com.home.mail.repository.jpa.MailAccountDao;
/**
 * 邮件账户服务管理类
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = true)
public class MailAccountService extends JpaServiceImpl<MailAccount, Long> {

 
	@Autowired
	private MailAccountDao mailAccountDao;
 
	public MailAccount getByUsernameAndProtocol(String username, Long protocolId) {
		return mailAccountDao.getByUsernameAndProtocol(username, protocolId);
	}
	
	public MailAccount getOfSystem() {
		return mailAccountDao.getOfSystem();
	}

	public Boolean existUsername(Long id, String username, Long protocolId) {
		MailAccount account = getByUsernameAndProtocol(username, protocolId);
		if (account == null || id != null && id.equals(account.getId())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public MailAccount save(MailAccount account) {
		if (account.getId() == null) {
			account.encryptPassword();
			account.setDelFlag(false);
			account.setCreateTime(new Date());
			account.setType(MailAccount.TYPE_NORMAL);
			account.setCreator(CoreThreadContext.getUserId());
		}
		account.setUpdateTime(new Date());
		account.setUpdator(CoreThreadContext.getUserId());
		return super.save(account);
	}

	@Transactional(readOnly = false)
	public void stop(Long id) {
		MailAccount account = super.get(id);
		account.setDelFlag(true);
		save(account);
	}

	@Transactional(readOnly = false)
	public void start(Long id) {
		MailAccount account = super.get(id);
		account.setDelFlag(false);
		save(account);
	}
	
	@Transactional(readOnly = false)
	public void entryPassword(Long id, String plainPassword) {
		MailAccount account = super.get(id);
		account.setPlainPassword(plainPassword);
		account.encryptPassword();
		save(account);
	}
	
	public Boolean isSamePassword(Long id, String plainPassword) {
		MailAccount account = super.get(id);
		return account.isSamePassword(plainPassword);
	}
	
	
	public Page<MailAccount> page(final Long protocolId, final String searchKey, Pageable pageable) {
		return mailAccountDao.findAll(new Specification<MailAccount>() {
			@Override
			public Predicate toPredicate(Root<MailAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
				if (protocolId != null) {
					predicates.add(cb.equal(root.get(MailAccount.PROP_PROTOCOL).get(MailProtocol.PROP_ID).as(Long.class), protocolId));
				}
				if (StringUtils.isNotEmpty(searchKey)) {
					predicates.add(cb.like(root.get(MailAccount.PROP_USERNAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%"));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
	}
  

}
