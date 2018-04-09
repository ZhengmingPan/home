package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.BaseUser;

@Repository
public interface BaseUserDao extends JpaDao<BaseUser, Long> {

	BaseUser getByLoginName(String loginName);
}
