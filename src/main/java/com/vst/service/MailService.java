package com.vst.service;

import java.util.List;

import javax.mail.Message;

public interface MailService {

	public List<Message> getMail();
	public int replyMail(Message message, String bugID) throws Exception;
}
