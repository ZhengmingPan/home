package com.home.core.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.service.JpaServiceImpl;
import com.home.core.entity.Token;
import com.home.core.entity.User;
import com.home.core.repository.jpa.TokenDao;

@Service
@Transactional(readOnly = true)
public class TokenService extends JpaServiceImpl<Token, String> {
	@Autowired
	private TokenDao tokenDao;

	@Transactional(readOnly = false)
	public void disable(Long userId) {
		tokenDao.disable(userId);
	}

	@Transactional(readOnly = false)
	public String apply(Long userId, String terminal, String remark) {
		disable(userId);

		Token token = new Token();
		token.setUser(new User(userId));
		token.setTerminal(terminal);
		token.setRemark(remark);
		token.setCreateTime(new Date());
		token.setExpireTime(DateUtils.addDays(new Date(), Token.DEFAULT_EXPIRE));
		token.setStatus(Token.STATUS_ENABLE);

		save(token);

		return token.getId();
	}

	public boolean activate(Long userId) {
		List<Token> tokens = tokenDao.listByEnable(userId);
		return !tokens.isEmpty();
	}
}
