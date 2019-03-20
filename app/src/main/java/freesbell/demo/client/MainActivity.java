package freesbell.demo.client;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import freesbell.demo.client.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.adapter.DeviceListAdapter;
import freesbell.demo.adapter.DeviceListAdapter.CameraListItem;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNETPrePointList_st;
import freesbell.demo.bean.JSONStructProtocal.IPCNetCamColorCfg_st;
import freesbell.demo.bean.JSONStructProtocal.MSG_LOGIN_t;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.dialog.MainSettingDialog;
import freesbell.demo.dialog.MainSettingDialog.OnListener;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.ServiceStub;

public class MainActivity extends Activity implements OnClickListener{
	private final String TAG = "MainActivity";
	
	public static MainActivity mSelf = null;
	private static final int MENU_ID_ADD = 100;
	private static final int MENU_ID_SEARCH = 101;
	private static final int MENU_ID_ABOUT = 102;
	private static final int MENU_ID_EXIT = 103;
	private LinearLayout ipcammain;
	private ExpandableListView cameraListView = null;
	private ImageView imgAddCamera = null;
	private EditText mSearchDev;
	public DeviceListAdapter listAdapter = null;
	private DatabaseUtil dbUtil = null;
	
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";
	
	private MyStatusBroadCast mStatusBroadCast;
	private ProgressDialog vserprogressdialog = null;
    private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
    public MSG_LOGIN_t mMSG_LOGIN_t = mJSONStructProtocal.new MSG_LOGIN_t();

    public BridgeService mBridgeService;
    private ServiceConnection mConn = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			ControllerBinder myBinder = (ControllerBinder) service;
			mBridgeService = myBinder.getBridgeService();
			mBridgeService.setServiceStub(mServiceStub);
			
			mBridgeService.MainActivityInitNativeCaller(
					MainActivity.this);
			
			dbUtil = new DatabaseUtil(MainActivity.this);
			getCameraList();
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
			bd.putString("uuid", uuid);
			msg.setData(bd);
			
