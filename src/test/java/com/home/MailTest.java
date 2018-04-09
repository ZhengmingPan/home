package com.home;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
 

public class MailTest {
	
	

	public static void main(String[] args) throws Exception {
		 
		String protocol = "smtp";
		String server = "smtp.163.com";
		String from = "pan19940609@163.com";
		String to = "1319650191@qq.com";
		String subject = "你好";
		String body = "<h2>追日</h2><img src=\"cid:its_logo_jpg\" />";
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.host", server);
		 
		Session session = Session.getInstance(props, new Authenticator() {
 			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String username, password;
				String result = JOptionPane.showInputDialog("用戶名,密碼");
				StringTokenizer st = new StringTokenizer(result, ",");
				username = st.nextToken();
				password = st.nextToken();
 				return new PasswordAuthentication(username, password);
			}
 		});
		session.setDebug(true);
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	    msg.setSentDate(new Date());
	    msg.setSubject(subject);
	    
	    MimeMultipart multipart = new MimeMultipart("mixed");
	    
	    MimeBodyPart htmlBodyPart = new MimeBodyPart();
 	    htmlBodyPart.setContent(body, "text/html;charset=utf-8");
 	    multipart.addBodyPart(htmlBodyPart);
 	    
 	    MimeBodyPart jpgBodyPart = new MimeBodyPart();
 	    FileDataSource fds = new FileDataSource("F:\\management\\src\\static\\images\\avatar.jpg");
 	    jpgBodyPart.setDataHandler(new DataHandler(fds));
 	    jpgBodyPart.setContentID("its_logo_jpg");
 	    multipart.addBodyPart(jpgBodyPart);
 	    
 	    MimeBodyPart attachBodyPart = new MimeBodyPart();
 	    FileDataSource fds1 = new FileDataSource("F:\\management\\src\\static\\images\\avatar.jpg");
 	    attachBodyPart.setDataHandler(new DataHandler(fds1));
 	    attachBodyPart.setFileName(fds.getName());
 	    multipart.addBodyPart(attachBodyPart);
 	    
 	    
 	    msg.setContent(multipart);
 	    
	    msg.saveChanges();
	    
	    Transport transport = session.getTransport();
	    transport.connect();
	    transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
 	    transport.close();
	    msg.writeTo(new FileOutputStream("D:\\mail.eml"));
	}

}
