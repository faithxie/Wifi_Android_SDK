package freesbell.demo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil{
	
	@SuppressWarnings("unused")
	private static final String STR_TAG = "HttpUtil" ;
    private HttpURLConnection url_con;
    
	public interface HttpResult{
		public abstract void httpResult(String strResult, int operation);
	}	
	private HttpResult httpRt = null;
	
	HttpUtil(HttpResult httpResult){
		httpRt = httpResult;
	}
	
	public void send_get_request(String urlStr, int operation, boolean result){
        try{
			String temp = "";
			URL url = new  URL(urlStr);
			url_con= (HttpURLConnection)url.openConnection();
			InputStream in =url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in));
			String inputLine;
			while (((inputLine = rd.readLine()) != null))  {  
				//������ÿһ�к������һ��"\n"������ 
				temp += inputLine ;//+ "\n";
			}   
			System.out.println(temp);
			if(result && httpRt != null){
				httpRt.httpResult(temp, operation) ;
			}
			in.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(url_con!=null){
                url_con.disconnect();
            }
        }
    }

}