			P2PMsgHandler.sendMessage(msg);
		}

		@Override
		public void onMessageRecieve(String uuid, int msg, byte[] str) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.main);

		findView();
		
		setControlListener();
		listAdapter = new DeviceListAdapter(this);
		cameraListView.setAdapter(listAdapter);
		cameraListView.setGroupIndicator(null);
		
		vserprogressdialog = new ProgressDialog(this);
		vserprogressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		vserprogressdialog.setMessage(getString(R.string.searching_tip));
		
        mStatusBroadCast = new MyStatusBroadCast();
		IntentFilter filter = new IntentFilter(ContentCommon.CAMERA_INTENT_STATUS_CHANGE);
		registerReceiver(mStatusBroadCast, filter);
		
		// bind ServiceBridge
		Intent intent = new Intent();
		intent.setClass(this, BridgeService.class);
		bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}

	private void setControlListener() {
		ipcammain.setOnClickListener(this);
		imgAddCamera.setOnClickListener(this);
		cameraListView.setOnGroupClickListener(new OnGroupClickListener(){
	
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Log.d(TAG,"onGroupClick:" + groupPosition);
	
				if (groupPosition < listAdapter.mCamListItems.size()) {
					CameraListItem mapItem = listAdapter
							.getItemContent(groupPosition);
					if (mapItem == null) {
						return true;//do not expand
					}
	
					int status = mapItem.status;
					Log.i(TAG, "status:" + status);
	
					String uuid = mapItem.uuid;
					CameraParamsBean cpb = getCamera(uuid);
					if (status != ContentCommon.P2P_STATUS_ON_LINE) {
						String user = mapItem.user;
						String pwd = mapItem.pwd;
						NativeCaller.StartP2P(uuid, user, pwd,1);
						if(cpb!=null){
							cpb.isLogining = false;
						}
						setResponseTarget(uuid,ContentCommon.REMOTE_MSG_RESP_LOGIN);
						return true;
					}
					if(cpb.devType == ContentCommon.P2P_CLIENT_TYPE_WIFI_COM_CONTROLLER){
						Intent intent = new Intent(MainActivity.this,
								WifiControllerTestActivity.class);
						intent.putExtra(ContentCommon.STR_CAMERA_ID, uuid);
						startActivityForResult(intent,1);
						return true;
					}
					
					String devname = mapItem.name;
					String user = mapItem.user;
					String pwd = mapItem.pwd;
					int p2pMode = mapItem.p2p_mode;
					ArrayList<String> didList = new ArrayList<String>();
					ArrayList<String> nameList = new ArrayList<String>();
					ArrayList<Integer> positonList = new ArrayList<Integer>();
					ArrayList<Integer> stypeList = new ArrayList<Integer>();
					didList.clear();
					nameList.clear();
					positonList.clear();
					stypeList.clear();
					
					didList.add(uuid);
					nameList.add(devname);
					positonList.add(groupPosition);
					stypeList.add(p2pMode);
					
					Intent intent = new Intent(MainActivity.this,
							CameraViewerActivity.class);
					intent.putStringArrayListExtra("didlist", didList);
					intent.putStringArrayListExtra("namelist", nameList);
					intent.putIntegerArrayListExtra("positionlist", positonList);
					intent.putIntegerArrayListExtra("stypelist", stypeList);
					intent.putExtra("playmode", 1);
					startActivityForResult(intent,2);
				} else {
					showAddActivity();
				}
				return true;//do not show sub item.
			}
			
		});
		cameraListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d(TAG,"onItemClick:" + arg2);
				if (arg2 < listAdapter.mCamListItems.size()) {
					CameraListItem mapItem = listAdapter
							.getItemContent(arg2);
					if (mapItem == null) {
						return;
					}
	
					int status = mapItem.status;
					Log.i(TAG, "status:" + status);
	
					if (status != ContentCommon.P2P_STATUS_ON_LINE) {
						String did = mapItem.uuid;
						String user = mapItem.user;
						String pwd = mapItem.pwd;
						NativeCaller.StartP2P(did, user, pwd,1);
						setResponseTarget(did,ContentCommon.REMOTE_MSG_RESP_LOGIN);
						//NativeCaller.StartPPPPExt("192.168.168.1", did, user, pwd);
						return;
					}
				} else {
					showAddActivity();
				}
	
			}
		});
	
		cameraListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
	
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						showSettingContextMenu(position);
						return false;
					}
	
				});
		mSearchDev.addTextChangedListener(new TextWatcher(){
	
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				listAdapter.filterDevice(s.toString());
				listAdapter.notifyDataSetChanged();
			}
	
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	public void showDelSureDialog(Context context, final String did) {
		Log.i(TAG, "showDelSureDialog!!!!!!!!!");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.del_alert);
		builder.setPositiveButton(R.string.str_ok,
		new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
				BridgeService.mSelf.deleteCamera(did);
				listAdapter.delCamera(did);
				listAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton(R.string.str_cancel,
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();
	}
	public void showSettingPopWindow(int position) {
		CameraListItem cli = listAdapter.getItemCamera(position);
		String pos = "0";
		Intent intentevent = new Intent(this,
				DeviceAttributeActivity.class);
		intentevent.putExtra(ContentCommon.STR_CAMERA_ID, cli.uuid);
		intentevent.putExtra("position", pos);
		startActivityForResult(intentevent,200);
	}
	private MainSettingDialog dialog3;

	private void showSettingContextMenu(final int position) {
		Log.i(TAG, "showSettingContextMenu+===" + position);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();

		Log.d(TAG, "long position" + position);
		final CameraListItem cli = listAdapter.getItemCamera(position);

		final int status = cli.status;
		dialog3 = new MainSettingDialog(this, width,
				height, status);
		dialog3.mRoot.setOnClickListener(this);
		dialog3.setOnClickListener(new OnListener() {

			@Override
			public void onItemClick(int itemposition, int count) {
				Log.i(TAG, "count" + count);
				if (count == 3) {//online
					switch (itemposition) {
					case 0: {
						String did = cli.uuid;
						String pos = "0";
						Intent intentevent = new Intent(MainActivity.this,
								DeviceAttributeActivity.class);
						intentevent.putExtra(ContentCommon.STR_CAMERA_ID, did);
						intentevent.putExtra("position", pos);
						startActivityForResult(intentevent,200);
					}
						break;
					case 1:
						if (status == ContentCommon.P2P_STATUS_ON_LINE) {
							String name1 = cli.name;
							String did1 = cli.uuid;
							String user = cli.user;
							String pwd = cli.pwd;
							Intent intentVid = new Intent(
									MainActivity.this,
									PlayBackTFActivity.class);
							intentVid.putExtra(ContentCommon.STR_CAMERA_NAME,
									name1);
							intentVid.putExtra(ContentCommon.STR_CAMERA_ID,
									did1);
							intentVid.putExtra(ContentCommon.STR_CAMERA_PWD,
									pwd);
							intentVid.putExtra(ContentCommon.STR_CAMERA_USER,
									user);
							Log.i(TAG, "camera_user:"+user+"---pwd"+pwd);
							startActivity(intentVid);
						} else {
							Log.i(TAG, "不在�?");
							Toast.makeText(
									MainActivity.this,
									getResources().getString(
											R.string.remote_video_offline),
									Toast.LENGTH_SHORT).show();

						}
						break;
					 case 2:{//delete the device
						 String did = cli.uuid;
						 showDelSureDialog(MainActivity.this, did);
					 }
					 break;
					default:
						break;
					}
				} else {// 4 //offline
					switch (itemposition) {
					case 0: {
						String did = cli.uuid;
						String pos = "0";
						Intent intentevent = new Intent(MainActivity.this,
								DeviceAttributeActivity.class);
						intentevent.putExtra(ContentCommon.STR_CAMERA_ID, did);
						intentevent.putExtra("position", pos);
						startActivityForResult(intentevent,200);
						break;
					}
					case 1:{//delete the device
						 String did = cli.uuid;
						 showDelSureDialog(MainActivity.this, did);
					 }
					 break;
					default:
						break;
					}
				}
			}
		});
		dialog3.show();
	}
	private void showAddActivity() {
		Log.d(TAG, "showAddActivity() 1");
		Intent in = new Intent(this,
    			AddDeviceActivity.class);
    	startActivityForResult(in, 201);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.root:
			if (dialog3.isShowing())
				dialog3.dismiss();
			break;
		case R.id.imgAddDevice:
			Log.d(TAG, "imgAddCamera 1");
			showAddActivity();
			break;
		default:
			break;
		}
	}
	
	/**
	 * BridgeService Feedback execute
	 * 
	 * */
	public void setPPPPSnapshotNotify(String did, byte[] bImage, int len) {
	
		Bitmap bmp = BitmapFactory.decodeByteArray(bImage, 0, len);
		if (bmp == null) {
//			Log.d(TAG, "bmp can't be decode...");
			return;
		}
		Drawable drawable = new BitmapDrawable(bmp);
		if (listAdapter.UpdateCameraImage(did, drawable)) {
			Message msg = new Message();
			updateListHandler.sendMessage(msg);
		}
	}
	
	private Handler updateListHandler = new Handler() {
		public void handleMessage(Message msg) {
			listAdapter.notifyDataSetChanged();
		}
	};
	
	private class MyStatusBroadCast extends BroadcastReceiver {
	
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG,"BroadcastReceiver:" + action);
			if (ContentCommon.CAMERA_INTENT_STATUS_CHANGE.equals(action)) {
				String did = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
				int status = intent.getIntExtra(ContentCommon.STR_PPPP_STATUS,ContentCommon.P2P_STATUS_UNKNOWN);
				if(did!=null&&!did.contentEquals("null")){
					listAdapter.UpdataCameraStatus(did, status);
				}else{
					listAdapter.UpdateAllCamera();
				}
				listAdapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "onActivityResult ---------------------");
		
		if (requestCode == 2) {
			if (resultCode == 2) {
				Bitmap btmp = null;
				byte[] h264bytearray = data.getByteArrayExtra("h264");
				if(h264bytearray!=null){
					int size[] = new int[2];
					
					int nVideoWidth = data.getIntExtra("width", 0);
					int nVideoHeight = data.getIntExtra("height", 0);
					if(nVideoWidth <= 0||nVideoHeight <= 0)
						return;
					
					byte[] yuvbuf = new byte[nVideoWidth*nVideoHeight*2];
					NativeCaller.DecodeH264Frame(h264bytearray, 1,yuvbuf, h264bytearray.length, size);
					
					int w = size[0];
					int h = size[1];
					byte[] rgb = new byte[ w * h * 2];
					NativeCaller.YUV4202RGB565(yuvbuf, rgb, w,h);
					ByteBuffer buffer = ByteBuffer.wrap(rgb);
					rgb = null;
					btmp = Bitmap.createBitmap(w, h,
							Bitmap.Config.RGB_565);
					btmp.copyPixelsFromBuffer(buffer);
				}else{
					btmp = (Bitmap) data.getParcelableExtra("bmp");
				}
				
				if(btmp!=null){
					String did = data.getStringExtra("did");
					Drawable drawable = new BitmapDrawable(btmp);
	
					if (listAdapter.UpdateCameraImage(did, drawable)) {
						listAdapter.notifyDataSetChanged();
					}
	//				 存储图片到sdcard里面
					if (existSdcard()) {
						saveBmpToSDcard(did, btmp);
					}
				}
			}
	
		} else if (requestCode == 200) {
			if (data.hasExtra("DID")) {
				Log.i(TAG, "Client接收数据刷新list");
				int backPosition = data.getIntExtra("backPosition", 0);
				String backDid = data.getStringExtra("DID");
				
				BridgeService.mSelf.deleteCamera(backDid);
				listAdapter.delCamera(backDid);
				listAdapter.notifyDataSetChanged();
			}
			Log.i(TAG, "接受的数�?" + data.getStringExtra("DID"));
	
		}else if(requestCode==201){
			Log.i(TAG, "addActivity back");
			if(data!=null&&data.hasExtra("Flag")){
				String name=data.getStringExtra(ContentCommon.STR_CAMERA_NAME);
				String did=data.getStringExtra(ContentCommon.STR_CAMERA_ID);
				String user=data.getStringExtra(ContentCommon.STR_CAMERA_USER);
				String pwd=data.getStringExtra(ContentCommon.STR_CAMERA_PWD);
				
				int devType = data.getIntExtra(ContentCommon.STR_DEVICE_TYPE,0);
				if (listAdapter.addCamera(name, did, user, pwd)) {
					Log.i(TAG, "add============");
					listAdapter.notifyDataSetChanged();
					addCamera2db(name, did, user, pwd);
				}
			}
		}
	
	}
	private void addCamera2db(String name, String did, String user, String pwd) {
		mBridgeService.addCamera(name, did, user, pwd);
	}
	private void saveBmpToSDcard(String did, Bitmap bitmap) {
		FileOutputStream fos = null;
		File div = new File(Environment.getExternalStorageDirectory(),
				"ipcam/pic");
		if (!div.exists()) {
			div.mkdirs();
		}
		try {
			File file = new File(div, did + ".jpg");
			fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)) {
				fos.flush();
				dbUtil.open();
				Cursor cursor = dbUtil.queryFirstpic(did);
				if (cursor.getCount() <= 0) {
					Log.d(TAG, "还没有图片，则保�?");
					String path = file.getAbsolutePath();
					dbUtil.addFirstpic(did, path);
				}
				dbUtil.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean existSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}// end


	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(mSelf == null){
			mSelf = this;
			return;
		}

	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "MainActivity onDestroy()");
		mSelf = null;
		if(mStatusBroadCast!=null){
			unregisterReceiver(mStatusBroadCast);
			mStatusBroadCast = null;
		}
		mBridgeService.unbindSetNull("MainActivity");
		mBridgeService.unbindSetNull(mServiceStub);
		unbindService(mConn);

		super.onDestroy();
	}

	private void findView() {
		ipcammain = (LinearLayout) findViewById(R.id.ipcammain);
		imgAddCamera = (ImageView) findViewById(R.id.imgAddDevice);
		cameraListView = (ExpandableListView) findViewById(R.id.listviewCamera);
		mSearchDev = (EditText)findViewById(R.id.search_dev_et);
	}
	
	private AlertDialog dialog = null;
	private int timeTag = 0;
	private int timeOne = 0;
	private int timeTwo = 0;
	private void showSureDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.app);
		builder.setMessage(R.string.exit_alert);
		builder.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();

						mSelf = null;
						NativeCaller.Free();

						// stop BridgeService
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, BridgeService.class);
						stopService(intent);
					}
				});
		builder.setNegativeButton(R.string.str_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						timeTag = 0;
						dialog.dismiss();
					}
				});
		dialog = builder.create();
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog1, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Date date = new Date();
					if (timeTag == 0) {
						timeTag = 1;
						timeOne = date.getSeconds();
					} else if (timeTag == 1) {
						timeTwo = date.getSeconds();
						if (timeTwo - timeOne <= 3) {
							moveTaskToBack(true);
							timeTag = 0;
							dialog.dismiss();
						} else {
							timeTag = 1;
							showSureDialog(MainActivity.this);
						}
					}

				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(listAdapter.getSelectedCount()>0 || mSearchDev.getVisibility() == View.VISIBLE){
				if(listAdapter.getSelectedCount()>0){
					listAdapter.deSelectAll();
				}
				if(mSearchDev.getVisibility() == View.VISIBLE){
					mSearchDev.setVisibility(View.GONE);
					listAdapter.restoreDeviceList();
				}
				listAdapter.notifyDataSetChanged();
				return true;
			}
			
			if (dialog != null) {
				timeTag = 0;
			}
			showSureDialog(this);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exitApp(View view){
		finish();
		
		mSelf = null;
		NativeCaller.Free();

		// stop BridgeService
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, BridgeService.class);
		stopService(intent);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case MENU_ID_ABOUT:
			break;
		case MENU_ID_EXIT:
			showSureDialog(this);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuItem item = menu.add(0, MENU_ID_ADD, 0, R.string.updateruan);
		// item.setIcon(R.drawable.updated);
		// item = menu.add(0, MENU_ID_SEARCH, 0, R.string.str_search);
		// item.setIcon(R.drawable.search);
		MenuItem item = menu.add(0, MENU_ID_ABOUT, 0, R.string.str_about);
		item.setIcon(R.drawable.info);
		item = menu.add(0, MENU_ID_EXIT, 0, R.string.exit);
		item.setIcon(R.drawable.content_remove);
		return super.onCreateOptionsMenu(menu);
	}
	public CameraParamsBean getCamera(String uuid) {
		return BridgeService.mSelf.getCamera(uuid);
	}
	private Handler P2PMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			int msgType = msg.what;
			String json = bd.getString("json");
			String uuid = bd.getString("uuid");
			JSONObject jsonData = null;
			try {
				jsonData = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(jsonData == null)
				return;
			
			CameraParamsBean cpb = getCamera(uuid);
			switch(msgType){
			case ContentCommon.REMOTE_MSG_RESP_LOGIN:
				JSONObject jsonDEV_SYS_INFO_t = null;
				JSONArray jsonvideo_input = null;
				JSONObject jsonFIX_SYS_INFO_t = null;
				String did,dev_ver,dev_name;
				Intent intent = new Intent(ContentCommon.CAMERA_INTENT_STATUS_CHANGE);//"camera_status_change");
				
				try {
					JSONObject jsonLoginInfo_t = jsonData.getJSONObject("Login.info");
					int tick = jsonLoginInfo_t.getInt("Tick");

					if(cpb!=null){
						String user = cpb.getUser();
						String pwd = cpb.getPwd();
						Log.d(TAG,"LoginToIPC now...");
						if(cpb!=null){
							cpb.isLogining = true;
						}
						NativeCaller.LoginToIPC(uuid,"admin",pwd,tick);
					}
					return;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					jsonDEV_SYS_INFO_t = new JSONObject(json).getJSONObject("dev_info");
					jsonvideo_input = jsonDEV_SYS_INFO_t.getJSONArray("video_input");
					//jsonFIX_SYS_INFO_t = jsonsys_info.getJSONObject("FIX_SYS_INFO_t");
					did = jsonDEV_SYS_INFO_t.getString("p2p_uuid");
					dev_ver = jsonDEV_SYS_INFO_t.getString("dev_ver");
					dev_name = jsonDEV_SYS_INFO_t.getString("name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

				intent.putExtra(ContentCommon.STR_CAMERA_ID, uuid);
				intent.putExtra(ContentCommon.STR_PPPP_STATUS, ContentCommon.P2P_STATUS_ON_LINE);
				MainActivity.this.sendBroadcast(intent);

				Log.d(TAG,"login:" + did + ".");
				if(cpb!=null){
					cpb.status = ContentCommon.P2P_STATUS_ON_LINE;
					cpb.mDevVer = dev_ver;
					cpb.name = dev_name;
					cpb.isLogining = false;
				}
				//update ui
				break;
			}
		}
	};
	private void getCameraList() {
		if(mBridgeService == null)
			return;
		ArrayList<CameraParamsBean> clist = mBridgeService.getCameraList();
		for(CameraParamsBean cpb:clist){
			String name = cpb.getName();
			String did = cpb.getDid();
			String user = cpb.getUser();
			String pwd = cpb.getPwd();
			int status = cpb.getStatus();
			int p2p_mode = cpb.getP2PMode();
			ArrayList<Map<String, Object>> sl = cpb.getSensorList();
			listAdapter.addCamera(name, did, user, pwd,status,p2p_mode,sl);
		}
		listAdapter.notifyDataSetChanged();
	}

	//for login
	public void setResponseTarget(String uuid,int msg){
		mServiceStub.setResponseTarget(uuid, msg);
	}
	private Handler StatusMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent = new Intent(ContentCommon.CAMERA_INTENT_STATUS_CHANGE);
			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			String did = bd.getString(STR_DID);
			
			switch (msgType) {
			case ContentCommon.P2P_MSG_TYPE_P2P_STATUS:

				//broadcast the status.
				intent.putExtra(ContentCommon.STR_CAMERA_ID, did);
				intent.putExtra(ContentCommon.STR_PPPP_STATUS, msgParam);
				MainActivity.this.sendBroadcast(intent);

				if (msgParam == ContentCommon.P2P_STATUS_INVALID_ID
						|| msgParam == ContentCommon.P2P_STATUS_CONNECT_FAILED
						|| msgParam == ContentCommon.P2P_STATUS_DEVICE_NOT_ON_LINE
						|| msgParam == ContentCommon.P2P_STATUS_CONNECT_TIMEOUT
						|| msgParam == ContentCommon.P2P_STATUS_INVALID_PWD
						|| msgParam == ContentCommon.P2P_STATUS_INVALID_USER_PWD) {

					NativeCaller.StopP2P(did);
					
					CameraParamsBean cpb = getCamera(did);
					if(cpb!=null){
						if(cpb.isUpdateing && msgParam == ContentCommon.P2P_STATUS_DEVICE_NOT_ON_LINE){
//								String did = cpb.did;
							String user = cpb.user;
							String pwd = cpb.pwd;
							NativeCaller.StartP2P(did, user, pwd,1);
						}
					}
				}
				
				break;
			case ContentCommon.P2P_MSG_TYPE_P2P_DEV_TYPE:{
				Log.d(TAG, "P2P_MSG_TYPE_P2P_DEV_TYPE:" + msgParam);
				CameraParamsBean cpb = getCamera(did);
				cpb.devType = msgParam;
			}break;
			case ContentCommon.P2P_MSG_TYPE_P2P_MODE:
				CameraParamsBean cpb = getCamera(did);
				Log.d(TAG, "P2P_MSG_TYPE_P2P_MODE:" + msgParam);
				if(cpb.devType == ContentCommon.P2P_CLIENT_TYPE_WIFI_COM_CONTROLLER){
					Log.d(TAG, "Connected!");
					intent.putExtra(ContentCommon.STR_CAMERA_ID, did);
					intent.putExtra(ContentCommon.STR_PPPP_STATUS, ContentCommon.P2P_STATUS_ON_LINE);
					MainActivity.this.sendBroadcast(intent);

					Log.d(TAG,"login:" + did + ".");
					if(cpb!=null){
						cpb.status = ContentCommon.P2P_STATUS_ON_LINE;
						cpb.mDevVer = "0.01";
						cpb.name = "wifi controller";
						cpb.isLogining = false;
					}
				}else
				if(cpb.devType == ContentCommon.P2P_CLIENT_TYPE_CAMERA){
					//send login data!
					mMSG_LOGIN_t.interval = 10;
					mMSG_LOGIN_t.user = "admin";
					mMSG_LOGIN_t.passwd = "";
					NativeCaller.sendJsonCmd(did , ContentCommon.REMOTE_MSG_LOGIN , mMSG_LOGIN_t.toJSONString());
					
					if(cpb!=null){
						if(cpb.isLogining){
							cpb.mLoginTryTimes++;
							if(cpb.mLoginTryTimes>2){
								Log.d(TAG,"mLoginTryTimes:" + cpb.mLoginTryTimes);
								cpb.mLoginTryTimes=0;
								cpb.isLogining=false;
								cpb.status = ContentCommon.P2P_STATUS_INVALID_USER_PWD;
								Toast.makeText(MainActivity.this, did + " "+ getResources().getString(R.string.p2p_status_uuid_or_pwd_invalid), 3).show();
								mBridgeService.updateCameraStatus(did, ContentCommon.P2P_STATUS_INVALID_USER_PWD);

								//update ui.
								intent.putExtra(ContentCommon.STR_PPPP_STATUS, ContentCommon.P2P_STATUS_INVALID_USER_PWD);
								MainActivity.this.sendBroadcast(intent);
								NativeCaller.StopP2P(did);
							}
						}
					}
				}
				break;
			}
		}
	};

	/**
	 * BridgeService Feedback execute
	 * **/
	public void setStatusMsgNotify(String did, int type, int param) {
		Log.d(TAG, "type:" + type + " param:" + param);
		
		//update status.
		switch (type) {
		case ContentCommon.P2P_MSG_TYPE_P2P_STATUS:
			mBridgeService.updateCameraStatus(did,param);
			break;
		case ContentCommon.P2P_MSG_TYPE_P2P_MODE:
			break;
		}
		
		Bundle bd = new Bundle();
		Message msg = StatusMsgHandler.obtainMessage();
		msg.what = type;
		bd.putInt(STR_MSG_PARAM, param);
		bd.putString(STR_DID, did);
		msg.setData(bd);
		StatusMsgHandler.sendMessage(msg);
	}
}
