package freesbell.demo.utils;
/*
 * author:hujian
 * date:2015/12
 * */
import java.util.ArrayList;
import java.util.List;

import fenzhi.nativecaller.NativeCaller;

public abstract class ServiceStub {
	private final String TAG = "ServiceStub";
	private List<String> mUUIDList;
	private List<Integer> mCmdList;
	
	public ServiceStub(){
		mUUIDList = new ArrayList<String>();
		mCmdList = new ArrayList<Integer>();
	}
	
	public abstract void onMessageRecieve(String uuid,int msg,String str);
	public abstract void onMessageRecieve(String uuid,int msg,byte[] str);
	public void setRecievedMessage(String uuid,int msg,String str){
		if(uuid!=null){
			for(String id:mUUIDList){
				if(id.contentEquals(uuid)){
					for(int cmd:mCmdList){
						if(cmd == msg){
							onMessageRecieve(id,msg,str);
						}
					}
					break;
				}
			}
		}else{
			for(int cmd:mCmdList){
				if(cmd == msg){
					onMessageRecieve(null,msg,str);
				}
			}
		}
	}
	
	public void setRecievedMessage(String uuid,int msg,byte[] str){
		if(uuid!=null){
			for(String id:mUUIDList){
				if(id.contentEquals(uuid)){
					for(int cmd:mCmdList){
						if(cmd == msg){
							onMessageRecieve(id,msg,str);
						}
					}
					break;
				}
			}
		}else{
			for(int cmd:mCmdList){
				if(cmd == msg){
					onMessageRecieve(null,msg,str);
				}
			}
		}
	}
	
	public void setResponseTarget(String uuid,int msg){
		boolean isExist = false;
		for(String id:mUUIDList){
			if(id.contentEquals(uuid)){
				isExist = true;
				break;
			}
		}
		if(isExist == false)
			mUUIDList.add(uuid);
		
		for(int cmd:mCmdList){
			if(cmd == msg){
				return;
			}
		}
		mCmdList.add(msg);
	}
	
	public int sendMessage(String uuid,int msg,String str){
		for(String id:mUUIDList){
			if(id.contentEquals(uuid)){
				for(int cmd:mCmdList){
					if(cmd == msg+1){//response needs add 1 for each cmd/msg
						return sendMessageDirectly(uuid ,msg, str);
					}
				}
				//set cmd and send the message.
				mCmdList.add(msg+1);
				return sendMessageDirectly(uuid ,msg, str);
			}
		}
		mUUIDList.add(uuid);
		mCmdList.add(msg+1);
		return sendMessageDirectly(uuid ,msg, str);
	}
	public int sendMessage(String uuid,int msg,byte[] str){
		for(String id:mUUIDList){
			if(id.contentEquals(uuid)){
				for(int cmd:mCmdList){
					if(cmd == msg+1){//response needs add 1 for each cmd/msg
						return sendMessageDirectly(uuid ,msg, str);
					}
				}
				//set cmd and send the message.
				mCmdList.add(msg+1);
				return sendMessageDirectly(uuid ,msg, str);
			}
		}
		mUUIDList.add(uuid);
		mCmdList.add(msg+1);
		return sendMessageDirectly(uuid ,msg, str);
	}
	
	public int sendMessageDirectly(String uuid,int msg,String str){
		return NativeCaller.sendJsonCmd(uuid ,msg, str);
	}
	public int sendMessageDirectly(String uuid,int msg,byte[] data){
		return NativeCaller.P2PSendData(uuid, msg,data, data.length,0);
	}
}
