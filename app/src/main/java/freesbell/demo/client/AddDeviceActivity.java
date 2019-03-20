package freesbell.demo.client;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.realtek.simpleconfig.SmartLinkActivity;

import freesbell.demo.client.R;
import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.adapter.SearchListAdapter;
import freesbell.demo.bean.JSONStructProtocal;
import freesbell.demo.bean.JSONStructProtocal.IPCNetUserInfo_st;
import freesbell.demo.client.BridgeService.ControllerBinder;
import freesbell.demo.client.ComboBox.ListViewItemClickListener;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddDeviceActivity extends Activity {	
	private static final String TAG = "AddDeviceActivity" ;		
	
	private Button btnAdd = null;
	private Button btnCancel = null;	
	private EditText devNameEdit,old_pwd_ed;
//	private ComboBox devComboBoxType = null;
	private EditText userEdit = null;
	private EditText pwdEdit = null;	
	private EditText didEdit = null;
	private CheckBox dev_show_passwd_cb;
	//private boolean isEditName = false;
	
	private String strName = "";	
	private String strUser = "";
	private String strPwd = "admin";
	private String strOldDID = "";
	private static final int SEARCH_TIME = 3000 ;
	
	private int mDevType = 0;
	private boolean changeDevType = true;
	private int option = ContentCommon.INVALID_OPTION;	
	private TextView textViewAddCamera = null;
	
	private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
	private String[] mDevTypeList; 
	//= {  "普通开关","调幅开关","遥控插座","情景面板","遥控器","网关",
//			"路由中继器","红外转发器","灯(只有开关作用)","可调光的灯","自带开关功能的灯","亮度传感器","窗帘控制器",
//			"温度传感器","压力传感器","流量传感器","摄像头"};
	private Button btnScanId;
	private Button btn_config_wifi;

	private Button btnSearchCamera;
	private Button reRoot;
    private TextView descriptionadd;
	private ListView searchList;
	private LinearLayout old_pwd_layout,edit_user_layout;
	private SearchListAdapter listAdapter = null;
	private LinearLayout addDescription;
	private ProgressDialog progressdlg = null;
	private BridgeService mBridgeService;
	private PopupWindow searchCameraPopWindow;
	private boolean isSearched;
	private Intent data;
	private JSONStructProtocal mJSONStructProtocal = new JSONStructProtocal();
	private IPCNetUserInfo_st mIPCNetUserInfo_st = mJSONStructProtocal.new IPCNetUserInfo_st();
	private ServiceConnection mConn=new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBridgeService = ((ControllerBinder)service).getBridgeService();
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
		public void onMessageRecieve(String uuid, int msg, byte[] str) {
			// TODO Auto-generated method stub
			
		}
		
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.add_device);        
        Log.d(TAG, "AddCameraActivity onCreate") ;
        Intent in = getIntent();
        option = in.getIntExtra(ContentCommon.CAMERA_OPTION, ContentCommon.INVALID_OPTION);
        if(option != ContentCommon.INVALID_OPTION){
        	strName = in.getStringExtra(ContentCommon.STR_CAMERA_NAME);
        	strOldDID = in.getStringExtra(ContentCommon.STR_CAMERA_ID);
        	strUser = in.getStringExtra(ContentCommon.STR_CAMERA_USER);
        	strPwd = in.getStringExtra(ContentCommon.STR_CAMERA_PWD);
        }
