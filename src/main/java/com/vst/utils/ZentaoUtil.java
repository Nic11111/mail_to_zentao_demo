package com.vst.utils;

import javax.mail.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZentaoUtil {

	@Value("${zentao.url}")
	public static String ZENTAO_URL;
	
	@Value("${zentao.account}")
	public static String ZENTAO_ACCOUNT;
	
	@Value("${zentao.password}")
	public static String ZENTAO_PASSWORD;
	
	@Value("${zentao.session_id_path}")
	public static String ZENTAO_SESSION_ID_PATH;
	
	@Value("${zentao.refer_to}")
	public static String ZENTAO_REFER_TO;
	
	@Value("${zentao.exec_method_path}")
	public static String ZENTAO_EXEC_METHOD_PATH;
	
	@Value("${zentao.login_path}")
	public static String ZENTAO_LOGIN_PATH;
	
	@Value("${zentao.logout_path}")
	public static String ZENTAO_LOGOUT_PATH;
	
	@Value("${zentao.bug_browse_path}")
	public static String ZENTAO_BUG_BROWSE_PATH;
	
	@Value("${zentao.bug_create_path}")
	public static String ZENTAO_BUG_CREATE_PATH;
	
	@Autowired
	private static ZentaoMethod zentaoMethod;
	
	public static int createBug(Message message) throws Exception {
		String sessionIdUrl = ZENTAO_URL + ZENTAO_SESSION_ID_PATH;
		String zentaoID = zentaoMethod.getZentaoID(sessionIdUrl);
		
		String loginUrl = ZENTAO_URL + ZENTAO_LOGIN_PATH;
		zentaoMethod.zentaoLogin(loginUrl, ZENTAO_ACCOUNT, ZENTAO_PASSWORD, zentaoID);
		
		String createBugUrl = ZENTAO_URL + ZENTAO_BUG_CREATE_PATH;
		zentaoMethod.createBug(createBugUrl, message, zentaoID);
		
		String browseBugUrl = ZENTAO_URL + ZENTAO_BUG_BROWSE_PATH;
		int bugID = zentaoMethod.getBugID(browseBugUrl, message.getSubject(), zentaoID);
		
		return bugID;
	}
}
