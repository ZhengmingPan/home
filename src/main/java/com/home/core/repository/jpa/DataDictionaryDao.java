package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.DataDictionary;

@Repository
public interface DataDictionaryDao extends JpaDao<DataDictionary, Long> {

}
