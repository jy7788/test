
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
 * DZ ��¼�뷢�� ʵ��
 * 
 * @author Administrator aizili.com������
 * 
 */
public class test extends Thread{
	static final String domainurl = "http://www.aizili.com/";	//ԭʼ��ַ
	static final String loginurl = "http://www.aizili.com/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes";//��¼��ַ
	static final String loginUsername = "username"; // ��¼�û���
	static final String loginPassword = "password"; // ����
	
	static final String username = "admin"; // ��¼�û���(�Լ���)
	static final String password = "123456"; // ��¼����(�Լ���)

	/**
	 * ״̬���Ӧ HttpServletResponse �ĳ�����ϸ����
	 * 
	 * @author Administrator
	 * @time 2012-4-8 12:24
	 */
	static class HttpStatus {
		static int SC_MOVED_TEMPORARILY = 301; // ҳ���Ѿ������Ƶ�����һ���µ�ַ
		static int SC_MOVED_PERMANENTLY = 302; // ҳ����ʱ�ƶ�������һ���µĵ�ַ
		static int SC_SEE_OTHER = 303; // �ͻ�������ĵ�ַ����ͨ������� URL ������
		static int SC_TEMPORARY_REDIRECT = 307; // ҳ����ʱ�ƶ�������һ���µĵ�ַ
	}

	/**
	 * ��ȡ formhash ֵvalue
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
		// ���ҳ������
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

		// �ҳ���� formhash �����ݣ����ǵ�¼�õ� formhash
		String login_formhash = sb.substring(pos + 23, pos + 23 + 8);
		return login_formhash;
	}
	
	/**
	 * ��¼
	 * @param httpclient	
	 * @param user	�û���
	 * @param pass	����
	 * @param formhash	�ύ�ı�formhashֵ
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean logicDz(DefaultHttpClient httpclient,String formhash) throws ClientProtocolException, IOException{
		/* ����post���� */
		HttpPost httpPost = new HttpPost(loginurl);
		
		/* ������¼���� */
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("formhash", formhash));
		
		/* ��ӵ�httpPost�ύ�������� */
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		/*ִ�в���ӡ��¼��������ʾ���*/
//		printHttpGet(httpclient.execute(httpPost));
		
		HttpResponse response = httpclient.execute(httpPost);//����ӡ��¼���
		
		/*�жϵ�¼�Ƿ�ɹ�*/
		HttpEntity entity = response.getEntity();
		StringBuffer sb = null;
		// ���ҳ������
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
		
		
		if(sb.indexOf("title=\"�����ҵĿռ�\"") != -1){
			int pos = sb.indexOf("title=\"�����ҵĿռ�\"");
			String username =sb.substring(pos+15, pos+50);
			username = username.substring(0, username.indexOf("<", 1));
			
			System.out.println("��¼ʱ�� �û���Ϊ��"+username);
			System.out.println("#################################   ��¼�ɹ�   ############################");
			return true;
		}else{
			return false;
		}
		
		/*�ͷ���Դ*/
