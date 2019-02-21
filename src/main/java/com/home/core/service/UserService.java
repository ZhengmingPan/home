package com.home.core.service;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.home.common.service.JpaServiceImpl;
import com.home.common.utils.RequestUtils;
import com.home.core.entity.User;
import com.home.core.repository.jpa.UserDao;

@Service
@Transactional(readOnly = true)
public class UserService extends JpaServiceImpl<User, Long> {

	@Value("${shiro.hashAlgorithmName}")
	private String hashAlgorithmName;
	@Value("${shiro.hashIterations}")
	private int hashIterations;

	@Autowired
	private UserDao userDao;

	@Transactional(readOnly = false)
	public User register(User user, HttpServletRequest request) {
		if (StringUtils.isNotEmpty(user.getPlainPassword())) {
			encrypt(user);
		}
		try {
			user.setRegisterTime(new Date());
			user.setRegisterIp(RequestUtils.getIpAddress(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.save(user);
	}

	@Transactional(readOnly = false)
	public void entryPasword(Long id, String plainPassword) {
		User user = super.get(id);
		user.setPlainPassword(plainPassword);
		encrypt(user);
		super.save(user);
	}

	@Transactional(readOnly = false)
	public void start(Long id) {
		User user = super.get(id);
		user.setStatus(User.STATUS_ACTIVE);
		super.save(user);
	}

	@Transactional(readOnly = false)
	public void stop(Long id) {
		User user = super.get(id);
		user.setStatus(User.STATUS_TERMINATED);
		super.save(user);
	}

	public boolean existLoginName(Long id, String loginName) {
		User user = getByLoginName(loginName);
		if (user == null || id != null && id.equals(user.getId())) {
			return false;
		}
		return true;
	}

	public User getByLoginName(String loginName) {
		return userDao.getByLoginName(loginName);
	}

	public Page<User> page(final String loginName, final String name, final String mobile, final String status, final Date startDate, final Date endDate, final String searchKey, Pageable page) {
		return userDao.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();

				if (StringUtils.isNotEmpty(loginName)) {
					predicates.add(cb.equal(root.get(User.PROP_LOGIN_NAME).as(String.class), loginName));
				}
				if (StringUtils.isNotEmpty(name)) {
					predicates.add(cb.equal(root.get(User.PROP_NAME).as(String.class), name));
				}
				if (StringUtils.isNotEmpty(mobile)) {
					predicates.add(cb.equal(root.get(User.PROP_MOBILE).as(String.class), mobile));
				}
				if (StringUtils.isNotEmpty(status)) {
					predicates.add(cb.equal(root.get(User.PROP_STATUS).as(String.class), status));
				}
				if (startDate != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(User.PROP_REGISTER_TIME).as(Date.class), startDate));
				}
				if (endDate != null) {
					predicates.add(cb.lessThanOrEqualTo(root.get(User.PROP_REGISTER_TIME).as(Date.class), endDate));
				}

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(User.PROP_LOGIN_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(User.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

	// SHA-1的1024次并salt加密
	private void encrypt(User user) {
		String salt = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		user.setSalt(salt);
		SimpleHash sh = new SimpleHash(hashAlgorithmName, user.getPlainPassword(), salt, hashIterations);
		user.setPassword(sh.toString());
	}
}
