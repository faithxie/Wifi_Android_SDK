package freesbell.demo.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import freesbell.demo.client.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.bean.FileItem;
import freesbell.demo.bean.WifiScanBean;

public class RemoteFileListAdapter extends BaseAdapter{
	private final String TAG = "RemoteFileListAdapter";
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<FileItem> list;
	private ViewHolder holder;
	private String mSDRootDir;
	private String mRecordDate;
	private String mCurPathPrefix;
	public RemoteFileListAdapter(Context context,ArrayList<FileItem> l){
		this.context=context;
		inflater=LayoutInflater.from(context);
		list=l;
		mSDRootDir = "/mnt/sdcard0/media/sensor0";
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
		if(convertView==null){
			convertView=inflater.inflate(R.layout.remote_file_list_item, null);
			holder = new ViewHolder();
			holder.fileicon=(ImageView)convertView.findViewById(R.id.file_icon_id);
			holder.filename=(TextView)convertView.findViewById(R.id.file_name_id);
			holder.fileinfo=(TextView)convertView.findViewById(R.id.file_info_id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
//		Log.d(TAG,"getView");
//		if(list.size()==0){
//			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
//		}else if(position==0){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
//		}else if(position==list.size()-1){
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
//		}else{
//			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
//		}
		FileItem fi = list.get(position);
		holder.filename.setText(fi.name);

		Log.d(TAG,fi.path);
		if(fi.type == 4){
			holder.fileinfo.setText("RootDir" + fi.path.substring(mSDRootDir.length()));
			holder.fileicon.setImageResource(R.drawable.file_icon_theme);
		}else{
			//if(mRecordDate == null){
				//if(fi.parent!=null&&fi.parent.name!=null && fi.parent.parent!=null && fi.parent.parent.name !=null){
				if(fi.path.length()>mSDRootDir.length()+10){
					if(mCurPathPrefix==null||!fi.path.contentEquals(mCurPathPrefix)){
						String ys = fi.path.substring(mSDRootDir.length()+1);
						if(ys.length()>9 && ys.charAt(6) == '/'){
						//String ms = ys.substring(4,5);
						String ds = ys.substring(7,9);
						ys = ys.substring(0,6);
	//					int ym = Integer.parseInt(fi.parent.parent.name);
	//					int y = ym/100;
	//					int m = ym%100;
	//					int d = Integer.parseInt(fi.parent.name);
	//					mRecordDate = y + "/" + m + "/" + d + "-";
						int ym = Integer.parseInt(ys);
						int y = ym/100;
						int m = ym%100;
						int d = Integer.parseInt(ds);
						mRecordDate = y + "/" + m + "/" + d + "-";
						}else{
							mRecordDate = "unkwon";
						}
						mCurPathPrefix = fi.path.substring(0,mSDRootDir.length()+10);
					}
				}
			//}
			if(fi.name.length()>10 && fi.name.charAt(6) == '-'){
				int start,end = 0;
				String startStr = fi.name.substring(0, 6);
				String endStr = null;
				start = Integer.parseInt(startStr);
				int h = start/10000;
				int m = start%10000;
				int s = start%100;
				m = m/100;
				DecimalFormat df = new DecimalFormat();
				df.applyPattern("##");
				String startDateTime = mRecordDate + df.format(h) + ":" + df.format(m) + ":" + df.format(s);
				
				if(fi.name.length() > 13 ){
					endStr = fi.name.substring(7, 13);
					end = Integer.parseInt(endStr);
				}
				//Log.d(TAG,"start:" + start + " end:" + end);
				if(endStr != null && end > start){
					int tl = end;
					int eh = tl/10000;
					int em = tl%10000;
					int es = em%100;
					em = em/100;
					
					tl = start;
					int sh = tl/10000;
					int sm = tl%10000;
					int ss = sm%100;
					sm = sm/100;
					
					int lh = eh - sh;
					int lm = em - sm;
					if(lm<0){
						lh -= 1;
						lm = em + 60 - sm;
					}
					int ls = es - ss;
					if(ls<0){
						lm -= 1;
						ls = es + 60 - ss;
					}
					//Log.d(TAG,fi.name + " " + lh + ":" + lm + ":" + ls);
					fi.duration = lh*3600 + lm*60 + ls;//df.format(lh) + ":" + df.format(lm) + ":" + df.format(ls);
					holder.fileinfo.setText(startDateTime + "  (" + df.format(lh) + ":" + df.format(lm) + ":" + df.format(ls) + "  " + (fi.size/(1048576)) + "M)");
				}else{
					holder.fileinfo.setText(startDateTime + "  (unkown  " + (fi.size/(1048576)) +"M)");
					fi.duration = 0;
				}
				fi.time = startDateTime;
				//Log.d(TAG,"start:" + startStr + " end:" + endStr);
			}
			
			holder.fileicon.setImageResource(R.drawable.file_icon_video);
		}
		return convertView;
	}
	public void addFileItem(FileItem fi){
		list.add(fi);
	}

	public void clearFileItem(){
		list.clear();
	}
	public FileItem getFileItem(int position){
		return list.get(position);
	}
	public ArrayList<FileItem> getFileItemList(){
		return list;
	}
	public void setFileItemList(ArrayList<FileItem> l){
		mRecordDate = null;
		list = l;
	}
	public void setRootDir(String rootdir){
		mSDRootDir = rootdir;
	}
    private class ViewHolder{
    	TextView filename;
    	ImageView fileicon;
    	TextView fileinfo;
    }
}
