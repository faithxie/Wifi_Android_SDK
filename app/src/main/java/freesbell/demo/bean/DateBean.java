package freesbell.demo.bean;

public class DateBean {
	public int now;
	public int timeZone;
	public boolean ntpEnable;
	public String ntpSer;
	public int year,mon,day,hour,min,sec;
	public int getNow() {
		return now;
	}
	public void setNow(int now) {
		this.now = now;
	}
//	public int getTz() {
//		return tz;
//	}
//	public void setTz(int tz) {
//		this.tz = tz;
//	}
//	public int getNtp_enable() {
//		return ntp_enable;
//	}
//	public void setNtp_enable(int ntp_enable) {
//		this.ntp_enable = ntp_enable;
//	}
	public String getNtp_ser() {
		return ntpSer;
	}
	public void setNtp_ser(String ntp_ser) {
		this.ntpSer = ntp_ser;
	}
	@Override
	public String toString() {
		return "DateBean [now=" + now + ", tz=" + timeZone + ", ntp_enable="
				+ ntpEnable + ", ntp_ser=" + ntpSer + "]";
	}
	
}
