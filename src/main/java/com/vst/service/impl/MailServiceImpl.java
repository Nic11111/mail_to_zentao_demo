package com.vst.service.impl;

import java.util.List;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.service.MailService;
import com.vst.utils.MailUtil;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private MailUtil mailUtil;
	
	@Override
	public List<Message> getMail() {
		
		logger.info("call getMail().");
		return mailUtil.getMail();
	}

	@Override
	public int replyMail(Message message, String bugID) throws Exception {
		
		logger.info("call replyMail().");
//		return mailUtil.replyMail(message, bugID);
		return 0;
	}
	
}
