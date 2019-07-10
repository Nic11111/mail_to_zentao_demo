package com.vst.task;

import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.simplejavamail.converter.EmailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vst.utils.MailUtil;
import com.vst.utils.ZentaoUtil;

@Component
public class TimerTask {
	
	@Autowired
	private MailUtil mailUtil;
	
	@Autowired
	private ZentaoUtil zentaoUtil;
	
	
	
	@Scheduled(cron="${job.timer}")
	public void workFlow() throws Exception {
		
		// 读邮件
//		List<Message> messagesToSave = mailService.getMail();
//		
//		for (Message msg : messagesToSave) {
//			
//			int bugID = zentaoService.createBug(msg);
//			persistenceService.storeMail(msg, bugID);
//			String fromMail = "";
//			if (msg.getFrom() != null && msg.getFrom()[0] != null) {
//				if(msg.getFrom()[0].toString().contains("<")) {
//					fromMail = msg.getFrom()[0].toString().substring(
//							msg.getFrom()[0].toString().lastIndexOf("<")+1, msg.getFrom()[0].toString().lastIndexOf(">"));
//				} else {
//					fromMail = msg.getFrom()[0].toString();
//				}
//			}
//			mailService.replyMail(msg, String.valueOf(21));
//		}
		
		List<Message> messagesReceive = mailUtil.getMail();
		
		for (Message msg : messagesReceive) {
			String title = msg.getSubject();
			String eml = EmailConverter.mimeMessageToEML((MimeMessage)msg);
			InternetAddress[] fromAddress = (InternetAddress[]) msg.getFrom();
			String from = fromAddress[0].getAddress();
			InternetAddress[] toAddress = (InternetAddress[]) msg.getRecipients(RecipientType.TO);
			String to = toAddress[0].getAddress();
			Date sentDate = msg.getSentDate();
			
			// receiveMail(title, eml, from, to, sentDate);
		}
	}

}
