package com.xinlang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

public class SinaNews {

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
//			String newsId = sinaLogonDog.getNewsId(url_,"gn:","\"");
//			String mid = sinaLogonDog.getMid(url_,"mid=\\\"","\\\"");//获取微博编号
			//微博点赞
//			sinaLogonDog.addOrRemoveLikeBlog(mid);
			String str = "心已死。。。" ;
			//微博转发
//			sinaLogonDog.forwardBlog(mid,str);
			//微博评论
//			sinaLogonDog.commentBlog(mid, str);
			//新闻评论
//			sinaLogonDog.commentNews(newsId, str,url_);
//				sinaLogonDog.addLikeNews("1-1-32087773", "559DDF4C-CA667E15-92511948-889-926");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void addLikeNews(String newsid,String parent) throws ClientProtocolException, IOException{
		url = "http://comment5.news.sina.com.cn/cmnt/vote";
		HttpPost postMethod = new HttpPost(url);
		postMethod
				.setHeader(
						"Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		postMethod.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
		postMethod.setHeader("Accept-Encoding", "gzip, deflate");
		postMethod
				.setHeader(
						"User-Agent",
						//"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; InfoPath.2)");
						"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
		postMethod.setHeader("Connection", "Keep-Alive");
		postMethod.setHeader("Cache-Control", "no-cache");
		//referer的设置很重要
		postMethod.setHeader("Referer", "http://comment5.news.sina.com.cn/comment/skin/default.html");
//		postMethod.setHeader("Host", "comment5.news.sina.com.cn");
//		postMethod.setHeader("Origin", "http://comment5.news.sina.com.cn");
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("channel", "gn"));
		nvps.add(new BasicNameValuePair("newsid", newsid));
		nvps.add(new BasicNameValuePair("parent", parent));
		nvps.add(new BasicNameValuePair("format", "js"));
		nvps.add(new BasicNameValuePair("vote", "1"));
		nvps.add(new BasicNameValuePair("callback", "function (o){}"));
		nvps.add(new BasicNameValuePair("domain", "sina.com.cn"));
		//设置编码
		postMethod.setEntity(new UrlEncodedFormEntity(nvps));
		response = client.execute(postMethod);
		System.out.println("评论后返回状态:"+response.toString());
		postMethod.releaseConnection();
	}
	
	public void commentNews(String newsid,String content,String referer) throws ClientProtocolException, IOException{
		url = "http://comment5.news.sina.com.cn/cmnt/submit";
		HttpPost postMethod = new HttpPost(url);
		postMethod
				.setHeader(
						"Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		postMethod.setHeader("Referer", referer);
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
		//拼凑登录的http请求
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("channel", "cj"));
		nvps.add(new BasicNameValuePair("newsid", newsid));
		nvps.add(new BasicNameValuePair("parent", ""));
		nvps.add(new BasicNameValuePair("content", content));
		nvps.add(new BasicNameValuePair("format", "json"));
		nvps.add(new BasicNameValuePair("ie", "gbk"));
		nvps.add(new BasicNameValuePair("oe", "gbk"));
		nvps.add(new BasicNameValuePair("ispost", ""));
		//nvps.add(new BasicNameValuePair("share_url", referer));
		nvps.add(new BasicNameValuePair("video_url", ""));
		nvps.add(new BasicNameValuePair("img_url", ""));
		nvps.add(new BasicNameValuePair("iframe", "1"));
		//设置编码
		postMethod.setEntity(new UrlEncodedFormEntity(nvps,"gbk"));
		response = client.execute(postMethod);
		System.out.println("评论后返回状态:"+response.toString());
		postMethod.releaseConnection();
	}
	
	/**
	 * 从html中分析出mid值
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String get(String url,String beg,String end) throws ClientProtocolException, IOException{
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		StringBuffer sb =null;
		HttpGet getMethod = newHttpGet(url);
		response = client.execute(getMethod);
		System.out.println(response.toString());
		HttpEntity entity = response.getEntity();
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
		System.out.println("返回信息:"+sb);

		//释放连接
		getMethod.releaseConnection();
		return null;
	}
	
	/**
	 * 利用HttpGet获取页面html并从中解析出新闻id
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getMid(String url) throws ClientProtocolException, IOException{
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		HttpGet getMethod = newHttpGet(url);

		response = client.execute(getMethod);
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
		return get(url,"gn:","\"");
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
