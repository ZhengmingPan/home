package com.home;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ReceiveTest {

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.setProperty("mail.store.protocol", "pop3");
			props.setProperty("mail.pop3.host", "pop3.163.com");

			Session session = Session.getInstance(props);
			Store store = session.getStore("pop3");
			store.connect("pop3.163.com", "pan19940609", "pan199462");

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);

			Message[] messages = folder.getMessages();
			for (int i=messages.length - 1;i>=0; i-- ) {
				MimeMessage mm = (MimeMessage) messages[i];
				System.out.println(mm.getContentType());
				/*if(i == messages.length - 1) {
					MimeMultipart mimeMultipart =(MimeMultipart) mm.getContent();
					int count = mimeMultipart.getCount();
					for(int k=0;k<count;k++) {
						
						MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(k);
						System.out.println((MimeBodyPart)part.getContent());
						if(part.getFileName() != null) {
							InputStream in = part.getInputStream();
						    File dir = new File("/opt/upload");
						    if(!dir.exists()) {
						    	dir.mkdirs();
						    }
						    File file = new File("/opt/upload/" + part.getFileName());
						    if(!file.exists()) {
						    	file.createNewFile();
						    }
						    BufferedInputStream bis = null;
						    BufferedOutputStream bos = null;
						    bos = new BufferedOutputStream(new FileOutputStream(file));
							bis = new BufferedInputStream(in);
							int c;
							while ((c = bis.read()) != -1) {
								bos.write(c);
								bos.flush();
							} 
							bis.close();
							bos.close();
						    
						}

						System.err.println("============================================");
					}
				}*/
				
			}
			folder.close(false);
			store.close();
		} catch (Exception e) {
		}
	}
	
	public static void getMailContent(Part part, StringBuilder contentText) throws Exception {
		String contentType = part.getContentType();
		// 获得邮件的MimeType类型
		System.out.println("邮件的MimeType类型: " + contentType);

		int nameIndex = contentType.indexOf("name");
		boolean conName = false;
		if (nameIndex != -1) {
			conName = true;
		}

		System.out.println("邮件内容的类型:　" + contentType);
		if (part.isMimeType("text/plain") && conName == false) {
			contentText.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && conName == false) {
			contentText.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) part;
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i), contentText);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent(), contentText);
		} else {
		}
	}
}
