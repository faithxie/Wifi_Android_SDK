package freesbell.demo.adapter;

import java.util.ArrayList;
import java.util.Map;

import freesbell.demo.client.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.bean.CameraParamsBean;
import freesbell.demo.client.BridgeService;
import freesbell.demo.client.MainActivity;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.DatabaseUtil;
import freesbell.demo.utils.HorizontalListView;

public class DeviceListAdapter extends BaseExpandableListAdapter {
	private static final String LOG_TAG = "DeviceListAdapter" ;	
	private MainActivity devFrgPage;
	private LayoutInflater listContainer = null;
	@SuppressWarnings("unused")
	private Context context = null;
	private DatabaseUtil dbUtil;
	private int picNumber;
	private Drawable able;
	public ArrayList<CameraListItem> mCamListItems = new ArrayList<CameraListItem>();
	private ArrayList<CameraListItem> mCamListItemsBackup = new ArrayList<CameraListItem>();
	private int mSelectedCount = 0;
	private BridgeService mBridgeService;
	
	public class CameraViewItem{
		public View convertView;
		public ImageView imgSnapShot;
		public ImageButton imgBtnSetting;
		public ImageButton imgBtnSelect;
	    public TextView devName;      
	    public TextView devID;    
	    public TextView devType;
	    public TextView devStatus;
	    public HorizontalListView mSensorListView;
	    
	    public CameraListItem mCameraListItem;
	}

	public class CameraListItem{
	    public String uuid;
	    public String name;
	    public String user;
	    public String pwd;
	    public int status;
	    public int p2p_mode;
	    ArrayList<Map<String, Object>> sl;
	    public Drawable snapshoot;
	    public ArrayList<Map<String, Object>> mSensorList;
	    public boolean mSelected;
	    
	    public CameraViewItem mCameraViewItem;
	}
	public class SensorListItem{    
		public ImageView imgSnapShot;
	    public TextView devName;
	}
	public class FourSensorListItem{
		public SensorListItem fourItem[] = new SensorListItem[4];
		public int position;
	}
	
