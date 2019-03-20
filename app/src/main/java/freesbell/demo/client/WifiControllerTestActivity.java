package freesbell.demo.client;

import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.ServiceStub;

public class WifiControllerTestActivity extends Activity implements OnClickListener{
	private final String TAG = "WifiControllerTestActivity";
	private String mUUID;
	private TextView mTitle;
	private ImageButton mExit;
	private BridgeService mBridgeService;
	private Button btn_gpio1,btn_gpio2,btn_gpio3,btn_gpio4,btn_send,btn_update;
	private EditText com_to_send_et;
	private boolean gpio1 = false,gpio2 = false,gpio3 = false,gpio4 = false;
	
	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
		}
	};

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
		public void onMessageRecieve(String uuid, int cmd, byte[] data) {
			Log.d(TAG, "msg:" + cmd);
			
			Bundle bd = new Bundle();
			Message msg = P2PMsgHandler.obtainMessage();
			msg.what = cmd;
			bd.putByteArray("data", data);
			msg.setData(bd);
			
			P2PDataHandler.sendMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wifi_controller_test);
		getDataFromOther();
		
		findView();
		setListener();
		initViewData();

		Intent intent=new Intent(this,BridgeService.class);
		bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}
	
	private void getDataFromOther() {
		Intent intent = getIntent();
		mUUID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
	}

	private void findView() {
		mExit = (ImageButton)findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		
		btn_gpio1 = (Button)findViewById(R.id.btn_gpio1);
		btn_gpio2 = (Button)findViewById(R.id.btn_gpio2);
		btn_gpio3 = (Button)findViewById(R.id.btn_gpio3);
		btn_gpio4 = (Button)findViewById(R.id.btn_gpio4);
		btn_send = (Button)findViewById(R.id.btn_send);
		com_to_send_et = (EditText) findViewById(R.id.com_to_send_et);
		btn_update = (Button)findViewById(R.id.btn_update);
	}
	private void setListener() {
		mExit.setOnClickListener(this);
		
		btn_gpio1.setOnClickListener(this);
		btn_gpio2.setOnClickListener(this);
		btn_gpio3.setOnClickListener(this);
		btn_gpio4.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_update.setOnClickListener(this);
	}

	private void initViewData(){
		mTitle.setText("WifiController");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		  case R.id.back:
			  finish();
			  break;
		  case R.id.btn_gpio1:{
			  byte[] msg = new byte[3];
			  msg[0] = 1;
			  msg[1] = 1;
			  msg[2] = (byte) (gpio1?0:1);//
			  mServiceStub.sendMessage(mUUID, ContentCommon.IPCNET_MCU_INFO_REQ, msg);
		  }break;
		  case R.id.btn_gpio2:{
			  byte[] msg = new byte[3];
			  msg[0] = 1;
			  msg[1] = 2;
			  msg[2] = (byte) (gpio2?0:1);
			  mServiceStub.sendMessage(mUUID, ContentCommon.IPCNET_MCU_INFO_REQ, msg);
		  }break;
		  case R.id.btn_gpio3:{
			  byte[] msg = new byte[3];
			  msg[0] = 1;
			  msg[1] = 3;
			  msg[2] = (byte) (gpio3?0:1);
			  mServiceStub.sendMessage(mUUID, ContentCommon.IPCNET_MCU_INFO_REQ, msg);
		  }break;
		  case R.id.btn_gpio4:{
			  byte[] msg = new byte[3];
			  msg[0] = 1;
			  msg[1] = 4;
			  msg[2] = (byte) (gpio4?0:1);
			  mServiceStub.sendMessage(mUUID, ContentCommon.IPCNET_MCU_INFO_REQ, msg);
		  }break;
		  case R.id.btn_send:
			  byte[] input2send = com_to_send_et.getText().toString().getBytes();
			  if(input2send!=null&&input2send.length>0){
				  byte[] msg = new byte[input2send.length + 1];
				  msg[0] = 2;
				  System.arraycopy(input2send, 0, msg, 1, input2send.length);
				  mServiceStub.sendMessage(mUUID, ContentCommon.IPCNET_MCU_INFO_REQ, msg);
			  }
			  break;
		  case R.id.btn_update:
			  
			  break;
		}
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
			case ContentCommon.IPC_GPIO_SET_RESP:
				try {
					int ret = jsonData.getInt("ret");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
				break;
			}
		}
	};
	private Handler P2PDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			int msgType = msg.what;
			byte[] data = bd.getByteArray("data");
			
			if(msgType == ContentCommon.IPCNET_MCU_INFO_RESP){
				switch(data[0]){
				case 1:
					Log.d(TAG, "gpio" + data[1] + " status:" + data[2]);
					boolean onoff = (data[2]&1) == 0;
					switch(data[1]){
					case 1:
						btn_gpio1.setText(onoff?"on":"off");
						gpio1 = !gpio1;
						break;
					case 2:
						btn_gpio2.setText(onoff?"on":"off");
						gpio2 = !gpio2;
						break;
					case 3:
						btn_gpio3.setText(onoff?"on":"off");
						gpio3 = !gpio3;
						break;
					case 4:
						btn_gpio4.setText(onoff?"on":"off");
						gpio4 = !gpio4;
						break;
					}
					break;
				case 2:
					break;
				}
			}
		}
	};
}
