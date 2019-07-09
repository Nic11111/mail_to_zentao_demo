package com.vst.service.impl;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vst.constant.ZentaoParam;
import com.vst.service.ZentaoService;
import com.vst.utils.ZentaoMethod;

@Service
public class ZentaoServiceImpl implements ZentaoService {

	@Autowired
	private ZentaoMethod zentaoMethod;
	
	@Override
	public int createBug(Message message) throws Exception {
		String zentaoID = zentaoMethod.getZentaoID();
		zentaoMethod.zentaoLogin(
				ZentaoParam.ZENTAO_ACCOUNT, ZentaoParam.ZENTAO_PASSWORD, zentaoID);
		zentaoMethod.createBug(message, zentaoID);
		int bugID = zentaoMethod.getBugID(message.getSubject(), zentaoID);
		return bugID;
	}

}
