package com.vst.utils;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

	@Value("${mail.pop_server}")
	private String POP_SERVER;
	
	@Value("${mail.smtp_server}")
	private String SMTP_SERVER;
	
	@Value("${mail.mailbox}")
	private String EMAIL_ADDRESS;
	
	@Value("${mail.pop_password}")
	private String POP_PASSWORD;
	
	@Value("${mail.pop_port}")
	private int POP_PORT;
	
	@Value("${mail.smtp_port}")
	private int SMTP_PORT;
	
	@Value("${period}")
	private int PERIOD;
	
	
	public List<Message> getMail() {
		
		Properties props = new Properties();
		props.setProperty("mail.pop3.host", POP_SERVER);
		props.setProperty("mail.pop3.port", String.valueOf(POP_PORT));
		props.setProperty("mail.pop3.ssl.enable", "true");
		
		Session session = Session.getDefaultInstance(props, null);
		try {
			Store store = session.getStore("pop3");
			store.connect(POP_SERVER, EMAIL_ADDRESS, POP_PASSWORD);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			
			Message messages[] = folder.getMessages();
			List<Message> messageList = new ArrayList<Message>();
			for (Message msg : messages) {
				InternetAddress[] address = (InternetAddress[]) msg.getFrom();
				String from = address[0].getAddress();
				if (from == null) {
					from = "";
				}
				String personal = address[0].getPersonal();
				if (personal == null) {
					personal = "";
				}
				if (new Date().getTime() < (msg.getSentDate().getTime() + PERIOD)) {
					messageList.add(msg);
				}
			}
			return messageList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int replyMail(Message message, String bugID) throws Exception {
		MimeMessage mms = (MimeMessage)message;
		Email email = EmailBuilder.replyingTo(mms)
				.from(EMAIL_ADDRESS)
				.withSubject("RE: " + mms.getSubject() + " bugID:" + bugID)
				.prependText(null)
				.buildEmail();
		MailerBuilder
			.withSMTPServer(SMTP_SERVER, SMTP_PORT, 
				EMAIL_ADDRESS, POP_PASSWORD)
			.buildMailer()
			.sendMail(email);
		return 0;
	}
}
