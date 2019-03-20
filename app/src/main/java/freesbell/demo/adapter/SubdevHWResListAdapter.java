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
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import freesbell.demo.bean.HwResBean;
import freesbell.demo.bean.WifiScanBean;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SubdevHWResListAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<HwResBean> list;
	private ViewHolder holder;
	private OnClickListener mOnClickListener;
	private OnClickListener mOnCheckedClickListener;
	private boolean isInList,isEditMode;
	public SubdevHWResListAdapter(Context context,ArrayList<HwResBean> hrl,boolean inlist,boolean edit){
		this.context=context;
		inflater=LayoutInflater.from(context);

		if(hrl!=null){
			list = hrl;
		}else{
			list=new ArrayList<HwResBean>();
		}
		isInList = inlist;
		isEditMode = edit;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=inflater.inflate(R.layout.hw_res_list_item, null);
			holder = new ViewHolder();
			holder.hw_res_img = (ImageView)convertView.findViewById(R.id.hw_res_img);
			holder.hw_res_name=(TextView)convertView.findViewById(R.id.hw_res_name);
			holder.hw_res_mode=(TextView)convertView.findViewById(R.id.hw_res_mode);
			holder.hw_res_setting = (ImageButton)convertView.findViewById(R.id.hw_res_setting);
			holder.hw_res_checkbox_inlist = (ImageView)convertView.findViewById(R.id.hw_res_checkbox_inlist);
			holder.hw_res_checkbox_outlist = (ImageView)convertView.findViewById(R.id.hw_res_checkbox_outlist);
			//if(mOnClickListener!=null){
				holder.hw_res_setting.setOnClickListener(mOnClickListener);
				holder.hw_res_checkbox_inlist.setOnClickListener(mOnCheckedClickListener);
				holder.hw_res_checkbox_outlist.setOnClickListener(mOnCheckedClickListener);
			//}
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		HwResBean hrb = list.get(position);
		hrb.tag = holder;
		holder.hw_res_setting.setTag(hrb);
		if(isInList){
			holder.hw_res_checkbox_inlist.setTag(hrb);
		}else{
			holder.hw_res_checkbox_outlist.setTag(hrb);
		}
		
		if(list.size()==0){
			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
		}else if(position==0){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
		}else if(position==list.size()-1){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
		}else{
		convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		}
		holder.hw_res_name.setText("gpio" + hrb.index);
		if(isInList){
			holder.hw_res_checkbox_inlist.setImageResource(hrb.insel ? R.drawable.btn_check_on_holo_light
	                : R.drawable.btn_check_off_holo_light);
		}else{
			holder.hw_res_checkbox_outlist.setImageResource(hrb.outsel ? R.drawable.btn_check_on_holo_light
	                : R.drawable.btn_check_off_holo_light);
		}
		setHwImg(position);
		setHwMode(position);
		
		if(isEditMode){
			holder.hw_res_setting.setVisibility(View.GONE);
			if(isInList){
				holder.hw_res_checkbox_outlist.setVisibility(View.GONE);
				holder.hw_res_checkbox_inlist.setVisibility(View.VISIBLE);
			}else{
				holder.hw_res_checkbox_inlist.setVisibility(View.GONE);
				holder.hw_res_checkbox_outlist.setVisibility(View.VISIBLE);
			}
		}else{
			holder.hw_res_checkbox_inlist.setVisibility(View.GONE);
			holder.hw_res_checkbox_outlist.setVisibility(View.GONE);
			holder.hw_res_setting.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public void setOnClickListener(OnClickListener l){
		mOnClickListener = l;
	}
	public void setOnCheckedChangeListener(OnClickListener l){
		mOnCheckedClickListener = l;
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
    	ImageView hw_res_checkbox_outlist,hw_res_checkbox_inlist;
    }

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
    
//	public void expandItem(int pos){
//		HwResBean hrb = list.get(pos);
//		
//		ViewHolder hdr = (ViewHolder) hrb.tag;
//
//	}
	public void setEditMode(boolean d){
		isEditMode = d;
	}

}
