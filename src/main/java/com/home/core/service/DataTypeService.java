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
import com.home.core.entity.DataType;
import com.home.core.repository.jpa.DataTypeDao;

@Service
@Transactional(readOnly = true)
public class DataTypeService extends JpaServiceImpl<DataType, Long> {

	@Autowired
	private DataTypeDao dataTypeDao;

	public DataType getByCode(String code) {
		return dataTypeDao.getByCode(code);
	}

	public boolean existCode(Long id, String code) {
		DataType dataType = getByCode(code);
		if (dataType == null || id != null && id.equals(dataType.getId())) {
			return false;
		}
		return true;
	}

	public Page<DataType> page(final String searchKey, Pageable page) {
		return dataTypeDao.findAll(new Specification<DataType>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9182250526439247785L;

			@Override
			public Predicate toPredicate(Root<DataType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(DataType.PROP_CODE).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(DataType.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

}
