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

public class SinaWeiBo {
	
	String url = null;
	HttpResponse response = null;
	private DefaultHttpClient client = new DefaultHttpClient();
	
	void SinaWeiBo(String username,String pwd){
		SinaLogonDog sinaLogonDog = new SinaLogonDog();
		sinaLogonDog.init();
		try {
			client = sinaLogonDog.logonAndValidate(username,pwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinaLogonDog sinaLogonDog = new SinaLogonDog();
		sinaLogonDog.init();
		try {
//			sinaLogonDog.logonAndValidate("1151871470@qq.com", "a901110");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void comment(String url,String username,String pwd){
		if(getCookie()==null){
			SinaLogonDog sinaLogonDog = new SinaLogonDog();
			sinaLogonDog.init();
			try {
				client = sinaLogonDog.logonAndValidate(username,pwd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ΢������
	 * @param mid
	 * @param content
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void commentBlog(String mid,String content) throws ClientProtocolException, IOException{
		url = "http://weibo.com/aj/v6/comment/add";
		HttpPost postMethod2Praise = newHttpPost(url);
		//ƴ�յ�¼��http����
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("content", content));
		nvps.add(new BasicNameValuePair("mid", mid));
		//���ñ���
		postMethod2Praise.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		response = client.execute(postMethod2Praise);
		postMethod2Praise.releaseConnection();
	}
	
	/**
	 * ת��������
	 * @param mid
	 * @param reason
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void forwardBlog(String mid,String reason) throws ClientProtocolException, IOException{
		url = "http://weibo.com/aj/v6/mblog/forward";
		HttpPost postMethod2Praise = newHttpPost(url);
		//ƴ�յ�¼��http����
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("reason", reason));
		nvps.add(new BasicNameValuePair("mid", mid));
		//���ñ���
		postMethod2Praise.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		response = client.execute(postMethod2Praise);
		postMethod2Praise.releaseConnection();
	}
	
	/**
	 * ΢�����޻�ȡ�����޵Ĳ���
	 * @param mid
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void addOrRemoveLikeBlog(String mid) throws ClientProtocolException, IOException{
		url = "http://weibo.com/aj/v6/like/add?ajwvr=6";
		HttpPost postMethod2Praise = newHttpPost(url);
		//ƴ�յ�¼��http����
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("version", "mini"));
		nvps.add(new BasicNameValuePair("qid", "heart"));
		nvps.add(new BasicNameValuePair("mid", mid));
		nvps.add(new BasicNameValuePair("like_src", "1"));
		nvps.add(new BasicNameValuePair("location", "v6_content_home"));
		postMethod2Praise.setEntity(new UrlEncodedFormEntity(nvps));
		response = client.execute(postMethod2Praise);
		postMethod2Praise.releaseConnection();
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
	 * ����HttpGet��ȡҳ��html�����н�����΢��id
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
		return get(url,"mid=\\\"","\\\"");
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

	public DefaultHttpClient getClient() {
		return client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}

}
