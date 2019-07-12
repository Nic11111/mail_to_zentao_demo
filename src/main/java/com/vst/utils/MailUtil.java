package com.vst.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.simplejavamail.converter.EmailConverter;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	@Value("${mail.pop_server}")
	private String POP_SERVER;
	
	@Value("${mail.smtp_server}")
	private String SMTP_SERVER;
	
	@Value("${mail.mailbox}")
	private String EMAIL_ADDRESS;
	
	@Value("${mail.imap_server}")
	private String IMAP_SERVER;
	
	@Value("${mail.pop_password}")
	private String POP_PASSWORD;
	
	@Value("${mail.imap_password}")
	private String IMAP_PASSWORD;
	
	@Value("${mail.imap_port}")
	private int IMAP_PORT;
	
	@Value("${mail.pop_port}")
	private int POP_PORT;
	
	@Value("${mail.smtp_port}")
	private int SMTP_PORT;
	
//	@Value("${period}")
//	private int PERIOD;
	
	
	public List<Message> getMail() {
		
		Properties props = new Properties();
		props.setProperty("mail.imap.host", IMAP_SERVER);
		props.setProperty("mail.imap.port", String.valueOf(IMAP_PORT));
		props.setProperty("mail.imap.ssl.enable", "true");
		
		Session session = Session.getDefaultInstance(props, null);
		try {
			Store store = session.getStore("imap");
			logger.info("connect mail box.");
			store.connect(IMAP_SERVER, EMAIL_ADDRESS, IMAP_PASSWORD);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			logger.info("connected.");
			
			Message[] messages = folder.getMessages();
			List<Message> messageList = new ArrayList<Message>();
			logger.info("邮箱共有: " + folder.getMessageCount() + "封邮件.");
			logger.info("邮箱共有: " + folder.getUnreadMessageCount() + "封未读邮件.");
			logger.info("================================================\r\n");
			
			for (Message msg : messages) {
				if (msg.getFrom() != null && msg.getFrom()[0] != null) {
					if(msg.getFrom()[0].toString().contains("<")) {
						logger.info("邮件来自: " + msg.getFrom()[0].toString().substring(msg.getFrom()[0].toString().lastIndexOf("<")+1, msg.getFrom()[0].toString().lastIndexOf(">")));
					} else {
						logger.info("邮件来自: " + msg.getFrom()[0]);
					}
				}
				logger.info("邮件主题: " + msg.getSubject());
				logger.info("发送日期: " + msg.getSentDate());
				String type = msg.getContentType().toString().substring(0, msg.getContentType().toString().indexOf(";"));
				logger.info("邮件类型: " + type);
				logger.info("邮件内容: " + msg.getContent());
				if(type.equals("text/html")) {
					// 根据文件的类型来更改文件的解析方式  text/html  multipart/alternative表示复合类型
				}
				InternetAddress[] address = (InternetAddress[]) msg.getFrom();
				String from = address[0].getAddress();
				if (from == null) {
					from = "";
				}
				String personal = address[0].getPersonal();
				if (personal == null) {
					personal = "";
				}
				String fromaddr = personal + "<" + from + ">";
				logger.info("邮件作者：" + fromaddr);
				logger.info("================================================\r\n");
				Flags flags = msg.getFlags();
				messageList.add(msg);
//				if (!flags.contains(Flags.Flag.SEEN)) {
//					messageList.add(msg);
//					msg.setFlag(Flag.SEEN, true);
//				}
			}
			return messageList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int replyMail(/*Message message, String bugID*/String subject, String eml, String from, String to, String content) throws Exception {
//		MimeMessage mms = (MimeMessage)message;
//		Email email = EmailBuilder.replyingTo(mms)
//				.from(EMAIL_ADDRESS)
//				.withSubject("RE: " + mms.getSubject() + " bugID:" + bugID)
//				.prependText(null)
//				.buildEmail();
//		MailerBuilder
//			.withSMTPServer(SMTP_SERVER, SMTP_PORT, 
//				EMAIL_ADDRESS, POP_PASSWORD)
//			.buildMailer()
//			.sendMail(email);
		
		MimeMessage mmsg = EmailConverter.emlToMimeMessage(eml);
		Email email = EmailBuilder.replyingTo(mmsg)
				.from(from)
				.withSubject(subject)
				.prependText(content)
				.buildEmail();
		logger.info("email build.");
		MailerBuilder
			.withSMTPServer(SMTP_SERVER, SMTP_PORT, EMAIL_ADDRESS, IMAP_PASSWORD)
			.buildMailer().sendMail(email);
		logger.info("email replied.");
		
		return 0;
	}
}
