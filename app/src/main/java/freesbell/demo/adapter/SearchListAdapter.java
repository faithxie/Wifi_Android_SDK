package freesbell.demo.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freesbell.demo.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import freesbell.demo.content.ContentCommon;

public class SearchListAdapter extends BaseAdapter {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "SearchListAdapter" ;	
	
	private LayoutInflater listContainer = null;
	@SuppressWarnings("unused")
	private Context context = null;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();;
	
	public final class SearchListItem{    
	    public TextView devName;      
	    public TextView devID;             
	}    
	
	public SearchListAdapter(Context ct){
		context = ct;
		listContainer = LayoutInflater.from(ct);   
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		SearchListItem  searchListItem = null; 		
		if(convertView == null){
			searchListItem = new SearchListItem();  
			convertView = listContainer.inflate(R.layout.search_list_item, null);    
			searchListItem.devName = (TextView)convertView.findViewById(R.id.searchDevName) ;
			searchListItem.devID = (TextView)convertView.findViewById(R.id.searchDevID) ;
			convertView.setTag(searchListItem);
		}else{
			searchListItem = (SearchListItem)convertView.getTag();
		}		
//		if(listItems.size()==1){
//			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
//		}else if(position==0){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
//		}else if(position==listItems.size()-1){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
//		}else{
//		}
		convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		searchListItem.devName.setText((String)listItems.get(position).get(ContentCommon.STR_CAMERA_NAME));
		searchListItem.devID.setText((String)listItems.get(position).get(ContentCommon.STR_CAMERA_ID));
        
		return convertView;
	}
	
	/**
	 * �������б�����������
	 * @param mac
	 * @param ipaddr
	 * @param port
	 */
	public boolean AddCamera(String mac, String name, String did){
		
		//��������MAC�ظ�������ӵ��б���
		if(!CheckCameraInfo(mac)){
			return false;
		}
		for(Map<String, Object> map:listItems){
			String uuid = (String) map.get(ContentCommon.STR_CAMERA_ID);
			if(uuid!=null&&uuid.contentEquals(did)){
				return true;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put(ContentCommon.STR_CAMERA_MAC, mac) ;
		map.put(ContentCommon.STR_CAMERA_NAME, name);
		map.put(ContentCommon.STR_CAMERA_ID, did);		
		listItems.add(map) ;		
		return true;
	}
	
	public boolean AddCamera(String mac, String name, String did,int devType){
		
		//��������MAC�ظ�������ӵ��б���
		if(!CheckCameraInfo(mac)){
			return false;
		}
		for(Map<String, Object> map:listItems){
			String uuid = (String) map.get(ContentCommon.STR_CAMERA_ID);
			if(uuid!=null&&uuid.contentEquals(did)){
				return true;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put(ContentCommon.STR_CAMERA_MAC, mac) ;
		map.put(ContentCommon.STR_CAMERA_NAME, name);
		map.put(ContentCommon.STR_CAMERA_ID, did);
		map.put(ContentCommon.STR_DEVICE_TYPE, devType);
		listItems.add(map) ;		
		return true;
	}

	/**
	 * ����Ƿ��ظ�
	 * @param mac
	 * @return
	 */
	private boolean CheckCameraInfo(String mac) {
		// TODO Auto-generated method stub
		//�����б?����Ƿ�����ͬmac��ַ�������
		int size = listItems.size();
		int i;
		for(i = 0; i < size; i++){
			String strMac = (String)listItems.get(i).get(ContentCommon.STR_CAMERA_MAC);
			if(mac.equals(strMac)){
				return false;
			}
		}
		return true;
	}

	public Map<String, Object> getItemContent(int pos) {
		// TODO Auto-generated method stub
		if(pos > listItems.size()){
			return null;
		}
		return listItems.get(pos);
	}
	
	public void ClearAll(){
		listItems.clear();
	}
	
}