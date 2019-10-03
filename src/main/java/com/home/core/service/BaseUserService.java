package com.home.core.service;

import java.util.ArrayList;
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

import com.home.common.service.JpaServiceImpl;
import com.home.core.entity.BaseUser;
import com.home.core.entity.Role;
import com.home.core.entity.User;
import com.home.core.repository.jpa.BaseUserDao;

@Service
@Transactional(readOnly = true)
public class BaseUserService extends JpaServiceImpl<BaseUser, Long> {

	@Autowired
	private BaseUserDao baseUserDao;

	public BaseUser getByLoginName(String username) {
		return baseUserDao.getByLoginName(username);
	}

	public Page<BaseUser> pageByRole(final Long roleId, final String searchKey, Pageable page) {
		return baseUserDao.findAll(new Specification<BaseUser>() { 
			
			private static final long serialVersionUID = -6434132315996246596L;

			@Override
			public Predicate toPredicate(Root<BaseUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(BaseUser.PROP_LOGIN_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(BaseUser.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}

				if (roleId != null) {
					predicates.add(cb.equal(root.join(BaseUser.PROP_ROLES).get(Role.PROP_ID).as(Long.class), roleId));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

	public Page<BaseUser> pageToSearch(final String searchKey, Pageable page) {
		return baseUserDao.findAll(new Specification<BaseUser>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5485142749206744506L;

			@Override
			public Predicate toPredicate(Root<BaseUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				predicates.add(cb.equal(root.get(BaseUser.PROP_STATUS).as(String.class), User.STATUS_ACTIVE));

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(BaseUser.PROP_LOGIN_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(BaseUser.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

	public Page<BaseUser> pageByGroup(final Long groupId, final String searchKey, Pageable page) {
		return baseUserDao.findAll(new Specification<BaseUser>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3427814871031114309L;

			@Override
			public Predicate toPredicate(Root<BaseUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(BaseUser.PROP_LOGIN_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(BaseUser.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}

				if (groupId != null) {
					predicates.add(cb.equal(root.join(BaseUser.PROP_GROUPS).get(Role.PROP_ID).as(Long.class), groupId));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

}
