package freesbell.demo.bean;

public class MailBean {
	private String svr = "";
	private String user = "";
	private int port;
	private String pwd = "";
	private int ssl;
	private String sender = "";
	private String receiver1 = "";
	private String receiver2 = "";
	private String receiver3 = "";
	private String receiver4 = "";
	private boolean isChecked;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getSvr() {
		return svr;
	}
	public void setSvr(String svr) {
		this.svr = svr;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getSsl() {
		return ssl;
	}
	public void setSsl(int ssl) {
		this.ssl = ssl;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver1() {
		return receiver1;
	}
	public void setReceiver1(String receiver1) {
		this.receiver1 = receiver1;
	}
	public String getReceiver2() {
		return receiver2;
	}
	public void setReceiver2(String receiver2) {
		this.receiver2 = receiver2;
	}
	public String getReceiver3() {
		return receiver3;
	}
	public void setReceiver3(String receiver3) {
		this.receiver3 = receiver3;
	}
	public String getReceiver4() {
		return receiver4;
	}
	public void setReceiver4(String receiver4) {
		this.receiver4 = receiver4;
	}
	@Override
	public String toString() {
		return "MailBean [svr=" + svr + ", user=" + user + ", port=" + port
				+ ", pwd=" + pwd + ", ssl=" + ssl + ", sender=" + sender
				+ ", receiver1=" + receiver1 + ", receiver2=" + receiver2
				+ ", receiver3=" + receiver3 + ", receiver4=" + receiver4 + "]";
	}
	
}
