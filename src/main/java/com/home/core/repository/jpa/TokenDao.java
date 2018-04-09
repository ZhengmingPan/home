package com.home.core.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.dao.JpaDao;
import com.home.core.entity.Token;

@Repository
public interface TokenDao extends JpaDao<Token, String> {

	@Modifying
	@Transactional
	@Query("UPDATE Token entity SET entity.status=" + Token.STATUS_DISABLE + " WHERE entity.user.id=:userId")
	void disable(@Param("userId") Long userId);

	@Query("From Token entity Where entity.user.id=:userId And entity.status=" + Token.STATUS_ENABLE)
	List<Token> listByEnable(@Param("userId") Long userId);

}
