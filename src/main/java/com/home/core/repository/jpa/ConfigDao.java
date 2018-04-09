package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Config;

@Repository
public interface ConfigDao extends JpaDao<Config, Long> {

	Config getByCode(String code);

}
