package com.vst.utils;

import javax.mail.Message;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.vst.constant.ZentaoParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class ZentaoMethod {
	
	public String getZentaoID() {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(
					ZentaoParam.ZENTAO_URL + ZentaoParam.ZENTAO_SESSION_ID_PATH);
			httpGet.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			httpGet.setHeader("Accept-Encoding", "gzip, deflate");
			httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
			CloseableHttpResponse response = httpClient.execute(httpGet);

			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				String tempStr = header.getValue();
				if (tempStr.contains("zentaosid")) {
					return tempStr.split(";")[0].split("=")[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	public void zentaoLogin(String username, String password, String zentaoID) throws Exception {
		
		if (username.isEmpty() || password.isEmpty() || zentaoID.isEmpty()) {
			throw new Exception("请检查禅道登录参数");
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(
					ZentaoParam.ZENTAO_URL + ZentaoParam.ZENTAO_LOGIN_PATH);

			httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);

			String postBody = "account=" + username + "&password=" + password + "&referer=" + ZentaoParam.ZENTAO_REFER_TO;
			StringEntity postEntity = new StringEntity(postBody, "UTF-8");
			httpPost.setEntity(postEntity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			System.out.println(
					"**************login response: \n" + EntityUtils.toString(responseEntity) + "\n**************");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public int createBug(Message message, String zentaoID) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(
					ZentaoParam.ZENTAO_URL + ZentaoParam.ZENTAO_BUG_CREATE_PATH);

			httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);
			
			ParseMimeMessage parseMimeMessage = new ParseMimeMessage((MimeMessage)message);
			parseMimeMessage.getMailContent((Part)message);
			
			String postBody = "product=" + 1 + "&module=" + 1 + "&title=" 
					+ message.getSubject() + "&openedBuild=" + 1
					+ "&steps=" + parseMimeMessage.getBodyText();
			StringEntity postEntity = new StringEntity(postBody, "UTF-8");
			httpPost.setEntity(postEntity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			System.out.println(
					"**************bug create response: \n" + EntityUtils.toString(responseEntity) + "\n**************");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public int getBugID(String subject, String zentaoID) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(
					ZentaoParam.ZENTAO_URL + ZentaoParam.ZENTAO_BUG_BROWSE_PATH);

			httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpGet.setHeader("Accept-Encoding", "gzip, deflate");
			httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
			httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpGet.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);

			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			String responseEntityStr = EntityUtils.toString(responseEntity);
			System.out.println(
					"**************bug browse response: \n" + responseEntityStr + "\n**************");
			JSONObject jsonObject = JSONObject.fromObject(responseEntityStr);
			JSONObject jsonObject2 = JSONObject.fromObject(jsonObject.get("data"));
			JSONArray jsonArray = JSONArray.fromObject(jsonObject2.get("bugs"));
			Object[] array = jsonArray.toArray();
			for (int index = 0; index < array.length; index++) {
				JSONObject object = JSONObject.fromObject(array[index]);
				if (object.get("status").equals("active") && object.get("title").equals(subject)) {
					return Integer.valueOf(object.get("id").toString());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