//        int devType = in.getIntExtra(DeviceData.DEVTYPE, DeviceData.DEV_TYPE_INVALID);
//        if(devType != DeviceData.DEV_TYPE_INVALID){
//        	mDevType = devType;
//        	changeDevType = false;
//        }
        progressdlg = new ProgressDialog(this);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.searching_tip));
        listAdapter = new SearchListAdapter(this);        
        findView();        
        InitParams();  
        Intent intent=new Intent();
        intent.setClass(this, BridgeService.class);
        bindService(intent,mConn, Context.BIND_AUTO_CREATE);
        
        //searchCamera popwindow
        showSearchCameraPopWindow();
    }    
    
	private void showSearchCameraPopWindow() {
		LinearLayout layout=(LinearLayout)LayoutInflater.from(this).inflate(R.layout.searchcamera_popwindow, null);
		searchCameraListView = (ListView)layout.findViewById(R.id.searchcamera_pop_listview);
		Button btnRefreshSearchCamera=(Button)layout.findViewById(R.id.refresh_camera);
		btnRefreshSearchCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchCameraPopWindow.dismiss();
				startSearch();
			}
		});
		searchCameraListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("tag", "position:"+arg2);
				searchCameraPopWindow.dismiss();
				Map<String, Object> mapItem = (Map<String, Object>)listAdapter.getItemContent(arg2);
				if(mapItem == null){
					return;
				}
				
				String strName = (String)mapItem.get(ContentCommon.STR_CAMERA_NAME);	
				String strDID = (String)mapItem.get(ContentCommon.STR_CAMERA_ID);
				String strUser= ContentCommon.DEFAULT_USER_NAME;
				String strPwd=ContentCommon.DEFAULT_USER_PWD;
				devNameEdit.setText(strName);
				//userEdit.setText(strUser);
				pwdEdit.setText(strPwd);
				didEdit.setText(strDID);
			}
		});
		searchCameraPopWindow=new PopupWindow(layout,300,500);
		searchCameraPopWindow.setFocusable(true);
		searchCameraPopWindow.getContentView().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				searchCameraPopWindow.dismiss();
				searchCameraPopWindow.setFocusable(false);
				return true;
			}
		});
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	if(keyCode==KeyEvent.KEYCODE_BACK){
//    		searchCameraPopWindow.dismiss();
//    	}
    	return super.onKeyDown(keyCode, event);
    }
	private void InitParams() {
//		if(changeDevType || mDevType >= DeviceData.DEV_TYPE_INVALID || mDevType<0){
//			mDevType = 0;
//			mDevTypeList = new String[DeviceData.mDevTypeList.length];
//			for(int i=0;i<DeviceData.mDevTypeList.length;i++){
//				mDevTypeList[i] = getResources().getString(DeviceData.mDevTypeList[i]); 
//			}
//		}else{
//			mDevTypeList = new String[1];
//			//for(int i=0;i<DeviceData.mDevTypeList.length;i++){
//				mDevTypeList[0] = getResources().getString(DeviceData.mDevTypeList[mDevType]);
//			//}
//				devNameEdit.setText(DeviceData.mDevTypeList[mDevType]);
//		}
//		
//		devComboBoxType.setData(mDevTypeList);
//		devComboBoxType.setListViewOnClickListener(new ComboListViewItemClickListener());
	    
		if(option == ContentCommon.EDIT_CAMERA){
			textViewAddCamera.setText(R.string.operation_edit_device) ;
			addDescription.setVisibility(View.GONE);
			reRoot.setVisibility(View.VISIBLE);
			
//			old_pwd_layout.setVisibility(View.VISIBLE);
			pwdEdit.setHint(R.string.new_pwd_hint);
    	}else{
    		textViewAddCamera.setText(R.string.operation_add_device) ;
    		String message = (String) getResources().getText(R.string.description);
    		String mess = (String) getResources().getText(R.string.description1);
    		String me = (String) getResources().getText(R.string.description2);
    		descriptionadd.setText("        "+message+"\n"+"        "+mess+"\n"+"        "+me);
    		addDescription.setVisibility(View.VISIBLE);
    	}
		
		if(option != ContentCommon.INVALID_OPTION){
			devNameEdit.setText(strName) ;
			//userEdit.setText(strUser);
			pwdEdit.setText(strPwd);
			didEdit.setText(strOldDID);
		}
		dev_show_passwd_cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); 
				}else{
					pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
				}
			}
        	
        });

		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "btnAdd.setOnClickListener ") ;
				
				Intent in = new Intent() ;
				
				String strDevName = devNameEdit.getText().toString() ;				
				//String strUser = userEdit.getText().toString();
				String strUser = "admin";
				String strPwd = pwdEdit.getText().toString();
				String strDID = didEdit.getText().toString();	
				
				if(strDevName.length() == 0){
					Toast.makeText(getApplicationContext(), getString(R.string.input_camera_name),
						     Toast.LENGTH_SHORT).show();
					return;
				}		
				
				if(strDID.length() == 0){
					Toast.makeText(getApplicationContext(), getString(R.string.input_camera_id),
						     Toast.LENGTH_SHORT).show();
					return;
				}
			
				if(strUser.length() == 0){
					Toast.makeText(getApplicationContext(), getString(R.string.input_camera_user),
						     Toast.LENGTH_SHORT).show();
					return;
				}	
				
				in.setAction(ContentCommon.STR_CAMERA_INFO_RECEIVER) ;		
				if(option == ContentCommon.INVALID_OPTION){
					option = ContentCommon.ADD_CAMERA;
				}
				
				in.putExtra(ContentCommon.CAMERA_OPTION, option);
				
				if(option != ContentCommon.INVALID_OPTION){
					in.putExtra(ContentCommon.STR_CAMERA_OLD_ID, strOldDID);
				}
				Log.i("info","add:"+option);
				in.putExtra(ContentCommon.STR_CAMERA_NAME, strDevName);
				in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
				in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
				in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
				in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
				if(option==ContentCommon.ADD_CAMERA){//三星手机无法添加相机修改
					data=new Intent();
					data.putExtra(ContentCommon.STR_CAMERA_NAME, strDevName);
					data.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
					data.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
					data.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
					data.putExtra(ContentCommon.STR_DEVICE_TYPE,mDevType);
					data.putExtra("Flag", "aaa");
					setResult(201, data);
					
					finish();
				}else{
					sendBroadcast(in);//update database in mobile phone.
					
					//modify password!//send to device to modify
					if(strPwd.length()>0){
						mIPCNetUserInfo_st.Op = "Change";
						mIPCNetUserInfo_st.Passwd = strPwd;
						Cmds.setSetUserInfo(mServiceStub,strOldDID,mIPCNetUserInfo_st.toJSONString());
					}
					showToast(R.string.setting_ok);
				}
			}
			
		});
		
		mBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}			
		});
		btnCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}			
		});		
		btnScanId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(AddDeviceActivity.this,CaptureActivityPortrait.class);
				startActivityForResult(it, 2014);
			}
		});
		 reRoot.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!didEdit.getText().equals("")&& didEdit.getText() != null){
						String strDID = didEdit.getText().toString();
//						NativeCaller.PPPPRebootDevice(strDID);
						Toast.makeText(getApplicationContext(),getString(R.string.rebooting_now) ,3000).show();
					}else{
						Toast.makeText(getApplicationContext(), getString(R.string.input_camera_id),
							     Toast.LENGTH_SHORT).show();
					}
					
				}
		});
		btn_config_wifi.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent it=new Intent(AddDeviceActivity.this,SmartLinkActivity.class);
				startActivity(it);
			}
		});
		btnSearchCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isSearched){
					isSearched=true;
					startSearch();
				}else{
					AlertDialog.Builder dialog=new AlertDialog.Builder(AddDeviceActivity.this);
			        dialog.setTitle(getResources().getString(R.string.add_search_result));
			        dialog.setPositiveButton(getResources().getString(R.string.refresh),new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startSearch();
							
						}
					});
			        dialog.setNegativeButton(getResources().getString(R.string.str_cancel), null);
			        dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg2) {
							Map<String, Object> mapItem = (Map<String, Object>)listAdapter.getItemContent(arg2);
							if(mapItem == null){
								return;
							}
							
							String strName = (String)mapItem.get(ContentCommon.STR_CAMERA_NAME);	
							String strDID = (String)mapItem.get(ContentCommon.STR_CAMERA_ID);
							String strUser= ContentCommon.DEFAULT_USER_NAME;
							int devType = (Integer)mapItem.get(ContentCommon.STR_DEVICE_TYPE);
							String strPwd=ContentCommon.DEFAULT_USER_PWD;
							devNameEdit.setText(strName);
							//userEdit.setText(strUser);
							pwdEdit.setText(strPwd);
							didEdit.setText(strDID);
//							devComboBoxType.setCurPos(devType);
							Log.d(TAG,"set device type:" + devType);
						}
					});
			       
			       dialog.show();
			      
					
