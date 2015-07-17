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
//			String mid = sinaLogonDog.getMid(url_,"mid=\\\"","\\\"");//��ȡ΢�����
			//΢������
//			sinaLogonDog.addOrRemoveLikeBlog(mid);
			String str = "������������" ;
			//΢��ת��
//			sinaLogonDog.forwardBlog(mid,str);
			//΢������
//			sinaLogonDog.commentBlog(mid, str);
			//��������
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
		//referer�����ú���Ҫ
		postMethod.setHeader("Referer", "http://comment5.news.sina.com.cn/comment/skin/default.html");
//		postMethod.setHeader("Host", "comment5.news.sina.com.cn");
//		postMethod.setHeader("Origin", "http://comment5.news.sina.com.cn");
		//ƴ�յ�¼��http����
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("channel", "gn"));
		nvps.add(new BasicNameValuePair("newsid", newsid));
		nvps.add(new BasicNameValuePair("parent", parent));
		nvps.add(new BasicNameValuePair("format", "js"));
		nvps.add(new BasicNameValuePair("vote", "1"));
		nvps.add(new BasicNameValuePair("callback", "function (o){}"));
		nvps.add(new BasicNameValuePair("domain", "sina.com.cn"));
		//���ñ���
		postMethod.setEntity(new UrlEncodedFormEntity(nvps));
		response = client.execute(postMethod);
		System.out.println("���ۺ󷵻�״̬:"+response.toString());
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
		//ƴ�յ�¼��http����
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
		//���ñ���
		postMethod.setEntity(new UrlEncodedFormEntity(nvps,"gbk"));
		response = client.execute(postMethod);
		System.out.println("���ۺ󷵻�״̬:"+response.toString());
		postMethod.releaseConnection();
	}
	
	/**
	 * ��html�з�����midֵ
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
		//��ѹ������htmlҳ����н�ѹ
		Header ceheader = entity.getContentEncoding();  
		if (ceheader != null) {  
			for (HeaderElement element : ceheader.getElements()) {  
				if (element.getName().equalsIgnoreCase("gzip")) {  
					response.setEntity(new GzipDecompressingEntity(response.getEntity()));
				}
			}
		}
		entity = response.getEntity();
		// ���ҳ������
		if (entity != null) {
			is = entity.getContent();
			sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(is));
			line = null;
			while ((line = br.readLine()) != null) {
				if(line.indexOf(beg)>0){
					//�ͷ�����
					getMethod.releaseConnection();
					//return StringUtils.substringBetween(line,"mid=\\\"","\\\"");
					return StringUtils.substringBetween(line,beg,end);
				}
				sb.append(line);
			}
			is.close();
		}
		System.out.println("������Ϣ:"+sb);

		//�ͷ�����
		getMethod.releaseConnection();
		return null;
	}
	
	/**
	 * ����HttpGet��ȡҳ��html�����н���������id
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
		System.out.println("ģ�����urlʱ����������״̬:"+response.toString());
		HttpEntity entity = response.getEntity();
		
		StringBuffer sb = null;
		// ���ҳ������
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
		System.out.println("ģ�����urlʱ������������Ϣ:"+sb);
		//�ض���
		String tmp = StringUtils.substringBetween(sb.toString(), "(", ")");
		url = tmp.substring(1,tmp.length()-1);
		System.out.println("�ض����ַ:"+url);		
		return get(url,"gn:","\"");
	}
	
	/*
	 * �½�httppost��������headerαװһ��
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
	 * ��DefaultHttpClient�л�ȡ
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
		//αװ�������
		getMethod.setHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; CIBA; MAXTHON 2.0)");
		getMethod.setHeader("Connection", "Keep-Alive");
		getMethod.setHeader("Cache-Control", "no-cache");
		if(getCookie()!=null)
			getMethod.setHeader("Cookie", getCookie());
		return getMethod;
	}

}
