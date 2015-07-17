import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.common.SinaWeiBo;


public class as {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		SinaWeiBo sinaWeiBo = new SinaWeiBo();
		String userName = "1151871470@qq.com";
		String pwd = "a901110";
		String mid = "3861619988174869";
		String content = "ºÃ°É¡£¡£¡£";
		String url = "http://weibo.com/2454788424/CpR14yiEJ?type=comment#_rnd1436854757529";
		try {
			sinaWeiBo.forward(userName, pwd, url, mid, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