//					searchCameraListView.setAdapter(listAdapter);
//					searchCameraPopWindow.setFocusable(true);
//					searchCameraPopWindow.showAtLocation(findViewById(R.id.addcamera), Gravity.CENTER, 0, 0);
				}
			}
		});
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, Object> mapItem = (Map<String, Object>)listAdapter.getItemContent(arg2);
				if(mapItem == null){
					return;
				}
				
				String strName = (String)mapItem.get(ContentCommon.STR_CAMERA_NAME);	
				String strDID = (String)mapItem.get(ContentCommon.STR_CAMERA_ID);
				
				String strUser= ContentCommon.DEFAULT_USER_NAME;
				String strPwd=ContentCommon.DEFAULT_USER_PWD;
				devNameEdit.setText(strName);
				//userEdit.setText(strUser);
				pwdEdit.setText(strPwd);
				didEdit.setText(strDID);
			}
		});
		mScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(searchCameraPopWindow!=null&&searchCameraPopWindow.isShowing()){
					searchCameraPopWindow.dismiss();
				}
				
				return false;
			}
		});
	}
	 @Override
	protected void onStop() {
	    	NativeCaller.StopSearch() ;
			super.onStop();
		}
	 @Override
	protected void onDestroy() {
		 if(updateListHandler!=null){
			 updateListHandler.removeCallbacks(updateThread);
		 }
		super.onDestroy();
		mBridgeService.unbindSetNull("AddDeviceActivity");
		unbindService(mConn);
	}
	 
	  Runnable updateThread = new Runnable(){ 

		    public void run(){ 
		    	NativeCaller.StopSearch() ;	
		    	progressdlg.dismiss();
		    	Message msg = updateListHandler.obtainMessage() ;
		    	msg.what = 1;
		    	updateListHandler.sendMessage(msg); 
		    } 
	    };
	    
	 Handler updateListHandler = new Handler(){ 
			@Override 
			public void handleMessage(Message msg) { 
			
				if(msg.what == 1){
					listAdapter.notifyDataSetChanged();
					if(listAdapter.getCount()>0){
						AlertDialog.Builder dialog=new AlertDialog.Builder(AddDeviceActivity.this);
				        dialog.setTitle(getResources().getString(R.string.add_search_result));
				        dialog.setPositiveButton(getResources().getString(R.string.refresh),new  DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startSearch();
							}
						});
				        dialog.setNegativeButton(getResources().getString(R.string.str_cancel), null);
				        dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int arg2) {
								Map<String, Object> mapItem = (Map<String, Object>)listAdapter.getItemContent(arg2);
								if(mapItem == null){
									return;
								}
								
								String strName = (String)mapItem.get(ContentCommon.STR_CAMERA_NAME);	
								String strDID = (String)mapItem.get(ContentCommon.STR_CAMERA_ID);
								String strUser= ContentCommon.DEFAULT_USER_NAME;
								String strPwd=ContentCommon.DEFAULT_USER_PWD;
								int devType = (Integer)mapItem.get(ContentCommon.STR_DEVICE_TYPE);
								devNameEdit.setText(strName);
									// userEdit.setText(strUser);
								pwdEdit.setText(strPwd);
								didEdit.setText(strDID);
//								if(changeDevType)
//									devComboBoxType.setCurPos(devType);
								mDevType = devType;
								Log.d(TAG,"set device type:" + devType);
							}
						});
				       
				       dialog.show();
					 }else{
						 Toast.makeText(AddDeviceActivity.this, getResources().getString(R.string.add_search_no), Toast.LENGTH_LONG).show();
						 isSearched=false;
					 }
					 
			
				}		
			} 
	    };

	private ListView searchCameraListView;

	private ScrollView mScrollView;    
		private void startSearch(){
			listAdapter.ClearAll();
			//searchList.setVisibility(View.GONE);
	    	progressdlg.show();
	    	mBridgeService.startSearch(AddDeviceActivity.this);
	    	updateListHandler.postDelayed(updateThread, SEARCH_TIME);
	    }
	/***
     * BridgeService Feedback execute
     * */
    public void setSearchResultData(int cameraType, String strMac, String strName, String strDeviceID, String strIpAddr, int port, int devType){
    	Log.d(TAG,"SearchResult:"+ strDeviceID + " dev:" + devType);
    	if(!listAdapter.AddCamera(strMac, strName, strDeviceID,devType)){
    		return ;
    	}  
    }

