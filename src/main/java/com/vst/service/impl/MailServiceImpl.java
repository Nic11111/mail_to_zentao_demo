package com.vst.service.impl;

import java.util.List;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vst.service.MailService;
import com.vst.utils.MailUtil;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Override
	public List<Message> getMail() {
		
		return MailUtil.getMail();
	}



	@Override
	public int replyMail(Message message, String bugID) throws Exception {
		
		return MailUtil.replyMail(message, bugID);
	}
	
}
