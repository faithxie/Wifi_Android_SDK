package freesbell.demo.bean;

public interface CallbackHandler {
//	public void CallBack(int cmd,Object op);
	public void updateAllCameraUI();
	public void updateCameraUI(String uuid,int option);
}
