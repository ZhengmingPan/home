package com.home.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.service.JpaServiceImpl;
import com.home.core.entity.UserGroup;
import com.home.core.repository.jpa.UserGroupDao;

@Service
@Transactional(readOnly = true)
public class UserGroupService extends JpaServiceImpl<UserGroup, Long> {

	@Autowired
	private UserGroupDao userGroupDao;

	@Override
	@Transactional(readOnly = false)
	public UserGroup save(UserGroup group) {
		if (group.getId() == null) {
			group.setDelFlag(false);
		}
		return super.save(group);
	}

	public UserGroup getByCode(String code) {
		return userGroupDao.getByCode(code);
	}

	public List<UserGroup> listRootGroups() {
		return userGroupDao.listRootGroup();
	}

	public List<UserGroup> listByParentId(Long parentId) {
		return userGroupDao.listByParentId(parentId);
	}

	public Boolean existCode(Long id, String code) {
		UserGroup group = getByCode(code);
		if (group == null || id != null && id.equals(group.getId())) {
			return false;
		}
		return true;
	}

}
