package com.home.core.service;

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
import com.home.core.entity.Log;
import com.home.core.repository.jpa.LogDao;

@Service
@Transactional(readOnly = true)
public class LogService extends JpaServiceImpl<Log, String> {

	@Autowired
	private LogDao logDao; 


	public Page<Log> page(final Date startTime, final Date endTime, final String searchKey, Pageable page) {
		return logDao.findAll(new Specification<Log>() {
			@Override
			public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
				if (startTime != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(Log.PROP_LOG_TIME).as(Date.class), startTime));
				}
				if (endTime != null) {
					predicates.add(cb.lessThanOrEqualTo(root.get(Log.PROP_LOG_TIME).as(Date.class), endTime));
				}
				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(Log.PROP_DESCRIPTION).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(Log.PROP_URL).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

}
