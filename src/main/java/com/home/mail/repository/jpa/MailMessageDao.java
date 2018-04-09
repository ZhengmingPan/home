package com.home.mail.repository.jpa;

import org.springframework.stereotype.Repository;

import com.home.common.dao.JpaDao;
import com.home.mail.entity.MailMessage;
@Repository
public interface MailMessageDao extends JpaDao<MailMessage, String> {

}
