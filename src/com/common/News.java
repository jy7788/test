package com.common;

public abstract class News extends basicYuQing{

	protected abstract void comment(String userName,String pwd,String url,String mid,String content);
	
	/**
	 * ����֧��
	 * @param url
	 * @param mid
	 */
	public abstract void support(String url,String mid);
	/**
	 * ʵ��֧��
	 * @param userName
	 * @param pwd
	 * @param url
	 * @param mid
	 */
	public abstract void support(String userName,String pwd,String url,String mid);
}
