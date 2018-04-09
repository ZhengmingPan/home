package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Role;

@Repository
public interface RoleDao extends JpaDao<Role, Long> {

}
