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
import com.home.core.entity.Config;
import com.home.core.repository.jpa.ConfigDao;

@Service
@Transactional(readOnly = true)
public class ConfigService extends JpaServiceImpl<Config, Long> {

	@Autowired
	private ConfigDao configDao;

	public Config getByCode(String code) {
		return configDao.getByCode(code);
	}

	public boolean existCode(Long id, String code) {
		Config config = getByCode(code);
		if (config == null || id != null && id.equals(config.getId())) {
			return false;
		}
		return true;
	}

	public Page<Config> page(final String searchKey, Pageable page) {
		return configDao.findAll(new Specification<Config>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7653449304545489581L;

			@Override
			public Predicate toPredicate(Root<Config> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(Config.PROP_CODE).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(Config.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

}
