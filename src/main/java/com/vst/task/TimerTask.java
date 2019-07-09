package com.vst.task;

import java.util.List;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vst.service.MailService;
import com.vst.service.PersistenceService;
import com.vst.service.ZentaoService;

@Component
public class TimerTask {
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private PersistenceService persistenceService;
	
	@Autowired
	private ZentaoService zentaoService;
	
	
	@Scheduled(cron="${job.timer}")
	public void workFlow() throws Exception {
		
		// 读邮件
		List<Message> messagesToSave = mailService.getMail();
		
		for (Message msg : messagesToSave) {
			
			
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
		}
		
	}
	
	public int createBugs() throws Exception {
		return 0;
	}

	public int scanZentaoBugs() throws Throwable {
		return 0;
	}
	
	public int finishBugs() throws Throwable {
		return 0;
	}

}
