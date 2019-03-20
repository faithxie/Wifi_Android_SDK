package freesbell.demo.client;

import java.util.Date;

import freesbell.demo.client.R;

import fenzhi.nativecaller.NativeCaller;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
/**
 * @param 
 * @date
 * @autor
 */
public class StartActivity extends Activity {	
	private static final String LOG_TAG = "StartActivity" ;	
	private static boolean isView = false;
	private ProgressDialog progressDialog = null;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			Intent in = new Intent(StartActivity.this, MainActivity.class);
			startActivity(in);
			finish();
			Log.e(LOG_TAG, "start over");
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);  
        FrameLayout img=(FrameLayout)findViewById(R.id.start_root);
        
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        img.startAnimation(animation);
        Intent intent=new Intent();
        intent.setClass(StartActivity.this, BridgeService.class);
		startService(intent);
		startin();
		Log.i("info", "StartActivty onCreate");
       
    }

    private void startin(){
    	new Thread(new Runnable(){
 			@Override
 			public void run() {
 				try{
 					Log.i("info", "startin()");
 					NativeCaller.P2PInitial("RNISQPNGFMEPPEHNEGRHRPIRNDQOLQTFEHSNEMLNTEGFPHQMGJNJENNJFQKINJPGMNSQFFTKRGNMNIPQTKJIKLOHNNONGPPEET");
 					NativeCaller.P2PInitialOther("EBGDEIBIKEJNGBJNEKGKFIEEHBMDHNNEGIEABDCABNJFLKLFCDAJCCOAHKLGJJKKBJMDLDCBONMCAFDPJFNPIKAA");
 					
 					long lStartTime = new Date().getTime();
 					Log.d("info", "lStartTime: " + lStartTime);

 					long lEndTime = new Date().getTime();
 					Log.d("info", "lEndTime: " + lEndTime);
 					if(lEndTime - lStartTime <= 1000){
 						Thread.sleep(1000);
 					}
 					Message msg = new Message();
 					mHandler.sendMessage(msg);
 				}catch(Exception e){
 					
 				}
 			}        	
         }).start();  
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			finish();
		return super.onKeyDown(keyCode, event);
	}
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d("info","StartActivity onRestart()");
    }
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.d("info","StartActivity onStart()");
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("info","StartActivity onResume()");
    }
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d("info","StartActivity onStop()");
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("info","StartActivity onDestroy()");
    }
	
	
}