package com.home.core.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Annex;

@Repository
public interface AnnexDao extends JpaDao<Annex, String> {

	Annex getByPath(String path);

}
