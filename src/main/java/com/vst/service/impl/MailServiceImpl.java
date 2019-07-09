package com.vst.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vst.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	private static final String POP_SERVER_URL = "pop.qq.com";
	private static final String SMTP_SERVER_URL = "smtp.qq.com";
	private static final String EMAIL_ADDRESS = "714413099@qq.com";
	private static final String PASSWORD = "aanfoofqvrwtbcea";
	
	private static final int POP_SERVER_PORT = 995;
	private static final int SMTP_SERVER_PORT = 587;
	
	@Value("${period}")
	private int period;
	
	
	@Override
	public int sendMail() {
		
		Email email = EmailBuilder.startingBlank()
				.from("test email box", EMAIL_ADDRESS)
				.to("test recieve", EMAIL_ADDRESS)
				.withSubject("test20190628")
				.withPlainText("testcontent20190628")
				.buildEmail();
		MailerBuilder
			.withSMTPServer(SMTP_SERVER_URL, SMTP_SERVER_PORT, 
					EMAIL_ADDRESS, PASSWORD)
			.buildMailer()
			.sendMail(email);
		logger.info("mail sent. finish.");
		
		return 0;
	}
	
	

	@Override
	public List<Message> getMail() {
		
		Properties props = new Properties();
		props.setProperty("mail.pop3.host", POP_SERVER_URL);
		props.setProperty("mail.pop3.port", String.valueOf(POP_SERVER_PORT));
		props.setProperty("mail.pop3.ssl.enable", "true");
		
		Session session = Session.getDefaultInstance(props, null);
		
		try {
			
			Store store = session.getStore("pop3");
			logger.info("start connect mail box.");
			store.connect(POP_SERVER_URL, EMAIL_ADDRESS, PASSWORD);
			logger.info("connected.");
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			logger.info("getting messages.");
			logger.info("邮箱共有: " + folder.getMessageCount() + " 封邮件.");
			logger.info("邮箱共有: " + folder.getUnreadMessageCount() + " 封未读邮件.");
			logger.info("邮箱共删除: " + folder.getDeletedMessageCount() + " 封邮件.");
			logger.info("================================================\r\n");
			
			Message messages[] = folder.getMessages();
			List<Message> messageList = new ArrayList<Message>();
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
				if (new Date().getTime() < (msg.getSentDate().getTime() + period)) {
					messageList.add(msg);
				}
			}
			return messageList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}



	@Override
	public int replyMail(Message message, String bugID) throws Exception {
		MimeMessage mms = (MimeMessage)message;
		Email email = EmailBuilder.replyingTo(mms)
				.from(EMAIL_ADDRESS)
				.withSubject("RE: " + mms.getSubject() + " bugID:" + bugID)
				.prependText(null)
				.buildEmail();
		MailerBuilder
			.withSMTPServer(SMTP_SERVER_URL, SMTP_SERVER_PORT, 
				EMAIL_ADDRESS, PASSWORD)
			.buildMailer()
			.sendMail(email);
		logger.info("mail sent. finish.");

		return 0;
	}
	
}
