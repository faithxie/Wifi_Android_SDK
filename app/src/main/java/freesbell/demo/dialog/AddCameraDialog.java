package freesbell.demo.dialog;

import freesbell.demo.client.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import freesbell.demo.adapter.SearchListAdapter;

public class AddCameraDialog extends Dialog{

	public AddCameraDialog(Context context,SearchListAdapter adapter,int width,int height) {
		super(context);
		this.getContext().setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		super.setContentView(R.layout.addcameradialog);
		Button btnRefresh=(Button)findViewById(R.id.btnrefresh);
		ListView listview=(ListView)findViewById(R.id.listView);
		
		
		
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.dialog_layout);
		RelativeLayout.LayoutParams lpp=new RelativeLayout.LayoutParams(width*2/3,100);
		
		
		Window window=this.getWindow();
		window.setGravity(Gravity.CENTER);
		android.view.WindowManager.LayoutParams lp=window.getAttributes();
		window.setLayout(width,height);
		
	}

}
