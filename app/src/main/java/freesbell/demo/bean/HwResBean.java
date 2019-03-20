package freesbell.demo.bean;

import java.util.ArrayList;

import freesbell.demo.utils.Cmds;
import freesbell.demo.utils.ServiceStub;
import freesbell.demo.client.R;

public class HwResBean{
	
	public String name;
	public int mode;//0:out,1:in,2:adc,3:dac,4:pwm and so on.
	public int value;//value of adc or dac, or rate of pulse width for pwm.
	//0:low or disable,1:high or enable.
	public int trigger;//0:high,1:low,2:rise edge,3:fall edge.
	public int gate;
	public int index;
	public boolean insel = false,outsel = false;
//	public DeviceData mParent;
//	public ArrayList<SubdevBean> subdev = new ArrayList<SubdevBean>();
	public Object tag;
	public void opHw(ServiceStub stub,String uuid){
		switch(mode){
		case 0://out
		case 3://dac
		case 4://pwm
			if(value == 0){
				value = 1;
			}else if(value == 1){
				value = 0;
			}
			Cmds.setGPIO(stub, uuid, this);
			break;
		case 1://in
		case 2://adc
//			if(value == 0){
//			}else if(value == 1){
//			}
			Cmds.getGPIO(stub, uuid, index);
			break;
			default:;
		}
	}
}
