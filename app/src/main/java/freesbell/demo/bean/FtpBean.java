package freesbell.demo.bean;

public class FtpBean {
	private String svr_ftp = "";
	private String user = "";
	private String pwd = "";
	private int port;
	private int mode;
	private int upload_interval;
	private String dir = "";
	
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getSvr_ftp() {
		return svr_ftp;
	}
	public void setSvr_ftp(String svr_ftp) {
		this.svr_ftp = svr_ftp;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getUpload_interval() {
		return upload_interval;
	}
	public void setUpload_interval(int upload_interval) {
		this.upload_interval = upload_interval;
	}
	@Override
	public String toString() {
		return "FtpBean [svr_ftp=" + svr_ftp + ", user=" + user + ", pwd="
				+ pwd + ", port=" + port + ", mode=" + mode
				+ ", upload_interval=" + upload_interval + ", dir=" + dir + "]";
	}
	
	
}
