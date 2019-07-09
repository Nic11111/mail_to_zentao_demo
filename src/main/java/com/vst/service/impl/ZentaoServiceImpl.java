package com.vst.service.impl;

import javax.mail.Message;

import org.springframework.stereotype.Service;

import com.vst.service.ZentaoService;
import com.vst.utils.ZentaoUtil;

@Service
public class ZentaoServiceImpl implements ZentaoService {

	@Override
	public int createBug(Message message) throws Exception {

		return ZentaoUtil.createBug(message);
	}

}
