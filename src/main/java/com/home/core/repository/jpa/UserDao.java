package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.User;

@Repository
public interface UserDao extends JpaDao<User, Long> {

	User getByLoginName(String loginName);

}
