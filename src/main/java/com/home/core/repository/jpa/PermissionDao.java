package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Permission;

@Repository
public interface PermissionDao extends JpaDao<Permission, String> {

}
