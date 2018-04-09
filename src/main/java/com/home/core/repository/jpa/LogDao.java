package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Log;

@Repository
public interface LogDao extends JpaDao<Log, String> {

}