	public DeviceListAdapter(Context ct){
		devFrgPage=(MainActivity) ct;
		context = ct;
		mBridgeService = BridgeService.mSelf;
		listContainer = LayoutInflater.from(ct);   
		dbUtil=new DatabaseUtil(context);
		mCamListItems.clear();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CameraViewItem cvi;
		CameraListItem cameraListItem = mCamListItems.get(groupPosition);
		synchronized(this){	
			if(convertView == null){
				cvi = new CameraViewItem();
				convertView = listContainer.inflate(R.layout.camera_list_item, null);
				cvi.convertView = convertView;
				cvi.imgSnapShot = (ImageView)convertView.findViewById(R.id.imgSnapshot);
				cvi.devName = (TextView)convertView.findViewById(R.id.cameraDevName) ;
				cvi.devID = (TextView)convertView.findViewById(R.id.cameraDevID) ;
				cvi.devType = (TextView)convertView.findViewById(R.id.textP2pMode);
				cvi.devStatus = (TextView)convertView.findViewById(R.id.textP2pStatus);
				cvi.imgBtnSetting=(ImageButton)convertView.findViewById(R.id.imgBtnP2pSetting);
				cvi.imgBtnSelect=(ImageButton)convertView.findViewById(R.id.devChooseImgBtn);
				convertView.setTag(cvi);
			}else{
				cvi = (CameraViewItem)convertView.getTag();
			}
			cvi.mCameraListItem = cameraListItem;
			cameraListItem.mCameraViewItem = cvi;
			MyOnTouchListener myOnTouchListener=new MyOnTouchListener(groupPosition);
			cvi.imgBtnSetting.setOnTouchListener(myOnTouchListener);
			cvi.imgBtnSelect.setTag(cvi);
			cvi.imgBtnSelect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					CameraViewItem  cli = (CameraViewItem) v.getTag();
					if(cli!=null){
						cli.mCameraListItem.mSelected = !cli.mCameraListItem.mSelected;
						if(cli.mCameraListItem.mSelected){
							mSelectedCount++;
						}else{
							mSelectedCount--;
						}
						cli.imgBtnSelect.setBackgroundResource(cli.mCameraListItem.mSelected?R.drawable.dev_choose_pressed:R.drawable.dev_choose_normal);
					}
				}
			});
			cvi.imgBtnSelect.setBackgroundResource(cameraListItem.mSelected?R.drawable.dev_choose_pressed:R.drawable.dev_choose_normal);
			Drawable drawable = cameraListItem.snapshoot;
			String did=cameraListItem.uuid;
			getFristPic(did);
			if(drawable != null){
				cvi.imgSnapShot.setBackgroundDrawable(drawable);
			}else{
				if(picNumber!=0){
					cvi.imgSnapShot.setBackgroundDrawable(able);
				}else{	
					cvi.imgSnapShot.setBackgroundResource(R.drawable.vidicon);
				}
			}
			
			cvi.devName.setText((String)cameraListItem.name);
			cvi.devID.setText((String)cameraListItem.uuid);
			
			int mode = cameraListItem.p2p_mode;
			int p2p_mode = R.string.p2p_mode_unknown;
			switch(mode){
			case ContentCommon.P2P_MODE_P2P_NORMAL:
				p2p_mode = R.string.p2p_mode_p2p_normal;
				break;
			case ContentCommon.P2P_MODE_P2P_RELAY:
				p2p_mode = R.string.p2p_mode_p2p_relay;
				break;
			default:
				p2p_mode = R.string.p2p_mode_unknown;
				break;				
			}
			
			cvi.devType.setText(p2p_mode);
			MyOnTouchLister myLister=new MyOnTouchLister(groupPosition);
			convertView.setOnTouchListener(myLister);
 
			int status = cameraListItem.status;
			
			int resid = R.string.p2p_status_unknown;
			switch(status){
			case ContentCommon.P2P_STATUS_CONNECTING:
				resid = R.string.p2p_status_connecting;
				break;
			case ContentCommon.P2P_STATUS_CONNECT_FAILED:
				resid = R.string.p2p_status_connect_failed;
				break;
			case ContentCommon.P2P_STATUS_DISCONNECT:
				resid = R.string.p2p_status_disconnect;
				break;
			case ContentCommon.P2P_STATUS_INITIALING:
				resid = R.string.p2p_status_initialing;
				break;
			case ContentCommon.P2P_STATUS_INVALID_ID:
				resid = R.string.p2p_status_invalid_id;
				break;
			case ContentCommon.P2P_STATUS_ON_LINE:
				resid = R.string.p2p_status_online;
				break;
			case ContentCommon.P2P_STATUS_DEVICE_NOT_ON_LINE:
				resid = R.string.p2p_status_offline;
				break;
			case ContentCommon.P2P_STATUS_CONNECT_TIMEOUT:
				resid = R.string.p2p_status_connect_timeout;
				break;
			case ContentCommon.P2P_STATUS_INVALID_PWD:
				resid = R.string.p2p_status_wrongpwd;
				break;
			case ContentCommon.P2P_STATUS_INVALID_USER_PWD:
				resid = R.string.p2p_status_uuid_or_pwd_invalid;
				break;
			default:
				resid = R.string.p2p_status_unknown;
			}
			cvi.devStatus.setText(resid);
		}
		return convertView;
	}
	public Drawable getFristPic(String did){
		dbUtil.open();
		Cursor cursor=dbUtil.queryFirstpic(did);
		picNumber=cursor.getCount();
		Bitmap bmp=null;
		while(cursor.moveToNext()){
			String filePath=cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_FILEPATH));
			bmp=BitmapFactory.decodeFile(filePath);
		}
		dbUtil.close();
		able=new BitmapDrawable(bmp);
		return able;
		
	}
	public int getSelectedCount(){
		return mSelectedCount;
	}
	public void deSelectAll(){
		for(CameraListItem cli:mCamListItems){
			cli.mSelected = false;
		}
		mSelectedCount = 0;
	}
	private class MyOnTouchLister implements OnTouchListener{
		private int position;
		public MyOnTouchLister(int pos) {
			position=pos;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			CameraListItem cameraListItem = mCamListItems.get(position);
			int status = cameraListItem.status;//(Integer)mapItem.get(ContentCommon.STR_PPPP_STATUS);
			return false;
		}
	}
	private class MyOnTouchListener implements OnTouchListener{
        private int position;
        private Map<String, Object> mapItem;
        public ImageButton imgBtn;
        public MyOnTouchListener(int position){
        	this.position=position;
        }
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			imgBtn=(ImageButton)v;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				devFrgPage.showSettingPopWindow(position);
				break;
			}
			return false;
		}
		
	}
	
	public CameraListItem getItemCamera(int position){
		return mCamListItems.get(position);
	}
	/**
	 * �������б�����������
	 * @param mac
	 * @param ipaddr
	 * @param port
	 */
	public boolean addCamera(String name, String did, String user, String pwd){
		if(!CheckCameraInfo(did)){
			return false;
		}

		CameraListItem cli = new CameraListItem();
		cli.snapshoot = null;
		cli.name = name;
		cli.uuid = did;
		cli.user = user;
		cli.pwd = pwd;
		cli.status = ContentCommon.P2P_STATUS_UNKNOWN;
		cli.p2p_mode = ContentCommon.P2P_MODE_UNKNOWN;
		cli.mSelected = false;
		
		synchronized(this){
			mCamListItems.add(cli);
		}
		
		return true;
	}
	
	public boolean addCamera(String name, String did, String user, String pwd,int status,int p2p_mode,ArrayList<Map<String, Object>> sl){
		if(!CheckCameraInfo(did)){
			return false;
		}

		synchronized(this){
			CameraListItem cli = new CameraListItem();
			cli.snapshoot = null;
			cli.name = name;
			cli.uuid = did;
			cli.user = user;
			cli.pwd = pwd;
			cli.status = status;
			cli.sl = sl;
			cli.p2p_mode = p2p_mode;
			cli.mSelected = false;
			
			mCamListItems.add(cli);
		}

		return true;
	}
	
	/**
	 * 
	 * @param did
	 * @param status
	 * @return
	 */
	public boolean UpdataCameraStatus(String did, int status){	
		//Log.d(LOG_TAG, "UpdataCameraStatus... did: " + did + " status: " + status);
		synchronized(this){
			Log.d(LOG_TAG,"UpdataCameraStatus:" + did + " status:" + status);
			for(CameraListItem cli:mCamListItems){
				if(did.equals(cli.uuid)){
					if(cli.status == status){
						return false;
					}
					cli.status = status;
					return true;
				}
			}
			return false;
		}
	}
	/**
	 * ����Ƿ��ظ�
	 * @param mac
	 * @return
	 */
	private boolean CheckCameraInfo(String did) {
		synchronized(this){
			for(CameraListItem cli:mCamListItems){
				String strdid = cli.uuid;
				if(strdid.equals(did)){
					return false;
				}
			}
			return true;
		}
	}

	public CameraListItem getItemContent(int pos) {
		// TODO Auto-generated method stub
		synchronized(this){
			if(pos > mCamListItems.size()){
				return null;
			}

			CameraListItem cli = mCamListItems.get(pos);
			return cli;
		}
	}
    public ArrayList<CameraParamsBean>   getmListItems(){
    	ArrayList<CameraParamsBean> list=new ArrayList<CameraParamsBean>();
    	for(int i=0;i<mCamListItems.size();i++){
    		CameraParamsBean bean=new CameraParamsBean(BridgeService.mSelf);
//    		Map<String, Object> map = mListItems.get(i);
    		CameraListItem cli = mCamListItems.get(i);
    		String name=cli.name;
    		String did=cli.uuid;
    		String user=cli.user;
    		String pwd=cli.pwd;
    		int status=cli.status;
    		bean.setDid(did);
    		bean.setName(name);
    		bean.setPwd(pwd);
    		bean.setStatus(status);
    		bean.setUser(user);
    		list.add(bean);
    	}
    	
    	
    	return list ;
    }
    
	public boolean delCamera(String did) {
		Log.d(LOG_TAG, "Call delCamera");
		synchronized(this){
			for(CameraListItem cli:mCamListItems){
				if(did.equals(cli.uuid)){
					mCamListItems.remove(cli) ;
					return true;
				}
			}		
			return false;
		}
	}

	public boolean UpdateCamera(String oldDID, 
			String name, String did, String user, String pwd) {
		synchronized(this){
			for(CameraListItem cli:mCamListItems){
				if(oldDID.equals(cli.uuid)){
					cli.name = name;
					cli.user = did;
					cli.user = user;
					cli.pwd = pwd;
					cli.p2p_mode = ContentCommon.PPPP_DEV_TYPE_UNKNOWN;
					cli.status = ContentCommon.P2P_STATUS_UNKNOWN;
					return true;
				}
			}
			return false;
		}
	}

	public void UpdateAllCamera(){
		for(CameraListItem cli:mCamListItems){
			CameraParamsBean cbp = mBridgeService.getCamera(cli.uuid);
			if(cbp!=null){
//				cli.name = cbp.name;
				cli.user = cbp.user;
				cli.pwd = cbp.pwd;
				cli.p2p_mode = cbp.p2p_mode;
				cli.status = cbp.status;
			}
		}
	}

	public boolean UpdateCameraImage(String did, Drawable drawable) {
		synchronized(this){
			for(CameraListItem cli:mCamListItems){
				if(did.equals(cli.uuid)){
					cli.snapshoot = drawable;
					return true;
				}
			}
			return false;
		}
	}
	@Override
	public int getGroupCount() {
		if(mCamListItems==null){
			return 0;
		}
		return mCamListItems.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		if(mCamListItems.size()<=groupPosition){
			return 0;
		}
		CameraListItem cli = mCamListItems.get(groupPosition);
		if(cli == null)
			return 0;
		ArrayList<Map<String, Object>> sensorList = cli.sl;
		if(sensorList == null)
			return 0;
		return sensorList.size()/4;
	}
	@Override
	public Object getGroup(int groupPosition) {
		if(mCamListItems==null){
			return  null;
		}
		return mCamListItems.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(mCamListItems.size()<=groupPosition){
			return null;
		}
		CameraListItem cli = mCamListItems.get(groupPosition);
		if(cli == null)
			return null;
		ArrayList<Map<String, Object>> sensorList = cli.sl;
		if(sensorList == null)
			return null;
		return sensorList.get(childPosition);
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (groupPosition+1)*childPosition;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		CameraListItem cli = mCamListItems.get(groupPosition);
		synchronized(this){
			FourSensorListItem  cameraListItem = null; 		
			if(convertView == null){
				cameraListItem = new FourSensorListItem();  
				cameraListItem.position = childPosition;
				convertView = listContainer.inflate(R.layout.horizontal_list_item, null);
				
				SensorListItem sensor = new SensorListItem();
				setChildItemClickListener(convertView.findViewById(R.id.layout_item_id0),cameraListItem);
				sensor.imgSnapShot = (ImageView)convertView.findViewById(R.id.img_list_item0);
				sensor.devName = (TextView)convertView.findViewById(R.id.text_list_item0) ;
				cameraListItem.fourItem[0] = sensor;
				
				sensor = new SensorListItem();
				setChildItemClickListener(convertView.findViewById(R.id.layout_item_id1),cameraListItem);
				sensor.imgSnapShot = (ImageView)convertView.findViewById(R.id.img_list_item1);
				sensor.devName = (TextView)convertView.findViewById(R.id.text_list_item1) ;
				cameraListItem.fourItem[1] = sensor;
				
				sensor = new SensorListItem();
				setChildItemClickListener(convertView.findViewById(R.id.layout_item_id2),cameraListItem);
				sensor.imgSnapShot = (ImageView)convertView.findViewById(R.id.img_list_item2);
				sensor.devName = (TextView)convertView.findViewById(R.id.text_list_item2) ;
				cameraListItem.fourItem[2] = sensor;
				
				sensor = new SensorListItem();
				setChildItemClickListener(convertView.findViewById(R.id.layout_item_id3),cameraListItem);
				sensor.imgSnapShot = (ImageView)convertView.findViewById(R.id.img_list_item3);
				sensor.devName = (TextView)convertView.findViewById(R.id.text_list_item3) ;
				cameraListItem.fourItem[3] = sensor;
				convertView.setTag(cameraListItem);
			}else{
				cameraListItem = (FourSensorListItem)convertView.getTag();
			}	
		
			ArrayList<Map<String, Object>> sensorList = cli.sl;
			
			int startno = childPosition*4;
			if(startno + 4>sensorList.size())
				return convertView;

			for(int i = 0;i<4;i++){
				Map<String, Object> chmap = sensorList.get(startno + i);
				SensorListItem sensor = cameraListItem.fourItem[i];
				sensor.imgSnapShot.setBackgroundResource(R.drawable.vidicon);
				sensor.devName.setText((String)chmap.get(ContentCommon.STR_CAMERA_NAME));
			}
		}
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void setChildItemClickListener(View v,FourSensorListItem cli){
		v.setTag(cli);
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				FourSensorListItem cli = (FourSensorListItem)v.getTag();
				Log.d(LOG_TAG,"line" + cli.position + " click!");
				switch(v.getId()){
				case R.id.layout_item_id0:
					Log.d(LOG_TAG,"list_item0" + cli.fourItem[0].devName);
					break;
				case R.id.layout_item_id1:
					Log.d(LOG_TAG,"list_item1" + cli.fourItem[1].devName);
					break;
				case R.id.layout_item_id2:
					Log.d(LOG_TAG,"list_item2" + cli.fourItem[2].devName);
					break;
				case R.id.layout_item_id3:
					Log.d(LOG_TAG,"list_item3" + cli.fourItem[3].devName);
					break;
				default:;
				}
			}
		});
	}
	
	private boolean mFilterMode = false;
	public boolean isFilterMode(){
		return mFilterMode;
	}
	public void filterDevice(String filter){
		if(!mFilterMode){
			mFilterMode = true;
			mCamListItemsBackup.clear();
			for(CameraListItem cli:mCamListItems){
				mCamListItemsBackup.add(cli);
			}
		}
		mCamListItems.clear();
		
		for(CameraListItem cli:mCamListItemsBackup){
			if(cli.uuid.contains(filter) || cli.name.contains(filter)){
				mCamListItems.add(cli);
			}
		}
	}
	public void restoreDeviceList(){
		if(mFilterMode){
			mFilterMode = false;
			mCamListItems.clear();
			for(CameraListItem cli:mCamListItemsBackup){
				mCamListItems.add(cli);
			}
		}
	}
}