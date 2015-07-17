package com.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SinaNews extends News{

	static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
			+ "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
			+ "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
			+ "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";  //新浪微博的加密公钥
	private static final Log logger = LogFactory.getLog(SinaWeiBo.class);
	
	private static Map<String,String> postHeader = new HashMap<String,String>();
	private static Map<String,String> getHeader = new HashMap<String,String>();
	DefaultHttpClient client = null;
	static{
		postHeader.put("Accept",
				"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		postHeader.put("Referer", "http://weibo.com/");
		postHeader.put("Accept-Language", "zh-cn");
		postHeader.put("Content-Type", "application/x-www-form-urlencoded");
		postHeader.put("Accept-Encoding", "gzip, deflate");
		postHeader.put(
				"User-Agent",
				//"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; InfoPath.2)");
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
		postHeader.put("Connection", "Keep-Alive");
		postHeader.put("Cache-Control", "no-cache");
		
		getHeader.put("Accept","image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		getHeader.put("Accept-Language", "zh-CN");
		getHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		getHeader.put("Accept-Encoding", "deflate");
		getHeader.put(
				"User-Agent",
				//"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; InfoPath.2)");
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
		getHeader.put("Connection", "Keep-Alive");
		getHeader.put("Cache-Control", "no-cache");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void comment(String userName, String pwd, String url, String mid,
			String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void support(String url, String mid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void support(String userName, String pwd, String url, String mid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected DefaultHttpClient login(String userName, String pwd)  throws  IOException {
		// TODO Auto-generated method stub
		String url = null;
		HttpResponse response = null;
		String charset = null;
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		String su = encodeAccount(userName);
//		DefaultHttpClient client = new DefaultHttpClient();
		//通过访问登录地址从返回值中得到有用信息(serverTime等)
		url = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su=" + su
				+ "&rsakt=mod&client=ssologin.js(v1.4.5)" + "&_=" + getServerTime();
		HttpGet getMethod = newHttpGet(url, getHeader,client);

		response = client.execute(getMethod);
		
		HttpEntity entity = response.getEntity();
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			charset = EntityUtils.getContentCharSet(entity);
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is));
			line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		}

		System.out.println("请求登录页面服务器返回信息:"+sb);
		String jsonBody = StringUtils.substringBetween(sb.toString(), "(", ")");

		String nonce = "";
		long servertime = 0L;
		String rsakv = "";
		nonce = StringUtils.substringBetween(jsonBody, "nonce\":\"", "\"");
		rsakv = StringUtils.substringBetween(jsonBody, "rsakv\":\"", "\"");
		servertime = Long.parseLong(StringUtils.substringBetween(jsonBody, "servertime\":", ","));
		//释放连接
		getMethod.releaseConnection();
		
		url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)";//新浪通行证主页面
		HttpPost postMethod2Logon = newHttpPost(url,postHeader,client);
		
		String pwdString = servertime + "\t" + nonce + "\n" + pwd;
		String sp = "";
		try {
			sp = new BigIntegerRSA().rsaCrypt(SINA_PK, "10001", pwdString);
		} catch (InvalidKeyException e) {
			logger.error("AES加密密钥大于128！", e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e);
		} catch (BadPaddingException e) {
			logger.error(e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		} catch (InvalidKeySpecException e) {
			logger.error(e);
		} catch (NoSuchPaddingException e) {
			logger.error(e);
		}
		// SinaSSOEncoder sso = new SinaSSOEncoder();
		// String sp = sso.encode(account.getPasswd(), servertime, nonce);
		logger.info("su=" + su);//用户名
		logger.info("servertime=" + servertime);
		logger.info("nonce=" + nonce);
		logger.info("sp=" + sp);//加密后的密码
		logger.info("su=" + su + "&servertime=" + servertime + "&nonce=" + nonce + "&sp=" + sp);
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("entry", "weibo"));
		nvps.add(new BasicNameValuePair("gateway", "1"));
		nvps.add(new BasicNameValuePair("from", ""));
		nvps.add(new BasicNameValuePair("savestate", "7"));
		nvps.add(new BasicNameValuePair("useticket", "1"));
		nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
		nvps.add(new BasicNameValuePair("useticket", "1"));
		nvps.add(new BasicNameValuePair("vsnf", "1"));
		nvps.add(new BasicNameValuePair("vsnval", ""));
		nvps.add(new BasicNameValuePair("su", su));
		nvps.add(new BasicNameValuePair("service", "miniblog"));
		nvps.add(new BasicNameValuePair("servertime", servertime + ""));
		nvps.add(new BasicNameValuePair("nonce", nonce));
		nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
		nvps.add(new BasicNameValuePair("rsakv", rsakv));
		nvps.add(new BasicNameValuePair("sp", sp));
		nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
		nvps.add(new BasicNameValuePair("prelt", "115"));
		nvps.add(new BasicNameValuePair("url",
				"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
		nvps.add(new BasicNameValuePair("returntype", "META"));
		postMethod2Logon.setEntity(new UrlEncodedFormEntity(nvps));
		response = client.execute(postMethod2Logon);
		System.out.println("模拟登录时服务器返回状态:"+response.toString());
		entity = response.getEntity();
		// 输出页面内容
		if (entity != null) {
			charset = EntityUtils.getContentCharSet(entity);
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is));
			line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		}
		System.out.println("登录后服务器返回信息:"+sb);
		postMethod2Logon.releaseConnection();//释放连接	
		System.out.println("--------------------------------------------------------------------------");
		
		//释放连接
		getMethod.releaseConnection();
		System.out.println("--------------------------------------------------------------------------");
		

		return client;
	}
	
	// 用户名编码
	@SuppressWarnings("deprecation")
	/**
	 * 用户名也需要经过处理
	 * */
	private String encodeAccount(String account) {
		return (new sun.misc.BASE64Encoder()).encode(URLEncoder.encode(account).getBytes());
	}
	
	// servertime的产生（不用）
	public String getServerTime() {
		long servertime = new Date().getTime() / 1000;
		return String.valueOf(servertime);
	}


}
