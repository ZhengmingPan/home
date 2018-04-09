package com.home.mail.repository.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.mail.entity.MailAccount;

@Repository
public interface MailAccountDao extends JpaDao<MailAccount, Long> {

	@Query("From MailAccount entity Where entity.username=:username And entity.protocol.id=:protocolId")
	MailAccount getByUsernameAndProtocol(@Param("username")String username, @Param("protocolId")Long protocolId);

	@Query("From MailAccount entity Where entity.type=0")
	MailAccount getOfSystem();

}
