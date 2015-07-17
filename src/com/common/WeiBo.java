package com.common;

import java.io.IOException;


public abstract class WeiBo extends basicYuQing{

	/**
	 * 需要传微博的唯一标识符mid
	 * @param userName
	 * @param pwd
	 * @param url
	 * @param mid
	 * @param content
	 * @throws IOException
	 */
	public abstract void comment(String userName,String pwd,String url,String mid,String content)throws IOException ;
	
	/**
	 * 通过url将mid解析出来
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