//	private void checkTwoDimension() {
//		Intent scanintent = new Intent("com.google.zxing.client.android.SCAN");
//		if(getPackageManager().queryIntentActivities(scanintent,PackageManager.MATCH_DEFAULT_ONLY).size()==0){
//			InputStream open=null;
//			FileOutputStream out=null;
//			try {
//				open = getApplicationContext().getAssets().open("BarcodeScanner3.72.apk");
//				File file=new File( Environment.getExternalStorageDirectory(),"scanner.apk");
//				out=new FileOutputStream(file);
//				int len=0;
//				byte[] buff=new byte[1024];
//				while((len=open.read(buff))!=-1){
//					out.write(buff,0,len);
//				}
//				
//				Intent intent2 = new Intent();
//                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent2.setAction(android.content.Intent.ACTION_VIEW);
//                intent2.setDataAndType(Uri.fromFile(file),
//                                "application/vnd.android.package-archive");
//                startActivity(intent2);
//			} catch (IOException e) {
//				Log.d("tag","exception:"+e.getMessage());
//				e.printStackTrace();
//			}finally{
//				if(open!=null){
//					try {
//						open.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if(out!=null){
//					try {
//						out.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}else{
//			Log.i("info", "33333333333333");
//			startActivityForResult(scanintent, 0);
//		}
//	}
    ImageButton mBackBtn;
	private void findView() {
		btnAdd = (Button)findViewById(R.id.btnAdd) ;
		btnCancel = (Button)findViewById(R.id.btnCancel) ;
		mBackBtn = (ImageButton)findViewById(R.id.back);
		devNameEdit = (EditText)findViewById(R.id.editDevName);
		old_pwd_layout = (LinearLayout)findViewById(R.id.old_pwd_layout);
		edit_user_layout = (LinearLayout)findViewById(R.id.edit_user_layout);
		old_pwd_ed = (EditText)findViewById(R.id.old_pwd_ed);
//		devNameEdit.addTextChangedListener(new TextWatcher(){
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				isEditName = true;
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		devComboBoxType = (ComboBox)findViewById(R.id.id_combobox);
		//userEdit = (EditText)findViewById(R.id.editUser);
		pwdEdit = (EditText)findViewById(R.id.editPwd);
		dev_show_passwd_cb = (CheckBox)findViewById(R.id.dev_show_passwd_cb);
		didEdit = (EditText)findViewById(R.id.editDID);
		btnScanId = (Button)findViewById(R.id.scanID);
		btn_config_wifi = (Button)findViewById(R.id.btn_config_wifi);
		textViewAddCamera = (TextView)findViewById(R.id.textview_add_camera);
		btnSearchCamera = (Button)findViewById(R.id.btn_searchCamera);
		searchList = (ListView)findViewById(R.id.searchcamera_listview);
		mScrollView = (ScrollView)findViewById(R.id.scrollView);
		addDescription = (LinearLayout) findViewById(R.id.add_description);
		descriptionadd = (TextView) findViewById(R.id.decriptionadd);
		reRoot = (Button) findViewById(R.id.reroot);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		 if (requestCode == 0) {
//			 if (resultCode == RESULT_OK) {
//				 try {
//					String contents =new String(data.getStringExtra("SCAN_RESULT").getBytes(),"UTF-8");
//					if(!TextUtils.isEmpty(contents)){
//						didEdit.setText(contents);
//					}else{
//						showToast(R.string.scan_cameraid_fail);
//					}
//					
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				} 
//			 }
//		 }
		 if(resultCode==2014){
			 String contents =new String(data.getStringExtra("did"));
			 Log.e("info", "back:"+contents);
				if(!TextUtils.isEmpty(contents)){
					didEdit.setText(contents);
				}else{
					showToast(R.string.scan_cameraid_fail);
				}
		 }
		 
	}
	public void setListViewHeight(){
		ListAdapter adapter = searchList.getAdapter();
		if(adapter==null){
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = adapter.getCount(); i < len; i++) {   //listAdapter.getCount()������������Ŀ
            View listItem = adapter.getView(i, null, searchList);
            listItem.measure(0, 0);  //��������View �Ŀ��
            totalHeight += listItem.getMeasuredHeight();  //ͳ������������ܸ߶�
			}
		 ViewGroup.LayoutParams params = searchList.getLayoutParams();
         params.height = totalHeight + (searchList.getDividerHeight() * (adapter.getCount() - 1));
         searchList.setLayoutParams(params);
		}
	public void showToast(String content){
		Toast.makeText(AddDeviceActivity.this, content, Toast.LENGTH_LONG).show();
	}
	public void showToast(int rid){
		String string = getResources().getString(rid);
		Toast.makeText(AddDeviceActivity.this, string, Toast.LENGTH_LONG).show();
	}
	private class ComboListViewItemClickListener implements ListViewItemClickListener {
		
		@Override
		public void onItemClick(int position) {
			// TODO Auto-generated method stub
			Log.d(TAG, "data = " + mDevTypeList[position]);
			mDevType = position;
			
			//if(!isEditName)
				devNameEdit.setText(mDevTypeList[position]);
//			HashMap<String, Object> item = new HashMap<String, Object>();
//          item.put("imageItem", R.drawable.lamp);
//          int i=position+1;
//          item.put("textItem", "设备" + i);
//          mRoomItems.add(item);
//          mRoomAdapter.notifyDataSetChanged();
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
			case ContentCommon.IPCNET_USER_SET_RESP:
				try {
					int ret = jsonData.getInt("ret");
					if(ret == 0){
						showToast(R.string.user_set_success);
					}else{
						showToast(R.string.user_set_failed);
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
}