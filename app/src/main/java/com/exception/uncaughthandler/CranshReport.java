package com.exception.uncaughthandler;

import freesbell.demo.client.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class CranshReport extends Activity {
	public static final String REPORT_CONTENT = "content";

	protected StringBuilder reportTitle;
	protected StringBuilder reportContent;
	private TextView reportTextview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.setContentView(R.layout.sc_cransh_report);
		String bugContent = getIntent().getStringExtra(REPORT_CONTENT);
		BuildContent(bugContent);

		reportTextview = (TextView)this.findViewById(R.id.crash_report);
		reportTextview.setMovementMethod(ScrollingMovementMethod.getInstance());
		reportTextview.setText(reportContent.toString());
	}

	private void BuildContent(String bugContent) {
		reportContent = new StringBuilder();
		reportContent.append("Model: ").append(Build.MODEL).append("\n");
		reportContent.append("Device: ").append(Build.DEVICE).append("\n");
		reportContent.append("Product: ").append(Build.PRODUCT).append("\n");
		reportContent.append("Manufacturer :").append(Build.MANUFACTURER).append("\n");
		reportContent.append("Version: ").append(Build.VERSION.RELEASE).append("\n");
		reportContent.append(bugContent);
	}

	public void Cancel(View view) {
		if(view.getId() == R.id.cancel) {
			finish();
		}
	}
}
