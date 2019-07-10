package com.vst.service.impl;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.service.ZentaoService;
import com.vst.utils.ZentaoUtil;

@Service
public class ZentaoServiceImpl implements ZentaoService {

	private static final Logger logger = LoggerFactory.getLogger(ZentaoServiceImpl.class);
	
	@Autowired
	private ZentaoUtil zentaoUtil;
	
	@Override
	public int createBug(Message message) throws Exception {
		
		logger.info("call createBug().");
//		return zentaoUtil.createBug(message);
		return 0;
	}

}
