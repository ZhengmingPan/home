package com.home.core.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.UserGroup;

@Repository
public interface UserGroupDao extends JpaDao<UserGroup, Long> {

	@Query("From UserGroup entity Where entity.parentId is null")
	List<UserGroup> listRootGroup();
	
	@Query("From UserGroup entity Where entity.parentId=:parentId")
	List<UserGroup> listByParentId(@Param("parentId")Long parentId);

	UserGroup getByCode(String code);

}
