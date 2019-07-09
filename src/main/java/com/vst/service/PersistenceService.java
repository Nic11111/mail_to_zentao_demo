package com.vst.service;

import javax.mail.Message;

import com.vst.pojo.EmailToZentao;

public interface PersistenceService {

	public int storeMail(Message message, int bugID);
	public EmailToZentao loadMail();
}
