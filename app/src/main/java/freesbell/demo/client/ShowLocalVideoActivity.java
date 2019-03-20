package freesbell.demo.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import fenzhi.nativecaller.NativeCaller;
import freesbell.demo.content.ContentCommon;
import freesbell.demo.utils.MyRender;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �ۿ�����¼��
 * */
public class ShowLocalVideoActivity extends BaseActivity implements
		OnClickListener, OnGestureListener, OnDoubleTapListener,
		OnTouchListener {
	private final static String TAG = "ShowLocalVideoActivity";
//	private DatabaseUtil mDbUtil;
	private TextView mTv_Prompt;
	private String filePath = null;
	private String videoTime;
	private String strDID = null;
	private ImageView img;//
	private ImageView imgPause;//
	private String strCameraName;
	private Button btnBack;
	private TextView tvTitle;
	private TextView tvTime;
	private TextView showVideoTime;
	private GestureDetector gt = new GestureDetector(this);
	private boolean isPlaying = false;// ������ͣ�ı�־
	private boolean flag = true;// �ر��̵߳ı�־
	private RelativeLayout topLayout;
	private RelativeLayout bottomLayout;
	private float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
	private boolean isShowing = false;
	private boolean isStart = true;
	private int videoSumTime;//
	private int progress = 0;//
	private int frameCout = 0;
	private int sum;// �ܹ��ж���֡
	private ProgressBar seekBar;
	private TextView tvSumTime;
	private TextView tvCurrentTime;
	private Button btnPlay;
	private Button btnLeft;
	private Button btnRight;
	private Button btnDelete;
	private boolean isPause = false;
	private int sumTime;
//	private ArrayList<Map<String, Object>> arrayList;
	private boolean isView = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				isShowing = false;
				topLayout.setVisibility(View.GONE);
				bottomLayout.setVisibility(View.GONE);
				break;
			case 2:
				seekBar.setMax(videoSumTime);
				tvSumTime.setText(getTime(videoSumTime / 1000));
				startVideo();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDataFromOther();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		mDbUtil = new DatabaseUtil(this);
		setContentView(R.layout.showlocalvideo_activity);
		findView();
		setListener();
		tvTime.setText(getResources().getString(R.string.local_video_date)
				+ getContent(filePath));
		firstPicture();
		mHandler.sendEmptyMessageDelayed(1, 3000);
	}

	private void setListener() {
		btnPlay.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		img.setOnTouchListener(this);
		topLayout.setOnTouchListener(this);
		bottomLayout.setOnTouchListener(this);
	}

	private String mess;

	private void getDataFromOther() {
		Intent intent = getIntent();
		strDID = intent.getStringExtra("did");
		filePath = intent.getStringExtra("filepath");
		strCameraName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
//		arrayList = (ArrayList<Map<String, Object>>) intent
//				.getSerializableExtra("arrayList");
//		position = intent.getIntExtra("position", 0);
//		mess = intent.getStringExtra("videotime");
		Log.i(TAG, "mess:"+mess);
		videoTime = "..";//mess(mess);

//		Log.d(TAG, "strDID:" + strDID+"arrayList:"+arrayList+"==videotime:"+videoTime+"----filePath:"+filePath);
	}

	private void findView() {
		wh = getWindowManager().getDefaultDisplay().getWidth();
		ht = getWindowManager().getDefaultDisplay().getHeight();
		btnBack = (Button) findViewById(R.id.back);
		btnBack.setOnClickListener(this);
		
//		btnDelete = (Button) findViewById(R.id.btn_delete);
//		btnDelete.setOnClickListener(this);
		
		tvTitle = (TextView) findViewById(R.id.takevideo_title);
		showVideoTime = (TextView) findViewById(R.id.showvideotime);//������ʾʱ��
		showVideoTime.setText("VideoTime");
		tvTitle.setText(strCameraName + " "
				+ getResources().getString(R.string.main_phone)
		/* + getResources().getString(R.string.main_vid) */);
		tvTime = (TextView) findViewById(R.id.takevideo_time);
		img = (ImageView) findViewById(R.id.img_playvideo);
		imgPause = (ImageView) findViewById(R.id.img_pause);
		topLayout = (RelativeLayout) findViewById(R.id.top);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottom);
		seekBar = (ProgressBar) findViewById(R.id.progressBar1);
		tvSumTime = (TextView) findViewById(R.id.sumtime);
		tvCurrentTime = (TextView) findViewById(R.id.currenttime);
		btnPlay = (Button) findViewById(R.id.btn_play);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);

		myGLSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
		myRender = new MyRender(myGLSurfaceView);
		myGLSurfaceView.setRenderer(myRender);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh,
					wh * 3 / 4);
			lp.gravity = Gravity.CENTER;
			myGLSurfaceView.setLayoutParams(lp);
			img.setLayoutParams(lp);
			tvTime.setVisibility(View.GONE);
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			tvTime.setVisibility(View.VISIBLE);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh, ht);
			lp.gravity = Gravity.CENTER;
			img.setLayoutParams(lp);
			myGLSurfaceView.setLayoutParams(lp);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		wh = getWindowManager().getDefaultDisplay().getWidth();
		ht = getWindowManager().getDefaultDisplay().getHeight();
		if (getResources().getConfiguration().orientation == newConfig.ORIENTATION_PORTRAIT) {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh,
					wh * 3 / 4);
			lp.gravity = Gravity.CENTER;
			myGLSurfaceView.setLayoutParams(lp);
			img.setLayoutParams(lp);
			// tvTime.setVisibility(View.GONE);
		} else if (getResources().getConfiguration().orientation == newConfig.ORIENTATION_LANDSCAPE) {
			// tvTime.setVisibility(View.VISIBLE);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh, ht);
			lp.gravity = Gravity.CENTER;
			img.setLayoutParams(lp);
			myGLSurfaceView.setLayoutParams(lp);
		}
	}

	private String getContent(String filePath) {
		Log.d(TAG, "filePath:" + filePath);
		String s = filePath.substring(filePath.lastIndexOf("/") + 1);
		String date = s.substring(0, 10);
		String time = s.substring(11, 16).replace("_", ":");
		String result = time;
		Log.d(TAG, "result:" + result);
		Log.d(TAG, "sss:" + s.substring(0, 16));
		return result;
	}

	private String getTime(int time) {
		int second = time % 60;
		int minute = time / 60;
		int hour = minute / 60;
		String strSecond = "";
		String strMinute = "";
		String strHour = "";
		if (second < 10) {
			strSecond = "0" + second;
		} else {
			strSecond = String.valueOf(second);
		}
		if (minute < 10) {
			strMinute = "0" + minute;
		} else {
			strMinute = String.valueOf(minute);
		}
		if (hour < 10) {
			strHour = "0" + hour;
		} else {
			strHour = String.valueOf(hour);
		}

		return strHour + ":" + strMinute + ":" + strSecond;
	}

	/**
	 * ������Ƶ���߳�
	 * */
	private class PlayThread extends Thread {
		public void run() {
			if (filePath != null) {
				File file = new File(filePath);
				FileInputStream in = null;
				try {
					in = new FileInputStream(file);
					Log.d(TAG, "file available:" + in.available());
					byte[] header = new byte[4];
					in.read(header);
					int fType = byteToInt(header);
					Log.d(TAG, "fType:" + fType);
					frameCout = 0;
					sumTime = 0;
					flag = true;
					mProgressHandler.postDelayed(myProRunnable, 0);
					while (in.available() != 0 && flag) {

						synchronized (ShowLocalVideoActivity.this) {
							Log.d(TAG, "flag=" + flag + " isPlaying:" + isPlaying);

							if (isPlaying) {
								frameCout++;
								Log.d(TAG, "frameCout:" + frameCout);
								switch (fType) {
								case 1: {// yuv
									long startTiem = (new Date()).getTime();
									byte[] sizebyte = new byte[4];
									byte[] typebyte = new byte[4];
									byte[] timebyte = new byte[4];
									in.read(sizebyte);
									in.read(typebyte);
									in.read(timebyte);
									int length = byteToInt(sizebyte);
									Log.d(TAG, "length:" + length);
									if (length == 0) {
//										if (length == 0) {//
//											Log.d(TAG, "length = 0");
											flag = false;
											isStart = true;
											isPlaying = false;
											mProgressHandler.sendEmptyMessage(2);
											return;
//										}
									}
									int bIFrame = byteToInt(typebyte);
									int time = byteToInt(timebyte);
									byte[] h264byte = new byte[length];
									in.read(h264byte);
									byte[] yuvbuff = new byte[720 * 1280 * 3 / 2];
									int[] wAndh = new int[2];
									int result = NativeCaller
											.DecodeH264Frame(h264byte, 1,
													yuvbuff, length, wAndh);

									if (result > 0) {
										int width = wAndh[0];
										int height = wAndh[1];
										myRender.writeSample(yuvbuff, width,
												height);
										int comsumeTime = (int) ((new Date()
												.getTime() - startTiem));
										int sleepTime = time - comsumeTime;
										if (sleepTime > 0) {
											sumTime += comsumeTime;
											int count = sleepTime / 10;
											int remainTime = sleepTime % 10;
											for (int i = 0; i < count; i++) {
												sumTime += 10;
												Thread.sleep(10);
											}
											sumTime += remainTime;
											Thread.sleep(remainTime);
										} else {
											sumTime += time;
										}
									}
								}
									break;
								case 2: {
									long startTiem = (new Date()).getTime();
									byte[] lengthBytes = new byte[4];
									in.read(lengthBytes);// ��ͼƬ�ĳ���
									int length = byteToInt(lengthBytes);
									if (length == 0) {// �������
										Log.d(TAG, "���Ž���");
										flag = false;
										isStart = true;
										isPlaying = false;
										mProgressHandler.sendEmptyMessage(2);
										return;
									}
									byte[] timeBytes = new byte[4];
									in.read(timeBytes);// ��ͼƬ��ʱ���
									int time = byteToInt(timeBytes);
									Log.d(TAG, "������֡ʱ���time:" + time);
									byte[] contentBytes = new byte[length];
									in.read(contentBytes);
									Bitmap bmp = BitmapFactory.decodeByteArray(
											contentBytes, 0,
											contentBytes.length);

									if (bmp != null) {
										Message message = mPlayHandler
												.obtainMessage();
										message.obj = bmp;
										mPlayHandler.sendMessage(message);
									}
									int comsumeTime = (int) ((new Date()
											.getTime() - startTiem));
									int sleepTime = time - comsumeTime;
									if (sleepTime > 0) {
										sumTime += comsumeTime;
										int count = sleepTime / 10;
										int remainTime = sleepTime % 10;
										for (int i = 0; i < count; i++) {
											sumTime += 10;
											Thread.sleep(10);
										}
										sumTime += remainTime;
										Thread.sleep(remainTime);
									} else {
										sumTime += time;
									}

								}
								default:
									break;
								}
							} else {
								Log.d(TAG, "wait 1");
								isPause = true;
								ShowLocalVideoActivity.this.wait();
								Log.d(TAG, "wait 2");
								isPause = false;
								mProgressHandler.postDelayed(myProRunnable, 0);
							}
						}
					}
					Log.d(TAG, "�����߳̽���");
				} catch (Exception e) {
					Log.d(TAG, "�����쳣��" + e.getMessage());
				} finally {
					if (in != null) {
						try {
							in.close();
							in = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	};

	/**
	 * ����¼��
	 * */
	private void pVideo() {
		synchronized (ShowLocalVideoActivity.this) {
			ShowLocalVideoActivity.this.notifyAll();
		}
	}

	/**
	 * ¼��ĵ�һ��֡
	 * **/
	private void firstPicture() {
		if (filePath != null) {
			new Thread() {
				public void run() {
					File file = new File(filePath);
					FileInputStream in = null;
					try {

						in = new FileInputStream(file);
						int fileSumLength = in.available();
						// ��¼����ļ�ͷ
						byte[] header = new byte[4];
						int read = in.read(header);
						int fType = byteToInt(header);
						Log.d(TAG, "fType:" + fType);
						switch (fType) {
						case 1: {// h264
							in.skip(fileSumLength - 8);
							byte[] sumB = new byte[4];
							in.read(sumB);
							videoSumTime = byteToInt(sumB);
							Log.d(TAG, "videoSumTime:" + videoSumTime);
							mHandler.sendEmptyMessage(2);
						}
							isView = true;
							break;
						case 2: {// jpg

							isView = false;
							byte[] lengthBytes = new byte[4];
							byte[] timeBytes = new byte[4];
							in.read(lengthBytes);
							in.read(timeBytes);
							int length = byteToInt(lengthBytes);
							int time = byteToInt(timeBytes);

							byte[] contentBytes = new byte[length];
							in.read(contentBytes);
							Bitmap bmp = BitmapFactory.decodeByteArray(
									contentBytes, 0, contentBytes.length);
							Message message = mPlayHandler.obtainMessage();
							message.obj = bmp;
							mPlayHandler.sendMessage(message);

							// ��ȡ¼���ļ�����ʱ��
							int need = fileSumLength - (length + 16);
							in.skip(need);
							byte[] sumB = new byte[4];
							in.read(sumB);

							// test
							// sum = byteToInt(sumB);
							// int remaider=sum%5;
							// if(remaider>0){
							// videoSumTime=sum/5+1;
							// }else{
							// videoSumTime = sum/5;
							// }
							// test
							videoSumTime = byteToInt(sumB);
							Log.d(TAG, "������ʱ��  videoSumTime:" + videoSumTime);
							mHandler.sendEmptyMessage(2);
						}
						default:
							break;
						}
					} catch (Exception e) {
						Log.d(TAG, "��ȡ��һ֡�쳣��" + e.getMessage());
					} finally {
						if (in != null) {
							try {
								in.close();
								in = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}.start();
		}
	}

	/**
	 * ���²��Ž����handler
	 * */
	private Handler mPlayHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isView = false) {
				myGLSurfaceView.setVisibility(View.GONE);
			}
			Bitmap bmp = (Bitmap) msg.obj;
			if (bmp == null) {
				Log.d(TAG, "play this picture failed");
				return;
			}
			img.setImageBitmap(bmp);
		}
	};

	private String mess(String mess) {
		String d = mess.substring(0, 10);
		String dd = mess.substring(10, 11);
		String de = mess.substring(11, 19);
		String dee = de.replace(dd, ":");
		String ddee = d + " " + dee;
		return ddee;
	}

	private int secs, mins, hours, days, months, years;

//	private String showTime(int time) {
//		String year = mess.substring(0, 4);
//		String month = mess.substring(5, 7);
//		String day = mess.substring(8, 10);
//		String hour = mess.substring(11, 13);
//		String min = mess.substring(14, 16);
//		String sec = mess.substring(17, 19);
//		String get = getTime(time);
//		int second = time % 60;
//		// int minute = time / 60;
//		// int hourss = minute / 60;
//		secs = Integer.valueOf(sec) + Integer.valueOf(second);
//		mins = Integer.valueOf(min);
//		hours = Integer.valueOf(hour);
//		days = Integer.valueOf(day);
//		months = Integer.valueOf(month);
//		years = Integer.valueOf(year);
//		if (secs == 60) {
//			secs = 0;
//			mins += 1;
//			if (mins == 60) {
//				mins = 0;
//				hours += 1;
//				if (hours == 24) {
//					hours = 0;
//				}
//			}
//		}
//
//		String sss = null;
//		String mmm = null;
//		String hhh = null;
//		if (secs < 10) {
//			sss = "0" + secs;
//		} else {
//			sss = String.valueOf(secs);
//		}
//		if (mins < 10) {
//			mmm = "0" + mins;
//		} else {
//			mmm = String.valueOf(mins);
//		}
//		if (hours < 10) {
//			hhh = "0" + hours;
//		} else {
//			hhh = String.valueOf(hours);
//		}
//
//		return year + "-" + month + "-" + day + " " + hhh + ":" + mmm + ":"
//				+ sss;
//	}

	/***
	 * ��ʾ���Ž������handler
	 * **/
	private Handler mProgressHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				seekBar.setProgress(progress);
				System.out.println(">>>>>>>>>>>>>>>>>>>" + progress);
				tvCurrentTime.setText(getTime(progress));
				showVideoTime.setText("progress:" + progress);
				System.out.println(">>>>>>>>>>>>>>>>" + getTime(progress));
				break;
			case 2:
				imgPause.setVisibility(View.GONE);
				btnPlay.setBackgroundResource(R.drawable.video_play_pause_selector);
				finish();
				break;
			default:
				break;
			}

		}
	};
	private int position;
	private PlayThread playThread;

	private Runnable myProRunnable = new Runnable() {

		@Override
		public void run() {
			if (seekBar.getProgress() != seekBar.getMax()) {
				seekBar.setProgress(sumTime);
				tvCurrentTime.setText(getTime(sumTime / 1000));
				showVideoTime.setText("progress:" + progress);
				mProgressHandler.postDelayed(myProRunnable, 300);
				// mProgressHandler.sendEmptyMessage(1);
			}
		}

	};
	private GLSurfaceView myGLSurfaceView;
	private MyRender myRender;
	private int ht;
	private int wh;

	public static byte[] intToByte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();// �����λ���������λ
			temp = temp >> 8;// ������8λ
		}
		return b;
	}

	public static int byteToInt(byte[] b) {
		int s = 0;
		int s0 = b[0] & 0xff;// ���λ
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}

	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();// �����λ���������λ
			temp = temp >> 8;// ������8λ
		}
		return b;

	}

	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// ���λ
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// ���λ
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff; // s0����
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

