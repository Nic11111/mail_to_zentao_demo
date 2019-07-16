package com.vst.utils;

import java.nio.charset.Charset;

import javax.mail.Message;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


@Component
public class ZentaoMethod {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String getZentaoID(String sessionIdUrl) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(sessionIdUrl);
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
	
	
	public void zentaoLogin(String loginUrl, String username, String password, String zentaoID) throws Exception {
		
		if (username.isEmpty() || password.isEmpty() || zentaoID.isEmpty()) {
			throw new Exception("请检查禅道登录参数");
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(loginUrl);

			httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);

			String postBody = "account=" + username + "&password=" + password;
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
	
	
	public int createBug(String createBugUrl, Message message, String zentaoID, String eml, String fileName, int productId, int openedBuildId) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			
			ParseMimeMessage parseMimeMessage = new ParseMimeMessage((MimeMessage)message);
			parseMimeMessage.getMailContent((Part)message);
			
			StringBody product = new StringBody(
					String.valueOf(productId), ContentType.create("text/plain", Consts.UTF_8));
			StringBody module = new StringBody(
					String.valueOf(1), ContentType.create("text/plain", Consts.UTF_8));
			StringBody title = new StringBody(
					message.getSubject(), ContentType.create("text/plain", Consts.UTF_8));
			StringBody openedBuild = new StringBody(
					String.valueOf(openedBuildId), ContentType.create("text/plain", Consts.UTF_8));
			StringBody content = new StringBody(
					parseMimeMessage.getBodyText(), ContentType.create("text/plain", Consts.UTF_8));
			HttpEntity entity = MultipartEntityBuilder
	                .create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	                .setCharset(Charset.forName("utf-8"))
	                .addPart("product", product).addPart("module", module).addPart("title", title)
	                .addPart("steps", content).addPart("openedBuild", openedBuild)
	                .addBinaryBody("files", eml.getBytes(Charset.forName("utf8")), ContentType.APPLICATION_OCTET_STREAM, fileName)
	                .build();
			
			HttpUriRequest request = RequestBuilder
					.post(createBugUrl).setEntity(entity).build();
			request.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);
			
			ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(request, responseHandler);
			System.out.println(
					"**************bug create response: \n" + responseBody + "\n**************");

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
	
	public int updateBug(String updateBugUrl, String eml, String zentaoID, String status) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(updateBugUrl);

			httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Content-Type", "multipart/form-data;");
			httpPost.setHeader("Cookie",
					"ang=zh-cn; theme=default; windowWidth=1920; windowHeight=974; zentaosid=" + zentaoID);
			
//			ParseMimeMessage parseMimeMessage = new ParseMimeMessage((MimeMessage)message);
//			parseMimeMessage.getMailContent((Part)message);
//			
//			String postBody = "product=" + productId + "&module=" + 1 + "&title=" 
//					+ message.getSubject() + "&openedBuild=" + openedBuildId
//					+ "&steps=" + parseMimeMessage.getBodyText();
			String postBody = "";
			
			StringEntity postEntity = new StringEntity(postBody, "UTF-8");
			httpPost.setEntity(postEntity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			System.out.println(
					"**************bug edit response: \n" + EntityUtils.toString(responseEntity) + "\n**************");

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
	
	public int getBugID(String browseBugUrl, String subject, String zentaoID) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(browseBugUrl);

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
			ObjectMapper objectMapper = new ObjectMapper();
			
//			JSONObject jsonObject = JSONObject.fromObject(responseEntityStr);
//			JSONObject jsonObject2 = JSONObject.fromObject(jsonObject.get("data"));
//			JSONArray jsonArray = JSONArray.fromObject(jsonObject2.get("bugs"));
//			Object[] array = jsonArray.toArray();
//			for (int index = 0; index < array.length; index++) {
//				JSONObject object = JSONObject.fromObject(array[index]);
//				if (object.get("status").equals("active") && object.get("title").equals(subject)) {
//					return Integer.valueOf(object.get("id").toString());
//				}
//			}
			JsonNode root = objectMapper.readTree(responseEntityStr);
			String dataNodeStr = root.get("data").asText();
			ArrayNode bugsArray = (ArrayNode)objectMapper.readTree(dataNodeStr).get("bugs");
			for (JsonNode bug : bugsArray) {
				if (bug.get("status").asText().equals("active") 
						&& bug.get("title").asText().equals(subject)) {
					return Integer.valueOf(bug.get("id").asText());
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
	
	public JsonNode getBug(String getBugUrl, String zentaoID) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(getBugUrl);

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
					"**************get bug response: \n" + responseEntityStr + "\n**************");
//			JSONObject jsonObject = JSONObject.fromObject(responseEntityStr);
//			JSONObject jsonObjectData = JSONObject.fromObject(jsonObject.get("data"));
//			JSONObject jsonObjectBug = JSONObject.fromObject(jsonObjectData.get("bug"));
			
			JsonNode root = objectMapper.readTree(responseEntityStr);
			JsonNode dataNode = root.get("data");
			String bugNodeStr = dataNode.get("bug").asText();
			JsonNode bugNode = objectMapper.readTree(bugNodeStr);
			
			return bugNode;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
