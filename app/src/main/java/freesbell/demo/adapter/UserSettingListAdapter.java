package freesbell.demo.adapter;

import java.util.ArrayList;
import java.util.List;

import freesbell.demo.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserSettingListAdapter extends BaseAdapter {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "SettingListAdapter" ;	
	public boolean mUserStatusShow = false;
	private LayoutInflater listContainer = null;
	@SuppressWarnings("unused")
	private Context context = null;
	private List<String> listItems = new ArrayList<String>();
	
	public final class SettingListItem{    
	    public TextView SettingName; 
	    public ImageView SettingImg;
	}    
	
	public UserSettingListAdapter(Context ct,List<String> listItems){
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SettingListItem  settingListItem = null; 		
		if(convertView == null){
			settingListItem = new SettingListItem();
//			if(position == 0){
//				
//				convertView = listContainer.inflate(R.layout.user_status_list_item, null); 
//				ImageView img = (ImageView)convertView.findViewById(R.id.settingImg);
//				img.setImageResource(R.drawable.arrowdown);
//			}else if(position == 1){
				convertView = listContainer.inflate(R.layout.user_setting_list_item, null); 
			//}  
			settingListItem.SettingName = (TextView)convertView.findViewById(R.id.settingName) ;
			settingListItem.SettingImg=(ImageView)convertView.findViewById(R.id.settingImg);
			convertView.setTag(settingListItem);
		}else{
			settingListItem = (SettingListItem)convertView.getTag();
		}			
		/*if(position==0){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
		}else if(position==listItems.size()-1){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
		}else{
			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		}*/
		settingListItem.SettingName.setText((String)listItems.get(position));
		return convertView;
	}
	
	public void ClearAll(){
		listItems.clear();
	}
	
}