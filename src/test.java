
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DZ 登录与发贴 实例
 * 
 * @author Administrator aizili.com创建人
 * 
 */
public class test extends Thread{
	static final String domainurl = "http://www.aizili.com/";	//原始地址
	static final String loginurl = "http://www.aizili.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes";//登录地址
	static final String loginUsername = "username"; // 登录用户名
	static final String loginPassword = "password"; // 密码
	
	static final String username = "admin"; // 登录用户名(自己改)
	static final String password = "123456"; // 登录密码(自己改)

	/**
	 * 状态码对应 HttpServletResponse 的常量详细描述
	 * 
	 * @author Administrator
	 * @time 2012-4-8 12:24
	 */
	static class HttpStatus {
		static int SC_MOVED_TEMPORARILY = 301; // 页面已经永久移到另外一个新地址
		static int SC_MOVED_PERMANENTLY = 302; // 页面暂时移动到另外一个新的地址
		static int SC_SEE_OTHER = 303; // 客户端请求的地址必须通过另外的 URL 来访问
		static int SC_TEMPORARY_REDIRECT = 307; // 页面暂时移动到另外一个新的地址
	}

	/**
	 * 获取 formhash 值value
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String getFormhash(String url,DefaultHttpClient httpclient) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);

		
		HttpEntity entity = response.getEntity();
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			String charset = EntityUtils.getContentCharSet(entity);
			InputStream is = entity.getContent();
			sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		}
		
		
		int pos = sb.indexOf("name=\"formhash\" value=");

		// 找出这个 formhash 的内容，这是登录用的 formhash
		String login_formhash = sb.substring(pos + 23, pos + 23 + 8);
		return login_formhash;
	}
	
	/**
	 * 登录
	 * @param httpclient	
	 * @param user	用户名
	 * @param pass	密码
	 * @param formhash	提交的表单formhash值
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean logicDz(DefaultHttpClient httpclient,String formhash) throws ClientProtocolException, IOException{
		/* 创建post连接 */
		HttpPost httpPost = new HttpPost(loginurl);
		
		/* 创建登录条件 */
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("formhash", formhash));
		
		/* 添加到httpPost提交的内容中 */
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		/*执行并打印登录后内容显示情况*/
//		printHttpGet(httpclient.execute(httpPost));
		
		HttpResponse response = httpclient.execute(httpPost);//不打印登录情况
		
		/*判断登录是否成功*/
		HttpEntity entity = response.getEntity();
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			String charset = EntityUtils.getContentCharSet(entity);
			InputStream is = entity.getContent();
			sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\t\n");
			}
			is.close();
		}
		
		
		if(sb.indexOf("title=\"访问我的空间\"") != -1){
			int pos = sb.indexOf("title=\"访问我的空间\"");
			String username =sb.substring(pos+15, pos+50);
			username = username.substring(0, username.indexOf("<", 1));
			
			System.out.println("登录时的 用户名为："+username);
			System.out.println("#################################   登录成功   ############################");
			return true;
		}else{
			return false;
		}
		
		/*释放资源*/
