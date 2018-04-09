package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Area;

@Repository
public interface AreaDao extends JpaDao<Area, String> {

}
