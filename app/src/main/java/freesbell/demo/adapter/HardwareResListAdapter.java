package freesbell.demo.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import freesbell.demo.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.bean.HwResBean;
import freesbell.demo.bean.WifiScanBean;

public class HardwareResListAdapter extends BaseExpandableListAdapter{

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<HwResBean> list;
	private ViewHolder holder;
	private OnClickListener mOnClickListener;
	public HardwareResListAdapter(Context context,ArrayList<HwResBean> hrl){
		this.context=context;
		inflater=LayoutInflater.from(context);

		if(hrl!=null){
			list = hrl;
		}else{
			list=new ArrayList<HwResBean>();
		}
	}
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		if(convertView==null){
//			convertView=inflater.inflate(R.layout.hw_res_list_item, null);
//			holder = new ViewHolder();
//			holder.hw_res_img = (ImageView)convertView.findViewById(R.id.hw_res_img);
//			holder.hw_res_name=(TextView)convertView.findViewById(R.id.hw_res_name);
//			holder.hw_res_mode=(TextView)convertView.findViewById(R.id.hw_res_mode);
//			holder.hw_res_setting = (ImageButton)convertView.findViewById(R.id.hw_res_setting);
//			if(mOnClickListener!=null){
//				holder.hw_res_setting.setOnClickListener(mOnClickListener);
//			}
//			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder)convertView.getTag();
//		}
//		HwResBean hrb = list.get(position);
//		hrb.tag = holder;
//		holder.hw_res_setting.setTag(hrb);
////		if(list.size()==0){
////			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
////		}else if(position==0){
////			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
////		}else if(position==list.size()-1){
////			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
////		}else{
//		convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
////		}
//		holder.hw_res_name.setText(hrb.name);
//		setHwImg(position);
//		setHwMode(position);
//		
//
//		return convertView;
//	}

	public void setOnClickListener(OnClickListener l){
		mOnClickListener = l;
	}
	private void setHwMode(int pos){
		HwResBean hrb = list.get(pos);
		int resid = R.string.hw_res_out;
		switch(hrb.mode){
		case 0://out
			resid = R.string.hw_res_out;
			break;
		case 1://in
			resid = R.string.hw_res_in;
			break;
		case 2://adc
			resid = R.string.hw_res_adc;
			break;
		case 3://dac
			resid = R.string.hw_res_dac;
			break;
		case 4://pwm
			resid = R.string.hw_res_pwm;
			break;
			default:;
		}
		ViewHolder hdr = (ViewHolder) hrb.tag;
		hdr.hw_res_mode.setText(resid);
	}
	public void setHwImg(int pos){
		HwResBean hrb = list.get(pos);
		ViewHolder hdr = (ViewHolder) hrb.tag;
		int resid = R.drawable.gpio_off;
		switch(hrb.mode){
		case 0://out
			if(hrb.value == 0){
				resid = R.drawable.gpio_off;
			}else if(hrb.value == 1){
				resid = R.drawable.gpio_on;
			}
			hdr.hw_res_img.setImageResource(resid);
			break;
		case 1://in
			if(hrb.value == 0){
				resid = R.drawable.gpio_off;
			}else if(hrb.value == 1){
				resid = R.drawable.gpio_on;
			}
			hdr.hw_res_img.setImageResource(resid);
			break;
		case 2://adc
			break;
		case 3://dac
			break;
		case 4://pwm
			break;
			default:;
		}
	}

    private class ViewHolder{
    	ImageView hw_res_img;
    	TextView hw_res_name;
    	TextView hw_res_mode;
    	ImageButton hw_res_setting;
    	EditText hw_res_value;
    }

	@Override
	public int getGroupCount() {
		return list.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition);
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		HwResBean hrb = list.get(groupPosition);
//		holder = (ViewHolder) hrb.tag;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.hw_res_list_item, null);
//			if(holder==null)
			holder = new ViewHolder();
			holder.hw_res_img = (ImageView)convertView.findViewById(R.id.hw_res_img);
			holder.hw_res_name=(TextView)convertView.findViewById(R.id.hw_res_name);
			holder.hw_res_mode=(TextView)convertView.findViewById(R.id.hw_res_mode);
			holder.hw_res_setting = (ImageButton)convertView.findViewById(R.id.hw_res_setting);
			if(mOnClickListener!=null){
				holder.hw_res_setting.setOnClickListener(mOnClickListener);
			}
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
//		HwResBean hrb = list.get(groupPosition);
		hrb.tag = holder;
		holder.hw_res_setting.setTag(hrb);

		convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);

		holder.hw_res_name.setText("gpio" + hrb.index);//holder.hw_res_name.setText(hrb.name);
		setHwImg(groupPosition);
		setHwMode(groupPosition);

		return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		HwResBean hrb = list.get(groupPosition);
//		holder = (ViewHolder) hrb.tag;
//		if(convertView==null){
//			convertView=inflater.inflate(R.layout.hw_res_list_sub_item, null);
//			if(holder==null)
//				holder = new ViewHolder();
//			holder.hw_res_value=(EditText)convertView.findViewById(R.id.hw_res_value);
////			holder.hw_res_img = (ImageView)convertView.findViewById(R.id.hw_res_img);
////			holder.hw_res_name=(TextView)convertView.findViewById(R.id.hw_res_name);
////			holder.hw_res_mode=(TextView)convertView.findViewById(R.id.hw_res_mode);
////			holder.hw_res_setting = (ImageButton)convertView.findViewById(R.id.hw_res_setting);
//			if(mOnClickListener!=null){
//				holder.hw_res_value.setOnClickListener(mOnClickListener);
//			}
//			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder)convertView.getTag();
//		}
////		HwResBean hrb = list.get(groupPosition);
//		hrb.tag = holder;
//		holder.hw_res_setting.setTag(hrb);
//		holder.hw_res_value.setTag(hrb);
////
////		convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
////
////		holder.hw_res_name.setText(hrb.name);
////		setHwImg(groupPosition);
////		setHwMode(groupPosition);

		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
    
	public void expandItem(int pos){
		HwResBean hrb = list.get(pos);
		
		ViewHolder hdr = (ViewHolder) hrb.tag;
//		hdr.hw_res_value.getFocusables(0);
//		hdr.hw_res_value.setFocusable(true);
//		hdr.hw_res_value.setFocusableInTouchMode(true);
//		hdr.hw_res_value.requestFocus();
	}
//	public void getEditTextFocus(HwResBean hrb){
//		ViewHolder hdr = (ViewHolder) hrb.tag;
////		hdr.hw_res_value.getFocusables(0);
//		hdr.hw_res_value.setFocusable(true);
//		hdr.hw_res_value.setFocusableInTouchMode(true);
//		hdr.hw_res_value.requestFocus();
//	}
}
