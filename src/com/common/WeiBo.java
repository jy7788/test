package com.common;

import java.io.IOException;


public abstract class WeiBo extends basicYuQing{

	/**
	 * ��Ҫ��΢����Ψһ��ʶ��mid
	 * @param userName
	 * @param pwd
	 * @param url
	 * @param mid
	 * @param content
	 * @throws IOException
	 */
	public abstract void comment(String userName,String pwd,String url,String mid,String content)throws IOException ;
	
	/**
	 * ͨ��url��mid��������
	 * @param userName
	 * @param pwd
	 * @param url
	 * @param content
	 * @throws IOException
	 */
	public abstract void comment(String userName,String pwd,String url,String content)throws IOException ;
	
	public abstract void addlike(String userName,String pwd,String url,String mid)throws IOException;
	
	public abstract void addlike(String userName,String pwd,String url)throws IOException;
	
	public abstract void forward(String userName,String pwd,String url,String mid,String content)throws IOException;
	
	public abstract void forward(String userName,String pwd,String url,String content)throws IOException;
}
