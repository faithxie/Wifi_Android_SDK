package freesbell.demo.utils;

import android.util.Log;
import freesbell.demo.bean.LiveVideoBean;

public class AccGyroSensorUtils {
	public final static String TAG = "AccGyroSensorUtils";
	private final static int ORIENT_X0 = 0;
	private final static int ORIENT_X180 = 1;
	private final static int ORIENT_Y0 = 0;
	private final static int ORIENT_Y180 = 1;
	private boolean mHorizontal = false;
	private boolean mVertical = false;
	private int mOrien;
//	private boolean mWorkMode = true;//true: self use, false: other use
	
	//please select current mode
	//self inside right hand
	private final int INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE = 1;
	//self inside left hand
	private final int INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE = 2;
	//self outside right hand
	private final int INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE = 3;
	//self outside left hand
	private final int INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE = 4;
	
	//other inside right hand
	private final int INNER_CAM_OTHER_INSIDE_RIGHT_HAND_MODE = 5;
	//other inside left hand
	private final int INNER_CAM_OTHER_INSIDE_LEFT_HAND_MODE = 6;
	//other outside	right hand
	private final int INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE = 7;
	//other outside	left hand
	private final int INNER_CAM_OTHER_OUTSIDE_LEFT_HAND_MODE = 8;
	private int mCurrentMode = -1;
	private int mDiffX = 0;
	private int mDiffY = 0;
	private int mDiffZ = 0;
	private int mPreX = 0;
	private int mPreY = 0;
	private int mPreZ = 0;
	
	private int mDiffSumX = 0;
	private int mDiffSumY = 0;
	private int mDiffSumZ = 0;
	
	private int mTimeoutCnt = 0;
	
	private final int ROTATE_ANGLE_SENSITIVITY_VALUE = 5000;
	
	public void updateAcc(int x,int y,int z)
	{
		int max_axis = 0;
		int axis_value = 0;
		if(Math.abs(x)>Math.abs(y)){
			if(Math.abs(x)>Math.abs(z)){
				max_axis = 1;
				axis_value = x;
			}else{
				max_axis = 3;
				axis_value = z;
			}
		}else{
			if(Math.abs(y)>Math.abs(z)){
				max_axis = 2;
				axis_value = y;
			}else{
				max_axis = 3;
				axis_value = z;
			}
		}

		if(mCurrentMode<0){
			if(Math.abs(axis_value)<500){
				return;
			}
			if(mSelfUseMode){//self mode
				Log.e(TAG, "self mode");
				switch(max_axis){
				case 1://x axis 
					if(x>0){
						Log.e(TAG, "---->  x:" + x);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE;
						//INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE
					}else{
						Log.e(TAG, "---->  x:" + x);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
						//INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE
					}
					break;
				case 2://y axis
					if(y>0){
						Log.e(TAG, "---->  y:" + y);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					}else{
						Log.e(TAG, "---->  y:" + y);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					}
					break;
				case 3://z axis
					if(z>0){
						Log.e(TAG, "---->  z:" + z);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					}else{
						Log.e(TAG, "---->  z:" + z);
						mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					}
					break;
				}
			}else{//other mode
				Log.e(TAG, "other mode");
				switch(max_axis){
				case 1://x axis 
					if(x>0){
						Log.e(TAG, "---->  x:" + x);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE;
						//mCurrentMode = INNER_CAM_OTHER_INSIDE_LEFT_HAND_MODE;
					}else{
						Log.e(TAG, "---->  x:" + x);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_LEFT_HAND_MODE;
						//mCurrentMode = INNER_CAM_OTHER_INSIDE_RIGHT_HAND_MODE;
					}
					break;
				case 2://y axis
					if(y>0){
						Log.e(TAG, "---->  y:" + y);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE;
					}else{
						Log.e(TAG, "---->  y:" + y);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE;
					}
					break;
				case 3://z axis
					if(z>0){
						Log.e(TAG, "---->  z:" + z);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE;
					}else{
						Log.e(TAG, "---->  z:" + z);
						mCurrentMode = INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE;
					}
					break;
				}
			}
		}
	}
	
	private boolean mSelfUseMode = true;
	public boolean getWorkMode(){
		return mSelfUseMode;
	}
	public void setWorkMode(boolean wm){
		mSelfUseMode = wm;
	}

