package com.home.mail.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MimeMessageUtils {

	public static final String ADDRESS_SEPARATOR = ";";
	
	private static MimeMessage mimeMessage;  
	
	public static void setMimeMessage(MimeMessage mimeMessage) {
		MimeMessageUtils.mimeMessage = mimeMessage;  
	} 
 
	public static String getRecipientsByType(RecipientType type) {
		try {
			return transferFromAddressArray(getMimeMessage().getRecipients(type));
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getFroms() {
		try {
			return transferFromAddressArray(getMimeMessage().getFrom());
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getSubject() {
		String subject = null;
		try {
			subject = decodeText(getMimeMessage().getSubject());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return subject;
	} 
	
	public static String getContent() {
		String contentText = null;
		try { 
			StringBuffer content = new StringBuffer(); 
			content.append(getMimeMessage().getContent());
			//appendContent((Part)getMimeMessage().getContent(), content);
			contentText = decodeText(content.toString());
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (MessagingException e) { 
			e.printStackTrace();
		}  
		return contentText;
	}
	 
	private static void appendContent(Part part, StringBuffer content) throws IOException, MessagingException { 
 		String contentType = part.getContentType();
		if(contentType.startsWith("text/")) {
			content.append(part.getContent());
		} else if(contentType.startsWith("message/rfc822")) {
			appendContent((Part)part.getContent(), content);
		}
		else if(contentType.startsWith("multipart/")) {
			MimeMultipart multipart = (MimeMultipart) part.getContent();  
            int partCount = multipart.getCount();  
            for (int i = 0; i < partCount; i++) {  
                MimeBodyPart bodyPart = (MimeBodyPart)multipart.getBodyPart(i);  
                appendContent(bodyPart,content);  
            } 
		}  
	}
	
	private static String decodeText(String encodeText) throws UnsupportedEncodingException { 
        if (encodeText == null || "".equals(encodeText)) {  
            return "";  
        } else {     
            return MimeUtility.decodeText(encodeText);  
        }  
    }


	private static String transferFromAddressArray(Address[] address) {
		Set<String> recipients = Sets.newHashSet();
		if(address == null) { 
			return null;
		}
		Lists.newArrayList(address).forEach(fromItem -> {
			recipients.add(transferFromAddress(fromItem));
		});
		return recipients.stream().filter(element -> StringUtils.isNotEmpty(element)).collect(Collectors.toSet()).stream().collect(Collectors.joining(ADDRESS_SEPARATOR));

	}

	private static String transferFromAddress(Address address) {
		try {
			InternetAddress intAddr = (InternetAddress) address;
			StringBuilder builder = new StringBuilder();
			builder.append(decodeText(intAddr.getPersonal())).append("<").append(intAddr.getAddress()).append(">");
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	 
	private static MimeMessage getMimeMessage() {
		return mimeMessage;
	} 
}
