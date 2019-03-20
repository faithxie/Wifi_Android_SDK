package com.exception.uncaughthandler;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

public class ExceptionHandler implements UncaughtExceptionHandler {
	private static final String TAG = "ExceptionHandler";

	private Context mContext;
	public ExceptionHandler(Context context) {
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		Log.e(TAG, "UncaughtException: " + throwable.getMessage());

		if (throwable != null) {
			StringBuilder sb = new StringBuilder();

			String temp = throwable.getMessage();
			if (temp != null) {
				sb.append(temp);
			}

			sb.append("\n\nName: ");
			sb.append(thread.getName());

			sb.append("\nTrace: \n");
			StackTraceElement[] elements = throwable.getStackTrace();
			if(elements != null) {
				for (StackTraceElement element : elements) {
					temp = element.toString();
					if(temp != null) {
						sb.append(temp);
					}
					sb.append("\n");
				}
			}

			/** if the exception was thrown in a background thread inside
			   AsyncTask, then the actual exception can be found with getCause */
			sb.append("\nCaused by: \n");
			Throwable theCause = throwable.getCause();
			if(theCause != null) {
				temp = theCause.toString();
				if(temp != null) {
					sb.append(temp);
				}
			}

			sb.append("\nCaused stack: \n");
			theCause = throwable.getCause();
			if(theCause != null) {
				elements = theCause.getStackTrace();
				if (elements != null) {
					for (StackTraceElement element : elements) {
						temp = element.toString();
						if (temp != null) {
							sb.append(temp);
						}
						sb.append("\n");
					}
				}
			}
			Log.e(TAG, sb.toString());

			Intent intent = new Intent(mContext, CranshReport.class);
			intent.putExtra(CranshReport.REPORT_CONTENT, sb.toString());
			mContext.startActivity(intent);

			Crash(mContext);
		}
	}

	public void uncaughtException(Exception e) {
		uncaughtException(Thread.currentThread(), e);
	}

	private boolean Crash(Context context) {
		if (context == null) {
			return false;
		}

		if (context instanceof Activity) {
			((Activity)context).finish();
		}

		Process.killProcess(Process.myPid());

		return true;
	}
}
