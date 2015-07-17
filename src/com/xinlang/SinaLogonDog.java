package com.xinlang;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpClientConnection;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


/**
 * 新浪通行证主页面:http://login.sina.com.cn/member/my.php?entry=sso
 * 微博主页面:http://weibo.com(有重定向)
 * @author jy7788
 *
 */
public class SinaLogonDog {
	
	private static final Log logger = LogFactory.getLog(SinaLogonDog.class);
	
	static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
		+ "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
		+ "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
		+ "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";  //新浪微博的加密公钥
	String url = null;
	HttpResponse response = null;
	DefaultHttpClient client = new DefaultHttpClient();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinaLogonDog sinaLogonDog = new SinaLogonDog();
		sinaLogonDog.init();
		try {
			sinaLogonDog.logonAndValidate("1151871470@qq.com", "a901110");
			sinaLogonDog.logonAndValidate("273775401@qq.com", "h1987614");
			String url_ = "http://comment5.news.sina.com.cn/page/info?format=js&channel=cj&newsid=31-1-17720790&group=0&compress=1&ie=gbk&oe=gbk&page=1&page_size=100&jsvar=requestId_29902459";			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public DefaultHttpClient logonAndValidate(String userName,String pwd) throws  IOException {
		String charset = null;
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		String su = encodeAccount(userName);
		//通过访问登录地址从返回值中得到有用信息(serverTime等)
		url = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su=" + su
				+ "&rsakt=mod&client=ssologin.js(v1.4.5)" + "&_=" + getServerTime();
		HttpGet getMethod = newHttpGet(url);

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
		HttpPost postMethod2Logon = newHttpPost(url);
		
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
	

	/*
	 * 初始化httpclient对象
	 */
	public void init(){
		client = new DefaultHttpClient();
		//设置代理
//		client.getHostConfiguration().setProxy("136.0.16.217", 7808);
		//抢先认证
//		client.getParams().setAuthenticationPreemptive(true);
		//client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	}
	
	// inputstream转化为String类型
	public static String inputStreamToString(InputStream is, String charset) throws IOException {
		int i = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		if (null == charset) {
			return baos.toString();
		} else {
			return baos.toString(charset);
		}
	}

	/*
	 * 新建httppost对象并设置header伪装一下
	 */
	protected HttpPost newHttpPost(String url) {
		HttpPost postMethod = new HttpPost(url);
		postMethod
				.setHeader(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		postMethod.setHeader("Referer", "http://weibo.com/");
		postMethod.setHeader("Accept-Language", "zh-cn");
		postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
		postMethod.setHeader("Accept-Encoding", "gzip, deflate");
		postMethod
				.setHeader(
						"User-Agent",
						//"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; InfoPath.2)");
						"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
		postMethod.setHeader("Connection", "Keep-Alive");
		postMethod.setHeader("Cache-Control", "no-cache");
		if(getCookie()!=null)
			postMethod.setHeader("Cookie", getCookie());
		return postMethod;
	}

	public static String decode(String string) {
		return string.replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%3D", "=")
				.replaceAll("%26", "&");
	}

	// 用户名编码
	@SuppressWarnings("deprecation")
	/**
	 * 用户名也需要经过处理
	 * */
	private String encodeAccount(String account) {
		return (new sun.misc.BASE64Encoder()).encode(URLEncoder.encode(account).getBytes());
	}

	// 六位随机数nonce的产生（不用）
	public String makeNonce(int len) {
		String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str = "";
		for (int i = 0; i < len; i++) {
			str += x.charAt((int) (Math.ceil(Math.random() * 1000000) % x.length()));
		}
		return str;
	}

	// servertime的产生（不用）
	public String getServerTime() {
		long servertime = new Date().getTime() / 1000;
		return String.valueOf(servertime);
	}
	/*
	 * 从DefaultHttpClient中获取
	 */
	public String getCookie(){
		CookieStore cs = client.getCookieStore();
		StringBuffer cookieStr = new StringBuffer();;
		List<Cookie> list = cs.getCookies();
		for (Cookie cookie : list) {
			cookieStr.append(cookie.getName() + "="
					+ cookie.getValue() + "; ");
		}
		if(cookieStr!=null)
			return cookieStr.toString();
		else
			return null;
	}
	
	protected HttpGet newHttpGet(String url) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.setHeader("Accept","image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		getMethod.setHeader("Accept-Language", "zh-CN");
		getMethod.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		getMethod.setHeader("Accept-Encoding", "deflate");
		//伪装成浏览器
		getMethod.setHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; CIBA; MAXTHON 2.0)");
		getMethod.setHeader("Connection", "Keep-Alive");
		getMethod.setHeader("Cache-Control", "no-cache");
		if(getCookie()!=null)
			getMethod.setHeader("Cookie", getCookie());
		return getMethod;
	}
}

