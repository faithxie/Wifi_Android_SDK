package freesbell.demo.adapter;

import java.util.ArrayList;
import java.util.List;

import freesbell.demo.client.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.client.DeviceAttributeActivity.DeviceAttrBean;

public class DeviceAttributeListAdapter extends BaseAdapter {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "SettingListAdapter" ;	
	
	private LayoutInflater listContainer = null;
	@SuppressWarnings("unused")
	private Context context = null;
	private List<DeviceAttrBean> listItems = new ArrayList<DeviceAttrBean>();
	
	public final class SettingListItem{    
	    public TextView SettingName; 
	    public ImageView SettingImg;
	    public TextView attribute_info; 
	}    
	
	public DeviceAttributeListAdapter(Context ct,List<DeviceAttrBean> listItems){
		context = ct;
		this.listItems=listItems;
		listContainer = LayoutInflater.from(ct);   
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SettingListItem  settingListItem = null; 		
		if(convertView == null){
			settingListItem = new SettingListItem();  
			convertView = listContainer.inflate(R.layout.device_attribute_list_item, null);    
			settingListItem.SettingName = (TextView)convertView.findViewById(R.id.settingName) ;
			settingListItem.SettingImg=(ImageView)convertView.findViewById(R.id.settingImg);
			settingListItem.attribute_info = (TextView)convertView.findViewById(R.id.attribute_info) ;
			convertView.setTag(settingListItem);
		}else{
			settingListItem = (SettingListItem)convertView.getTag();
		}			
//		if(position==0){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
//		}else if(position==listItems.size()-1){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
//		}else{
			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
//		}
		settingListItem.SettingName.setText(listItems.get(position).name);
		settingListItem.attribute_info.setText(listItems.get(position).info);
		return convertView;
	}
	
	public void ClearAll(){
		listItems.clear();
	}
	
}