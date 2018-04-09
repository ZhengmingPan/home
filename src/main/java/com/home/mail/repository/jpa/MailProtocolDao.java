package com.home.mail.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.mail.entity.MailProtocol;
@Repository
public interface MailProtocolDao extends JpaDao<MailProtocol, Long> {

	@Query("From MailProtocol entity Where entity.delFlag = false")
	List<MailProtocol> listOfTrue();

	MailProtocol getByName(String name);

}
