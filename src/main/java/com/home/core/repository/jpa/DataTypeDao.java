package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.DataType;

@Repository
public interface DataTypeDao extends JpaDao<DataType, Long> {

	DataType getByCode(String code);

}
