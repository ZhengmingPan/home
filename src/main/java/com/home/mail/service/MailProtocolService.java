package com.home.mail.service;

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
import com.home.mail.entity.MailProtocol;
import com.home.mail.repository.jpa.MailProtocolDao;

@Service
@Transactional(readOnly = true)
public class MailProtocolService extends JpaServiceImpl<MailProtocol, Long> {
	
	@Autowired
	private MailProtocolDao mailProtocolDao;
	 
	public List<MailProtocol> listOfTrue() {
		return mailProtocolDao.listOfTrue();
	}
	
	public MailProtocol getByName(String name) {
		return mailProtocolDao.getByName(name);
	}
	
	public Boolean existName(Long id, String name) {
		MailProtocol protocol = getByName(name);
		if(protocol == null || id != null && id.equals(protocol.getId())) {
			return false;
		}
		return true;
	}
	
	@Override
	@Transactional(readOnly = false)
	public MailProtocol save(MailProtocol protocol) {
		if(protocol.getId() == null) {
			protocol.setDelFlag(false);
		}
		return super.save(protocol);
	}
	
	@Transactional(readOnly = false)
	public void stop(Long id) {
		MailProtocol protocol = super.get(id);
		protocol.setDelFlag(true);
		save(protocol);
	}
	
	@Transactional(readOnly = false)
	public void start(Long id) {
		MailProtocol protocol = super.get(id);
		protocol.setDelFlag(false);
		save(protocol);
	}
	
	public Page<MailProtocol> page( final String searchKey, Pageable pageable) {
		return mailProtocolDao.findAll(new Specification<MailProtocol>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2224226692677785395L;

			@Override
			public Predicate toPredicate(Root<MailProtocol> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
 				if(StringUtils.isNotEmpty(searchKey)) {
					predicates.add(cb.like(root.get(MailProtocol.PROP_NAME).as(String.class), "%"+StringUtils.trimToEmpty(searchKey)+"%"));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
	}
}