//	private void showDeletDialog(final int position) {
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		dialog.setMessage(R.string.del_alert);
//		dialog.setPositiveButton(R.string.str_ok,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						int tempposition = position;
////						mDbUtil.open();
//						Log.d(TAG, "arrayList.size:" + arrayList.size());
//						Map<String, Object> map = arrayList.get(position);
//						String filePath = (String) map.get("path");
//						if (mDbUtil.deleteVideoOrPicture(strDID, filePath,
//								DatabaseUtil.TYPE_VIDEO)) {
//							File file = new File(filePath);
//							if (file != null) {
//								file.delete();
//								if (1 == arrayList.size()) {
//									// mTv_TakeTime.setVisibility(View.INVISIBLE);
//									// mTv_Sum.setText(desc+"0");
//									map.clear();
//									arrayList.remove(position);
////									mAdapter.notifyDataSetChanged();
//								} else if (position == arrayList.size() - 1) {
//									Map<String, Object> map2 = arrayList
//											.get(position - 1);
//									String path = (String) map2.get("path");
//									String content = getContent(path);
//									// mTv_TakeTime.setText(content);
//									map.clear();
//									arrayList.remove(position);
//									// mTv_Sum.setText(desc+arrayList.size()+"/"+tempposition);
////									mAdapter.notifyDataSetChanged();
//								} else {
//									Map<String, Object> map2 = arrayList
//											.get(position + 1);
//									String path = (String) map2.get("path");
//									String content = getContent(path);
//									// mTv_TakeTime.setText(content);
//									tempposition += 1;
//									map.clear();
//									arrayList.remove(position);
//									// mTv_Sum.setText(desc+arrayList.size()+"/"+tempposition);
//									// mAdapter.notifyDataSetChanged();
//								}
//							}
//						}
//
//						if (arrayList.size() == 0) {
//							// tvNoPics.setVisibility(View.VISIBLE);
//							isShowing = true;
//							topLayout.setVisibility(View.VISIBLE);
//							bottomLayout.setVisibility(View.VISIBLE);
//						}
//						
//						mDbUtil.close();
//					}
//				});
//		dialog.setNegativeButton(R.string.str_cancel,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//
//					}
//				});
//		dialog.show();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_delete:
//			showDeletDialog(position);
//			Intent data=new Intent();
//			data.putExtra("list", arrayList);
//			setResult(2, data);
//			finish();
//			break;
		case R.id.back:
			flag = false;
			finish();
			break;
		case R.id.btn_left:
			position--;
			if (position >= 0) {
//				flag = false;
//				Map<String, Object> map = arrayList.get(position);
//				filePath = (String) map.get("path");
//				tvTime.setText(getResources().getString(
//						R.string.local_video_date)
//						+ getContent(filePath));
//				firstPicture();
//				startVideo();
			} else {
				position++;
				showToast(R.string.playvideo_no_next);
			}
			break;
		case R.id.btn_right:
