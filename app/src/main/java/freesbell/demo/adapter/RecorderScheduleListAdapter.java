package freesbell.demo.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import freesbell.demo.client.R;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import freesbell.demo.bean.FileItem;
import freesbell.demo.bean.WifiScanBean;
import android.widget.ImageView;
import android.widget.TextView;

public class RecorderScheduleListAdapter extends BaseAdapter{
	private final String TAG = "RemoteFileListAdapter";
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<RecorderSchedule> list;
	private ViewHolder holder;
	public RecorderScheduleListAdapter(Context context,ArrayList<RecorderSchedule> l){
		this.context=context;
		inflater=LayoutInflater.from(context);
		list=l;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position>list.size()){
			Log.e(TAG,"RecorderScheduleListAdapter position:" + position);
			return null;
		}
		RecorderSchedule rs = list.get(position);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.recorder_schedule_list_item, null);
			//convertView = LayoutInflater.from(context).inflate(R.layout.recorder_schedule_list_item,null);
			holder = new ViewHolder();
			holder.time_enable=(CheckBox)convertView.findViewById(R.id.time_enable);
			holder.date_id=(TextView)convertView.findViewById(R.id.date_id);
			holder.time_begin1=(TextView)convertView.findViewById(R.id.time_begin1);
			holder.time_end1=(TextView)convertView.findViewById(R.id.time_end1);
			holder.time_begin2=(TextView)convertView.findViewById(R.id.time_begin2);
			holder.time_end2=(TextView)convertView.findViewById(R.id.time_end2);
			convertView.setTag(holder);
			
			holder.time_enable.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					RecorderSchedule rsc = (RecorderSchedule)buttonView.getTag();
					if(rsc!=null){
						rsc.enable = isChecked;
					}
				}
			});
			
			holder.time_begin1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ViewHolder holder=(ViewHolder)v.getTag();
					if(holder!=null){
						showTimePicker(0,holder.mRecSch,holder.time_begin1);
						//holder.mRecSch.start1 = holder.time_begin1.getText().toString();
						Log.d(TAG,"start1:" + holder.mRecSch.start1);
					}
				}
				
			});
			holder.time_end1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ViewHolder holder=(ViewHolder)v.getTag();
					if(holder!=null){
						showTimePicker(1,holder.mRecSch,holder.time_end1);
//						holder.mRecSch.end1 = holder.time_end1.getText().toString();
//						Log.d(TAG,"end1:" + holder.mRecSch.end1);
					}
				}
				
			});
			holder.time_begin2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ViewHolder holder=(ViewHolder)v.getTag();
					if(holder!=null){
						showTimePicker(2,holder.mRecSch,holder.time_begin2);
//						holder.mRecSch.start2 = holder.time_begin2.getText().toString();
//						Log.d(TAG,"start2:" + holder.mRecSch.start2);
					}
				}
				
			});
			holder.time_end2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ViewHolder holder=(ViewHolder)v.getTag();
					if(holder!=null){
						showTimePicker(3,holder.mRecSch,holder.time_end2);
//						holder.mRecSch.end2 = holder.time_end2.getText().toString();
					}
				}
				
			});
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
//		Log.d(TAG,"getView");
		if(list.size()==0){
			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
		}else if(position==0){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
		}else if(position==list.size()-1){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
		}else{
			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		}
		switch(position){
		case 0://every day.
			holder.date_id.setText("每天");
			break;
		case 1:
			holder.date_id.setText("周日");
			break;
		case 2:case 3:
		case 4:case 5:
		case 6:case 7:
			holder.date_id.setText("周" + (position - 1));
			break;
		}
		holder.time_enable.setTag(rs);
		holder.time_enable.setChecked(rs.enable);

		holder.mRecSch = rs;
		
		holder.time_begin1.setTag(holder);
		holder.time_begin1.setText(rs.start1);
