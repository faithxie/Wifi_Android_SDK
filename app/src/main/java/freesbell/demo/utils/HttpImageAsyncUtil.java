package freesbell.demo.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class HttpImageAsyncUtil extends AsyncTask<String, String, Drawable>{
	
	private String strIpAddr;
	private int nPort;
	private AsyncResult asyncResult = null;
	
	public interface AsyncResult{
		public abstract void ImageResult(String addr, int port, Drawable drawable);
	}
	
	public HttpImageAsyncUtil(String addr, int port, AsyncResult async){
		strIpAddr = addr;
		nPort = port;
		asyncResult = async;		
		//System.out.println("strIpAddr: " + strIpAddr + " nPort: " + nPort) ;
	}

	@Override
	protected Drawable doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL url;
		HttpURLConnection urlConn = null;
		InputStream is = null;
		try{
			//System.out.println(params[0]);				
			url = new URL(params[0]);
			urlConn = (HttpURLConnection) url.openConnection();
			is = urlConn.getInputStream();
			Drawable drawable = Drawable.createFromStream(is, "");
			is.close();
			urlConn.disconnect();
			return drawable;
		}catch(Exception e){
			System.out.println("HttpImageAsyncUtil Exception");
		}finally{
			if(urlConn != null)
				urlConn.disconnect();
		}		
		return null;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(asyncResult != null && result != null){
			asyncResult.ImageResult(strIpAddr, nPort, result);
		}			
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	
	
}