package freesbell.demo.client;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNetSnapShoot_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetTimeCfg_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TestSnapshot extends Activity implements OnClickListener{
	private static String TAG="TestSnapshot";
	private String strDID;
	private String cameraName;
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetSnapShoot_st mIPCNetSnapShoot_st = mJSONStructProtocal.new IPCNetSnapShoot_st();
	private BridgeService mBridgeService;
	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			ControllerBinder myBinder = (ControllerBinder) arg1;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			mBridgeService.setSnapshotCallback(TestSnapshot.this);
			//start jni snapshot receive thread
			NativeCaller.StartSnapshot(strDID);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_snapshot);

		findView();
		setListener();
		
		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);
	}
	private ServiceStub mServiceStub = new ServiceStub(){
		@Override
		public void onMessageRecieve(String uuid ,int cmd, String json) {
			Log.d(TAG, "msg:" + cmd);
			
			Bundle bd = new Bundle();
			Message msg = P2PMsgHandler.obtainMessage();
			msg.what = cmd;
			bd.putString("json", json);
			msg.setData(bd);
			
			P2PMsgHandler.sendMessage(msg);
		}

		@Override
		public void onMessageRecieve(String uuid, int msg, byte[] str) {
			// TODO Auto-generated method stub
			
		}
	};

	private Button cancel_btn,snapshot_btn;
	private ImageView snapshot_iv;

	private void setListener() {
		cancel_btn.setOnClickListener(this);
		snapshot_btn.setOnClickListener(this);
	}

	private void findView() {
		cancel_btn = (Button)findViewById(R.id.cancel_btn);
		snapshot_btn = (Button)findViewById(R.id.snapshot_btn);
		snapshot_iv = (ImageView)findViewById(R.id.snapshot_iv);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.snapshot_btn:
			mIPCNetSnapShoot_st.ViCh = 0;
			Cmds.getSnapshot(mServiceStub, strDID,mIPCNetSnapShoot_st.toJSONString());
			break;
		case R.id.cancel_btn:
			finish();
			break;
		default:
			break;
		}
	}

	private void getDataFromOther() {
		Intent intent = getIntent();
		strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
	}

	private Handler P2PMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			int msgType = msg.what;
			String json = bd.getString("json");
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(jsonData == null)
				return;
			
			switch(msgType){
			case ContentCommon.IPCNET_SNAP_SHOOT_RESP:
				Log.d(TAG, "IPCNET_SNAP_SHOOT_RESP");
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
	}

	public void showToast(String content){
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	public void showToast(int rid){
		Toast.makeText(this, getResources().getString(rid), Toast.LENGTH_LONG).show();
	}

	private Bitmap bmp;
	public void setPPPPSnapshotNotify(String did, byte[] data, int len) {
		bmp=BitmapFactory.decodeByteArray(data, 0, len);
//		Bitmap bitmap = Bitmap.createScaledBitmap(bmp, snapshot_iv.getWidth(),
//				snapshot_iv.getHeight(), true);
//		snapshot_iv.setImageBitmap(bitmap);
		mhandler.sendEmptyMessage(0);
	}
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(bmp!=null){
				Log.d(TAG, "show img now!");
				Bitmap bitmap = Bitmap.createScaledBitmap(bmp, snapshot_iv.getWidth(),
						snapshot_iv.getHeight(), true);
				snapshot_iv.setImageBitmap(bitmap);
			}
		}

	};
}
