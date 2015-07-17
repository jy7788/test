package com.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class SinaWeiBo extends WeiBo{

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
	public SinaWeiBo(){
		client = new DefaultHttpClient();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void comment(String userName, String pwd,String url, String mid,
			String content) throws IOException {
		// TODO Auto-generated method stub
		client = login(userName,pwd);
		//获取微博页面
		get(url);
		
		String url_ = "http://weibo.com/aj/v6/comment/add";
			HttpPost postMethod = newHttpPost(url_,postHeader,client);
			
			//拼凑登录的http请求
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("mid", mid));
			//设置编码
			postMethod.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
			HttpResponse response = client.execute(postMethod);
			System.out.println(response.toString());
			postMethod.releaseConnection();	
	}

	@Override
	public void addlike(String userName, String pwd,String url, String mid) throws IOException {
		// TODO Auto-generated method stub
		client = login(userName,pwd);
		get(url);
		
		String url_ = "http://weibo.com/aj/v6/like/add?ajwvr=6";
		HttpPost postMethod = newHttpPost(url_,postHeader,client);
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("version", "mini"));
		nvps.add(new BasicNameValuePair("qid", "heart"));
		nvps.add(new BasicNameValuePair("mid", mid));
		nvps.add(new BasicNameValuePair("like_src", "1"));
		nvps.add(new BasicNameValuePair("location", "v6_content_home"));
		postMethod.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = client.execute(postMethod);
		System.out.println(response.toString());
		postMethod.releaseConnection();
	}

	@Override
	public void forward(String userName, String pwd,String url, String mid,
			String content) throws IOException {
		// TODO Auto-generated method stub
		client = login(userName,pwd);
		get(url);
		
		String url_ = "http://weibo.com/aj/v6/mblog/forward";
		HttpPost postMethod = newHttpPost(url_,postHeader,client);
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("reason", content));
		nvps.add(new BasicNameValuePair("mid", mid));
		postMethod.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		HttpResponse response = client.execute(postMethod);
		System.out.println(response.toString());
		postMethod.releaseConnection();
	}
	
	@Override
	public void comment(String userName, String pwd, String url, String content)
			throws IOException {
		// TODO Auto-generated method stub

		client = login(userName,pwd);
		String mid = getMid(url,"mid=\\\"","\\\"");
		String url_ = "http://weibo.com/aj/v6/comment/add";
			HttpPost postMethod = newHttpPost(url_,postHeader,client);
			
			//拼凑登录的http请求
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("mid", mid));
			//设置编码
			postMethod.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
			HttpResponse response = client.execute(postMethod);
			System.out.println(response.toString());
			postMethod.releaseConnection();	
	}

	@Override
	public void addlike(String userName, String pwd, String url)
			throws IOException {
		// TODO Auto-generated method stub
		client = login(userName,pwd);
		String mid = getMid(url,"mid=\\\"","\\\"");
		
		String url_ = "http://weibo.com/aj/v6/like/add?ajwvr=6";
		HttpPost postMethod = newHttpPost(url_,postHeader,client);
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("version", "mini"));
		nvps.add(new BasicNameValuePair("qid", "heart"));
		nvps.add(new BasicNameValuePair("mid", mid));
		nvps.add(new BasicNameValuePair("like_src", "1"));
		nvps.add(new BasicNameValuePair("location", "v6_content_home"));
		postMethod.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = client.execute(postMethod);
		System.out.println(response.toString());
		postMethod.releaseConnection();
	}

	@Override
	public void forward(String userName, String pwd, String url, String content)
			throws IOException {
		// TODO Auto-generated method stub
		client = login(userName,pwd);
		String mid = getMid(url,"mid=\\\"","\\\"");
		
		String url_ = "http://weibo.com/aj/v6/mblog/forward";
		HttpPost postMethod = newHttpPost(url_,postHeader,client);
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("reason", content));
		nvps.add(new BasicNameValuePair("mid", mid));
		postMethod.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		HttpResponse response = client.execute(postMethod);
		System.out.println(response.toString());
		postMethod.releaseConnection();
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

	public void get(String url) throws ClientProtocolException, IOException{
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		HttpGet getMethod = newHttpGet(url,getHeader,client);
		HttpResponse response = client.execute(getMethod);
		System.out.println("模拟访问url时服务器返回状态:"+response.toString());
		HttpEntity entity = response.getEntity();
		
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is,"gbk"));
			line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		}
		System.out.println("模拟访问url时服务器返回信息:"+sb);
		//重定向
		String tmp = StringUtils.substringBetween(sb.toString(), "(", ")");
		url = tmp.substring(1,tmp.length()-1);
		System.out.println("重定向地址:"+url);
		getMethod = newHttpGet(url,getHeader,client);
		response = client.execute(getMethod);
		getMethod.releaseConnection();
	}

	/**
	 * 利用HttpGet获取页面html并从中解析出微博id或者新闻id
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getMid(String url,String beg,String end) throws ClientProtocolException, IOException{
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		HttpGet getMethod = newHttpGet(url,getHeader,client);

		HttpResponse response = client.execute(getMethod);
		System.out.println("获取mid时服务器返回状态:"+response.toString());
		HttpEntity entity = response.getEntity();
		
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is,"gbk"));
			line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		}
		System.out.println("获取mid时服务器返回html信息:"+sb);
		//重定向
		String tmp = StringUtils.substringBetween(sb.toString(), "(", ")");
		url = tmp.substring(1,tmp.length()-1);
		System.out.println("重定向地址:"+url);		
		
		getMethod = newHttpGet(url,getHeader,client);
		response = client.execute(getMethod);
		System.out.println("获取mid重定向时服务器返回状态："+response.toString());
		entity = response.getEntity();
		//对压缩过的html页面进行解压
		Header ceheader = entity.getContentEncoding();  
		if (ceheader != null) {  
			for (HeaderElement element : ceheader.getElements()) {  
				if (element.getName().equalsIgnoreCase("gzip")) {  
					response.setEntity(new GzipDecompressingEntity(response.getEntity()));
				}
			}
		}
		entity = response.getEntity();
		// 输出页面内容
		if (entity != null) {
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is));
			line = null;
			while ((line = br.readLine()) != null) {
				if(line.indexOf(beg)>0){
					//释放连接
					getMethod.releaseConnection();
					//return StringUtils.substringBetween(line,"mid=\\\"","\\\"");
					return StringUtils.substringBetween(line,beg,end);
				}
				sb.append(line);
			}
			is.close();
		}
		System.out.println("获取mid失败返回页面信息:"+sb);

		//释放连接
		getMethod.releaseConnection();
		return null;
	}
	
}