//		httpPost.abort();
	}
	
	/**
	 * ���� �ɹ��󷵻�ҳ������
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
		nvps.add(new BasicNameValuePair("message", message));			//����
		nvps.add(new BasicNameValuePair("subject", subject));			//����
		nvps.add(new BasicNameValuePair("formhash", login_formhash));	//�ύform��hashֵ(�����ύform��)
		
		/*���µĿ��Բ����ã�����һ����̳�У�����������ֵ��*/
		nvps.add(new BasicNameValuePair("allownoticeauthor", "1"));
		nvps.add(new BasicNameValuePair("wysiwyg", "1"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		
		response = httpclient.execute(httpPost);
		
		/*�ͷ���Դ*/
		httpPost.abort();
		return response;
	}
	
	/**
	 * �ظ�
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
		
		/*�ύ��url����Ҫ����domainurl�ĵ�ַ*/
		url = getReMessageUrl(url,httpclient);
		System.out.println("�ظ��ύ����ַ  url="+url);
		HttpPost httpPost = new HttpPost(url);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("message", message));			//����
		nvps.add(new BasicNameValuePair("subject", subject));			//����
		nvps.add(new BasicNameValuePair("formhash", login_formhash));	//�ύform��hashֵ(�����ύform��)
		
		/*���µĿ��Բ����ã�����һ����̳�У�����������ֵ��*/
		nvps.add(new BasicNameValuePair("allownoticeauthor", "1"));
		nvps.add(new BasicNameValuePair("wysiwyg", "1"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "gbk"));
		response = httpclient.execute(httpPost);
		
		/* �ͷ���Դ */
		httpPost.abort();
		return response;
	} 
	
	/**
	 * ��ȡ�ظ���url
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
		// ���ҳ������
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
		// �ҳ���� reMessageUrl ������
		String reMessageUrl = domainurl + sb.substring(pos+26, pos_end+8);
		
		//ȥ��amp;
		reMessageUrl = reMessageUrl.replaceAll("amp;", "");
		
		//�ͷ���Դ
		httpGet.abort();
		return reMessageUrl;
	}
	
	/**
	 * ��ȡ�ض����url
	 * @param httpclient
	 * @param response
	 * @return ����url��ַ
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
	 * ����HttpResponse���� ��ӡҳ������
	 * @param response
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void printHttpGet(HttpResponse response) throws IllegalStateException, IOException{
		HttpEntity entity = response.getEntity();
		
		StringBuffer sb = null;
		// ���ҳ������
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
	 * ������ main()
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String args[]) throws IOException, InterruptedException {
		test test = new test();
		
//		connectionManager
		

		DefaultHttpClient httpclient = new DefaultHttpClient();// �õ�httpclientʵ��

		/* ��¼��̳ */
		System.out.println("#################################   ��ʼ��¼   ############################");
		String login_formhash = test.getFormhash(domainurl,httpclient);//��ȡformhash
		if(!test.logicDz(httpclient,login_formhash)){
			System.out.println("#################################   ��¼ʧ��   ############################");
			return;
		}
		
		
		/* �������� */
		System.out.println("#################################   ��������   ############################");
		String url = null;		//������url
		String message = null;	//����������
		String subject = null;	//�����ı���
		
		
//		/* ������Ƿ�������  �Լ�ȥ�Ӱ�,�������̶߳��һ�𷢣����õ�  ����fid��ͬ���ѣ��ɴ�1��ʼ��50*/
//		for(int i=0;i<1;i++){
//			/* �������� */
//			url = "http://www.aizili.com/forum.php?mod=post&action=newthread&fid=24&extra=&topicsubmit=yes";
//			message = "����齨��ҵ�Ŷӣ���IT��ҵ����ӭ���룬�����������ϵ��ʽ";
//			subject = "����齨��ҵ�Ŷӣ���IT��ҵ����ӭ����";
//			login_formhash = test.getFormhash(url,httpclient);
//			
//			/* ���÷������� �ĸ��������������һ��Ϊform�ύ��hashֵ */
//			HttpResponse response = test.postMessage(httpclient, url, message, subject, login_formhash);
//			/* ���Կ������� */
//			System.out.println("#################################   ������ɺ� ��������   ############################");
//			test.printHttpGet(response);	//��ӡ�������ҳ�濴��
//			
//			System.out.println("#################################   �ض�����תҳ��   ############################");
//			/* ��ȡ�ض����ʶ�� */
//			int statuscode = response.getStatusLine().getStatusCode();
//			if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
//					|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
//					|| (statuscode == HttpStatus.SC_SEE_OTHER)
//					|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
//				System.out.println("#################################   ��ʼ����ظ�   ############################");
//				/* ����ظ� */
//				url = test.redirectHttp(httpclient, response);
//				message = "��ý���һ���Լ����һ�ѡ���ԵļӴ�ҵģ���ü�����ϵ��QQ֮���";
//				subject = "����齨��ҵ�Ŷӣ���IT��ҵ����ӭ����";
//				login_formhash = test.getFormhash(url,httpclient);
//				
//				for(int j=0;j<1;j++){
//					//���Զ�ζ�һ�����ӻظ�
//				}
//
//				sleep(10000);//��10���ٻظ�
//				
//				response = test.postReMessage(httpclient,url, message, subject, login_formhash);
//	
//				/*��ӡҳ������*/
//				test.printHttpGet(response);	
//			}
//		}	
		
		
		url = "http://www.aizili.com/thread-264893-1-1.html";
		message = "����治��������ϵ��ʽ���£�����";
		subject = "����齨��ҵ�Ŷӣ���IT��ҵ����ӭ����";
		login_formhash = test.getFormhash(url,httpclient);
		for(int i=15;i<20;i++){
			System.out.println(message+" ������" + i);
			test.postReMessage(httpclient,url, message, subject, login_formhash);
			sleep(10000);
		}
		
		/* �ر����ӹ����� */
		httpclient.getConnectionManager().shutdown();
	}
}