//		Log.d(TAG,"start1:"+rs.start1);
		holder.time_end1.setTag(holder);
		holder.time_end1.setText( rs.end1);
//		Log.d(TAG,"end1:"+rs.end1);
		holder.time_begin2.setTag(holder);
		holder.time_begin2.setText( rs.start2);
//		Log.d(TAG,"start2:"+rs.start2);
		holder.time_end2.setTag(holder);
		holder.time_end2.setText(rs.end2);
//		Log.d(TAG,"end2:"+rs.end2);
		
		return convertView;
	}
	
	public RecorderSchedule getFileItem(int position){
		return list.get(position);
	}
	public ArrayList<RecorderSchedule> getFileItemList(){
		return list;
	}
	public void setFileItemList(ArrayList<RecorderSchedule> l){
		list = l;
	}
	
//	private String mTmpTimeStr;
	private String getTimeByIndex(RecorderSchedule rs,int i){
		switch(i){
		case 0:
			return rs.start1;
		case 1:
			return rs.end1;
		case 2:
			return rs.start2;
		case 3:
			return rs.end2;
		}
		
		return null;
	}
	private void setTimeByIndex(RecorderSchedule rs,int i,String str,int h,int m,int s){
		switch(i){
		case 0:
			rs.start1 = str;
			
			rs.start1hour = h;
			rs.start1min = m;
			rs.start1sec = s;
			break;
		case 1:
			rs.end1 = str;
			
			rs.end1hour = h;
			rs.end1min = m;
			rs.end1sec = s;
			break;
		case 2:
			rs.start2 = str;
			
			rs.start2hour = h;
			rs.start2min = m;
			rs.start2sec = s;
			break;
		case 3:
			rs.end2 = str;
			
			rs.end2hour = h;
			rs.end2min = m;
			rs.end2sec = s;
			break;
		}
	}
	private void showTimePicker(final int i,final RecorderSchedule rs,final TextView time_tv){
//		d = "1970-01-01 " + d;
//		final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = null;
//		try {
//			date = f.parse(d);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return;
//		}
		String d = getTimeByIndex(rs,i);
		if(d==null)
			return;
		
		int j=0;
		for(;j<d.length();j++){
			if(d.charAt(j)==':'){
				break;
			}
		}
		String hstr = d.substring(0,j);
		j++;
		
		int msi = j;
		for(;j<d.length();j++){
			if(d.charAt(j)==':'){
				break;
			}
		}
		String mstr = d.substring(msi,j);
		int hourOfDay = Integer.parseInt(hstr), minute = Integer.parseInt(mstr);
//		
//		RecorderTime rt = getTimeByIndex(rs,i);
//		int hourOfDay = rt.hour, minute = rt.min;
		
		TimePickerDialog dialog = new TimePickerDialog(context,
		new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay,
					int minute) {
				String timeStr = String.format("%1$02d:%2$02d:59",hourOfDay,minute);
				Log.d(TAG,"timeStr:"+timeStr);
				time_tv.setText(timeStr);
//				mTmpTimeStr = timeStr;
				setTimeByIndex(rs,i,timeStr,hourOfDay,minute,59);
			}
			
		},hourOfDay, minute,true);
		dialog.show();
		
//		d = mTmpTimeStr;
	}
    private class ViewHolder{
    	CheckBox time_enable;
    	TextView date_id;
    	TextView time_begin1;
    	TextView time_end1;
    	TextView time_begin2;
    	TextView time_end2;
    	RecorderSchedule mRecSch;
    }
    public class RecorderSchedule{
		public boolean enable;
		public int index;
//		public RecorderTime start1,end1;
//		public RecorderTime start2,end2;
		public String start1,end1;
		public String start2,end2;
		
		public int start1hour;
		public int start1min;
		public int start1sec;
		public int end1hour;
		public int end1min;
		public int end1sec;
		public int start2hour;
		public int start2min;
		public int start2sec;
		public int end2hour;
		public int end2min;
		public int end2sec;
	}
}