//			position++;
//			if (position < arrayList.size()) {
//				flag = false;
//				isStart = true;
//				Map<String, Object> map = arrayList.get(position);
//				filePath = (String) map.get("path");
//				tvTime.setText(getResources().getString(
//						R.string.local_video_date)
//						+ getContent(filePath));
//				firstPicture();
//				startVideo();
//			} else {
//				position--;
//				showToast(R.string.playvideo_no_nextone);
//			}
			break;
		case R.id.btn_play:
			if (isPlaying) {
				btnPlay.setBackgroundResource(R.drawable.video_play_pause_selector);
				imgPause.setVisibility(View.GONE);
				Log.d(TAG, "pause");
				isPlaying = false;
			} else {
				btnPlay.setBackgroundResource(R.drawable.video_play_play_selector);
				imgPause.setVisibility(View.GONE);
				if (isStart) {// ��ʼ����¼��
					startVideo();
				} else {
					isPlaying = true;
					pVideo();
				}
			}
			break;
		default:
			break;
		}

	}

	private void startVideo() {
		Log.d(TAG, "startVideo()");
		imgPause.setVisibility(View.GONE);
		isStart = false;
		isPlaying = true;
		progress = 0;
		seekBar.setProgress(progress);
		tvCurrentTime.setText(getTime(progress));
		playThread = new PlayThread();
		playThread.start();
	}

	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		if (arg0 == KeyEvent.KEYCODE_BACK) {
			flag = false;
//			Intent data=new Intent();
//			data.putExtra("list", arrayList);
//			setResult(2, data);
			finish();
			return true;
		}
		return super.onKeyDown(arg0, arg1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gt.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		if (isShowing) {
			isShowing = false;
			topLayout.setVisibility(View.GONE);
			bottomLayout.setVisibility(View.GONE);
		} else {
			isShowing = true;
			topLayout.setVisibility(View.VISIBLE);
			bottomLayout.setVisibility(View.VISIBLE);
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Log.d(TAG, "onDoubleTap");

		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		Log.d(TAG, "onSingleTapConfirmed");

		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.top:
			return true;
		case R.id.bottom:
			return true;
		case R.id.img_playvideo:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (isShowing) {
					isShowing = false;
					topLayout.setVisibility(View.GONE);
					bottomLayout.setVisibility(View.GONE);
				} else {
					isShowing = true;
					topLayout.setVisibility(View.VISIBLE);
					bottomLayout.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}

			return true;
		default:
			break;
		}

		return false;
	}
}