package freesbell.demo.adapter;

import java.util.ArrayList;

import freesbell.demo.client.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import freesbell.demo.content.ContentCommon;
/**
 * 
 * @author �޸Ĺ��� ding fangchao
 *
 */
public class MainSettingAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<String> list;
	private String[] ss;
	private int[]  imgs;
    public MainSettingAdapter(Context context,int status){
    	this.context=context;
    	inflater = LayoutInflater.from(context);
    	list=new ArrayList<String>();
    	Resources resources = context.getResources();
    	if(status==ContentCommon.P2P_STATUS_ON_LINE){
    		ss=new String[3];
    		ss[0]=resources.getString( R.string.setting);
    		//ss[1]=resources.getString( R.string.edit);
//    		ss[1]=resources.getString( R.string.check_localpic);
//    		ss[1]=resources.getString(R.string.loca_picorvedio);
    		ss[1] = resources.getString(R.string.remote_video_title);
//    		ss[2]=resources.getString(R.string.alerm_log);
    		ss[2]=resources.getString( R.string.delete_camera);
    		imgs=new int[3];
    		imgs[0]=R.drawable.ic_setting_camera;
    		//imgs[1]=R.drawable.ic_edit_camera;
//    		imgs[1]=R.drawable.ic_menu_album_inverse;
//    		imgs[1]=R.drawable.ic_menu_checkvideo;
    		imgs[1]=R.drawable.ic_menu_checkvideo;//R.drawable.remotetfcard;
//    		imgs[2]=R.drawable.ic_menu_event;
    		imgs[2]=R.drawable.ic_delete_camera;
    		
    	}else{
    		ss=new String[2];
    		//ss[0]=resources.getString( R.string.edit);
    		ss[0]=resources.getString( R.string.setting);
//    		ss[1]=resources.getString( R.string.loca_picorvedio);
//    		ss[2]=resources.getString(R.string.check_localvid);
//    		ss[2]=resources.getString(R.string.alerm_log);
    		ss[1]=resources.getString( R.string.delete_camera);
    		imgs=new int[2];
    		//imgs[0]=R.drawable.ic_edit_camera;
    		imgs[0]=R.drawable.ic_setting_camera;
//    		imgs[1]=R.drawable.ic_menu_album_inverse;
//    		imgs[1]=R.drawable.ic_menu_checkvideo;
//    		imgs[2]=R.drawable.ic_menu_event;
    		imgs[1]=R.drawable.ic_delete_camera;
    	}
    	
    }
	@Override
	public int getCount() {
		return ss.length;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.mainsetting_listitem, null);
			holder=new ViewHolder();
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.tv=(TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		if(ss.length==0){
			convertView.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
		}else if(position==0){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
		}else if(position==ss.length-1){
			convertView.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
		}else{
			convertView.setBackgroundResource(R.drawable.listitem_pressed_selector);
		}
		holder.tv.setText(ss[position]);
		holder.img.setBackgroundResource(imgs[position]);
		
		return convertView;
	}
    private class ViewHolder{
    	TextView tv;
    	ImageView img;
    }
}
