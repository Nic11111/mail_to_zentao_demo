package com.vst.service.impl;

import javax.mail.Message;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.simplejavamail.converter.EmailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.mapper.PersistenceMapper;
import com.vst.pojo.EmailToZentao;
import com.vst.service.PersistenceService;
import com.vst.utils.ParseMimeMessage;

@Service
public class PersistenceServiceImpl implements PersistenceService {

	@Autowired
	private PersistenceMapper persistenceMapper;
	
	@Override
	public int storeMail(Message message, int bugID) {
		try {
			ParseMimeMessage parseMimeMessage = new ParseMimeMessage((MimeMessage)message);
			parseMimeMessage.getMailContent((Part)message);
			String emlFileStr = EmailConverter.mimeMessageToEML((MimeMessage)message);
			EmailToZentao emailToZentao = new EmailToZentao();
			emailToZentao.setBugId(bugID);
			emailToZentao.setMailSubject(message.getSubject());
			emailToZentao.setMailContent(parseMimeMessage.getBodyText());
			emailToZentao.setEmlFile(emlFileStr);
			
			persistenceMapper.insertEmailToZentao(emailToZentao);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public EmailToZentao loadMail() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
