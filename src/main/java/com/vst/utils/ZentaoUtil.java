package com.vst.utils;

import javax.mail.internet.MimeMessage;

import org.simplejavamail.converter.EmailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;


@Component
public class ZentaoUtil {

	@Value("${zentao.url}")
	private String ZENTAO_URL;
	
	@Value("${zentao.account}")
	private String ACCOUNT;
	
	@Value("${zentao.password}")
	private String PASSWORD;
	
	@Value("${zentao.session_id}")
	private String SESSION_ID;
	
	@Value("${zentao.refer_to}")
	private String REFER_TO;
	
	@Value("${zentao.exec_method}")
	private String EXEC_METHOD;
	
	@Value("${zentao.login}")
	private String LOGIN;
	
	@Value("${zentao.logout}")
	private String LOGOUT;
	
	@Value("${zentao.bug_view}")
	private String BUG_VIEW;
	
	@Value("${zentao.bug_browse}")
	private String BUG_BROWSE;
	
	@Value("${zentao.bug_create}")
	private String BUG_CREATE;
	
	@Value("${zentao.bug_edit}")
	private String BUG_EDIT;
	
	@Autowired
	private ZentaoMethod zentaoMethod;
	
	private String zentaoID;
	
	
	public int createBug(int productId, int openedBuildId, 
			String subject, String eml, String fileName, String from, String to) throws Exception {
		
		MimeMessage msg = EmailConverter.emlToMimeMessage(eml);
		if (zentaoID == null) {
			String sessionIdUrl = ZENTAO_URL + SESSION_ID;
			zentaoID = zentaoMethod.getZentaoID(sessionIdUrl);
			
			String loginUrl = ZENTAO_URL + LOGIN;
			zentaoMethod.zentaoLogin(loginUrl, ACCOUNT, PASSWORD, zentaoID);
		}
		String createBugUrl = String.format(ZENTAO_URL + BUG_CREATE, productId);
		zentaoMethod.createBug(createBugUrl, msg, zentaoID, eml, fileName, productId, openedBuildId);
		
		String browseBugUrl = ZENTAO_URL + BUG_BROWSE;
		int bugID = zentaoMethod.getBugID(browseBugUrl, msg.getSubject(), zentaoID);
		
		return bugID;
	}
	
	public int updateBug(int bugId, String eml, String status) throws Exception {
		
		if (zentaoID == null) {
			String sessionIdUrl = ZENTAO_URL + SESSION_ID;
			zentaoID = zentaoMethod.getZentaoID(sessionIdUrl);
			
			String loginUrl = ZENTAO_URL + LOGIN;
			zentaoMethod.zentaoLogin(loginUrl, ACCOUNT, PASSWORD, zentaoID);
		}
		String updateBugUrl = String.format(ZENTAO_URL + BUG_EDIT, bugId);
		zentaoMethod.updateBug(updateBugUrl, eml, zentaoID, status);
		
		return 0;
	}
	
	public JsonNode getBug(int bugId) throws Exception {
		
		if (zentaoID == null) {
			String sessionIdUrl = ZENTAO_URL + SESSION_ID;
			zentaoID = zentaoMethod.getZentaoID(sessionIdUrl);
			
			String loginUrl = ZENTAO_URL + LOGIN;
			zentaoMethod.zentaoLogin(loginUrl, ACCOUNT, PASSWORD, zentaoID);
		}
		String getBugUrl = String.format(ZENTAO_URL + BUG_VIEW, bugId);
		JsonNode bug = zentaoMethod.getBug(getBugUrl, zentaoID);
		
		return bug;
	}
}