	public boolean getInvertMode(String uuid,int gyr_x,int gyr_y,int gyr_z)
	{
		if(mSelfUseMode == false)//use for others
			return false;
		
		boolean invert_it = false;
		int max_axis;//1:x 2:y 3:z
		int axis_value;
		if(mPreX!=0){//it maybe 0,but it tend to be not 0 most of time.
			mDiffX = gyr_x - mPreX;
		}
		mPreX = gyr_x;
		mDiffSumX += mDiffX;
		if(mPreY!=0){//it maybe 0,but it tend to be not 0 most of time.
			mDiffY = gyr_y - mPreY;
		}
		mPreY = gyr_y;
		mDiffSumY += mDiffY;
		if(mPreZ!=0){//it maybe 0,but it tend to be not 0 most of time.
			mDiffZ = gyr_z - mPreZ;
		}
		mPreZ = gyr_z;
		mDiffSumZ += mDiffZ;
		Log.e(TAG, "mDiffX:" + mDiffX + " mDiffY:" + mDiffY + "  mDiffZ:" + mDiffZ);
		Log.w(TAG, "mDiffSumX:" + mDiffSumX + " mDiffSumY:" + mDiffSumY + "  mDiffSumZ:" + mDiffSumZ);
		
		if(Math.abs(mDiffX)>Math.abs(mDiffY)){
			if(Math.abs(mDiffX)>Math.abs(mDiffZ)){
				max_axis = 1;
				axis_value = mDiffX;
			}else{
				max_axis = 3;
				axis_value = mDiffZ;
			}
		}else{
			if(Math.abs(mDiffY)>Math.abs(mDiffZ)){
				max_axis = 2;
				axis_value = mDiffY;
			}else{
				max_axis = 3;
				axis_value = mDiffZ;
			}
		}
		if(Math.abs(axis_value)<300){
			mTimeoutCnt++;
			Log.e(TAG, "no rotate");
			if(mTimeoutCnt>3){
				mDiffSumX = 0;
				mDiffSumY = 0;
				mDiffSumZ = 0;
			}
//			return mWorkMode;
		}else{
			mDiffSumX=0;
		}
		
		switch(mCurrentMode){
		//self inside right hand
		case INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				if(mDiffSumY>ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					invert_it = true;
				}
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				if(mDiffSumZ>ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE;
				}
				break;
			}
		}break;
		//self inside left hand
		case INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				if(mDiffSumY>ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE;
					invert_it = true;
				}else{
					
				}
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				if(mDiffSumZ<-ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE;
					invert_it = false;
				}
				break;
			}
		}break;
		//self outside right hand
		case INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				if(mDiffSumY<-ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_INSIDE_RIGHT_HAND_MODE;
					invert_it = false;
				}
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				if(mDiffSumZ<-ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE;
					invert_it = false;
				}
				break;
			}
		}break;
		//self outside left hand
		case INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_SELF_OUTSIDE_LEFT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				if(mDiffSumY<-ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_INSIDE_LEFT_HAND_MODE;
					invert_it = false;
				}else{
					
				}
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				if(mDiffSumZ>ROTATE_ANGLE_SENSITIVITY_VALUE){
					mCurrentMode = INNER_CAM_SELF_OUTSIDE_RIGHT_HAND_MODE;
					invert_it = false;
				}
				break;
			}
		}break;
		//---------------------------------------------------------------------------------------------------
		//other inside right hand
		case INNER_CAM_OTHER_INSIDE_RIGHT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_OTHER_INSIDE_RIGHT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				break;
			}
		}break;
		//other inside left hand
		case INNER_CAM_OTHER_INSIDE_LEFT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_OTHER_INSIDE_LEFT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				break;
			}
		}break;
		//other outside	right hand
		case INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_OTHER_OUTSIDE_RIGHT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				break;
			}
		}break;
		//other outside	left hand
		case INNER_CAM_OTHER_OUTSIDE_LEFT_HAND_MODE:{
			Log.e(TAG, "INNER_CAM_OTHER_OUTSIDE_LEFT_HAND_MODE");
			switch(max_axis){
			case 1://x axis rotate
				Log.e(TAG, "rotate x:" + mDiffX + " mDiffSumX:" + mDiffSumX);
				break;
			case 2://y axis rotate
				Log.e(TAG, "rotate y:" + mDiffY + " mDiffSumY:" + mDiffSumY);
				break;
			case 3://z axis rotate
				Log.e(TAG, "rotate z:" + mDiffZ + " mDiffSumZ:" + mDiffSumZ);
				break;
			}
		}break;
		}
		return invert_it;
	}
	
	private float mCurrentAngle = 0;
	private boolean mRealTimeAngle = false;
	private float angle = 0;
	public float getRotateAngle(String uuid,int x,int y,int z){
		if(mRealTimeAngle == false){
			if(x>3000){
				angle = -180;
			}else if(Math.abs(x)<2000){
				if(Math.abs(z)>2000){
					return angle;
				}
				angle = -90;
			}else if(x<-3000){
				angle = 0;
			}
			return angle;
		}
		
		updateAcc(x, y, z);
		
		int sensitive = 100;
		int tmp = x/sensitive;
		x = tmp*sensitive;
		
		tmp = y/sensitive;
		y = tmp*sensitive;
		
		tmp = z/sensitive;
		z = tmp*sensitive;
		
		if(x==0){
			x=1;
		}
		if(y==0){
			y=1;
		}
		
		float modeangle = 0;
		if(mSelfUseMode){
			modeangle = 180;
		}
		
		double angle = Math.toDegrees(Math.atan((double)x/y));

		if(x<0){
			if(y>0){
				mCurrentAngle = (float) (modeangle + 90 + angle);//ok
			}else{
				mCurrentAngle = (float) (modeangle - 90 + angle);//ok
			}
		}else{
			if(y>0){
				mCurrentAngle = (float) (modeangle  + 90 + angle);
			}else{
				mCurrentAngle = (float) (modeangle + 270+angle);
			}
		}
		
		Log.d(TAG, "angle:" + angle);
		return mCurrentAngle;
	}
}