//		httpPost.abort();
	}
	
	/**
	 * 发贴 成功后返回页面内容
	 * @param httpclient
	 * @param url
	 * @param message
	 * @param subject
	 * @param login_formhash
	 * @return HttpResponse
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse postMessage(DefaultHttpClient httpclient,String url,String message,String subject,String login_formhash) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("message", message));			//内容
		nvps.add(new BasicNameValuePair("subject", subject));			//标题
		nvps.add(new BasicNameValuePair("formhash", login_formhash));	//提交form的hash值(防外提交form的)
		
		/*以下的可以不设置，看了一下论坛中，这两个都有值的*/
		nvps.add(new BasicNameValuePair("allownoticeauthor", "1"));
		nvps.add(new BasicNameValuePair("wysiwyg", "1"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		response = httpclient.execute(httpPost);
		
		/*释放资源*/
		httpPost.abort();
		return response;
	}
	
	/**
	 * 回复
	 * @param httpclient
	 * @param httpost
	 * @param message
	 * @param subject
	 * @param login_formhash
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse postReMessage(DefaultHttpClient httpclient,String url,String message,String subject,String login_formhash) throws ClientProtocolException, IOException{
		HttpResponse response = null;
		
		/*提交的url，需要加上domainurl的地址*/
		url = getReMessageUrl(url,httpclient);
		System.out.println("回复提交表单地址  url="+url);
		HttpPost httpPost = new HttpPost(url);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("message", message));			//内容
		nvps.add(new BasicNameValuePair("subject", subject));			//标题
		nvps.add(new BasicNameValuePair("formhash", login_formhash));	//提交form的hash值(防外提交form的)
		
		/*以下的可以不设置，看了一下论坛中，这两个都有值的*/
		nvps.add(new BasicNameValuePair("allownoticeauthor", "1"));
		nvps.add(new BasicNameValuePair("wysiwyg", "1"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		response = httpclient.execute(httpPost);
		
		/* 释放资源 */
		httpPost.abort();
		return response;
	} 
	
	/**
	 * 获取回复的url
	 * @param url
	 * @param httpclient
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getReMessageUrl(String url,DefaultHttpClient httpclient) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
//		 Invalid use of SingleClientConnManager: connection still allocated.
		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity entity = response.getEntity();
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			String charset = EntityUtils.getContentCharSet(entity);
			InputStream is = entity.getContent();
			sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\t\n");
			}
			is.close();
		}
		int pos = sb.indexOf("id=\"fastpostform\" action=");
		int pos_end = sb.indexOf("fastpost\"");
		
		System.out.println(sb.length()+"  pos="+pos+"  pos_end="+pos_end);
		// 找出这个 reMessageUrl 的内容
		String reMessageUrl = domainurl + sb.substring(pos+26, pos_end+8);
		
		//去除amp;
		reMessageUrl = reMessageUrl.replaceAll("amp;", "");
		
		//释放资源
		httpGet.abort();
		return reMessageUrl;
	}
	
	/**
	 * 获取重定向的url
	 * @param httpclient
	 * @param response
	 * @return 返回url地址
	 */
	public String redirectHttp(DefaultHttpClient httpclient,HttpResponse response) {
		Header header = response.getFirstHeader("location");
		String urlRedirect = "";
		if(!header.getValue().contains(domainurl)){
			urlRedirect = domainurl+header.getValue();
		} else {
			urlRedirect = header.getValue();
		}

		return urlRedirect;
	}
	
	/**
	 * 根据HttpResponse对象 打印页面内容
	 * @param response
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void printHttpGet(HttpResponse response) throws IllegalStateException, IOException{
		HttpEntity entity = response.getEntity();
		
		StringBuffer sb = null;
		// 输出页面内容
		if (entity != null) {
			String charset = EntityUtils.getContentCharSet(entity);
			InputStream is = entity.getContent();
			sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\t\n");
			}
			is.close();
			System.out.println(sb.toString());
		}
		
	}
	
	/**
	 * 主函数 main()
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String args[]) throws IOException, InterruptedException {
		test test = new test();
		
//		connectionManager
		

		DefaultHttpClient httpclient = new DefaultHttpClient();// 得到httpclient实例

		/* 登录论坛 */
		System.out.println("#################################   开始登录   ############################");
		String login_formhash = test.getFormhash(domainurl,httpclient);//获取formhash
		if(!test.logicDz(httpclient,login_formhash)){
			System.out.println("#################################   登录失败   ############################");
			return;
		}
		
		
		/* 开发发贴 */
		System.out.println("#################################   开发发贴   ############################");
		String url = null;		//发贴的url
		String message = null;	//发贴的内容
		String subject = null;	//发贴的标题
		
		
//		/* 这个就是发贴机了  自己去加吧,可以用线程多个一起发，不用等  其中fid不同而已，可从1开始到50*/
//		for(int i=0;i<1;i++){
//			/* 发贴参数 */
//			url = "http://www.aizili.com/forum.php?mod=post&action=newthread&fid=24&extra=&topicsubmit=yes";
//			message = "最近组建创业团队，搞IT行业，欢迎加入，请留下你的联系方式";
//			subject = "最近组建创业团队，搞IT行业，欢迎加入";
//			login_formhash = test.getFormhash(url,httpclient);
//			
//			/* 调用发贴方法 四个参数，其中最后一个为form提交的hash值 */
//			HttpResponse response = test.postMessage(httpclient, url, message, subject, login_formhash);
//			/* 测试看看内容 */
//			System.out.println("#################################   发贴完成后 内容如下   ############################");
//			test.printHttpGet(response);	//打印发贴后的页面看看
//			
//			System.out.println("#################################   重定向跳转页面   ############################");
//			/* 获取重定向标识码 */
//			int statuscode = response.getStatusLine().getStatusCode();
//			if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
//					|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
//					|| (statuscode == HttpStatus.SC_SEE_OTHER)
//					|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
//				System.out.println("#################################   开始发表回复   ############################");
//				/* 发表回复 */
//				url = test.redirectHttp(httpclient, response);
//				message = "最好介绍一下自己，我会选择性的加大家的，最好加上联系的QQ之类的";
//				subject = "最近组建创业团队，搞IT行业，欢迎加入";
//				login_formhash = test.getFormhash(url,httpclient);
//				
//				for(int j=0;j<1;j++){
//					//可以多次对一个帖子回复
//				}
//
//				sleep(10000);//等10秒再回复
//				
//				response = test.postReMessage(httpclient,url, message, subject, login_formhash);
//	
//				/*打印页面内容*/
//				test.printHttpGet(response);	
//			}
//		}	
		
		
		url = "http://www.aizili.com/thread-264893-1-1.html";
		message = "这个真不敢乱留联系方式，怕！好怕";
		subject = "最近组建创业团队，搞IT行业，欢迎加入";
		login_formhash = test.getFormhash(url,httpclient);
		for(int i=15;i<20;i++){
			System.out.println(message+" 现在是" + i);
			test.postReMessage(httpclient,url, message, subject, login_formhash);
			sleep(10000);
		}
		
		/* 关闭连接管理器 */
		httpclient.getConnectionManager().shutdown();
	}
}
