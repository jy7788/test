package com.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class basicYuQing {

	protected abstract DefaultHttpClient login(String userName,String pwd) throws  IOException ;
	
	public HttpGet newHttpGet(String url,Map<String,String> map,DefaultHttpClient client){
		HttpGet httpGet = new HttpGet(url);
		for(Map.Entry<String, String> entry:map.entrySet()){
			httpGet.setHeader(entry.getKey(), entry.getValue());
		}
		if(getCookie(client)!=null){
			httpGet.setHeader("Cookie", getCookie(client));
		}
		return httpGet;
	}
	
	public HttpPost newHttpPost(String url,Map<String,String> map,DefaultHttpClient client){
		HttpPost httpPost = new HttpPost(url);
		for(Map.Entry<String, String> entry:map.entrySet()){
			httpPost.setHeader(entry.getKey(), entry.getValue());
		}
		if(getCookie(client)!=null){
			httpPost.setHeader("Cookie", getCookie(client));
		}
		return httpPost;
	}
	
	/*
	 * 从DefaultHttpClient中获取
	 */
	public String getCookie(DefaultHttpClient client){
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
}
